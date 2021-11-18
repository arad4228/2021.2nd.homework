import javax.swing.*;

public abstract class CommandButton extends JButton implements Command {
    private Calculator calculator;

    public CommandButton(Calculator calculator) {
        super();
        this.calculator = calculator;
    }

    public Calculator getCalculator() { return calculator; }

    @Override
    public abstract void execute();
}
