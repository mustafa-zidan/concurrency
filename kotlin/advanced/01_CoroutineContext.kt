/**
 * This file demonstrates Coroutine Context and Dispatchers in Kotlin.
 * 
 * USE CASE:
 * Coroutine Context and Dispatchers are essential for:
 * - Controlling which threads coroutines run on
 * - Optimizing CPU-intensive vs I/O-intensive workloads
 * - Keeping UI threads responsive in Android/Desktop applications
 * - Implementing thread confinement for thread-safety
 * - Debugging and monitoring coroutines in complex applications
 * - Customizing thread pools for specific workloads
 * - Ensuring proper resource utilization across the application
 * 
 * LOW-LEVEL EXPLANATION:
 * The coroutine context is a persistent set of elements that control coroutine behavior:
 * 1. Context Structure:
 *    a. CoroutineContext is an indexed set of Element instances, each with a unique Key
 *    b. Elements can be combined using the + operator to form a new context
 *    c. Common elements include Job, CoroutineDispatcher, CoroutineName, etc.
 * 2. Dispatcher Implementation:
 *    a. Dispatchers determine which thread(s) the coroutine executes on
 *    b. Default: Uses a thread pool sized to CPU cores for CPU-bound work
 *    c. IO: Uses a larger thread pool optimized for I/O operations
 *    d. Main: Uses the main/UI thread (platform-specific implementation)
 *    e. Unconfined: Starts in the caller thread, but may switch after suspension
 * 3. Thread Management:
 *    a. Dispatchers maintain thread pools internally
 *    b. When a coroutine is dispatched, it's scheduled to run on a thread from the pool
 *    c. After suspension, the coroutine may resume on a different thread from the same pool
 *    d. Custom dispatchers can be created from ExecutorService or thread pools
 * 
 * Common dispatchers:
 * - Dispatchers.Default: CPU-intensive work
 * - Dispatchers.IO: I/O operations
 * - Dispatchers.Main: UI thread (in Android/JavaFX)
 * - Dispatchers.Unconfined: Starts in caller thread, may change after suspension
 */

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    println("Kotlin Coroutine Context Example")
    println("================================")

    // Run examples
    coroutineContextExample()
    advancedContextExample() // Latest Kotlin coroutines context features
}

/**
 * Coroutine Context and Dispatchers
 * 
 * Coroutines run in a context that defines various aspects of their behavior.
 * Dispatchers determine which thread(s) the coroutine runs on.
 */
suspend fun coroutineContextExample() = coroutineScope {
    println("1. Coroutine Context and Dispatchers Example")

    // Measure execution time
    val time = measureTimeMillis {
        // Create 100,000 coroutines
        val jobs = List(100_000) {
            // Launch each coroutine on the Default dispatcher (shared thread pool)
            launch(Dispatchers.Default) {
                // Just delay a bit to simulate work
                delay(10)
            }
        }

        // Wait for all coroutines to complete
        jobs.forEach { it.join() }
    }

    println("Launched 100,000 coroutines in $time ms")

    // Demonstrate different dispatchers
    launch(Dispatchers.Default) {
        println("Running on Default dispatcher (thread ${Thread.currentThread().id})")
    }

    launch(Dispatchers.IO) {
        println("Running on IO dispatcher (thread ${Thread.currentThread().id})")
    }

    // Create a custom dispatcher with a fixed thread pool
    val customDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

    launch(customDispatcher) {
        println("Running on custom dispatcher (thread ${Thread.currentThread().id})")
    }

    // Always close custom dispatchers to release resources
    customDispatcher.close()

    println()
}

