package com.example.demo;

public class Main {
    public static void main(String[] args) {
        // Instantiate your Fib class (assuming a range of 10)
        Fib fibonacci = new Fib(10);
        
        // Print the result
        System.out.println("Fibonacci Series: " + fibonacci.getFibSeries());
    }
}