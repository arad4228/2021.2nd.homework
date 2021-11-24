public class StateSecondOperand implements State  {
    CalcV1 calcV1;

    public StateSecondOperand(CalcV1 calcV1) {
        this.calcV1 = calcV1;
    }

    @Override
    public void processOperator(char op) {
        if (op == '=') {
            calcV1.printOutResult();
            calcV1.setState(calcV1.getStartState());
        }
    }
}
