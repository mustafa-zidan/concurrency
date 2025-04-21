package threads.beginner

import threads.util.ThreadUtil

/**
 * This file demonstrates the Runnable Interface model in Kotlin.
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
 * - Works well with Kotlin's lambda expressions
 * 
 * NOTE: In Kotlin, you can use lambda expressions to create Runnables more concisely.
 * However, for most Kotlin applications, coroutines provide a more powerful and idiomatic
 * approach to concurrency. See the intermediate examples for coroutines.
 */

fun main() {
    println("Kotlin Runnable Interface Example")
    println("================================")

    // Run examples
    runnableInterfaceExample()
    virtualThreadWithRunnableExample() // Java 21 feature
}

/**
 * Runnable Interface
 * 
 * The preferred way to create a thread is by implementing the Runnable interface.
 * In Kotlin, this is typically done using lambda expressions.
 */
private fun runnableInterfaceExample() {
    println("1. Runnable Interface Example")

    // Create a task using a lambda (Kotlin's way to implement Runnable)
    val task = Runnable {
        println("Thread ID: ${Thread.currentThread().threadId()} is running with Runnable")
    }

    // Create and start 3 threads with the same Runnable
    for (i in 0 until 3) {
        val thread = Thread(task)
        thread.start()
    }

    // Even more concise with Kotlin - pass the lambda directly
    for (i in 0 until 3) {
        Thread {
            println("Thread ID: ${Thread.currentThread().threadId()} is running with lambda")
        }.start()
    }

    // Wait a moment for threads to complete
    ThreadUtil.sleep(500)
    println()
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
 * 
 * NOTE: While virtual threads with Runnable work well in Kotlin, coroutines provide
 * similar benefits with a more Kotlin-idiomatic API. For most Kotlin applications,
 * coroutines are preferred.
 */
private fun virtualThreadWithRunnableExample() {
    println("2. Virtual Threads with Runnable (Java 21+)")

    try {
        // Check if we're running on Java 21 or later
        if (!ThreadUtil.supportsVirtualThreads()) {
            ThreadUtil.printVirtualThreadsRequirement()
            return
        }

        // Create a Runnable task using a Kotlin lambda
        val task = Runnable {
            val threadType = if (Thread.currentThread().isVirtual) "virtual" else "platform"
            println("Running in a $threadType thread with ID: ${Thread.currentThread().threadId()}")

            // Simulate some I/O-bound work
            ThreadUtil.sleep(100)
        }

        // Run the same task in both platform and virtual threads for comparison
        println("Running task in a platform thread:")
        val platformThread = Thread(task)
        platformThread.start()
        platformThread.join()

        println("\nRunning the same task in a virtual thread:")
        val virtualThread = Thread.ofVirtual().start(task)
        virtualThread.join()

        // Create many virtual threads with the same Runnable
        println("\nCreating many virtual threads with the same Runnable:")

        val threadCount = 10
        val threads = Array(threadCount) { i ->
            Thread.ofVirtual().name("vthread-$i").start(task)
        }

        val startTime = System.currentTimeMillis()

        // Wait for all threads to complete
        for (thread in threads) {
            thread.join()
        }

        val endTime = System.currentTimeMillis()
        println("Created and ran $threadCount virtual threads in ${endTime - startTime}ms")

    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }

    println()
}