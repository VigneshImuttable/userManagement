package com.syn.UserManagement.users.applications.interfaces;

import com.syn.UserManagement.users.contracts.output.DataTransportDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface PdfFormService {


    DataTransportDTO formsUsingHTML(Long userId) throws IOException;

    DataTransportDTO createPdfForm(Long userId);
}
