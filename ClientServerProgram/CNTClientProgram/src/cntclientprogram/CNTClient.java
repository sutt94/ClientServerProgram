/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cntclientprogram;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Billy
 */
public class CNTClient {

    private static String host;
    private static int numberOfThreads;
    private static ClientSideThread[] clientThreads;

    private static void createThreads(int numberOfThreads, String host, int port, String command) {
        clientThreads = new ClientSideThread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            ;
            clientThreads[i] = new ClientSideThread(host, port, command);

        }
    }

    private static void startThreads() {
        int i;
        boolean running = true;
        for (i = 0; i < clientThreads.length; i++) {

            clientThreads[i].start();
        }
        while (running) {
            running = false;
            for (i = 0; i < clientThreads.length; i++) {
                if (clientThreads[i].isAlive()) {
                    running = true;

                }
            }
        }
    }

    private static void getResponseTimes(int numberOfThreads) {
        double sumTotalTimes = 0;
        System.out.println("");
        System.out.printf("Server reponse time(s) in miliseconds below. %n");
        for (ClientSideThread thread : clientThreads) {
            System.out.printf("%.2f, ", thread.getTotalTime());
            sumTotalTimes += thread.getTotalTime();
        }
        System.out.printf("%nAverage Server response time: %.2f ms", (sumTotalTimes / ((double) numberOfThreads)));
        System.out.println("");
        System.out.println("");
    }

    private static void serverExit() {

        try {

            Socket socket = new Socket(host, 5000);
            PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);
            serverOutput.printf("exit");
            socket.close();
        } catch (IOException i) {
        }
    }

    public static void main(String[] args) {
        UI ui;
        String command;
        boolean running = true;

        if (args.length < 1) { //exit if no host provided
            System.out.printf("No host ip provided, exiting program...");
            return;
        } else {
            host = args[0];
            if (args.length == 2) { //if two parameters passed set number of threads
                numberOfThreads = Integer.parseInt(args[1]);
            } else {
                System.out.printf("Number of threads not defined, using default of 1 thread.%n");
                numberOfThreads = 1;
            }
        }
        if (numberOfThreads == 0) {
            numberOfThreads = 1;
        }

        ui = new UI(numberOfThreads);
        while (running = true) {
            ui.displayUserMenu();
            command = ui.getServerCommandFromUserCommand();

            if (command.equals("thread")) {
                numberOfThreads = ui.updateNumberOfThreads();
            } else if (command.equals("exit")) {
                serverExit();
                System.out.printf("The program is exitting...%n");
                running = false;
                break;
            } else {
                System.out.printf("The command to be run is on the host is%s%n", command);
                System.out.println("Output from the host: ");

                createThreads(numberOfThreads, host, 5000, command);
                startThreads();

                getResponseTimes(numberOfThreads);

            }
        }

        return;
    }

}
