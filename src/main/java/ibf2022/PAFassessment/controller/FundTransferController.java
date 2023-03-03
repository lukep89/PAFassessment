package ibf2022.PAFassessment.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

        // List<String> forNameDropList = new ArrayList<>();
        // List<String> forValueDropList = new ArrayList<>();
        // List<String> forTextDropList = new ArrayList<>();

        // for (Account acc : accounts) {
        // String name = acc.getName() + " (" + acc.getAccountId() + ")";
        // forNameDropList.add(name);

        // forTextDropList.add(acc.getName());

        // forValueDropList.add(acc.getAccountId());
        // }

        // System.out.println("========= @1 controller accounts : " + accounts +
        // "\n\n");
        // System.out.println("========= @1 controller forNameDropList : " +
        // forNameDropList + "\n\n");
        // System.out.println("========= @2 controller forValueDropList : " +
        // forValueDropList + "\n\n");
        // System.out.println("========= @2 controller forTextDropList : " +
        // forTextDropList + "\n\n");

        model.addAttribute("accounts", accounts);
        model.addAttribute("transfer", new Transfer());
        // model.addAttribute("forNameDropList", forNameDropList);
        // model.addAttribute("forValueDropList", forValueDropList);
        // model.addAttribute("forTextDropList", forTextDropList);

        return "index";
    }

    @PostMapping(path = "/transfer")
    public String postTransfer(@Valid Transfer transfer, BindingResult binding, Model model) {

        System.out.println("======== @2 POST transfer: " + transfer.toString() + "\n\n");

        String fromAccount = transfer.getFromAccount();
        String toAccount = transfer.getToAccount();

        // System.out.println("======== @2 POST fromAccount: " + fromAccount + "\n\n");
        // System.out.println("======== @2 POST toAccount: " + toAccount + "\n\n");

        List<String> cErrors = transferSvc.getCErrorList(transfer);
        System.out.println("======== @3 POST cErrors: " + cErrors + "\n\n"); // able to get errors

        if (binding.hasErrors()) {
            List<Account> accounts = transferSvc.getListOfAccounts();
            model.addAttribute("accounts", accounts);
            model.addAttribute("transfer", transfer);

            // model.addAttribute("fromAccount", fromAccount);
            // model.addAttribute("toAccount", toAccount);
            // return "index2";
            return "index";
        }

        if (cErrors.size() > 0) {

            List<Account> accounts = transferSvc.getListOfAccounts();
            model.addAttribute("accounts", accounts);
            model.addAttribute("transfer", transfer);
            model.addAttribute("cErrors", cErrors);

            return "index";
        }

        logAudiSvc.saveToRedis(transfer);

        String msg = "Yur trnasfer of " + transfer.getAmount() + " from" + "\n"
                + transfer.getFromAccount() + " to" + "\n"
                + transfer.getToAccount() + " \n" + "is successful." + "\n\n\n"
                + "Transaction id is " + transfer.getTransactionId();

        model.addAttribute("msg", msg);

        return "completed";
    }
}
