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

import javax.mail.MessagingException;

@Controller
public class RegisterController {

    @Autowired
    MailerService mailer;

    @Autowired
    CartService cartService;

    @Autowired
    SessionService sessionService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    UseService useService;

    @RequestMapping("/home/register/confim-email")
    public String confirmEmail() {

        return "/home/confim-email";
    }

    @RequestMapping("/home/send/confim-email")
    public String sendConfirmEmail(@RequestParam("email") String email,
                                   Model model) {

        if (userDAO.findByEmail(email).isPresent()) {
            model.addAttribute("sameEmail", "Email của này đã được sử dụng vui" +
                    " lòng sử dụng email khác");
            return "/home/confim-email";
        }

        int code = (int) Math.floor(((Math.random() * 899999) + 100000));
        sessionService.setAttribute("otp", code);
        sessionService.setAttribute("emailToUseCreate", email);
        try {
            mailer.send(email, "OTP xác nhận email. Vui lòng không gửi cho " +
                            "bất kì ai!",
                    "Chúng tôi nhận được yêu cầu tạo tài khoản của bạn để mua" +
                            " sắm tại trang web thương mại điện tử COMUCA " +
                            "<br> Mã xác thực của bạn là:" + code);
            System.out.println("Send mail successfull" + email);

        } catch (MessagingException e) {
            return e.getMessage();
        }

        return "/home/confim-OTP";
    }

    @RequestMapping("/home/send/redirect-otp")
    public String redirectConfirmOTP(@RequestParam("otp") String otp,
                                     Model model) {

        if (otp.equalsIgnoreCase(sessionService.getAttribute("otp").toString())) {
            sessionService.remove("otp");
            User user = new User();
            user.setEmail(sessionService.getAttribute("emailToUseCreate").toString());
            model.addAttribute("user", user);
            return "/home/register";
        }
        model.addAttribute("sendFail", "Mã OTP không đúng vui lòng nhập lại " +
                "hoặc chọn email khác");
        return "/home/confim-OTP";
    }

    @RequestMapping("/home/register/create")
    public String creat(@Validated @ModelAttribute("user") User user,
                        BindingResult errors) {

        if (errors.hasErrors()) {
            System.out.println("do"+errors);
            return "/home/register";
        }
        user.setImage("noavatar.jpg");
//        user.setEmail(sessionService.getAttribute("emailToUseCreate").toString());
        userDAO.save(user);
        sessionService.remove("emailToUseCreate");
        return "redirect:/home/index";
    }
    @ModelAttribute("total")
    public Integer dsa(){
        return cartService.quantityProductInCart();
    }

    @ModelAttribute("isLogin")
    public Boolean getUseService(Model model) {
        return useService.isLogin(model);
    }
}
