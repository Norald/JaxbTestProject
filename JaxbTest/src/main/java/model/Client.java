package model;

import adapter.DateAdapter;
import lombok.Data;
import model.enums.ClientGender;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Date;

@Data
@XmlRootElement(name = "client")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name", "surname", "birthday", "gender", "documents", "account"})
public class Client {

    @XmlElement
    private int id;

    @XmlElement
    private String name;

    @XmlElement
    private String surname;

    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date birthday;

    @XmlElement
    private ClientGender gender;

    @XmlElementWrapper(name = "documents")
    @XmlElement(name = "document")
    private ArrayList<Document> documents;

    @XmlElement(name = "account")
    private Account account;


}
