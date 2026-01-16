package com.parking.parkinglot1.servlets;

import com.parking.parkinglot1.common.UserDto;
import com.parking.parkinglot1.ejb.UsersBean;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Users", value = "/Users")
public class Users extends HttpServlet {

    @Inject
    UsersBean usersBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<UserDto> users = usersBean.findAllUsers();
        request.setAttribute("users", users);

        // TASK: Do not set the attribute with the names if the user doesn't have the usergroup
        if (request.isUserInRole("INVOICING")) {
            // Assuming you have a method in usersBean to get these names
            // If not, you can replace this with the appropriate logic
            List<String> invoicingNames = usersBean.findInvoicingNames();
            request.setAttribute("invoicingNames", invoicingNames);
        }

        request.getRequestDispatcher("/WEB-INF/pages/users.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TASK: Protect the doPost() method of the Users servlet
        if (!request.isUserInRole("INVOICING")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not authorized for invoicing.");
            return;
        }

        // Logic for invoicing (e.g., processing a payment or update) goes here

        response.sendRedirect(request.getContextPath() + "/Users");
    }
}