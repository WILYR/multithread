package com.java.multithread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class AThread1 implements Runnable {

    @Override
    public void run() {
        System.out.print("first");
    }
}

class BThread2 implements Runnable {

    @Override
    public void run() {
        System.out.print("second");
    }
}

class CThread3 implements Runnable {

    @Override
    public void run() {
        System.out.print("third");
    }
}

class Foo1 {

    private int number = 1;
    private Lock locker = new ReentrantLock();
    private Condition condition = locker.newCondition();

    public void first(Runnable thread) throws InterruptedException {
        locker.lock();
        try {
            thread.run();
            number++;
            condition.signalAll();
        } finally {
            locker.unlock();
        }

    }

    public void second(Runnable thread) throws InterruptedException {
        locker.lock();
        try {
            while (number != 2) {
                condition.await();
            }
            thread.run();
            number++;
            condition.signalAll();
        } finally {
            locker.unlock();
        }
    }

    synchronized public void third(Runnable thread) throws InterruptedException {
        locker.lock();
        try {
            while (number != 3) {
                condition.await();
            }
            thread.run();
            number++;
            condition.signalAll();
        } finally {
            locker.unlock();
        }
    }
}

public class task1Lock {
    public static void main(String[] args) throws InterruptedException {
        Foo1 foo = new Foo1();
        Runnable threadA = new AThread1();
        Runnable threadB = new BThread2();
        Runnable threadC = new CThread3();
        CompletableFuture.runAsync(() -> {
            try {
                foo.third(threadC);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        CompletableFuture.runAsync(() -> {
            try {
                foo.second(threadB);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        CompletableFuture.runAsync(() -> {
            try {
                foo.first(threadA);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread.sleep(1000);
    }
}
