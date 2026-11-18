public class Main {
    // Fixed numbers of the project
    static final int START_STOCK = 30;
    static final int THREADS = 4;
    static final int ATTEMPTS = 9;
    static final int COUNTERS = 3;
    static final int SLEEP_MS = 10;

    // Shared static variables
    static int stock = START_STOCK;
    static int served = 0;
    static int rejected = 0;

    static int busy = 0;       // counters in use right now
    static int maxBusy = 0;    // the biggest value busy ever had

    static MySemaphore mutex = new MySemaphore(1);              // binary: protects the variables
    static MySemaphore stations = new MySemaphore(COUNTERS);    // counting: the 3 counters

    public static void main(String[] args) throws InterruptedException {
        Shopper[] shoppers = new Shopper[THREADS];

        for (int i = 0; i < THREADS; i++) {
            shoppers[i] = new Shopper(i + 1);   // id 1 to 4
            shoppers[i].start();
        }
        for (int i = 0; i < THREADS; i++) {
            shoppers[i].join();
        }

        System.out.println("Loaves sold: " + served);
        System.out.println("Customers without bread: " + rejected);
        System.out.println("Loaves left: " + stock);
        System.out.println("Most counters busy at the same time: " + maxBusy);
    }
}
