public class StateFirstOperand implements State {
    CalcV1 calcV1;

    public StateFirstOperand(CalcV1 calcV1) {
        this.calcV1 = calcV1;
    }

    @Override
    public void processOperator(char op) {
        calcV1.setOperator(op);
        calcV1.setState(calcV1.getOperatorState());
    }
}
