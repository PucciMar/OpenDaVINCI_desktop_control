package models;

import java.util.Observable;

public class NetStrings extends Observable
{
    String encodedNetString(String plainInput)
    {
        FeedbackMessage feedbackMessage = new FeedbackMessage();
        //if the input string is empty, return "error"
        if (plainInput.isEmpty())
        {
            feedbackMessage.setMessage("NetString encode error: incoming String is Null!");
            setChanged();
            notifyObservers(feedbackMessage);
            throw new NullPointerException();
        }

        //return a NetString in the form of [len]":"[string]","
        return plainInput.length() + ":" + plainInput + ",";
    }

    public String decodedNetString(String netString)
    {
        FeedbackMessage feedbackMessage = new FeedbackMessage();
        //if the NetString is less than 3 characters, it's either an invalid one or contains an empty string
        if (netString.length() < 3)
        {
            feedbackMessage.setMessage("NetString decode error: outgoing String is too short!");
            setChanged();
            notifyObservers(feedbackMessage);
            throw new Error();
        }
        int colonIndex = netString.indexOf(':');

        // if there's no colon, then it's an invalid NetString
        if (colonIndex < 0)
        {
            feedbackMessage.setMessage("NetString decode error: outgoing String missing colon!");
            setChanged();
            notifyObservers(feedbackMessage);
            throw new Error();
        }

        //parse until the colon. Those should be the control digits
        String parsedDigits = netString.substring(0, colonIndex);
        int controlDigits = Integer.parseInt(parsedDigits);

        //if the control digit is smaller than 1, then it's either not a digit or the NetString is invalid
        if (controlDigits < 1)
        {
            feedbackMessage.setMessage("NetString decode error: invalid digit!");
            setChanged();
            notifyObservers(feedbackMessage);
            throw new Error();
        }

        //parse after the semicolon until the end of the string
        String command = netString.substring(colonIndex + 1);

        // if it's an empty string, return "error"
        if (command.isEmpty())
        {
            feedbackMessage.setMessage("NetString decode error: outgoing String is Null!");
            setChanged();
            notifyObservers(feedbackMessage);
            throw new NullPointerException();
        }

        //if last character is a comma, remove it
        if (command.substring(command.length() - 1).equals(","))
            command = command.substring(0, command.indexOf(',') - 1);

        //if string's length isn't equal with the control digits, it's an invalid NetString
        int commandLength = command.length();
        if (commandLength != controlDigits)
        {
            feedbackMessage.setMessage("NetString decode error: outgoing String length not equal digit!");
            setChanged();
            notifyObservers(feedbackMessage);
            throw new Error();
        }

        return command;
    }
}
