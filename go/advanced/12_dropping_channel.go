/**
 * This file demonstrates the Dropping Channel Pattern in Go.
 *
 * The dropping channel pattern allows for non-blocking sends to a channel,
 * dropping messages when the channel is full rather than blocking.
 */

package advanced

import (
	"fmt"
)

/**
 * Dropping Channel Pattern (Non-blocking Sends)
 *
 * This pattern is useful when it's better to drop messages than to block
 * the sender. It's commonly used in high-throughput systems where
 * occasional message loss is acceptable.
 */
func DroppingChannelDemo() {
	fmt.Println("Dropping Channel Pattern (Non-blocking Sends)")

	// Create a buffered channel with limited capacity
	messages := make(chan string, 3)

	// Function to try sending a message without blocking
	trySend := func(msg string) bool {
		select {
		case messages <- msg:
			return true
		default:
			return false
		}
	}

	// Try sending several messages
	for i := 1; i <= 5; i++ {
		msg := fmt.Sprintf("Message %d", i)
		if trySend(msg) {
			fmt.Printf("Sent: %s\n", msg)
		} else {
			fmt.Printf("Dropped: %s (buffer full)\n", msg)
		}
	}

	// Receive all messages from the channel
	close(messages)
	for msg := range messages {
		fmt.Printf("Received: %s\n", msg)
	}

	fmt.Println()
}
