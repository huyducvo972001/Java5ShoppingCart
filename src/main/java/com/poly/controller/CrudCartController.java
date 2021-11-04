package com.poly.controller;

import com.poly.dao.ProductDAO;
import com.poly.entity.Description;
import com.poly.entity.OrderDetail;
import com.poly.entity.Product;
import com.poly.entity.User;
import com.poly.service.CartService;
import com.poly.service.SessionService;
import com.poly.service.UseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CrudCartController {

    @Autowired
    CartService cartService;

    @Autowired
    ProductDAO productDAO;

    @Autowired
    SessionService sessionService;

    @Autowired
    UseService useService;

    @RequestMapping("/add-to-cart/{product_id}")
    public String addToCart(@PathVariable("product_id") String product_id,
                            Model model, @RequestParam Integer id){
        User u = sessionService.getAttribute("user_Login");

        if(u == null){
            return "redirect:/home/login";
        }

        Product product = productDAO.findById(Integer.parseInt(product_id)).orElse(null);
        product.setDescriptions(new ArrayList<>());
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setQuantity(1);
        cartService.add(orderDetail);
        switch (id){
            case 1:
                return "redirect:/home/product-detail/"+product_id;
            case 2:
                return "redirect:/home/product/"+product.getCategory().getId()+
                        "?id=-1";
            default:
                return "redirect:/home/index";
        }

    }

    @RequestMapping("/remove-to-cart/{product_id}")
    public String removeToCart(@PathVariable("product_id") String product_id){
        cartService.remove(Integer.parseInt(product_id));
        return "redirect:/home/cart";
    }

    @RequestMapping("/update-to-cart/{product_id}")
    public String updateToCart(@PathVariable("product_id") String product_id,
                               @RequestParam("qty") Integer qty){
        cartService.update(Integer.parseInt(product_id), qty);
        return "redirect:/home/cart";
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
