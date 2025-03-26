/**
 * This file demonstrates RWMutex (Read-Write Mutex) in Go.
 *
 * RWMutex is a reader/writer mutual exclusion lock. The lock can be held by
 * any number of readers or a single writer, but not both simultaneously.
 */

package advanced

import (
	"fmt"
	"sync"
	"time"
)

/**
 * RWMutex (Read-Write Mutex)
 *
 * This pattern allows multiple readers to access shared data concurrently,
 * but ensures exclusive access for writers, improving performance when
 * reads are more common than writes.
 */
func RWMutexDemo() {
	fmt.Println("RWMutex (Read-Write Mutex)")

	// Create a shared resource
	var sharedData = make(map[string]int)
	var rwMutex sync.RWMutex
	var wg sync.WaitGroup

	// Writer function
	writer := func(key string, value int) {
		defer wg.Done()

		// Acquire write lock
		rwMutex.Lock()
		defer rwMutex.Unlock()

		fmt.Printf("Writing %s = %d\n", key, value)
		sharedData[key] = value
		time.Sleep(100 * time.Millisecond) // Simulate work
	}

	// Reader function
	reader := func(id int) {
		defer wg.Done()

		// Acquire read lock
		rwMutex.RLock()
		defer rwMutex.RUnlock()

		fmt.Printf("Reader %d: ", id)
		for k, v := range sharedData {
			fmt.Printf("%s=%d ", k, v)
		}
		fmt.Println()
		time.Sleep(50 * time.Millisecond) // Simulate work
	}

	// Start some writers
	for i := 0; i < 3; i++ {
		wg.Add(1)
		go writer(fmt.Sprintf("key%d", i), i*10)
	}

	// Start some readers
	for i := 0; i < 5; i++ {
		wg.Add(1)
		go reader(i)
	}

	// Wait for all goroutines to finish
	wg.Wait()
	fmt.Println()
}
