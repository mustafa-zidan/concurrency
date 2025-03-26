/**
 * This file demonstrates Batch Processing with Buffered Channels in Go.
 *
 * Batch processing allows for more efficient handling of data by processing
 * multiple items at once rather than one at a time.
 */

package advanced

import (
	"fmt"
	"time"
)

/**
 * Batch Processing with Buffered Channels
 *
 * This pattern collects individual items into batches before processing them,
 * which can be more efficient for operations with high setup/teardown costs.
 */
func BatchProcessingDemo() {
	fmt.Println("Batch Processing with Buffered Channels")

	// Create a source of items
	source := make(chan int)
	go func() {
		for i := 1; i <= 20; i++ {
			source <- i
			time.Sleep(10 * time.Millisecond)
		}
		close(source)
	}()

	// Create a channel for batches
	batchSize := 5
	batches := make(chan []int)

	// Collect items into batches
	go func() {
		batch := make([]int, 0, batchSize)

		for item := range source {
			batch = append(batch, item)

			// When batch is full, send it and create a new one
			if len(batch) >= batchSize {
				batches <- batch
				batch = make([]int, 0, batchSize)
			}
		}

		// Send any remaining items
		if len(batch) > 0 {
			batches <- batch
		}

		close(batches)
	}()

	// Process the batches
	for batch := range batches {
		fmt.Printf("Processing batch: %v\n", batch)
		time.Sleep(50 * time.Millisecond) // Simulate batch processing
	}

	fmt.Println()
}
