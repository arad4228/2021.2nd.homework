import javax.swing.*;
import java.awt.*;

public abstract class Display extends Component {
    private int height;
    private int width;

    public Display(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public abstract JPanel create();
    public abstract void show();
}
