package Client;

import javax.swing.*;
import Common.WebSocketCommand;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class LibraryClient {
    protected static boolean authorized = false;
    protected static int role = -1;
    protected static String userName = "";
    protected static JFrame initialFrame = new JFrame();
    protected static JFrame afterLoginFrame = new JFrame();
    protected static RentablesDisplay rentablesDisplayFrame = new RentablesDisplay();
    protected static JFrame loginFrame;
    protected static JFrame errorFrame;
    protected static Socket socket;
    protected static PrintWriter out;
    protected static BufferedReader in;

    protected static void openAfterLoginFrame(){
        JButton userData = new JButton("Konto");
        JButton browseButton = new JButton("Przeglądaj zbiory biblioteki Papieskiej");

        userData.setBounds(10, 10, 220, 50);
        userData.addActionListener(new LoginFrameOpener());
        browseButton.setBounds(230, 10, 300, 50);
        browseButton.addActionListener(new RentablesDisplay.RentablesDisplayOpener());

        afterLoginFrame.add(userData);
        afterLoginFrame.add(browseButton);

        if(authorized && role == 0)
        {
            JButton manageUsersButton = new JButton("Zarządzaj użytkownikami");
            manageUsersButton.setBounds(530, 10, 300, 50);
            afterLoginFrame.add(manageUsersButton);
        }

        afterLoginFrame.setSize(850, 100);

        afterLoginFrame.setLayout(null);

        afterLoginFrame.setVisible(true);
    }

    public static void main(String[] args)
    {
        try {
            socket = new Socket("127.0.0.1", 9000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Creating instance of JButton
            JButton loginButton = new JButton("Zaloguj");
            JButton browseButton = new JButton("Przeglądaj zbiory biblioteki Papieskiej");

            // x axis, y axis, width, height
            loginButton.setBounds(10, 10, 220, 50);
            loginButton.addActionListener(new LoginFrameOpener());
            browseButton.setBounds(230, 10, 300, 50);
            browseButton.addActionListener(new RentablesDisplay.RentablesDisplayOpener());
            // adding button in JFrame
            initialFrame.add(loginButton);
            initialFrame.add(browseButton);

            // 400 width and 500 height
            initialFrame.setSize(550, 100);

            // using no layout managers
            initialFrame.setLayout(null);

            // making the frame visible
            initialFrame.setVisible(true);
        }catch (Exception e) {
            errorFrame = new JFrame();
            JLabel errorMessage = new JLabel("Wystąpił błąd przy w działaniu aplikacji bilbioteki!");
            errorMessage.setBounds(10, 10, 500, 100);
            errorFrame.add(errorMessage);
            String exceptionMessage = e.getMessage();
            JLabel errorCause = new JLabel(exceptionMessage);
            errorCause.setHorizontalAlignment(SwingConstants.CENTER);
            errorCause.setBounds(110, 50, 450, 100);
            errorFrame.add(errorCause);
            errorFrame.setSize(520, 220);
            errorFrame.setVisible(true);
        }


//        System.out.println("Wpisz swoje imie:");
//        Scanner scanner = new Scanner(System.in);
//        String userName = scanner.nextLine();
//        try {
//            Socket s = new Socket("127.0.0.1", 9000);
//            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
//            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
//            String inputLine;
//            while ((inputLine = in.readLine()) != null)
//            {
//                JSONObject question = new JSONObject(inputLine);
//                System.out.println(question.getString("QuestionTitle"));
//                System.out.println("A: " + question.getString("A"));
//                System.out.println("B: " + question.getString("B"));
//                System.out.println("C: " + question.getString("C"));
//                System.out.println("D: " + question.getString("D"));
//                String answerFull = scanner.nextLine();
//                WebSocketCommand a = new WebSocketCommand();
//
//                String answerJson = new JSONObject(a, new String[]{"User", "QuestionId", "AnswerLetter"}).toString();
//                out.println(answerJson);
//            }
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }
}
