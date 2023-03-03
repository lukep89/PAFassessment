package ibf2022.PAFassessment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import ibf2022.PAFassessment.model.Account;
import ibf2022.PAFassessment.model.Transfer;
import ibf2022.PAFassessment.service.TransferService;

@Controller
public class TransferController {

    @Autowired
    private TransferService transferService;

    @GetMapping(path = "/")
    public String showIndex(Model model) {

        List<Account> accounts = transferService.getListOfAccounts();

        // List<String> forNameDropList = new ArrayList<>();
        // List<String> forValueDropList = new ArrayList<>();
        // List<String> forTextDropList = new ArrayList<>();

        // for (Account acc : accounts) {
        // String name = acc.getName() + " (" + acc.getAccountId() + ")";
        // forNameDropList.add(name);

        // forTextDropList.add(acc.getName());

        // forValueDropList.add(acc.getAccountId());
        // }

        System.out.println("========= @1 controller accounts : " + accounts + "\n\n");
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
    public String postTransfer(Transfer transfer, Model model) {

        System.out.println("======== @2 POST transfer: " + transfer.toString());
        return "completed";
    }
}
