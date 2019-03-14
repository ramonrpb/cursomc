package com.ramonbarros.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.ramonbarros.cursomc.ClienteNewDTO;
import com.ramonbarros.cursomc.domain.Cliente;
import com.ramonbarros.cursomc.domain.enums.TipoCliente;
import com.ramonbarros.cursomc.repositories.ClienteRepository;
import com.ramonbarros.cursomc.resources.exceptions.FieldMessage;
import com.ramonbarros.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		// inclua os testes aqui, inserindo erros na lista
		
		if(null != objDto.getTipo() && objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCodigo()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if(null != objDto.getTipo() && objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}
		
		if(null != objDto.getEmail() && !objDto.getEmail().isEmpty()) {
			Cliente cliente = clienteRepository.findByEmail(objDto.getEmail());
			if(null != cliente) {
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
