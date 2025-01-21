package org.example.adds.Security;

import lombok.RequiredArgsConstructor;
import org.example.adds.Users.Users;
import org.example.adds.Users.UsersRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        final Users user = usersRepo.findByPhone(phone).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UsersDetails(user);
    }
}
