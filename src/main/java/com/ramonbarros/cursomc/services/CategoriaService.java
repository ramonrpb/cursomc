package com.ramonbarros.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.ramonbarros.cursomc.domain.Categoria;
import com.ramonbarros.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

	public Categoria buscar(Integer id) {
		Optional<Categoria> categoria = ((CrudRepository<Categoria, Integer>) repo).findById(id);
		return categoria.orElse(null);
	}
}
