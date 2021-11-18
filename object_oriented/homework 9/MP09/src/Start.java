public class Start implements State {
    private CalcV1 calcV1;

    Start(CalcV1 calcV1)
    {
        this.calcV1 = calcV1;
    }

    @Override
    public void Operation(String context) {
        if (calcV1.getCurrentStates() == calcV1.getSTART())
        {
            calcV1.setOperand1(Integer.parseInt(context));
            calcV1.setState(calcV1.getFirstOperand());
        }
    }
}