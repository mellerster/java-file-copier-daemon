import java.io.File;
import java.io.IOException;
import java.util.Map;


public class Main {
    private static String srcDir = "C:\\Users\\Diego\\Nextcloud\\SubidasInstantáneas";
    private static String checkDir = "E:\\Diego\\Nextcloud";
    public static void main(String[] args) {
        /*run();
        // Sleep for five seconds
        try {
            while(true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException ignored) {
        }
        System.out.println("Done.");*/
        Compare compare = new Compare();
        Copy copy = new Copy();
		try
		{
            File dir1 = new File(srcDir);
            File dir2 = new File(checkDir);
			Map<String,File> files = compare.getDiff(dir1,dir2);
            System.out.println("Done comparing, copying");
            copy.copyFiles(files, srcDir, checkDir);      
            System.out.println("Done copying");
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
    }

    private static void run(){
        // Create runnable action for daemon
        Runnable daemonRunner = new Runnable() {
            public void run() {
                // Repeat forever
                while (true) {
                    /*DirectoryComparer dirDiff = new DirectoryComparer(srcDir,".+","*");
                    Set<String> ret = dirDiff.compareDir(checkDir);
                    System.out.println(ret.size());*/
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
