public class Keyboard extends AbstractExternalDevice{
    private AbstractNotebookComputer computer;
    private double externalSpace = 80.0;

    public Keyboard(String type, AbstractNotebookComputer computer) {
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
