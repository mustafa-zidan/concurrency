"""
This file demonstrates the Concurrent.Futures module in Python.

The concurrent.futures module provides a high-level interface for asynchronously
executing callables using threads or processes.
It simplifies the use of threads and processes with features like:
- Thread and process pools
- Futures (a class representing the result of asynchronous execution)
- Callbacks
"""

import concurrent.futures
import threading
import multiprocessing
import time
import random
import os

def main():
    print("Python Concurrent.Futures Example")
    print("=================================")
    
    # Run examples
    concurrent_futures_example()
    advanced_concurrent_futures_example()  # Latest Python concurrent.futures features

"""
Concurrent.Futures Basics

The concurrent.futures module provides a high-level interface for asynchronously
executing callables using threads or processes.
"""
def concurrent_futures_example():
    print("1. Concurrent.Futures Example")
    
    # Define a function to be executed
    def worker(name, delay):
        print(f"Worker {name}: starting")
        time.sleep(delay)  # Simulate work
        print(f"Worker {name}: finishing")
        return f"Result from {name}"
    
    # Using ThreadPoolExecutor
    print("Using ThreadPoolExecutor:")
    with concurrent.futures.ThreadPoolExecutor(max_workers=3) as executor:
        # Submit tasks to the executor
        future1 = executor.submit(worker, "A", 1)
        future2 = executor.submit(worker, "B", 0.5)
        future3 = executor.submit(worker, "C", 1.5)
        
        # Get results as they complete
        for future in concurrent.futures.as_completed([future1, future2, future3]):
            result = future.result()
            print(f"Got result: {result}")
    
    # Using ProcessPoolExecutor
    print("\nUsing ProcessPoolExecutor:")
    with concurrent.futures.ProcessPoolExecutor(max_workers=3) as executor:
        # Map function to arguments
        results = executor.map(worker, ["X", "Y", "Z"], [0.7, 0.3, 0.5])
        
        # Process results in order
        for result in results:
            print(f"Got result: {result}")
    
    print()

