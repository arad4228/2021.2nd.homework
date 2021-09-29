
public class CompareSize implements Comparable{
	
	@Override
	public int compareTo(Object o1, Object o2) {
		FileInfo f1 = (FileInfo) o1;
		FileInfo f2 = (FileInfo) o2;
		if(f1.getSize() - f2.getSize() > 0)
			return 1;
		else if(f1.getSize() - f2.getSize() == 0)
			return 0;
		else
			return -1;
	}
}
