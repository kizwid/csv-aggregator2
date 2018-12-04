package kevsanders.aggregator;

import org.apache.commons.dbcp.BasicDataSource;
import org.h2.tools.Csv;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
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

    public static String load() throws SQLException {
        String csvFile="src\\main\\resources\\small-sample.csv";
        String dbUrl = "jdbc:h2:mem:test";
        try (Connection con = getConnection(dbUrl);) {
            try(Statement statement = con.createStatement()) {
                statement.execute("create table sample as select * from CSVREAD('" + csvFile + "')");
            }
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            try(Statement statement = con.createStatement()) {
                new Csv().write(pw, statement.executeQuery("SELECT Company" +
                        ",count(case when ActionsType='Downloaded' then 1 else null end) Downloaded" +
                        ",count(case when ActionsType='Watched' then 1 else null end) Watched" +
                        ",count(case when ActionsType='Subscribed' then 1 else null end) Subscribed" +
                        " FROM sample GROUP BY Company"));
            }

            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName("org.h2.Driver");
            ds.setUrl(dbUrl);
            ds.setUsername("");
            ds.setPassword("");

            JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
            System.out.println(jdbcTemplate.queryForObject("select count(*) from sample", Integer.class));
            System.out.println(jdbcTemplate.queryForObject("select count(distinct Company) from sample", Integer.class));

            return sw.toString();
        }
    }


}
