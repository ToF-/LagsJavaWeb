package com.example.lags.controller;

import com.example.lags.model.Customer;
import com.example.lags.form.CustomerForm;
import com.example.lags.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
@Controller
public class CustomerController {
    @Autowired
    private Repository repository;

    @GetMapping("/customers")
    public String getCustomers(Model model) {
        List<Customer> customers = repository.findAllCustomers();
        model.addAttribute("customerList", customers);
        return "customers";
    }

    @GetMapping("/customerCreate")
    public String getCustomerCreate(Model model) {
        CustomerForm customerForm = new CustomerForm("","",null);
        model.addAttribute("customerForm",customerForm);
        return "customerCreate";
    }
    @PostMapping("/customerCreate")
    public String postCustomerCreate(Model model, @Valid CustomerForm customerForm, BindingResult bindingResult) throws SQLException {
        if(bindingResult.hasErrors()) {
            return "customerCreate";
        }
        if(!repository.createCustomer(customerForm.getCustomer())) {
            ObjectError error = new ObjectError("error","This customer already exists.");
            String message = String.format("The customer %s already exists.", customerForm.getId());
            model.addAttribute("errorMsg",message);
            bindingResult.addError(error);
            return "/customerCreate";
        }
        return "redirect:/customers";
    }
    @GetMapping("/customerUpdate/{id}")
    public String getCustomerUpdate(@PathVariable("id") String id, Model model) {
        Optional<Customer> found = repository.findCustomerById(id);
        if(found.isPresent()) {
            CustomerForm customerForm = new CustomerForm(found.get().getId(), found.get().getName(), found.get().getOrders());
            customerForm.setId(found.get().getId());
            customerForm.setName(found.get().getName());
            model.addAttribute("customerForm", customerForm);
            return "/customerUpdate";
        }
        String message = String.format("The customer %s cannot be found.", id);
        model.addAttribute("errorMsg", message);
        return "redirect:/customers";
    }
    @PostMapping("/customerUpdate/{id}")
    public String postCustomerUpdate(@PathVariable("id") String id, Model model, @Valid CustomerForm customerForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "/customerUpdate";
        }
        repository.updateCustomer(customerForm.getCustomer());
        return "redirect:customers";
    }
    @GetMapping("/customerDelete/{id}")
    public String getCustomerDelete(@PathVariable("id") String id, Model model) {
        Optional<Customer> found = repository.findCustomerById(id);
        if(found.isPresent()) {
            CustomerForm customerForm = new CustomerForm("","",null);
            customerForm.setId(found.get().getId());
            customerForm.setName(found.get().getName());
            model.addAttribute("customerForm", customerForm);
            return "/customerDelete";
        }
        String message = String.format("The customer %s cannot be found.", id);
        model.addAttribute("errorMsg", message);
        return "redirect:/customers";
    }

    @PostMapping("/customerDelete/{id}")
    public String postCustomerDelete(@PathVariable("id") String id, Model model, @Valid CustomerForm customerForm, BindingResult bindingResult) {
        if(!repository.deleteCustomer(id)) {
            String message = String.format("The customer %s has orders and cannot be deleted.", id);
            model.addAttribute("errorMsg", message);
            return "customerDelete";
        }
        return "redirect:customers";
    }
    @GetMapping("/customerRetrieve/{id}")
    public String getCustomerRetrieve(@PathVariable("id") String id, Model model) {
        Optional<Customer> found = repository.findCustomerById(id);
        if(found.isPresent()) {
            CustomerForm customerForm = new CustomerForm(found.get().getId(), found.get().getName(), found.get().getOrders());
            customerForm.setId(found.get().getId());
            customerForm.setName(found.get().getName());
            model.addAttribute("customerForm", customerForm);
            return "/customerRetrieve";
        }
        String message = String.format("The customer %s cannot be found.", id);
        model.addAttribute("errorMsg", message);
        return "redirect:/customers";
    }


}
