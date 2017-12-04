package controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.FeedbackMessage;
import models.UDPSender;
import models.UDPReceiver;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


public class Controller implements Observer
{
    public Button send;

    public Spinner p;
    public Spinner i;
    public Spinner d;

    public Text pCurr;
    public Text iCurr;
    public Text dCurr;

    public Label feedbackOutput;

    public Button lane;
    public Button park;
    public Button overtake;
    public Button stop;

    public Button connect;

    public ImageView imageView;

    public TextField ip;

    public Label connectStatus;
    public Label connectedFeed;
    public GridPane gridImage;
    public Label p_receiver;
    public Label p_sender;

    private String IP = "";
    private int PORT_RECEIVER = 0;
    private int PORT_SENDER = 0;

    private UDPReceiver receiver;

    private boolean isConnected = false;

    public Controller() throws IOException
    {
//       gridImage.getProperties().addListener(new ChangeListener<Number>() {
//           @Override
//           public void changed(ObservableValue<? extends Number> observable,
//                               Number oldValue, Number newValue) {
//
//                gridImage.setPrefHeight(imageView.getFitHeight());
//           }
//       });
    }

    public void sendPIDPackage(ActionEvent actionEvent) throws IOException
    {
        if (IP.isEmpty() || PORT_SENDER < 1)
        {
            feedbackOutput.setText("HOST NOT SET!!!");
            return;
        }
        String sent = "";
        int ret = 0;
        actionEvent.getSource();
        String toSend;
        UDPSender udpSender = new UDPSender();
        udpSender.addObserver(this);

        if (!p.getEditor().getText().isEmpty())
        {
            toSend = "p/" + p.getEditor().getText();
            udpSender.sendData(IP, PORT_SENDER, toSend);
            pCurr.setText(p.getEditor().getText());
            sent += "P";
            ret += 1;

        }
        if (!i.getEditor().getText().isEmpty())
        {
            toSend = "i/" + i.getEditor().getText();
            udpSender.sendData(IP, PORT_SENDER, toSend);
            iCurr.setText(i.getEditor().getText());
            sent += "I";
            ret += 1;
        }
        if (!d.getEditor().getText().isEmpty())
        {
            toSend = "d/" + d.getEditor().getText();
            udpSender.sendData(IP, PORT_SENDER, toSend);
            dCurr.setText(d.getEditor().getText());
            sent += "D";
            ret += 1;
        }

        if (ret > 0)
        {
            feedbackOutput.setText(sent + " sent to " + IP + ":" + PORT_SENDER + " . . .");
        }
        else
        {
            feedbackOutput.setText("Nothing to send. . .");
        }

    }

    public void connect(ActionEvent actionEvent) throws IOException
    {
        if (IP.isEmpty() || PORT_SENDER < 1)
        {
            feedbackOutput.setText("HOST NOT SET!!!");
            return;
        }

        if (!isConnected)
        {
            startUDPReceiver(IP, PORT_RECEIVER);
        }
        else
        {
            stopUDPReceiver();
        }
    }

    public void sendFunction(ActionEvent actionEvent) throws IOException
    {
        if (IP.isEmpty() || PORT_SENDER < 1)
        {
            feedbackOutput.setText("HOST NOT SET!!!");
            return;
        }
        String toSend = "";
        UDPSender udpSender = new UDPSender();
        udpSender.addObserver(this);

        Button sourceButton = (Button) actionEvent.getSource();
        if (sourceButton.getId().equals("stop"))
        {
            toSend = "stop";
        }
        else if (sourceButton.getId().equals("lane"))
        {
            toSend = "lane";
        }
        else if (sourceButton.getId().equals("park"))
        {
            toSend = "park";
        }
        else if (sourceButton.getId().equals("overtake"))
        {
            toSend = "overtake";
        }

        if (!toSend.isEmpty())
        {
            udpSender.sendData(IP, PORT_SENDER, toSend);
            feedbackOutput.setText("Sent: " + toSend + " to " + IP + ":" + PORT_SENDER + " . . .");
        }

    }

