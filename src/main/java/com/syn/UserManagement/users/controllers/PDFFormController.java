package com.syn.UserManagement.users.controllers;

import com.syn.UserManagement.users.applications.services.PdfFormServiceImpl;
import com.syn.UserManagement.users.contracts.output.DataTransportDTO;
import com.syn.UserManagement.users.contracts.output.ResponseDTO;
import com.syn.UserManagement.users.domain.model.User;
import com.syn.UserManagement.users.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("app/user")
public class PDFFormController {

    @Autowired
    private PdfFormServiceImpl pdfFormService;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/create")
    public User createuser(@RequestBody User user) {
        return userRepo.save(user);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<?> PdfDownload(@RequestParam("userId") long userId, HttpServletResponse response) {
        ResponseDTO a1 = new ResponseDTO();
        byte[] a = null;
        DataTransportDTO serviceResponse = new DataTransportDTO();
        try {
            serviceResponse = pdfFormService.formsUsingHTML(userId);
            if (serviceResponse.isSuccess()) {
                a = (byte[]) serviceResponse.getData();
            } else {
                a1.setMessage(serviceResponse.getMessage());
                return new ResponseEntity<ResponseDTO>(a1, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            a1.setMessage(e.getLocalizedMessage());
            return new ResponseEntity<ResponseDTO>(a1, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setContentType("application/pdf");
        response.setContentLength(a.length);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + userId + ".pdf\"");
        try {
            FileCopyUtils.copy(a, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(a1, HttpStatus.OK);
    }


}
