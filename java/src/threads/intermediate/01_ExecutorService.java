package threads.intermediate;

import threads.util.ThreadUtil;

/**
 * This file demonstrates the ExecutorService model in Java.
 * 
 * USE CASE:
 * ExecutorService is ideal for:
 * - Production applications that need to manage many concurrent tasks
 * - Server applications that handle multiple client requests
 * - Applications that need to limit the number of concurrent threads
 * - Batch processing of large datasets in parallel
 * - When you need to collect results from asynchronous tasks
 * - Applications where thread creation/destruction is a performance bottleneck
 * 
 * LOW-LEVEL EXPLANATION:
 * ExecutorService provides a high-level abstraction over thread management:
 * 1. Thread Pool Implementation:
 *    a. Creates a pool of worker threads that stay alive waiting for tasks
 *    b. When a task is submitted, a worker thread takes it from a queue and executes it
 *    c. After task completion, the worker thread returns to the pool for reuse
 * 2. Internal Components:
 *    a. Thread Pool: A collection of pre-created worker threads
 *    b. Work Queue: A queue that holds tasks waiting to be executed
 *    c. Thread Factory: Creates new threads when needed
 *    d. Rejected Execution Handler: Handles tasks when the queue is full
 * 3. Execution Flow:
 *    a. Client submits a task (Runnable/Callable) to the ExecutorService
 *    b. Task is placed in the work queue
 *    c. When a worker thread becomes available, it takes a task from the queue
 *    d. The worker thread executes the task and returns to the pool
 * 4. Pool Types:
 *    a. Fixed Thread Pool: Fixed number of threads
 *    b. Cached Thread Pool: Dynamically sized pool that creates threads as needed
 *    c. Single Thread Executor: Single worker thread with a queue
 *    d. Scheduled Thread Pool: For delayed or periodic execution
 * 
 * Advantages:
 * - Thread pooling (reuse threads instead of creating new ones)
 * - Task queuing with configurable policies
 * - Scheduled execution of tasks
 * - Managed thread lifecycle
 * - Reduced overhead of thread creation/destruction
 * - Better resource utilization and performance
 */

import java.util.concurrent.*;

class ExecutorService {

    public static void main(String[] args) {
        System.out.println("Java ExecutorService Example");
        System.out.println("===========================");

        // Run examples
        executorServiceExample();
        virtualThreadExecutorExample(); // Java 21 feature
    }

