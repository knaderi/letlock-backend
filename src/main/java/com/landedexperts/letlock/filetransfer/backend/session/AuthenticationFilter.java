package com.landedexperts.letlock.filetransfer.backend.session;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.response.ReturnCodeMessageResponse;

@Component
public class AuthenticationFilter extends HttpFilter {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        List<String> openEndpoints = AuthenticationManager.getInstance().getOpenEndpoints();
        List<String> adminEndpoints = AuthenticationManager.getInstance().getAdminEndpoints();
        if (!openEndpoints.contains(req.getRequestURI())) {
            // Authenticate
            long userId = SessionManager.getInstance().getUserId(getToken(req));
            if (userId == -1) {
                ReturnCodeMessageResponse response = new ReturnCodeMessageResponse("TOKEN_INVALID", "Invalid token");
                res.setContentType("application/JSON;charset=UTF-8");
                // res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
                res.setStatus(HttpServletResponse.SC_OK); //200
                res.getWriter().print(new ObjectMapper().writeValueAsString(response));
                return;
            }
            
            //Authorize for admin role
            if (adminEndpoints.contains(req.getServletPath()) && !(userId == 1)) {// TODO: check for admin role later
                ReturnCodeMessageResponse response = new ReturnCodeMessageResponse("ADMIN_USER_EXPECTED", "Admin user is required to perform this action");
                res.setContentType("application/JSON;charset=UTF-8");
                // res.setStatus(HttpServletResponse.SC_FORBIDDEN); //403
                res.setStatus(HttpServletResponse.SC_OK); //200
                res.getWriter().print(new ObjectMapper().writeValueAsString(response));
                return;
            }
            
            req.setAttribute("user.id", userId);
        }
        
        chain.doFilter(req, res);
    }
    
    String getToken (HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return token == null ? "" : token.replace("Bearer ", "");
    }

    
}
