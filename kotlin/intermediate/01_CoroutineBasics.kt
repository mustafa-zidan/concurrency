/**
 * This file demonstrates the Coroutine Basics in Kotlin.
 * 
 * USE CASE:
 * Kotlin Coroutines are ideal for:
 * - Asynchronous programming without callback hell
 * - Network operations and API calls
 * - UI applications to keep the main thread responsive
 * - Handling multiple concurrent operations
 * - Database access and file I/O
 * - Applications that need to scale to thousands of concurrent tasks
 * - When you need clean error handling for asynchronous code
 * 
 * LOW-LEVEL EXPLANATION:
 * Coroutines work through a sophisticated suspension mechanism:
 * 1. Coroutine Creation:
 *    a. Coroutines are created using coroutine builders (launch, async, etc.)
 *    b. Each coroutine is an instance of a Continuation object
 *    c. Coroutines are much lighter than threads (only a few dozen bytes overhead)
 * 2. Suspension Mechanism:
 *    a. The Kotlin compiler transforms suspend functions using continuation-passing style
 *    b. When a coroutine reaches a suspension point (e.g., delay()), it saves its state
 *    c. The thread executing the coroutine is freed to do other work
 *    d. When the suspension completes, the coroutine resumes from where it left off
 * 3. Dispatcher System:
 *    a. Dispatchers determine which thread(s) the coroutine runs on
 *    b. Default dispatcher uses a thread pool sized to the number of CPU cores
 *    c. A suspended coroutine doesn't block the thread it was running on
 * 4. State Machine Transformation:
 *    a. The compiler converts suspend functions into state machines
 *    b. Each suspension point becomes a state in the state machine
 *    c. This allows efficient resumption after suspension
 * 
 * Advantages:
 * - Much lighter weight than threads (can create thousands of coroutines)
 * - Built-in cancellation support
 * - Structured concurrency
 * - Suspension points make code more readable than callbacks
 * - Efficient resource utilization
 * - Sequential code style for asynchronous operations
 */

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    println("Kotlin Coroutine Basics Example")
    println("===============================")

    // Run examples
    coroutineBasicsExample()
    structuredConcurrencyExample() // Latest Kotlin coroutines feature
}

/**
 * Coroutine Basics
 * 
 * Coroutines are Kotlin's approach to asynchronous programming.
 * They are lightweight threads that can be suspended and resumed.
 */
suspend fun coroutineBasicsExample() = coroutineScope {
    println("1. Coroutine Basics Example")

    // Launch a coroutine in the current scope
    launch {
        println("Coroutine 1: Starting on thread ${Thread.currentThread().id}")
        delay(100) // This suspends the coroutine but doesn't block the thread
        println("Coroutine 1: Resuming on thread ${Thread.currentThread().id} after delay")
    }

    // Launch another coroutine
    launch {
        println("Coroutine 2: Starting on thread ${Thread.currentThread().id}")
        delay(50)
        println("Coroutine 2: Resuming on thread ${Thread.currentThread().id} after delay")
    }

    println("Main thread continues while coroutines are suspended")

    // Using async to get a result
    val deferred = async {
        println("Async coroutine: Computing a value on thread ${Thread.currentThread().id}")
        delay(200)
        "Computed result"
    }

    // Wait for the result (suspends the current coroutine, not the thread)
    val result = deferred.await()
    println("Received async result: $result")

    println()
}

