package edu.java.spring.jdbc;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentJdbcDAO {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private DriverManagerDataSource dataSource;
    private String insertQuery;
    private String updateAgeByNameSQL = "update STUDENT set age = ? where name = ?";
    private String deleteStudent = "DELETE FROM STUDENT WHERE id = ?";

    @Autowired
    private PlatformTransactionManager transactionManager;


    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void updateAgeByName(String name, int age) {
        jdbcTemplate.update(updateAgeByNameSQL, age, name);
        Logger.getInstance(StudentJdbcDAO.class).info("Update Record Name = " + name + " Age = " + age);
    }

    public void deleteStudentById(int id) {
        jdbcTemplate.update(deleteStudent, id);
        Logger.getInstance(StudentJdbcDAO.class).info("Delete Record id= " + id);
    }

    private void createTableIfNotExist() throws SQLException {
        DatabaseMetaData dbmd;
        dbmd = dataSource.getConnection().getMetaData();
        ResultSet rs = dbmd.getTables(null, null, "STUDENT", null);
        if (rs.next()) {
            Logger.getInstance(StudentJdbcDAO.class).info("Table " + rs.getString("TABLE_NAME") + " already exists !");
            return;
        }
        jdbcTemplate.execute("CREATE TABLE STUDENT ("
                + " id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1),"
                + " name VARCHAR(1000),"
                + " age INTEGER)");
    }

    public void setDataSource(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void insert(String name, int age) {
        jdbcTemplate.update(insertQuery, name, age);
        Logger.getInstance(StudentJdbcDAO.class).info("Created Record Name = " + name + " Age = " + age);
    }

    public void setInsertQuery(String insertQuery) {
        this.insertQuery = insertQuery;
    }

    public int totalRecord() {
        return jdbcTemplate.execute(new StatementCallback<Integer>() {
            @Override
            public Integer doInStatement(Statement statement) throws SQLException, DataAccessException {
                ResultSet rs = statement.executeQuery("select count(*) from student");
                return rs.next() ? rs.getInt(1) : 0;

            }

        });
    }

    private final static class StudentRowMapper implements RowMapper<Student> {
        @Override
        public Student mapRow(ResultSet resultSet, int i) throws SQLException {
            Student student = new Student();
            student.setId(resultSet.getInt("id"));
            student.setName(resultSet.getString("name"));
            student.setAge(resultSet.getInt("age"));
            return student;
        }
    }


    public List<Student> loadStudent(String name) {
        return jdbcTemplate.query(
                "SELECT * FROM STUDENT WHERE NAME LIKE '%" + name + "%'",
                new StudentRowMapper());
    }

    public List<Student> loadAllStudent() {
        return jdbcTemplate.query(
                "SELECT * FROM STUDENT",
                new StudentRowMapper());
    }


    public int[] add(List<Student> students) {
        List<Object[]> batch = new ArrayList<>();
        students.forEach(student -> batch.add(new Object[]{student.getName(), student.getAge()}));
        return jdbcTemplate.batchUpdate(insertQuery, batch);
    }

    public void save(Object name, Object age) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        String countQuery = "SELECT COUNT(*) FROM STUDENT";
        try {
            int total = jdbcTemplate.queryForObject(countQuery, Integer.class);
            Logger.getInstance(StudentJdbcDAO.class).info("before save data, total record is " + total);
            transactionManager.commit(status);
        } catch (Exception exception) {
            // error(exp, exp);
            transactionManager.rollback(status);
            int total = jdbcTemplate.queryForObject(countQuery, Integer.class);
            Logger.getInstance(StudentJdbcDAO.class).info("after rollback, total record is " + total);
        }

    }

    public void save(String name, int age) {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        String countQuery = "SELECT COUNT(*) FROM STUDENT";
        int total = jdbcTemplate.queryForObject(countQuery, Integer.class);

        try {
            Logger.getInstance(StudentJdbcDAO.class).info("before delete data, total record is " + total);
            String sql = "DELETE FROM STUDENT WHERE name= ? and age = ?";
            jdbcTemplate.update(sql, name, age);

            total = jdbcTemplate.queryForObject(countQuery, Integer.class);
            Logger.getInstance(StudentJdbcDAO.class).info("after save data, total record is " + total);

            String countQuery2 = "SELECT COUNT(*) FROM STUDENT";
             total = jdbcTemplate.queryForObject(countQuery2, Integer.class);
            transactionManager.commit(status);
        } catch(Exception exception){
            transactionManager.rollback(status);
            total = jdbcTemplate.queryForObject(countQuery, Integer.class);
            Logger.getInstance(StudentJdbcDAO.class).info("after rollback, total record is " + total);

        }
    }
}

