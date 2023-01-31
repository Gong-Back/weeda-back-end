package gongback.weeda.service;

import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.domain.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final RoleService roleService;

    private final UserRepository userRepository;

    public CustomUserDetailsService(RoleService roleService, UserRepository userRepository) {
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(final String username) {
        return userRepository.findByEmail(username)
                .switchIfEmpty(Mono.error(new WeedaApplicationException(ResponseCode.NOT_FOUND)))
                .flatMap(user -> roleService.findRoleNamesByUserId(user.getId()).map(SimpleGrantedAuthority::new).collectList())
                .map(userRoles -> new User(username, "", userRoles));
    }
}
