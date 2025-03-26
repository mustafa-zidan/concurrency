package threads.advanced;

import threads.util.ThreadUtil;
import java.util.concurrent.*;
import java.util.stream.*;
import java.util.*;

/**
 * This file demonstrates the CompletableFuture model in Java.
 *
 * USE CASE:
 * CompletableFuture is ideal for:
 * - Complex asynchronous workflows with dependencies between operations
 * - When you need to combine results from multiple asynchronous operations
 * - Applications requiring non-blocking I/O operations
 * - UI applications that need to update based on background task results
 * - Microservices that need to make multiple parallel API calls
 * - When you need robust error handling for asynchronous code
 *
 * LOW-LEVEL EXPLANATION:
 * CompletableFuture extends the Future interface with completion mechanisms:
 * 1. Internal Structure:
 *    a. Contains a result value (or exception) that may be set in the future
 *    b. Maintains a linked list of dependent actions to execute when completed
 *    c. Uses an internal state machine to track completion status
 * 2. Execution Model:
 *    a. By default, uses ForkJoinPool.commonPool() for async operations
 *    b. Can accept a custom Executor for controlling thread allocation
 *    c. Operations can run in the calling thread or asynchronously
 * 3. Completion Mechanisms:
 *    a. Explicit completion: complete(), completeExceptionally()
 *    b. Completion from a function: supplyAsync(), runAsync()
 *    c. Completion from another CompletableFuture: thenCompose()
 * 4. Chaining and Composition:
 *    a. When a CompletableFuture completes, it triggers all dependent actions
 *    b. Each chained operation creates a new CompletableFuture
 *    c. The completion chain propagates both results and exceptions
 *
 * Advantages:
 * - Chaining operations (thenApply, thenCompose, etc.)
 * - Combining multiple futures (allOf, anyOf)
 * - Exception handling (exceptionally, handle)
 * - Functional programming style
 * - Non-blocking execution model
 * - Composable asynchronous operations
 */
class CompletableFutureExample {
    public static void main(String[] args) throws Exception {
        System.out.println("Java CompletableFuture Example");
        System.out.println("=============================");

        // Run examples
        completableFutureExample();
        virtualThreadCompletableFutureExample(); // Java 21 feature
    }

    /**
     * CompletableFuture (Java 8+)
     * <p>
     * CompletableFuture represents a future result of an asynchronous computation.
     * It provides a functional approach to handling asynchronous operations.
     * </p>
     */
    private static void completableFutureExample() throws Exception {
        System.out.println("1. CompletableFuture Example");

        // Create a CompletableFuture that runs asynchronously
        CompletableFuture<String> future = 
            CompletableFuture.supplyAsync(() -> {
                System.out.println("Generating data in thread ID: " + 
                                   Thread.currentThread().threadId());
                // Simulate work
                ThreadUtil.sleep(200);
                return "Some data";
            });

        // Chain operations to the future
        CompletableFuture<String> processedFuture = 
            future.thenApply(data -> {
                System.out.println("Processing data in thread ID: " + 
                                   Thread.currentThread().threadId());
                return data.toUpperCase();
            });

        // Get the result (blocks until complete)
        String result = processedFuture.get();
        System.out.println("Final result: " + result);

        // Example of combining multiple futures
        CompletableFuture<String> future1 = 
            CompletableFuture.supplyAsync(() -> "Hello");

        CompletableFuture<String> future2 = 
            CompletableFuture.supplyAsync(() -> "World");

        CompletableFuture<Void> combinedFuture = 
            CompletableFuture.allOf(future1, future2);

        // Wait for both futures to complete
        combinedFuture.get();

        // Get results from both futures
        String combined = future1.get() + " " + future2.get();
        System.out.println("Combined result: " + combined);
        System.out.println();
    }

