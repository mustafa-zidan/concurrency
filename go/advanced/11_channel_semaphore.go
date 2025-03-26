/**
 * This file demonstrates using a Buffered Channel as a Semaphore in Go.
 *
 * A semaphore is a synchronization primitive that controls access to a shared resource.
 * In Go, buffered channels can be used as semaphores to limit concurrency.
 */

package advanced

import (
	"fmt"
	"time"
)

/**
 * Buffered Channel as a Semaphore
 *
 * This pattern uses a buffered channel to limit the number of goroutines
 * that can access a resource concurrently, similar to a counting semaphore.
 */
func ChannelSemaphoreDemo() {
	fmt.Println("Buffered Channel as a Semaphore")

	// Create a buffered channel as a semaphore with 3 slots
	semaphore := make(chan struct{}, 3)

	// Function that uses the semaphore to limit concurrency
	worker := func(id int) {
		fmt.Printf("Worker %d waiting for semaphore\n", id)
		semaphore <- struct{}{} // Acquire semaphore

		fmt.Printf("Worker %d acquired semaphore\n", id)
		time.Sleep(100 * time.Millisecond) // Simulate work

		<-semaphore // Release semaphore
		fmt.Printf("Worker %d released semaphore\n", id)
	}

	// Start 10 workers (but only 3 can run at a time)
	for i := 1; i <= 10; i++ {
		go worker(i)
	}

	// Wait for all workers to finish
	time.Sleep(500 * time.Millisecond)
	fmt.Println()
}
