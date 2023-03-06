package ibf2022.PAFassessment.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ibf2022.PAFassessment.exception.OrderException;
import ibf2022.PAFassessment.model.Account;
import ibf2022.PAFassessment.model.Transfer;
import ibf2022.PAFassessment.repo.AccountsRepository;

@Service
public class FundsTransferService {

    @Autowired
    private AccountsRepository accountRepo;

    public List<Account> getListOfAccounts() {
        return accountRepo.getListOfAccounts();
    }

    public Optional<Account> getAccountById(String id) {
        return accountRepo.getAccountById(id);
    }

    // check errors
    // if any fail @transactional rollback
    public List<String> getCErrorList(Transfer transfer) {

        String fromAccountId = transfer.getFromAccount();
        String toAccountId = transfer.getToAccount();

        Account fromAcc = accountRepo.getAccountById(fromAccountId).get();
        Account toAcc = accountRepo.getAccountById(toAccountId).get();

        String fromAccId = fromAcc.getAccountId();
        String toAccId = toAcc.getAccountId();

        Float amount = transfer.getAmount();

        Float fromAccBalance = fromAcc.getBalance();
        Float toAccBalance = toAcc.getBalance();

        List<String> cErrors = new ArrayList<>();

        if (accountRepo.getAccountById(fromAccountId).get() == null) {
            cErrors.add("C0 - 'From account' do not exist.");
        }

        if (accountRepo.getAccountById(toAccountId).get() == null) {
            cErrors.add("C0 - 'To account' do not exist.");
        }

        if (fromAccountId.length() != 10) {
            cErrors.add("C1 - 'From account' invalid account id length.");
        }

        if (toAccountId.length() != 10) {
            cErrors.add("C1 - 'To account' invalid account id length.");
        }

        if (fromAccountId.equals(toAccountId)) {
            cErrors.add("C2 - The from & to account should not be the same.");
        }

        if (amount <= 0) {
            cErrors.add("C3 - The transfer amount should be more than 0 dollars.");
        }

        if (amount < 10) {
            cErrors.add("C4 - The transfer amount should at least 10 dollars.");
        }

        if (fromAccBalance < amount) {
            cErrors.add("C5 - The account should suddicient funds for transfer.");
        }

        return cErrors;

    }

    @Transactional(rollbackFor = OrderException.class)
    public void updateAccount(Transfer transfer) throws OrderException {

        String transacId = UUID.randomUUID().toString().substring(0, 10);
        transfer.setTransactionId(transacId);
        transfer.setDate(LocalDate.now());

        // System.out.println("======== @5 Service transfer: " + transfer.toString() +
        // "\n\n");

        Account fromAcc = getAccountById(transfer.getFromAccount()).get();
        Account toAcc = getAccountById(transfer.getToAccount()).get();

        Float fromDeltaBalance = fromAcc.getBalance() - transfer.getAmount();
        Float toDeltaBalance = transfer.getAmount() + toAcc.getBalance();

        Boolean fromResult = accountRepo.updateBalanceById(fromDeltaBalance, transfer.getFromAccount());
        Boolean toResult = accountRepo.updateBalanceById(toDeltaBalance, transfer.getToAccount());
        // Boolean toResult = false; //for testing OrderException

        if (fromResult != toResult) {
            throw new OrderException("Error on the update of account balance!");
        }
    }

}
