import service.XmlService;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class JaxbApplication {
    private static final String XML = "src/main/resources/clients_import.xml";
    private static final String XML_METHOD_1_RESULT = "src/main/resources/clients_method_1_result.xml";
    private static final String XML_METHOD_2_RESULT = "src/main/resources/clients_method_2_result.xml";
    private static final String XML_METHOD_3_RESULT = "src/main/resources/clients_method_3_result.xml";


    public static void main(String[] args) {
        XmlService xmlService = new XmlService();
        try {
            xmlService.generateXmlSortedByAccountOpenDate(XML, XML_METHOD_1_RESULT);
            xmlService.generateXmlAndRemoveNotActualDocuments(XML, XML_METHOD_2_RESULT);
            xmlService.generateXmlAndRemoveWrongDocuments(XML, XML_METHOD_3_RESULT);
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
