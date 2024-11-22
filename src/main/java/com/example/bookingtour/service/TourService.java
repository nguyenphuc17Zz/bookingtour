package com.example.bookingtour.service;

import com.example.bookingtour.entity.Customer;
import com.example.bookingtour.entity.Tour;
import com.example.bookingtour.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourService {
    @Autowired
    private TourRepository tourRepository;

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public List<Tour> searchTours(String searchTerm) {
        return this.tourRepository.searchTours(searchTerm);
    }

    public void deleteTour(int tourId) {
        Tour tour = tourRepository.findById(tourId).orElseThrow(() -> new RuntimeException("Tour not found"));
        tour.setStatus(false);
        tourRepository.save(tour);
    }

    public void restoreTour(int tourId) {
        Tour tour = tourRepository.findById(tourId).orElseThrow(() -> new RuntimeException("Tour not found"));
        tour.setStatus(true);
        tourRepository.save(tour);
    }

    public void saveTour(Tour tour) {
        tourRepository.save(tour);
    }

    public Tour findById(int id) {
        Tour t = tourRepository.findById(id).orElseThrow(() -> new RuntimeException("Tour not found"));
        return t;
    }
    public List<Tour> getAllToursActive(){
        return tourRepository.getAllToursActive();
    }
    public int countTour(){
        return tourRepository.countTour();
    }
}
