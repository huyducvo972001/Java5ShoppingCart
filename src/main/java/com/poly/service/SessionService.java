package com.poly.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class SessionService {
    @Autowired
    HttpSession session;

    public <T> T getAttribute(String name) {
        T sessionValue = (T) session.getAttribute(name);
        return sessionValue;
    }

    public void setAttribute(String name, Object value) {
        session.setAttribute(name, value);
    }

    public void remove(String name) {
        session.removeAttribute(name);
    }
}
