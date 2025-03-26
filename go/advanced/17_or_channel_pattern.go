/**
 * This file demonstrates the Or-channel Pattern in Go.
 *
 * The or-channel pattern combines multiple channels into one that closes
 * when any of the input channels close. It's useful for implementing
 * "first response wins" scenarios.
 */

package advanced

import (
	"fmt"
	"time"
)

/**
 * Or-channel Pattern (First Response Wins)
 *
 * This pattern allows you to wait for the first of multiple operations to complete.
 * It's useful for implementing timeouts, cancellation, or taking the fastest result.
 */
func OrChannelPatternDemo() {
	fmt.Println("Or-channel Pattern (First Response Wins)")

	// Function that creates a channel that closes after a specified duration
	sig := func(after time.Duration) <-chan struct{} {
		c := make(chan struct{})
		go func() {
			defer close(c)
			time.Sleep(after)
		}()
		return c
	}

	// Or function combines multiple channels into one that closes when any input channel closes
	or := func(channels ...<-chan struct{}) <-chan struct{} {
		out := make(chan struct{})

		// Start a goroutine for each input channel
		for _, c := range channels {
			go func(ch <-chan struct{}) {
				select {
				case <-ch:
					close(out) // First channel to close triggers output channel to close
				case <-out:
					// Another channel already triggered the close
				}
			}(c)
		}

		return out
	}

	// Create some signal channels with different timeouts
	start := time.Now()
	<-or(
		sig(100*time.Millisecond),
		sig(200*time.Millisecond),
		sig(300*time.Millisecond),
	)
	fmt.Printf("Done after %v\n", time.Since(start))
	fmt.Println()
}
