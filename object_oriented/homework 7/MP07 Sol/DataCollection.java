import java.util.Iterator;

public interface DataCollection<T> extends Iterable<T> {
    boolean put(T t);
    T elemAt(int index);
    int length();
    Iterator createIterator();
}
