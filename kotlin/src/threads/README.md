# Kotlin Concurrency Guide

This module provides a comprehensive guide to concurrency in Kotlin, from basic threads to advanced coroutines and Flow.

## Overview

Kotlin offers multiple approaches to concurrency:

1. **Java-style Threading**: Using Thread and Runnable (inherited from Java)
2. **Coroutines**: Kotlin's lightweight concurrency framework
3. **Flow**: Kotlin's solution for asynchronous data streams

This guide covers all three approaches, organized by complexity level.

## Beginner Level

### [01_BasicThread.kt](beginner/01_BasicThread.kt)
- Creating threads by extending the Thread class
- Using virtual threads (Java 21+)
- Understanding thread lifecycle and limitations

### [02_RunnableInterface.kt](beginner/02_RunnableInterface.kt)
- Implementing the Runnable interface
- Using Kotlin lambdas for concise thread creation
- Using virtual threads with Runnable (Java 21+)

## Intermediate Level

### [01_CoroutineBasics.kt](intermediate/01_CoroutineBasics.kt)
- Introduction to coroutines
- Coroutine builders (launch, async, runBlocking)
- Suspending functions
- Coroutine dispatchers
- Structured concurrency

### [02_KotlinThreadingModels.kt](intermediate/02_KotlinThreadingModels.kt)
- Overview of different threading models in Kotlin
- Comparison of Thread, Runnable, Coroutines, and Flow
- Guidance on when to use each approach

## Advanced Level

### [01_CoroutineContext.kt](advanced/01_CoroutineContext.kt)
- Coroutine context and its elements
- Dispatchers and thread management
- Job hierarchy and lifecycle
- Exception handling
- Custom context elements

### [02_Flow.kt](advanced/02_Flow.kt)
- Asynchronous data streams with Flow
- Flow operators for data transformation
- Context preservation and dispatchers
- Exception handling in flows
- Completion and cancellation
- StateFlow and SharedFlow

## Coroutines vs Threads

| Feature | Threads | Coroutines |
|---------|---------|------------|
| Weight | Heavyweight (1MB+ stack per thread) | Lightweight (few KB per coroutine) |
| Number | Limited by system resources | Can create millions |
| Blocking | Blocks the thread | Suspends without blocking |
| Switching | Expensive context switching | Cheap suspension/resumption |
| Cancellation | No built-in cancellation | Built-in cancellation support |
| Exception Handling | Try-catch blocks | Structured exception handling |
| API | Imperative | Mostly declarative |
| Debugging | Stack traces can be complex | Structured and readable stack traces |

## When to Use Each Approach

### Use Threads When:
- Working with legacy Java code
- Need direct control over thread behavior
- Implementing low-level concurrency primitives
- When coroutines are not available

### Use Coroutines When:
- Building modern Kotlin applications
- Need high scalability (thousands of concurrent operations)
- Working with asynchronous operations
- Need structured concurrency with parent-child relationships
- Want clean, sequential-looking code for async operations

### Use Flow When:
- Working with streams of data
- Need reactive programming patterns
- Processing asynchronous sequences
- Building real-time applications
- Handling UI events or continuous data sources

## Dependencies

To use coroutines and Flow in your project, add the following dependency:

```kotlin
// In build.gradle.kts
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
```

## Further Reading

- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Kotlin Flow Guide](https://kotlinlang.org/docs/flow.html)
- [Kotlin Concurrency Documentation](https://kotlinlang.org/docs/concurrency-overview.html)