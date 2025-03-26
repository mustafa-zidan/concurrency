# Kotlin Threading and Coroutines Examples

This directory contains examples of threading and coroutine patterns in Kotlin, organized by difficulty level.

## Overview

Kotlin provides two main approaches to concurrency:
1. Traditional Java-style threading (Thread, Runnable) which Kotlin fully supports
2. Coroutines - Kotlin's lightweight, more flexible approach to asynchronous programming

These examples demonstrate both approaches, with a focus on coroutines as they are the recommended way to handle concurrency in Kotlin.

## Directory Structure

- **beginner/**: Basic threading examples suitable for those new to Kotlin concurrency
- **intermediate/**: Introduction to coroutines and more advanced threading concepts
- **advanced/**: Complex coroutine patterns and features

## Examples

### Beginner Level

1. **Basic Thread Class** (`01_BasicThread.kt`)
   - Creating threads in Kotlin using the Thread class
   - Starting and joining threads
   - Thread lifecycle management

2. **Runnable Interface** (`02_RunnableInterface.kt`)
   - Creating threads by implementing the Runnable interface
   - Using Kotlin lambda expressions for concise thread creation
   - Benefits of Runnable over extending Thread

### Intermediate Level

1. **Coroutine Basics** (`01_CoroutineBasics.kt`)
   - Introduction to coroutines
   - Coroutine builders (launch, async)
   - Suspending functions
   - Structured concurrency

2. **Kotlin Threading Models Overview** (`02_KotlinThreadingModels.kt`)
   - Comprehensive overview of different threading and coroutine models
   - Comparison of different approaches

### Advanced Level

1. **Coroutine Context** (`01_CoroutineContext.kt`)
   - Coroutine context and dispatchers
   - Job management
   - Exception handling
   - Coroutine scopes

2. **Flow** (`02_Flow.kt`)
   - Kotlin's reactive streams implementation
   - Creating, transforming, and collecting flows
   - Flow operators
   - Error handling in flows

## How to Run

To compile and run these examples, you need Kotlin 1.3 or higher and the kotlinx-coroutines-core library.

### Setting Up

Make sure you have the Kotlin compiler installed. You can install it via SDKMAN:

```bash
sdk install kotlin
```

Or via Homebrew:

```bash
brew install kotlin
```

### Compiling and Running

```bash
# Compile a specific example
kotlinc -cp kotlinx-coroutines-core-1.6.4.jar src/kotlin/beginner/01_BasicThread.kt -d BasicThread.jar

# Run the compiled example
kotlin -cp kotlinx-coroutines-core-1.6.4.jar:BasicThread.jar MainKt
```

Alternatively, you can use an IDE like IntelliJ IDEA with Kotlin support to run the examples directly.

## Learning Path

For the best learning experience, it's recommended to go through the examples in the following order:

1. Start with the beginner examples to understand the basics of threading in Kotlin
2. Move on to the intermediate examples to learn about coroutines
3. Finally, explore the advanced examples to master complex coroutine patterns

Each example includes detailed comments explaining the concepts and code.

## Additional Resources

- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Kotlin Flow Guide](https://kotlinlang.org/docs/flow.html)
- [Kotlin Coroutines by Example](https://kotlinlang.org/docs/coroutines-by-example.html)