/**
 * Advanced Coroutine Context Features (Latest Kotlin Features)
 * 
 * USE CASE:
 * Advanced context features are valuable for:
 * - Large-scale applications with complex coroutine hierarchies
 * - Performance-critical applications that need fine-tuned thread utilization
 * - Debugging complex asynchronous workflows
 * - Applications with specific threading requirements (e.g., database connections)
 * - When you need to isolate different parts of your application
 * - Implementing custom threading policies for specific workloads
 * - Enterprise applications with sophisticated resource management needs
 * 
 * LOW-LEVEL EXPLANATION:
 * The latest Kotlin coroutine context features provide enhanced control:
 * 1. CoroutineName Implementation:
 *    a. Stores a string name in the coroutine context
 *    b. Integrates with debugging tools to show meaningful names in stack traces
 *    c. Implemented as a simple context element with minimal overhead
 * 2. limitedParallelism Mechanism:
 *    a. Creates a new dispatcher that limits the number of threads used
 *    b. Uses a semaphore internally to control concurrent execution
 *    c. Shares the same thread pool as the parent dispatcher
 *    d. Prevents thread starvation by limiting specific workloads
 * 3. Context Inheritance:
 *    a. Child coroutines automatically inherit their parent's context
 *    b. The + operator merges contexts, with right-side elements overriding left-side
 *    c. Context elements are stored in an optimized immutable map-like structure
 *    d. Context access is optimized for fast key-based lookup
 * 4. Scope Management:
 *    a. CoroutineScope combines a context with lifecycle management
 *    b. Cancelling a scope cancels all coroutines launched in that scope
 *    c. Scopes can be nested to create hierarchical cancellation structures
 *    d. Custom scopes allow isolation of different parts of an application
 * 
 * Latest features include:
 * - CoroutineName for better debugging
 * - CoroutineScope with multiple context elements
 * - limitedParallelism for fine-grained control over thread usage
 * - Dispatchers.IO optimizations
 * - Context preservation and propagation
 * - Enhanced exception handling and debugging
 */
suspend fun advancedContextExample() = coroutineScope {
    println("2. Advanced Coroutine Context Features (Latest)")

    // CoroutineName for better debugging
    println("Using CoroutineName:")
    launch(CoroutineName("NamedCoroutine")) {
        println("Coroutine name: ${coroutineContext[CoroutineName]?.name}")
        println("Running on thread: ${Thread.currentThread().name}")
    }.join()

    // Combining multiple context elements
    println("\nCombining multiple context elements:")
    launch(Dispatchers.Default + CoroutineName("Combined")) {
        println("Coroutine name: ${coroutineContext[CoroutineName]?.name}")
        println("Dispatcher: ${coroutineContext[CoroutineDispatcher]}")
        println("Running on thread: ${Thread.currentThread().name}")
    }.join()

    // limitedParallelism for fine-grained control (Kotlin 1.6+)
    println("\nUsing limitedParallelism:")

    // Create a dispatcher with limited parallelism
    val limitedDispatcher = Dispatchers.IO.limitedParallelism(2)

    val jobs = List(5) { index ->
        launch(limitedDispatcher) {
            println("Task $index starting on thread ${Thread.currentThread().name}")
            delay(100)
            println("Task $index finishing on thread ${Thread.currentThread().name}")
        }
    }

    jobs.forEach { it.join() }

    // Context preservation and propagation
    println("\nContext preservation and propagation:")

    // Create a parent coroutine with a specific context
    withContext(Dispatchers.Default + CoroutineName("Parent")) {
        println("Parent context: ${coroutineContext[CoroutineName]}, thread: ${Thread.currentThread().name}")

        // Child inherits context from parent
        launch {
            println("Child inherits context: ${coroutineContext[CoroutineName]}, thread: ${Thread.currentThread().name}")
        }

        // Child can override parts of the context
        launch(CoroutineName("ChildOverride")) {
            println("Child overrides name: ${coroutineContext[CoroutineName]}, thread: ${Thread.currentThread().name}")
        }

        // Wait for children to complete
        coroutineContext.job.children.forEach { it.join() }
    }

    // Coroutine scope with custom context
    println("\nCoroutine scope with custom context:")

    // Create a custom scope with specific context elements
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default + CoroutineName("CustomScope"))

    val job = scope.launch {
        println("Coroutine in custom scope")
        println("Context elements: ${coroutineContext[CoroutineName]}, ${coroutineContext[CoroutineDispatcher]}")

        // Demonstrate context inheritance in nested coroutines
        launch {
            println("Nested coroutine in custom scope")
            println("Inherited context: ${coroutineContext[CoroutineName]}")
        }
    }

    job.join()

    // Cancelling the scope cancels all coroutines launched in it
    scope.cancel()

    println()
}
