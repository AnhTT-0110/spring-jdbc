package edu.java.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HelloClazz implements DisposableBean {

    String message;

    private List<JavaClazz> clazzes;

    public void setMessage(String message){
        this.message = "Call From Setter: " + message;
    }

    public HelloClazz() {
    }

    public String getMessage() {
        return message;
    }

    public HelloClazz(int person) {
        message = "From Constructor: Say hello to " + person +" person(s)!";
    }

    public HelloClazz(HelloClazz clazz) {
        message = clazz.message;
    }

    public void printMessage() {
        System.out.printf("Your Message : " + message);
    }

    @Bean(name="bean2")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public HelloClazz getHelloBean() {
        return new HelloClazz();
    }

    private void initMessage() {
        System.out.println("Calling init method....");
        message = "From init method: Say hello bean!";
    }

    private void release() {
        message = null;
        System.out.println("Message is null");
    }

    @Override
    public void destroy() throws Exception {
        message = null;
        System.out.println("Message is null");

    }

    public List<JavaClazz> getClazzes() {
        return clazzes;
    }

    public void setClazzes(List clazzes) {
        this.clazzes = clazzes;
    }
}
