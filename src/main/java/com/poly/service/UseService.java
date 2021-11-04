package com.poly.service;

import com.poly.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
public class UseService {
    @Autowired
    SessionService sessionService;


    public Boolean isLogin(Model model){
        User u = sessionService.getAttribute("user_Login");

        if(u != null){
            model.addAttribute("imageAvatar",u.getImage());
            return true;
        }
        model.addAttribute("imageAvatar","noavatar.jpg");
        return false;
    }

}
