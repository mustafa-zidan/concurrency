/**
 * This file demonstrates Select in Go.
 *
 * The select statement lets a goroutine wait on multiple communication operations.
 * It blocks until one of its cases can run, then executes that case.
 * If multiple cases are ready, it chooses one at random.
 */

package basic

import (
	"fmt"
	"time"
)

/**
 * Select Basics
 *
 * The select statement lets a goroutine wait on multiple communication operations.
 * It blocks until one of its cases can run, then executes that case.
 * If multiple cases are ready, it chooses one at random.
 */
func SelectDemo() {
	fmt.Println("Select Example")

	// Create channels
	c1 := make(chan string)
	c2 := make(chan string)

	// Send values on each channel
	go func() {
		time.Sleep(1 * time.Second)
		c1 <- "one"
	}()

	go func() {
		time.Sleep(2 * time.Second)
		c2 <- "two"
	}()

	// Use select to await both values simultaneously
	for i := 0; i < 2; i++ {
		select {
		case msg1 := <-c1:
			fmt.Println("Received", msg1)
		case msg2 := <-c2:
			fmt.Println("Received", msg2)
		}
	}

	// Select with timeout
	fmt.Println("\nSelect with timeout:")
	ch := make(chan string)

	go func() {
		time.Sleep(2 * time.Second)
		ch <- "result"
	}()

	select {
	case res := <-ch:
		fmt.Println("Received:", res)
	case <-time.After(1 * time.Second):
		fmt.Println("Timeout: operation took too long")
	}

	// Non-blocking select
	fmt.Println("Non-blocking select:")
	select {
	case msg := <-ch:
		fmt.Println("Received message:", msg)
	default:
		fmt.Println("No message available")
	}

	// Drain the channel
	<-ch
	fmt.Println()
}
