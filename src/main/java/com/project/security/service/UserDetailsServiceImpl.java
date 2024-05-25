package com.project.security.service;

import com.project.entity.concretes.user.User;
import com.project.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameEquals(username);

        if(user!= null){
            return new UserDetailsImpl(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    false,
                    user.getPassword(),
                    user.getUserRole().getRoleType().name(),//burda name() method olan yazilmali
                    user.getSsn());
        }
        throw new UsernameNotFoundException("User's "+username+" not found");
    }

}
