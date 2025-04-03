package com.order.servlet;

import com.order.ejb.OrderMessageProducer;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

public class OrderTestServlet extends HttpServlet {

    @EJB
    private OrderMessageProducer messageProducer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderIdParam = req.getParameter("orderId");
        String status = req.getParameter("status");

        if (orderIdParam == null || status == null) {
            resp.getWriter().write("Missing parameters: orderId or status");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);
            messageProducer.sendOrderMessage(orderId, status);
            resp.getWriter().write("Sent JMS message for orderId: " + orderId + ", status: " + status);
        } catch (NumberFormatException e) {
            resp.getWriter().write(" Invalid orderId: must be a number.");
        }
    }
}
