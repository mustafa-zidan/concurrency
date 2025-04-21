package threads.advanced

import threads.util.ThreadUtil

/**
 * This file demonstrates the Coroutine Context in Kotlin.
 * 
 * USE CASE:
 * Coroutine Context is ideal for:
 * - Fine-grained control over coroutine execution
 * - Custom thread management for different types of work
 * - Proper error handling in concurrent code
 * - Debugging and monitoring coroutines
 * - Implementing custom coroutine scopes for specific components
 * - Controlling the lifecycle of related coroutines
 * 
 * LOW-LEVEL EXPLANATION:
 * The CoroutineContext is a persistent set of elements that influences coroutine behavior:
 * 1. Structure:
 *    a. Indexed set of elements, each with a unique Key
 *    b. Immutable, but can create new contexts by adding/replacing elements
 *    c. Main elements include Job, CoroutineName, CoroutineDispatcher, and CoroutineExceptionHandler
 * 2. Job:
 *    a. Represents the lifecycle of a coroutine
 *    b. Forms parent-child hierarchies
 *    c. Controls cancellation propagation
 *    d. Tracks completion state
 * 3. CoroutineDispatcher:
 *    a. Determines which thread(s) the coroutine executes on
 *    b. Manages thread switching during suspension/resumption
 *    c. Provides thread confinement or unconfined execution
 * 4. CoroutineName:
 *    a. Assigns a name for debugging purposes
 *    b. Appears in thread dumps and logging
 * 5. CoroutineExceptionHandler:
 *    a. Catches uncaught exceptions in coroutines
 *    b. Last resort for handling errors
 *    c. Only works for root coroutines
 * 
 * Advantages:
 * - Composable (can combine multiple context elements)
 * - Type-safe access to context elements
 * - Structured concurrency through job hierarchy
 * - Customizable behavior for different use cases
 * - Clean separation of concerns
 */

