"""
This file demonstrates the Multiprocessing module in Python.

Python's Global Interpreter Lock (GIL) prevents multiple threads from executing Python bytecode simultaneously.
This means that threads cannot fully utilize multiple CPU cores for CPU-bound tasks.
Multiprocessing bypasses the GIL by using separate processes instead of threads.
"""

import multiprocessing
import time
import os
import random
import numpy as np
from concurrent.futures import ProcessPoolExecutor

def main():
    print("Python Multiprocessing Example")
    print("==============================")
    
    # Run examples
    multiprocessing_example()
    advanced_multiprocessing_example()  # Latest Python multiprocessing features

"""
Multiprocessing Basics

The multiprocessing module allows you to spawn processes in much the same way as you can spawn threads.
It offers both local and remote concurrency, effectively side-stepping the Global Interpreter Lock.
"""
def multiprocessing_example():
    print("1. Multiprocessing Example")
    
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
Advanced Multiprocessing Features (Latest Python Features)

Python has enhanced its multiprocessing capabilities in recent versions:
- Shared memory with multiprocessing.shared_memory (Python 3.8+)
- Process Pools with context managers
- Improved inter-process communication
- Better resource management
- ProcessPoolExecutor enhancements
"""
def advanced_multiprocessing_example():
    print("2. Advanced Multiprocessing Features (Latest)")
    
    # Shared memory (Python 3.8+)
    print("Using shared memory (Python 3.8+):")
    
    try:
        # Check if shared_memory is available (Python 3.8+)
        from multiprocessing import shared_memory
        
        # Create a shared memory block
        shm = shared_memory.SharedMemory(create=True, size=100)
        
        # Write data to shared memory
        buffer = shm.buf
        buffer[0:5] = bytearray([1, 2, 3, 4, 5])
        
        def read_from_shared_memory(shm_name):
            # Attach to the existing shared memory block
            existing_shm = shared_memory.SharedMemory(name=shm_name)
            # Read data from shared memory
            data = list(existing_shm.buf[0:5])
            print(f"Process {os.getpid()} read from shared memory: {data}")
            # Close the shared memory block
            existing_shm.close()
        
        # Create a process that reads from shared memory
        p = multiprocessing.Process(target=read_from_shared_memory, args=(shm.name,))
        p.start()
        p.join()
        
        # Clean up
        shm.close()
        shm.unlink()  # Free the shared memory block
        
    except ImportError:
        print("shared_memory module not available (requires Python 3.8+)")
    
    # Process Pool with context manager
    print("\nProcess Pool with context manager:")
    
    def process_data(data):
        # Simulate CPU-intensive processing
        result = sum(x * x for x in range(data * 100000))
        return f"Process {os.getpid()} processed data {data}, result: {result}"
    
    # Using context manager for proper resource cleanup
    with multiprocessing.Pool(processes=3) as pool:
        results = pool.map(process_data, [1, 2, 3, 4, 5])
        for result in results:
            print(result)
    
    # Improved inter-process communication with Pipes
    print("\nImproved inter-process communication with Pipes:")
    
    def sender(conn):
        print(f"Sender process {os.getpid()} starting")
        for i in range(5):
            data = {"index": i, "value": random.random()}
            print(f"Sending: {data}")
            conn.send(data)
            time.sleep(0.1)
        conn.close()
    
    def receiver(conn):
        print(f"Receiver process {os.getpid()} starting")
        while True:
            try:
                data = conn.recv()
                print(f"Received: {data}")
            except EOFError:
                print("Connection closed by sender")
                break
    
    # Create a pipe
    parent_conn, child_conn = multiprocessing.Pipe()
    
    # Create sender and receiver processes
    sender_process = multiprocessing.Process(target=sender, args=(child_conn,))
    receiver_process = multiprocessing.Process(target=receiver, args=(parent_conn,))
    
    # Start processes
    sender_process.start()
    receiver_process.start()
    
    # Close unused connection ends in the parent process
    child_conn.close()
    parent_conn.close()
    
    # Wait for processes to complete
    sender_process.join()
    receiver_process.join()
    
    # ProcessPoolExecutor with improved features (Python 3.7+)
    print("\nProcessPoolExecutor with improved features:")
    
    def compute_intensive_task(params):
        x, y = params
        # Simulate a compute-intensive task
        result = 0
        for i in range(1000000):
            result += (x * i) % y
        return result
    
    # Generate parameters for tasks
    params = [(random.randint(1, 100), random.randint(1, 100)) for _ in range(10)]
    
    # Using ProcessPoolExecutor with max_workers and context manager
    start_time = time.time()
    
    with ProcessPoolExecutor(max_workers=4) as executor:
        # Submit all tasks and get futures
        futures = [executor.submit(compute_intensive_task, param) for param in params]
        
        # Process results as they complete
        for i, future in enumerate(futures):
            result = future.result()
            print(f"Task {i} result: {result}")
    
    end_time = time.time()
    print(f"ProcessPoolExecutor execution time: {end_time - start_time:.2f} seconds")
    
    # Using numpy with multiprocessing for efficient numerical computations
    print("\nUsing numpy with multiprocessing:")
    
    def process_array(array_chunk):
        # Perform some computation on the array chunk
        return np.sum(np.square(array_chunk))
    
    # Create a large numpy array
    large_array = np.random.rand(10000000)
    
    # Split the array into chunks
    chunks = np.array_split(large_array, 4)
    
    # Process chunks in parallel
    with multiprocessing.Pool(processes=4) as pool:
        results = pool.map(process_array, chunks)
    
    # Combine results
    total_result = sum(results)
    print(f"Parallel numpy computation result: {total_result}")
    
    # Compare with sequential computation
    sequential_result = np.sum(np.square(large_array))
    print(f"Sequential numpy computation result: {sequential_result}")
    print(f"Results match: {abs(total_result - sequential_result) < 1e-10}")
    
    print()

if __name__ == "__main__":
    main()