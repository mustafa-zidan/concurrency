/**
 * This file demonstrates Error Handling with WaitGroup in Go.
 *
 * This pattern shows how to collect errors from multiple goroutines
 * while still using a WaitGroup to synchronize their completion.
 */

package advanced

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

/**
 * Error Handling with WaitGroup
 *
 * This pattern uses a buffered channel to collect errors from multiple
 * goroutines, allowing for proper error handling in concurrent code.
 */
func WaitGroupErrorHandlingDemo() {
	fmt.Println("Error Handling with WaitGroup")

	// Create a WaitGroup and a channel for errors
	var wg sync.WaitGroup
	errorChan := make(chan error, 5) // Buffered channel to collect errors

	// Worker function that might return an error
	workerWithError := func(id int) {
		defer wg.Done()

		fmt.Printf("Worker %d starting\n", id)
		time.Sleep(time.Duration(rand.Intn(500)) * time.Millisecond)

		// Simulate an error in some workers
		if id%2 == 0 {
			err := fmt.Errorf("worker %d encountered an error", id)
			errorChan <- err
			fmt.Printf("Worker %d failed: %v\n", id, err)
			return
		}

		fmt.Printf("Worker %d completed successfully\n", id)
	}

	// Launch several workers
	for i := 1; i <= 5; i++ {
		wg.Add(1)
		go workerWithError(i)
	}

	// Wait for all workers to finish
	wg.Wait()

	// Close the error channel
	close(errorChan)

	// Check if any errors occurred
	var errors []error
	for err := range errorChan {
		errors = append(errors, err)
	}

	if len(errors) > 0 {
		fmt.Printf("Encountered %d errors:\n", len(errors))
		for _, err := range errors {
			fmt.Printf("- %v\n", err)
		}
	} else {
		fmt.Println("All workers completed without errors")
	}

	fmt.Println()
}
