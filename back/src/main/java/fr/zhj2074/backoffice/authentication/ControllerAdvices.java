package fr.zhj2074.backoffice.authentication;

import fr.zhj2074.backoffice.AuthFilter;
import fr.zhj2074.backoffice.api.users.UserId;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerAdvices {

    @ModelAttribute("userId")
    public UserId extractUserId(HttpServletRequest request) {
        return (UserId) request.getAttribute(AuthFilter.USER_REQUEST_ATTRIBUTE);
    }
}
