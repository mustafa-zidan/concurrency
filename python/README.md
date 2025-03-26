# Python Threading and Concurrency Examples

This directory contains examples of threading and concurrency patterns in Python, organized by difficulty level.

## Overview

Python offers several approaches to concurrency:

1. **Threading Module**: Traditional threading with the Global Interpreter Lock (GIL)
2. **Multiprocessing**: True parallelism using separate processes
3. **Asyncio**: Asynchronous I/O using coroutines
4. **Concurrent.futures**: High-level interface for asynchronously executing callables

These examples demonstrate each approach and explain their strengths, weaknesses, and appropriate use cases.

## Directory Structure

- **beginner/**: Basic threading examples suitable for those new to Python concurrency
- **intermediate/**: More advanced threading concepts and synchronization
- **advanced/**: Complex concurrency patterns using multiprocessing and asyncio

## Examples

### Beginner Level

1. **Threading Module** (`01_threading_module.py`)
   - Basic thread creation and management
   - Thread class and Thread objects
   - Starting, joining, and daemon threads
   - Thread lifecycle management

### Intermediate Level

1. **Thread Synchronization** (`01_thread_synchronization.py`)
   - Locks, RLocks, and Semaphores
   - Conditions and Events
   - Queues for thread-safe communication
   - Avoiding race conditions

2. **Concurrent Futures** (`02_concurrent_futures_example.py`)
   - ThreadPoolExecutor for managing thread pools
   - Submitting tasks and getting results
   - Map and as_completed functions
   - Callbacks and exception handling

3. **Python Threading Models Overview** (`03_python_threading_models.py`)
   - Comprehensive overview of different threading and concurrency models
   - Comparison of different approaches
   - When to use each model

### Advanced Level

1. **Multiprocessing** (`01_multiprocessing_example.py`)
   - Process creation and management
   - Process pools
   - Shared memory and synchronization
   - Inter-process communication

2. **Asyncio** (`02_asyncio_example.py`)
   - Coroutines and event loops
   - Async/await syntax
   - Tasks and Futures
   - Asynchronous I/O operations

## How to Run

To run these examples, you need Python 3.6 or higher.

```bash
# Run a specific example
python src/python/beginner/01_threading_module.py

# Or navigate to the src directory and run
cd src
python python/beginner/01_threading_module.py
```

## Learning Path

For the best learning experience, it's recommended to go through the examples in the following order:

1. Start with the beginner examples to understand the basics of Python threading
2. Move on to the intermediate examples to learn about thread synchronization and concurrent.futures
3. Finally, explore the advanced examples to master multiprocessing and asyncio

Each example includes detailed comments explaining the concepts and code.

## Important Notes

- **Global Interpreter Lock (GIL)**: Python's GIL prevents multiple threads from executing Python bytecode simultaneously. This means that threading in Python is not suitable for CPU-bound tasks, but works well for I/O-bound tasks.
- **Multiprocessing**: For CPU-bound tasks, use the multiprocessing module to bypass the GIL.
- **Asyncio**: For I/O-bound tasks with many concurrent operations, asyncio often provides better performance than threading.

## Additional Resources

- [Python Threading Documentation](https://docs.python.org/3/library/threading.html)
- [Python Multiprocessing Documentation](https://docs.python.org/3/library/multiprocessing.html)
- [Python Asyncio Documentation](https://docs.python.org/3/library/asyncio.html)
- [Python Concurrent.futures Documentation](https://docs.python.org/3/library/concurrent.futures.html)