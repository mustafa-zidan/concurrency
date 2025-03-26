/**
 * This file demonstrates Dynamic Task Creation with WaitGroup in Go.
 *
 * This pattern shows how to handle dynamically created tasks with WaitGroup,
 * where the number of goroutines is not known in advance.
 */

package advanced

import (
	"fmt"
	"math/rand"
	"sync"
)

/**
 * Dynamic Task Creation with WaitGroup
 *
 * This pattern demonstrates how to use WaitGroup with recursively spawned
 * goroutines, ensuring all dynamically created tasks complete before continuing.
 */
func DynamicWaitGroupDemo() {
	fmt.Println("Dynamic Task Creation with WaitGroup")

	var wg sync.WaitGroup

	// Declare the parent worker function
	var parentWorker func(id int, depth int)

	// Define the parent worker function
	parentWorker = func(id int, depth int) {
		defer wg.Done()

		fmt.Printf("Parent worker %d (depth %d) starting\n", id, depth)

		// Base case for recursion
		if depth <= 0 {
			fmt.Printf("Parent worker %d reached max depth\n", id)
			return
		}

		// Spawn child workers
		numChildren := rand.Intn(3) + 1 // 1 to 3 children
		fmt.Printf("Parent worker %d spawning %d children\n", id, numChildren)

		for i := 0; i < numChildren; i++ {
			childID := id*10 + i
			wg.Add(1)
			go parentWorker(childID, depth-1)
		}

		fmt.Printf("Parent worker %d done\n", id)
	}

	// Start the initial parent workers
	for i := 1; i <= 2; i++ {
		wg.Add(1)
		go parentWorker(i, 2) // Maximum depth of 2
	}

	// Wait for all workers (parents and children) to finish
	wg.Wait()
	fmt.Println()
}
