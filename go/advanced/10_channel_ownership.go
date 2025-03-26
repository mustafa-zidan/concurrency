/**
 * This file demonstrates Channel Ownership Pattern in Go.
 *
 * The channel ownership pattern establishes clear ownership and responsibility
 * for a channel, making code more maintainable and less prone to errors.
 */

package advanced

import (
	"fmt"
)

/**
 * Channel Ownership Pattern
 *
 * In this pattern, a function creates, owns, and returns a channel.
 * The owner is responsible for closing the channel when done.
 * Receivers only read from the channel and check when it's closed.
 */
func ChannelOwnershipDemo() {
	fmt.Println("Channel Ownership Pattern")

	// Generator function that owns and returns a channel
	generator := func(nums ...int) <-chan int {
		// Create and own the channel
		out := make(chan int)

		// Start a goroutine that sends values and closes the channel when done
		go func() {
			defer close(out) // Ensure the channel is closed when the goroutine exits

			for _, n := range nums {
				out <- n
			}
		}()

		// Return the receive-only channel to the caller
		return out
	}

	// Use the generator
	ch := generator(1, 2, 3, 4, 5)

	// Receive values until the channel is closed
	for n := range ch {
		fmt.Println("Received:", n)
	}

	fmt.Println()
}
