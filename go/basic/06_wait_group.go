/**
 * This file demonstrates WaitGroup in Go.
 *
 * WaitGroup is used to wait for a collection of goroutines to finish.
 * It provides a way to synchronize goroutines.
 */

package basic

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

/**
 * WaitGroup Basics
 *
 * WaitGroup is used to wait for a collection of goroutines to finish.
 * It provides a way to synchronize goroutines.
 */
func WaitGroupDemo() {
	fmt.Println("WaitGroup Example")

	// Create a WaitGroup
	var wg sync.WaitGroup

	// Define a worker function
	worker := func(id int) {
		defer wg.Done() // Decrement the counter when the goroutine completes

		fmt.Printf("Worker %d starting\n", id)
		time.Sleep(time.Duration(rand.Intn(1000)) * time.Millisecond)
		fmt.Printf("Worker %d done\n", id)
	}

	// Launch several workers
	for i := 1; i <= 5; i++ {
		wg.Add(1) // Increment the WaitGroup counter
		go worker(i)
	}

	// Wait for all workers to finish
	wg.Wait()
	fmt.Println()
}
