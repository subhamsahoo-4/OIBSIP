package com.library.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.library.entity.Member;
import com.library.repository.MemberRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class MemberStatusFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;

    public MemberStatusFilter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {

            String email = authentication.getName();

            Member member = memberRepository
                    .findByEmailIgnoreCase(email)
                    .orElse(null);

            if (member == null || !member.isActive()) {

                SecurityContext context =
                        SecurityContextHolder.createEmptyContext();

                SecurityContextHolder.setContext(context);

                HttpSession session = request.getSession(false);

                if (session != null) {
                    session.invalidate();
                }

                if (request.getRequestURI().startsWith("/api/")) {
                    response.setStatus(
                            HttpServletResponse.SC_UNAUTHORIZED
                    );
                    return;
                }

                response.sendRedirect("/login.html");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}