import java.util.Iterator;

public class DynamicArrayIterator<T> implements Iterator<T> {
    private DynamicArray<T> array;
    private int index;

    public DynamicArrayIterator(DynamicArray<T> arr) {
        array = arr;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return (index < array.length());
    }

    @Override
    public T next() {
        Object obj = array.elemAt(index);
        index++;
        return (T) obj;
    }

    @Override
    public void remove() {
        //throw new UnsupportedOperationException();
    }
}
