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


    public List<Transport> getAllTransports(){
        return this.transportRepository.findAll();
    }
    public List<Transport> searchTransports(String searchTerm){
        return this.transportRepository.searchTransports(searchTerm);
    }

    public Transport findById(int id){
        return transportRepository.findTransportById(id);
    }

//    public void deleteTransport(int transportId) {
//        transportRepository.deleteById(transportId);
//    }

    public void deleteTransport(int transportId) {
        Transport transport = transportRepository.findById(transportId).orElseThrow(() -> new RuntimeException("Transport not found"));
        transport.setStatus(false);
        transportRepository.save(transport);
    }


    public void restoreTransport(int transportId) {
        Transport transport = transportRepository.findById(transportId).orElseThrow(() -> new RuntimeException("Transport not found"));
        transport.setStatus(true);
        transportRepository.save(transport);
    }

    public void editTransport(int transportId) {
        Transport transport = transportRepository.findById(transportId).orElseThrow(() -> new RuntimeException("Transport not found"));
    }

    public void save(Transport transport) {
        transportRepository.save(transport);
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
