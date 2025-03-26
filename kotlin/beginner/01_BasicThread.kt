/**
 * This file demonstrates the Basic Thread model in Kotlin.
 * 
 * USE CASE:
 * The Basic Thread model in Kotlin is suitable for:
 * - Simple applications with a small number of threads
 * - When interoperating with Java code that uses threads
 * - Educational purposes to understand threading fundamentals
 * - Legacy Kotlin applications that haven't migrated to coroutines
 * - When you need direct control over thread properties (priority, daemon status)
 * - Scenarios where you want to avoid adding coroutine dependencies
 * 
 * LOW-LEVEL EXPLANATION:
 * Kotlin's Thread implementation is a direct wrapper around Java's Thread class:
 * 1. Thread Creation:
 *    a. Kotlin creates a JVM thread which maps 1:1 to an OS thread
 *    b. Each thread gets its own stack memory (typically 1MB by default)
 *    c. The thread is registered with the JVM's thread management system
 * 2. Kotlin-specific Enhancements:
 *    a. Lambda support for creating threads (Thread { ... })
 *    b. Extension functions on Thread class for better usability
 *    c. Scope functions (apply, also, etc.) for cleaner thread configuration
 *    d. Improved thread-local variable syntax
 * 3. Execution Model:
 *    a. When start() is called, the JVM signals the OS to schedule the thread
 *    b. The OS scheduler determines when the thread runs based on priority and available CPU cores
 *    c. The thread executes the run() method or lambda in a separate call stack
 *    d. When execution completes, the thread dies and resources are eventually reclaimed
 * 
 * Note: While Kotlin supports this model, coroutines are the recommended approach for most
 * concurrency needs in Kotlin due to their lightweight nature and better integration with
 * Kotlin's language features.
 */

fun main() {
    println("Kotlin Basic Thread Example")
    println("===========================")

    // Run examples
    basicThreadExample()
    threadScopeExample() // Latest Kotlin threading feature
}

/**
 * Basic Thread Class
 * 
 * Kotlin can use Java's Thread class directly.
 */
fun basicThreadExample() {
    println("1. Basic Thread Example")

    // Create a custom thread by extending Thread class
    class MyThread : Thread() {
        override fun run() {
            println("Thread ID: ${Thread.currentThread().id} is running in MyThread class")
        }
    }

    // Create and start 3 threads
    for (i in 0 until 3) {
        val thread = MyThread()
        thread.start() // This calls the run() method in a new thread
    }

    // Wait a moment for threads to complete
    Thread.sleep(500)
    println()
}

/**
 * Thread Scope (Kotlin 1.6+)
 * 
 * USE CASE:
 * Thread Scope features in Kotlin are useful for:
 * - Applications that need thread-isolated data storage
 * - When migrating from Java threads to Kotlin but not yet to coroutines
 * - Resource management across multiple threads
 * - When you need thread-specific caching
 * - Implementing thread-safe singletons
 * - Scenarios where thread identity is important for the business logic
 * 
 * LOW-LEVEL EXPLANATION:
 * Kotlin enhances Java's threading model with several improvements:
 * 1. ThreadLocal Enhancements:
 *    a. Kotlin provides a cleaner API for ThreadLocal with lambda-based initialization
 *    b. ThreadLocal.withInitial { ... } creates a thread-local with a factory function
 *    c. Each thread gets its own isolated copy of the value
 *    d. Under the hood, this uses Java's ThreadLocal but with Kotlin's function types
 * 2. Resource Management:
 *    a. Kotlin's 'use' function works with AutoCloseable resources
 *    b. It ensures resources are properly closed even if exceptions occur
 *    c. This is implemented using Kotlin's inline functions and try-finally blocks
 *    d. The compiler optimizes this to efficient bytecode
 * 3. Thread Configuration:
 *    a. Kotlin's scope functions (apply, also, let, etc.) make thread configuration cleaner
 *    b. These are implemented as extension functions that return the receiver object
 *    c. They create temporary scopes where 'this' refers to the thread being configured
 * 
 * While Kotlin's primary concurrency model is coroutines, these features
 * provide better ergonomics when working with traditional threads.
 */
fun threadScopeExample() {
    println("2. Thread Scope Example")

    // Create a thread-local variable with initial value factory
    val threadLocal = ThreadLocal.withInitial { 
        "Initial value for ${Thread.currentThread().name}" 
    }

    // Create a thread that accesses the thread-local variable
    val thread1 = Thread {
        println("Thread ${Thread.currentThread().name} initial value: ${threadLocal.get()}")

        // Change the value in this thread
        threadLocal.set("Modified in ${Thread.currentThread().name}")
        println("Thread ${Thread.currentThread().name} after modification: ${threadLocal.get()}")
    }.apply { name = "CustomThread-1" }

    // Create another thread
    val thread2 = Thread {
        println("Thread ${Thread.currentThread().name} initial value: ${threadLocal.get()}")

        // Change the value in this thread
        threadLocal.set("Changed in ${Thread.currentThread().name}")
        println("Thread ${Thread.currentThread().name} after change: ${threadLocal.get()}")
    }.apply { name = "CustomThread-2" }

    // Start threads
    thread1.start()
    thread2.start()

    // Wait for threads to complete
    thread1.join()
    thread2.join()

    // Check value in main thread
    println("Main thread value: ${threadLocal.get()}")

    // Demonstrate Kotlin's use function for thread safety
    println("\nDemonstrating Kotlin's 'use' function for thread safety:")

    // Create a resource that needs to be closed
    class ThreadResource(val name: String) : AutoCloseable {
        init {
            println("Resource '$name' created in thread: ${Thread.currentThread().name}")
        }

        fun doWork() {
            println("Resource '$name' doing work in thread: ${Thread.currentThread().name}")
        }

        override fun close() {
            println("Resource '$name' closed in thread: ${Thread.currentThread().name}")
        }
    }

    // Create threads that use the resource with 'use' function
    val resourceThread1 = Thread {
        ThreadResource("Resource-1").use { resource ->
            resource.doWork()
            // Resource will be automatically closed when the block exits
        }
    }.apply { name = "ResourceThread-1" }

    val resourceThread2 = Thread {
        ThreadResource("Resource-2").use { resource ->
            resource.doWork()
            // Resource will be automatically closed when the block exits
        }
    }.apply { name = "ResourceThread-2" }

    // Start and join threads
    resourceThread1.start()
    resourceThread2.start()
    resourceThread1.join()
    resourceThread2.join()

    println()
}