    public void getIPAndPort(ActionEvent actionEvent) throws IOException
    {
        actionEvent.getSource();
        String ip_port = ip.getText();
        int occurrence_dot = 0;
        int occurrence_column = 0;
        for (int i = 0; i < ip_port.length(); i++)
        {
            if (ip_port.charAt(i) == '.')
            {
                occurrence_dot++;
            }
            else if (ip_port.charAt(i) == ':')
            {
                occurrence_column++;
            }
        }

        if (ip_port.contains("localhost"))
        {
            occurrence_dot = 3;
        }

        if (occurrence_column != 2 || occurrence_dot != 3 || ip_port.indexOf(':') == -1 || ip_port.indexOf(
                ':') == 0 || ip_port.indexOf(
                ':') == (ip_port.length() - 1))
        {
            feedbackOutput.setText("Wrong host format! Try the following pattern IP:PORT_RECEIVER:PORT_SENDER. . .");
            connectedFeed.setText("HOST IS NOT SET!");
            p_receiver.setText("");
            p_sender.setText("");
            return;
        }

        this.IP = ip_port.substring(0, ip_port.indexOf(':'));

        try
        {
            ip_port = ip_port.substring(ip_port.indexOf(':') + 1);
            System.out.println(ip_port);
            this.PORT_RECEIVER = Integer.parseInt(ip_port.substring(0, ip_port.indexOf(':')));
            this.PORT_SENDER = Integer.parseInt(ip_port.substring(ip_port.indexOf(':') + 1));
            if (this.PORT_SENDER < 1024 || this.PORT_RECEIVER < 1024)
            {
                feedbackOutput.setText("PORT_RECEIVER and PORT_SENDER must be a number greater than 1024!");
                this.IP = "";
                this.PORT_RECEIVER = 0;
                this.PORT_SENDER = 0;
                connectedFeed.setText("HOST IS NOT SET!");
                p_receiver.setText("");
                p_sender.setText("");
                return;
            }
        }
        catch (NumberFormatException e)
        {
            //e.printStackTrace();
            feedbackOutput.setText("PORT_RECEIVER and PORT_SENDER must be a number!");
            this.IP = "";
            this.PORT_RECEIVER = 0;
            this.PORT_SENDER = 0;
            connectedFeed.setText("HOST IS NOT SET!");
            p_receiver.setText("");
            p_sender.setText("");
            return;
        }

        connectedFeed.setText(IP);
        p_receiver.setText("R:" + String.valueOf(PORT_RECEIVER));
        p_sender.setText("S:" + String.valueOf(PORT_SENDER));

        connect(null);
    }

    private void setImage(BufferedImage img)
    {
        Image fxImage = SwingFXUtils.toFXImage(img, null);
        imageView.setImage(fxImage);
    }

    private void startUDPReceiver(String ip, int port) throws IOException
    {
        receiver = new UDPReceiver(ip, port);
        receiver.addObserver(this);
        receiver.receiveBytes();
    }

    private void stopUDPReceiver() throws IOException
    {
        receiver.disconnect();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (arg instanceof BufferedImage)
        {
            setImage((BufferedImage) arg);
        }
        else if (arg instanceof FeedbackMessage)
        {
            Platform.runLater(() -> feedbackOutput.setText(((FeedbackMessage) arg).getMessage()));

            if (((FeedbackMessage) arg).getMessage().equals("Connected"))
            {
                isConnected = true;
                connect.getStylesheets().clear();
                connect.getStylesheets().add("view/disconnectStyle.css");
                Platform.runLater(() ->
                {
                    connect.setText("Disconnect");
                    connectStatus.setText("Connected!");
                });
            }
            else if (((FeedbackMessage) arg).getMessage().equals("Disconnected"))
            {
                isConnected = false;
                connect.getStylesheets().clear();
                connect.getStylesheets().add("view/connectStyle.css");
                Platform.runLater(() ->
                {
                    connect.setText("Connect");
                    connectStatus.setText("Disconnected!");
                });
            }
        }
    }

}
