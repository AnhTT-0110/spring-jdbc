package edu.java.spring;

import org.apache.log4j.Logger;

public class HelloWorld {

    private final static Logger LOGGER = Logger.getLogger(HelloWorld.class);
    String message;

    public void sayHello() {
        LOGGER.info("From HelloWorld: " + message);
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public HelloWorld(String name, HelloClazz clazz) {
        message = "From HelloWorld constructor:" + name + " : " + clazz.getMessage() ;
    }
}
