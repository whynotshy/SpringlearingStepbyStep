package com.yc.springframework.context;

import com.yc.springframework.stereotype.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @program: testspring
 * @description:
 * @author: LIN
 * @create: 2021~04~05 11:51
 */
public class MyAnnotationConfigApplicationContext implements MyApplicationContext{
    private Map<String,Object> beanMap=new HashMap<String, Object>();

    public MyAnnotationConfigApplicationContext(Class<?>... componentClasses) {
        try {
            register(componentClasses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register(Class<?>[] componentClasses) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, ClassNotFoundException {
        if (componentClasses==null){
            throw  new RuntimeException("没有指定配置类");
        }
        for (Class cl :componentClasses) {
            //请实现这个里面的代码
            //源码一:      只实现IOC，MyPostConstruct  myPreDestory
            if (!cl.isAnnotationPresent(MyConfiguration.class)){
                continue;
            }
            String[] basePackages=getAppConfigBasePackages(cl);
            if (cl.isAnnotationPresent(MyComponentScan.class)){
                MyComponentScan mcs= (MyComponentScan) cl.getAnnotation(MyComponentScan.class);
                if (mcs.basePackages()!=null&&mcs.basePackages().length>0){
                    basePackages=mcs.basePackages();
                }
            }
            //处理@MyBean的情况
            Object obj=cl.newInstance();
            handleAtMyBean(cl,obj);

            //处理        basePackages  基础包下的所有的托管bean
            for (String basePackage:basePackages) {
                System.out.println("扫描包路径"+basePackage);
                scanPackageAndSubPackageClasses(basePackage);
            }

            //继续处理其他的托管bean
            handleManageBean();
            //源码二:      实现DI    循环  beanMap中的每个bean     找到他们每个类中的每个由@AutoWire   @Resource注解的方法以实现Di
            handleDi(beanMap);
        }
    }

    private void handleDi(Map<String, Object> beanMap) throws InvocationTargetException, IllegalAccessException {
        Collection<Object> objectCollection=beanMap.values();
        for (Object obj:objectCollection) {
            Class cls=obj.getClass();
            Method[] ms=cls.getDeclaredMethods();
            for (Method m:ms) {
                if (m.isAnnotationPresent(MyAutowire.class)&&m.getName().startsWith("set")){

                    invokeAutowiredMethod(m,obj);
                }else if (m.isAnnotationPresent(MyResource.class)&&m.getName().startsWith("set")){
                    invokeResourceMethod(m,obj);
                }
            }
            Field[] fs=cls.getDeclaredFields();
            for (Field f:fs) {
                if (f.isAnnotationPresent(MyAutowire.class)){

                }else if (f.isAnnotationPresent(MyResource.class)){

                }
            }
        }
    }

    private void invokeResourceMethod(Method m, Object obj) throws InvocationTargetException, IllegalAccessException {
        //1.取出  MyResource的name属性值  当成beanid
        MyResource mr=m.getAnnotation(MyResource.class);
        String beanId=mr.name();
        //2.如果没有，则取出  m方法中参数的类型名    改成首字母小写     当成beanID
        if (beanId==null||beanId.equalsIgnoreCase("")){
            String pname=m.getParameterTypes()[0].getSimpleName();
            beanId=pname.substring(0,1).toLowerCase()+pname.substring(1);
        }
        //3.判断  从beanMap取出
        Object o=beanMap.get(beanId);
        //4.invoke
        m.invoke(obj,o);
    }

    private void invokeAutowiredMethod(Method m, Object obj) throws InvocationTargetException, IllegalAccessException {
        //1.取出  m的参数类型
        Class  typeClass=m.getParameterTypes()[0];
        //2.从beanMap中循环所有的Object
        Set<String> keys=beanMap.keySet();
        for (String key:keys) {
            //4.如果是 则从beanMap取出
            Object o=beanMap.get(key);
            // 3.判断  这些Object 是否为  参数类型的实例   instanceof
            Class[] interfaces=o.getClass().getInterfaces();
            for (Class c:interfaces) {
                System.out.println(c.getName()+"\t"+typeClass);
                if (c==typeClass){
                    //5.invoke
                    m.invoke(obj,o);
                    break;
                }
            }
        }
    }

    /**
     *
     * 处理ManageBeanClasses      所有的Class类       筛选出所有的@Component    @Service    @Reposity  的类并实例化   存到beanMap中
     */
    private void handleManageBean() throws InstantiationException, IllegalAccessException {
        for (Class c:managedBeanClasses) {
            if (c.isAnnotationPresent(MyComponent.class)){
                saveManagedBean(c);
            }else  if (c.isAnnotationPresent(MyService.class)){
                saveManagedBean(c);

            }else  if (c.isAnnotationPresent(MyRepository.class)){
                saveManagedBean(c);

            }else  if (c.isAnnotationPresent(MyController.class)){
                saveManagedBean(c);

            }else{
                    continue;
            }

        }
    }
    public void saveManagedBean(Class c) throws IllegalAccessException, InstantiationException {
        Object obj=c.newInstance();
        String beanid=c.getSimpleName().substring(0,1).toLowerCase()+c.getSimpleName().substring(1);
        beanMap.put(beanid,obj);
    }
    /**
     *
     * @param basePackage
     */
    private void scanPackageAndSubPackageClasses(String basePackage) throws IOException, ClassNotFoundException {
        String packagePath=basePackage.replaceAll("\\.","/");
        System.out.println("掃描包路徑"+packagePath+"提换后"+packagePath);
        Enumeration<URL> files=Thread.currentThread().getContextClassLoader().getResources(packagePath);    //当前ClassPath的绝对URI路径。
        while(files.hasMoreElements()){
            URL url=files.nextElement();
            System.out.println("扫描的包"+url.getFile());
            //TODO: 递归这些目录      查找      .class文件
            findClassesInPackages(url.getFile(),basePackage);
        }
    }
    private Set<Class> managedBeanClasses=new HashSet<Class>();

    /**
     * 查找       file下面及子包所有咬托管的class，存到一个set(managedBeanClasses)中去  ......
     * @param file
     * @param basePackage
     */
    private void findClassesInPackages(String file, String basePackage) throws ClassNotFoundException {
        File f=new File(file);
        File[] classFiles=f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".class")||pathname.isDirectory();
            }
        });
        for (File cf:classFiles) {
            if (cf.isDirectory()){
                //时目录则递归
                basePackage+=""+cf.getName().substring(cf.getName().lastIndexOf("/")+1);
                findClassesInPackages(cf.getAbsolutePath(),basePackage);
            }else {
                //加载    cf      作为class文件
                URL[] urls=new URL[]{};
                URLClassLoader ucl=new URLClassLoader(urls);
                System.out.println("localClass:\t"+cf.getName());
                Class c=ucl.loadClass(basePackage+"."+cf.getName().replace(".class",""));        //com.yc.bean.hello;
                managedBeanClasses.add(c);
            }
        }
    }

    /**
     * 处理       MyAppConfig配置类中的@Bean注解     完成IOC操作
     * @param cls
     * @param obj
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void handleAtMyBean(Class cls,Object obj) throws InvocationTargetException, IllegalAccessException {
        //1.获取cls中的所有method
        Method[] ms =cls.getDeclaredMethods();
        //2.循环，判断，每个method上是否有  @MyBean注解
        for (Method m:ms) {
            System.out.println(m.getAnnotation(MyBean.class));
            if (m.isAnnotationPresent(MyBean.class)){
                //3.有 则invoke它，将空上返回值   存到      beanMap，键是方法名,值 是返回值 对象
                Object o=m.invoke(obj);
                //TODO:         加入处理        @MyBean注解对应的方法所实例化的类中的  @MyPostConstruct        对应的方法
                handlePostConstruct(o,o.getClass());
                beanMap.put(m.getName(),o);
            }
        }
        //2.
    }

    /**
     * 处理一个bean 中的    @MyPostConstruct对应的方法
     * @param o
     * @param cls
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void handlePostConstruct(Object o, Class<?> cls) throws InvocationTargetException, IllegalAccessException {
        Method[] ms=cls.getDeclaredMethods();
        for (Method m:ms) {
            if (m.isAnnotationPresent(MyPostConstruct.class)){
                m.invoke(o);
            }
        }
    }

    /**
     * 获取当前   AppConfig类所在的路径
     * @param cl
     * @return
     */
    public String[] getAppConfigBasePackages(Class cl){
        String[] paths=new String[1];
        paths[0]=cl.getPackage().getName();
        return paths;
    }
    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}
