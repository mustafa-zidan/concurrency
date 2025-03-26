/**
 * This file demonstrates a Try Lock Pattern in Go.
 *
 * While Go's standard mutex doesn't have a built-in "try lock" feature,
 * this pattern implements a non-blocking lock attempt using channels.
 */

package advanced

import (
	"fmt"
)

/**
 * Try Lock Pattern
 *
 * This pattern implements a mutex-like structure that allows for non-blocking
 * lock acquisition attempts, returning success or failure immediately.
 */
func TryLockDemo() {
	fmt.Println("Try Lock Pattern")

	// Create a channel-based try lock
	tryLock := make(chan struct{}, 1)
	tryLock <- struct{}{} // Initialize as unlocked

	// Function to try to acquire the lock
	acquireLock := func() bool {
		select {
		case <-tryLock:
			return true // Lock acquired
		default:
			return false // Lock not acquired
		}
	}

	// Function to release the lock
	releaseLock := func() {
		select {
		case tryLock <- struct{}{}:
			// Lock released
		default:
			panic("Releasing an unlocked lock")
		}
	}

	// Try to acquire the lock
	if acquireLock() {
		fmt.Println("Lock acquired")
		// Do something with the locked resource
		releaseLock()
		fmt.Println("Lock released")
	} else {
		fmt.Println("Failed to acquire lock")
	}

	// Try again (should succeed)
	if acquireLock() {
		fmt.Println("Lock acquired again")
		releaseLock()
	}

	fmt.Println()
}
