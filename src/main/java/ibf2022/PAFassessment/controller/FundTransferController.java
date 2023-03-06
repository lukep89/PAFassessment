package ibf2022.PAFassessment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import ibf2022.PAFassessment.exception.OrderException;
import ibf2022.PAFassessment.model.Account;
import ibf2022.PAFassessment.model.Transfer;
import ibf2022.PAFassessment.service.FundsTransferService;
import ibf2022.PAFassessment.service.LogAuditService;
import jakarta.validation.Valid;

@Controller
public class FundTransferController {

    @Autowired
    private FundsTransferService transferSvc;

    @Autowired
    private LogAuditService logAudiSvc;

    @GetMapping(path = { "/", "/index.html" })
    public String showIndex(Model model) {

        List<Account> accounts = transferSvc.getListOfAccounts();

        model.addAttribute("accounts", accounts);
        model.addAttribute("transfer", new Transfer());

        return "index";
    }

    @PostMapping(path = "/transfer")
    public String postTransfer(@Valid Transfer transfer, BindingResult binding, Model model) throws OrderException {

        System.out.println("======== @2 POST transfer: " + transfer.toString() + "\n\n");

        // System.out.println("======== @2 POST fromAccount: " + fromAccount + "\n\n");
        // System.out.println("======== @2 POST toAccount: " + toAccount + "\n\n");

        List<String> cErrors = transferSvc.getCErrorList(transfer);
        System.out.println("======== @3 POST cErrors: " + cErrors + "\n\n"); // able to get errors

        // put binding errors and custom errors together
        if (binding.hasErrors() || cErrors.size() > 0) {
            List<Account> accounts = transferSvc.getListOfAccounts();
            model.addAttribute("accounts", accounts);
            model.addAttribute("transfer", transfer);
            model.addAttribute("cErrors", cErrors);

            return "index";
        }

        // if all validation pass
        // update the transfer amount on both from and to account's balance
        // if update not done then @ transactional and rollback
        transferSvc.updateAccount(transfer);

        // if update of account balances successful then log to redis
        logAudiSvc.saveToRedis(transfer);

        String fromName = transferSvc.getAccountById(transfer.getFromAccount()).get().getName();
        String toName = transferSvc.getAccountById(transfer.getToAccount()).get().getName();

        // System.out.println("======== @8 POST fromName: " + fromName + "\n\n");
        // System.out.println("======== @8 POST toName: " + toName + "\n\n");

        model.addAttribute("transfer", transfer);
        model.addAttribute("fromName",
                fromName);
        model.addAttribute("toName",
                toName);

        return "completed";
    }
}
