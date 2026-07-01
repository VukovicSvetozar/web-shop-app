package org.vs.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MailService {

	private final JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String from;

	public boolean posaljiPin(String email, String pin) {
		boolean poslato = false;
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setFrom(from);
		smm.setTo(email);
		String naslov = "WebShop - aktivacija naloga";
		smm.setSubject(naslov);
		String tekst = "Vrijeme: " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) + "\n" + "Pin: " + pin;
		smm.setText(tekst);
		try {
			javaMailSender.send(smm);
			poslato = true;
		} catch (MailException ex) {
			poslato = false;
		}
		return poslato;
	}

}
