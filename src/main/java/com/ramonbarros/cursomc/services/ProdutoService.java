package com.ramonbarros.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.ramonbarros.cursomc.domain.Categoria;
import com.ramonbarros.cursomc.domain.Pedido;
import com.ramonbarros.cursomc.domain.Produto;
import com.ramonbarros.cursomc.repositories.CategoriaRepository;
import com.ramonbarros.cursomc.repositories.ProdutoRepository;
import com.ramonbarros.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto find(Integer id) {
		Optional<Produto> obj = ((CrudRepository<Produto, Integer>) repo).findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String directions){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(directions), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}
}
