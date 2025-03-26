/**
 * This file demonstrates Basic Goroutines in Go.
 *
 * In Go, concurrency is handled using goroutines, which are lightweight threads managed by the Go runtime.
 * Unlike OS threads, goroutines are multiplexed onto a smaller number of system threads, allowing you to run millions of them efficiently.
 */

package basic

import (
	"fmt"
	"time"
)

/**
 * GoroutineDemo Basic Goroutines
 *
 * Goroutines are lightweight threads managed by the Go runtime.
 * They are less expensive than OS threads, allowing thousands to run concurrently.
 * Notice that the goroutines are not guaranteed to run in any specific order.
 */
func GoroutineDemo() {
	fmt.Println("Basic Goroutine Example")

	// Start goroutines by adding the go keyword before the function call
	go sayHello("Alice")
	go sayHello("Bob")
	go sayHello("Charlie")

	// Anonymous function as a goroutine
	go func() {
		fmt.Println("Hello from an anonymous function!")
	}()

	// Sleep to allow goroutines to execute
	// In real code, you would use proper synchronization
	// as the functions may take more time than the Sleep time
	time.Sleep(100 * time.Millisecond)
	fmt.Println()
}

// A helper function
func sayHello(name string) {
	fmt.Printf("Hello, %s!\n", name)
}