"""
Advanced Concurrent.Futures Features (Latest Python Features)

Python has enhanced its concurrent.futures capabilities in recent versions:
- Improved exception handling
- Cancellation support
- Context manager enhancements
- Integration with asyncio
- Performance improvements
"""
def advanced_concurrent_futures_example():
    print("2. Advanced Concurrent.Futures Features (Latest)")
    
    # Exception handling
    print("Exception handling:")
    
    def task_with_exception(value):
        print(f"Task {value} starting")
        if value == 2:
            raise ValueError(f"Error in task {value}")
        time.sleep(0.1)
        return f"Result from task {value}"
    
    with concurrent.futures.ThreadPoolExecutor(max_workers=3) as executor:
        futures = [executor.submit(task_with_exception, i) for i in range(5)]
        
        for future in concurrent.futures.as_completed(futures):
            try:
                result = future.result()
                print(f"Success: {result}")
            except Exception as e:
                print(f"Task failed with exception: {e}")
    
    # Cancellation support
    print("\nCancellation support:")
    
    def long_running_task(name):
        print(f"Task {name} starting")
        for i in range(10):
            time.sleep(0.1)
            if threading.current_thread().is_alive():
                print(f"Task {name} step {i}")
            else:
                print(f"Task {name} was cancelled")
                return None
        return f"Result from task {name}"
    
    with concurrent.futures.ThreadPoolExecutor(max_workers=3) as executor:
        # Submit tasks
        future1 = executor.submit(long_running_task, "A")
        future2 = executor.submit(long_running_task, "B")
        future3 = executor.submit(long_running_task, "C")
        
        # Cancel one of the futures
        time.sleep(0.3)
        cancelled = future2.cancel()
        print(f"Cancellation of task B successful: {cancelled}")
        
        # Wait for remaining tasks
        for future in [future1, future3]:
            try:
                result = future.result()
                print(f"Task completed: {result}")
            except concurrent.futures.CancelledError:
                print("Task was cancelled")
    
    # Callbacks
    print("\nUsing callbacks:")
    
    def task(value):
        time.sleep(random.random())
        return value * value
    
    def callback(future):
        result = future.result()
        print(f"Callback received result: {result}")
    
    with concurrent.futures.ThreadPoolExecutor(max_workers=3) as executor:
        futures = []
        for i in range(5):
            future = executor.submit(task, i)
            future.add_done_callback(callback)
            futures.append(future)
        
        # Wait for all futures to complete
        concurrent.futures.wait(futures)
    
    # ProcessPoolExecutor with initializer
    print("\nProcessPoolExecutor with initializer:")
    
    # Global variable to demonstrate initialization
    global_data = None
    
    def initializer(data):
        global global_data
        global_data = data
        print(f"Process {os.getpid()} initialized with data: {global_data}")
    
    def process_with_global():
        return f"Process {os.getpid()} using global data: {global_data}"
    
    # Using initializer to set up each worker process
    with concurrent.futures.ProcessPoolExecutor(
        max_workers=3, 
        initializer=initializer, 
        initargs=("shared data",)
    ) as executor:
        futures = [executor.submit(process_with_global) for _ in range(3)]
        
        for future in concurrent.futures.as_completed(futures):
            print(future.result())
    
    # Integration with asyncio
    print("\nIntegration with asyncio:")
    
    import asyncio
    
    async def async_main():
        def cpu_bound_task(x):
            # Simulate CPU-bound work
            result = 0
            for i in range(10**6):
                result += i * x
            return result
        
        print("Running CPU-bound tasks with ProcessPoolExecutor from asyncio")
        
        # Get the event loop
        loop = asyncio.get_running_loop()
        
        # Create a ProcessPoolExecutor
        with concurrent.futures.ProcessPoolExecutor() as pool:
            # Schedule CPU-bound tasks to run concurrently
            results = await asyncio.gather(
                loop.run_in_executor(pool, cpu_bound_task, 1),
                loop.run_in_executor(pool, cpu_bound_task, 2),
                loop.run_in_executor(pool, cpu_bound_task, 3)
            )
            
            print(f"Results: {results}")
    
    try:
        # Run the async function
        asyncio.run(async_main())
    except (ImportError, AttributeError):
        print("asyncio integration not available or not supported")
    
    # ThreadPoolExecutor with thread name prefix
    print("\nThreadPoolExecutor with thread name prefix:")
    
    def report_thread_name():
        thread_name = threading.current_thread().name
        print(f"Running in thread: {thread_name}")
        return thread_name
    
    # Using thread_name_prefix to set custom thread names
    with concurrent.futures.ThreadPoolExecutor(
        max_workers=3, 
        thread_name_prefix="CustomWorker"
    ) as executor:
        futures = [executor.submit(report_thread_name) for _ in range(3)]
        
        for future in concurrent.futures.as_completed(futures):
            thread_name = future.result()
            print(f"Thread {thread_name} completed")
    
    # Shutdown with wait flag
    print("\nShutdown with wait flag:")
    
    executor = concurrent.futures.ThreadPoolExecutor(max_workers=2)
    
    # Submit some tasks
    futures = [executor.submit(time.sleep, 1) for _ in range(3)]
    
    # Shutdown with wait=True (default) to wait for tasks to complete
    print("Shutting down executor with wait=True")
    start_time = time.time()
    executor.shutdown(wait=True)
    end_time = time.time()
    print(f"Shutdown completed in {end_time - start_time:.2f} seconds")
    
    # Create another executor to demonstrate wait=False
    executor = concurrent.futures.ThreadPoolExecutor(max_workers=2)
    
    # Submit some tasks
    futures = [executor.submit(time.sleep, 1) for _ in range(3)]
    
    # Shutdown with wait=False to return immediately
    print("Shutting down executor with wait=False")
    start_time = time.time()
    executor.shutdown(wait=False)
    end_time = time.time()
    print(f"Shutdown returned in {end_time - start_time:.2f} seconds")
    
    # Wait a moment to let tasks complete
    time.sleep(2)
    
    print()

if __name__ == "__main__":
    main()