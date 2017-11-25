package models;

import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPReceiver {
    private Image img;
    public ImageView imgView;

    public UDPReceiver() throws IOException {
        System.out.println("Initialized");
        receiveBytes();
    }

    private void receiveBytes() throws IOException {
        Thread thread = new Thread(() ->
        {
            try{
                DatagramSocket socket = new DatagramSocket();

                byte[] buf = new byte[60000];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                while (true) {
                    socket.receive(packet);

                    // display response
                    String received = new String(packet.getData());
                    System.out.println("Quote of the Moment: " + received);


                    buf = packet.getData();

                    img = ImageIO.read(new ByteArrayInputStream(buf));

                    setImage(img);
                }

                //      socket.close();
            }catch(Exception e)
            {
                e.printStackTrace();
            }

        });
    }

    private void setImage(Image img){
        // No idea if this will work, needs testing
        imgView.setImage(javafx.scene.image.Image.impl_fromPlatformImage(img));
    }
}
