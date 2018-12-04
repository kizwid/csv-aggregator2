package kevsanders.aggregator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kevsa on 04/12/2018.
 */
public class CSVMapperTest {

    @Test
    public void simpleTest(){
        String csvFile = "Company, ActionsType, Action\n" +
                "ABC, Downloaded, Tutorial 1\n" +
                "ABC, Watched, Tutorial 2\n" +
                "PQR, Subscribed, Tutorial 1\n" +
                "ABC, Watched, Tutorial 2\n" +
                "PQR, Subscribed, Tutorial 3\n" +
                "XYZ, Subscribed, Tutorial 1\n" +
                "XYZ, Watched, Tutorial 3\n" +
                "PQR, Downloaded, Tutorial 1";

        assertEquals("Company, Downloaded, Watched, Subscribed\n" +
                "PQR, 1, 0, 2\n" +
                "ABC, 1, 2, 0\n" +
                "XYZ, 0, 1, 1\n", new CSVMapper().transformCsv(csvFile));

    }

}