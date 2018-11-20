package kevsanders.aggregator;

import org.apache.commons.dbcp.BasicDataSource;
import org.h2.tools.Csv;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;

/**
 * Created by kevsa on 20/11/2018.
 */
public class Main {

    public static void main(String[] args) throws SQLException {
        load();
    }

    public static void load() throws SQLException {
        String csvFile="src\\main\\resources\\small-sample.csv";
        String dbUrl = "jdbc:h2:mem:test";
        try (Connection con = getConnection(dbUrl);) {
            try(Statement statement = con.createStatement()) {
                statement.execute("create table sample as select * from CSVREAD('" + csvFile + "')");
            }
            try(Statement statement = con.createStatement()) {
                new Csv().write("sample_out.csv", statement.executeQuery("SELECT NOEUDADRESS, COUNT(NOEUDSRECE) FROM sample GROUP BY NOEUDADRESS"), null);
            }

            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName("org.h2.Driver");
            ds.setUrl(dbUrl);
            ds.setUsername("");
            ds.setPassword("");

            JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
            System.out.println(jdbcTemplate.queryForObject("select count(*) from sample", Integer.class));
            System.out.println(jdbcTemplate.queryForObject("select count(distinct Hello) from sample", Integer.class));

        }
    }


}
