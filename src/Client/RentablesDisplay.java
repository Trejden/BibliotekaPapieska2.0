package Client;

import Common.WebSocketCommand;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Client.LibraryClient.*;

public class RentablesDisplay extends JFrame {
    public static class RentablesDisplayOpener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{

                WebSocketCommand getAllBooksCommand = new WebSocketCommand();
                getAllBooksCommand.Command = 3;
                getAllBooksCommand.Arguments = new String[]{};
                JSONObject getAllBooksCommandJson = new JSONObject(getAllBooksCommand, new String[]{"Command", "Arguments"});
                System.out.println(getAllBooksCommandJson.toString());
                out.println(getAllBooksCommandJson.toString());
                String response = in.readLine();
                JSONObject responseJson = new JSONObject(response);
                int responseCode = responseJson.getInt("Status");
                if(responseCode != -1)
                {
                    String bookListJsonString = responseJson.getString("Message");
                    JSONArray booklistJsonArray = new JSONArray(bookListJsonString);
                    rentablesDisplayFrame.showWindowWithData(booklistJsonArray);
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

    JLabel label = new JLabel();

    public RentablesDisplay(){
        super();
        setSize(500, 500);
        add(label, BorderLayout.SOUTH);
    }
    public void showWindowWithData(JSONArray data){
        label.setText(data.toString());
        setVisible(true);
    }
}
