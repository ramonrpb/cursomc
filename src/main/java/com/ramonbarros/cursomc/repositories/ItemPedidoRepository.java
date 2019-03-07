package com.ramonbarros.cursomc.repositories;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.ramonbarros.cursomc.domain.ItemPedido;

@Repository
public interface ItemPedidoRepository extends JpaRepositoryImplementation<ItemPedido, Integer>{

}
