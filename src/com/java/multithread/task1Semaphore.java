package com.java.multithread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

class AThread implements Runnable {

    @Override
    public void run() {
        System.out.print("first");
    }
}

class BThread implements Runnable {

    @Override
    public void run() {
        System.out.print("second");
    }
}

class CThread implements Runnable {

    @Override
    public void run() {
        System.out.print("third");
    }
}

class Foo {
    Semaphore semaphore = new Semaphore(0);
    Semaphore semaphore2 = new Semaphore(0);

    public void first(Runnable thread) throws InterruptedException {
        thread.run();
        semaphore.release();
    }

    public void second(Runnable thread) throws InterruptedException {
        semaphore.acquire();
        thread.run();
        semaphore2.release();
    }

    synchronized public void third(Runnable thread) throws InterruptedException {
        semaphore2.acquire();
        thread.run();
    }
}

public class task1Semaphore {
    public static void main(String[] args) throws InterruptedException {
        Foo foo = new Foo();
        Runnable threadA = new AThread();
        Runnable threadB = new BThread();
        Runnable threadC = new CThread();
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
