/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package De5;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author AMIT GROUP
 */
public class Server {
    public static void main(String[] args) {
       try {
            ServerSocket server = new ServerSocket(5000);
            System.out.println("Server started");
            while(true){
                new ThreadSocket(server.accept()).start();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
