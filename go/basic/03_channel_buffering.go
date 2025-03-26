/**
 * This file demonstrates Channel Buffering in Go.
 *
 * Buffered channels accept a limited number of values without a receiver.
 * This can be useful when you know how many values will be sent in advance.
 */

package basic

import (
	"fmt"
)

/**
 * BufferedChannelDemo Channel Buffering
 *
 * Buffered channels accept a limited number of values without a receiver.
 * This can be useful when you know how many values will be sent in advance.
 */
func BufferedChannelDemo() {
	fmt.Println("Channel Buffering Example")

	// Create a buffered channel with capacity 3
	ch := make(chan string, 3)

	// Send values to the buffered channel
	// These won't block because the channel has buffer space
	ch <- "buffered"
	ch <- "channel"
	ch <- "example"

	// Receive values
	fmt.Println(<-ch)
	fmt.Println(<-ch)
	fmt.Println(<-ch)

	// Demonstrate channel closing
	jobs := make(chan int, 5)
	done := make(chan bool)

	// Producer
	go func() {
		for i := 1; i <= 5; i++ {
			fmt.Println("Sending job", i)
			jobs <- i
		}
		close(jobs) // Close the channel when done sending
		fmt.Println("All jobs sent")
	}()

	// Consumer
	go func() {
		for {
			j, more := <-jobs
			if more {
				fmt.Println("Received job", j)
			} else {
				fmt.Println("All jobs received")
				done <- true
				return
			}
		}
	}()

	// Wait until all jobs are processed
	<-done
	fmt.Println()
}
