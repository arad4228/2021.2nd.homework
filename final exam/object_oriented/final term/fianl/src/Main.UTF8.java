// 첫번쨰 템플릿 메쇼드를 활룡해서 중복되는 알고리즘을 하나의 객체에 담아서 작성을 하고 개별적인 알고리즘을 따로 구현하는 식으로
// 작성하면 될 꺼같습니다. movie나 모든 라이브러리, list가 동일한 알고리즘을 가지고 있기에 해당 패텅읋 통해 같은 부분은
// 같이 정의해서 사용하고 틀린부분을 고치면 될꺼같습니다.
// 두번쨰는 퍼사드를 통해서  하나의 인터페이스를 만들어 묶어서 제공하는 갓이 옳다고 생각됩니다.
// 퍼사드는 여러 인터페이스를 모아서 하나의 통합인터페이스를 제공하므로 영화나 라이브러리, list에 대해서 모두 모아서 만들고 개별을
// 접근할때는 각각의 객체를 통해 접근하면 될꺼같습니다.
import java.util.Random;

public class Main {
    final int MIN_PRICE = 1000;   // 판매 영화(혹은 시리즈) 최소 가격
    final int MAX_PRICE = 10000;  // 판매 영화(혹은 시리즈) 최대 가격
    final int DIFF = MAX_PRICE - MIN_PRICE;

    // Random클래스 사용할 때 항상 같은 패턴이 나오도록 하기 위해, seed를 고정시킴
    Random random = new Random(MAX_PRICE);

    private int getRandomPrice() {
        return random.nextInt(DIFF) + MIN_PRICE;
    }

    public MovieSeries createMovieSeries(String name, String[] movieNames,
                                         int[] years, double discountRate) {
        // 새로운 시리즈 생성
        MovieSeries ms = new MovieSeries(name, 2021, discountRate);

        // 시리즈에 주어진 영화 제목 및 제작년도 추가
        for (int i = 0; i < movieNames.length; i++) {
            ms.add(new Movie(movieNames[i], years[i], getRandomPrice(), "HD"));
        }

        // 시리즈에 첫 번째 영화를 다시 추가해봄(가격과 해상도가 달라지고, 기존 영화 교체)
        ms.add(new Movie(movieNames[0], years[0], getRandomPrice(), "4K"));
        return ms;
    }

    // 라이브러리 생성
    public MovieLibrary createLibraryAndAddMovies() {
        final String LIBRARY_NAME = "MyLibrary";

        String[] movieNames = { "Movie1", "Movie2", "Movie3" };
        String[] movieSeriesNames = { "Awesome Movie 1", "Awesome Movie 2", "Awesome Movie 3",
                "Awesome Movie 4" };
        int[] productionYears = { 2013, 2015, 2018, 2021 };

        System.out.println("***** 라이브러리 생성하고 영화 추가 *****");
        MovieLibrary library = new MovieLibrary(LIBRARY_NAME);
        for (int i = 0; i < movieNames.length; i++) {
            library.insert(new Movie(movieNames[i], productionYears[i], getRandomPrice(), "4K"), false);
        }
        System.out.println("***** 시리즈 생성해서 라이브러리에 추가 *****");
        MovieSeries ms = createMovieSeries("Awesome Movie Series", movieSeriesNames, productionYears, 30);
        System.out.printf("***** 시리즈의 세 번째 영화가 %s인지 확인 *****\n", movieSeriesNames[2]);
        Movie f = (Movie) ms.getMovie(2);
        System.out.printf("세 번째 영화 제목: %s, 제작 년도: %d\n", f.getName(), f.getProductionYear());

        System.out.println("***** 시리즈에서 세 번째 영화 삭제 *****");
        ms.remove(ms.getMovie(2));

        System.out.println("***** 시리즈에 있는 영화 목록 출력 *****");
        ms.list();

        System.out.println("***** 라이브러리에 영화 시리즈 추가 *****");
        library.insert(ms, false);
        return library;
    }

    public void deleteMovieFromLibrary(MovieLibrary library) {
        System.out.println("***** 라이브러리에 없는 영화 삭제 *****");
        Movie movie = new Movie("New Movie", 1984, 0, "HD");
        library.delete(movie);
        System.out.println("***** 라이브러리에 있는 영화 삭제 *****");
        movie = new Movie("Movie1", 2013, 0, "HD");
        library.delete(movie);
    }

    public void printLibrary(MovieLibrary library) {
        System.out.println("***** 라이브러리 내용 출력해보기 *****");
        System.out.println(library);
    }

    public static void main(String[] args) {
        Main m = new Main();

        MovieLibrary library = m.createLibraryAndAddMovies();
        m.deleteMovieFromLibrary(library);
        m.printLibrary(library);
    }
}
