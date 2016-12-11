package biz.clag.ordermanage.model;

import java.io.Serializable;
import java.util.LinkedList;

public class Tables implements Serializable {
    private LinkedList<Table> mTables;

    public LinkedList<Table> getTables() {
        return mTables;
    }

    public Tables() {
        mTables = new LinkedList<>();
        mTables.add(new Table(1));
        mTables.add(new Table(2));
        mTables.add(new Table(3));
        mTables.add(new Table(4));
        mTables.add(new Table(5));
    }

}