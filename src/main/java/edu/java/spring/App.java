package edu.java.spring;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
//        HelloClazz helloClazz = (HelloClazz) context.getBean("helloJavaClazz");
//        helloClazz.printMessage();
//        HelloClazz helloJavaClazz2z = (HelloClazz) context.getBean("helloJavaClazz2");
//        helloJavaClazz2z.printMessage();


//
//        HelloClazz obj2 = (HelloClazz) context.getBean("helloJavaClazz");
//        obj2.printMessage();
//        System.out.println("compare: " + (obj2 == helloClazz));

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
//        HelloClazz helloClazz = (HelloClazz) context.getBean("helloJavaClazz");
//        helloClazz.printMessage();
//
//        context.registerShutdownHook();

        HelloWorld helloWorld = (HelloWorld) context.getBean("helloWorld");
        helloWorld.sayHello();

        Logger LOGGER = Logger.getLogger(HelloWorld.class);

        JavaClazz clazz = (JavaClazz) context.getBean("jee01");
        LOGGER.info("Map Implement is " + clazz.getStudents().getClass());
        LOGGER.info("There are " + clazz.getStudents().size() + " in the class");
        HelloClazz  clazz1 = (HelloClazz) context.getBean("helloJavaClazz");
        LOGGER.info("Total classes is " + clazz1.getClazzes().size());
    }
}
