package threads.util;

/**
 * Utility class for thread-related operations.
 * This class provides common functionality used across the threading examples.
 */
public class ThreadUtil {

    /**
     * Checks if the current Java version supports virtual threads (Java 21+).
     * 
     * @return true if virtual threads are supported, false otherwise
     */
    public static boolean supportsVirtualThreads() {
        return Runtime.version().feature() >= 21;
    }
    
    /**
     * Prints a message indicating that virtual threads require Java 21 or later.
     * This is used when a virtual thread example is run on an older Java version.
     */
    public static void printVirtualThreadsRequirement() {
        System.out.println("Virtual threads require Java 21 or later.");
        System.out.println("Current Java version: " + Runtime.version());
    }
    
    /**
     * Waits for the specified number of milliseconds.
     * This is a convenience method to handle InterruptedException.
     * 
     * @param millis the time to wait in milliseconds
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
    }
}