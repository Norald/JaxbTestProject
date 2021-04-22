package adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends XmlAdapter<String, Date> {

    private static final String CUSTOM_FORMAT_STRING = "yyyy-MM-dd";

    @Override
    public Date unmarshal(String v) throws Exception {
        return new SimpleDateFormat(CUSTOM_FORMAT_STRING).parse(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
        return new SimpleDateFormat(CUSTOM_FORMAT_STRING).format(v);
    }
}
