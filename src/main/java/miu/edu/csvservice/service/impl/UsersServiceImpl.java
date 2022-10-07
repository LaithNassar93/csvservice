package miu.edu.csvservice.service.impl;

import miu.edu.csvservice.domain.Authentication;
import miu.edu.csvservice.repository.RoleRepository;
import miu.edu.csvservice.repository.UserRepository;
import miu.edu.csvservice.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServiceImpl implements UserService, UserDetailsService {

    private final UserRepository usersRepository;

    private final RoleRepository repository;

    private final PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UserRepository usersRepository, RoleRepository repository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authentication authentication = usersRepository
                .getUserByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username not Found")
                );
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authentication.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(authentication.getUsername(), authentication.getPassword(), authorities);
    }

    @Override
    public Authentication getByUserName(String username) {
        return usersRepository
                .getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
    }

    @Override
    public Authentication saveUser(Authentication authentication) {
        authentication.setPassword(passwordEncoder.encode(authentication.getPassword()));
        return usersRepository.save(authentication);
    }

}
