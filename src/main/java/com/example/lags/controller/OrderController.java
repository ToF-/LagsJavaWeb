package com.example.lags.controller;

import com.example.lags.form.CustomerForm;
import com.example.lags.form.OrderForm;
import com.example.lags.model.Order;
import com.example.lags.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@SuppressWarnings("ALL")
@Controller
public class OrderController {
    @Autowired
    private Repository repository;
    @GetMapping("/orderCreate/{id}")
    public String getOrderCreate(@PathVariable("id") String customerId, Model model) {
        OrderForm orderForm = new OrderForm("", customerId, LocalDate.now(), 1,0);
        model.addAttribute("orderForm",orderForm);
        System.out.println(orderForm.toString());
        return "/orderCreate";
    }
    @PostMapping("/orderCreate")
    public String postOrderCreate(Model model, @Valid OrderForm orderForm, BindingResult bindingResult) {
        System.out.println(orderForm.toString());
        if(bindingResult.hasErrors()) {
            return "orderCreate";
        }
        String result = repository.createOrder(orderForm.getCustomerId(), orderForm.getOrder());
        if(!result.equals("")){
            ObjectError error = new ObjectError("error", result);
            String message = String.format(result, orderForm.getId());
            model.addAttribute("errorMsg",message);
            bindingResult.addError(error);
            return "/orderCreate";
        }
        return String.format("redirect:/customerUpdate/%s", orderForm.getCustomerId());
    }
    @GetMapping("/orderUpdate/{id}")
    public String getOrderUpdate(@PathVariable("id") String orderId, Model model) {
        Optional<Order> found = repository.findOrderById(orderId);
        if(found.isPresent()) {
            Order order = found.get();
            OrderForm orderForm = new OrderForm(order);
            model.addAttribute("orderForm", orderForm);
            return "/orderUpdate";
        }
        return String.format("redirect:/customers"); // should be an error page
    }
    @PostMapping("/orderUpdate/{id}")
    public String postOrderUpdate(@PathVariable("id") String orderId, Model model, @Valid OrderForm orderForm, BindingResult bindingResult) {
            return "/orderUpdate";
        }
}
