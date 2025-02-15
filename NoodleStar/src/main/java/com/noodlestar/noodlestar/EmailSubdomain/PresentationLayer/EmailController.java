package com.noodlestar.noodlestar.EmailSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.EmailSubdomain.BusinessLayer.EmailService;
import com.noodlestar.noodlestar.EmailSubdomain.PresentationLayer.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
        return "Email sent successfully.";
    }
}
