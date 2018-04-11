package dk.roninit.model;

import java.util.ArrayList;
import java.util.List;

public class MarkedItemInstanceList {

    private List<MarkedItemView> list = new ArrayList<>();

    public List<MarkedItemView> getList() {
        return list;
    }

    public void setList(List<MarkedItemView> list) {
        this.list = list;
    }
}
