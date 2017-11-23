package models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Scanner;

class UDPClient
{
    public static void main(String args[]) throws Exception
    {
        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();

//        System.out.println("Enter host IP");
//        String ip=inFromUser.readLine();
        //System.err.println(ip);
//"localhost"
        InetAddress IPAddress = InetAddress.getByName("192.168.43.52");
        byte[] sendData;
//        byte[] receiveData = new byte[1024];
        String sentence = inFromUser.readLine();
        Netstrings netstring = new Netstrings();
        sentence = netstring.encodedNetString(sentence);
        System.out.println(sentence);
        sendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8888);
        clientSocket.send(sendPacket);
//        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//        clientSocket.receive(receivePacket);
//        String modifiedSentence = new String(receivePacket.getData());
//        System.out.println("FROM SERVER:" + modifiedSentence);
        clientSocket.close();
    }
}