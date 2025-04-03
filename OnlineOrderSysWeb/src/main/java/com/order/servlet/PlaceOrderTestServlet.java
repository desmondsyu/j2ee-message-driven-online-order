package com.order.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import com.order.ejb.OrderEntity;
import com.order.ejb.OrderService;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Date;

public class PlaceOrderTestServlet extends HttpServlet {

    @EJB
    private OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer customerId = Integer.parseInt(req.getParameter("customerId"));
            Integer productId = Integer.parseInt(req.getParameter("productId"));
            Integer quantity = Integer.parseInt(req.getParameter("quantity"));
            Double totalAmount = Double.parseDouble(req.getParameter("totalAmount"));

            OrderEntity order = new OrderEntity();
            order.setCustomerId(customerId);
            order.setProductId(productId);
            order.setQuantity(quantity);
            order.setTotalAmount(totalAmount);
            order.setOrderDate(new Date());
            order.setStatus("Pending");

            orderService.saveOrder(order);

            resp.getWriter().write("Order placed successfully with status: Pending");
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("Failed to place order: " + e.getMessage());
        }
    }
}
