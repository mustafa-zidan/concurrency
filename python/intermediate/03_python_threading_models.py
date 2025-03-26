"""
This file demonstrates different threading and concurrency models in Python.
Python offers several approaches to concurrency, each with different trade-offs.
"""

import threading
import multiprocessing
import asyncio
import concurrent.futures
import time
import queue
import random


def main():
    print("Python Threading Models Examples")
    print("================================")

    # Run examples
    threading_module_example()
    thread_synchronization_example()
    multiprocessing_example()
    asyncio_example()
    concurrent_futures_example()


"""
1. Threading Module
"""
def threading_module_example():
    print("1. Threading Module Example")
    
    # Define a function to be executed in a thread
    def worker(name):
        print(f"Thread {name}: starting")
        time.sleep(1)  # Simulate work
        print(f"Thread {name}: finishing")
    
    # Create and start multiple threads
    threads = []
    for i in range(3):
        t = threading.Thread(target=worker, args=(i,))
        threads.append(t)
        t.start()
    
    # Wait for all threads to complete
    for t in threads:
        t.join()
    
    # Using a thread with a class
    class MyThread(threading.Thread):
        def __init__(self, name):
            super().__init__()
            self.name = name
        
        def run(self):
            print(f"MyThread {self.name}: starting")
            time.sleep(1)  # Simulate work
            print(f"MyThread {self.name}: finishing")
    
    # Create and start thread using the class
    my_thread = MyThread("A")
    my_thread.start()
    my_thread.join()
    
    print()


"""
2. Thread Synchronization
"""
def thread_synchronization_example():
    print("2. Thread Synchronization Example")
    
    # Shared resource
    counter = 0
    
    # Create a lock for synchronization
    lock = threading.Lock()
    
    def increment_counter(amount, thread_name):
        nonlocal counter
        
        for _ in range(amount):
            # Acquire the lock before modifying the shared resource
            with lock:
                current = counter
                time.sleep(0.001)  # Simulate some work
                counter = current + 1
                print(f"Thread {thread_name}: counter = {counter}")
    
    # Create threads that will increment the counter
    threads = []
    for i in range(3):
        t = threading.Thread(target=increment_counter, args=(3, i))
        threads.append(t)
        t.start()
    
    # Wait for all threads to complete
    for t in threads:
        t.join()
    
    print(f"Final counter value: {counter}")
    
    # Example with a thread-safe queue
    print("\nThread-safe Queue Example:")
    
    # Create a thread-safe queue
    q = queue.Queue()
    
    # Producer thread
    def producer():
        for i in range(5):
            item = f"item-{i}"
            q.put(item)
            print(f"Produced {item}")
            time.sleep(random.random() * 0.1)
    
    # Consumer thread
    def consumer():
        while True:
            item = q.get()
            if item is None:
                break
            print(f"Consumed {item}")
            time.sleep(random.random() * 0.2)
            q.task_done()
    
    # Start producer and consumer threads
    producer_thread = threading.Thread(target=producer)
    consumer_thread = threading.Thread(target=consumer)
    
    producer_thread.start()
    consumer_thread.start()
    
    # Wait for producer to finish
    producer_thread.join()
    
    # Signal consumer to exit
    q.put(None)
    
    # Wait for consumer to finish
    consumer_thread.join()
    
    print()


"""
3. Multiprocessing
"""
def multiprocessing_example():
    print("3. Multiprocessing Example")
    
    """
    Python's Global Interpreter Lock (GIL) prevents multiple threads from executing Python bytecode simultaneously.
    This means that threads cannot fully utilize multiple CPU cores for CPU-bound tasks.
    Multiprocessing bypasses the GIL by using separate processes instead of threads.
    """
    
    # Define a CPU-intensive function
    def cpu_bound(number):
        return sum(i * i for i in range(number))
    
    # Sequential execution
    start_time = time.time()
    results = [cpu_bound(n) for n in [5000000, 5000000, 5000000, 5000000]]
    end_time = time.time()
    print(f"Sequential execution time: {end_time - start_time:.2f} seconds")
    
    # Parallel execution with multiprocessing
    start_time = time.time()
    
    # Create a pool of worker processes
    with multiprocessing.Pool(processes=4) as pool:
        # Map the function to the arguments in parallel
        results = pool.map(cpu_bound, [5000000, 5000000, 5000000, 5000000])
    
    end_time = time.time()
    print(f"Parallel execution time: {end_time - start_time:.2f} seconds")
    
    # Sharing data between processes
    print("\nSharing data between processes:")
    
    def update_shared_value(shared_value, lock):
        with lock:
            shared_value.value += 1
            print(f"Process {multiprocessing.current_process().name}: shared_value = {shared_value.value}")
    
    # Create shared value and lock
    shared_value = multiprocessing.Value('i', 0)  # 'i' for integer
    lock = multiprocessing.Lock()
    
    # Create and start processes
    processes = []
    for _ in range(3):
        p = multiprocessing.Process(target=update_shared_value, args=(shared_value, lock))
        processes.append(p)
        p.start()
    
    # Wait for all processes to complete
    for p in processes:
        p.join()
    
    print(f"Final shared value: {shared_value.value}")
    print()


"""
4. Asyncio (Asynchronous I/O)
"""
def asyncio_example():
    print("4. Asyncio Example")
    
    """
    Asyncio is a library for writing concurrent code using the async/await syntax.
    It's particularly well-suited for I/O-bound tasks like network operations.
    Asyncio uses a single-threaded event loop and cooperative multitasking.
    """
    
    async def fetch_data(name, delay):
        print(f"Coroutine {name}: starting")
        await asyncio.sleep(delay)  # Non-blocking sleep
        print(f"Coroutine {name}: finished after {delay}s")
        return f"Result from {name}"
    
    async def main_coroutine():
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
    
    # Run the asyncio event loop
    asyncio.run(main_coroutine())
    print()


"""
5. Concurrent.Futures
"""
def concurrent_futures_example():
    print("5. Concurrent.Futures Example")
    
    """
    The concurrent.futures module provides a high-level interface for asynchronously
    executing callables using threads or processes.
    It simplifies the use of threads and processes with features like:
    - Thread and process pools
    - Futures (a class representing the result of asynchronous execution)
    - Callbacks
    """
    
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


if __name__ == "__main__":
    main()