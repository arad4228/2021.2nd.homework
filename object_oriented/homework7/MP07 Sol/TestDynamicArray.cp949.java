import java.util.Iterator;
import java.util.ArrayList;

public class TestDynamicArray {
    public static void main(String[] args) {
        DynamicArray<String> arr = new DynamicArray<String>();
        arr.put("Seoul");
        arr.put("Busan");
        arr.put("Gwangju");
        arr.put("Daejeon");
        arr.put("Cheonan");
        arr.put("Ulsan");
        arr.put("Daegu");
        arr.put("Incheon");

        System.out.println("index");
        for (int i = 0; i < arr.length(); i++) {
            String s = (String) arr.elemAt(i);
            System.out.println(s);
        }

        System.out.println("iterator");
        for (Iterator iter = arr.createIterator(); iter.hasNext(); ) {
            String s = (String) iter.next();
            System.out.println(s);
        }

        System.out.println("for-each");
        for (String s : arr) {
            System.out.println(s);
        }

        ArrayList<String> list = new ArrayList<String>();
        // 여기서는 만들어진 어댑터 클래스가 ArrayListDataCollectionAdapter라고 가정함. 다른 이름으로 만들었다면 
        // 다음 코드를 수정할 것
        DataCollection<String> arr2 = new ArrayListDataCollectionAdapter<String>(list);
        arr2.put("Seoul");
        arr2.put("Busan");
        arr2.put("Gwangju");
        arr2.put("Daejeon");
        arr2.put("Cheonan");
        arr2.put("Ulsan");
        arr2.put("Daegu");
        arr2.put("Incheon");

        System.out.println("index");
        for (int i = 0; i < arr2.length(); i++) {
            String s = (String) arr2.elemAt(i);
            System.out.println(s);
        }

        System.out.println("\niterator");
        for (Iterator iter = arr2.createIterator(); iter.hasNext(); ) {
            String s = (String) iter.next();
            System.out.println(s);
        }

        System.out.println("\nfor-each");
        for (String s : arr2) {
            System.out.println(s);
        }
    }
}
