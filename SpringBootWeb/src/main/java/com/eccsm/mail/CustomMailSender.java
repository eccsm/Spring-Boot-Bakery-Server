package com.eccsm.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class CustomMailSender {
	
	@Autowired
    private JavaMailSender javaMailSender;

	public void sendEmail(String name,String email,String message) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("admin@admin.com");

        msg.setSubject("New Question from "+name);
        msg.setText(message+"\n\nContact Mail: "+email);

        javaMailSender.send(msg);

    }
	
	public void sendPasswordEmail(String token,String email) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject("Password Reset");
        msg.setText("Hello, For reset your mail please click the link below\n\nReset: http://localhost:4200/resetpassword?token="+token);

        javaMailSender.send(msg);

    }
	

}
