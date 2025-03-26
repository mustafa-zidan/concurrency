"""
This file demonstrates Asyncio (Asynchronous I/O) in Python.

Asyncio is a library for writing concurrent code using the async/await syntax.
It's particularly well-suited for I/O-bound tasks like network operations.
Asyncio uses a single-threaded event loop and cooperative multitasking.
"""

import asyncio
import time
import random
import aiohttp
import sys

async def main():
    print("Python Asyncio Example")
    print("======================")
    
    # Run examples
    await asyncio_example()
    await advanced_asyncio_example()  # Latest Python asyncio features

"""
Asyncio Basics

Asyncio is a library for writing concurrent code using the async/await syntax.
It's particularly well-suited for I/O-bound tasks like network operations.
"""
async def asyncio_example():
    print("1. Asyncio Example")
    
    async def fetch_data(name, delay):
        print(f"Coroutine {name}: starting")
        await asyncio.sleep(delay)  # Non-blocking sleep
        print(f"Coroutine {name}: finished after {delay}s")
        return f"Result from {name}"
    
    # Run coroutines concurrently
    print("Running coroutines concurrently:")
    tasks = [
        fetch_data("A", 1),
        fetch_data("B", 0.5),
        fetch_data("C", 1.5)
    ]
    
    # Wait for all tasks to complete and gather results
    results = await asyncio.gather(*tasks)
    print(f"Results: {results}")
    
    # Using asyncio.create_task
    print("\nUsing asyncio.create_task:")
    task1 = asyncio.create_task(fetch_data("X", 0.7))
    task2 = asyncio.create_task(fetch_data("Y", 0.3))
    
    # Do some other work while tasks are running
    print("Doing other work while tasks are running...")
    await asyncio.sleep(0.1)
    print("Other work completed")
    
    # Wait for tasks to complete
    result1 = await task1
    result2 = await task2
    print(f"Task results: {result1}, {result2}")
    
    print()

