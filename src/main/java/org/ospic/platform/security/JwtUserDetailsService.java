package org.ospic.platform.security;


import org.ospic.platform.tenant.app.security.services.UserDetailsImpl;
import org.ospic.platform.tenant.app.users.domain.User;
import org.ospic.platform.tenant.app.users.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Md. Amran Hossain
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(user);
    }
}
