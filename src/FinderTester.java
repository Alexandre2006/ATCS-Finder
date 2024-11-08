import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.io.*;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinderTester {

    private final Finder studentSolution = new Finder();
    private int keyCol;
    private int valCol;
    private BufferedReader csvReader, queryReader, resultReader;

    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    public void testElements() {
        setTestData("elements", 2, 0);
        runTest();
    }

    @Test
    @Timeout(value = 150, unit = TimeUnit.MILLISECONDS)
    public void testIPs() {
        setTestData("IP", 0, 1);
        runTest();
    }

    @Test
    @Timeout(value = 325, unit = TimeUnit.MILLISECONDS)
    public void testStocks() {
        setTestData("DJIA", 0, 2);
        runTest();
    }

    @Test
    @Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
    public void testProducts() {
        setTestData("UPC", 0, 2);
        runTest();
    }

    private void runTest() {
        try {
            String query = queryReader.readLine();
            while (query != null) {
                    assertEquals(resultReader.readLine(), studentSolution.query(query), "Key mismatch error.");
                query = queryReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error opening query and result files.");
        }
    }

    private void setTestData(String fileName, int keyCol, int valCol) {
        try {
            csvReader = new BufferedReader(new FileReader("test_files/" + fileName + ".csv"));
            queryReader = new BufferedReader(new FileReader("test_files/" + fileName + "_queries.txt"));
            resultReader = new BufferedReader(new FileReader("test_files/" + fileName + "_results.txt"));

            studentSolution.buildTable(csvReader, keyCol, valCol);
        }
        catch (IOException e) {
            System.out.println("Error opening " + fileName + ".csv");
            e.printStackTrace();
        }
    }

}
