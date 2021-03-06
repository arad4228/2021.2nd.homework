import java.util.Date;

public class FileInfo {
    private String name;
    private String type;
    private int size;
    private Date modifiedDate;

    public FileInfo(String name, String type, int size, Date modified)
    {
        this.name = name;
        this.type = type;
        this.size = size;
        this.modifiedDate = modified;
    }
    public String getName()
    {
        return this.name;
    }
    public String getType()
    {
        return this.type;
    }
    public int getSize()
    {
        return this.size;
    }
    public Date getModifiedDate()
    {
        return this.modifiedDate;
    }
    public String toString()
    {
        String str = ("name: "+ this.name +"\n" + "type: " + this.type + "\n"+
                "size: " + this.size + "\n"+ "Date: " + this.modifiedDate);
        return str;
    }
}