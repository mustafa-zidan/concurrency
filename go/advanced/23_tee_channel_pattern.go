/**
 * This file demonstrates the Tee Channel Pattern in Go.
 *
 * The tee channel pattern splits one input channel into multiple output channels,
 * similar to how the Unix 'tee' command works with streams.
 */

package advanced

import (
	"fmt"
	"time"
)

/**
 * Tee Channel Pattern (One Input, Multiple Outputs)
 *
 * This pattern allows you to send each value from an input channel
 * to multiple output channels, effectively duplicating the data stream.
 */
func TeeChannelPatternDemo() {
	fmt.Println("Tee Channel Pattern (One Input, Multiple Outputs)")

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

	// Tee function splits one channel into two
	tee := func(in <-chan int) (<-chan int, <-chan int) {
		out1 := make(chan int)
		out2 := make(chan int)

		go func() {
			defer close(out1)
			defer close(out2)

			for n := range in {
				// Need local copies of out channels for the select
				out1, out2 := out1, out2

				// Send to both channels
				for i := 0; i < 2; i++ {
					select {
					case out1 <- n:
						out1 = nil // Disable this case after successful send
					case out2 <- n:
						out2 = nil // Disable this case after successful send
					}
				}
			}
		}()

		return out1, out2
	}

	// Create an input channel
	input := gen(1, 2, 3)

	// Split it into two output channels
	out1, out2 := tee(input)

	// Receive from both output channels
	for i := 0; i < 3; i++ {
		fmt.Printf("out1: %d, out2: %d\n", <-out1, <-out2)
	}

	fmt.Println()
}
