package Service;

import DAO.AccountDAO;
import Model.Account;


public class AccountService {

    AccountDAO accountDAO = new AccountDAO();

    public Account registerAccount(Account account) {
        if (account == null) {
            return null;
        }
        if (
            account.getUsername() == null || account.getUsername().isBlank() ||
            account.getPassword() == null || account.getPassword().length() < 4
            ) {
                return null;
            }
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }

        return accountDAO.createAccount(account);
    }

    public Account loginAccount(Account account) {
        if (account == null || account.getUsername() == null || account.getPassword() == null) {
            return null;
        }

        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());

        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        } else {
            return null;
        }
    }

}