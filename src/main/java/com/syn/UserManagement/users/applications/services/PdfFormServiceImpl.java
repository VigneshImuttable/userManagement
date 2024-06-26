package com.syn.UserManagement.users.applications.services;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.itextpdf.text.pdf.PdfDocument;
import com.syn.UserManagement.users.applications.interfaces.PdfFormService;
import com.syn.UserManagement.users.contracts.output.DataTransportDTO;
import com.syn.UserManagement.users.domain.model.User;
import com.syn.UserManagement.users.domain.repository.UserRepository;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Service
public class PdfFormServiceImpl implements PdfFormService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public DataTransportDTO formsUsingHTML(Long userId) throws IOException {

        PdfDocument pdfDocument = null;

        //Modifying HTML
        DataTransportDTO response = null;

        response = createPdfForm(userId);

        if (!response.isSuccess()) {
            return response;
        }

        //Generating pdf from HTML

        File inputFile;
        File outputFile;
        inputFile = File.createTempFile(String.valueOf(userId) + "-synergech", ".html");
        FileWriter fileWriter = new FileWriter(inputFile);
        fileWriter.write(response.getHtmlString());
        fileWriter.close();
        outputFile = File.createTempFile(String.valueOf(userId) + "-synergech", ".pdf");

        HtmlConverter.convertToPdf(inputFile, outputFile);
        byte[] array = null;
        try {
            array = FileUtils.readFileToByteArray(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setData(array);
        return response;
    }

    @Override
    public DataTransportDTO createPdfForm(Long userId) {
        DataTransportDTO response = new DataTransportDTO();
        String filePathName = "pdfForm.html";
        File htmlFile = new File(filePathName);
        try {
            ClassPathResource resource = new ClassPathResource("html/" + filePathName);
            FileUtils.copyInputStreamToFile(resource.getInputStream(), htmlFile);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            return response;
        }
        Document doc = null;

        try {
            doc = Jsoup.parse(htmlFile, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            return response;
        }

        //Element initialization
        Element logo = doc.getElementById("logo");
        Element userNameElement = doc.getElementById("userName");
        Element userIdElement = doc.getElementById("userId");
        Element ageElement = doc.getElementById("age");
        Element genderElement = doc.getElementById("gender");
        Element dobElement = doc.getElementById("dob");
        Element emailElement = doc.getElementById("email");
        Element addressElement = doc.getElementById("address");
        Element mobileNumberElement = doc.getElementById("mobileNumber");

        User user = userRepo.getReferenceById(userId);
        userNameElement.html(user.getUserName());
        userIdElement.html(String.valueOf(user.getUserId()));
        ageElement.html(String.valueOf(user.getAge()));
        genderElement.html(user.getGender());
        dobElement.html(user.getDob().toString()); // Format date according to your needs
        emailElement.html(user.getEmail());
        addressElement.html(user.getAddress());
        mobileNumberElement.html(String.valueOf(user.getMobileNumber()));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String createDate = "";

        Resource resource = new ClassPathResource("static/synergech_logo.png");
        try {
            System.out.println("url for logo " + resource.getURL().toString());
            logo.attr("src", resource.getURL().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Generate new HTML code
        response.setHtmlString(doc.html());

        response.setSuccess(true);
        return response;
    }
}
