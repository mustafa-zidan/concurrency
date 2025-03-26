package threads;

import threads.util.ThreadUtil;

/**
 * This file provides an overview of different threading models in Java.
 * Java offers several approaches to concurrency, from basic Thread class
 * to advanced concurrency utilities in the java.util.concurrent package.
 * 
 * This class serves as a reference guide to the different threading examples
 * available in this project. For detailed implementations, see the referenced files.
 */

class ThreadingModels {

    public static void main(String[] args) throws Exception {
        System.out.println("Java Threading Models Overview");
        System.out.println("============================");

        // Display information about each threading model
        basicThreadInfo();
        runnableInterfaceInfo();
        executorServiceInfo();
        completableFutureInfo();
        virtualThreadsInfo();
    }

    /**
     * 1. Basic Thread Class
     * 
     * The most fundamental way to create a thread in Java is by extending the Thread class.
     */
    private static void basicThreadInfo() {
        System.out.println("1. Basic Thread Class");
        System.out.println("   - Extends the Thread class and overrides run() method");
        System.out.println("   - Simple but has limitations (single inheritance, tight coupling)");
        System.out.println("   - Example: See 01_BasicThread.java in threads.beginner package");
        System.out.println();
    }

    /**
     * 2. Runnable Interface
     * 
     * The preferred way to create a thread is by implementing the Runnable interface.
     */
    private static void runnableInterfaceInfo() {
        System.out.println("2. Runnable Interface");
        System.out.println("   - Implements the Runnable interface and pass to Thread constructor");
        System.out.println("   - Advantages:");
        System.out.println("     * Separates the task (what to run) from the thread (how to run)");
        System.out.println("     * Allows a class to extend another class while still being runnable");
        System.out.println("     * Can be used with thread pools and executors");
        System.out.println("   - Example: See 02_RunnableInterface.java in threads.beginner package");
        System.out.println();
    }

    /**
     * 3. ExecutorService
     * 
     * The java.util.concurrent package provides higher-level concurrency utilities.
     */
    private static void executorServiceInfo() {
        System.out.println("3. ExecutorService");
        System.out.println("   - Manages thread creation, reuse, and lifecycle");
        System.out.println("   - Advantages:");
        System.out.println("     * Thread pooling (reuse threads instead of creating new ones)");
        System.out.println("     * Task queuing with configurable policies");
        System.out.println("     * Scheduled execution of tasks");
        System.out.println("     * Managed thread lifecycle");
        System.out.println("   - Example: See 01_ExecutorService.java in threads.intermediate package");
        System.out.println();
    }

    /**
     * 4. CompletableFuture (Java 8+)
     * 
     * CompletableFuture represents a future result of an asynchronous computation.
     */
    private static void completableFutureInfo() {
        System.out.println("4. CompletableFuture");
        System.out.println("   - Represents a future result of an asynchronous computation");
        System.out.println("   - Advantages:");
        System.out.println("     * Chaining operations (thenApply, thenCompose, etc.)");
        System.out.println("     * Combining multiple futures (allOf, anyOf)");
        System.out.println("     * Exception handling (exceptionally, handle)");
        System.out.println("     * Functional programming style");
        System.out.println("   - Example: See 01_CompletableFutureExample.java in threads.advanced package");
        System.out.println();
    }

    /**
     * 5. Virtual Threads (Java 21+)
     * 
     * Virtual threads are lightweight threads that make it easier to write,
     * maintain, and observe high-throughput concurrent applications.
     */
    private static void virtualThreadsInfo() {
        System.out.println("5. Virtual Threads (Java 21+)");
        System.out.println("   - Lightweight threads managed by the JVM");
        System.out.println("   - Advantages:");
        System.out.println("     * Much lighter weight than platform threads (can create millions)");
        System.out.println("     * Efficient for I/O-bound applications");
        System.out.println("     * Use the same Thread API, making migration easy");
        System.out.println("     * Allow writing simple sequential code that scales well");
        System.out.println("   - Examples:");
        System.out.println("     * 01_BasicThread.java in threads.beginner package (virtualThreadExample)");
        System.out.println("     * 02_RunnableInterface.java in threads.beginner package (virtualThreadWithRunnableExample)");
        System.out.println("     * 01_ExecutorService.java in threads.intermediate package (virtualThreadExecutorExample)");
        System.out.println("     * 01_CompletableFutureExample.java in threads.advanced package (virtualThreadCompletableFutureExample)");

        if (ThreadUtil.supportsVirtualThreads()) {
            System.out.println("   - Your Java version supports virtual threads: " + Runtime.version());
        } else {
            System.out.println("   - Virtual threads require Java 21 or later");
            System.out.println("   - Your current Java version: " + Runtime.version());
        }

        System.out.println();
    }
}
