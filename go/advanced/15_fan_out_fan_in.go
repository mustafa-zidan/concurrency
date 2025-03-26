/**
 * This file demonstrates the Fan-out, Fan-in Pattern in Go.
 *
 * Fan-out: Multiple goroutines read from the same channel, distributing work.
 * Fan-in: Multiple goroutines write to the same channel, combining results.
 * This pattern is useful for parallel processing of data.
 */

package advanced

import (
	"fmt"
	"time"
)

/**
 * Fan-out, Fan-in Pattern
 *
 * This pattern allows for parallel processing of data:
 * - Fan-out: Distribute work across multiple goroutines
 * - Fan-in: Collect and combine results from multiple goroutines
 */
func FanOutFanInDemo() {
	fmt.Println("Fan-out, Fan-in Pattern")

	// Generator function
	gen := func(nums ...int) <-chan int {
		out := make(chan int)
		go func() {
			defer close(out)
			for _, n := range nums {
				out <- n
				time.Sleep(100 * time.Millisecond) // Simulate slow generation
			}
		}()
		return out
	}

	// Worker function that squares its input
	square := func(in <-chan int) <-chan int {
		out := make(chan int)
		go func() {
			defer close(out)
			for n := range in {
				fmt.Printf("Worker squaring %d\n", n)
				time.Sleep(200 * time.Millisecond) // Simulate processing time
				out <- n * n
			}
		}()
		return out
	}

	// Fan-in function to combine multiple channels into one
	fanIn := func(channels ...<-chan int) <-chan int {
		out := make(chan int)

		// For each input channel, start a goroutine that forwards values
		for _, ch := range channels {
			go func(c <-chan int) {
				for n := range c {
					out <- n
				}
			}(ch)
		}

		// We need a way to close the output channel when all input channels are done
		// This is a simplified version that doesn't close the output channel
		// In a real application; you would use a WaitGroup to track when to close

		return out
	}

	// Create a single input channel
	input := gen(1, 2, 3, 4, 5)

	// Distribute work to 3 workers (fan-out)
	c1 := square(input)
	c2 := square(input)
	c3 := square(input)

	// Combine results (fan-in)
	for n := range fanIn(c1, c2, c3) {
		fmt.Println("Result:", n)

		// Break after receiving 5 results
		if n > 20 {
			break
		}
	}

	fmt.Println()
}
