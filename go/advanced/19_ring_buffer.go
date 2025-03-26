/**
 * This file demonstrates the Ring Buffer Pattern in Go.
 *
 * A ring buffer (circular buffer) is a fixed-size buffer that wraps around
 * when it reaches its end. In Go, this can be implemented using a buffered channel.
 */

package advanced

import (
	"fmt"
)

/**
 * Ring Buffer Pattern
 *
 * This pattern implements a fixed-size circular buffer using a buffered channel.
 * When the buffer is full, adding a new item removes the oldest item.
 */
func RingBufferDemo() {
	fmt.Println("Ring Buffer Pattern")

	// Create a ring buffer using a buffered channel
	ringBuffer := make(chan int, 5)

	// Fill the buffer
	for i := 1; i <= 5; i++ {
		ringBuffer <- i
	}

	// Function to add a new item and return the oldest
	rotate := func(newValue int) int {
		// Get the oldest value
		oldest := <-ringBuffer

		// Add the new value
		ringBuffer <- newValue

		return oldest
	}

	// Rotate the buffer a few times
	for i := 6; i <= 10; i++ {
		oldest := rotate(i)
		fmt.Printf("Added %d, removed %d\n", i, oldest)
	}

	// Print the final state of the buffer
	fmt.Print("Final buffer state: ")
	close(ringBuffer)
	for n := range ringBuffer {
		fmt.Printf("%d ", n)
	}
	fmt.Println()
	fmt.Println()
}
