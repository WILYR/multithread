package com.java.multithread;

import java.util.concurrent.CompletableFuture;

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
    public int number = 1;
    synchronized public void first(Runnable thread) throws InterruptedException {
        while (number != 1) {
            wait();
        }
        thread.run();
        number++;
        notifyAll();
    }

    synchronized public void second(Runnable thread) throws InterruptedException {
        while (number != 2) {
            wait();
        }
        thread.run();
        number++;
        notifyAll();
    }

    synchronized public void third(Runnable thread) throws InterruptedException {
        while (number != 3) {
            wait();
        }
        thread.run();
        number++;
        notifyAll();
    }
}

public class task1 {
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
