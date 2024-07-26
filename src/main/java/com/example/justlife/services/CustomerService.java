package com.example.justlife.services;

import com.example.justlife.dtos.CustomerDTO;
import com.example.justlife.dtos.ResponseDTO;

public interface CustomerService {
    ResponseDTO<CustomerDTO> addCustomer(CustomerDTO customerDTO);
    ResponseDTO<CustomerDTO> getCustomer(Integer customerId);
}
