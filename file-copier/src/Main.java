public class Main {
    public static void main(String[] args) {
        run();
        // Sleep for five seconds
        try {
            while(true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException ignored) {
        }
        System.out.println("Done.");
    }

    private static void run(){
        // Create runnable action for daemon
        Runnable daemonRunner = new Runnable() {
            public void run() {
                // Repeat forever
                while (true) {
                    System.out.println("I'm a Daemon.");
                    // Sleep for half a second
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        };
        // Create and start daemon thread
        Thread daemonThread = new Thread(daemonRunner);
        daemonThread.setDaemon(true);
        daemonThread.start();
    }
}
