"""
This file demonstrates Thread Synchronization in Python.

Thread synchronization is essential when multiple threads access shared resources
to prevent race conditions and ensure data consistency.
"""

import threading
import time
import queue
import random

def main():
    print("Python Thread Synchronization Example")
    print("====================================")
    
    # Run examples
    thread_synchronization_example()
    advanced_synchronization_example()  # Latest Python synchronization features

"""
Thread Synchronization Basics

Thread synchronization is necessary when multiple threads access shared resources.
Python provides several mechanisms for synchronization, including locks, semaphores,
events, and thread-safe queues.
"""
def thread_synchronization_example():
    print("1. Thread Synchronization Example")
    
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
Advanced Synchronization Features (Latest Python Features)

Python has enhanced its synchronization capabilities in recent versions:
- RLock (Reentrant Lock) improvements
- Condition variables with timeout
- Barrier synchronization
- Semaphore enhancements
- Thread-safe data structures
"""
def advanced_synchronization_example():
    print("2. Advanced Synchronization Features (Latest)")
    
    # RLock (Reentrant Lock)
    print("Using RLock (Reentrant Lock):")
    
    # Create a reentrant lock
    rlock = threading.RLock()
    
    def nested_locks():
        print("Acquiring lock first time")
        with rlock:
            print("Lock acquired first time")
            # With a regular Lock, this would deadlock
            print("Acquiring lock second time")
            with rlock:
                print("Lock acquired second time")
                # Do some work
                time.sleep(0.1)
            print("Released inner lock")
        print("Released outer lock")
    
    # Create and start a thread that uses nested locks
    t = threading.Thread(target=nested_locks)
    t.start()
    t.join()
    
    # Condition variable with timeout
    print("\nCondition variable with timeout:")
    
    # Create a condition variable
    condition = threading.Condition()
    
    # Shared data
    data_ready = False
    
    def data_provider():
        global data_ready
        
        with condition:
            print("Provider: preparing data...")
            time.sleep(2)  # Simulate data preparation
            data_ready = True
            print("Provider: data is ready, notifying")
            condition.notify_all()
    
    def data_consumer(name, timeout):
        global data_ready
        
        with condition:
            if not data_ready:
                print(f"Consumer {name}: waiting for data with timeout {timeout}s")
                # Wait with timeout
                result = condition.wait(timeout=timeout)
                if not result:  # If wait timed out
                    print(f"Consumer {name}: timeout occurred, data not available")
                    return
            
            print(f"Consumer {name}: data received, processing")
    
    # Create provider and consumer threads
    provider = threading.Thread(target=data_provider)
    consumer1 = threading.Thread(target=data_consumer, args=("A", 3))  # Enough time
    consumer2 = threading.Thread(target=data_consumer, args=("B", 1))  # Not enough time
    
    # Start threads
    consumer1.start()
    consumer2.start()
    provider.start()
    
    # Wait for all threads to complete
    provider.join()
    consumer1.join()
    consumer2.join()
    
    # Barrier synchronization
    print("\nBarrier synchronization:")
    
    # Create a barrier for 3 threads
    barrier = threading.Barrier(3)
    
    def worker(name, barrier):
        print(f"Worker {name} starting")
        time.sleep(random.random())  # Simulate different work durations
        print(f"Worker {name} reached the barrier")
        
        # Wait for all threads to reach the barrier
        barrier.wait()
        
        print(f"Worker {name} continuing after the barrier")
    
    # Create and start worker threads
    workers = []
    for i in range(3):
        t = threading.Thread(target=worker, args=(i, barrier))
        workers.append(t)
        t.start()
    
    # Wait for all workers to complete
    for t in workers:
        t.join()
    
    # Semaphore for limiting concurrent access
    print("\nSemaphore for limiting concurrent access:")
    
    # Create a semaphore that allows 2 concurrent accesses
    semaphore = threading.Semaphore(2)
    
    def limited_resource(name):
        print(f"Thread {name} waiting to access the resource")
        
        with semaphore:
            print(f"Thread {name} acquired the resource")
            time.sleep(1)  # Simulate using the resource
            print(f"Thread {name} releasing the resource")
    
    # Create and start threads that access the limited resource
    threads = []
    for i in range(5):
        t = threading.Thread(target=limited_resource, args=(i,))
        threads.append(t)
        t.start()
    
    # Wait for all threads to complete
    for t in threads:
        t.join()
    
    # BoundedSemaphore (ensures release() is not called too many times)
    print("\nBoundedSemaphore:")
    
    # Create a bounded semaphore
    bounded_semaphore = threading.BoundedSemaphore(value=2)
    
    def use_bounded_semaphore(name):
        print(f"Thread {name} acquiring semaphore")
        bounded_semaphore.acquire()
        
        try:
            print(f"Thread {name} working with semaphore")
            time.sleep(0.5)
        finally:
            print(f"Thread {name} releasing semaphore")
            bounded_semaphore.release()
            # Uncommenting the line below would raise a ValueError
            # bounded_semaphore.release()  # This would exceed the initial value
    
    # Create and start threads
    threads = []
    for i in range(3):
        t = threading.Thread(target=use_bounded_semaphore, args=(i,))
        threads.append(t)
        t.start()
    
    # Wait for all threads to complete
    for t in threads:
        t.join()
    
    print()

if __name__ == "__main__":
    main()