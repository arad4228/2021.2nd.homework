public class NotebookComputer extends AbstractNotebookComputer{
    private String owner;

    public NotebookComputer(String name) {
        this.owner = name;
    }

    public String toString() {
        return owner+"'s Notebook Computer";
    }

    @Override
    public double requiredSpace() {
        return 250.0;
    }
}
