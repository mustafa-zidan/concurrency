# Java Threading and Concurrency Examples

This directory contains examples of threading and concurrency patterns in Java, organized by difficulty level.

## Overview

Java provides robust support for multithreading through its built-in Thread class, Runnable interface, and the concurrency utilities in the `java.util.concurrent` package. These examples demonstrate the progression from basic threading concepts to advanced concurrency patterns.

## Directory Structure

- **beginner/**: Basic threading examples suitable for those new to Java concurrency
- **intermediate/**: More advanced threading concepts using the concurrency utilities
- **advanced/**: Complex concurrency patterns and modern Java concurrency features

## Examples

### Beginner Level

1. **Basic Thread Class** (`01_BasicThread.java`)
   - Creating threads by extending the Thread class
   - Starting and joining threads
   - Thread lifecycle management

2. **Runnable Interface** (`02_RunnableInterface.java`)
   - Creating threads by implementing the Runnable interface
   - Benefits of Runnable over extending Thread
   - Lambda expressions for concise thread creation

### Intermediate Level

1. **ExecutorService** (`01_ExecutorService.java`)
   - Thread pool management
   - Task submission and execution
   - Callable and Future for returning results

2. **Java Threading Models Overview** (`02_JavaThreadingModels.java`)
   - Comprehensive overview of different threading models
   - Comparison of different approaches

### Advanced Level

1. **CompletableFuture** (`01_CompletableFutureExample.java`)
   - Asynchronous programming with CompletableFuture
   - Chaining asynchronous operations
   - Exception handling in asynchronous code
   - Combining multiple asynchronous operations

## How to Run

To compile and run these examples, you need Java Development Kit (JDK) 8 or higher.

### Compiling

```bash
# Compile a specific example
javac src/java/beginner/01_BasicThread.java

# Compile all examples
find src/java -name "*.java" -exec javac {} \;
```

### Running

```bash
# Run a specific example (from the project root)
java -cp src src/java/beginner/01_BasicThread

# Or navigate to the src directory and run
cd src
java java/beginner/01_BasicThread
```

## Learning Path

For the best learning experience, it's recommended to go through the examples in the following order:

1. Start with the beginner examples to understand the basics of Java threading
2. Move on to the intermediate examples to learn about the concurrency utilities
3. Finally, explore the advanced examples to master complex concurrency patterns

Each example includes detailed comments explaining the concepts and code.