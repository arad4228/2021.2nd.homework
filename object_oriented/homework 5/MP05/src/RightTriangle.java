import java.awt.*;

public class RightTriangle extends Shape{

    RightTriangle(String type, Point[] points) {
        super(type,points);
    }

    @Override
    public double calcArea() {
        double area = ((points[0].getY() - points[2].getY())* (points[2].getX() - points[1].getX()) / 2);
        return area;
    }

}
