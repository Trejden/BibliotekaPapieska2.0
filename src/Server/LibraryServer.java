package Server;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class LibraryServer implements Runnable{
    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;

    public LibraryServer(int port){
        this.serverPort = port;
    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Serwer zatrzymany!") ;
                    return;
                }
                throw new RuntimeException(
                        "Błąd przy akceptowaniu połączenia", e);
            }
            System.out.println("Zaakceptowano polączenie klienta");
            new Thread(new ServerConnectionWorker(clientSocket)).start();
        }
        System.out.println("Serwer zatrzymany.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Błąd przy zatrzymywaniu serwera", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Nie można otworzyć portu: " + this.serverPort, e);
        }
    }
}
