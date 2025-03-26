/**
 * This file demonstrates Atomic Operations in Go.
 *
 * Atomic operations provide low-level synchronization mechanisms that are
 * more efficient than mutexes for simple operations like incrementing counters.
 */

package advanced

import (
	"fmt"
	"sync"
	"sync/atomic"
)

/**
 * Atomic Operations
 *
 * This pattern uses atomic operations from the sync/atomic package for
 * thread-safe access to shared variables without the overhead of mutexes.
 */
func AtomicOperationsDemo() {
	fmt.Println("Atomic Operations")

	// Create an atomic counter
	var atomicCounter int64
	var wg sync.WaitGroup

	// Function to increment the atomic counter
	atomicIncrement := func() {
		defer wg.Done()

		for i := 0; i < 1000; i++ {
			// Atomically increment the counter
			atomic.AddInt64(&atomicCounter, 1)
		}
	}

	// Launch 10 goroutines
	for i := 0; i < 10; i++ {
		wg.Add(1)
		go atomicIncrement()
	}

	// Wait for all goroutines to finish
	wg.Wait()

	fmt.Printf("Final atomic counter value: %d\n", atomicCounter)

	// Compare-and-swap atomic operation
	fmt.Println("\nCompare-and-swap atomic operation:")

	var value int64 = 100

	// Try to swap with wrong expected value
	swapped := atomic.CompareAndSwapInt64(&value, 200, 300)
	fmt.Printf("Swap with wrong expected value: swapped=%v, value=%d\n", swapped, value)

	// Try to swap with correct expected value
	swapped = atomic.CompareAndSwapInt64(&value, 100, 300)
	fmt.Printf("Swap with correct expected value: swapped=%v, value=%d\n", swapped, value)

	fmt.Println()
}
