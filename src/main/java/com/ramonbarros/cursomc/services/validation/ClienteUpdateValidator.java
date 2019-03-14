package com.ramonbarros.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.ramonbarros.cursomc.ClienteDTO;
import com.ramonbarros.cursomc.domain.Cliente;
import com.ramonbarros.cursomc.repositories.ClienteRepository;
import com.ramonbarros.cursomc.resources.exceptions.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer idCLiente = Integer.valueOf(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();
		
		if(null != objDto.getEmail() && !objDto.getEmail().isEmpty()) {
			Cliente cliente = clienteRepository.findByEmail(objDto.getEmail());
			if(null != cliente && !cliente.getId().equals(idCLiente)) {
				list.add(new FieldMessage("email", "Email já existente"));
			}
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getFieldMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
