package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


public class UDPClient
{
    String toSend;

    public UDPClient(String _toSend) {
        toSend = _toSend;
        try {
            sendData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendData() throws IOException {
        DatagramSocket clientSocket = new DatagramSocket();

//"localhost"
        InetAddress IPAddress = InetAddress.getByName("192.168.43.52");
        byte[] sendData;
        String sentence;
        Netstrings netstring = new Netstrings();
        sentence = netstring.encodedNetString(toSend);
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