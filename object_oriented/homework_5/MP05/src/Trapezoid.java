import java.awt.*;

public class Trapezoid extends Shape{

    Trapezoid(String type, Point[] points) {
        super(type, points);
    }
    @Override
    public double calcArea() {
        double area = ((points[3].getX() - points[0].getX()) + (points[1].getX() - points[2].getX())) * (points[0].getY() - points[2].getY());
        return area;
    }
}
