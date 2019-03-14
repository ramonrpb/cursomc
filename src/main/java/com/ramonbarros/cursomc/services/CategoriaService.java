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
import org.springframework.stereotype.Service;

import com.ramonbarros.cursomc.domain.Categoria;
import com.ramonbarros.cursomc.dto.CategoriaDTO;
import com.ramonbarros.cursomc.repositories.CategoriaRepository;
import com.ramonbarros.cursomc.services.exceptions.DataIntegrityException;
import com.ramonbarros.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Integer id) {
		Optional<Categoria> obj = ((CrudRepository<Categoria, Integer>) repo).findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria categoria) {
		categoria.setId(null);
		return repo.save(categoria);
	}
	
	public Categoria update(Categoria categoria) {
		Categoria novaCategoria = find(categoria.getId());
		updateData(novaCategoria, categoria);
		return repo.save(novaCategoria);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos!");
		}
	}
	
	public List<CategoriaDTO> findAll(){
		List<Categoria> lista = repo.findAll();
		List<CategoriaDTO> listaDTO = lista.stream().map(cat -> new CategoriaDTO(cat)).collect(Collectors.toList());
		return listaDTO;
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String directions){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(directions), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO categoriaDTO) {
		return new Categoria(categoriaDTO.getId(), categoriaDTO.getNome());
	}
	
	private void updateData(Categoria novaCategoria, Categoria categoria) {
		novaCategoria.setNome(categoria.getNome());
	}
}
