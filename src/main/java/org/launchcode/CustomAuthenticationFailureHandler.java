package org.launchcode;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
                                throws IOException, ServletException {
        String msg = exception.getMessage();
        request.setAttribute("message", msg);
        RequestDispatcher rd = request.getRequestDispatcher("user/login/error");
        rd.forward(request,response);
        //response.sendRedirect("/user/login/username?error=true");
    }
}
