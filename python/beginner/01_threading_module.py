"""
This file demonstrates the Threading Module in Python.

The threading module in Python is used to create and manage threads.
It provides a higher-level interface for working with threads compared to the lower-level _thread module.
"""

import threading
import time
import queue
import random
import concurrent.futures

def main():
    print("Python Threading Module Example")
    print("===============================")
    
    # Run examples
    threading_module_example()
    advanced_threading_example()  # Latest Python threading features

"""
Threading Module Basics

The threading module provides a way to create and manage threads in Python.
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
Advanced Threading Features (Latest Python Features)

Python has added several improvements to its threading capabilities in recent versions:
- Thread-local storage improvements
- Better exception handling
- Enhanced daemon thread behavior
- Improved thread lifecycle management
"""
def advanced_threading_example():
    print("2. Advanced Threading Features (Latest)")
    
    # Thread-local storage
    print("Thread-local storage:")
    
    # Create thread-local storage
    thread_local = threading.local()
    
    def thread_with_local_data(name, value):
        # Each thread has its own 'data' attribute
        thread_local.data = value
        print(f"Thread {name} stored: {thread_local.data}")
        time.sleep(random.random())
        print(f"Thread {name} retrieved: {thread_local.data}")
    
    # Create multiple threads that use thread-local storage
    threads = []
    for i in range(3):
        t = threading.Thread(target=thread_with_local_data, args=(i, f"Data-{i}"))
        threads.append(t)
        t.start()
    
    # Wait for all threads to complete
    for t in threads:
        t.join()
    
    # Exception handling in threads
    print("\nException handling in threads:")
    
    def thread_with_exception():
        try:
            print("Thread started")
            # Simulate an error
            raise ValueError("Simulated error in thread")
        except Exception as e:
            print(f"Caught exception in thread: {e}")
    
    # Create and start a thread that will raise an exception
    t = threading.Thread(target=thread_with_exception)
    t.start()
    t.join()
    
    # Using ThreadPoolExecutor from concurrent.futures (Python 3.2+)
    print("\nUsing ThreadPoolExecutor:")
    
    def worker_with_result(value):
        print(f"Processing {value}")
        time.sleep(random.random())
        return value * value
    
    # Create a ThreadPoolExecutor
    with concurrent.futures.ThreadPoolExecutor(max_workers=3) as executor:
        # Submit tasks and get Future objects
        futures = [executor.submit(worker_with_result, i) for i in range(5)]
        
        # Process results as they complete
        for future in concurrent.futures.as_completed(futures):
            try:
                result = future.result()
                print(f"Task result: {result}")
            except Exception as e:
                print(f"Task generated an exception: {e}")
    
    # Daemon threads
    print("\nDaemon threads:")
    
    def daemon_worker():
        print("Daemon thread starting")
        while True:
            print("Daemon working...")
            time.sleep(0.5)
    
    # Create a daemon thread
    daemon_thread = threading.Thread(target=daemon_worker, daemon=True)
    daemon_thread.start()
    
    # Main thread continues and will exit, causing daemon to terminate
    print("Main thread sleeping for 1.5 seconds")
    time.sleep(1.5)
    print("Main thread exiting, daemon will be terminated")
    
    # Event for thread synchronization (Python 3.8+ has improved event handling)
    print("\nUsing Event for thread synchronization:")
    
    # Create an event
    event = threading.Event()
    
    def waiter(name):
        print(f"Waiter {name} waiting for event")
        event.wait()  # Wait until the event is set
        print(f"Waiter {name} received event, continuing")
    
    # Create threads that wait for the event
    waiters = []
    for i in range(3):
        t = threading.Thread(target=waiter, args=(i,))
        t.start()
        waiters.append(t)
    
    # Sleep a bit then set the event
    time.sleep(1)
    print("Setting event")
    event.set()  # All waiting threads will now proceed
    
    # Wait for all threads to complete
    for t in waiters:
        t.join()
    
    # Timer threads (convenient way to run a function after a delay)
    print("\nUsing Timer threads:")
    
    def delayed_task():
        print("Delayed task executed")
    
    # Create a timer that will execute after 1 second
    timer = threading.Timer(1.0, delayed_task)
    timer.start()
    
    print("Timer started, waiting...")
    timer.join()
    
    print()

if __name__ == "__main__":
    main()