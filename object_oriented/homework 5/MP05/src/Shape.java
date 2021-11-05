import java.awt.*;

public abstract class Shape {
    protected  Point[] points;
    private String type;
    public Shape(String type, Point[] points)
    {
        this.type = type;
        this.points = points;
    }

    @Override
    public String toString()
    {
        String restr = this.type + "\n";
        for (int i = 0 ; i < points.length; i++) {
            restr = restr + ("P" + i + ": java.awt.Point[x =" + (int)points[i].getX() + ", y =" + (int)points[i].getY() + "]\n");
        }
        restr = restr + "area: "+ calcArea()+ "\n";
        return restr;
    }

    public abstract double calcArea();


}
