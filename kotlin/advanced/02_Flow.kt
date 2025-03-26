/**
 * This file demonstrates Flow in Kotlin.
 * 
 * USE CASE:
 * Flow is ideal for:
 * - Processing streams of data (network responses, sensor readings, user events)
 * - Implementing reactive UIs that respond to data changes
 * - Handling real-time updates from databases or APIs
 * - Building data processing pipelines with transformations
 * - When you need backpressure handling (slow consumers, fast producers)
 * - Applications requiring clean cancellation of data streams
 * - Replacing callback-based APIs with sequential code
 * 
 * LOW-LEVEL EXPLANATION:
 * Flow is built on top of coroutines and provides a reactive streams implementation:
 * 1. Flow Architecture:
 *    a. Flow is an interface with a single collect method
 *    b. FlowCollector receives values via its emit function
 *    c. Flows are cold - they only execute when collected
 *    d. Each collector gets its own fresh execution of the flow
 * 2. Execution Model:
 *    a. Flow builders (flow {}, flowOf(), etc.) create Flow instances
 *    b. Intermediate operators (map, filter, etc.) create wrapped Flow instances
 *    c. Terminal operators (collect, toList, etc.) start the collection process
 *    d. Values flow from upstream to downstream through the operator chain
 * 3. Suspension Mechanism:
 *    a. Flow leverages coroutine suspension for backpressure
 *    b. emit() is a suspending function that can pause if downstream is slow
 *    c. Flow operators execute in the collector's coroutine by default
 *    d. flowOn() operator changes the upstream context while preserving downstream context
 * 4. Cancellation:
 *    a. Flow collection respects coroutine cancellation
 *    b. When a collector's coroutine is cancelled, the flow stops emitting
 *    c. Cancellation is cooperative and checked at suspension points
 * 
 * Advantages:
 * - Cold streams (only emit when collected)
 * - Backpressure handling through coroutine suspension
 * - Cancellation support via coroutine cancellation
 * - Composable operations (map, filter, etc.)
 * - Type safety and exception handling
 * - Integration with structured concurrency
 */

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.milliseconds

fun main() = runBlocking {
    println("Kotlin Flow Example")
    println("===================")

    // Run examples
    coroutineFlowExample()
    advancedFlowExample() // Latest Kotlin Flow features
}

/**
 * Flow Basics
 * 
 * Flow is Kotlin's approach to reactive programming with coroutines.
 * It's designed for handling streams of asynchronous data.
 */
