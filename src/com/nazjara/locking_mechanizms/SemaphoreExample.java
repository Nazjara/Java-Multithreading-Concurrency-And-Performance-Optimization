package com.nazjara.locking_mechanizms;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreExample {

    public static void main(String[] args) {
        int numberOfThreads = 10;

        List<Thread> threads = new ArrayList<>();

        Barrier barrier = new Barrier(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new CoordinatedWorkRunner(barrier)));
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }

    public static class Barrier {
        private final int numberOfWorkers;
        // Initializing semaphore with 0 will block all threads on acquire() before release() is called
        private Semaphore semaphore = new Semaphore(0);
        private int counter = 0;
        private Lock lock = new ReentrantLock();

        public Barrier(int numberOfWorkers) {
            this.numberOfWorkers = numberOfWorkers;
        }

        public void barrier() {
            lock.lock();
            boolean isLastWorker = false;
            try {
                counter++;

                if (counter == numberOfWorkers) {
                    isLastWorker = true;
                }
            } finally {
                lock.unlock();
            }

            if (isLastWorker) {
                // Release all blocked threads (current thread is not blocked)
                semaphore.release(9);
            } else {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public static class CoordinatedWorkRunner implements Runnable {
        private Barrier barrier;

        public CoordinatedWorkRunner(Barrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            // Performing Part 1
            System.out.println(Thread.currentThread().getName() + " part 1 of the work is finished");

            // All threads must perform part 1 before part 2 starts
            barrier.barrier();

            // Performing Part2
            System.out.println(Thread.currentThread().getName() + " part 2 of the work is finished");
        }
    }
}
