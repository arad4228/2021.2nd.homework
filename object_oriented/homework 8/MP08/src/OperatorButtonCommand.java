import javax.swing.*;

public class OperatorButtonCommand implements Command{
    private Calculator calculator;
    private JButton cmdButton;
    private JLabel display;

    OperatorButtonCommand(Calculator calculator, JButton cmdButton, JLabel display)
    {
        this.calculator = calculator;
        this.cmdButton = cmdButton;
        this.display = display;
    }
    @Override
    public void execute()
    {
        if (calculator.isOperand1Set()) {  // 첫 번째 피연산자 값이 지정되어야만 연산자 처리 가능
            calculator.setOperatorSet(true);
            calculator.setOperator(cmdButton.getText().charAt(0));
        }
    }
}
