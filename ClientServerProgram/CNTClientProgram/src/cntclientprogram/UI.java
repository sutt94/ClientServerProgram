/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cntclientprogram;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Billy
 */
public class UI {

    private String[] commands = {"thread", "uptime", "date", "netstat", "free",
        "ps aux", "users", "exit"};

    private int numberOfThreads;

    public UI() {
        this.numberOfThreads = 0;
    }

    public UI(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    //gets the numeric command entered by the user
    public int getNumericCommand() {
        int numericCommand;
        Scanner userInput = new Scanner(System.in);

        try {
            numericCommand = userInput.nextInt();
            if (numericCommand < 0 || numericCommand > 7) {
                System.out.printf("Incorrect input, enter a number 0-7.%n");
                numericCommand = getNumericCommand();
            }
        } catch (InputMismatchException i) {
            System.out.printf("Incorrect input, please enter a numeric command of 0-7.%n");
            numericCommand = getNumericCommand();
        }

        return numericCommand;
    }

    public String getServerCommandFromUserCommand() {
        int numericCommand = this.getNumericCommand();

        return this.commands[numericCommand];
    }

    public int updateNumberOfThreads() {
        int newNumberOfThreads = 1;

        System.out.printf("Enter the new number of threads: ");
        Scanner userInput = new Scanner(System.in);
        try {
            newNumberOfThreads = userInput.nextInt();
        } catch (InputMismatchException i) {
            System.out.printf("Incorrect input... please enter a numeric representation of the  number of threads to use.%n");

        }

        this.numberOfThreads = newNumberOfThreads;
        return newNumberOfThreads;
    }

    public void displayUserMenu() {
        System.out.printf("Please enter a command to be run on the server.%n");
        System.out.printf("Threads to be used is %d%n", this.numberOfThreads);
        System.out.printf("0. Change the number of threads.%n");
        System.out.printf("1. Get the uptime of the host.%n");
        System.out.printf("2. Get the date and time information.%n");
        System.out.printf("3. Get  the host netstat.%n");
        System.out.printf("4. Check the host memory use.%n");
        System.out.printf("5. Check the processes currently running on the host.%n");
        System.out.printf("6. Check who is currently using the host.%n");
        System.out.printf("7. Quit%n");
        System.out.printf("%n");
        System.out.printf("Please enter a numeric command: ");
        return;
    }
}
