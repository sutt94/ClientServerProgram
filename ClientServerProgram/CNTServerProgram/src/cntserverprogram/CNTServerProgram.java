/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cntserverprogram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CNTServerProgram {

    public static void main(String[] args) {
        Socket cSocket = null;
        ServerSocket sSocket = null;
        String command;

        //Server socket
        try {
            sSocket = new ServerSocket(5000);
            System.out.println("The Server Socket has been created");
            System.out.println("Waiting for client...");
            System.out.println("");
        } catch (IOException i) {
            System.out.println(i);
        }

        try {
            boolean running = true;
            while (true) {

                //open input and output streams, accept socket
                cSocket = sSocket.accept();
                System.out.printf("The client has been accepted...%n");
                System.out.printf("Running command from client thread...%n");
                System.out.println("");

                BufferedReader inputStream = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
                PrintWriter outputStream = new PrintWriter(cSocket.getOutputStream(), true);

                while (((command = inputStream.readLine()) != null) && !cSocket.isClosed()) {
                    System.out.println(command);
                    System.out.println("");

                    //if the command is exit, close and quit  
                    if (command.equals("exit")) {
                        System.out.printf("The program is exiting...");

                        outputStream.close();
                        inputStream.close();
                        
                        return;

                    } //if the command is not exit execute the command, send output to client
                    else {
                        try {
                            Runtime runtime = Runtime.getRuntime();
                            Process process = runtime.exec(command);

                            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

                            String line;
                            while ((line = input.readLine()) != null) {
                                System.out.println(line);
                                outputStream.println(line);
                            }
                            //signals the output from the command is complete
                            outputStream.println("done");

                            //report completion of commands, waiting for a new command
                            System.out.println("The command was run succesfully...");
                            System.out.println("");
                            System.out.printf("Awaiting command from client...");
                            System.out.println("");
                            process.waitFor();

                        } catch (Exception i) {

                            System.out.println(i.toString());
                        }
                    }
                }
            }
        } catch (IOException i) {
            System.out.println(i);
        }
    }

}
