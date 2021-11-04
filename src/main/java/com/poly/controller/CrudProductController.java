package com.poly.controller;

import com.poly.dao.*;
import com.poly.entity.*;


import com.poly.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/manager-product")
public class CrudProductController {

    @Autowired
    ProductDAO dao;

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    SupplierDAO supplierDAO;

    @Autowired
    SessionService sessionService;

    @Autowired
    OriginDAO originDAO;

    @Autowired
    SessionService session;

    public void exportOptions(Model model) {
        List<Category> categoryList = categoryDAO.findAll();
        model.addAttribute("categoryList", categoryList);

        List<Origin> originList = originDAO.findAll();
        model.addAttribute("originList", originList);

        List<Supplier> supplierList = supplierDAO.findAll();
        model.addAttribute("supplierList", supplierList);
    }

    public void productEvent(Model model, @RequestParam("keywords") Optional<String> kw, @RequestParam("p") Optional<Integer> p) {
        model.addAttribute("isUpdate", false);
        Product product = new Product();
        model.addAttribute("product", product);
        String kwords = " ";
        if (kw.orElse(session.getAttribute("keywords")) != null) {
            kwords = kw.orElse(session.getAttribute("keywords"));
        } else {
            kwords = " ";
        }

        session.setAttribute("keywords", kwords);
        Pageable pageable = PageRequest.of(p.orElse(0), 10);

        Page<Product> pageProducts = dao.findByKeywords(kwords, pageable);
        model.addAttribute("pageProducts", pageProducts);


        exportOptions(model);
    }

    public void saveImage(Product product, @RequestParam(
            "attach") MultipartFile attach) {
        try {
            if (!attach.isEmpty()) {
                File file = new File(System.getProperty("user" +
                        ".dir") + "\\src\\main\\resources\\static\\img\\"+
                        attach.getOriginalFilename());
                attach.transferTo(file);

                product.setImage(attach.getOriginalFilename());
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @RequestMapping("/{tab}")
    public String product(@PathVariable("tab") String tab, Model model, @RequestParam("keywords") Optional<String> kw,
                          @RequestParam("p") Optional<Integer> p) {
        productEvent(model, kw, p);

        switch (tab) {
            case "products":
                return "admin/products";
            case "product-action":
                return "admin/product-action";
        }
        return "admin/table";
    }

    @PostMapping("/product/create")
    public String create(@RequestParam(
            "attach") MultipartFile attach,
                         @Validated @ModelAttribute("product") Product product,
                         BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            exportOptions(model);
            model.addAttribute("isUpdate", false);

            return "admin/product-action";
        } else {
            if (attach.getOriginalFilename().equals("")) {
                product.setImage("noavatar.jpg");
            } else {
                saveImage(product, attach);
            }

            dao.save(product);
        }

        return "redirect:/manager-product/products";
    }

    @RequestMapping("/product/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        dao.deleteById(id);
        return "redirect:/manager-product/products";
    }

    @GetMapping("/set-form/{id}")
    public String edit(Model model, @PathVariable("id") Integer id, @RequestParam("keywords") Optional<String> kw,
                       @RequestParam("p") Optional<Integer> p) {
//        productEvent(model, p);
        exportOptions(model);
        Product product = dao.findById(id).orElse(null);
        model.addAttribute("product", product);
        model.addAttribute("isUpdate", true);
        return "admin/product-action";
    }

    @RequestMapping("/product/update")
    public String update(@RequestParam(
            "attach") MultipartFile attach, @Validated Product product,
                         BindingResult errors, Model model) {
        exportOptions(model);
        if (errors.hasErrors()) {
            model.addAttribute("isUpdate", true);
            return "admin/product-action";
        } else {

            if (!attach.getOriginalFilename().equals("")) {
                saveImage(product, attach);
            }
            dao.save(product);
        }

        return "redirect:/manager-product/products";
    }

    @ModelAttribute("titleTab")
    public String titleTab() {
        return "Sản phẩm";
    }

    @ModelAttribute("user_nav")
    public User user() {
        return sessionService.getAttribute("user_Login");
    }
}
