public class Movie  {
    private String name;
    private int makeDay;
    private int price;
    private String option;

    public void setName(String name) {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public int getProductionYear() {
        return makeDay;
    }

    public void setMakeDay(int makeDay) {
        this.makeDay = makeDay;
    }

    public int getPrice() {
        return price;
    }

    public Movie(String name, int makeDay, int price, String option)
    {
        this.name = name;
        this.makeDay = makeDay;
        this.price = price;
        this.option = option;
    }
    public boolean equals(Movie m1, Movie m2)
    {
        return m1.getName().equals(m2.getName());
    }
    public String toString()
    {
        return "영화: " + this.name + ", 가격: " + this.price + "원";
    }


}
