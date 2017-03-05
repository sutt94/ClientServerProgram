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
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Billy
 */
public class ClientSideThread extends Thread {

    private String serverCommand;
    private String host;
    private long startingTime;
    private long timeElapsed;
    private long totalTime;
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
        this.startingTime = System.nanoTime();
    }

    private void stopTimer() {
        this.timeElapsed = System.nanoTime() - this.startingTime;
    }
    

    private void updateTotalTime() {
        this.totalTime += this.timeElapsed;
        
    }
//method executed by the thread
    public void run() {

        while (true) {
            try {

                String string = null;
                Socket socket = new Socket(this.host, this.port);
                PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                
                
                
                //begin timer and send command to server
                beginTimer();
                outputStream.println(this.serverCommand);
                
                //print output from server until done message received, do timer stuff
                //don't take into account the time the client is simply printing the line it recieved from the server
                while (true) {
                    //get line of input from the server
                    string = inputStream.readLine();
                    if (!string.equals("done")) {
                        //update the elapsed time
                        stopTimer();
                        //add the elapsed time to the total time
                        updateTotalTime();
                        //reset elapsed time for next calculation
                        timeElapsed = 0;
                        //print the line of output from the server
                        System.out.println(string);
                        //start the timer before reading next line from the server
                        beginTimer();
                    } else {
                        break;
                    }
                }
                //get elapsed time from the last line received from server
                stopTimer();
                //update total time
                updateTotalTime();
                //convert total time from nano to ms
                totalTime = TimeUnit.NANOSECONDS.toMillis(totalTime);
                //close streams
                inputStream.close();
                outputStream.close();
                
                System.out.printf("%n");
                System.out.printf("%n");
                //close socket
                socket.close();
                //exit loop
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
