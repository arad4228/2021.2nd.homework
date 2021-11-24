public class SecondOperator implements State{
    private CalcV1 calcV1;
    private char operator;

    SecondOperator(CalcV1 calcV1)
    {
        this.calcV1 = calcV1;
    }
    @Override
    public void Operation(String context)
    {
        if (calcV1.getCurrentStates() == calcV1.getSecondOperand())
        {
            this.operator = context.charAt(0);
            if (this.operator == '=')
            {
                calcV1.printOutResult();
                calcV1.setState(calcV1.getSTART());
            }
        }
    }
}
