import com.opencsv.CSVWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Main {
    public static void main(String[] args) throws ParserConfigurationException, TransformerException {
        Manipulation manipulation = new Manipulation();
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String jsonFileName = "data.json";
        String jsonFileNameXml = "dataa.json";
        String fileNameXml = "data.xml";
        String[] employeeInfo1 = "1,John,Smith,USA,25".split(",");
        String[] employeeInfo2 = "2,Ivan,Petrov,RU,23".split(",");
        //записываем файл CSV
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            writer.writeNext(employeeInfo1);
            writer.writeNext(employeeInfo2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //создаем  пустой файл XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        //создаем теги и сохраняем в них данные
        Element rootElement = doc.createElement("staff");
        doc.appendChild(rootElement);
        Element employee1 = doc.createElement("employee");
        rootElement.appendChild(employee1);
        Element id1 = doc.createElement("id");
        id1.appendChild(doc.createTextNode("1"));
        employee1.appendChild(id1);
        Element firstName1 = doc.createElement("firstName");
        firstName1.appendChild(doc.createTextNode("John"));
        employee1.appendChild(firstName1);
        Element lastName1 = doc.createElement("lastName");
        lastName1.appendChild(doc.createTextNode("Smith"));
        employee1.appendChild(lastName1);
        Element country1 = doc.createElement("country");
        country1.appendChild(doc.createTextNode("USA"));
        employee1.appendChild(country1);
        Element age1 = doc.createElement("age");
        age1.appendChild(doc.createTextNode("25"));
        employee1.appendChild(age1);

        //создаем теги и сохраняем в них данные
        Element employee2 = doc.createElement("employee");
        rootElement.appendChild(employee2);
        Element id2 = doc.createElement("id");
        id2.appendChild(doc.createTextNode("2"));
        employee2.appendChild(id2);
        Element firstName2 = doc.createElement("firstName");
        firstName2.appendChild(doc.createTextNode("Ivan"));
        employee2.appendChild(firstName2);
        Element lastName2 = doc.createElement("lastName");
        lastName2.appendChild(doc.createTextNode("Petrov"));
        employee2.appendChild(lastName2);
        Element country2 = doc.createElement("country");
        country2.appendChild(doc.createTextNode("RU"));
        employee2.appendChild(country2);
        Element age2 = doc.createElement("age");
        age2.appendChild(doc.createTextNode("23"));
        employee2.appendChild(age2);

        //записываем данные в файл XML
        DOMSource domSource = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(fileNameXml));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("indent", "yes");//включаем отступы
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");//добавляем пробелы для каждого уровня
        transformer.transform(domSource, streamResult);


        // преобразовываем файл CVS в список
        List<Employee> employees = manipulation.parseCSV(fileName, columnMapping);
        //преобразовываем список в json
        String json = Manipulation.listToJson(employees);
        //записываем в файл json
        Manipulation.toJson(json, jsonFileName);

        // преобразовываем файл xnl в список
        List<Employee> employeesFromXML = manipulation.parseXML(fileNameXml);
        //преобразовываем список в json
        String jn = Manipulation.listToJson(employeesFromXML);
        //записываем в файл json
        Manipulation.toJson(jn, jsonFileNameXml);
        String s = Manipulation.readString("dataa.json");
        List<Employee> list = Manipulation.jsonToList(s);
       //выводим в консоль содержание списка list
        for (Employee employee : list) {
            System.out.println(employee.toString());
        }
    }

}