fun main() {
    println("Kotlin Coroutine Context Example")
    println("===============================")

    // Check if coroutines are available
    if (!ThreadUtil.supportsCoroutines()) {
        ThreadUtil.printCoroutinesRequirement()
        return
    }

    // Import coroutine libraries dynamically to avoid compilation errors if not available
    try {
        // This code simulates what would happen if coroutines were available
        // In a real project, you would directly use the imports at the top of the file
        println("1. Coroutine Dispatchers")
        println("   Dispatchers control which thread(s) the coroutine uses.")
        println("   Example code (requires kotlinx-coroutines-core dependency):")
        println()
        println("   import kotlinx.coroutines.*")
        println()
        println("   fun main() = runBlocking {")
        println("       // Launch coroutines with different dispatchers")
        println("       launch(Dispatchers.Default) {")
        println("           println(\"Default dispatcher: ${Thread.currentThread().name}\")")
        println("       }")
        println()
        println("       launch(Dispatchers.IO) {")
        println("           println(\"IO dispatcher: ${Thread.currentThread().name}\")")
        println("       }")
        println()
        println("       launch(Dispatchers.Unconfined) {")
        println("           println(\"Unconfined dispatcher (before): ${Thread.currentThread().name}\")")
        println("           delay(100)")
        println("           println(\"Unconfined dispatcher (after): ${Thread.currentThread().name}\")")
        println("       }")
        println()
        println("       // Create a single-threaded context")
        println("       val singleThreadContext = newSingleThreadContext(\"MyThread\")")
        println("       launch(singleThreadContext) {")
        println("           println(\"Single thread context: ${Thread.currentThread().name}\")")
        println("       }")
        println("       // Don't forget to close custom dispatchers when done")
        println("       singleThreadContext.close()")
        println("   }")
        println()
        
        println("2. Coroutine Job Hierarchy")
        println("   Jobs form parent-child relationships that affect cancellation and lifecycle.")
        println("   Example code:")
        println()
        println("   import kotlinx.coroutines.*")
        println()
        println("   fun main() = runBlocking {")
        println("       // Parent job")
        println("       val parentJob = launch {")
        println("           println(\"Parent job started\")")
        println("           ")
        println("           // Child job 1")
        println("           val child1 = launch {")
        println("               println(\"Child 1 started\")")
        println("               delay(1000)")
        println("               println(\"Child 1 completed\")")
        println("           }")
        println("           ")
        println("           // Child job 2")
        println("           val child2 = launch {")
        println("               println(\"Child 2 started\")")
        println("               delay(500)")
        println("               println(\"Child 2 completed\")")
        println("           }")
        println("           ")
        println("           // Wait for children to complete")
        println("           println(\"Parent waiting for children\")")
        println("       }")
        println("       ")
        println("       // Wait for parent (and all children) to complete")
        println("       parentJob.join()")
        println("       println(\"All jobs completed\")")
        println("   }")
        println()
        
        println("3. Coroutine Exception Handling")
        println("   CoroutineExceptionHandler catches uncaught exceptions in coroutines.")
        println("   Example code:")
        println()
        println("   import kotlinx.coroutines.*")
        println()
        println("   fun main() = runBlocking {")
        println("       // Create an exception handler")
        println("       val handler = CoroutineExceptionHandler { context, exception ->")
        println("           println(\"Caught exception: ${exception.message}\")")
        println("           println(\"In coroutine: ${context[CoroutineName]?.name}\")")
        println("       }")
        println("       ")
        println("       // Launch a coroutine with the exception handler")
        println("       val job = launch(handler + CoroutineName(\"errorProne\")) {")
        println("           println(\"Coroutine started\")")
        println("           throw RuntimeException(\"Simulated error\")")
        println("       }")
        println("       ")
        println("       // Try to join the job (will not throw because handler caught the exception)")
        println("       job.join()")
        println("       println(\"Continued after error\")")
        println("   }")
        println()
        
        println("4. Combining Context Elements")
        println("   Context elements can be combined using the + operator.")
        println("   Example code:")
        println()
        println("   import kotlinx.coroutines.*")
        println()
        println("   fun main() = runBlocking {")
        println("       // Create a combined context with multiple elements")
        println("       val combinedContext = Dispatchers.IO + CoroutineName(\"IOTask\") + ")
        println("           CoroutineExceptionHandler { _, exception ->")
        println("               println(\"Error in IO task: ${exception.message}\")")
        println("           }")
        println("       ")
        println("       // Launch a coroutine with the combined context")
        println("       launch(combinedContext) {")
        println("           println(\"Running in thread: ${Thread.currentThread().name}\")")
        println("           println(\"Coroutine name: ${coroutineContext[CoroutineName]?.name}\")")
        println("           ")
        println("           // Simulate an error")
        println("           if (Math.random() > 0.5) {")
        println("               throw RuntimeException(\"Random failure\")")
        println("           }")
        println("       }")
        println("   }")
        println()
        
        println("5. Custom Coroutine Context")
        println("   You can create custom context elements for specific needs.")
        println("   Example code:")
        println()
        println("   import kotlinx.coroutines.*")
        println("   import kotlin.coroutines.CoroutineContext")
        println()
        println("   // Define a custom context element")
        println("   class RequestId(val id: String) : CoroutineContext.Element {")
        println("       // Each context element type needs a unique key")
        println("       companion object Key : CoroutineContext.Key<RequestId>")
        println("       ")
        println("       // The key identifies the element type in the context")
        println("       override val key: CoroutineContext.Key<RequestId> = Key")
        println("   }")
        println()
        println("   fun main() = runBlocking {")
        println("       // Create a coroutine with the custom context element")
        println("       launch(RequestId(\"request-123\")) {")
        println("           // Access the custom context element")
        println("           val requestId = coroutineContext[RequestId]?.id")
        println("           println(\"Processing request: $requestId\")")
        println("           ")
        println("           // Child coroutines inherit the context")
        println("           launch {")
        println("               val childRequestId = coroutineContext[RequestId]?.id")
        println("               println(\"Child processing same request: $childRequestId\")")
        println("           }")
        println("       }")
        println("   }")
        
    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}