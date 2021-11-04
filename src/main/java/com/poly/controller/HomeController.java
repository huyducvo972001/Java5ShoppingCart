package com.poly.controller;

import com.poly.dao.ProductDAO;
import com.poly.entity.Product;
import com.poly.entity.User;
import com.poly.service.CartService;

import com.poly.service.SessionService;
import com.poly.service.UseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    ProductDAO dao;

    @Autowired
    HttpServletRequest request;

    @Autowired
    CartService cartService;

    @Autowired
    SessionService sessionService;

    @Autowired
    UseService useService;

    @RequestMapping("/home/index")
    public String home(Model model, Product product) {

        List<Product> list5Product = dao.find5Product();
        model.addAttribute("list5Product", list5Product);

        List<Product> list5SmartPhone = dao.find5SmartPhone();
        model.addAttribute("list5SmartPhone", list5SmartPhone);

        List<Product> list5Laptop = dao.find5Laptop();
        model.addAttribute("list5Laptop", list5Laptop);

        List<Product> list5Accessory = dao.find5Accessory();
        model.addAttribute("list5Accessory", list5Accessory);

        return "/home/index";
    }

    public void addActive(Model model) {
        boolean isDT = false;
        boolean isTL = false;
        boolean isLT = false;
        boolean isPC = false;
        boolean isMH = false;
        boolean isDH = false;
        boolean isTN = false;
        boolean isPK = false;
        boolean isL = false;
        String uri = request.getRequestURI();
        int cid = Integer.parseInt(uri.substring(uri.lastIndexOf("/") + 1));
        switch (cid) {
            case 2:
                isDT = true;
                break;
            case 10:
                isTL = true;
                break;
            case 1:
                isLT = true;
                break;
            case 5:
                isPC = true;
                break;
            case 6:
                isL = true;
                break;
            case 9:
                isMH = true;
                break;
            case 4:
                isDH = true;
                break;
            case 8:
                isTN = true;
                break;
            case 3:
                isPK = true;
                break;
        }

        model.addAttribute("isDT", isDT);
        model.addAttribute("isTL", isTL);
        model.addAttribute("isLT", isLT);
        model.addAttribute("isPC", isPC);
        model.addAttribute("isMH", isMH);
        model.addAttribute("isDH", isDH);
        model.addAttribute("isTN", isTN);
        model.addAttribute("isPK", isPK);
        model.addAttribute("isL", isL);
    }

    public void selectLogo(String category_id, Model model) {
        List<Product> listIconLogos = dao.fillProductOfCategory(category_id);
        List<Product> listIconLogo = new ArrayList<>();
        String imageName = "";
        for (Product prod : listIconLogos) {
            if (imageName.equals(prod.getSupplier().getImage())) {
                continue;
            }
            imageName = prod.getSupplier().getImage();
            listIconLogo.add(prod);
        }
        model.addAttribute("listIconLogo", listIconLogo);
    }

    @RequestMapping("/home/product/{category_id}")
    public String product(@PathVariable("category_id") String category_id,
                          Model model, @RequestParam("p") Optional<Integer> p
            , @RequestParam String id) {
        addActive(model);
        Pageable pageable = PageRequest.of(p.orElse(0), 20);
        Page<Product> pageCategory = dao.fillByCategory(category_id, pageable);
        if (Integer.parseInt(id) > -1) {
            pageCategory = dao.findBandCategory(category_id, id, pageable);
        } else {
            switch (id) {
                case "-2":
                    pageCategory = dao.SortByDecrease(category_id, pageable);
                    break;
                case "-3":
                    pageCategory = dao.SortByAscending(category_id, pageable);
                    break;
                case "-4":
                    pageCategory = dao.SortByDateDecrease(category_id, pageable);
                    break;
                case "-5":
                    pageCategory = dao.SortByDateAscending(category_id, pageable);
                    break;
            }
        }
        selectLogo(category_id, model);
        model.addAttribute("pageCategory", pageCategory);
        return "/home/products";
    }

    @RequestMapping("/home/product-detail/{product_id}")
    public String productDetails(Model model,
                                 @PathVariable("product_id") String product_id) {
        Optional<Product> productDetails = dao.findById(Integer.parseInt(product_id));
        model.addAttribute("productDetails", productDetails.orElse(new Product()));
        List<Product> list5Product = dao.find5Product();
        model.addAttribute("list5Product", list5Product);
        return "/home/product-detail";
    }

    @RequestMapping("/home/result-search")
    public String searchProduct(@RequestParam("keyword_search") String keyword,
                                Model model) {

        List<Product> productList = dao.findByName(keyword);
        for (Product p : productList) {
            System.out.println(p.toString());
        }
        model.addAttribute("productList", productList);
        return "/home/search-product";
    }

    @ModelAttribute("total")
    public Integer dsa() {
        return cartService.quantityProductInCart();
    }

    @ModelAttribute("isLogin")
    public Boolean getUseService(Model model) {
        return useService.isLogin(model);
    }

    @ModelAttribute("isAdmin")
    public Boolean checkAdmin() {


        try {
            User u = sessionService.getAttribute("user_Login");

            if (u.getRoles().stream().anyMatch(role -> role.getId() == 1)) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("");
        }
        return false;
    }

}
