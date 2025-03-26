/**
 * This file demonstrates the Cancellation Pattern in Go.
 *
 * The cancellation pattern allows for gracefully stopping goroutines
 * when they are no longer needed, preventing resource leaks.
 */

package advanced

import (
	"fmt"
	"time"
)

/**
 * Context-like Cancellation Pattern
 *
 * This pattern provides a way to signal to goroutines that they should stop
 * their work and clean up. It's similar to how context.Context works in Go's
 * standard library, but implemented with a simple done channel.
 */
func CancellationPatternDemo() {
	fmt.Println("Context-like Cancellation Pattern")

	// Create a done channel for cancellation
	done := make(chan struct{})

	// Generator that can be canceled
	cancellableGen := func(done <-chan struct{}) <-chan int {
		out := make(chan int)

		go func() {
			defer close(out)

			for i := 0; ; i++ {
				select {
				case <-done:
					fmt.Println("Generator cancelled")
					return
				case out <- i:
					time.Sleep(100 * time.Millisecond)
				}
			}
		}()

		return out
	}

	// Start the generator
	ch := cancellableGen(done)

	// Receive some values
	for i := 0; i < 5; i++ {
		fmt.Println("Received:", <-ch)
	}

	// Cancel the generator
	fmt.Println("Cancelling generator...")
	close(done)

	// Give the generator time to exit
	time.Sleep(200 * time.Millisecond)
	fmt.Println()
}
