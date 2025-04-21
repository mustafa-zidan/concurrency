package threads.util

/**
 * Utility class for thread-related operations in Kotlin.
 * This class provides common functionality used across the threading examples.
 */
object ThreadUtil {

    /**
     * Checks if the current Java version supports virtual threads (Java 21+).
     * 
     * @return true if virtual threads are supported, false otherwise
     */
    fun supportsVirtualThreads(): Boolean {
        return Runtime.version().feature() >= 21
    }
    
    /**
     * Prints a message indicating that virtual threads require Java 21 or later.
     * This is used when a virtual thread example is run on an older Java version.
     */
    fun printVirtualThreadsRequirement() {
        println("Virtual threads require Java 21 or later.")
        println("Current Java version: ${Runtime.version()}")
    }
    
    /**
     * Waits for the specified number of milliseconds.
     * This is a convenience method to handle InterruptedException.
     * 
     * @param millis the time to wait in milliseconds
     */
    fun sleep(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            System.err.println("Sleep interrupted: ${e.message}")
        }
    }
    
    /**
     * Checks if coroutines are available in the classpath.
     * This is used to provide informative messages when coroutine examples are run.
     * 
     * @return true if coroutines are available, false otherwise
     */
    fun supportsCoroutines(): Boolean {
        return try {
            // Try to access a coroutine class
            Class.forName("kotlinx.coroutines.CoroutineScope")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }
    
    /**
     * Prints a message indicating that coroutines require the kotlinx-coroutines-core dependency.
     * This is used when a coroutine example is run without the necessary dependency.
     */
    fun printCoroutinesRequirement() {
        println("Kotlin Coroutines require the kotlinx-coroutines-core dependency.")
        println("Add the following to your build.gradle:")
        println("implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3'")
    }
}