suspend fun coroutineFlowExample() = coroutineScope {
    println("1. Flow Example")

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

/**
 * Advanced Flow Features (Latest Kotlin Features)
 * 
 * USE CASE:
 * Advanced Flow features are essential for:
 * - State management in modern applications (StateFlow)
 * - Pub/sub patterns with multiple subscribers (SharedFlow)
 * - Real-time UI updates that need to reflect the latest state
 * - When you need to share data between multiple collectors
 * - Complex error handling scenarios in data streams
 * - Applications requiring fine-grained control over flow execution context
 * - When you need to handle lifecycle events in data streams
 * 
 * LOW-LEVEL EXPLANATION:
 * The latest Flow features provide sophisticated stream handling capabilities:
 * 1. StateFlow Implementation:
 *    a. StateFlow is a hot flow that always has a current value
 *    b. Implemented as an atomic reference with a conflated broadcast channel
 *    c. Only emits when the value actually changes (equality check)
 *    d. New subscribers immediately receive the current value
 * 2. SharedFlow Architecture:
 *    a. SharedFlow is a hot flow that can buffer and replay values
 *    b. Uses a ring buffer to store emitted values for replay
 *    c. Can be configured with replay cache size and buffer capacity
 *    d. Supports multiple concurrent collectors with a single upstream
 * 3. Context Preservation:
 *    a. flowOn() creates a boundary in the flow where context changes
 *    b. Upstream operations run in the specified context
 *    c. Downstream operations run in the collector's context
 *    d. Values are passed between contexts using channels
 * 4. Exception Handling:
 *    a. catch() operator intercepts exceptions from upstream
 *    b. Exceptions after catch() propagate to the collector
 *    c. onCompletion() observes both normal and exceptional completion
 *    d. Structured concurrency ensures proper resource cleanup
 * 
 * Latest features include:
 * - StateFlow and SharedFlow for sharing flow data
 * - Flow context preservation
 * - Flow exception handling
 * - Flow testing utilities
 * - Flow lifecycle integration
 * - Improved performance and memory usage
 * - Better integration with Android lifecycle components
 */
suspend fun advancedFlowExample() = coroutineScope {
    println("2. Advanced Flow Features (Latest)")

    // StateFlow - hot flow that always has a value
    println("Using StateFlow:")

    val mutableStateFlow = MutableStateFlow("Initial value")

    // Launch a coroutine to collect from the StateFlow
    val collectorJob = launch {
        mutableStateFlow.collect { value ->
            println("StateFlow collector received: $value")
        }
    }

    // Update the StateFlow value
    delay(100)
    mutableStateFlow.value = "Updated value"
    delay(100)
    mutableStateFlow.value = "Final value"

    // Cancel the collector after some time
    delay(100)
    collectorJob.cancel()

    // SharedFlow - hot flow that can have multiple subscribers
    println("\nUsing SharedFlow:")

    val mutableSharedFlow = MutableSharedFlow<String>(
        replay = 2, // Buffer size for replay to new subscribers
        extraBufferCapacity = 1 // Extra buffer capacity to avoid suspension
    )

    // Launch multiple collectors
    val collector1 = launch {
        mutableSharedFlow.collect { value ->
            println("Collector 1 received: $value")
        }
    }

    // Emit some values
    mutableSharedFlow.emit("First message")
    mutableSharedFlow.emit("Second message")

    // Add a second collector that will receive the replay buffer
    val collector2 = launch {
        mutableSharedFlow.collect { value ->
            println("Collector 2 received: $value")
        }
    }

    // Emit another value that both collectors will receive
    mutableSharedFlow.emit("Third message")

    // Cancel collectors
    delay(100)
    collector1.cancel()
    collector2.cancel()

    // Flow context preservation and exception handling
    println("\nFlow context preservation and exception handling:")

    // Create a flow with a specific context
    val flowWithContext = flow {
        println("Flow started in context: ${currentCoroutineContext()}")
        for (i in 1..3) {
            emit(i)
        }
    }.flowOn(Dispatchers.IO) // Upstream flow runs on IO dispatcher

    // Collect with exception handling
    try {
        flowWithContext
            .onEach { 
                println("Processing value $it on thread: ${Thread.currentThread().name}")
                if (it == 2) throw RuntimeException("Error on value $it")
            }
            .catch { e -> 
                // Handle exceptions in the upstream flow
                println("Caught exception: ${e.message}")
                emit(-1) // Emit a fallback value
            }
            .collect { value ->
                println("Collected value: $value")
            }
    } catch (e: Exception) {
        println("Uncaught exception: ${e.message}")
    }

    // Flow lifecycle and cancellation
    println("\nFlow lifecycle and cancellation:")

    val flow = flow {
        try {
            println("Flow started")
            for (i in 1..10) {
                delay(50)
                emit(i)
                println("Emitted: $i")
            }
        } finally {
            println("Flow completed or cancelled")
        }
    }

    // Launch a coroutine that will be cancelled
    val job = launch {
        flow
            .onStart { println("Collection started") }
            .onCompletion { cause -> 
                if (cause != null) {
                    println("Collection completed with exception: ${cause.message}")
                } else {
                    println("Collection completed normally")
                }
            }
            .collect { value ->
                println("Received: $value")
                if (value == 3) cancel() // Cancel when we receive 3
            }
    }

    job.join() // Wait for the job to complete

    // Latest Flow operators
    println("\nLatest Flow operators:")

    // Create a flow of delayed values
    val tickFlow = flow {
        for (i in 1..5) {
            delay(100)
            emit(i)
        }
    }

    // collectLatest - cancels the previous collection when a new value is emitted
    println("Using collectLatest:")
    tickFlow
        .onEach { println("Value $it emitted") }
        .collectLatest { value ->
            println("Collecting $value...")
            delay(150) // This takes longer than the emission rate
            println("Done collecting $value") // This will only be printed for the last value
        }

    // stateIn - converts a cold flow to a hot StateFlow
    println("\nUsing stateIn:")
    val stateFlow = flow {
        for (i in 1..3) {
            delay(100)
            emit("State $i")
        }
    }.stateIn(
        scope = this,
        started = SharingStarted.Eagerly,
        initialValue = "Initial state"
    )

    // Collect from the StateFlow
    launch {
        stateFlow.collect { println("StateFlow value: $it") }
    }

    // Wait for the flow to complete
    delay(500)

    println()
}
