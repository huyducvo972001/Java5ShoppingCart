package com.poly.controller;

import com.poly.dao.UserDAO;
import com.poly.entity.User;
import com.poly.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/manager-user")
public class CrudUserController {
    @Autowired
    UserDAO dao;
    @Autowired
    SessionService sessionService;

    public void userEvent(Model model) {
        model.addAttribute("isUpdate", false);
        User user = new User();
        model.addAttribute("user", user);
        List<User> listUser = dao.findAll();
        model.addAttribute("listUser", listUser);
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

    @RequestMapping("/{tab}")
    public String user(@PathVariable("tab") String tab, Model model) {
        userEvent(model);
        switch (tab) {
            case "users":
                return "admin/users";
            case "user-action":
                return "admin/user-action";
        }
        return "admin/table";
    }

    @PostMapping("/user/create")
    public String create(@RequestParam(
            "attach") MultipartFile attach,
                         @Validated @ModelAttribute("user") User user,
                         BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("isUpdate", false);
            return "admin/user-action";
        } else {
            if(attach.getOriginalFilename().equals("")){
                user.setImage("noavatar.jpg");
            }else{
                saveImage(user, attach);
            }

            dao.save(user);
        }
        return "redirect:/manager-user/users";
    }

    @RequestMapping("/user/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        dao.deleteById(id);
        return "redirect:/manager-user/users";
    }

    @GetMapping("/set-form/{id}")
    public String edit(Model model, @PathVariable("id") Integer id) {
        User user = dao.findById(id).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("isUpdate", true);
        return "admin/user-action";
    }

    @RequestMapping("/user/update")
    public String update(@RequestParam(
            "attach") MultipartFile attach, @Validated User user, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("isUpdate", true);
            return "admin/user-action";
        } else {
            if (!attach.getOriginalFilename().equals("")) {
                saveImage(user, attach);
            }
            dao.save(user);
        }

        return "redirect:/manager-user/users";
    }
    @ModelAttribute("titleTab")
    public String titleTab(){
        return "Người dùng";
    }
    @ModelAttribute("user_nav")
    public User user() {
        return sessionService.getAttribute("user_Login");
    }
}
