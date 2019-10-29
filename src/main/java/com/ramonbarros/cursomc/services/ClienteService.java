package com.ramonbarros.cursomc.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ramonbarros.cursomc.domain.Cidade;
import com.ramonbarros.cursomc.domain.Cliente;
import com.ramonbarros.cursomc.domain.Endereco;
import com.ramonbarros.cursomc.domain.enums.Perfil;
import com.ramonbarros.cursomc.domain.enums.TipoCliente;
import com.ramonbarros.cursomc.dto.ClienteDTO;
import com.ramonbarros.cursomc.dto.ClienteNewDTO;
import com.ramonbarros.cursomc.repositories.CidadeRepository;
import com.ramonbarros.cursomc.repositories.ClienteRepository;
import com.ramonbarros.cursomc.repositories.EnderecoRepository;
import com.ramonbarros.cursomc.resources.exceptions.AuthorizationException;
import com.ramonbarros.cursomc.security.UserSS;
import com.ramonbarros.cursomc.services.exceptions.DataIntegrityException;
import com.ramonbarros.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = ((CrudRepository<Cliente, Integer>) repo).findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente update(Cliente cliente) {
		Cliente novoCliente = find(cliente.getId());
		updateData(novoCliente, cliente);
		return repo.save(novoCliente);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente que possui pedidos!");
		}
	}
	
	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		cliente = repo.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
	}
	
	public List<ClienteDTO> findAll(){
		List<Cliente> lista = repo.findAll();
		List<ClienteDTO> listaDTO = lista.stream().map(cat -> new ClienteDTO(cat)).collect(Collectors.toList());
		return listaDTO;
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String directions){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(directions), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO clienteDTO) {
		Cliente cliente = new Cliente(null, clienteDTO.getNome(), clienteDTO.getEmail(), clienteDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteDTO.getTipo()), pe.encode(clienteDTO.getSenha()));
		Optional<Cidade> cidade = cidadeRepository.findById(clienteDTO.getCidadeId());
		Endereco endereco = new Endereco(null, clienteDTO.getLogradouro(), clienteDTO.getNumero(), clienteDTO.getComplemento(), clienteDTO.getBairro(), clienteDTO.getCep(), cliente, cidade.get());
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteDTO.getTelefone1());
		if(null != clienteDTO.getTelefone2()) {
			cliente.getTelefones().add(clienteDTO.getTelefone2());
		}
		if(null != clienteDTO.getTelefone3()) {
			cliente.getTelefones().add(clienteDTO.getTelefone3());
		}
		return cliente;
	}
	
	private void updateData(Cliente novoCliente, Cliente cliente) {
		novoCliente.setNome(cliente.getNome());
		novoCliente.setEmail(cliente.getEmail());
	}
}
