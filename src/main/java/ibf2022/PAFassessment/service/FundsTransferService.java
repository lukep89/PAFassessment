package ibf2022.PAFassessment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            // model.addAttribute("C0a", "C0 - 'From account' do not exist.");
            cErrors.add("C0 - 'From account' do not exist.");
        }

        if (accountRepo.getAccountById(toAccountId).get() == null) {
            // model.addAttribute("COb", "C0 - 'To account' do not exist.");
            cErrors.add("C0 - 'To account' do not exist.");
        }

        if (fromAccountId.length() != 10) {
            // model.addAttribute("C1a", "C1 - 'From account' invalid account id length.");
            cErrors.add("C1 - 'From account' invalid account id length.");
        }

        if (toAccountId.length() != 10) {
            // model.addAttribute("C1b", "C1 - 'To account' invalid account id length.");
            cErrors.add("C1 - 'To account' invalid account id length.");
        }

        if (fromAccountId.equals(toAccountId)) {
            // model.addAttribute("C2", "C2 - The accounts should not be the same.");
            cErrors.add("C2 - The accounts should not be the same.");
        }

        if (amount <= 0) {
            // model.addAttribute("C3", "C3 - The transfer amount should be more than 0
            // dollars.");
            cErrors.add("C3 - The transfer amount should be more than 0 dollars.");
        }

        if (amount < 10) {
            // model.addAttribute("C4", "C4 - The transfer amount should at least 10
            // dollars.");
            cErrors.add("C4 - The transfer amount should at least 10 dollars.");
        }

        if (fromAccBalance < amount) {
            cErrors.add("C5 - The account should suddicient funds for transfer.");
        }

        return cErrors;

    }

}
