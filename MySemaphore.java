// Our own semaphore, built only with synchronized, wait() and notifyAll().
// (java.util.concurrent is not allowed in this lab.)
public class MySemaphore {
    private int permits;

    public MySemaphore(int permits) {
        this.permits = permits;
    }

    public synchronized void acquire() {
        // while, not if: after wait() the thread must check permits again
        while (permits == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        permits--;
    }

    public synchronized void release() {
        permits++;
        notifyAll();   // wake the sleeping threads so they can check again
    }
}
