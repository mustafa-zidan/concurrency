/**
 * Main entry point for Go concurrency examples.
 *
 * This file provides a menu to run various concurrency examples
 * from both the basic and advanced packages.
 */

package main

import (
	"fmt"
	"os"
	"strconv"

	"threads/advanced"
	"threads/basic"
)

func main() {
	fmt.Println("Go Concurrency Examples")
	fmt.Println("======================")

	if len(os.Args) > 1 {
		// If command line argument is provided, run the specified example
		runExample(os.Args[1])
	} else {
		// Otherwise, show the menu
		showMenu()
	}
}

func showMenu() {
	fmt.Println("\nBasic Examples:")
	fmt.Println("1. Goroutines")
	fmt.Println("2. Channels")
	fmt.Println("3. Buffered Channels")
	fmt.Println("4. WaitGroup")
	fmt.Println("5. Select")
	fmt.Println("6. Mutex")

	fmt.Println("\nAdvanced Examples:")
	fmt.Println("11. Channel Ownership")
	fmt.Println("12. Fan-out, Fan-in")
	fmt.Println("13. Cancellation Pattern")
	fmt.Println("14. Or-channel Pattern")
	fmt.Println("15. Tee Channel Pattern")
	fmt.Println("16. Dynamic Buffer Sizing")
	fmt.Println("17. Channel as Semaphore")
	fmt.Println("18. Dropping Channel")
	fmt.Println("19. Ring Buffer")
	fmt.Println("20. Batch Processing")
	fmt.Println("21. Priority Select")
	fmt.Println("22. Select with Send/Receive")
	fmt.Println("23. Nil Channel Select")
	fmt.Println("24. RWMutex")
	fmt.Println("25. Atomic Operations")
	fmt.Println("26. Sync.Once")
	fmt.Println("27. Try Lock")
	fmt.Println("28. Scheduling Hints")
	fmt.Println("29. WaitGroup Error Handling")
	fmt.Println("30. Dynamic WaitGroup")
	fmt.Println("31. WaitGroup with Timeout")
	fmt.Println("32. Worker Pool")

	fmt.Println("\n0. Exit")

	fmt.Print("\nEnter your choice: ")
	var choice string
	fmt.Scanln(&choice)

	runExample(choice)
}

func runExample(choice string) {
	num, err := strconv.Atoi(choice)
	if err != nil {
		fmt.Println("Invalid choice. Please enter a number.")
		return
	}

	fmt.Println()

	switch num {
	// Basic examples
	case 1:
		basic.GoroutineDemo()
	case 2:
		basic.ChannelDemo()
	case 3:
		basic.BufferedChannelDemo()
	case 4:
		basic.WaitGroupDemo()
	case 5:
		basic.SelectDemo()
	case 6:
		basic.MutexDemo()

	// Advanced examples
	case 11:
		advanced.ChannelOwnershipDemo()
	case 12:
		advanced.FanOutFanInDemo()
	case 13:
		advanced.CancellationPatternDemo()
	case 14:
		advanced.OrChannelPatternDemo()
	case 15:
		advanced.TeeChannelPatternDemo()
	case 16:
		advanced.DynamicBufferSizingDemo()
	case 17:
		advanced.ChannelSemaphoreDemo()
	case 18:
		advanced.DroppingChannelDemo()
	case 19:
		advanced.RingBufferDemo()
	case 20:
		advanced.BatchProcessingDemo()
	case 21:
		advanced.PrioritySelectDemo()
	case 22:
		advanced.SelectSendReceiveDemo()
	case 23:
		advanced.NilChannelSelectDemo()
	case 24:
		advanced.RWMutexDemo()
	case 25:
		advanced.AtomicOperationsDemo()
	case 26:
		advanced.SyncOnceDemo()
	case 27:
		advanced.TryLockDemo()
	case 28:
		advanced.WaitGroupErrorHandlingDemo()
	case 29:
		advanced.DynamicWaitGroupDemo()
	case 30:
		advanced.WaitGroupTimeoutDemo()
	case 31:
		advanced.WorkerPoolDemo()

	case 0:
		fmt.Println("Exiting...")
		return

	default:
		fmt.Println("Invalid choice. Please try again.")
	}

	// After running an example, show the menu again
	showMenu()
}
