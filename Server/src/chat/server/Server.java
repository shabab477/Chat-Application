package chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Shabab
 */

public class Server {
    
    
    public static ServerThread[] array = new ServerThread[10];
    public static int size = 0;
    public Server() throws Exception
    {
        String s = InetAddress.getLocalHost().toString();
        String ip[] = s.split("/");
        JOptionPane.showMessageDialog(new JFrame(), ip[1]);
        ServerSocket servSock;
        try {
            servSock = new ServerSocket(33333, 10);
            
            while(true)
            {
                Socket sock = servSock.accept();
                array[size++] = new ServerThread(sock);
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
          
        
    }
    
    public static void main(String[] args) throws Exception{
        Server sv = new Server();
    }
}

class ServerThread implements Runnable
{
    Socket sock;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    ServerThread(Socket sock)
    {
        this.sock = sock;
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        Thread th = new Thread(this);
        th.start();
        
    }
    
    
    @Override
    public void run() {
        while(true)
        {
            try {
                String s =(String) ois.readObject();
                
                sendMessage(s);
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                
                JOptionPane.showMessageDialog(new JFrame(), "Error", "Dialog",
        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void sendMessage(String s)
    {
        int count = 0;
        for(int c = 0; c < Server.size; c++)
        {
            try {
                //System.out.println("here");
                Server.array[c].oos.writeObject(s);
                
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}