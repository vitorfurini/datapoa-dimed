package com.datapoamobilidade.repository;

import com.datapoamobilidade.entity.Linha;
import com.datapoamobilidade.query.LinhaQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinhaRepository extends JpaRepository<Linha, Long> {

    boolean existsByCodigo(String codigo);

    boolean existsByNome(String nome);

    List<LinhaQuery> findByNome(@Param("nome") String nome);

    List<LinhaQuery> findByNomeAndCodigo(@Param("nome") String nome, @Param("codigo") String codigo);
}