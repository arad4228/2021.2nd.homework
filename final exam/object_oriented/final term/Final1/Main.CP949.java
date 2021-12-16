import java.util.Random;

public class Main {
    final int MIN_PRICE = 1000;   // �Ǹ� ��ȭ(Ȥ�� �ø���) �ּ� ����
    final int MAX_PRICE = 10000;  // �Ǹ� ��ȭ(Ȥ�� �ø���) �ִ� ����
    final int DIFF = MAX_PRICE - MIN_PRICE;

    // RandomŬ���� ����� �� �׻� ���� ������ �������� �ϱ� ����, seed�� ������Ŵ
    Random random = new Random(MAX_PRICE);

    private int getRandomPrice() {
        return random.nextInt(DIFF) + MIN_PRICE;
    }

    public MovieSeries createMovieSeries(String name, String[] movieNames,
                                         int[] years, double discountRate) {
        // ���ο� �ø��� ����
        MovieSeries ms = new MovieSeries(name, 2021, discountRate);

        // �ø�� �־��� ��ȭ ���� �� ���۳⵵ �߰�
        for (int i = 0; i < movieNames.length; i++) {
            ms.add(new Movie(movieNames[i], years[i], getRandomPrice(), "HD"));
        }

        // �ø�� ù ��° ��ȭ�� �ٽ� �߰��غ�(���ݰ� �ػ󵵰� �޶�����, ���� ��ȭ ��ü)
        ms.add(new Movie(movieNames[0], years[0], getRandomPrice(), "4K"));
        return ms;
    }

    // ���̺귯�� ����
    public MovieLibrary createLibraryAndAddMovies() {
        final String LIBRARY_NAME = "MyLibrary";

        String[] movieNames = { "Movie1", "Movie2", "Movie3" };
        String[] movieSeriesNames = { "Awesome Movie 1", "Awesome Movie 2", "Awesome Movie 3",
                "Awesome Movie 4" };
        int[] productionYears = { 2013, 2015, 2018, 2021 };

        System.out.println("***** ���̺귯�� �����ϰ� ��ȭ �߰� *****");
        MovieLibrary library = new MovieLibrary(LIBRARY_NAME);
        for (int i = 0; i < movieNames.length; i++) {
            library.insert(new Movie(movieNames[i], productionYears[i], getRandomPrice(), "4K"), false);
        }
        System.out.println("***** �ø��� �����ؼ� ���̺귯���� �߰� *****");
        MovieSeries ms = createMovieSeries("Awesome Movie Series", movieSeriesNames, productionYears, 30);
        System.out.printf("***** �ø����� �� ��° ��ȭ�� %s���� Ȯ�� *****\n", movieSeriesNames[2]);
        Movie f = (Movie) ms.getMovie(2);
        System.out.printf("�� ��° ��ȭ ����: %s, ���� �⵵: %d\n", f.getName(), f.getProductionYear());

        System.out.println("***** �ø���� �� ��° ��ȭ ���� *****");
        ms.remove(ms.getMovie(2));

        System.out.println("***** �ø�� �ִ� ��ȭ ��� ��� *****");
        ms.list();

        System.out.println("***** ���̺귯���� ��ȭ �ø��� �߰� *****");
        library.insert(ms, false);
        return library;
    }

    public void deleteMovieFromLibrary(MovieLibrary library) {
        System.out.println("***** ���̺귯���� ���� ��ȭ ���� *****");
        Movie movie = new Movie("New Movie", 1984, 0, "HD");
        library.delete(movie);
        System.out.println("***** ���̺귯���� �ִ� ��ȭ ���� *****");
        movie = new Movie("Movie1", 2013, 0, "HD");
        library.delete(movie);
    }

    public void printLibrary(MovieLibrary library) {
        System.out.println("***** ���̺귯�� ���� ����غ��� *****");
        System.out.println(library);
    }

    public static void main(String[] args) {
        Main m = new Main();

        MovieLibrary library = m.createLibraryAndAddMovies();
        m.deleteMovieFromLibrary(library);
        m.printLibrary(library);
    }
}
