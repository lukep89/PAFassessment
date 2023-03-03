package ibf2022.PAFassessment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.PAFassessment.model.Account;
import ibf2022.PAFassessment.repo.AccountRepo;

@Service
public class TransferService {
    
    @Autowired
    private AccountRepo accountRepo;

    public List<Account> getListOfAccounts(){
        return accountRepo.getListOfAccounts();
    }

}