/**
 * Structured Concurrency (Latest Kotlin Coroutines Feature)
 * 
 * USE CASE:
 * Structured Concurrency is essential for:
 * - Applications requiring robust error handling
 * - When you need to manage the lifecycle of related concurrent tasks
 * - Preventing resource leaks in asynchronous code
 * - Implementing timeout patterns for network requests
 * - Graceful cancellation of complex operations
 * - Microservices that need to coordinate multiple API calls
 * - Building reliable concurrent systems with clear hierarchies
 * 
 * LOW-LEVEL EXPLANATION:
 * Structured Concurrency provides a hierarchical model for coroutines:
 * 1. Coroutine Hierarchy:
 *    a. Coroutines form a parent-child relationship tree
 *    b. Each coroutine has a Job object that represents its lifecycle
 *    c. Jobs form a hierarchy that mirrors the coroutine hierarchy
 * 2. Exception Handling:
 *    a. By default, exceptions propagate up the hierarchy
 *    b. When a child coroutine fails, its exception is propagated to its parent
 *    c. SupervisorJob changes this behavior to isolate failures
 *    d. CoroutineExceptionHandler catches exceptions at the scope level
 * 3. Cancellation Mechanism:
 *    a. Cancellation flows down the hierarchy (parent to children)
 *    b. When a parent is cancelled, all its children are automatically cancelled
 *    c. Cancellation is cooperative - coroutines check for cancellation at suspension points
 *    d. CancellationException is used to signal cancellation
 * 4. Scope Management:
 *    a. CoroutineScope defines the lifetime boundaries for coroutines
 *    b. coroutineScope builder creates a scope that waits for all children
 *    c. supervisorScope creates a scope that isolates child failures
 *    d. Scopes ensure that no coroutines are "forgotten" or leaked
 * 
 * Latest features include:
 * - SupervisorJob for more flexible error handling
 * - CoroutineExceptionHandler for custom error handling
 * - withTimeout and withTimeoutOrNull for timeout handling
 * - Cancellation and exception propagation improvements
 * - Enhanced debugging and stack trace preservation
 */
suspend fun structuredConcurrencyExample() = coroutineScope {
    println("2. Structured Concurrency Example (Latest Features)")

    // SupervisorJob - allows children to fail independently
    println("Using SupervisorJob:")
    val supervisor = SupervisorJob()
    val scope = CoroutineScope(coroutineContext + supervisor)

    val job1 = scope.launch {
        try {
            println("Child 1 starting")
            delay(100)
            throw RuntimeException("Simulated failure in child 1")
        } catch (e: Exception) {
            println("Child 1 caught exception: ${e.message}")
        }
    }

    val job2 = scope.launch {
        println("Child 2 starting")
        delay(200)
        println("Child 2 completed successfully")
    }

    // Wait for both jobs to complete
    job1.join()
    job2.join()

    // CoroutineExceptionHandler - for custom error handling
    println("\nUsing CoroutineExceptionHandler:")
    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Caught exception in handler: ${exception.message}")
    }

    val scopeWithHandler = CoroutineScope(coroutineContext + exceptionHandler)

    val job3 = scopeWithHandler.launch {
        println("Job with exception handler starting")
        delay(100)
        throw RuntimeException("Simulated failure with handler")
    }

    try {
        job3.join()
    } catch (e: Exception) {
        println("Exception was handled by handler, not propagated")
    }

    // Timeout handling
    println("\nUsing timeout functions:")

    try {
        withTimeout(300) {
            println("Starting task with timeout")
            delay(500) // This will exceed the timeout
            println("This line will not be printed")
        }
    } catch (e: TimeoutCancellationException) {
        println("Task timed out as expected")
    }

    // withTimeoutOrNull returns null instead of throwing an exception
    val result = withTimeoutOrNull(300) {
        println("Starting another task with timeout")
        delay(500) // This will exceed the timeout
        "This result will not be returned"
    }

    println("Result after timeout: $result")

    // Cancellation and cooperative behavior
    println("\nDemonstrating cancellation:")

    val job = launch {
        try {
            repeat(1000) { i ->
                println("Job is running... $i")
                delay(50)
            }
        } catch (e: CancellationException) {
            println("Job was cancelled: ${e.message}")
            // Cleanup code can go here
        } finally {
            println("Job cleanup in finally block")
        }
    }

    delay(200) // Let the job run for a while
    println("Cancelling job...")
    job.cancel("Cancelled by user")
    job.join() // Wait for cancellation to complete

    println()
}
