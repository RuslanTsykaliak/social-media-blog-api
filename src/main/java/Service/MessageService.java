package Service;

import DAO.MessageDAO;
import Model.Message;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class MessageService {

    private final MessageDAO messageDAO;

    public MessageService(Connection connection) {
        this.messageDAO = new MessageDAO(connection);
    }

    public void create(Message message) throws SQLException {
        if (message == null || message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Invalid message: Text must be between 1 and 255 characters.");
        }
        if (message.getPosted_by() <= 0) {
            throw new IllegalArgumentException("Invalid message: Posted_by account ID is not valid.");
        }
        messageDAO.create(message);
    }

    public Message read(int messageId) throws SQLException {
        return messageDAO.read(messageId);
    }

    public void update(Message message) throws SQLException {
        if (message == null || message.getMessage_id() <= 0 || message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Invalid message for update: Text must be between 1 and 255 characters and message ID must be valid.");
        }
        messageDAO.update(message);
    }

    public void delete(Message message) throws SQLException {
        if (message == null || message.getMessage_id() <= 0) {
            throw new IllegalArgumentException("Invalid message for deletion: Message ID must be valid.");
        }
        messageDAO.delete(message);
    }

    public List<Message> getAll() throws SQLException {
        return messageDAO.getAll();
    }

    public List<Message> readByAccount(int accountId) throws SQLException {
        if (accountId <= 0) {
            throw new IllegalArgumentException("Invalid account ID for retrieving messages.");
        }
        return messageDAO.readByAccount(accountId);
    }
}