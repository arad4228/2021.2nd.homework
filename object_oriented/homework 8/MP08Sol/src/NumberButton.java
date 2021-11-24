public class NumberButton extends CommandButton {
    CalcGUIV1 display;

    public NumberButton(Calculator calculator, CalcGUIV1 display) {
        super(calculator);
        this.display = display;
    }

    @Override
    public void execute() {
        Calculator calculator = getCalculator();
        if (calculator.isOperand1Set() && calculator.isOperatorSet()) { // 첫 번째 피연산자와 연산자가 지정되었다면 두 번째 피연산자 값으로 사용
            int num2 = Integer.parseInt(getText());
            calculator.setOperand2(num2);
            display.showText("" + num2);
            calculator.setOperand2Set(true);
        }
        else {  // 첫 번째 피연산자 값 지정
            int num1 = Integer.parseInt(getText());
            display.showText("" + num1);
            calculator.setOperand1(num1);
            calculator.setOperand1Set(true);
        }
    }
}
