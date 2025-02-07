package Client;

import Common.WebSocketCommand;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class LibraryClient {
    public static void main(String[] args)
    {
        System.out.println("Wpisz swoje imie:");
        Scanner scanner = new Scanner(System.in);
        String userName = scanner.nextLine();
        try {
            Socket s = new Socket("127.0.0.1", 9000);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                JSONObject question = new JSONObject(inputLine);
                System.out.println(question.getString("QuestionTitle"));
                System.out.println("A: " + question.getString("A"));
                System.out.println("B: " + question.getString("B"));
                System.out.println("C: " + question.getString("C"));
                System.out.println("D: " + question.getString("D"));
                String answerFull = scanner.nextLine();
                WebSocketCommand a = new WebSocketCommand();

                String answerJson = new JSONObject(a, new String[]{"User", "QuestionId", "AnswerLetter"}).toString();
                out.println(answerJson);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
