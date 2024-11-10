package com.example.bookingtour.service;

import com.example.bookingtour.entity.Booking;
import com.example.bookingtour.entity.Notification;
import com.example.bookingtour.entity.Transport;
import com.example.bookingtour.repository.BookingRepository;
import com.example.bookingtour.repository.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

//    public List<Booking> getAllBookings(){
//        return this.bookingRepository.findAll();
//    }
//
//    public List<Booking> searchBookings(String searchTerm){
//        return this.bookingRepository.searchBookings(searchTerm);
//    }


    public List<Object[]>getAllBookings(){
        return  bookingRepository.getAllBookings();
    }
    public List<Object[]> searchBookings(String searchTerm){
        return bookingRepository.searchBookings(searchTerm);
    }




    public  List<Object[]> findEditById(int id){
        return bookingRepository.findEditBookingById(id);
    }


    public Booking findById(int id){
        return bookingRepository.findBookingById(id);
    }

    public void deleteBooking(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Transport not found"));
        booking.setBookingStatus(0);
        bookingRepository.save(booking);
    }

    public void restoreBooking(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Transport not found"));
        booking.setBookingStatus(1);
        bookingRepository.save(booking);
    }

    public void editBooking(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Transport not found"));
    }

    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

}
