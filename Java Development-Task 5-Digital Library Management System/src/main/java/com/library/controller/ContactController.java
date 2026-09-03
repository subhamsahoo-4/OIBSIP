
package com.library.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.request.ContactRequest;
import com.library.dto.response.ContactResponse;
import com.library.service.AuthService;
import com.library.service.ContactService;

@RestController
@RequestMapping("/api")
public class ContactController {

    private final ContactService contacts;
    private final AuthService auth;

    public ContactController(
            ContactService contacts,
            AuthService auth
    ) {
        this.contacts = contacts;
        this.auth = auth;
    }

    @PostMapping("/user/contact")
    @PreAuthorize("hasRole('USER')")
    public ContactResponse create(
            @RequestBody ContactRequest r,
            Authentication a
    ) {
        return ContactResponse.from(
                contacts.create(
                        auth.getByEmail(a.getName()).getId(),
                        r.subject(),
                        r.message()
                )
        );
    }

    @GetMapping("/admin/contact")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ContactResponse> all() {
        return contacts
                .all()
                .stream()
                .map(ContactResponse::from)
                .toList();
    }
}

