package it.cecchi.smarthome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

	@Autowired
	private MailSender mailSender;

	private @Value("${application.name}")
	String applicationName;

	public void sendMail(String to, String text) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(applicationName);
		message.setSubject(applicationName + " notification");
		message.setText(text);
		message.setTo(to);
		mailSender.send(message);
	}
}
