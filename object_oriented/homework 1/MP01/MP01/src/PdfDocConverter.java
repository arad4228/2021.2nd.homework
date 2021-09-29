
public class PdfDocConverter extends DocConverter{

	public PdfDocConverter() {
		super("pdf");
		}
	@Override
	public void save(String fileName) {
		System.out.println(fileName + this.getExtension() + "으로 저장합니다.");
	}
}
