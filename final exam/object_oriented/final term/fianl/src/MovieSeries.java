import java.util.ArrayList;
import java.util.Iterator;

public class MovieSeries{
    private String name;
    private int makeDay;
    private double discount;
    private ArrayList moviecomponents = new ArrayList();

    public MovieSeries(String name, int makeDay, double discount) {
        this.name = name;
        this.makeDay = makeDay;
        this.discount = discount;
    }
    public  Boolean add(Movie ms)
    {
        return this.moviecomponents.add(ms);
    }

    public boolean remove(Movie ms)
    {
        return moviecomponents.remove(ms);
    }
    public Movie getMovie(int index)
    {
        return (Movie) moviecomponents.get(index);
    }
    public boolean equals(MovieSeries ms1, MovieSeries ms2)
    {
    }
    public void list()
    {
        Iterator it = moviecomponents.iterator()
        while(it.hasNext())
        {
            Movie componet = (Movie) it.next();
            componet.toString();
        }
    }
}
