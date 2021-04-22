package comparator;

import model.Document;

import java.util.Comparator;

public class SortByDocumentGivenDateComparator implements Comparator<Document> {
    @Override
    public int compare(Document o1, Document o2) {
        if (o1.getGiven_date().before(o2.getGiven_date())) {
            return 1;
        } else if (o1.getGiven_date().after(o2.getGiven_date())) {
            return -1;
        } else {
            return 0;
        }
    }
}
