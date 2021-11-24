public class AgeSorter extends Sorter {

    @Override
    Boolean SortMethod(Person p1, Person p2) {
        if (p1.getAge() - p2.getAge() > 0)
            return true;
        else
            return false;
    }
}
