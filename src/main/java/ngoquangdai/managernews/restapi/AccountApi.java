package ngoquangdai.managernews.restapi;

import ngoquangdai.managernews.entity.Account;
import ngoquangdai.managernews.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountApi {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @RequestMapping(path = "login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody Account account){

        Account existingAccount = accountRepository.findAccountByUsername(account.getUsername());
        if (existingAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login fails");
        }
        boolean result = passwordEncoder.matches(account.getPassword(),
                existingAccount.getPassword());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(path = "register", method = RequestMethod.POST)
    public Account register(@RequestBody Account account){
        account.setPassword((passwordEncoder.encode(account.getPassword())));
        accountRepository.save(account);
        return account;
    }
}
