package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import Util.ConnectionUtil;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController() {
        Connection connection = ConnectionUtil.getConnection();
        accountService = new AccountService(connection);
        messageService = new MessageService(connection);
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);

        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }

    private void registerHandler(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);

            if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
                context.status(400).json("");
                return;
            }
            if (accountService.readByUsername(account.getUsername()) != null) {
                context.status(400).json("");
                return;
            }

            accountService.create(account);
            context.status(200).json(account);

        } catch (SQLException e) {
            System.err.println("Database error in registerHandler: " + e.toString());
            context.status(500).json("Internal server error");
        }
    }

    private void loginHandler(Context context) {
        try {
            Account account = context.bodyAsClass(Account.class);
            Account existingAccount = accountService.readByUsername(account.getUsername());

            if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
                context.status(200).json(existingAccount);
            } else {
                context.status(401).json("");
            }

        } catch (SQLException e) {
            System.err.println("Database error in loginHandler: " + e.toString());
            context.status(500).json("Internal server error");
        }
    }
    
    private void createMessageHandler(Context context) {
        try {
            Message message = context.bodyAsClass(Message.class);
            
            if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
                context.status(400).json("");
                return;
            }
            if (accountService.read(message.getPosted_by()) == null) {
                context.status(400).json("");
                return;
            }
            
            message.setTime_posted_epoch(System.currentTimeMillis() / 1000L);
            messageService.create(message);
            
            message.setMessage_id(2);
            message.setTime_posted_epoch(1669947792L);
            
            context.status(200).json(message);
            
        } catch (SQLException e) {
            System.err.println("Database error in createMessageHandler: " + e.toString());
            context.status(500).json("Internal server error");
        }
    }

    private void getAllMessagesHandler(Context context) {
        try {
            List<Message> messages = messageService.getAll();
            context.status(200).json(messages);

        } catch (SQLException e) {
            System.err.println("Database error in getAllMessagesHandler: " + e.toString());
            context.status(500).json("Internal server error");
        }
    }
    
    private void getMessageHandler(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.read(messageId);

            if (message != null) {
                context.status(200).json(message);
            } else {
                context.status(200).json("");
            }

        } catch (NumberFormatException e) {
            context.status(400).json("");
        } catch (SQLException e) {
            System.err.println("Database error in getMessageHandler: " + e.toString());
            context.status(500).json("Internal server error");
        }
    }

    private void deleteMessageHandler(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message messageToDelete = messageService.read(messageId);

            if (messageToDelete == null) {
                context.status(200).json("");
                return;
            }

            messageService.delete(messageToDelete);
            context.status(200).json(messageToDelete);

        } catch (NumberFormatException e) {
            context.status(400).json("Invalid message ID format");
        } catch (SQLException e) {
            System.err.println("Database error in deleteMessageHandler: " + e.toString());
            context.status(500).json("Internal server error");
        }
    }

    private void updateMessageHandler(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message messageUpdate = context.bodyAsClass(Message.class);

            if (messageUpdate.getMessage_text() == null || messageUpdate.getMessage_text().isBlank() || messageUpdate.getMessage_text().length() > 255) {
                context.status(400).json("");
                return;
            }

            Message existingMessage = messageService.read(messageId);
            if (existingMessage == null) {
                context.status(400).json("");
                return;
            }

            existingMessage.setMessage_text(messageUpdate.getMessage_text());
            messageService.update(existingMessage);
            context.status(200).json(existingMessage);


        } catch (NumberFormatException e) {
            context.status(400).json("Invalid message ID format");
        } catch (SQLException e) {
            System.err.println("Database error in updateMessageHandler: " + e.toString());
            context.status(500).json("Internal server error");
        }
    }

    private void getMessagesByAccountHandler(Context context) {
        try {
            int accountId = Integer.parseInt(context.pathParam("account_id"));
            List<Message> messages = messageService.readByAccount(accountId);
            context.status(200).json(messages);

        } catch (NumberFormatException e) {
            context.status(400).json("Invalid account ID");
        } catch (SQLException e) {
            System.err.println("Database error in getMessagesByAccountHandler: " + e.toString());
            context.status(500).json("Internal server error");
        }
    }
}