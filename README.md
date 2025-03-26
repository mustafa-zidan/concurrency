# Threading Models in Different Programming Languages

This repository contains examples of different threading and concurrency models in four programming languages:
- Java
- Kotlin
- Python
- Go

Each language has its own approach to concurrency, with different strengths and trade-offs. These examples demonstrate the various concurrency models and provide explanations of how they work.

## Repository Structure

The repository is organized by programming language, with each language having its own directory structure:

```
├── java/
│   ├── src/
│   │   └── java/
│   │       ├── beginner/
│   │       │   ├── 01_BasicThread.java
│   │       │   ├── 02_RunnableInterface.java
│   │       │   └── ...
│   │       ├── intermediate/
│   │       │   └── ...
│   │       └── advanced/
│   │           └── ...
│   └── THREADING_MODELS.md
├── kotlin/
│   ├── beginner/
│   │   ├── 01_BasicThread.kt
│   │   ├── 02_RunnableInterface.kt
│   │   └── ...
│   ├── intermediate/
│   │   ├── 01_CoroutineBasics.kt
│   │   ├── 02_KotlinThreadingModels.kt
│   │   └── ...
│   ├── advanced/
│   │   └── ...
│   ├── CoroutineContext.kt
│   ├── Flow.kt
│   └── KotlinThreadingModels.kt
├── python/
│   ├── beginner/
│   │   ├── 01_threading_module.py
│   │   └── ...
│   ├── intermediate/
│   │   └── ...
│   ├── advanced/
│   │   ├── 01_multiprocessing_example.py
│   │   └── ...
│   └── README.md
└── go/
    ├── basic/
    │   ├── 01_goroutines.go
    │   ├── 02_channels.go
    │   ├── 03_channel_buffering.go
    │   ├── 04_mutex.go
    │   ├── 05_select.go
    │   ├── 06_wait_group.go
    │   └── ...
    ├── advanced/
    │   └── ...
    ├── go_concurrency_internals.md
    └── README.md
```

Each language directory is organized by skill level (beginner, intermediate, advanced), with files following a consistent naming convention: a number prefix followed by an underscore and then the name of the concept being demonstrated.

## Java Threading Models

Java provides several approaches to concurrency:

1. **Basic Thread Class** - Creating threads by extending the Thread class
   - Also includes **Virtual Threads** (Java 21+) - Lightweight threads managed by the JVM
2. **Runnable Interface** - Implementing the Runnable interface (preferred approach)
   - Can also be used with Virtual Threads for lightweight concurrency
3. **ExecutorService** - Using thread pools and the executor framework
   - Includes **VirtualThreadPerTaskExecutor** (Java 21+) for efficient I/O-bound workloads
4. **CompletableFuture** - Modern functional approach to asynchronous programming
   - Can leverage Virtual Threads for better performance with many concurrent operations

To run the Java examples:
```bash
cd java/src/java/beginner
javac 01_BasicThread.java
java 01_BasicThread

# Similarly for other examples
javac 02_RunnableInterface.java
java 02_RunnableInterface

# For intermediate examples
cd ../intermediate
javac 01_ExecutorService.java
java 01_ExecutorService

# For advanced examples
cd ../advanced
javac 01_CompletableFutureExample.java
java 01_CompletableFutureExample
```

## Kotlin Threading Models

Kotlin supports both Java-style threading and its own coroutines system:

1. **Basic Thread Class** - Using Java's Thread class in Kotlin
   - Includes thread-local storage with Kotlin's improved syntax
2. **Runnable Interface** - Using Java's Runnable interface with Kotlin's concise syntax
   - Enhanced with Kotlin's scope functions for cleaner resource management
3. **Coroutine Basics** - Lightweight threads that can be suspended and resumed
   - Latest features include structured concurrency with SupervisorJob and improved cancellation
4. **Coroutine Context and Dispatchers** - Controlling which threads coroutines run on
   - Now with limitedParallelism for fine-grained control over thread usage
5. **Flow** - Reactive programming approach for handling streams of asynchronous data
   - Latest additions include StateFlow and SharedFlow for sharing flow data

