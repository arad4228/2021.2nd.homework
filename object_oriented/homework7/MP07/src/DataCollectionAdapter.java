import java.util.ArrayList;
import java.util.Iterator;

public class DataCollectionAdapter<T> implements DataCollection<T>{
    private ArrayList<T> dataArray;

    public DataCollectionAdapter()
    {
        this.dataArray = new ArrayList<T>();
    }

    @Override
    public boolean put(T t) {
       dataArray.add(t);
       return dataArray.contains(t);
    }

    @Override
    public T elemAt(int index) {
        return dataArray.get(index);
    }

    @Override
    public int length() {
        return dataArray.size();
    }

    @Override
    public Iterator createIterator() {
        Iterator<T> iterator = dataArray.iterator();
        return iterator;
    }
}
