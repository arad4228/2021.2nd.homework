public class HardDrive extends AbstractExternalDevice{
    private AbstractNotebookComputer computer;
    private double externalSpace = 40.0;

    public HardDrive(String type ,AbstractNotebookComputer computer) {
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
        return computer.toString() +", " + super.toString();
    }
}
