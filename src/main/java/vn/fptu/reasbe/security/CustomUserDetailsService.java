package vn.fptu.reasbe.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.repository.UserRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userNameOrEmailOrPhone) throws UsernameNotFoundException {
        //allow user to log in by username or email
        User user = userRepository.findByUserNameOrEmailOrPhone(userNameOrEmailOrPhone, userNameOrEmailOrPhone, userNameOrEmailOrPhone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + userNameOrEmailOrPhone));

        GrantedAuthority authority = new SimpleGrantedAuthority(String.valueOf(user.getRole().getName()));

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(String.valueOf(authority)));

        return new org.springframework.security.core.userdetails.User(userNameOrEmailOrPhone, user.getPassword(), authorities);
    }
}
