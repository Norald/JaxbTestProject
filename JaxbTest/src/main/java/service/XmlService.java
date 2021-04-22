package service;

import comparator.SortByAccountOpenDateComparator;
import comparator.SortByDocumentGivenDateComparator;
import exception.DocumentWrongDateException;
import exception.WrongDocumentException;
import model.Client;
import model.ClientsHolder;
import model.Document;
import model.enums.DocumentType;
import util.XmlWorker;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class XmlService {

    private final static String ID_CARD_LIMIT_DATE = "2016-01-01";
    private final static String PASSPORT_LIMIT_DATE = "1992-01-01";


    public String generateXmlSortedByAccountOpenDate(String xmlIn, String xmlOut) throws JAXBException, FileNotFoundException {
        ClientsHolder holder = XmlWorker.readXml(xmlIn);
        ArrayList<Client> clients = holder.getClients();
        clients.sort(new SortByAccountOpenDateComparator());
        holder.setClients(clients);
        XmlWorker.saveToXml(holder, xmlOut);
        return xmlOut;
    }


    public String generateXmlAndRemoveNotActualDocuments(String xmlIn, String xmlOut) throws JAXBException, FileNotFoundException {
        ClientsHolder holder = XmlWorker.readXml(xmlIn);
        List<Client> clients = holder.getClients();
        holder.setClients((ArrayList<Client>) removeNotActualDocumentsFromClients(clients));
        XmlWorker.saveToXml(holder, xmlOut);
        return xmlOut;
    }

    public String generateXmlAndRemoveWrongDocuments(String xmlIn, String xmlOut) throws JAXBException, FileNotFoundException {
        ClientsHolder holder = XmlWorker.readXml(xmlIn);
        ArrayList<Client> clients = holder.getClients();
        holder.setClients((ArrayList<Client>) removeWrongDocumentsFromClients(clients));
        holder.setClients(clients);
        XmlWorker.saveToXml(holder, xmlOut);
        return xmlOut;
    }

    private List<Client> removeWrongDocumentsFromClients(List<Client> clients) {
        List<Client> clientsWithActualDocuments = new ArrayList<>();

        for (Client client : clients) {
            ArrayList<Document> actualDocuments = new ArrayList<>();
            for (Document document : client.getDocuments()) {
                try {
                    validateDocument(client, document);
                    actualDocuments.add(document);
                } catch (ParseException | WrongDocumentException | DocumentWrongDateException e) {
                    e.printStackTrace();
                }
            }
            client.setDocuments(actualDocuments);
            clientsWithActualDocuments.add(client);
        }
        return clientsWithActualDocuments;
    }

    private List<Client> removeNotActualDocumentsFromClients(List<Client> clients) {
        List<Client> list = new ArrayList<>();
        for (Client client : clients) {
            List<Document> documents = removeNotActualDocuments(client.getDocuments());
            client.setDocuments((ArrayList<Document>) documents);
            list.add(client);
        }
        return list;
    }

    private List<Document> removeNotActualDocuments(List<Document> documents) {
        List<Document> list = new ArrayList<>();
        Optional.ofNullable(returnUnique(returnListByDocumentType(documents, DocumentType.PASSPORT))).ifPresent(list::addAll);
        Optional.ofNullable(returnUnique(returnListByDocumentType(documents, DocumentType.IDCARD))).ifPresent(list::addAll);
        Optional.ofNullable(returnUnique(returnListByDocumentType(documents, DocumentType.INN))).ifPresent(list::addAll);
        return list;
    }

    private List<Document> returnListByDocumentType(List<Document> documents, DocumentType documentType) {
        return documents.stream().filter(document -> document.getType().equals(documentType)).collect(Collectors.toList());
    }

    private List<Document> returnUnique(List<Document> list) {
        List<Document> listDocument = new ArrayList<>();
        if (list.isEmpty()) {
            return null;
        }
        Document document = list.stream().sorted(new SortByDocumentGivenDateComparator()).findFirst().get();
        listDocument.add(document);
        return listDocument;
    }

    private void validateDocument(Client client, Document document) throws ParseException {
        if (document.getType().equals(DocumentType.INN)) {
            if (document.getGiven_date().before(client.getBirthday())) {
                throw new WrongDocumentException("INN is issued after birth.");
            }
        }
        if (document.getType().equals(DocumentType.IDCARD)) {
            if (document.getGiven_date().before(new SimpleDateFormat("yyyy-MM-dd").parse(ID_CARD_LIMIT_DATE))) {
                throw new DocumentWrongDateException("Wrong id card given date. Should be before: " + ID_CARD_LIMIT_DATE + ". Current value: " + document.getGiven_date());
            }
            if (subtractTwoDates(document.getGiven_date(), client.getBirthday()) < getAllowedAmountOfDays(document.getType()) || document.getGiven_date().before(client.getBirthday())) {
                throw new WrongDocumentException("ID card can be issued only from 14 years - 30 days.");
            }
        }
        if (document.getType().equals(DocumentType.PASSPORT)) {
            if (document.getGiven_date().before(new SimpleDateFormat("yyyy-MM-dd").parse(PASSPORT_LIMIT_DATE))) {
                throw new DocumentWrongDateException("Wrong passport given date. Should be before: " + PASSPORT_LIMIT_DATE + ". Current value: " + document.getGiven_date());
            }
            if (subtractTwoDates(document.getGiven_date(), client.getBirthday()) < getAllowedAmountOfDays(document.getType()) || document.getGiven_date().before(client.getBirthday())) {
                throw new WrongDocumentException("Passport can be issued only from 16 years - 30 days.");
            }
        }
    }

    private long subtractTwoDates(Date date1, Date date2) throws ParseException {
        LocalDate d1 = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date1), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDate d2 = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date2), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
        long diffDays = Math.abs(diff.toDays());
        return diffDays;
    }

    private int getAllowedAmountOfDays(DocumentType documentType) {
        if (documentType.equals(DocumentType.PASSPORT)) {
            return (16 * 365) - 30;
        } else if (documentType.equals(DocumentType.IDCARD)) {
            return (14 * 365) - 30;
        } else {
            return 0;
        }
    }
}
