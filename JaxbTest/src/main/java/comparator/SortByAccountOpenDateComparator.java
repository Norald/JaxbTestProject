package comparator;

import model.Client;

import java.util.Comparator;

public class SortByAccountOpenDateComparator implements Comparator<Client> {
    @Override
    public int compare(Client o1, Client o2) {
        return o1.getAccount().getOpen_date().compare(o2.getAccount().getOpen_date());
    }
}
