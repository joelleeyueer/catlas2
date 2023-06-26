package nus.iss.server.Components;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import nus.iss.server.Model.UserInfo;
import nus.iss.server.Model.UseridPassword;
import nus.iss.server.Repositories.UserInfoRepository;



@Component
public class UserInfoUserDetailsComponent implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = repository.findByName(username);
        return userInfo.map(UseridPassword::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));

    }
}
