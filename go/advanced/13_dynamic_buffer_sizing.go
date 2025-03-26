/**
 * This file demonstrates Dynamic Buffer Sizing in Go.
 *
 * Dynamic buffer sizing involves measuring and adjusting channel buffer sizes
 * to optimize performance based on workload characteristics.
 */

package advanced

import (
	"fmt"
	"time"
)

/**
 * Dynamic Buffer Sizing
 *
 * This pattern demonstrates how different buffer sizes affect performance
 * when dealing with producers and consumers operating at different speeds.
 */
func DynamicBufferSizingDemo() {
	fmt.Println("Dynamic Buffer Sizing")

	// Function to measure channel send blocking time with different buffer sizes
	measureBufferPerformance := func(bufferSize int, operations int) time.Duration {
		ch := make(chan int, bufferSize)

		start := time.Now()

		// Start a consumer that's slower than the producer
		go func() {
			for i := 0; i < operations; i++ {
				<-ch
				time.Sleep(1 * time.Millisecond) // Slow consumer
			}
		}()

		// Producer sends values as fast as possible
		for i := 0; i < operations; i++ {
			ch <- i
		}

		return time.Since(start)
	}

	// Test different buffer sizes
	bufferSizes := []int{1, 10, 100}
	operations := 100

	for _, size := range bufferSizes {
		duration := measureBufferPerformance(size, operations)
		fmt.Printf("Buffer size %d took %v for %d operations\n", size, duration, operations)
	}

	fmt.Println()
}
