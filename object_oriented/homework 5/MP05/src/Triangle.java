import java.awt.*;
import java.lang.Math;

public class Triangle extends Shape{

    Triangle(String type, Point[] points) {
        super(type,points);
    }

    private double distance(Point p1, Point p2)
    {
        double distance = Math.sqrt(Math.pow((p2.getX() - p1.getX()),2) + Math.pow((p2.getY() - p1.getY()),2));
        return distance;
    }

    @Override
    public double calcArea() {
        double a = distance(points[0], points[1]);
        double b = distance(points[2], points[0]);
        double c = distance(points[2], points[1]);
        double s = (a+b+c)/2;
        double area = Math.sqrt(s*(s-a)*(s-b)*(s-c));
        return area;
    }
}
