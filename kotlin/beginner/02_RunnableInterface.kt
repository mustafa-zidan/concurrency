/**
 * This file demonstrates the Runnable Interface model in Kotlin.
 * 
 * USE CASE:
 * The Runnable Interface in Kotlin is suitable for:
 * - When you need to separate task logic from thread management
 * - Code that needs to interoperate with Java threading libraries
 * - When your task class needs to extend another class
 * - When the same task needs to be executed by multiple threads
 * - Legacy applications that use thread pools or executors
 * - When you want to leverage Kotlin's functional features with Java's threading model
 * 
 * LOW-LEVEL EXPLANATION:
 * Kotlin enhances Java's Runnable interface with several language features:
 * 1. SAM (Single Abstract Method) Conversion:
 *    a. Kotlin automatically converts lambda expressions to Runnable instances
 *    b. The compiler generates an anonymous class implementing Runnable
 *    c. The lambda body becomes the implementation of the run() method
 *    d. This happens at compile time with minimal runtime overhead
 * 2. Function Type Compatibility:
 *    a. Kotlin treats () -> Unit function type as compatible with Runnable
 *    b. This allows passing lambdas directly to methods expecting Runnables
 *    c. The compiler handles the conversion between function types and SAM types
 * 3. Execution Model:
 *    a. When a Runnable is passed to a Thread constructor, it's stored as the thread's target
 *    b. When the thread starts, it calls the run() method of the Runnable
 *    c. The Runnable's code executes in the context of the thread
 * 
 * Kotlin's lambda expressions make it easier to create Runnables, resulting in
 * more concise and readable code compared to Java's anonymous inner classes.
 */

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {
    println("Kotlin Runnable Interface Example")
    println("=================================")

    // Run examples
    runnableInterfaceExample()
    advancedRunnableExample() // Latest Kotlin features with Runnable
}

/**
 * Runnable Interface
 * 
 * Kotlin can use Java's Runnable interface with more concise syntax.
 */
fun runnableInterfaceExample() {
    println("1. Runnable Interface Example")

    // Create a task using a lambda (implementing Runnable)
    val task = Runnable {
        println("Thread ID: ${Thread.currentThread().id} is running with Runnable")
    }

    // Create and start 3 threads with the same Runnable
    for (i in 0 until 3) {
        val thread = Thread(task)
        thread.start()
    }

    // Wait a moment for threads to complete
    Thread.sleep(500)
    println()
}

/**
 * Advanced Runnable Features in Kotlin
 * 
 * USE CASE:
 * Advanced Runnable features in Kotlin are ideal for:
 * - Modernizing legacy threading code without rewriting it completely
 * - Creating more readable and maintainable threading code
 * - Reducing boilerplate in thread configuration and management
 * - Building fluent threading APIs
 * - Conditional thread execution based on runtime conditions
 * - When you need fine-grained control over thread lifecycle
 * - Creating thread factories with consistent configuration
 * 
 * LOW-LEVEL EXPLANATION:
 * Kotlin enhances Runnable usage with several powerful language features:
 * 1. Scope Functions:
 *    a. apply: Configures an object and returns it (this in lambda)
 *       - Implemented as an extension function that returns the receiver
 *       - Compiled to efficient bytecode that doesn't create additional objects
 *    b. also: Similar to apply but uses 'it' instead of 'this'
 *       - Useful when you need to refer to the object by a different name
 *    c. let: Transforms an object and returns the result (it in lambda)
 *       - Enables safe operations on nullable objects with smart casting
 *    d. run: Executes a block on an object and returns the result (this in lambda)
 *       - Combines the features of with() and let()
 * 2. Functional Programming Features:
 *    a. takeIf: Returns the object if it satisfies a predicate, otherwise null
 *       - Enables concise conditional execution
 *    b. repeat: Executes a block of code multiple times
 *       - More readable than traditional for loops
 *    c. forEach: Iterates over a collection with a lambda
 *       - Enables functional-style iteration
 * 3. Extension Functions:
 *    a. Kotlin adds methods to existing Java classes without inheritance
 *    b. These are compiled to static methods that take the receiver as first parameter
 *    c. No runtime overhead compared to direct method calls
 * 
 * These features make threading code in Kotlin more expressive and less error-prone
 * while maintaining full compatibility with Java's threading model.
 */
fun advancedRunnableExample() {
    println("2. Advanced Runnable Features in Kotlin")

    // SAM conversion - no need for explicit Runnable interface
    println("Using SAM conversion:")
    val thread = Thread {
        println("This lambda is automatically converted to a Runnable")
        println("Running in thread: ${Thread.currentThread().name}")
    }
    thread.name = "SAM-Thread"
    thread.start()
    thread.join()

    // Using Kotlin's also scope function with threads
    println("\nUsing Kotlin scope functions with threads:")
    Thread {
        println("Thread created and configured with 'also' scope function")
    }.also {
        it.name = "Also-Thread"
        it.isDaemon = true
        println("Thread name: ${it.name}, isDaemon: ${it.isDaemon}")
    }.start()

    // Give the thread time to execute
    Thread.sleep(100)

    // Using Kotlin's apply scope function
    println("\nUsing 'apply' scope function:")
    Thread {
        println("Thread created and configured with 'apply' scope function")
    }.apply {
        name = "Apply-Thread"
        priority = Thread.MAX_PRIORITY
        println("Thread name: $name, priority: $priority")
    }.start()

    // Give the thread time to execute
    Thread.sleep(100)

    // Using Kotlin's run scope function with ExecutorService
    println("\nUsing 'run' scope function with ExecutorService:")
    Executors.newSingleThreadExecutor().run {
        // Submit a task
        submit {
            println("Task running in executor thread: ${Thread.currentThread().name}")
        }

        // Shutdown properly
        shutdown()
        awaitTermination(1, TimeUnit.SECONDS)
    }

    // Using Kotlin's let scope function
    println("\nUsing 'let' scope function:")
    Runnable {
        println("Runnable executed via 'let'")
    }.let { runnable ->
        val t = Thread(runnable)
        t.name = "Let-Thread"
        t.start()
        t.join()
    }

    // Using Kotlin's takeIf function for conditional thread execution
    println("\nUsing 'takeIf' function:")
    val condition = true

    Thread {
        println("This thread only starts if the condition is true")
    }.takeIf { condition }?.start()

    // Give the thread time to execute
    Thread.sleep(100)

    // Using Kotlin's repeat function for creating multiple threads
    println("\nUsing 'repeat' function to create multiple threads:")
    val threads = mutableListOf<Thread>()

    repeat(3) { index ->
        threads.add(Thread {
            println("Repeat thread $index running")
        }.apply { name = "Repeat-Thread-$index" })
    }

    // Start all threads
    threads.forEach { it.start() }

    // Wait for all threads to complete
    threads.forEach { it.join() }

    println()
}
