
import com.opencsv.CSVWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;



public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Manipulation manipulation = new Manipulation();
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String json = "data.json";
        String[] employeeInfo1 = "1,John,Smith,USA,25".split(",");
        String[] employeeInfo2 = "2,Ivan,Petrov,RU,23".split(",");

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            writer.writeNext(employeeInfo1 );
            writer.writeNext(employeeInfo2);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
