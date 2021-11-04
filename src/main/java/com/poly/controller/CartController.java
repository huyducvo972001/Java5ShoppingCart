package com.poly.controller;

import com.poly.dao.DeliveryInfoDAO;
import com.poly.dao.OrderDAO;
import com.poly.dao.OrderDetailDAO;
import com.poly.entity.DeliveryInfo;
import com.poly.entity.Order;
import com.poly.entity.OrderDetail;
import com.poly.entity.User;
import com.poly.service.CartService;
import com.poly.service.MailerService;
import com.poly.service.SessionService;
import com.poly.service.UseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    MailerService mailer;

    @Autowired
    SessionService sessionService;

    @Autowired
    DeliveryInfoDAO deliveryInfoDAO;

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    OrderDetailDAO orderDetailDAO;

    @Autowired
    UseService useService;

    @RequestMapping("/home/cart")
    public String cart(Model model) {
        Collection<OrderDetail> listOrder = cartService.getItems();

        double total = 0;
        for(OrderDetail o: listOrder){
            total += o.getQuantity()*(o.getProduct().getPrice()-o.getProduct().getDiscount());
        }
        DecimalFormat df = new DecimalFormat("###,###,###");
        model.addAttribute("totalMoney",df.format(total));
        model.addAttribute("totalMoneyShip",df.format(total+80000));
        model.addAttribute("listOrder", listOrder);
        return "/home/cart";
    }

    @RequestMapping("/home/cart/pay")
    public String pay(Model model) {
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        model.addAttribute("deliveryInfo", deliveryInfo);
        return "/home/delivery";
    }

    @RequestMapping("/home/cart/order")
    public String order(@ModelAttribute("deliveryInfo") DeliveryInfo deliveryInfo) {
        DecimalFormat df = new DecimalFormat("###,###,###");
        User u = sessionService.getAttribute("user_Login");
        deliveryInfo.setEmail(u.getEmail());
        deliveryInfoDAO.save(deliveryInfo);

        Order order = new Order();
        order.setCustomer(u);
        order.setNote("");
        order.setDeliveryInfo(deliveryInfo);
        order.setPurchaseDate(new Date());
        orderDAO.save(order);

        Collection<OrderDetail> orderList = cartService.getItems();

        String mailCart = "";
        int i=1;
        double totalMoney = 0;
        for (OrderDetail od : orderList) {

            mailCart += "<tr>\n" +
                    "                  <th scope=\"row\" style=\"border: 1px solid rgb(173, 171, 171); padding: 10px;\">"+(i++)+"</th>\n" +
                    "                  <td style=\"border: 1px solid rgb(173, 171, 171); padding: 10px;\">"+od.getProduct().getName()+"</td" +
                    ">\n" +
                    "                  <td style=\"border: 1px solid rgb(173, 171, 171); padding: 10px;\">"+od.getQuantity()+"</td>\n" +
                    "                  <td style=\"border: 1px solid rgb(173," +
                    " 171, 171); padding: 10px;\">"+df.format((od.getProduct().getPrice()-od.getProduct().getDiscount())*od.getQuantity())+"</td>\n" +
                    "                </tr>";
            totalMoney += (od.getProduct().getPrice()-od.getProduct().getDiscount())*od.getQuantity();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(od.getProduct());
            orderDetail.setQuantity(od.getQuantity());
            orderDetail.setPrice(od.getProduct().getPrice());
            orderDetailDAO.save(orderDetail);
        }

        try {
            mailer.send(u.getEmail(), "Xác nhận đơn đặt hàng", "Chúng tôi đã " +
                    "nhận được đơn hàng của bạn, đơn hàng của bạn gồm:" +
                    " " +
                    "<br> " +
                    "<table " +
                    "style=\"width: 100%;\">"+"<tr >\n" +
                    "                  <th scope=\"row\" style=\"border: 1px solid rgb(173, 171, 171); padding: 10px;\">#</th>\n" +
                    "                  <td style=\"border: 1px solid rgb(173, 171, 171); padding: 10px;\">Sản phẩm</td>\n" +
                    "                  <td style=\"border: 1px solid rgb(173, 171, 171); padding: 10px;\">Số lượng</td>\n" +
                    "                  <td style=\"border: 1px solid rgb(173, 171, 171); padding: 10px;\">Giá</td>\n" +
                    "                \n" +
                    "                </tr>  "+mailCart+"</table> <br> " +
                    "Tổng " +
                    "tiền cần thanh toán là: "+df.format(totalMoney+80000));

        } catch (MessagingException e) {
            return e.getMessage();
        }
        cartService.clear();
        System.out.println(cartService.getItems());
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


}
