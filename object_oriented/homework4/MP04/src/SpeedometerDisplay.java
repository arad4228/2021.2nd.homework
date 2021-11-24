import javax.swing.*;
import java.awt.*;

public class SpeedometerDisplay extends DisplayDecorator{
    JPanel speedometer_Panel;
    LabelPanel speedometer_Label;
    Display displayComponent;

    SpeedometerDisplay(Display display, int width, int height)
    {
        super(display,width, display.getHeight()+height);
        displayComponent = display;
    }
    @Override
    public JPanel create() {
        speedometer_Panel = new JPanel();
        speedometer_Panel.setLayout(new BoxLayout(speedometer_Panel, BoxLayout.Y_AXIS));
        speedometer_Panel.setMinimumSize(new Dimension(getWidth(),getHeight()));
        speedometer_Panel.setPreferredSize(new Dimension(getWidth(),getHeight()));
        speedometer_Panel.add(displayComponent.create());
        displayComponent.show();

        speedometer_Label = new LabelPanel();
        speedometer_Panel.add(speedometer_Label.createPanel(getWidth(),100));
        return speedometer_Panel;
    }

    @Override
    public void show() {
        speedometer_Label.updateText("Speed: 50");
    }


}
