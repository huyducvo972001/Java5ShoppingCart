package com.poly.service;

import com.poly.entity.Statistical;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticalService {


    public List<Statistical> fillToMonth(String year) {
        List<Statistical> list = new ArrayList<>();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=JAVA5ASM";
            Connection con = DriverManager.getConnection(url, "sa", "123456");
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select sum(quantity) , SUM" +
                    "(quantity*price), MONTH(purchase_date) from order_details, orders where " +
                    "order_details.id = orders.id and MONTH(purchase_date) " +
                    "between 1 and 12 and YEAR(purchase_date) = "+year+" group " +
                    "by" +
                    " MONTH(purchase_date)");
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
}
