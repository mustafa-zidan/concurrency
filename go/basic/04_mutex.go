/**
 * This file demonstrates Mutex in Go.
 *
 * Mutexes allow us to lock access to data to prevent race conditions.
 * They provide a way to ensure that only one goroutine can access
 * a particular piece of data at a time.
 */

package basic

import (
	"fmt"
	"sync"
)

/**
 * Mutex Basics
 *
 * Mutexes allow us to lock access to data to prevent race conditions.
 * They provide a way to ensure that only one goroutine can access
 * a particular piece of data at a time.
 */
func MutexDemo() {
	fmt.Println("Mutex Example")

	// Create a counter and mutex
	var counter int
	var mutex sync.Mutex

	// Create a WaitGroup to wait for all goroutines
	var wg sync.WaitGroup

	// Function to increment the counter
	increment := func() {
		defer wg.Done()

		for i := 0; i < 1000; i++ {
			// Lock the mutex before accessing the counter
			mutex.Lock()
			counter++
			// Unlock the mutex after modifying the counter
			mutex.Unlock()
		}
	}

	// Launch 10 goroutines
	for i := 0; i < 10; i++ {
		wg.Add(1)
		go increment()
	}

	// Wait for all goroutines to finish
	wg.Wait()

	fmt.Printf("Final counter value: %d\n", counter)
	fmt.Println()
}
