package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Client.LibraryClient.initialFrame;
import static Client.LibraryClient.loginFrame;

public class LoginFrameOpener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        initialFrame.setVisible(false);
        loginFrame = new JFrame();
        JTextField userName = new JTextField("Nazwa użytkownika");
        userName.setBounds(10,10,200,50);
        JPasswordField passwordField = new JPasswordField("Hasło");
        passwordField.setBounds(10,60,200,50);
        JButton loginButton = new JButton("Zaloguj");
        loginButton.setBounds(10,110,200,50);
        loginButton.addActionListener(new LoginActionHandler(userName, passwordField));
        loginFrame.setLayout(null);
        loginFrame.add(userName);
        loginFrame.add(passwordField);
        loginFrame.add(loginButton);
        loginFrame.setSize(300,200);
        loginFrame.setVisible(true);
    }
}
