package service;

import dao.AccountDao;
import model.Account;

public class AccountService {
    private AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
    public void createAccount(Account account) {
        accountDao.createAccount(account);
    }
    public boolean emailExists(String email) {
        return accountDao.emailExists(email);
    }
    public Account getAccountByEmail(String email) {
        return accountDao.getAccountByEmail(email);
    }
    public int getAccountIdByEmail(String email) {
        return accountDao.getAccountIdByEmail(email);
    }
    public void updateAccount(Account account) {
        accountDao.updateAccount(account);
    }

    public void deleteAccount(int accountId) {
        accountDao.deleteAccount(accountId);
    }
}
