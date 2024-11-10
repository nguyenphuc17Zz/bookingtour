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

    public List<Transport> getAllTransports(){
        return this.transportRepository.findAll();
    }
    public List<Transport> searchTransports(String searchTerm){
        return this.transportRepository.searchTransports(searchTerm);
    }

    public Transport findById(int id){
        return transportRepository.findTransportById(id);
    }

    public void deleteTransport(int transportId) {
        Transport transport = transportRepository.findById(transportId).orElseThrow(() -> new RuntimeException("Transport not found"));
        transport.setStatus(0);
        transportRepository.save(transport);
    }


    public void restoreTransport(int transportId) {
        Transport transport = transportRepository.findById(transportId).orElseThrow(() -> new RuntimeException("Transport not found"));
        transport.setStatus(1);
        transportRepository.save(transport);
    }

    public void editTransport(int transportId) {
        Transport transport = transportRepository.findById(transportId).orElseThrow(() -> new RuntimeException("Transport not found"));
    }

    public void save(Transport transport) {
        transportRepository.save(transport);
    }

}
