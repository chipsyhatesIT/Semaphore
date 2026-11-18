// One customer of the bakery. Each Shopper makes 9 attempts to take a loaf.
class Shopper extends Thread {
    private int id;

    public Shopper(int id) {
        this.id = id;
    }

    public void run() {
        for (int attempt = 1; attempt <= Main.ATTEMPTS; attempt++) {

            // (a) take a counter (permit of the counting semaphore)
            Main.stations.acquire();

            // (b) one more counter is busy; update the record
            Main.mutex.acquire();
            try {
                Main.busy++;
                if (Main.busy > Main.maxBusy) {
                    Main.maxBusy = Main.busy;
                }
            } finally {
                Main.mutex.release();
            }

            // (c) the attempt: check stock and change it in ONE critical section
            Main.mutex.acquire();
            try {
                if (Main.stock > 0) {
                    Main.stock--;
                    Main.served++;
                } else {
                    Main.rejected++;
                }
            } finally {
                Main.mutex.release();
            }

            // (d) serving takes time; the counter permit is still held
            try {
                Thread.sleep(Main.SLEEP_MS);
            } catch (InterruptedException e) {
            }

            // (e) this counter is free again
            Main.mutex.acquire();
            try {
                Main.busy--;
            } finally {
                Main.mutex.release();
            }

            // (f) give the counter back
            Main.stations.release();
        }
    }
}
