package service;

import comparator.SortByAccountOpenDateComparator;
import model.Account;
import model.Client;
import model.ClientsHolder;
import model.Document;
import model.enums.ClientGender;
import model.enums.DocumentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.XmlWorker;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class XmlServiceTest {

    private final String XML = "resources/clients_import_test.xml";
    private final String XML_METHOD_1_RESULT = "resources/clients_method_1_test.xml";
    private final String XML_METHOD_2_RESULT = "resources/clients_method_2_test.xml";
    private final String XML_METHOD_3_RESULT = "resources/clients_method_3_test.xml";

    private ClientsHolder clientsHolder;
    private XmlService xmlService;


    @BeforeEach
    public void init() throws JAXBException {
        clientsHolder = XmlWorker.readXml(XML);
        xmlService = new XmlService();
    }

    @Test
    public void shouldCheckSortXmlFileByAccountOpenDate() throws JAXBException, FileNotFoundException {
        //Given
        ArrayList<Client> clients = clientsHolder.getClients();
        clients.sort(new SortByAccountOpenDateComparator());

        //When
        xmlService.generateXmlSortedByAccountOpenDate(XML, XML_METHOD_1_RESULT);

        //Then
        Assertions.assertEquals(clients, XmlWorker.readXml(XML_METHOD_1_RESULT).getClients());
    }

    @Test
    public void shouldCheckIfNotActualDocumentsRemoved() throws JAXBException, FileNotFoundException, ParseException {
        //Given
        ClientsHolder clientsHolderNew = new ClientsHolder();
        Client client = new Client();
        client.setId(1234);
        client.setName("Unit name");
        client.setSurname("Unit surname");
        client.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("2000-04-02"));
        client.setGender(ClientGender.M);

        Document document1 = new Document();
        document1.setType(DocumentType.INN);
        document1.setNumber(123456);
        document1.setGiven_date(new SimpleDateFormat("yyyy-MM-dd").parse("2008-01-13"));

        Document document2 = new Document();
        document2.setType(DocumentType.INN);
        document2.setNumber(654321);
        document2.setGiven_date(new SimpleDateFormat("yyyy-MM-dd").parse("2021-04-21"));

        ArrayList<Document> documents = new ArrayList<>();
        documents.add(document1);
        documents.add(document2);

        Account account = new Account();
        account.setIban("UA123321123321123");

        client.setAccount(account);
        client.setDocuments(documents);

        ArrayList<Client> clients = new ArrayList<>();
        clients.add(client);

        clientsHolderNew.setClients(clients);

        XmlWorker.saveToXml(clientsHolderNew, XML_METHOD_2_RESULT);

        //When
        xmlService.generateXmlAndRemoveNotActualDocuments(XML_METHOD_2_RESULT, XML_METHOD_2_RESULT);

        //Then
        Assertions.assertEquals(client.getName(), XmlWorker.readXml(XML_METHOD_2_RESULT).getClients().get(0).getName());
        Assertions.assertNotEquals(client.getDocuments().contains(document1), XmlWorker.readXml(XML_METHOD_2_RESULT).getClients().get(0).getDocuments().contains(document1));
    }

    @Test
    public void shouldRemoveWrongDocuments() throws JAXBException, FileNotFoundException, ParseException {
        //Given
        ClientsHolder clientsHolderNew = new ClientsHolder();
        Client client = new Client();
        client.setId(1234);
        client.setName("Unit name");
        client.setSurname("Unit surname");
        client.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("2000-04-02"));
        client.setGender(ClientGender.M);

        Document document1 = new Document();
        document1.setType(DocumentType.INN);
        document1.setNumber(123456);
        document1.setGiven_date(new SimpleDateFormat("yyyy-MM-dd").parse("1999-01-13"));

        Document document2 = new Document();
        document2.setType(DocumentType.INN);
        document2.setNumber(654321);
        document2.setGiven_date(new SimpleDateFormat("yyyy-MM-dd").parse("2021-04-21"));

        Document document3 = new Document();
        document3.setType(DocumentType.PASSPORT);
        document3.setNumber(132467);
        document3.setGiven_date(new SimpleDateFormat("yyyy-MM-dd").parse("2006-04-21"));
        document3.setGiven("ASDDSA");
        document3.setSeries("AA");

        Document document4 = new Document();
        document4.setType(DocumentType.IDCARD);
        document4.setNumber(8888);
        document4.setGiven_date(new SimpleDateFormat("yyyy-MM-dd").parse("2008-04-21"));
        document4.setGiven("1234");

        ArrayList<Document> documents = new ArrayList<>();
        documents.add(document1);
        documents.add(document2);
        documents.add(document3);
        documents.add(document4);

        Account account = new Account();
        account.setIban("UA123321123321123");

        client.setAccount(account);
        client.setDocuments(documents);

        ArrayList<Client> clients = new ArrayList<>();
        clients.add(client);

        clientsHolderNew.setClients(clients);

        XmlWorker.saveToXml(clientsHolderNew, XML_METHOD_3_RESULT);

        //When
        xmlService.generateXmlAndRemoveWrongDocuments(XML_METHOD_3_RESULT, XML_METHOD_3_RESULT);

        //Then
        Assertions.assertEquals(client.getName(), XmlWorker.readXml(XML_METHOD_3_RESULT).getClients().get(0).getName());
        Assertions.assertNotEquals(client.getDocuments().contains(document1), XmlWorker.readXml(XML_METHOD_3_RESULT).getClients().get(0).getDocuments().contains(document1));
        Assertions.assertNotEquals(client.getDocuments().contains(document3), XmlWorker.readXml(XML_METHOD_3_RESULT).getClients().get(0).getDocuments().contains(document3));
        Assertions.assertNotEquals(client.getDocuments().contains(document4), XmlWorker.readXml(XML_METHOD_3_RESULT).getClients().get(0).getDocuments().contains(document4));
    }

}
