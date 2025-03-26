/**
 * This file demonstrates Select with Send and Receive Cases in Go.
 *
 * The select statement can handle both send and receive operations,
 * allowing for bidirectional communication with timeouts.
 */

package advanced

import (
	"fmt"
	"time"
)

/**
 * Select with Send and Receive Cases
 *
 * This pattern demonstrates how to use select to handle both sending and
 * receiving operations, with timeout handling for both directions.
 */
func SelectSendReceiveDemo() {
	fmt.Println("Select with Send and Receive Cases")

	// Create channels for sending and receiving
	requests := make(chan string)
	responses := make(chan string)

	// Start a worker that processes requests
	go func() {
		for {
			// Wait for a request
			req := <-requests

			// Process the request
			resp := "Response to: " + req

			// Send the response
			responses <- resp
		}
	}()

	// Function to send a request and get a response with timeout
	sendRequest := func(req string, timeout time.Duration) (string, bool) {
		// Send the request
		select {
		case requests <- req:
			// Request sent successfully
		case <-time.After(timeout):
			return "", false // Timeout sending request
		}

		// Wait for the response
		select {
		case resp := <-responses:
			return resp, true
		case <-time.After(timeout):
			return "", false // Timeout waiting for response
		}
	}

	// Send some requests
	for i := 1; i <= 3; i++ {
		req := fmt.Sprintf("Request %d", i)
		resp, ok := sendRequest(req, 500*time.Millisecond)

		if ok {
			fmt.Printf("Request: %s, Response: %s\n", req, resp)
		} else {
			fmt.Printf("Request: %s timed out\n", req)
		}
	}

	fmt.Println()
}
