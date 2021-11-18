public class FirstOperand implements State{
    private CalcV1 calcV1;
    private char operator;

    FirstOperand(CalcV1 calcV1)
    {
        this.calcV1 = calcV1;
    }
    @Override
    public void Operation(String context)
    {
        this.operator = context.charAt(0);
        if (calcV1.getCurrentStates() == calcV1.getFirstOperand())
        {
            calcV1.setState(calcV1.getOPERATOR());
            calcV1.setOperator(operator);
        }
    }
}
