package com.example.bookingtour.controller.admin;

import com.example.bookingtour.entity.Transport;
import com.example.bookingtour.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TransportController {
    @Autowired
    private TransportService transportService;



    @Autowired
    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }



    private Comparable<?> getPropertyValue(Transport transport, String propertyName) {
        if ("transportation_type".equals(propertyName)) {
            return transport.getTransportationType(); // String
        } else if ("tag".equals(propertyName)) {
            return transport.getTag(); // String
        } else if ("seat".equals(propertyName)) {
            return transport.getSeat();
        }else if("status".equals(propertyName)){
            return transport.getStatus();
        }
        return null;
    }

    @GetMapping("/admin/transportation")
    public String showPageIndexAdmin(
            Model model,
            @RequestParam(value="searchTerm",required=false) String searchTerm,
            @RequestParam(value = "resultsPerPage" , required = false, defaultValue = "10") int resultsPerPage,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "sort" , defaultValue = "name" , required = false) String sort,
            @RequestParam(value="order", defaultValue = "asc", required = false) String order
    ){
        List<Transport> transports;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            transports = transportService.searchTransports(searchTerm);
        } else {
            transports = transportService.getAllTransports();
        }
        if(transports.isEmpty()){
            model.addAttribute("message","Không tìm thấy kết quả phù hợp");
        }
        if ("asc".equals(order)) {
            transports.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value1 == null ? -1 : value1.compareTo(value2);
            });
        } else {
            transports.sort((c1, c2) -> {
                Comparable value1 = getPropertyValue(c1, sort);
                Comparable value2 = getPropertyValue(c2, sort);
                return value2 == null ? -1 : value2.compareTo(value1);
            });
        }


        int totalTransports = transports.size();
        int totalPages = (int) Math.ceil((double) totalTransports / resultsPerPage);
        int start = (page - 1) * resultsPerPage;
        int end = Math.min(start + resultsPerPage, totalTransports);

        List<Transport> paginatedTransports = transports.subList(start, end);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("resultsPerPage", resultsPerPage);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("sort",sort);
        model.addAttribute("order", order);
        model.addAttribute("transports", paginatedTransports);
        return "admin/transportation/index";
    }




    @GetMapping("admin/transportation/delete/{id}")
    public String deleteTransport(@PathVariable("id") int id){
        try {
           transportService.deleteTransport(id);
            return "redirect:/admin/transportation";

        } catch (Exception e) {
            return "redirect:/admin/transportation";
        }
    }

    @GetMapping("admin/transportation/restore/{id}")
    public String restoreTransport(@PathVariable("id") int id){
        try {
            transportService.restoreTransport(id);
            return "redirect:/admin/transportation";

        } catch (Exception e) {
            return "redirect:/admin/transportation";
        }
    }





    @GetMapping("admin/transportation/edit/{id}")
    public String showPageEditTransport(@PathVariable("id") int id,Model model){
        Transport transport = transportService.findById(id);
        model.addAttribute("transport",transport);
        return "admin/transportation/edit";
    }

    @PostMapping("admin/transportation/update/{id}/send")
    public String executeEditTransport(@PathVariable("id") int id , @ModelAttribute Transport transport, RedirectAttributes ra){
        Transport t = this.transportService.findById(id);
        t.setTransportationType(transport.getTransportationType());
        t.setTag(transport.getTag());
        t.setSeat(transport.getSeat());
        t.setStatus(transport.getStatus());
        transportService.save(t);
        ra.addFlashAttribute("message","Cập nhật thành công");
        return String.format("redirect:/admin/transportation/edit/%d", id);
    }


    @GetMapping("admin/transportation/add")
    public String showPageAddAdmin(Model model){
        model.addAttribute("transport",new Transport());
        return "admin/transportation/add";
    }

    @PostMapping("admin/transportation/add/send")
    public String executeAddAdmin(@ModelAttribute Transport transport, RedirectAttributes ra) {
        transportService.save(transport);
        ra.addFlashAttribute("message", "Thêm mới phương tiện thành công");
        return "redirect:/admin/transportation/add";
    }
}
