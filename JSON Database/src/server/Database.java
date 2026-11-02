package server;

public interface Database {

    boolean set(int index, String value);

    String get(int index);

    boolean delete(int index);
}