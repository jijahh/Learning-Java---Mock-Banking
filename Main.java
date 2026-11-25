import java.util.HashMap;
import java.util.Map;
import java.security.SecureRandom;

class AccountGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generateFormattedAccountNumber() {
        int number = random.nextInt(10000000, 100000000);
        return String.valueOf(number);
    }
}

class Bank {
    private Map<String, Account> accounts;

    public Bank() {
        this.accounts = new HashMap<>();
    }

    public String registerAccount(String name) {
        String accountNumber = AccountGenerator.generateFormattedAccountNumber();
        while (accounts.containsKey(accountNumber)) {
            accountNumber = AccountGenerator.generateFormattedAccountNumber();
        }
        this.accounts.put(accountNumber, new Account(name, accountNumber));
        return accountNumber;
    }

    public String transfer(String sourceAccountNumber, String destinationAccountNumber, int amount) {
        if (!this.accounts.containsKey(sourceAccountNumber)) {
            return "You do not have an account yet, please register first!";
        }
        if (!this.accounts.containsKey(destinationAccountNumber)) {
            return "The destination account was not found, please confirm the account number!";
        }
        Account sourceAccount = this.accounts.get(sourceAccountNumber);
        Account destinationAccount = this.accounts.get(destinationAccountNumber);
        if (sourceAccount.getBalance() < amount) {
            return "You do not have enough money in your account!";
        }
        sourceAccount.withdraw(amount);
        destinationAccount.deposit(amount);
        return "Transfer Successful! Your new balance is %d".formatted(sourceAccount.getBalance());
    }

    public Account getAccount(String accountNumber) {
        if (!this.accounts.containsKey(accountNumber)) {
            return null;
        }
        return this.accounts.get(accountNumber);
    }
}

class Account {
    private String accountNumber;
    private String name;
    private int balance;

    public Account(String name, String accountNumber) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = 0;
    }

    public String getName() {
        return this.name;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public int getBalance() {
        return this.balance;
    }

    public void deposit(int amount) {
        this.balance += amount;
    }

    public int withdraw(int amount) {
        if (amount <= this.balance) {
            this.balance -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        String jjaAccountNumber = bank.registerAccount("Elijah");
        String audsAccountNumber = bank.registerAccount("Auds");

        bank.getAccount(jjaAccountNumber).deposit(1000);

        System.out.println(bank.transfer(jjaAccountNumber, audsAccountNumber, 500));
        System.out.println(bank.getAccount(jjaAccountNumber).getBalance());
        System.out.println(bank.getAccount(audsAccountNumber).getBalance());
    }
}