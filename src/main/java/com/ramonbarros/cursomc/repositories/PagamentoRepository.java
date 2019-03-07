package com.ramonbarros.cursomc.repositories;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.ramonbarros.cursomc.domain.Pagamento;

@Repository
public interface PagamentoRepository extends JpaRepositoryImplementation<Pagamento, Integer>{

}
