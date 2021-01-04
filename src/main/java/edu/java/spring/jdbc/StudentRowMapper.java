package edu.java.spring.jdbc;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {
        try {
            Student student = new Student();
            student.setId(resultSet.getInt("id"));
            student.setName(resultSet.getString("name"));
            student.setAge(resultSet.getInt("age"));
            return student;
        } catch(Exception exp) {
            Logger.getInstance(StudentRowMapper.class).error(exp, exp);
            return null;
        }
    }
}
