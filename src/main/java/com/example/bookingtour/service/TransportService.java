package com.example.bookingtour.service;
import com.example.bookingtour.entity.Admin;
import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.entity.Transport;
import com.example.bookingtour.repository.AdminRepository;
import com.example.bookingtour.repository.CustomerRepository;
import com.example.bookingtour.repository.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportService {
    @Autowired
    private TransportRepository transportRepository;

//    public Customer findByEmail(String email){
//        return customerRepository.findCustomerByEmail(email);
//    }
//    public Customer findByPhoneNumber(String phoneNumber){
//        return customerRepository.findCustomerByPhoneNumber(phoneNumber);
//    }
//    public void add_update(Customer c){
//        customerRepository.save(c);
//    }

    public List<Transport> getAllTransport(){
        return transportRepository.findAll();
    }


//    public List<Customer> searchCustomers(String searchTerm){
//        return this.customerRepository.searchCustomers(searchTerm);
//    }
//    public void deleteCustomer(int customerId) {
//        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
//        customer.setStatus(false);
//        customerRepository.save(customer);
//    }
//    public void restoreCustomer(int customerId) {
//        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
//        customer.setStatus(true);
//        customerRepository.save(customer);
//    }
//    public Customer findById(int id){
//        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
//        return customer;
//    }
//    public void save(Customer customer) {
//        customerRepository.save(customer);
//    }



}
