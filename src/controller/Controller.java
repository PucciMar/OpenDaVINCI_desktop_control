package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import models.UDPClient;
import models.UDPReceiver;

import java.io.IOException;


public class Controller {
    public Button send;

    public Spinner p;
    public Spinner i;
    public Spinner d;

    public Text pCurr;
    public Text iCurr;
    public Text dCurr;

    private String toSend;


    public TextField ip;

    public Controller() throws IOException {
        startUDPReceiver();
    }

    public void sendPackage(ActionEvent actionEvent) {
        if (!p.getEditor().getText().isEmpty()) {
            toSend = "p" + p.getEditor().getText();
            UDPClient udPclient = new UDPClient(toSend);
            pCurr.setText(p.getEditor().getText());

        }
        if (!i.getEditor().getText().isEmpty()) {
            toSend = "i" + i.getEditor().getText();
            UDPClient udPclient = new UDPClient(toSend);
            iCurr.setText(i.getEditor().getText());
        }
        if (!d.getEditor().getText().isEmpty()) {
            toSend = "d" + d.getEditor().getText();
            UDPClient udPclient = new UDPClient(toSend);
            dCurr.setText(d.getEditor().getText());
        }
    }

    private void startUDPReceiver() throws IOException {
        UDPReceiver receiver = new UDPReceiver();
    }
}