To run the Kotlin examples (requires Kotlin and kotlinx.coroutines library):
```bash
cd kotlin

# For beginner examples
cd beginner
kotlinc -cp ../kotlinx-coroutines-core-1.6.4.jar 01_BasicThread.kt -include-runtime -d 01_BasicThread.jar
java -jar 01_BasicThread.jar

# Similarly for other beginner examples
kotlinc -cp ../kotlinx-coroutines-core-1.6.4.jar 02_RunnableInterface.kt -include-runtime -d 02_RunnableInterface.jar
java -jar 02_RunnableInterface.jar

# For intermediate examples
cd ../intermediate
kotlinc -cp ../kotlinx-coroutines-core-1.6.4.jar 01_CoroutineBasics.kt -include-runtime -d 01_CoroutineBasics.jar
java -jar 01_CoroutineBasics.jar

kotlinc -cp ../kotlinx-coroutines-core-1.6.4.jar 02_KotlinThreadingModels.kt -include-runtime -d 02_KotlinThreadingModels.jar
java -jar 02_KotlinThreadingModels.jar

# For root-level examples
cd ..
kotlinc -cp kotlinx-coroutines-core-1.6.4.jar CoroutineContext.kt -include-runtime -d CoroutineContext.jar
java -jar CoroutineContext.jar

kotlinc -cp kotlinx-coroutines-core-1.6.4.jar Flow.kt -include-runtime -d Flow.jar
java -jar Flow.jar
```

## Python Threading Models

Python offers several concurrency mechanisms:

1. **Threading Module** - Basic thread creation and usage
   - Latest features include improved thread-local storage and enhanced daemon thread behavior
2. **Thread Synchronization** - Using locks and thread-safe queues
   - Advanced features include RLock improvements and Barrier synchronization
3. **Multiprocessing** - Bypassing the GIL for CPU-bound tasks
   - Now with shared_memory module (Python 3.8+) for efficient data sharing
4. **Asyncio** - Asynchronous I/O with coroutines
   - Latest features include TaskGroups (Python 3.11+) and asyncio.to_thread (Python 3.9+)
5. **Concurrent.Futures** - High-level interface for async execution
   - Enhanced with improved exception handling and better integration with asyncio

To run the Python examples:
```bash
cd python

# Run beginner examples
cd beginner
python 01_threading_module.py

# Run intermediate examples
cd ../intermediate
python 01_thread_synchronization.py
python 02_asyncio_example.py

# Run advanced examples
cd ../advanced
python 01_multiprocessing_example.py
python 02_concurrent_futures_example.py
```

## Go Concurrency Models

Go has built-in concurrency features:

1. **Basic Goroutines** - Lightweight threads managed by the Go runtime
   - Latest features include improved runtime scheduler and better goroutine management
2. **WaitGroup** - Synchronizing multiple goroutines
   - Enhanced with patterns for dynamic task creation and error handling
3. **Channel Basics** - Communication between goroutines
   - Advanced patterns include channel ownership and fan-out/fan-in
4. **Channel Buffering** - Buffered channels and channel closing
   - Latest patterns include ring buffers and batch processing
5. **Select** - Waiting on multiple channel operations
   - Advanced features include priority selection and dynamic channel handling
6. **Mutex** - Preventing race conditions with mutual exclusion
   - Now with improved RWMutex and atomic operations

To run the Go examples:
```bash
cd go

# Run basic examples
cd basic
go run 01_goroutines.go
go run 02_channels.go
go run 03_channel_buffering.go
go run 04_mutex.go
go run 05_select.go
go run 06_wait_group.go

# Run advanced examples
cd ../advanced
# Run advanced examples as needed
```

## Key Differences Between Languages

### Java
- Uses OS threads directly
- Thread creation is relatively expensive
- Comprehensive concurrency utilities in java.util.concurrent
- No built-in channels or lightweight threads

### Kotlin
- Coroutines provide lightweight concurrency
- Structured concurrency model
- Suspension points make asynchronous code look synchronous
- Flow API for reactive programming

### Python
- Global Interpreter Lock (GIL) limits thread parallelism
- Asyncio provides cooperative multitasking
- Multiprocessing module for CPU-bound tasks
- Simple threading API but with performance limitations

### Go
- Goroutines are extremely lightweight (can create thousands)
- Channels provide built-in communication mechanism
- Select statement for multiplexing channels
- Designed for concurrency from the ground up

## Conclusion

Each language has its own approach to concurrency, with different strengths and weaknesses:

- **Java**: Mature, comprehensive concurrency utilities but heavier threads
- **Kotlin**: Coroutines provide elegant asynchronous programming
- **Python**: Simple API but limited by the GIL for CPU-bound tasks
- **Go**: Built for concurrency with lightweight goroutines and channels

The best approach depends on your specific use case, performance requirements, and the characteristics of your workload.