"""
Advanced Asyncio Features (Latest Python Features)

Python has enhanced its asyncio capabilities in recent versions:
- TaskGroups for structured concurrency (Python 3.11+)
- asyncio.to_thread for running blocking code in a separate thread (Python 3.9+)
- Improved cancellation and timeout handling
- Enhanced exception handling
- Better debugging support
"""
async def advanced_asyncio_example():
    print("2. Advanced Asyncio Features (Latest)")
    
    # TaskGroups for structured concurrency (Python 3.11+)
    print("Using TaskGroups for structured concurrency (Python 3.11+):")
    
    async def worker(name, delay):
        print(f"Worker {name} starting")
        await asyncio.sleep(delay)
        print(f"Worker {name} finished after {delay}s")
        return f"Result from worker {name}"
    
    # Check if TaskGroup is available (Python 3.11+)
    if sys.version_info >= (3, 11):
        async with asyncio.TaskGroup() as tg:
            # Create tasks that will be automatically awaited when the context exits
            task1 = tg.create_task(worker("A", 0.5))
            task2 = tg.create_task(worker("B", 0.3))
            task3 = tg.create_task(worker("C", 0.7))
            
            print("All tasks created in the TaskGroup")
        
        # All tasks are now complete
        print(f"Results: {task1.result()}, {task2.result()}, {task3.result()}")
    else:
        print("TaskGroup not available (requires Python 3.11+)")
        # Fallback to gather
        tasks = [
            worker("A", 0.5),
            worker("B", 0.3),
            worker("C", 0.7)
        ]
        results = await asyncio.gather(*tasks)
        print(f"Results using gather: {results}")
    
    # asyncio.to_thread for running blocking code (Python 3.9+)
    print("\nUsing asyncio.to_thread (Python 3.9+):")
    
    def blocking_io():
        # This is a blocking function that would normally block the event loop
        print("Starting blocking I/O operation")
        time.sleep(1)  # Blocking sleep
        print("Blocking I/O operation completed")
        return "Blocking operation result"
    
    # Check if to_thread is available (Python 3.9+)
    if hasattr(asyncio, "to_thread"):
        # Run the blocking function in a separate thread
        result = await asyncio.to_thread(blocking_io)
        print(f"Result from to_thread: {result}")
    else:
        print("asyncio.to_thread not available (requires Python 3.9+)")
        # Fallback to run_in_executor
        loop = asyncio.get_event_loop()
        result = await loop.run_in_executor(None, blocking_io)
        print(f"Result from run_in_executor: {result}")
    
    # Improved timeout handling
    print("\nImproved timeout handling:")
    
    async def long_running_task():
        print("Long-running task started")
        await asyncio.sleep(2)
        print("Long-running task completed")
        return "Long task result"
    
    # Using asyncio.timeout context manager (Python 3.11+)
    if sys.version_info >= (3, 11):
        try:
            async with asyncio.timeout(1):
                result = await long_running_task()
                print(f"Task result: {result}")
        except TimeoutError:
            print("Task timed out using asyncio.timeout")
    else:
        # Fallback to wait_for
        try:
            result = await asyncio.wait_for(long_running_task(), timeout=1)
            print(f"Task result: {result}")
        except asyncio.TimeoutError:
            print("Task timed out using asyncio.wait_for")
    
    # Enhanced exception handling
    print("\nEnhanced exception handling:")
    
    async def task_with_exception():
        await asyncio.sleep(0.1)
        raise ValueError("Simulated error in task")
    
    async def handle_exceptions():
        try:
            await task_with_exception()
        except ValueError as e:
            print(f"Caught exception: {e}")
            # Re-raise a different exception
            raise RuntimeError("Converted exception") from e
    
    try:
        await handle_exceptions()
    except RuntimeError as e:
        print(f"Caught converted exception: {e}")
        print(f"Original exception: {e.__cause__}")
    
    # Asynchronous context managers
    print("\nAsynchronous context managers:")
    
    class AsyncResource:
        async def __aenter__(self):
            print("Acquiring resource asynchronously")
            await asyncio.sleep(0.1)
            print("Resource acquired")
            return self
        
        async def __aexit__(self, exc_type, exc_val, exc_tb):
            print("Releasing resource asynchronously")
            await asyncio.sleep(0.1)
            print("Resource released")
            return False  # Don't suppress exceptions
        
        async def use(self):
            print("Using resource")
            await asyncio.sleep(0.1)
    
    # Use the asynchronous context manager
    async with AsyncResource() as resource:
        await resource.use()
    
    # Asynchronous iteration
    print("\nAsynchronous iteration:")
    
    class AsyncCounter:
        def __init__(self, limit):
            self.limit = limit
            self.counter = 0
        
        def __aiter__(self):
            return self
        
        async def __anext__(self):
            if self.counter < self.limit:
                self.counter += 1
                await asyncio.sleep(0.1)
                return self.counter
            else:
                raise StopAsyncIteration
    
    # Use the asynchronous iterator
    async for i in AsyncCounter(3):
        print(f"Async iteration: {i}")
    
    # Asynchronous HTTP requests with aiohttp
    print("\nAsynchronous HTTP requests with aiohttp:")
    
    async def fetch_url(session, url):
        try:
            async with session.get(url) as response:
                return await response.text()
        except Exception as e:
            return f"Error fetching {url}: {e}"
    
    try:
        # Create a session
        async with aiohttp.ClientSession() as session:
            # Fetch multiple URLs concurrently
            urls = [
                "https://example.com",
                "https://httpbin.org/get",
                "https://httpbin.org/delay/1"
            ]
            
            tasks = [fetch_url(session, url) for url in urls]
            results = await asyncio.gather(*tasks)
            
            for i, result in enumerate(results):
                print(f"URL {urls[i]} response length: {len(result)} characters")
    except ImportError:
        print("aiohttp module not available")
    
    print()

if __name__ == "__main__":
    # Run the main coroutine
    asyncio.run(main())