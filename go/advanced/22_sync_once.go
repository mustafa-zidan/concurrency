/**
 * This file demonstrates Sync.Once in Go.
 *
 * Sync.Once ensures that a function is executed only once, regardless of
 * how many goroutines attempt to execute it.
 */

package advanced

import (
	"fmt"
	"sync"
)

/**
 * Sync.Once for One-time Initialization
 *
 * This pattern ensures that initialization code runs exactly once,
 * even when called from multiple goroutines concurrently.
 */
func SyncOnceDemo() {
	fmt.Println("Sync.Once for One-time Initialization")

	var once sync.Once
	var onceValue int

	// Function that will only execute once
	initialize := func() {
		fmt.Println("Initializing...")
		onceValue = 42
	}

	// Call the initialization from multiple goroutines
	var wg sync.WaitGroup

	for i := 0; i < 5; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			fmt.Printf("Goroutine %d trying to initialize\n", id)
			once.Do(initialize)
			fmt.Printf("Goroutine %d sees value: %d\n", id, onceValue)
		}(i)
	}

	wg.Wait()
	fmt.Println()
}
