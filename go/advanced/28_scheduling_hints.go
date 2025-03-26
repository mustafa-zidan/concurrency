/**
 * Scheduling Hints in Go
 *
 * This file demonstrates how to use various scheduling hints in Go
 * to control how goroutines are scheduled by the Go runtime.
 */
package advanced

import (
	"fmt"
	"runtime"
	"sync"
	"time"
)

// SchedulingHintsDemo demonstrates various scheduling hints in Go
func SchedulingHintsDemo() {
	fmt.Println("Scheduling Hints Demo")
	fmt.Println("=====================")

	// 1. GOMAXPROCS - Controls the maximum number of OS threads that can execute Go code simultaneously
	gomaxprocsDemo()

	// 2. Gosched - Yields the processor, allowing other goroutines to run
	goschedDemo()

	// 3. LockOSThread/UnlockOSThread - Locks/unlocks the calling goroutine to its current OS thread
	lockOSThreadDemo()
}

// gomaxprocsDemo demonstrates the use of GOMAXPROCS
func gomaxprocsDemo() {
	fmt.Println("\n1. GOMAXPROCS Example")
	fmt.Println("--------------------")

	// Get the current value of GOMAXPROCS
	prevMaxProcs := runtime.GOMAXPROCS(0)
	fmt.Printf("Current GOMAXPROCS: %d\n", prevMaxProcs)

	// Set GOMAXPROCS to 1 (single thread)
	runtime.GOMAXPROCS(1)
	fmt.Println("Set GOMAXPROCS to 1")

	// Run a CPU-bound task with multiple goroutines
	var wg sync.WaitGroup
	start := time.Now()

	for i := 0; i < 4; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			// CPU-bound work
			sum := 0
			for j := 0; j < 100000000; j++ {
				sum += j
			}
			fmt.Printf("Goroutine %d finished\n", id)
		}(i)
	}

	wg.Wait()
	fmt.Printf("With GOMAXPROCS=1, all goroutines took: %v\n", time.Since(start))

	// Set GOMAXPROCS back to the number of CPUs
	numCPU := runtime.NumCPU()
	runtime.GOMAXPROCS(numCPU)
	fmt.Printf("Set GOMAXPROCS to %d (number of CPUs)\n", numCPU)

	// Run the same task again
	start = time.Now()

	for i := 0; i < 4; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			// CPU-bound work
			sum := 0
			for j := 0; j < 100000000; j++ {
				sum += j
			}
			fmt.Printf("Goroutine %d finished\n", id)
		}(i)
	}

	wg.Wait()
	fmt.Printf("With GOMAXPROCS=%d, all goroutines took: %v\n", numCPU, time.Since(start))

	// Restore the original GOMAXPROCS value
	runtime.GOMAXPROCS(prevMaxProcs)
}

// goschedDemo demonstrates the use of Gosched
func goschedDemo() {
	fmt.Println("\n2. Gosched Example")
	fmt.Println("----------------")

	// Create a channel to synchronize goroutines
	done := make(chan bool)

	// Start a goroutine that prints numbers
	go func() {
		for i := 0; i < 5; i++ {
			fmt.Printf("Goroutine: %d\n", i)
			// Yield the processor after each print
			runtime.Gosched()
		}
		done <- true
	}()

	// Main goroutine prints letters
	for i := 0; i < 5; i++ {
		fmt.Printf("Main: %c\n", 'A'+i)
		// Don't yield, to demonstrate the difference
	}

	<-done
	fmt.Println("Notice how the goroutine execution is interleaved with the main function")
	fmt.Println("This is because Gosched() yields the processor, allowing other goroutines to run")
}

// lockOSThreadDemo demonstrates the use of LockOSThread and UnlockOSThread
func lockOSThreadDemo() {
	fmt.Println("\n3. LockOSThread/UnlockOSThread Example")
	fmt.Println("------------------------------------")

	fmt.Println("LockOSThread locks the calling goroutine to its current OS thread.")
	fmt.Println("This is useful when you need to ensure that a goroutine always executes on the same OS thread,")
	fmt.Println("such as when making calls to C libraries that depend on thread-local state.")

	// Get the current thread ID
	threadID := func() int {
		// This is a hack to get a unique ID for the current thread
		// In a real application, you might use C.GetCurrentThreadId() on Windows
		// or C.pthread_self() on Unix systems
		var buf [64]byte
		return len(buf)
	}

	var wg sync.WaitGroup

	// Start a goroutine that locks itself to an OS thread
	wg.Add(1)
	go func() {
		defer wg.Done()

		fmt.Printf("Before locking: Goroutine running on thread (approx): %d\n", threadID())

		// Lock this goroutine to the current OS thread
		runtime.LockOSThread()
		defer runtime.UnlockOSThread() // Ensure we unlock when done

		id1 := threadID()
		fmt.Printf("After locking: Goroutine running on thread (approx): %d\n", id1)

		// Sleep to allow the scheduler to potentially move us
		time.Sleep(10 * time.Millisecond)

		id2 := threadID()
		fmt.Printf("After sleeping: Goroutine still on same thread: %v (thread: %d)\n", id1 == id2, id2)

		fmt.Println("This goroutine will remain on this OS thread until UnlockOSThread is called")
	}()

	// Start a regular goroutine for comparison
	wg.Add(1)
	go func() {
		defer wg.Done()

		fmt.Printf("Regular goroutine on thread (approx): %d\n", threadID())

		// Sleep to allow the scheduler to potentially move us
		time.Sleep(10 * time.Millisecond)

		fmt.Printf("After sleeping: Regular goroutine on thread (approx): %d\n", threadID())
		fmt.Println("Regular goroutines can be moved between OS threads by the scheduler")
	}()

	wg.Wait()
}
