package application.controllers.models.responseModels;

/**
 * Created by DZONI on 20.11.2016.
 */
public class BasicResponse {
    private boolean successful;
    private String responseMessage;
    private int messageId;

    public BasicResponse(boolean successful, String responseMessage, int messageId) {
        this.successful = successful;
        this.responseMessage = responseMessage;
        this.messageId = messageId;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean succesful) {
        this.successful = succesful;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "BasicResponse{" +
                "successful=" + successful +
                ", responseMessage='" + responseMessage + '\'' +
                ", messageId=" + messageId +
                '}';
    }
}
