package com.ramonbarros.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.ramonbarros.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage message);
	
}
