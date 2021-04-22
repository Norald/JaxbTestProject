package model;

import adapter.DateAdapter;
import lombok.Data;
import model.enums.DocumentType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@Data
@XmlRootElement(name = "document")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"type", "number", "given_date", "series", "given"})
public class Document {
    @XmlElement(name = "type")
    private DocumentType type;

    @XmlElement(name = "number")
    private int number;

    @XmlElement(name = "given_date")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date given_date;

    @XmlElement(name = "series")
    private String series;

    @XmlElement(name = "given")
    private String given;

}
