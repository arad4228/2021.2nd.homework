
import java.util.Map;
import java.util.HashMap;

public class WordProcessor{
	private ISpellChecker spellChecker;
	private Map<String, DocConverter> docConverters = new HashMap<String, DocConverter>();
	private String filename;
	
	WordProcessor(){};
	
	public WordProcessor(String fileName) {
		this.filename = fileName;
	}
	public void addDocConverter(DocConverter converter)
	{
		this.docConverters.put(converter.getExtension(), converter);
	}
	public void convertDocTo(String ext)
	{
		if(this.docConverters.containsKey(ext))
		{
			System.out.println("new doc."+ ext+ "로 변환해서 저장합니다.");
		}
		else
		{
			System.out.println(ext+"파일 형식을 지원하는 DocConverter가 없습니다.");
		}
		
	}
	public void setSpellChecker(ISpellChecker spellChecker)
	{
		this.spellChecker = spellChecker;
	}
	public void checkSpelling()
	{
		this.spellChecker.check();
	}
}
