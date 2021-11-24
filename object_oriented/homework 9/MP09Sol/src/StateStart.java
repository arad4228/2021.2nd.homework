public class StateStart implements State {
    CalcV1 calcV1;

    public StateStart(CalcV1 calcV1) {
        this.calcV1 = calcV1;
    }

    @Override
    public void processNumber(int num) {
        calcV1.setOperand1(num);
        calcV1.setState(calcV1.getFirstOperandState());
    }
}
