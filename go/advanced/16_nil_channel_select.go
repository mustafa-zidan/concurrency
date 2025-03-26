/**
 * This file demonstrates Select with Nil Channel Pattern in Go.
 *
 * The nil channel select pattern allows for dynamically enabling and disabling
 * select cases by setting channels to nil, as nil channels always block.
 */

package advanced

import (
	"fmt"
	"time"
)

/**
 * Select with Nil Channel Pattern
 *
 * This pattern uses the fact that operations on nil channels block forever,
 * allowing you to dynamically disable select cases by setting channels to nil.
 */
func NilChannelSelectDemo() {
	fmt.Println("Select with Nil Channel Pattern")

	// Create channels
	var input chan string = make(chan string)
	var output chan string = make(chan string)

	// Start a goroutine that sends values on the input channel
	go func() {
		for i := 1; i <= 3; i++ {
			time.Sleep(100 * time.Millisecond)
			input <- fmt.Sprintf("Input %d", i)
		}
		close(input)
	}()

	// Process values from input and send to output
	go func() {
		for val := range input {
			output <- "Processed: " + val
		}
		close(output)
	}()

	// Use nil channels to disable select cases
	for {
		var inputCh chan string = input
		var outputCh chan string = nil
		var inputVal string

		// If we have a value to send, enable the output case
		if inputVal != "" {
			outputCh = output
		}

		select {
		case val, ok := <-inputCh:
			if !ok {
				// Input channel closed, disable it
				input = nil
				// If we have no more input and no pending value, we're done
				if inputVal == "" {
					fmt.Println("All processing complete")
					goto Done
				}
			} else {
				// Got a new input value
				fmt.Println("Received:", val)
				inputVal = val
				// Enable output channel for next iteration
				outputCh = output
			}

		case outputCh <- inputVal:
			// Value sent to output, clear pending value
			fmt.Println("Sent:", inputVal)
			inputVal = ""
		}
	}

Done:
	fmt.Println()
}
