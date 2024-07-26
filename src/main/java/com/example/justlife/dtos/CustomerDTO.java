package com.example.justlife.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {
    private Integer id;
    private String name;
    private String email;
    private String phoneNumber;
}
