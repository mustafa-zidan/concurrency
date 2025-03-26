/**
 * This file demonstrates different threading models in Kotlin.
 * Kotlin supports both traditional Java threading models and its own coroutines system,
 * which provides a more lightweight and flexible approach to concurrency.
 */

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

fun main() {
    println("Kotlin Threading Models Examples")
    println("================================")

    // Run examples
    basicThreadExample()
    runnableInterfaceExample()
    coroutineBasicsExample()
    coroutineContextExample()
    coroutineFlowExample()
    
    // This is needed to let the coroutines finish before the program exits
    Thread.sleep(1000)
}

/**
 * 1. Basic Thread Class
 * 
 * Kotlin can use Java's Thread class directly.
 * This is the most basic way to create threads in Kotlin, inherited from Java.
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
 * 2. Runnable Interface
 * 
 * Kotlin can use Java's Runnable interface, but with Kotlin's more concise syntax.
 * Kotlin's lambda expressions make it easier to create Runnables.
 */
fun runnableInterfaceExample() {
    println("2. Runnable Interface Example")
    
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
 * 3. Coroutine Basics
 * 
 * Coroutines are Kotlin's approach to asynchronous programming.
 * They are lightweight threads that can be suspended and resumed.
 * Advantages:
 * - Much lighter weight than threads (can create thousands of coroutines)
 * - Built-in cancellation support
 * - Structured concurrency
 * - Suspension points make code more readable than callbacks
 */
fun coroutineBasicsExample() = runBlocking {
    println("3. Coroutine Basics Example")
    
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
 * 4. Coroutine Context and Dispatchers
 * 
 * Coroutines run in a context that defines various aspects of their behavior.
 * Dispatchers determine which thread(s) the coroutine runs on.
 * Common dispatchers:
 * - Dispatchers.Default: CPU-intensive work
 * - Dispatchers.IO: I/O operations
 * - Dispatchers.Main: UI thread (in Android/JavaFX)
 */
fun coroutineContextExample() = runBlocking {
    println("4. Coroutine Context and Dispatchers Example")
    
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
 * 5. Flow
 * 
 * Flow is Kotlin's approach to reactive programming with coroutines.
 * It's designed for handling streams of asynchronous data.
 * Advantages:
 * - Cold streams (only emit when collected)
 * - Backpressure handling
 * - Cancellation support
 * - Composable operations (map, filter, etc.)
 */
fun coroutineFlowExample() = runBlocking {
    println("5. Flow Example")
    
    // Create a flow that emits numbers with delays
    val flow = flow {
        for (i in 1..3) {
            delay(100) // Pretend we're doing something time-consuming
            emit(i) // Emit the next value
        }
    }
    
    // Collect and process the flow values
    println("Collecting flow values:")
    flow.collect { value ->
        println("Received: $value")
    }
    
    // Demonstrate flow operators
    println("\nFlow with operators:")
    flow
        .map { it * it } // Square each value
        .filter { it > 1 } // Filter out small values
        .collect { value ->
            println("Processed: $value")
        }
    
    // Demonstrate flow combination
    println("\nCombining flows:")
    val flow1 = flowOf("A", "B", "C")
    val flow2 = flowOf(1, 2, 3)
    
    flow1.zip(flow2) { a, b -> "$a$b" }
        .collect { println(it) }
    
    println()
}