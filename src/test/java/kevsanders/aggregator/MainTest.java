package kevsanders.aggregator;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by kevsa on 04/12/2018.
 */
public class MainTest {

    @Test
    public void simpleTest() throws SQLException {
        String sep = System.getProperty("line.separator");
        assertEquals("\"COMPANY\",\"DOWNLOADED\",\"WATCHED\",\"SUBSCRIBED\"" + sep +
                "\"PQR\",\"1\",\"0\",\"2\"" + sep +
                "\"ABC\",\"1\",\"2\",\"0\"" + sep +
                "\"XYZ\",\"0\",\"1\",\"1\"" + sep
                , Main.load());
    }


}