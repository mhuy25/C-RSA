/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package De5;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Acer
 */
public class Client {

    Socket socket = null;
    BufferedWriter out = null;
    BufferedReader in = null;
    BufferedReader stdIn = null;
    String rs="";
    String info="";
    String result="";

    public void Init_Connect(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void Close_Connect(BufferedReader in, BufferedWriter out, BufferedReader stdIn) {
        try {
            System.out.println("Closing connection");
            in.close();
            out.close();
            socket.close();
            stdIn.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void Sent(BufferedWriter out) {
        try {
            out.newLine();
            out.flush();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public String check(BufferedReader in) throws IOException {
        String dataServer = "";
        dataServer = in.readLine();
        return dataServer;
    }

     public String getInfo(BufferedReader in, BufferedWriter out) {
        String dataServer = "";
         try {             
            dataServer = in.readLine();   
        } catch (IOException e) {
            System.err.println(e);
        }
         return dataServer;
    }

    public String getStatistic(BufferedReader in, BufferedWriter out) {
        String dataServer = "";
        try {           
            dataServer = in.readLine();            
        } catch (IOException e) {
            System.err.println(e);
        }        
         return dataServer;
    }
    
    public String getCount(BufferedReader in, BufferedWriter out) {
         String dataServer = "";
        try {       
            dataServer = in.readLine();               
        } catch (IOException e) {
            System.err.println(e);
        }   
        return dataServer;
    }
    public ArrayList<String> getResult(BufferedReader in, BufferedWriter out){
         ArrayList<String> result = new ArrayList<String>();
        try { String dataServer = "";
            dataServer = in.readLine();
            StringTokenizer str = new StringTokenizer(dataServer, ";");
            while (str.hasMoreTokens()) {
                result.add(str.nextToken());
            }    
        } catch (IOException e) {
            System.err.println(e);
        }  
        return result;
    }

    public ArrayList<String> getStudent(BufferedReader in, BufferedWriter out, BufferedReader stdIn) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            String dataServer = "";
            dataServer = in.readLine();
            StringTokenizer str = new StringTokenizer(dataServer, ";");
            while (str.hasMoreTokens()) {
                result.add(str.nextToken());
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return result;
    }
    
    public String getPosition(BufferedReader in) throws IOException{
        String dataServer = "";
        dataServer = in.readLine();
        return dataServer;
    }


    public Client(String address, int port) throws UnknownHostException, IOException {
        Init_Connect(address, port);
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        stdIn = new BufferedReader(new InputStreamReader(System.in));
    }
}
