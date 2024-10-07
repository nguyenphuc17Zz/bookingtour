package com.example.bookingtour.service;

import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    public Customer findByEmail(String email){
        return customerRepository.findCustomerByEmail(email);
    }
    public Customer findByPhoneNumber(String phoneNumber){
        return customerRepository.findCustomerByPhoneNumber(phoneNumber);
    }
    public void add_update(Customer c){
        customerRepository.save(c);
    }
}
