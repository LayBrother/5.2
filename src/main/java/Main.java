import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.*;
import com.opencsv.bean.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    private static List<Employee> readXML(Node root) {
        List<Employee> list = new ArrayList<>();
        NodeList employees = root.getChildNodes();

        for (int i = 0; i < employees.getLength(); i++) {
            Node currentEmployee = employees.item(i);

            if (currentEmployee.getNodeType() != Node.TEXT_NODE) {
                Employee employeeFill = new Employee();
                NodeList nodeEmployee = currentEmployee.getChildNodes();

                for (int j = 0; j < nodeEmployee.getLength(); j++) {
                    Node propEmployee = nodeEmployee.item(j);

                    if (propEmployee.getNodeType() != Node.TEXT_NODE) {
                        if (("id").equals(propEmployee.getNodeName())) {
                            employeeFill.setId(Long.parseLong(propEmployee.getChildNodes().item(0).getTextContent()));
                        }

                        if (("firstName").equals(propEmployee.getNodeName())) {
                            employeeFill.setFirstName(propEmployee.getChildNodes().item(0).getTextContent());
                        }

                        if (("lastName").equals(propEmployee.getNodeName())) {
                            employeeFill.setLastName(propEmployee.getChildNodes().item(0).getTextContent());
                        }

                        if (("country").equals(propEmployee.getNodeName())) {
                            employeeFill.setCountry(propEmployee.getChildNodes().item(0).getTextContent());
                        }

                        if (("age").equals(propEmployee.getNodeName())) {
                            employeeFill.setAge(Integer.parseInt(propEmployee.getChildNodes().item(0).getTextContent()));
                        }
                    }
                }
                list.add(employeeFill);
            }
        }
        return list;
    }

    private static List<Employee> parseXML(String s) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(s));
        Node root = doc.getDocumentElement();
        return readXML(root);
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        String fileNameXML = "data.xml";

        List<Employee> listXML = parseXML(fileNameXML);
        String json = listToJson(listXML);

        try (FileWriter file = new FileWriter("data2.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}