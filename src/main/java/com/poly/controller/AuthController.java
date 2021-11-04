package com.poly.controller;

import com.poly.dao.UserDAO;
import com.poly.entity.User;
import com.poly.service.CartService;
import com.poly.service.MailerService;
import com.poly.service.SessionService;
import com.poly.service.UseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    SessionService sessionService;

    @Autowired
    CartService cartService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    UseService useService;

    @Autowired
    MailerService mailer;


    @RequestMapping("/home/login")
    public String login() {
        return "/home/login";
    }

    @RequestMapping("/home/logout")
    public String logout() {
        sessionService.remove("user_Login");
        sessionService.remove("total");
        cartService.clear();
        return "redirect:/home/index";
    }

    @RequestMapping("/home/action/login")
    public String actionLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Model model) {
        Optional<User> users = userDAO.findByUsername(username);

        if (users.isPresent()) {
            if (users.orElse(null).getPassword().equals(password)) {
                sessionService.setAttribute("user_Login", users.orElse(null));
                if (users.orElse(null).getRoles().stream().anyMatch(role -> role.getId() == 1)) {
                    return "redirect:/admin/statistical/2021";
                }
            }

        } else {
            model.addAttribute("hasErrorLogin", "Đăng nhập không thành công!");
            return "/home/login";
        }
        return "redirect:/home/index";
    }

    @ModelAttribute("total")
    public Integer dsa() {
        return cartService.quantityProductInCart();
    }

    @ModelAttribute("isLogin")
    public Boolean getUseService(Model model) {
        return useService.isLogin(model);
    }

    @RequestMapping("/home/acount-management")
    public String managementUser(Model model) {
        User user = sessionService.getAttribute("user_Login");
        model.addAttribute("user", user);
        return "/home/account-management.html";
    }

    public void saveImage(User user, @RequestParam(
            "attach") MultipartFile attach) {
        try {
            if (!attach.isEmpty()) {
                File file = new File(System.getProperty("user" +
                        ".dir") + "\\src\\main\\resources\\static\\admin" +
                        "\\image\\" + attach.getOriginalFilename());
                attach.transferTo(file);

                user.setImage(attach.getOriginalFilename());
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @RequestMapping("/home/acount-management/update")
    public String updateUser(@RequestParam("attach") MultipartFile attach, @Validated User user, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("message", "Thông tin nhập không đúng. Vui " +
                    "lòng kiểm tra lại!");
            return "/home/account-management.html";
        } else {
            if (!attach.getOriginalFilename().equals("")) {
                saveImage(user, attach);
            }
            userDAO.save(user);
            sessionService.setAttribute("user_Login", user);
        }
        return "redirect:/home/acount-management";
    }

    @RequestMapping("/home/acount/forgot-password")
    public String forgotPass() {
        return "/home/confirm-username";
    }

    @RequestMapping("/home/acount/confirm-username")
    public String confirmUsername(@RequestParam("username") String username,
                                  Model model) {
        Optional<User> user = userDAO.findByUsername(username);
        sessionService.setAttribute("userForgot", user);
        if (!userDAO.findByUsername(username).isPresent()) {
            model.addAttribute("errorUser", "Tên đăng nhập không hợp lệ. Vui " +
                    "lòng " +
                    "kiểm " +
                    "tra lại!");
        } else {
            int code = (int) Math.floor(((Math.random() * 899999) + 100000));
            sessionService.setAttribute("otpUsername", code);
            try {
                mailer.send(user.orElse(null).getEmail(), "OTP xác nhận email" +
                                ". Vui lòng" +
                                " " +
                                "không gửi" +
                                " cho" +
                                " " +
                                "bất kì ai!",
                        "Chúng tôi nhận được yêu cầu lấy lại mật khẩu của bạn" +
                                " để " +
                                "mua" +
                                " sắm tại trang web thương mại điện tử COMUCA " +
                                "<br> Mã xác thực của bạn là:" + code);


            } catch (MessagingException e) {
                return e.getMessage();
            }
            return "/home/confim-OTP-username";
        }

        return "/home/confirm-username";
    }

    @RequestMapping("/home/confirm-otp-password")
    public String confirmOTP(@RequestParam("otpUsername") String otp) {

        if (sessionService.getAttribute("otpUsername").toString().equals(otp)) {
            return "/home/change-for-password";
        }
        return "/home/confim-OTP-username";
    }

    @RequestMapping("/home/change-password")
    public String changePass(@RequestParam("password") String password,
                             @RequestParam("confirm-password") String confirmPassword, Model model) {
        if (password.equalsIgnoreCase(confirmPassword)) {
            Optional<User> user = sessionService.getAttribute("userForgot");
            user.orElse(null).setPassword(password);
            userDAO.save(user.orElse(null));

            sessionService.remove("userForgot");
            sessionService.remove("otpUsername");
            return "redirect:/home/index";
        }
        model.addAttribute("sendFail", "Vui lòng kiểm tra lại thông tin!");
        return "/home/change-for-password";
    }
}
