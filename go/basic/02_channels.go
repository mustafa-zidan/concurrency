/**
 * This file demonstrates Channel Basics in Go.
 *
 * Channels are the pipes that connect concurrent goroutines.
 * You can send values into channels from one goroutine and
 * receive those values in another goroutine.
 */

package basic

import (
	"fmt"
	"time"
)

// ChannelDemo Channel Basics
//
// Channels are the pipes that connect concurrent goroutines.
// You can send values into channels from one goroutine and
// receive those values in another goroutine.
func ChannelDemo() {
	fmt.Println("Channel Basics Example")

	// Create an unbuffered channel
	messages := make(chan string)

	// Send a message in a goroutine
	go func() {
		fmt.Println("Sending message to channel")
		messages <- "Hello, Channel!"
		fmt.Println("Message sent")
	}()

	// Receive the message
	msg := <-messages
	fmt.Println("Received message:", msg)

	// Channel as a synchronization mechanism
	done := make(chan bool)

	go func() {
		fmt.Println("Working...")
		time.Sleep(time.Second)
		fmt.Println("Done working")
		done <- true
	}()

	// Wait until work is done
	// note the channel here is blocking till something is sent
	<-done
	fmt.Println()
}
