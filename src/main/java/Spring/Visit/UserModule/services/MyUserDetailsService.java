package Spring.Visit.UserModule.services;

import Spring.Visit.UserModule.entities.User;
import Spring.Visit.UserModule.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Attempting to load user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->  {
                    logger.error("User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });
        logger.info("Successfully loaded user with email: {}", email);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities());
    }
}
