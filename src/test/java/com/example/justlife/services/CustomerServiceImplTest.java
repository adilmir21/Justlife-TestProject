package com.example.justlife.services;

import com.example.justlife.dtos.CustomerDTO;
import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.exceptions.ResourceNotFoundException;
import com.example.justlife.models.CustomerEntity;
import com.example.justlife.repositories.CustomerRepository;
import com.example.justlife.services.impl.CustomerServiceImpl;
import com.example.justlife.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addCustomer_Success() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1);

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1);

        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);

        ResponseDTO<CustomerDTO> responseDTO = customerService.addCustomer(customerDTO);

        assertEquals(Constants.STATUS_SUCCESS, responseDTO.getStatusCode());
        assertNotNull(responseDTO.getData());
        assertEquals(1, responseDTO.getData().getId());
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    public void getCustomer_Success() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1);

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customerEntity));

        ResponseDTO<CustomerDTO> responseDTO = customerService.getCustomer(1);

        assertEquals(Constants.STATUS_SUCCESS, responseDTO.getStatusCode());
        assertNotNull(responseDTO.getData());
        assertEquals(1, responseDTO.getData().getId());
        verify(customerRepository, times(1)).findById(1);
    }

    @Test
    public void getCustomer_NotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomer(1));
    }
}
