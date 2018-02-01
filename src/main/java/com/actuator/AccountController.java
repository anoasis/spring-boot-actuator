package com.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @RequestMapping(value = "/lists",method = RequestMethod.GET)
    public List<Account> getAccounts(){
        return accountRepository.findAll();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public  Account getAccountById(@PathVariable("id") Long id){
        return accountRepository.findOne(id);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public  ResponseEntity<Account> updateAccount(@PathVariable("id")Long id , @Valid @RequestBody Account account){
        Account a = accountRepository.findOne(id);
        if(a == null) {
            return ResponseEntity.notFound().build();
        }
        a.setName(account.getName());
        a.setMoney(account.getMoney());

        Account updated = accountRepository.save(account);
        return ResponseEntity.ok(updated);
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public Account postAccount( @Valid @RequestBody Account account){
        return accountRepository.save(account);
    }

}