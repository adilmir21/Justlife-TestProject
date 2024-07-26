package com.example.justlife.controllers;

import com.example.justlife.dtos.CustomerDTO;
import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{id}")
    ResponseDTO<CustomerDTO> getCustomer(@PathVariable Integer id){
        return customerService.getCustomer(id);
    }

    @PostMapping
    ResponseDTO<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO){
        return customerService.addCustomer(customerDTO);
    }
}
