package dao;

import model.Account;

public interface AccountDao {
    void createAccount(Account account);
    boolean emailExists(String username);
    int getAccountIdByEmail(String email);

    Account getAccountByEmail(String email);

    void updateAccount(Account account);

    void deleteAccount(int accountId);

}
