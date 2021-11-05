package br.com.alura.forum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Topico;

//AQUI JÁ TEM UM MONTE DE METODO PRONTO NO JPA
public interface TopicoRepository extends JpaRepository<Topico, Long> {

	//ESTOU PEGANDO RELACIONAMENTO CURSO E DENTRO DO CURSO TEM O ATRIBUTO NOME
	Page<Topico> findByCursoNome(String nomeCurso, Pageable paginacao);


}
