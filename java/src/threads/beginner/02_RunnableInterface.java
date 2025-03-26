package threads.beginner;

import threads.util.ThreadUtil;

/**
 * This file demonstrates the Runnable Interface model in Java.
 * 
 * USE CASE:
 * The Runnable Interface model is ideal for:
 * - Most production applications that need threading
 * - When you need to separate the task logic from thread management
 * - When your task class needs to extend another class
 * - When the same task needs to be executed by multiple threads
 * - When you plan to use thread pools or executors
 * - Code that needs to be maintainable and testable
 * 
 * LOW-LEVEL EXPLANATION:
 * The Runnable interface is a core part of Java's concurrency model:
 * 1. Runnable is a functional interface with a single method: void run()
 * 2. When you pass a Runnable to a Thread constructor:
 *    a. The Thread object stores the Runnable as a target
 *    b. When the thread starts, it calls the run() method of its target Runnable
 *    c. The Runnable's code executes in the context of the new thread
 * 3. This creates a clean separation between:
 *    a. The task logic (defined in the Runnable)
 *    b. The threading mechanics (handled by the Thread class)
 * 4. Under the hood, the Thread class's own run() method checks if it has a target Runnable
 *    and delegates to it if present
 * 
 * Advantages:
 * - Separates the task (what to run) from the thread (how to run)
 * - Allows a class to extend from another class while still being runnable in a thread
 * - Can be used with thread pools and executors
 * - Promotes code reuse and better design
 * - Works well with Java 8+ lambda expressions and method references
 */

class RunnableInterface {

    public static void main(String[] args) {
        System.out.println("Java Runnable Interface Example");
        System.out.println("==============================");

        // Run examples
        runnableInterfaceExample();
        virtualThreadWithRunnableExample(); // Java 21 feature
    }

    /**
     * Runnable Interface
     * 
     * The preferred way to create a thread is by implementing the Runnable interface.
     */
    private static void runnableInterfaceExample() {
        System.out.println("1. Runnable Interface Example");

        // Create a task by implementing Runnable
        Runnable task = () -> System.out.println("Thread ID: " + Thread.currentThread().threadId() +
                           " is running with Runnable");

        // Create and start 3 threads with the same Runnable
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(task);
            thread.start();
        }

        // Wait a moment for threads to complete
        ThreadUtil.sleep(500);
        System.out.println();
    }

    /**
     * Virtual Threads with Runnable (Java 21+)
     * 
     * USE CASE:
     * Virtual threads with Runnable are perfect for:
     * - Migrating existing Runnable-based code to use virtual threads with minimal changes
     * - Modernizing legacy applications that use the Runnable pattern
     * - When you need to maintain API compatibility while improving scalability
     * - Applications that need to handle many concurrent tasks (thousands or millions)
     * - When you want to write simple, sequential code that scales well
     * - Replacing complex asynchronous callback patterns with simpler code
     * 
     * LOW-LEVEL EXPLANATION:
     * When using Runnable with virtual threads:
     * 1. The Runnable interface remains unchanged - it's still just a task to execute
     * 2. Instead of creating a platform thread, you create a virtual thread:
     *    a. Thread.ofVirtual().start(runnable) creates and starts a virtual thread
     *    b. The JVM manages the scheduling of the virtual thread onto carrier threads
     * 3. The Runnable's code executes in the context of the virtual thread
     * 4. When the Runnable performs a blocking operation:
     *    a. The virtual thread is unmounted from its carrier thread
     *    b. The carrier thread can execute other virtual threads
     *    c. When the blocking operation completes, the virtual thread is scheduled to continue
     * 5. This happens transparently to the Runnable code - it doesn't need to be aware of virtual threads
     * 
     * Advantages:
     * - Same familiar Runnable interface
     * - Easy migration path from platform threads to virtual threads
     * - All the benefits of virtual threads (lightweight, efficient for I/O)
     * - No need to rewrite existing code that uses Runnable
     * - Maintains the separation of concerns between task logic and threading mechanics
     */
    private static void virtualThreadWithRunnableExample() {
        System.out.println("2. Virtual Threads with Runnable (Java 21+)");

        try {
            // Check if we're running on Java 21 or later
            if (!ThreadUtil.supportsVirtualThreads()) {
                ThreadUtil.printVirtualThreadsRequirement();
                return;
            }

            // Create a Runnable task
            Runnable task = () -> {
                String threadType = Thread.currentThread().isVirtual() ? "virtual" : "platform";
                System.out.println("Running in a " + threadType + " thread with ID: " + 
                                  Thread.currentThread().threadId());

                // Simulate some I/O-bound work
                ThreadUtil.sleep(100);
            };

            // Run the same task in both platform and virtual threads for comparison
            System.out.println("Running task in a platform thread:");
            Thread platformThread = new Thread(task);
            platformThread.start();
            platformThread.join();

            System.out.println("\nRunning the same task in a virtual thread:");
            Thread virtualThread = Thread.ofVirtual().start(task);
            virtualThread.join();

            // Create many virtual threads with the same Runnable
            System.out.println("\nCreating many virtual threads with the same Runnable:");

            final int THREAD_COUNT = 10;
            Thread[] threads = new Thread[THREAD_COUNT];

            long startTime = System.currentTimeMillis();

            // Create and start virtual threads
            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i] = Thread.ofVirtual().name("vthread-" + i).start(task);
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Created and ran " + THREAD_COUNT + 
                              " virtual threads in " + (endTime - startTime) + "ms");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
    }
}
