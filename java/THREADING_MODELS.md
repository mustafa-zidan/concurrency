# Threading and Concurrency Models

This document provides an overview of the threading and concurrency models implemented in the examples for each programming language.

## Table of Contents
- [Java Threading Models](#java-threading-models)
- [Kotlin Threading Models](#kotlin-threading-models)
- [Python Threading Models](#python-threading-models)
- [Go Concurrency Models](#go-concurrency-models)

## Java Threading Models

### Beginner Level
1. **Basic Thread Class** (01_BasicThread.java)
   - The most fundamental way to create a thread in Java by extending the Thread class.
   - Simple but has limitations: doesn't allow for inheritance from other classes and tightly couples the task with the thread execution mechanism.
   - Suitable for simple applications with a small number of threads.

2. **Runnable Interface** (02_RunnableInterface.java)
   - The preferred way to create a thread by implementing the Runnable interface.
   - Separates the task (what to run) from the thread (how to run).
   - Allows a class to extend from another class while still being runnable in a thread.

### Intermediate Level
1. **ExecutorService** (01_ExecutorService.java)
   - Higher-level concurrency utilities from the java.util.concurrent package.
   - Manages thread creation, reuse, and lifecycle.
   - Provides thread pooling, task queuing, scheduled execution, and managed thread lifecycle.

2. **Java Threading Models Overview** (02_JavaThreadingModels.java)
   - Comprehensive overview of different threading models in Java.
   - Demonstrates the progression from basic to advanced threading concepts.

### Advanced Level
1. **CompletableFuture** (01_CompletableFutureExample.java)
   - Represents a future result of an asynchronous computation (Java 8+).
   - Provides a functional approach to handling asynchronous operations.
   - Supports chaining operations, combining multiple futures, exception handling, and functional programming style.

## Kotlin Threading Models

### Beginner Level
1. **Basic Thread** (01_BasicThread.kt)
   - Similar to Java's basic thread model, using Kotlin syntax.
   - Creates threads by extending the Thread class.

2. **Runnable Interface** (02_RunnableInterface.kt)
   - Implements the Runnable interface in Kotlin.
   - Shows how to use lambda expressions for concise thread creation.

### Intermediate Level
1. **Coroutine Basics** (01_CoroutineBasics.kt)
   - Introduction to Kotlin's coroutines, a lightweight threading alternative.
   - Demonstrates launching coroutines, suspending functions, and basic coroutine builders.

2. **Kotlin Threading Models Overview** (02_KotlinThreadingModels.kt)
   - Comprehensive overview of different threading and coroutine models in Kotlin.
   - Shows the progression from Java-style threading to Kotlin coroutines.

### Advanced Level
1. **Coroutine Context** (01_CoroutineContext.kt)
   - Advanced coroutine concepts including context, dispatchers, and job management.
   - Shows how to control coroutine execution and handle cancellation.

2. **Flow** (02_Flow.kt)
   - Kotlin's reactive streams implementation built on coroutines.
   - Demonstrates creating, transforming, and collecting flows.
   - Shows advanced flow operators and error handling.

## Python Threading Models

### Beginner Level
1. **Threading Module** (01_threading_module.py)
   - Basic threading using Python's threading module.
   - Shows how to create and start threads, and how to use thread subclasses.

### Intermediate Level
1. **Thread Synchronization** (01_thread_synchronization.py)
   - Demonstrates thread synchronization techniques using locks, semaphores, and conditions.
   - Shows how to handle shared resources and avoid race conditions.

2. **Concurrent Futures** (02_concurrent_futures_example.py)
   - High-level interface for asynchronously executing callables.
   - Provides thread and process pools for executing tasks.

3. **Python Threading Models Overview** (03_python_threading_models.py)
   - Comprehensive overview of different threading and concurrency models in Python.
   - Shows the progression from basic threading to advanced concurrency.

### Advanced Level
1. **Multiprocessing** (01_multiprocessing_example.py)
   - Parallel processing using the multiprocessing module.
   - Bypasses the Global Interpreter Lock (GIL) for true parallelism.
   - Shows process pools, shared memory, and inter-process communication.

2. **Asyncio** (02_asyncio_example.py)
   - Asynchronous I/O framework for writing single-threaded concurrent code.
   - Uses coroutines, event loops, and futures for non-blocking I/O operations.
   - Ideal for I/O-bound and high-level structured network code.

## Go Concurrency Models

### Basic Level
1. **Goroutines** (01_goroutines.go)
   - Lightweight threads managed by the Go runtime.
   - Much cheaper than OS threads, allowing thousands of concurrent goroutines.

2. **Channels** (02_channels.go)
   - Communication mechanism between goroutines.
   - Provides synchronization without explicit locks.

3. **Channel Buffering** (03_channel_buffering.go)
   - Buffered channels that can hold a limited number of values.
   - Allows sending without a corresponding receiver being ready.

4. **Mutex** (04_mutex.go)
   - Mutual exclusion locks for protecting shared resources.
   - Used when channels are not suitable for synchronization.

5. **Select** (05_select.go)
   - Allows waiting on multiple channel operations.
   - Provides non-blocking communication and timeouts.

6. **Wait Group** (06_wait_group.go)
   - Synchronization primitive to wait for a collection of goroutines to finish.

### Advanced Level
Go includes many advanced concurrency patterns (07-27) including:
- Atomic operations
- Batch processing
- Cancellation patterns
- Channel ownership
- Fan-out/fan-in patterns
- Dynamic buffer sizing
- Error handling with wait groups
- Worker pools
- And many more sophisticated concurrency patterns

The advanced examples demonstrate idiomatic Go concurrency patterns that solve complex synchronization problems elegantly.