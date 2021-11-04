package com.poly.controller;

import com.poly.dao.CategoryDAO;
import com.poly.entity.Category;
import com.poly.entity.Product;
import com.poly.entity.User;
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

import java.util.Optional;

@Controller
public class CrudCategoryController {

    @Autowired
    SessionService sessionService;

    @Autowired
    CategoryDAO dao;

    @RequestMapping("/admin/categories")
    public String category(Model model,
                           @RequestParam("p") Optional<Integer> p) {
        Category category = new Category();
        model.addAttribute("category", category);
        Pageable pageable = PageRequest.of(p.orElse(0), 5);

        Page<Category> pageCategories = dao.findAll(pageable);
        model.addAttribute("pageCategories", pageCategories);
        return "admin/categories";
    }

    @PostMapping("/admin/category/create")
    public String create(@ModelAttribute("category") Category category,
                         Model model) {
        dao.save(category);
        return "redirect:/admin/categories";
    }
    @RequestMapping("/admin/category/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        dao.deleteById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/category/edit/{id}")
    public String edit(Model model,@PathVariable("id") Integer id, @RequestParam("p") Optional<Integer> p) {
        Category category = dao.findById(id).orElse(null);
        model.addAttribute("category", category);
        System.out.println(category.toString());
        Pageable pageable = PageRequest.of(p.orElse(0), 5);

        Page<Category> pageCategories = dao.findAll(pageable);
        model.addAttribute("pageCategories", pageCategories);
        return "admin/categories";
    }

    @ModelAttribute("titleTab")
    public String titleTab() {
        return "Danh mục";
    }

    @ModelAttribute("user_nav")
    public User user() {
        return sessionService.getAttribute("user_Login");
    }
}
