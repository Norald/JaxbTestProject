package util;

import model.ClientsHolder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlWorker {
    public static ClientsHolder readXml(String pathToFile) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ClientsHolder.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (ClientsHolder) unmarshaller.unmarshal(new File(pathToFile));
    }

    public static void saveToXml(ClientsHolder clientsHolder, String pathToFile) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ClientsHolder.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(clientsHolder, System.out);
        marshaller.marshal(clientsHolder, new File(pathToFile));
    }
}
