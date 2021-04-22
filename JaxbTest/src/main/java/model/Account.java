package model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@Data
@XmlRootElement(name = "account")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"iban", "open_date"})
public class Account {
    @XmlElement
    private String iban;

    @XmlElement(name = "open_date")
    private XMLGregorianCalendar open_date;
}
