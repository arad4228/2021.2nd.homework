
public class OdtDocConverter extends DocConverter{

	public OdtDocConverter() {
		super("odt");
	}

	@Override
	public void save(String fileName) {
		System.out.println(fileName + this.getExtension() + "으로 저장합니다.");
	}
}

