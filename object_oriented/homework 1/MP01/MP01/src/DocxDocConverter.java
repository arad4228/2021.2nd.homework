public class DocxDocConverter extends DocConverter{
	
	public DocxDocConverter() {
		super("docx");
	}

	@Override
	public void save(String fileName) {
		System.out.println(fileName + this.getExtension() + "���� �����մϴ�.");
	}

}
