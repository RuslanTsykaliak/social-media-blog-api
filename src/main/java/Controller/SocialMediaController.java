package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
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
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);

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
        Account account = context.bodyAsClass(Account.class);
        Account registeredAccount = accountService.registerAccount(account);

        if (registeredAccount != null) {
            context.status(200).json(registeredAccount);
        } else {
            context.status(400).json("");
        }
    }

    private void loginHandler(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account loggedInAccount = accountService.loginAccount(account);

        if (loggedInAccount != null) {
            context.status(200).json(loggedInAccount);
        } else {
            context.status(401).json("");
        }
    }

    public void createMessageHandler(Context context) {
        Message message = context.bodyAsClass(Message.class);
        Message createdMessage = messageService.createMessage(message);

        if (createdMessage != null) {
            context.status(200).json(createdMessage);
        } else {
            context.status(400).json("");
        }
    }

    public void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.status(200).json(messages);
    }

    public void getMessageByIdHandler(Context context) {
        String messageIdStr = context.pathParam("message_id");
        int messageId;

        try {
            messageId = Integer.parseInt(messageIdStr);
        } catch (NumberFormatException e ) {
            context.status(400).json("Invalid message ID format. Must be an integer.");
            return;
        }
        
        Message message = messageService.getMessageById(messageId);

        context.status(200);

        if (message != null) {
            context.json(message);
        } else {
            context.json("");
        }
    }

    public void deleteMessageByIdHandler(Context context) {
        String messageIdStr = context.pathParam("message_id");
        int messageId;

        try {
            messageId = Integer.parseInt(messageIdStr);
        } catch (NumberFormatException e) {
            context.status(400).json("Invalid message ID format. Must be an integer.");
            return;
        }

        Message deletedMessage = messageService.deleteMessageById(messageId);

        context.status(200);

        if (deletedMessage != null) {
            context.json(deletedMessage);
        } else {
            context.json("");
        }
    }

    public void updateMessageByIdHandler(Context context) {
        String messageIdStr = context.pathParam("message_id");
        int messageId;

        try {
            messageId = Integer.parseInt(messageIdStr);
        } catch (NumberFormatException e) {
            context.status(400).json("Invalid message ID format. Must be an integer.");
            return;
        }

        Message messageUpdate = context.bodyAsClass(Message.class);
        String newMessageText = messageUpdate.getMessage_text();

        Message updateMessage = messageService.updateMessageById(messageId, newMessageText);

        if (updateMessage != null) {
            context.status(200).json(updateMessage);
        } else {
            context.status(400).json("");
        }
    }

    public void getMessagesByAccountIdHandler(Context context) {
        String accountIdStr = context.pathParam("account_id");
        int accountId;

        try {
            accountId = Integer.parseInt(accountIdStr);
        } catch (NumberFormatException e) {
           context.status(400).json("Invalid account ID format. Must be an integer.");
           return;
        }

        List<Message> messages = messageService.getMessagesByAccountId(accountId);

        context.status(200).json(messages);
    }

}