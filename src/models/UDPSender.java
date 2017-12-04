package models;

import java.io.IOException;
import java.net.*;
import java.util.Observable;
import java.util.Observer;


public class UDPSender extends Observable implements Observer
{
    private DatagramSocket clientSocket;

    public void sendData(String ip, int port, String toSend) throws IOException
    {
        FeedbackMessage feedbackMessage;
        try
        {
            feedbackMessage = new FeedbackMessage();

            clientSocket = new DatagramSocket();

            //"localhost"
            InetAddress IPAddress = InetAddress.getByName(ip);

            byte[] sendData;
            String sentence;

            NetStrings netString = new NetStrings();
            netString.addObserver(this);
            sentence = netString.encodedNetString(toSend);

            feedbackMessage.setMessage("Sending " + sentence);
            setChanged();
            notifyObservers(feedbackMessage);
            System.out.println(sentence);

            sendData = sentence.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(sendPacket);
            clientSocket.close();
        }
        catch (IOException e)
        {
            clientSocket.close();
            feedbackMessage = new FeedbackMessage();
            feedbackMessage.setMessage("SENDER CONNECTION LOST! REASON: " + e.getMessage());
            setChanged();
            notifyObservers(feedbackMessage);
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (arg instanceof FeedbackMessage)
        {
            setChanged();
            notifyObservers(arg);
        }
    }
}