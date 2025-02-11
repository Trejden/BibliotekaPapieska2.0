package Client;

import Common.WebSocketCommand;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import static Client.LibraryClient.*;

public class LoginActionHandler implements ActionListener {
    JTextField userName;
    JPasswordField password;
    public LoginActionHandler(JTextField userName, JPasswordField password)
    {
        this.userName = userName;
        this.password = password;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String userNameValue = userName.getText();
            char[] passwordArray = password.getPassword();
            String passwordValue = new String(passwordArray);
            WebSocketCommand loginCommand = new WebSocketCommand();
            loginCommand.Command = 0;
            loginCommand.Arguments = new String[]{userNameValue, passwordValue};
            JSONObject loginCommandJson = new JSONObject(loginCommand, new String[]{"Command", "Arguments"});
            System.out.println(loginCommandJson.toString());
            out.println(loginCommandJson.toString());
            String response = in.readLine();
            JSONObject responseJson = new JSONObject(response);
            int responseCode = responseJson.getInt("Status");
            if(responseCode == -1)
            {
                errorFrame = new JFrame();
                JLabel errorMessage = new JLabel("Błędne dane logowania!");
                errorMessage.setBounds(10, 10, 500, 100);
                errorFrame.add(errorMessage);
                errorFrame.setSize(520, 220);
                errorFrame.setVisible(true);
            }
            else
            {
                authorized = true;
                role = responseCode;
                LibraryClient.userName = userNameValue;
                openAfterLoginFrame();
                loginFrame.setVisible(false);
            }
        } catch (Exception ex) {
            errorFrame = new JFrame();
            JLabel errorMessage = new JLabel("Wystąpił błąd przy w działaniu aplikacji bilbioteki!");
            errorMessage.setBounds(10, 10, 500, 100);
            errorFrame.add(errorMessage);
            String exceptionMessage = ex.getMessage();
            JLabel errorCause = new JLabel(exceptionMessage);
            errorCause.setHorizontalAlignment(SwingConstants.CENTER);
            errorCause.setBounds(110, 50, 450, 100);
            errorFrame.add(errorCause);
            errorFrame.setSize(520, 220);
            errorFrame.setVisible(true);
        }

    }
}
