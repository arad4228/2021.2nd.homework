import java.util.ArrayList;
// subject

public class PrimeObservableThread implements Runnable {
    private static final int SLEEPTIME = 500;

    private int primeNumber;
    private int numCount;
    private boolean first = true;
    private boolean stopRunning = false;
    private ArrayList<Observer> observers;

    public PrimeObservableThread() {
        this.observers = new ArrayList<Observer>();
    }

    public void registerObserver(Observer s)
    {
        this.observers.add(s);
    }

    public void removeObserver(Observer s)
    {
        int i = this.observers.indexOf(s);
        if(i >= 0)
        {
            observers.remove(i);
        }
    }

    public void notifyObserver(int primeNumber)
    {
        for(Observer observer :observers)
        {
            observer.update(primeNumber);
        }
    }

    public int getPrimeNumber() {
        return primeNumber;
    }

    public void stopRunning() {
        stopRunning = true;
    }

    public void startRunning() {
        stopRunning = false;
        run();
    }

    private void generatePrimeNumber() {
        while (stopRunning == false) {
            if (first) {
                first = false;
                primeNumber = 2;   // 첫 번째 소수는 2
                System.out.println(primeNumber);
                numCount = 1; // 다음 단계부터는 2를 더해서 홀수만 확인하므로 1로 바꿔서 다음 숫자를 3으로 만들어야 함
            } else {
                numCount += 2; // 2를 제외한 짝수는 소수가 될 수 없음. 따라서 홀수만 검사
                if (isPrimeNumber(numCount)) {
                    primeNumber = numCount;
                    // 소수를 생성함.여기서 옵저버에게 알려줘야함
                    notifyObserver(numCount);
                }
            }
            try {
                Thread.sleep(SLEEPTIME); // 1초 쉼
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isPrimeNumber(int n) {
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        generatePrimeNumber();
    }
}
