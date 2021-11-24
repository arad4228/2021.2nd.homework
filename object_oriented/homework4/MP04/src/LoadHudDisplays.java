import java.io.*;
import java.util.ArrayList;

public class LoadHudDisplays implements LoadDisplayInterface{
    private ArrayList<String> displays = new ArrayList<String>();
    private String display;

    LoadHudDisplays(String filename)
    {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filename));
            while((display = fileReader.readLine()) != null)
                this.displays.add(display);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public ArrayList<String> load() {
        return this.displays;
    }
}
