package edu.java.spring.jdbc;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class JdbcApp {
   static Logger logger = (Logger) Logger.getInstance(JdbcApp.class);
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student("Tran Thi A", 17));
        students.add(new Student("Le Van L", 20));
        students.add(new Student("Phan Thi Z", 25));


        ApplicationContext context =  new ClassPathXmlApplicationContext("beans.xml");
        StudentJdbcDAO jdbc = (StudentJdbcDAO) context.getBean("studentJdbcDAO");
        //logger.info("Create bean " + studentJdbcDAO);
       // studentJdbcDAO.insert("Tran Van A", 24);
        jdbc.updateAgeByName("Tran Van B", 20);
        logger.info("Total students is " + jdbc.totalRecord());
        jdbc.deleteStudentById(1301);

        jdbc.loadAllStudent().forEach(student -> logger.info("id: " + student.getId() + " + " + student.getAge()));
        logger.info("Total students is " + jdbc.totalRecord());
        int[] values = jdbc.add(students);
        for( int i = 0; i < values.length; i++) {
            logger.info ("add record "+ i + ": "+ (values[i] == 0 ? "failed" : "success" ));
        }

        logger.info("Total students is " + jdbc.totalRecord());

        jdbc.save("Tran Thi A", "23");
        jdbc.save("Tran Thi A", 23);
    }
}
