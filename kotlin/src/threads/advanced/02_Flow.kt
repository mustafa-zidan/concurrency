package threads.advanced

import threads.util.ThreadUtil

/**
 * This file demonstrates the Flow API in Kotlin.
 * 
 * USE CASE:
 * Flow is ideal for:
 * - Handling asynchronous streams of data
 * - Processing sequences of values asynchronously
 * - Reactive programming patterns
 * - UI events and updates
 * - Network responses that emit multiple values
 * - Sensor data or other continuous data sources
 * - Any scenario where you need to process a stream of values over time
 * 
 * LOW-LEVEL EXPLANATION:
 * Flow is a cold asynchronous data stream built on top of coroutines:
 * 1. Architecture:
 *    a. Producer: Creates and emits values (flow { ... })
 *    b. Intermediate operators: Transform values (map, filter, etc.)
 *    c. Terminal operators: Collect and process values (collect, toList, etc.)
 * 2. Cold Stream:
 *    a. Doesn't produce values until collected
 *    b. Produces fresh values for each collector
 *    c. Can be collected multiple times
 * 3. Backpressure:
 *    a. Automatically handled by coroutines' cooperative scheduling
 *    b. Producer suspends if collector is slow
 *    c. No need for explicit backpressure strategies
 * 4. Cancellation:
 *    a. Automatically cancels when the collector's coroutine is cancelled
 *    b. Respects structured concurrency
 *    c. Properly releases resources
 * 5. Exception Handling:
 *    a. Exceptions propagate through the flow pipeline
 *    b. Can be caught at collection site or with catch operator
 *    c. Follows structured concurrency principles
 * 
 * Advantages:
 * - Sequential code style for asynchronous streams
 * - Built on coroutines for lightweight concurrency
 * - Rich set of operators for data transformation
 * - Automatic backpressure handling
 * - Cancellation support
 * - Exception handling
 * - Composable and transformable
 */