    /**
     * ExecutorService
     * 
     * The java.util.concurrent package provides higher-level concurrency utilities.
     * ExecutorService manages thread creation, reuse, and lifecycle.
     */
    private static void executorServiceExample() {
        System.out.println("1. ExecutorService Example");

        // Create a fixed thread pool with 2 threads
        java.util.concurrent.ExecutorService executor = 
            Executors.newFixedThreadPool(2);

        // Submit 5 tasks to the executor
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " executed by thread ID: " + 
                                   Thread.currentThread().threadId());
                // Simulate work
                ThreadUtil.sleep(100);
            });
        }

        // Shutdown the executor and wait for tasks to complete
        executor.shutdown();
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    /**
     * Virtual Thread Executor (Java 21+)
     * 
     * USE CASE:
     * Virtual Thread Executor is perfect for:
     * - High-throughput server applications (web servers, API servers)
     * - Microservices handling many concurrent requests
     * - Applications with many I/O operations (database access, network calls, file operations)
     * - When you need to scale to thousands or millions of concurrent tasks
     * - When you want to avoid thread pool tuning and configuration
     * - Modernizing existing applications that use ExecutorService without changing their API
     * 
     * LOW-LEVEL EXPLANATION:
     * The Virtual Thread Executor works fundamentally differently from traditional executors:
     * 1. Implementation Differences:
     *    a. Traditional ExecutorService maintains a fixed pool of platform threads
     *    b. VirtualThreadPerTaskExecutor creates a new virtual thread for each submitted task
     *    c. No thread pool or work queue is needed since virtual threads are so lightweight
     * 2. Execution Flow:
     *    a. When a task is submitted, a new virtual thread is created immediately
     *    b. The virtual thread is scheduled onto a carrier thread from the ForkJoinPool.commonPool()
     *    c. When the virtual thread blocks on I/O, it's unmounted from the carrier thread
     *    d. The carrier thread can then run other virtual threads
     *    e. When the task completes, the virtual thread terminates (no reuse)
     * 3. Resource Management:
     *    a. No need to configure pool sizes or queue capacities
     *    b. The system automatically adjusts to the workload
     *    c. The number of carrier threads is typically equal to the number of CPU cores
     *    d. Memory usage scales with the number of blocked virtual threads, not the total number
     * 
     * Advantages:
     * - No thread pool size limit (can handle thousands or millions of tasks)
     * - Each task gets its own virtual thread
     * - Excellent for I/O-bound workloads
     * - Simplified resource management (no need to tune pool sizes)
     * - Automatic scaling based on workload
     * - Familiar ExecutorService API with dramatically improved scalability
     */
    private static void virtualThreadExecutorExample() {
        System.out.println("2. Virtual Thread Executor Example (Java 21+)");

        try {
            // Check if we're running on Java 21 or later
            if (!ThreadUtil.supportsVirtualThreads()) {
                ThreadUtil.printVirtualThreadsRequirement();
                return;
            }

            // Create an executor that creates a new virtual thread for each task
            java.util.concurrent.ExecutorService virtualExecutor = 
                Executors.newVirtualThreadPerTaskExecutor();

            System.out.println("Submitting tasks to virtual thread executor...");

            // Submit 10 tasks
            final int TASK_COUNT = 10;

            long startTime = System.currentTimeMillis();

            // Submit tasks and collect futures
            Future<?>[] futures = new Future[TASK_COUNT];

            for (int i = 0; i < TASK_COUNT; i++) {
                final int taskId = i;
                futures[i] = virtualExecutor.submit(() -> {
                    String threadType = Thread.currentThread().isVirtual() ? "virtual" : "platform";
                    System.out.println("Task " + taskId + " running in " + threadType + 
                                      " thread with ID: " + Thread.currentThread().threadId());

                    // Simulate I/O work
                    ThreadUtil.sleep(100);

                    return "Task " + taskId + " completed";
                });
            }

            // Wait for all tasks to complete and get results
            for (int i = 0; i < TASK_COUNT; i++) {
                try {
                    String result = (String) futures[i].get();
                    System.out.println("Result: " + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Completed " + TASK_COUNT + 
                              " tasks in " + (endTime - startTime) + "ms");

            // Compare with traditional thread pool
            System.out.println("\nComparing with traditional fixed thread pool:");

            java.util.concurrent.ExecutorService fixedExecutor = 
                Executors.newFixedThreadPool(4); // 4 threads

            startTime = System.currentTimeMillis();

            // Submit the same tasks to fixed thread pool
            for (int i = 0; i < TASK_COUNT; i++) {
                final int taskId = i;
                fixedExecutor.submit(() -> {
                    System.out.println("Task " + taskId + " executed by platform thread ID: " + 
                                      Thread.currentThread().threadId());

                    // Simulate I/O work
                    ThreadUtil.sleep(100);
                });
            }

            // Shutdown and wait for completion
            fixedExecutor.shutdown();
            fixedExecutor.awaitTermination(5, TimeUnit.SECONDS);

            endTime = System.currentTimeMillis();
            System.out.println("Fixed thread pool completed " + TASK_COUNT + 
                              " tasks in " + (endTime - startTime) + "ms");

            // Shutdown virtual executor
            virtualExecutor.shutdown();
            virtualExecutor.awaitTermination(5, TimeUnit.SECONDS);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
    }
}
