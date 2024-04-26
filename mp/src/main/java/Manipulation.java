import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Manipulation {

    //конвертируем файл CVS  в json и создаем из них список employee
    public List<Employee> parseCSV(String fileName, String[] columnMapping) {
        //создаем объект reader для чтения файла
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            //создаем объект strategy для создания столбцов
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            //устанавливаем тип объекта
            strategy.setType(Employee.class);
            //установка маппинга столбцов csv
            strategy.setColumnMapping(columnMapping);
            //создаем объект csv
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    //установка стратегии маппинга
                    .withMappingStrategy(strategy)
                    .build();
            //парсинг cvs файла и сосдание списка employees
            List<Employee> employees = csv.parse();
            return employees;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //преобразовываем список в файл json
    public static String listToJson(List<Employee> employees) {
        //создаем объект gson для сериализации объектов
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //создаем объект  listType для предоставления типа данных Employee
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        //преобразовываем список employees в  json  строку
        String json = gson.toJson(employees, listType);

        return json;
    }

    //записываем данные в файл
    public static void toJson(String json, String filePath) {
        //создаем объекг fileWrriter для чтения файла
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            //читаем файл
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //получение списка из документа xml
    public static List<Employee> parseXML(String xmlFilePath) {
        List<Employee> employees = new ArrayList<>();
        int id = 0;
        String firstName = "";
        String lastName = "";
        String country = "";
        int age = 0;
        try {
            //создаем объекты factory,builder,doc
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));
            //получаем список элементов
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            //перебираем список элементов
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    Element element = (Element) node;
                    // Извлекаем значения из дочерних элементов
                    NodeList childNodes = element.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node childNode = childNodes.item(j);
                        if (childNode instanceof Element) {
                            Element childElement = (Element) childNode;
                            switch (childElement.getNodeName()) {
                                case "id":
                                    id = Integer.parseInt(childElement.getTextContent());
                                    break;
                                case "firstName":
                                    firstName = childElement.getTextContent();
                                    break;
                                case "lastName":
                                    lastName = childElement.getTextContent();
                                    break;
                                case "country":
                                    country = childElement.getTextContent();
                                    break;
                                case "age":
                                    age = Integer.parseInt(childElement.getTextContent());
                                    break;
                            }
                        }
                    }
                    // Создаем новый объект типа Employee
                    Employee employee = new Employee(id, firstName, lastName, country, age);
                    // Добавляем созданный объект в список employees
                    employees.add(employee);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return employees;
    }

    public static String readString(String fileName) {
        //создаем объект  sb
        StringBuilder sb = new StringBuilder();
        //создаем объект br для записи файла
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            //читаем файл и записываем строку
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //преобразовываем json данные в java объекты
    public static List<Employee> jsonToList(String json) {
        List<Employee> employees = new ArrayList<>();
        Gson gson = new Gson();// создаем объект gson
        JsonParser parser = new JsonParser(); //создаем объект parser
        //преобразуем json в объект jsonArray
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();

        for (JsonElement element : jsonArray) { //проходим по всем элементам jsonArray
            JsonObject jsonObject = element.getAsJsonObject();
            //извлекаем данные и преобразуем в String
            long id = jsonObject.get("id").getAsLong();
            String firstName = jsonObject.get("firstName").getAsString();
            String lastName = jsonObject.get("lastName").getAsString();
            String country = jsonObject.get("country").getAsString();
            int age = jsonObject.get("age").getAsInt();
            //создаем новый объект с полученными значениями
            Employee employee = new Employee(id, firstName, lastName, country, age);
            //добавляем созданный объект в список employees
            employees.add(employee);
        }
        return employees;
    }
}
