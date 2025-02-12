package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.ArrayList;
import java.util.List;


public class MessageService {

    MessageDAO messageDAO = new MessageDAO();

    public Message createMessage(Message message) {
        if (message == null || message.getMessage_text() == null || message.getMessage_text().isBlank()) {
            return null;
        }

        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        if (messageId <= 0) {
            return null;
        }
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId) {
        if (messageId <= 0) {
            return null;
        }
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessageById(int messageId, String messageText) {
        if (
            messageId <= 0 || 
            messageText == null || 
            messageText.isBlank() || 
            messageText.length() > 255
        ) {
            return null;
        }
        return messageDAO.updateMessageById(messageId, messageText);
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        if (accountId <= 0) {
            return new ArrayList<>();
        }
        return messageDAO.getMessagesByAccountId(accountId);
    }

}