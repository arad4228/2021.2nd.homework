import javax.swing.*;
import java.awt.*;

public class TimeDisplay extends DisplayDecorator{
    JPanel time_Panel;
    LabelPanel time_Label;
    Display displayComponent;

    TimeDisplay(Display display, int width, int height)
    {
        super(display,width, display.getHeight()+height);
        displayComponent = display;
    }
    @Override
    public JPanel create() {
        time_Panel = new JPanel();
        time_Panel.setLayout(new BoxLayout(time_Panel, BoxLayout.Y_AXIS));

        time_Panel.setMinimumSize(new Dimension(getWidth(),getHeight()));
        time_Panel.setPreferredSize(new Dimension(getWidth(),getHeight()));
        time_Panel.add(displayComponent.create());
        displayComponent.show();

        time_Label = new LabelPanel();
        time_Panel.add(time_Label.createPanel(getWidth(),100));
        return time_Panel;
    }

    @Override
    public void show() {
        time_Label.updateText(java.time.LocalDateTime.now().toString());
    }

}
