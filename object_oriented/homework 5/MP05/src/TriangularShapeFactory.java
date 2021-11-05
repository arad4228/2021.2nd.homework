import java.awt.*;

public class TriangularShapeFactory implements ShapeFactory{
    @Override
    public Shape create(String type, Point[] points) {
        Shape shape = null;
        if(type.equals("Triangle"))
            shape = new Triangle(type, points);
        else if (type.equals("RightTriangle"))
            shape = new RightTriangle(type,points);

        return shape;
    }
}
