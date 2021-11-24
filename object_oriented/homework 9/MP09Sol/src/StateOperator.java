public class StateOperator implements State {
    CalcV1 calcV1;

    public StateOperator(CalcV1 calcV1) {
        this.calcV1 = calcV1;
    }

    @Override
    public void processNumber(int num) {
        calcV1.setOperand2(num);
        calcV1.setState(calcV1.getSecondOperandState());
    }
}
