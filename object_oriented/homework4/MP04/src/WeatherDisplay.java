import javax.swing.*;
import java.awt.*;

public class WeatherDisplay extends DisplayDecorator {
    JPanel weather_Panel;
    LabelPanel weather_Label;
    Display displayComponent;

    WeatherDisplay(Display display, int width, int height)
    {
        super(display, width, display.getHeight() + height);
        displayComponent = display;
    }

    @Override
    public JPanel create() {
        //새로운 날씨 패널을 위한 패널 생성
        weather_Panel = new JPanel();
        weather_Panel.setLayout(new BoxLayout(weather_Panel, BoxLayout.Y_AXIS));

        weather_Panel.setMinimumSize(new Dimension(getWidth(), getHeight()));
        weather_Panel.setPreferredSize(new Dimension(getWidth(), getHeight()));
        weather_Panel.add(displayComponent.create());
        displayComponent.show();

        weather_Label = new LabelPanel();
        weather_Panel.add(weather_Label.createPanel(getWidth(), getHeight()));
        return weather_Panel;
    }

    @Override
    public void show() {
        weather_Label.updateText("온도: 20도, 습도: 60");
    }
}
