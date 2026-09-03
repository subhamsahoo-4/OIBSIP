
package com.library.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.library.entity.Member;
import com.library.repository.MemberRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedAdmin(
            MemberRepository repo,
            PasswordEncoder encoder
    ) {
        return args -> {
            if (repo.findByEmailIgnoreCase("admin@library.com").isEmpty()) {

                Member m = new Member();

                m.setName("Library Admin");
                m.setEmail("admin@library.com");
                m.setPassword(encoder.encode("admin123"));
                m.setRole(Member.Role.ADMIN);
                m.setActive(true);

                repo.save(m);
            }
        };
    }
}