fun main() {
    println("Kotlin Flow Example")
    println("=================")

    // Check if coroutines are available
    if (!ThreadUtil.supportsCoroutines()) {
        ThreadUtil.printCoroutinesRequirement()
        return
    }

    // Import coroutine libraries dynamically to avoid compilation errors if not available
    try {
        // This code simulates what would happen if coroutines were available
        // In a real project, you would directly use the imports at the top of the file
        println("1. Basic Flow Creation and Collection")
        println("   Flow allows you to represent a stream of values that are computed asynchronously.")
        println("   Example code (requires kotlinx-coroutines-core dependency):")
        println()
        println("   import kotlinx.coroutines.*")
        println("   import kotlinx.coroutines.flow.*")
        println()
        println("   fun main() = runBlocking {")
        println("       // Create a flow")
        println("       val flow = flow {")
        println("           for (i in 1..3) {")
        println("               delay(100) // Pretend we're doing something asynchronous")
        println("               emit(i) // Emit the next value")
        println("           }")
        println("       }")
        println()
        println("       // Collect the flow")
        println("       println(\"Collecting flow:\")")
        println("       flow.collect { value ->")
        println("           println(\"Received: $value\")")
        println("       }")
        println("   }")
        println()
        
        println("2. Flow Operators")
        println("   Flow provides a rich set of operators for transforming data streams.")
        println("   Example code:")
        println()
        println("   import kotlinx.coroutines.*")
        println("   import kotlinx.coroutines.flow.*")
        println()
        println("   fun main() = runBlocking {")
        println("       // Create a flow and apply operators")
        println("       val flow = flow {")
        println("           for (i in 1..10) {")
        println("               emit(i)")
        println("           }")
        println("       }")
        println("           .filter { it % 2 == 0 } // Keep only even numbers")
        println("           .map { it * it }        // Square each number")
        println("           .take(3)                // Take only the first 3 values")
        println()
        println("       // Collect the transformed flow")
        println("       flow.collect { value ->")
        println("           println(\"Processed value: $value\")")
        println("       }")
        println("       // Output: 4, 16, 36 (squares of 2, 4, 6)")
        println("   }")
        println()
        
        println("3. Flow Context and Dispatchers")
        println("   Flow respects structured concurrency and context preservation.")
        println("   Example code:")
        println()
        println("   import kotlinx.coroutines.*")
        println("   import kotlinx.coroutines.flow.*")
        println()
        println("   fun main() = runBlocking {")
        println("       // Create a flow")
        println("       val flow = flow {")
        println("           println(\"Flow started in ${Thread.currentThread().name}\")")
        println("           for (i in 1..3) {")
        println("               emit(i)")
        println("           }")
        println("       }")
        println()
        println("       // Collect on a different dispatcher")
        println("       withContext(Dispatchers.Default) {")
        println("           flow.collect { value ->")
        println("               println(\"Collected $value in ${Thread.currentThread().name}\")")
        println("           }")
        println("       }")
        println()
        println("       // Using flowOn to change the upstream context")
        println("       flow")
        println("           .flowOn(Dispatchers.IO) // Upstream flow runs on IO dispatcher")
        println("           .collect { value ->")
        println("               // Collection happens in the current context (Default)")
        println("               println(\"Collected $value in ${Thread.currentThread().name}\")")
        println("           }")
        println("   }")
        println()
        
        println("4. Flow Exception Handling")
        println("   Flow provides operators for handling exceptions in the stream.")
        println("   Example code:")
        println()
        println("   import kotlinx.coroutines.*")
        println("   import kotlinx.coroutines.flow.*")
        println()
        println("   fun main() = runBlocking {")
        println("       // Create a flow that throws an exception")
        println("       val flow = flow {")
        println("           emit(1)")
        println("           emit(2)")
        println("           throw RuntimeException(\"Error in flow\")")
        println("           emit(3) // This will never be reached")
        println("       }")
        println()
        println("       // Handle exceptions with catch operator")
        println("       flow")
        println("           .catch { e -> ")
        println("               println(\"Caught exception: ${e.message}\")")
        println("               emit(-1) // Emit a fallback value")
        println("           }")
        println("           .collect { value ->")
        println("               println(\"Received: $value\")")
        println("           }")
        println("       // Output: 1, 2, Caught exception: Error in flow, -1")
        println("   }")
        println()
        
        println("5. Flow Completion and Cancellation")
        println("   Flow supports completion actions and respects cancellation.")
        println("   Example code:")
        println()
        println("   import kotlinx.coroutines.*")
        println("   import kotlinx.coroutines.flow.*")
        println()
        println("   fun main() = runBlocking {")
        println("       // Create a flow with completion handling")
        println("       val job = launch {")
        println("           flow {")
        println("               try {")
        println("                   emit(1)")
        println("                   emit(2)")
        println("                   emit(3)")
        println("               } finally {")
        println("                   println(\"Flow completed or cancelled\")")
        println("               }")
        println("           }")
        println("           .onCompletion { cause ->")
        println("               if (cause != null)")
        println("                   println(\"Flow completed exceptionally: ${cause.message}\")")
        println("               else")
        println("                   println(\"Flow completed normally\")")
        println("           }")
        println("           .collect { value ->")
        println("               println(\"Collected $value\")")
        println("               if (value == 2) {")
        println("                   cancel() // Cancel after receiving 2")
        println("                   println(\"Cancelling collection\")")
        println("               }")
        println("           }")
        println("       }")
        println()
        println("       job.join() // Wait for the job to complete")
        println("   }")
        println()
        
        println("6. StateFlow and SharedFlow")
        println("   StateFlow and SharedFlow are hot flow variants for specific use cases.")
        println("   Example code:")
        println()
        println("   import kotlinx.coroutines.*")
        println("   import kotlinx.coroutines.flow.*")
        println()
        println("   fun main() = runBlocking {")
        println("       // StateFlow - always has a value, emits updates to new subscribers")
        println("       val stateFlow = MutableStateFlow(0) // Initial value")
        println()
        println("       // Launch a coroutine to collect from the StateFlow")
        println("       val job1 = launch {")
        println("           stateFlow.collect { value ->")
        println("               println(\"StateFlow collector 1: $value\")")
        println("           }")
        println("       }")
        println()
        println("       // Update the value")
        println("       delay(100)")
        println("       stateFlow.value = 1")
        println("       delay(100)")
        println()
        println("       // Launch another collector - it immediately receives the current value")
        println("       val job2 = launch {")
        println("           stateFlow.collect { value ->")
        println("               println(\"StateFlow collector 2: $value\")")
        println("           }")
        println("       }")
        println()
        println("       // Update again")
        println("       delay(100)")
        println("       stateFlow.value = 2")
        println("       delay(100)")
        println()
        println("       // Cancel both collectors")
        println("       job1.cancel()")
        println("       job2.cancel()")
        println()
        println("       // SharedFlow - configurable buffer, replay and subscription behavior")
        println("       val sharedFlow = MutableSharedFlow<Int>(replay = 2) // Keep last 2 values")
        println()
        println("       // Emit some values")
        println("       sharedFlow.emit(10)")
        println("       sharedFlow.emit(20)")
        println("       sharedFlow.emit(30) // This will push 10 out of the replay cache")
        println()
        println("       // New subscriber gets the replay values (20 and 30)")
        println("       launch {")
        println("           sharedFlow.collect { value ->")
        println("               println(\"SharedFlow collector: $value\")")
        println("           }")
        println("       }")
        println()
        println("       // Emit a new value")
        println("       delay(100)")
        println("       sharedFlow.emit(40)")
        println("       delay(100)")
        println("   }")
        
    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}