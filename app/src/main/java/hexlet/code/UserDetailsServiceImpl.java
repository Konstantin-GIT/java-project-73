package hexlet.code;

import hexlet.code.exception.UserNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Получаем пользователя из репозитория
        User user = userRepository.findByEmail(username)
            .orElseThrow(()-> new UserNotFoundException("User not found"));

        String userRole = user.getRole().name();

        // Список полномочий, которые будут предоставлены пользователю после аутентификации
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userRole));

        // Формируем объект userdetails.User, передав email, пароль и роль пользователя
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(), user.getPassword(), authorities
        );

    }

}

