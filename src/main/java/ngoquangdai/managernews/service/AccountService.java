package ngoquangdai.managernews.service;

import ngoquangdai.managernews.entity.Account;
import ngoquangdai.managernews.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@Transactional
public class AccountService implements UserDetailsService {
    @Autowired
    AccountRepository accountRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        Account account = accountRepository.findAccountByUsername(username);

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (account.getRole() == 0){
            authorities.add(new SimpleGrantedAuthority("user"));
        } else if (account.getRole() == 1){
            authorities.add(new SimpleGrantedAuthority("admin"));
        } else {
            authorities.add(new SimpleGrantedAuthority("guest"));
        }
        return new User(account.getUsername(), account.getPassword(), authorities);
    }
}
