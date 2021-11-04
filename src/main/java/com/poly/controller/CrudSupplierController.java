package com.poly.controller;


import com.poly.dao.SupplierDAO;

import com.poly.entity.Supplier;
import com.poly.entity.User;
import com.poly.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
public class CrudSupplierController {

    @Autowired
    SessionService sessionService;

    @Autowired
    SupplierDAO dao;

    public void saveImage(Supplier supplier, @RequestParam(
            "attach") MultipartFile attach) {
        try {
            if (!attach.isEmpty()) {
                File file = new File(System.getProperty("user" +
                        ".dir") + "\\src\\main\\resources\\static\\home" +
                        "\\image\\logo_band\\" +
                        attach.getOriginalFilename());
                attach.transferTo(file);

                supplier.setImage(attach.getOriginalFilename());
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @RequestMapping("/admin/suppliers")
    public String supplier(Model model,
                           @RequestParam("p") Optional<Integer> p) {
        Supplier supplier = new Supplier();
        model.addAttribute("supplier", supplier);
        Pageable pageable = PageRequest.of(p.orElse(0), 5);

        Page<Supplier> pageSupplier = dao.findAll(pageable);
        model.addAttribute("pageSupplier", pageSupplier);
        return "admin/suppliers";
    }

    @PostMapping("/admin/suppliers/create")
    public String create(@RequestParam(
            "attach") MultipartFile attach,
                         @ModelAttribute("supplier") Supplier supplier,
                         Model model) {
        saveImage(supplier, attach);
        dao.save(supplier);
        return "redirect:/admin/suppliers";
    }

    @RequestMapping("/admin/suppliers/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        dao.deleteById(id);
        return "redirect:/admin/suppliers";
    }

    @GetMapping("/admin/suppliers/edit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id, @RequestParam("p") Optional<Integer> p) {
        Supplier supplier = dao.findById(id).orElse(null);
        model.addAttribute("supplier", supplier);
        Pageable pageable = PageRequest.of(p.orElse(0), 5);

        Page<Supplier> pageSupplier = dao.findAll(pageable);
        model.addAttribute("pageSupplier", pageSupplier);
        return "admin/suppliers";
    }

    @ModelAttribute("titleTab")
    public String titleTab() {
        return "Hãng sản xuất";
    }

    @ModelAttribute("user_nav")
    public User user() {
        return sessionService.getAttribute("user_Login");
    }
}
