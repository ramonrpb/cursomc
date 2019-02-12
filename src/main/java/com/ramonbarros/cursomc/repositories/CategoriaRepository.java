package com.ramonbarros.cursomc.repositories;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.ramonbarros.cursomc.domain.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepositoryImplementation<Categoria, Integer>{

}
