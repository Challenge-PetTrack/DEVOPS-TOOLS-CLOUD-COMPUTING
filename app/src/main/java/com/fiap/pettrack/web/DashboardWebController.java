package com.fiap.pettrack.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardWebController {

    @GetMapping({"/", "/dashboard"})
    public String redirecionarDashboard(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if ("ROLE_ADMIN".equals(role) || "ROLE_VET".equals(role)) {
                return "redirect:/admin/dashboard";
            } else if ("ROLE_TUTOR".equals(role)) {
                return "redirect:/portal-tutor/dashboard";
            }
        }

        return "redirect:/login";
    }
}
