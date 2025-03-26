/**
 * This file demonstrates the Priority Select Pattern in Go.
 *
 * The priority select pattern allows you to handle messages from multiple
 * channels with different priorities, ensuring higher priority messages
 * are processed first.
 */

package advanced

import (
	"fmt"
	"time"
)

/**
 * Priority Select Pattern
 *
 * This pattern implements a priority-based selection mechanism for channels.
 * It checks higher priority channels first before moving to lower priority ones.
 */
func PrioritySelectDemo() {
	fmt.Println("Priority Select Pattern")

	// Create channels with different priorities
	highPriority := make(chan string)
	mediumPriority := make(chan string)
	lowPriority := make(chan string)

	// Send data on all channels
	go func() {
		time.Sleep(100 * time.Millisecond)
		highPriority <- "High priority message"
		mediumPriority <- "Medium priority message"
		lowPriority <- "Low priority message"
	}()

	// Priority select implementation
	prioritySelect := func() {
		// First check high priority channel
		select {
		case msg := <-highPriority:
			fmt.Println("High priority:", msg)
			return
		default:
			// Continue to next priority level
		}

		// Then check medium priority channel
		select {
		case msg := <-mediumPriority:
			fmt.Println("Medium priority:", msg)
			return
		default:
			// Continue to next priority level
		}

		// Finally check low priority channel
		select {
		case msg := <-lowPriority:
			fmt.Println("Low priority:", msg)
			return
		default:
			fmt.Println("No messages available")
		}
	}

	// Wait for messages to be sent
	time.Sleep(200 * time.Millisecond)

	// Run the priority select
	prioritySelect()

	// Drain remaining channels
	fmt.Println("Draining remaining channels:")
	select {
	case msg := <-highPriority:
		fmt.Println("Remaining high priority:", msg)
	default:
		fmt.Println("No high priority messages left")
	}

	select {
	case msg := <-mediumPriority:
		fmt.Println("Remaining medium priority:", msg)
	default:
		fmt.Println("No medium priority messages left")
	}

	select {
	case msg := <-lowPriority:
		fmt.Println("Remaining low priority:", msg)
	default:
		fmt.Println("No low priority messages left")
	}

	fmt.Println()
}
