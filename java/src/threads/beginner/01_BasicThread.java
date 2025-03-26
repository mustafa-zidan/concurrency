package threads.beginner;

import threads.util.ThreadUtil;

/**
 * This file demonstrates the Basic Thread model in Java.
 * 
 * USE CASE:
 * The Basic Thread model is suitable for:
 * - Simple applications with a small number of threads
 * - Educational purposes to understand threading fundamentals
 * - Legacy code that already uses this pattern
 * - When you need full control over thread behavior (priority, daemon status, etc.)
 * - Scenarios where the task and its execution are naturally coupled
 * 
 * LOW-LEVEL EXPLANATION:
 * When you create a Thread in Java:
 * 1. The JVM allocates memory for the thread's stack (typically 1MB by default)
 * 2. The OS creates a native thread which is mapped 1:1 with the Java thread
 * 3. The thread is registered with the JVM's thread management system
 * 4. When start() is called, the JVM signals the OS to schedule the thread for execution
 * 5. The OS scheduler determines when the thread runs based on priority and available CPU cores
 * 6. The thread executes the run() method in a separate call stack
 * 7. When run() completes, the thread dies and resources are eventually reclaimed
 * 
 * This approach is simple but has limitations:
 * - It doesn't allow for inheritance from other classes (Java only supports single inheritance)
 * - It tightly couples the task with the thread execution mechanism
 * - Each thread consumes significant system resources (memory for stack, OS resources)
 * - There's overhead in context switching between threads
 */

class BasicThread {

    public static void main(String[] args) {
        System.out.println("Java Basic Thread Example");
        System.out.println("=========================");

        // Run examples
        basicThreadExample();
        virtualThreadExample(); // Java 21 feature
    }

    /**
     * Basic Thread Class
     * 
     * The most fundamental way to create a thread in Java is by extending the Thread class.
     */
    private static void basicThreadExample() {
        System.out.println("1. Basic Thread Example");

        // Create a custom thread by extending Thread class
        class MyThread extends Thread {
            @Override
            public void run() {
                System.out.println("Thread ID: " + Thread.currentThread().threadId() +
                                   " is running in MyThread class");
            }
        }

        // Create and start 3 threads
        for (int i = 0; i < 3; i++) {
            MyThread thread = new MyThread();
            thread.start(); // This calls the run() method in a new thread
        }

        // Wait a moment for threads to complete
        ThreadUtil.sleep(500);
        System.out.println();
    }

    /**
     * Virtual Threads (Java 21+)
     * 
     * USE CASE:
     * Virtual threads are ideal for:
     * - High-throughput server applications handling many concurrent connections
     * - Microservices that need to process many requests simultaneously
     * - Applications with many I/O-bound operations (database queries, network calls, file operations)
     * - Scenarios where you need to scale to thousands or millions of concurrent tasks
     * - Replacing complex asynchronous callback-based code with simpler sequential code
     * 
     * LOW-LEVEL EXPLANATION:
     * Virtual threads work fundamentally differently from platform threads:
     * 1. They are managed by the JVM, not the OS
     * 2. They don't have a fixed 1:1 mapping to OS threads - many virtual threads share a pool of carrier threads
     * 3. When a virtual thread performs a blocking operation:
     *    a. The JVM detects the blocking call
     *    b. It unmounts the virtual thread from its carrier thread
     *    c. The carrier thread is free to run other virtual threads
     *    d. When the blocking operation completes, the virtual thread is scheduled to continue
     * 4. Virtual threads use a technique called "continuation" to save and restore their execution state
     * 5. They have a much smaller memory footprint (few KB vs ~1MB for platform threads)
     * 6. Context switching between virtual threads is much cheaper than between platform threads
     * 
     * Advantages:
     * - Much lighter weight than platform threads (can create millions of them)
     * - Managed by the JVM rather than the OS
     * - Efficient for I/O-bound applications
     * - Use the same Thread API, making migration easy
     * - Allow writing simple sequential code that scales well
     */
    private static void virtualThreadExample() {
        System.out.println("2. Virtual Thread Example (Java 21+)");

        try {
            // Check if we're running on Java 21 or later
            if (!ThreadUtil.supportsVirtualThreads()) {
                ThreadUtil.printVirtualThreadsRequirement();
                return;
            }

            // Create a virtual thread using the builder
            Thread vThread = Thread.ofVirtual().name("virtual-thread").start(() -> {
                System.out.println("Running in a virtual thread!");
                System.out.println("Thread name: " + Thread.currentThread().getName());
                System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
            });

            // Wait for the virtual thread to complete
            vThread.join();

            // Create multiple virtual threads
            System.out.println("\nCreating multiple virtual threads:");

            // Using Thread.Builder
            Thread.Builder builder = Thread.ofVirtual().name("virtual-", 0);

            // Create and start 5 virtual threads
            Thread[] threads = new Thread[5];
            for (int i = 0; i < threads.length; i++) {
                final int id = i;
                threads[i] = builder.start(() -> {
                    System.out.println("Virtual thread " + id + " is running");
                    ThreadUtil.sleep(100); // Simulate some work
                    System.out.println("Virtual thread " + id + " is done");
                });
            }

            // Wait for all virtual threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
    }
}
