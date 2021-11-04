//package com.poly.service;
//
//import com.poly.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Service
//public class AuthInterceptor implements HandlerInterceptor {
//    @Autowired
//    SessionService session;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String uri = request.getRequestURI();
//        User user = session.getAttribute("user_Login"); // lấy từ session
//
//        try {
//            boolean isStaff =
//                    user.getRoles().stream().anyMatch(role -> role.getId() == 1);
//
//            System.out.println(user.getImage());
//            if (!isStaff && uri.startsWith("/admin/")) {
//                response.sendRedirect("/home/index");
//                return false;
//            }
//        } catch (Exception e) {
//            System.out.println("Chưa đăng nhập!");
//        }
//
//        return true;
//    }
//
//}
