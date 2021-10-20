public abstract class AbstractExternalDevice extends AbstractNotebookComputer{
    String deviceType;

    public  AbstractExternalDevice(String type){
        this.deviceType = type;
    }
    public String toString()
    {
        return deviceType;
    }
}
