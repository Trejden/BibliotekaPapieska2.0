package Common;

public class WebSocketResponse {
    public int Status;
    public String Message;
    public WebSocketResponse(int status, String message){
        this.Status = status;
        this.Message = message;
    }
}
