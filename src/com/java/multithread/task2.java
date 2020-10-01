package com.java.multithread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

class F extends Thread {
    public void run() {
        System.out.print(" fizz ");
    }
}

class B extends Thread {
    public void run() {
        System.out.print(" buzz ");
    }
}

class Fb extends Thread {
    public void run() {
        System.out.print(" fizzbuzz ");
    }
}

class Num extends Thread {

    public void run(int num) {
        System.out.print(" "+ num + " ");
    }
}

class FizzBuzz {

    Semaphore s1 = new Semaphore(0);
    Semaphore s2 = new Semaphore(0);
    Semaphore s3 = new Semaphore(0);


    public void fizz(int num, Runnable r) throws InterruptedException {
        s1.acquire();
        if (num % 3 == 0 && num % 5 != 0) {
            r.run();
        }
        s2.release();
    }

    public void buzz(int num, Runnable r) throws InterruptedException {
        s2.acquire();
        if (num % 5 == 0 && num % 3 != 0) {
            r.run();
        }
        s3.release();
    }

    public void fizzbuzz(int num, Runnable r) {
        if (num % 3 == 0 && num % 5 == 0) {
            r.run();
        }
        s1.release();
    }

    public void number(int num, Num r) throws InterruptedException {
        s3.acquire();
        if (num % 3 != 0 && num % 5 != 0) {
            r.run(num);
        }
    }
}


public class task2 {
    public static void main(String[] args) throws InterruptedException {
        FizzBuzz fizzBuzz = new FizzBuzz();
        int n = 15;
        Runnable fizz = new F();
        Runnable buzz = new B();
        Runnable fizzbuzz = new Fb();
        for (int i = 1; i <= n; i++) {
            int finalI = i;
            final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                try {
                    fizzBuzz.fizz(finalI, fizz);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            final CompletableFuture<Void> voidCompletableFuture1 = CompletableFuture.runAsync(() -> {
                try {
                    fizzBuzz.buzz(finalI, buzz);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            final CompletableFuture<Void> voidCompletableFuture2 = CompletableFuture.runAsync(() -> {
                fizzBuzz.fizzbuzz(finalI, fizzbuzz);
            });
            final CompletableFuture<Void> voidCompletableFuture3 = CompletableFuture.runAsync(() -> {
                try {
                    fizzBuzz.number(finalI, new Num());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            Thread.sleep(1000);
        }
    }
}
