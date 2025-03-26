/**
 * This file demonstrates WaitGroup with Worker Pool Pattern in Go.
 *
 * The worker pool pattern creates a fixed number of worker goroutines
 * that process jobs from a shared queue, improving resource utilization.
 */

package advanced

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

/**
 * WaitGroup with Worker Pool Pattern
 *
 * This pattern uses a fixed pool of goroutines to process jobs from a channel,
 * limiting concurrency while efficiently processing a stream of work items.
 */
func WorkerPoolDemo() {
	fmt.Println("WaitGroup with Worker Pool Pattern")

	// Create a pool of worker goroutines
	numWorkers := 3
	jobs := make(chan int, 10)
	results := make(chan int, 10)
	var wg sync.WaitGroup

	// Worker function
	worker := func(id int) {
		defer wg.Done()

		fmt.Printf("Pool worker %d started\n", id)

		for job := range jobs {
			fmt.Printf("Worker %d processing job %d\n", id, job)
			time.Sleep(time.Duration(rand.Intn(500)) * time.Millisecond)
			results <- job * 2 // Simple job: double the input
		}

		fmt.Printf("Pool worker %d finished\n", id)
	}

	// Start the worker pool
	for i := 1; i <= numWorkers; i++ {
		wg.Add(1)
		go worker(i)
	}

	// Send jobs to the workers
	go func() {
		for i := 1; i <= 10; i++ {
			jobs <- i
		}
		close(jobs) // Signal workers that no more jobs are coming
	}()

	// Start a goroutine to close the results channel when all workers are done
	go func() {
		wg.Wait()
		close(results)
	}()

	// Collect and print results
	for result := range results {
		fmt.Printf("Got result: %d\n", result)
	}

	fmt.Println()
}
