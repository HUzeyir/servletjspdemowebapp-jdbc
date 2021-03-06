package com.mycompany.servlets;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.mycompany.bean.User;
import com.mycompany.context.Context;
import com.mycompany.daoInter.UserDaoInter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginUser", urlPatterns = {"/login"})
public class LoginUser extends HttpServlet {


    UserDaoInter udi = Context.instanceUserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        BCrypt.Verifyer verifyer = BCrypt.verifyer();
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = udi.serchUserByEmail(email);
        System.out.println("user? ->" + user);


        try {
            BCrypt.Result rs = verifyer.verify(password.toCharArray(), user.getPassword().toCharArray());

            if (rs.verified && user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("loggidIn", user);
                System.out.println("session created");
                resp.sendRedirect("user");
//            req.getRequestDispatcher("search.jsp").forward(req, resp);
            } else {
                System.out.println("login called");
                resp.sendRedirect("login");
            }

        } catch (Exception ex) {
            resp.sendRedirect("login");
        }


    }

}
