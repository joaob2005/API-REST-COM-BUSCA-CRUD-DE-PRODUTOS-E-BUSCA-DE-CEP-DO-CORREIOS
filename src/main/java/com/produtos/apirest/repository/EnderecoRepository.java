package com.produtos.apirest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.produtos.apirest.models.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long>  {
	public Optional<Endereco> findById(Long id);
	Endereco findByCep(String cep);
	
}