    /**
     * Virtual Thread with CompletableFuture (Java 21+)
     * 
     * USE CASE:
     * Virtual Threads with CompletableFuture are perfect for:
     * - High-scale microservices making many concurrent API calls
     * - Data processing pipelines with many parallel I/O operations
     * - Applications that need to handle thousands of concurrent workflows
     * - Backend services that aggregate data from multiple sources
     * - When you need both the composition benefits of CompletableFuture and the scalability of virtual threads
     * - Modernizing existing CompletableFuture-based code without changing its structure
     * 
     * LOW-LEVEL EXPLANATION:
     * Combining Virtual Threads with CompletableFuture creates a powerful synergy:
     * 1. Integration Mechanism:
     *    a. CompletableFuture accepts an Executor for its async operations
     *    b. Virtual threads can be provided via Executors.newVirtualThreadPerTaskExecutor()
     *    c. Each async operation runs in its own virtual thread instead of the ForkJoinPool
     * 2. Execution Flow:
     *    a. When an async operation is submitted, a new virtual thread is created
     *    b. The virtual thread executes the operation and can efficiently block on I/O
     *    c. When the operation completes, the CompletableFuture's completion mechanisms are triggered
     *    d. Dependent operations can also run on virtual threads if the same executor is used
     * 3. Performance Characteristics:
     *    a. Scales to millions of concurrent operations with minimal overhead
     *    b. Blocking operations don't waste resources as virtual threads unmount from carrier threads
     *    c. Stack traces remain meaningful as each operation has its own virtual thread
     *    d. Debugging is simpler as the code follows a sequential flow
     * 
     * Advantages:
     * - Lightweight threads for each async operation
     * - Better CPU utilization
     * - Simplified debugging (stack traces preserve the call hierarchy)
     * - Reduced memory footprint compared to platform threads
     * - Eliminates the need for complex non-blocking I/O libraries
     * - Combines functional composition with efficient concurrency
     */
    private static void virtualThreadCompletableFutureExample() {
        System.out.println("2. Virtual Thread with CompletableFuture Example (Java 21+)");

        try {
            // Check if we're running on Java 21 or later
            if (!ThreadUtil.supportsVirtualThreads()) {
                ThreadUtil.printVirtualThreadsRequirement();
                return;
            }

            // Create an executor that uses virtual threads
            ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

            // Create a CompletableFuture that runs on a virtual thread
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                String threadType = Thread.currentThread().isVirtual() ? "virtual" : "platform";
                System.out.println("Generating data in " + threadType + " thread ID: " + 
                                  Thread.currentThread().threadId());

                // Simulate I/O work
                ThreadUtil.sleep(200);

                return "Data from virtual thread";
            }, virtualExecutor);

            // Chain operations
            CompletableFuture<String> processedFuture = future.thenApplyAsync(data -> {
                String threadType = Thread.currentThread().isVirtual() ? "virtual" : "platform";
                System.out.println("Processing data in " + threadType + " thread ID: " + 
                                  Thread.currentThread().threadId());
                return data.toUpperCase();
            }, virtualExecutor);

            // Get the result
            String result = processedFuture.get();
            System.out.println("Final result: " + result);

            // Example of many parallel operations with virtual threads
            System.out.println("\nRunning many parallel operations with virtual threads:");

            final int TASK_COUNT = 100;

            long startTime = System.currentTimeMillis();

            // Create many CompletableFutures using virtual threads
            List<CompletableFuture<String>> futures = IntStream.range(0, TASK_COUNT)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    // Simulate I/O operation with varying duration
                    try {
                        Thread.sleep(50 + (i % 5) * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "Result-" + i;
                }, virtualExecutor))
                .toList();

            // Combine all futures
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
            );

            // Wait for all to complete
            allFutures.get();

            // Get all results
            List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();

            long endTime = System.currentTimeMillis();

            System.out.println("Completed " + results.size() + " tasks in " + 
                              (endTime - startTime) + "ms");
            System.out.println("First few results: " + 
                              results.subList(0, Math.min(5, results.size())));

            // Shutdown the executor
            virtualExecutor.shutdown();
            virtualExecutor.awaitTermination(5, TimeUnit.SECONDS);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
    }
}
