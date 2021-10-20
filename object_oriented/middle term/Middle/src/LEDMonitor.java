public class LEDMonitor extends AbstractExternalDevice{
    private AbstractNotebookComputer computer;
    private double externalSpace = 150.0;

    public LEDMonitor(String type, AbstractNotebookComputer computer) {
        super(type);
        this.computer = computer;
    }

    @Override
    public double requiredSpace() {
        return computer.requiredSpace() + externalSpace;
    }
    @Override
    public String toString()
    {
        return computer.toString() +", " +  super.toString();
    }
}
