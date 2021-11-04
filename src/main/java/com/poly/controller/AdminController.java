package com.poly.controller;

import com.poly.dao.*;
import com.poly.entity.*;
import com.poly.service.SessionService;
import com.poly.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    OrderDetailDAO orderDetailDAO;

    @Autowired
    HttpServletRequest request;

    @Autowired
    StatisticalService statisticalService;

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    SessionService sessionService;

    public List<Statistical> fillToYear() {
        List<Statistical> list = new ArrayList<>();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=JAVA5ASM";
            Connection con = DriverManager.getConnection(url, "sa", "123456");
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select sum(quantity),SUM" +
                    "(quantity*price), YEAR(purchase_date) from order_details, orders where " +
                    "order_details.id = orders.id group by YEAR(purchase_date)");
            while (rs.next()) {
                Statistical s = new Statistical();
                s.setTotalQuantity(Integer.parseInt(rs.getString(1)));
                s.setTotalMoney(Double.parseDouble(rs.getString(2)));
                s.setDate(rs.getString(3));
                list.add(s);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }


    @RequestMapping("/index")
    public String index(Model model) {
        String uri = request.getRequestURI();
        User user = sessionService.getAttribute("user_Login");
        System.out.println(user.getEmail());
        if (!(user.getRoles().stream().anyMatch(role -> role.getId() == 1)) && uri.startsWith("/admin/")) {
            System.out.println("do");
            return "redirect:/home/index";
        }
        return "redirect:/admin/statistical/2021";
    }

    @RequestMapping("/statistical/{year}")
    public String statistical(@PathVariable("year") String year, Model model){

        DecimalFormat df = new DecimalFormat("###");
        List<Statistical> statisticalList =
                statisticalService.fillToMonth(year);
        String data = "";
        String dataToTalMoney = "";
        for (Statistical s : statisticalList) {
            data += s.getDate()+" ";
            dataToTalMoney += df.format(s.getTotalMoney())+" ";
        }
        List<Statistical> statisticalYearList = fillToYear();
        String dataYear = "";
        String dataTotalYearMoney = "";
        for (Statistical s : statisticalYearList) {
            dataYear += s.getDate()+" ";
            dataTotalYearMoney += df.format(s.getTotalMoney())+" ";
        }
        int total_product = 0;
        double total_income = 0;
        for (Report r:orderDetailDAO.findThongKe() ){
            total_product += r.getQuantity();
            total_income += r.getPriceTotal();
        }
        model.addAttribute("titleTab","Thống kê");

        model.addAttribute("total_customer", orderDAO.findAllCustomerId().size());
        model.addAttribute("total_product",total_product);
        model.addAttribute("total_income",total_income);
        model.addAttribute("statisticalYearList",statisticalYearList);
        model.addAttribute("data",data.trim());
        model.addAttribute("dataToTalMoney",dataToTalMoney.trim());
        model.addAttribute("dataYear",dataYear.trim());
        model.addAttribute("dataTotalYearMoney",dataTotalYearMoney.trim());
        return "/admin/table";
    }

    @RequestMapping("/invoice")
    public String invoice(Model model){
        model.addAttribute("titleTab","Hoá đơn");
        model.addAttribute("invoice",orderDetailDAO.findThongKess());
        System.out.println(orderDetailDAO.findThongKess());
        return "/admin/invoice";
    }
    @ModelAttribute("user_nav")
    public User user() {
        return sessionService.getAttribute("user_Login");
    }
}
