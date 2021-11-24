public class NameSorter {

	public void bubbleSort(Person[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = 0; j < data.length - 1; j++) {
                if (data[j].getName().compareTo(data[j + 1].getName()) > 0) {
                	Person temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
            }
        }
    }

}
