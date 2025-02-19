package Client;

import Common.WebSocketCommand;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Client.LibraryClient.*;

public class UserDataDisplay extends JFrame {
    public static class DisplayOpener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try{

                WebSocketCommand getUserCommand = new WebSocketCommand();
                getUserCommand.Command = 9;
                getUserCommand.Arguments = new String[]{userName};
                JSONObject getUserCommandJson = new JSONObject(getUserCommand, new String[]{"Command", "Arguments"});
                System.out.println(getUserCommandJson.toString());
                out.println(getUserCommandJson.toString());
                String response = in.readLine();
                System.out.println(response);
                JSONObject responseJson = new JSONObject(response);
                int responseCode = responseJson.getInt("Status");
                if(responseCode != -1)
                {
                    String userDataJson = responseJson.getString("Message");
                    JSONObject userData = new JSONObject(userDataJson);
                    userDataDisplayFrame.showWithData(userData);
                    initialFrame.setVisible(false);
                    afterLoginFrame.setVisible(false);
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

    public static class DisplayCloser implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            userDataDisplayFrame.setVisible(false);
            openAfterLoginFrame();
        }
    }

    JLabel name = new JLabel();
    JLabel role = new JLabel();
    JLabel email = new JLabel();
    JButton backButton = new JButton("wstecz");

    public UserDataDisplay()
    {
        super();
        setLayout(new GridLayout(20,1));
        setSize(500, 500);
        add(name);
        add(role);
        add(email);
        add(backButton);
        backButton.addActionListener(new DisplayCloser());
    }

    public void showWithData(JSONObject data){
        name.setText("Nazwa uzytkownika: " + data.getString("UserName"));
        String roleDisplay;
        int roleId = data.getInt("UserRole");
        roleDisplay = switch (roleId) {
            case 1 -> "Administrator Systemu";
            case 2 -> "Zarządca biblioteki";
            case 3 -> "Uzytkownik";
            default -> "";
        };
        role.setText("Rola użytkownika: "+roleDisplay);
        email.setText("Ares Email: " + data.getString("Mail"));
        setVisible(true);
    }
}
