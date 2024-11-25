package com.example.bookingtour.controller.admin;

import com.example.bookingtour.entity.Tour;
import com.example.bookingtour.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class TourController {
    @Autowired
    private TourService tourService;

    @GetMapping("/admin/tour")
    public String showTourPage(Model model,
                               @RequestParam(value = "searchTerm", required = false) String searchTerm,
                               @RequestParam(value = "resultsPerPage", required = false, defaultValue = "10") int resultsPerPage,
                               @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                               @RequestParam(value = "sort", defaultValue = "name", required = false) String sort,
                               @RequestParam(value = "order", defaultValue = "asc", required = false) String order) {
        List<Tour> tours;


        if (searchTerm != null && !searchTerm.isEmpty()) {
            tours = tourService.searchTours(searchTerm);
        } else {
            tours = tourService.getAllTours();
        }

        System.out.println("PHUC" + tours);
        if (tours.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy kết quả phù hợp");
        }
        if ("asc".equals(order)) {
            tours.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value1 == null ? -1 : value1.compareTo(value2);
            });
        } else {
            tours.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value2 == null ? -1 : value2.compareTo(value1);
            });
        }


        int totalTours = tours.size();
        int totalPages = (int) Math.ceil((double) totalTours / resultsPerPage);
        int start = (page - 1) * resultsPerPage;
        int end = Math.min(start + resultsPerPage, totalTours);

        List<Tour> paginatedTours = tours.subList(start, end);


        model.addAttribute("tours", paginatedTours);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("resultsPerPage", resultsPerPage);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);


        return "admin/tour/index";
    }

    private Comparable<?> getPropertyValue(Tour tour, String propertyName) {
        if ("name".equals(propertyName)) {
            return tour.getTour_name(); // String
        } else if ("destination".equals(propertyName)) {
            return tour.getDestination(); // String
        } else if ("status".equals(propertyName)) {
            return tour.isStatus() ? 1 : 0;
        }
        return null;
    }

    @PutMapping("admin/tour/delete/{tourId}")
    @ResponseBody
    public ResponseEntity<String> deleteTour(@PathVariable("tourId") int tourId) {
        try {
            tourService.deleteTour(tourId);
            return ResponseEntity.ok("Xóa tour thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa tour thất bại.");
        }
    }

    @PutMapping("admin/tour/restore/{tourId}")
    @ResponseBody
    public ResponseEntity<String> restoreTour(@PathVariable("tourId") int tourId) {
        try {
            tourService.restoreTour(tourId);
            return ResponseEntity.ok("Khôi phục tour thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Khôi phục tour thất bại.");
        }
    }

    @GetMapping("admin/tour/add")
    public String showPageAddTour(Model model) {
        model.addAttribute("tour", new Tour());
        return "admin/tour/add";
    }


    @PostMapping(value = "/admin/tour/add/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addTour(@RequestBody Map<String, Object> formData) {
        try {
            String base64Image = (String) formData.get("thumbnail");
            byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
            String uniqueFilename = UUID.randomUUID().toString() + ".png";
            Path filePath = Paths.get("src/main/resources/static/thumbnails_img/" + uniqueFilename);
            Files.write(filePath, imageBytes);

            // Xử lý tour_description
            String tourDescription = (String) formData.get("tour_description");
            tourDescription = tourDescription.replaceAll(" data-filename=\"[^\"]*\"", ""); // Xóa thuộc tính data-filename

            // Sử dụng regex để tìm các hình ảnh base64 trong mô tả
            Pattern pattern = Pattern.compile("src=\"data:image/(png|jpeg|jpg|gif|bmp|webp);base64,([^\"]+)\"");
            Matcher matcher = pattern.matcher(tourDescription);
            List<String> newImageSrcList = new ArrayList<>();

            while (matcher.find()) {
                String imageType = matcher.group(1); // Nhận định dạng hình ảnh
                byte[] imgBytes = Base64.getDecoder().decode(matcher.group(2)); // Nhận dữ liệu hình ảnh base64

                // Đặt tên tệp hình ảnh
                String uniqueImageFilename = UUID.randomUUID().toString() + "." + imageType; // Đặt tên theo định dạng
                Path imgPath = Paths.get("src/main/resources/static/description_img/" + uniqueImageFilename);
                Files.write(imgPath, imgBytes); // Ghi tệp hình ảnh

                // Thay thế đường dẫn hình ảnh trong mô tả
                tourDescription = tourDescription.replace("src=\"data:image/" + imageType + ";base64," + matcher.group(2) + "\"", "src=\"/description_img/" + uniqueImageFilename + "\"");
                newImageSrcList.add("/description_img/" + uniqueImageFilename);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Lấy các trường khác từ formData
            String tourName = (String) formData.get("tour_name");
            String destination = (String) formData.get("destination");
            LocalDate start_date = LocalDate.parse((String) formData.get("start_date"), formatter);
            LocalDate end_date = LocalDate.parse((String) formData.get("end_date"), formatter);

            double price = Double.parseDouble(formData.get("price").toString());
            int available_seats = Integer.parseInt(formData.get("available_seats").toString());
            String tour_type = (String) formData.get("tour_type");
            int trangThai = Integer.parseInt(formData.get("status").toString());
            boolean status = (trangThai == 1);

            System.out.println(tourDescription);
            // Tạo một đối tượng Tour mới và thiết lập các trường
            Tour tour = new Tour();
            tour.setTour_name(tourName);
            tour.setDestination(destination);
            tour.setThumbnail(uniqueFilename);
            tour.setStart_date(start_date);
            tour.setEnd_date(end_date);
            tour.setPrice(price);
            tour.setTour_description(tourDescription);
            tour.setAvailable_seats(available_seats);
            tour.setTour_type(tour_type);
            tour.setStatus(status);

            // Lưu tour vào cơ sở dữ liệu
            tourService.saveTour(tour);

            return ResponseEntity.ok("Thêm thành công!");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi tải lên hình ảnh");
        }
    }


    @GetMapping("admin/tour/edit/{id}")
    public String showPageEditTour(@PathVariable("id") int id, Model model) {
        Tour t = this.tourService.findById(id);



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedStartDate = t.getStart_date().format(formatter);
        model.addAttribute("startDate", formattedStartDate);

        String formattedEndDate = t.getEnd_date().format(formatter);
        model.addAttribute("endDate", formattedEndDate);



        model.addAttribute("tour", t);


        return "admin/tour/edit";
    }

    @PutMapping(value = "/admin/tour/update/{id}/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> Tour(@RequestBody Map<String, Object> formData) {
        try {
            int tour_id = Integer.parseInt(formData.get("tour_id").toString());
            Tour tour = tourService.findById(tour_id);
            boolean isChangeThumbnail = Boolean.parseBoolean(formData.get("isChangeThumbnail").toString());
            if (isChangeThumbnail) {
                String oldThumbnail = tour.getThumbnail();
                if (oldThumbnail != null && !oldThumbnail.isEmpty()) {
                    Path oldThumbnailPath = Paths.get("src/main/resources/static/thumbnails_img/" + oldThumbnail);
                    try {
                        Files.deleteIfExists(oldThumbnailPath);
                    } catch (IOException e) {
                        e.printStackTrace(); // Xử lý lỗi nếu không thể xóa
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Failed to delete old thumbnail.");
                    }
                }
                String base64Image = (String) formData.get("thumbnail");
                byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
                String uniqueFilename = UUID.randomUUID().toString() + ".png";
                Path filePath = Paths.get("src/main/resources/static/thumbnails_img/" + uniqueFilename);
                Files.write(filePath, imageBytes);
                tour.setThumbnail(uniqueFilename);
            }


            // Xử lý tour_description
            String tourDescription = (String) formData.get("tour_description");
            tourDescription = tourDescription.replaceAll(" data-filename=\"[^\"]*\"", ""); // Xóa thuộc tính data-filename

            // Sử dụng regex để tìm các hình ảnh base64 trong mô tả
            Pattern pattern = Pattern.compile("src=\"data:image/(png|jpeg|jpg|gif|bmp|webp);base64,([^\"]+)\"");
            Matcher matcher = pattern.matcher(tourDescription);
            List<String> newImageSrcList = new ArrayList<>();
            List<String> oldImageSrcList = new ArrayList<>();

            while (matcher.find()) {
                String imageType = matcher.group(1); // Nhận định dạng hình ảnh
                byte[] imgBytes = Base64.getDecoder().decode(matcher.group(2)); // Nhận dữ liệu hình ảnh base64

                // Đặt tên tệp hình ảnh
                String uniqueImageFilename = UUID.randomUUID().toString() + "." + imageType; // Đặt tên theo định dạng
                Path imgPath = Paths.get("src/main/resources/static/description_img/" + uniqueImageFilename);
                Files.write(imgPath, imgBytes); // Ghi tệp hình ảnh

                // Thay thế đường dẫn hình ảnh trong mô tả
                tourDescription = tourDescription.replace("src=\"data:image/" + imageType + ";base64," + matcher.group(2) + "\"", "src=\"/description_img/" + uniqueImageFilename + "\"");
                newImageSrcList.add("/description_img/" + uniqueImageFilename);
            }

            // Xóa hình ảnh cũ không còn trong mô tả
            String oldTourDescription = tour.getTour_description(); // Mô tả cũ
            Pattern oldImagePattern = Pattern.compile("src=\"/description_img/([^\"]+)\""); // Tìm hình ảnh cũ trong mô tả cũ
            Matcher oldImageMatcher = oldImagePattern.matcher(oldTourDescription);

            while (oldImageMatcher.find()) {
                String oldImageSrc = oldImageMatcher.group(1); // Lấy tên tệp hình ảnh cũ
                if (!newImageSrcList.contains("/description_img/" + oldImageSrc)) {
                    // Nếu hình ảnh cũ không tồn tại trong mô tả mới, xóa
                    Path oldImagePath = Paths.get("src/main/resources/static/description_img/" + oldImageSrc);
                    try {
                        Files.deleteIfExists(oldImagePath); // Xóa file nếu tồn tại
                    } catch (IOException e) {
                        e.printStackTrace(); // Xử lý lỗi khi không thể xóa
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Failed to delete old image.");
                    }
                }
            }

            tour.setTour_description(tourDescription);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Lấy các trường khác từ formData
            String tourName = (String) formData.get("tour_name");
            String destination = (String) formData.get("destination");
            LocalDate start_date = LocalDate.parse((String) formData.get("start_date"), formatter);
            LocalDate end_date = LocalDate.parse((String) formData.get("end_date"), formatter);

            double price = Double.parseDouble(formData.get("price").toString());
            int available_seats = Integer.parseInt(formData.get("available_seats").toString());
            String tour_type = (String) formData.get("tour_type");
            int trangThai = Integer.parseInt(formData.get("status").toString());
            boolean status = (trangThai == 1);

            System.out.println(tourDescription);
            // Tạo một đối tượng Tour mới và thiết lập các trường
            tour.setTour_name(tourName);
            tour.setDestination(destination);
            tour.setStart_date(start_date);
            tour.setEnd_date(end_date);
            tour.setPrice(price);
            tour.setTour_description(tourDescription);
            tour.setAvailable_seats(available_seats);
            tour.setTour_type(tour_type);
            tour.setStatus(status);

            // Lưu tour vào cơ sở dữ liệu
            tourService.saveTour(tour);

            return ResponseEntity.ok("Update thành công!");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi tải lên hình ảnh");
        }
    }

}
