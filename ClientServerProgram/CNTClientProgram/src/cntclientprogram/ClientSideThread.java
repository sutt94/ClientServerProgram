/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cntclientprogram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Billy
 */
public class ClientSideThread extends Thread {

    private String serverCommand;
    private String host;
    private long startingTime;
    private double timeElapsed;
    private double totalTime;
    private int port;

    public ClientSideThread(String host, int port, String serverCommand) {
        this.host = host;
        this.port = port;
        this.serverCommand = serverCommand;
        this.timeElapsed = 0;
        this.totalTime = 0;

    }

    public String getServerCommand() {
        return this.serverCommand;
    }

    public double getElaspedTime() {
        return this.timeElapsed;
    }

    public double getTotalTime() {
        return this.totalTime;
    }

    private void beginTimer() {
        this.startingTime = System.currentTimeMillis();
    }

    private void stopTimer() {
        long currentTime = System.currentTimeMillis();
        this.timeElapsed = currentTime - this.startingTime;
    }

    private void updateTotalTime() {
        this.totalTime += this.timeElapsed;
    }

    public void run() {

        while (true) {
            try {

                String string = null;
                Socket socket = new Socket(this.host, this.port);
                PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                beginTimer();
                totalTime = 0;

                outputStream.println(this.serverCommand);

                //print output from server until done message recieved, do timer stuff
                while (true) {
                    string = inputStream.readLine();
                    if (!string.equals("done")) {
                        stopTimer();
                        updateTotalTime();
                        System.out.println(string);
                        beginTimer();
                    } else {
                        break;
                    }
                }
                stopTimer();
                inputStream.close();
                outputStream.close();

                updateTotalTime();
                System.out.printf("%n");
                System.out.printf("%n");
                socket.close();

                break;
            } catch (UnknownHostException i) {
                System.out.printf("Error, the host is unknown...t%n");
                i.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            } catch (Exception i) {
                System.out.printf("There was an error, an exception has been thrown%n");
            }
        }

    }

}
