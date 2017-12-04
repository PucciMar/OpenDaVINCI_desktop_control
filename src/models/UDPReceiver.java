package models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Observable;

public class UDPReceiver extends Observable
{
    private DatagramSocket socket;
    private int timeout = 10000;

    private boolean isRunning;

    private volatile long lastReadTime;

    private BufferedImage img;
    private String IP;
    private int PORT;
    private FeedbackMessage feedback_message;

    public UDPReceiver(String ip_address, int port) throws IOException
    {
        if (ip_address.isEmpty() || port <= 0) throw new IllegalArgumentException();
        this.IP = ip_address;
        this.PORT = port;
    }

    public void receiveBytes() throws IOException
    {
        feedback_message = new FeedbackMessage();
        feedback_message.setMessage("Initialized UPD receiver on " + IP + ":" + PORT + " . . .");
        setChanged();
        notifyObservers(feedback_message);

        System.out.println(feedback_message.getMessage());
        Thread thread = new Thread(() ->
        {
            try
            {
                socket = new DatagramSocket(null);
                socket.setSoTimeout(timeout);
                InetSocketAddress address = new InetSocketAddress(IP, PORT);
                socket.bind(address);

                feedback_message = new FeedbackMessage();
                feedback_message.setMessage("Connected");
                setChanged();
                notifyObservers(feedback_message);

                byte[] buf = new byte[60000];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                while (isConnectionAlive() && isRunning)
                {
                    socket.receive(packet);
                    lastReadTime = System.currentTimeMillis();

                    // display response
                    String received = new String(packet.getData());
                    System.out.println("Quote of the Moment: received " + received);

                    buf = packet.getData();

                    img = ImageIO.read(new ByteArrayInputStream(buf));
                    if (img != null)
                    {
                        setChanged();
                        notifyObservers(img);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace(); //TODO delete after debug
                socket.close();
                feedback_message.setMessage("VIDEO CONNECTION LOST!");
                setChanged();
                notifyObservers(feedback_message);

                feedback_message = new FeedbackMessage();
                feedback_message.setMessage("Connected");
                setChanged();
                notifyObservers(feedback_message);
            }
//"CONNECTION TERMINATED!"
        });
        isRunning = true;
        thread.start();
    }

    public void disconnect()
    {
        isRunning = false;
        socket.disconnect();
        socket.close();

        feedback_message = new FeedbackMessage();
        feedback_message.setMessage("Disconnected");
        setChanged();
        notifyObservers(feedback_message);
    }

    private boolean isConnectionAlive()
    {
        int maxTimeout = 25000;
        return System.currentTimeMillis() - lastReadTime < maxTimeout;
    }
}
