public class Operator implements State{
    private CalcV1 calcV1;

    Operator(CalcV1 calcV1)
    {
        this.calcV1 = calcV1;
    }
    @Override
    public void Operation(String context)
    {
        if (calcV1.getCurrentStates() == calcV1.getOPERATOR())
        {
            calcV1.setOperand2(Integer.parseInt(context));
            calcV1.setState(calcV1.getSecondOperand());
        }
    }
}
