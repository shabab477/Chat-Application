package chat.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Shabab
 */
public class Client 
{   
    private static final String newline = System.lineSeparator();
    JFrame fr;
    JTextField send;
    JTextArea textArea ;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    
    void buildGui(String n)
    {
        fr = new JFrame("Chat Client");
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setSize(300, 300);
        send = new JTextField(20);
        fr.setResizable(false);
        
        fr.getContentPane().add(BorderLayout.SOUTH, send);
        
        //make buttons and listener
        
        JButton send = new JButton("Send");
        Box bx = new Box(BoxLayout.Y_AXIS);
        bx.setOpaque(true);
        send.addActionListener(new WriteThread(oos, n, this.send));
        bx.setBackground(Color.lightGray);
        bx.add(send);
        
        
        fr.getContentPane().add(BorderLayout.EAST, bx);
        
        textArea = new JTextArea(100, 100);

        JScrollPane scrollableTextArea = new JScrollPane(textArea);

 
        textArea.setEditable(true);
        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        fr.getContentPane().add(BorderLayout.CENTER, scrollableTextArea);
        fr.setVisible(true);
        
    }
   
	public static void main(String args[])
	{
            Client c = new Client();
		try
		{
                    Scanner sc = new Scanner(System.in);
                    
                    String serverAddress=JOptionPane.showInputDialog(new JFrame(), "Give ip address");
                    int serverPort=33333;
                    Socket server =new Socket(serverAddress,serverPort);
                    String name = JOptionPane.showInputDialog(new JFrame(), "Give your name");
                    c.oos = new ObjectOutputStream(server.getOutputStream());
                    c.ois = new ObjectInputStream(server.getInputStream());
                    c.buildGui(name);
                    c.new ReadThread(c.ois, c.textArea);
                    
                    
		}catch(Exception e)
		{
			System.out.println (e);
		}
		
	}
        
        
        class ReadThread implements Runnable{
    
            ObjectInputStream ois;
            JTextArea area;
            ReadThread(ObjectInputStream ois, JTextArea ja)
            {
                area = ja;
                this.ois = ois;
                Thread th = new Thread(this);
                th.start();
            }

            @Override
            public void run() {
                while(true)
                {
                    try {
                        String s = (String)ois.readObject();
                        
                        area.append(s + newline);
                    } catch (IOException ex) {
                        Logger.getLogger(ReadThread.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ReadThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }



        }
        
        



        class WriteThread implements ActionListener{
            ObjectOutputStream oos;
            String name;
            JTextField field;
            WriteThread(ObjectOutputStream oos, String name, JTextField f)
            {
                field = f;
                this.oos = oos;
                this.name = name;

                
                
            }

           
            @Override
            public void actionPerformed(ActionEvent ae) {
                String s = field.getText();
                try {
                    oos.writeObject(name + ": " + s);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                        
            }
        }



}

