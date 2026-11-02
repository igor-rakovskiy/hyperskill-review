package server;

import java.util.Arrays;

public class ArrayDatabase implements Database {

    private String[] db;
    private int size;

    public ArrayDatabase(int size) {
        this.size = size;
        this.db = new String[this.size];

        Arrays.fill(this.db, "");
    }

    @Override
    public boolean set(int index, String value) {
        if (inIndex(index)) {
            db[index] = value;
            return true;
        }

        return false;
    }

    @Override
    public String get(int index) {
        if (inIndex(index) && !db[index].isEmpty()) {
            return db[index];
        }

        return null;
    }

    @Override
    public boolean delete(int index) {
        return set(index, "");
    }

    private boolean inIndex(int index) {
        return index >= 0 && index < size;
    }
}