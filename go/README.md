# Go Concurrency Examples

This directory contains examples of concurrency patterns in Go, organized by difficulty level.

## Overview

Go was designed with concurrency as a first-class citizen. It provides two primary concurrency primitives:

1. **Goroutines**: Lightweight threads managed by the Go runtime
2. **Channels**: Type-safe pipes that allow goroutines to communicate and synchronize

Go's approach to concurrency is based on Communicating Sequential Processes (CSP), which emphasizes message passing rather than shared memory. This makes concurrent programming in Go simpler and less error-prone compared to traditional threading models.

## Directory Structure

- **basic/**: Fundamental concurrency patterns for those new to Go concurrency
- **advanced/**: Sophisticated concurrency patterns for more complex scenarios
- **main.go**: Entry point that demonstrates various concurrency examples
- **go_concurrency_internals.md**: Detailed explanation of Go's concurrency implementation

## Examples

### Basic Concurrency Patterns

1. **Goroutines** (`01_goroutines.go`)
   - Creating and running goroutines
   - Basic goroutine synchronization

2. **Channels** (`02_channels.go`)
   - Creating and using channels
   - Sending and receiving values
   - Channel blocking behavior

3. **Channel Buffering** (`03_channel_buffering.go`)
   - Buffered vs. unbuffered channels
   - Channel capacity and blocking

4. **Mutex** (`04_mutex.go`)
   - Mutual exclusion with sync.Mutex
   - Protecting shared resources

5. **Select** (`05_select.go`)
   - Multi-way concurrent control
   - Non-blocking channel operations
   - Timeouts

6. **Wait Group** (`06_wait_group.go`)
   - Waiting for multiple goroutines to complete
   - Basic synchronization patterns

### Advanced Concurrency Patterns

The advanced directory contains numerous sophisticated concurrency patterns (07-27) including:

7. **Atomic Operations** (`07_atomic_operations.go`)
   - Thread-safe counter implementations
   - Compare-and-swap operations

8. **Batch Processing** (`08_batch_processing.go`)
   - Processing items in batches for efficiency

9. **Cancellation Pattern** (`09_cancellation_pattern.go`)
   - Gracefully canceling goroutines

10. **Channel Ownership** (`10_channel_ownership.go`)
    - Patterns for managing channel ownership

11-27. **Additional Patterns**
    - Channel semaphores
    - Dropping channels
    - Dynamic buffer sizing
    - Fan-out/fan-in patterns
    - Error handling with wait groups
    - Worker pools
    - And many more sophisticated concurrency patterns

## How to Run

To run these examples, you need Go 1.11 or higher.

```bash
# Run the main program that demonstrates various examples
cd src/go
go run main.go

# Run a specific example
go run basic/01_goroutines.go

# Build and run all examples
go build -o concurrency_examples
./concurrency_examples
```

## Learning Path

For the best learning experience, it's recommended to go through the examples in the following order:

1. Start with the basic examples (01-06) to understand Go's concurrency primitives
2. Move on to the advanced examples (07-27) to learn sophisticated concurrency patterns
3. Explore the main.go file to see how these patterns can be combined

Each example includes detailed comments explaining the concepts and code.

## Go's Concurrency Philosophy

Go's approach to concurrency can be summarized by the slogan:

> "Do not communicate by sharing memory; instead, share memory by communicating."

This means that instead of using locks to protect shared memory (as in traditional threading models), Go encourages the use of channels to pass references to data between goroutines. This approach helps avoid many common concurrency pitfalls like race conditions and deadlocks.

## Additional Resources

- [Go Concurrency Patterns](https://blog.golang.org/pipelines)
- [Advanced Go Concurrency Patterns](https://blog.golang.org/advanced-go-concurrency-patterns)
- [Go Concurrency Visualized](https://divan.dev/posts/go_concurrency_visualize/)
- [Effective Go: Concurrency](https://golang.org/doc/effective_go.html#concurrency)