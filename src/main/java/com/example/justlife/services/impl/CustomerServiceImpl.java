package com.example.justlife.services.impl;

import com.example.justlife.dtos.CustomerDTO;
import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.exceptions.ResourceNotFoundException;
import com.example.justlife.models.CustomerEntity;
import com.example.justlife.repositories.CustomerRepository;
import com.example.justlife.services.CustomerService;
import com.example.justlife.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public ResponseDTO<CustomerDTO> addCustomer(CustomerDTO customerDTO) {
        log.info("CustomerServiceImpl :: addCustomer starts");
        Long startTime = System.currentTimeMillis();
        ResponseDTO<CustomerDTO> responseDTO = new ResponseDTO<>();

        CustomerEntity customer = new CustomerEntity();
        BeanUtils.copyProperties(customerDTO, customer);
        BeanUtils.copyProperties(customerRepository.save(customer), customerDTO);

        responseDTO.setData(customerDTO);
        responseDTO.setStatusCode(Constants.STATUS_SUCCESS);
        Long endTime = System.currentTimeMillis();
        log.info("CustomerServiceImpl :: addCustomer ends at {}ms", endTime - startTime);
        return responseDTO;
    }

    @Override
    public ResponseDTO<CustomerDTO> getCustomer(Integer customerId) {
        log.info("CustomerServiceImpl :: getCustomer starts");
        Long startTime = System.currentTimeMillis();
        ResponseDTO<CustomerDTO> responseDTO = new ResponseDTO<>();

        CustomerEntity customer = customerRepository.findById(customerId)
                        .orElseThrow(()-> new ResourceNotFoundException("Customer not found"));
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);

        responseDTO.setData(customerDTO);
        responseDTO.setStatusCode(Constants.STATUS_SUCCESS);
        Long endTime = System.currentTimeMillis();
        log.info("CustomerServiceImpl :: getCustomer ends at {}ms", endTime - startTime);
        return responseDTO;
    }


}
