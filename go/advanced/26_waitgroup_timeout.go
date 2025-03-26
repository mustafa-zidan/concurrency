/**
 * This file demonstrates WaitGroup with Timeout Pattern in Go.
 *
 * This pattern shows how to implement a timeout when waiting for goroutines
 * to complete, rather than waiting indefinitely.
 */

package advanced

import (
	"fmt"
	"sync"
	"time"
)

/**
 * WaitGroup with Timeout Pattern
 *
 * This pattern combines WaitGroup with select and time.After to implement
 * a timeout when waiting for goroutines to complete.
 */
func WaitGroupTimeoutDemo() {
	fmt.Println("WaitGroup with Timeout Pattern")

	var wg sync.WaitGroup
	done := make(chan struct{})

	// Start some workers
	for i := 1; i <= 3; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()

			// Worker 2 will take longer than the timeout
			if id == 2 {
				time.Sleep(2 * time.Second)
			} else {
				time.Sleep(500 * time.Millisecond)
			}

			fmt.Printf("Worker %d completed\n", id)
		}(i)
	}

	// Create a goroutine to signal when all workers are done
	go func() {
		wg.Wait()
		close(done)
	}()

	// Wait with timeout
	select {
	case <-done:
		fmt.Println("All workers completed in time")
	case <-time.After(1 * time.Second):
		fmt.Println("Timeout waiting for workers")
	}

	// Wait a bit longer to let the remaining workers finish
	time.Sleep(1500 * time.Millisecond)
	fmt.Println()
}
