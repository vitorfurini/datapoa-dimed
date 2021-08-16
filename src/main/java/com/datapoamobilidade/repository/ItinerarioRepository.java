package com.datapoamobilidade.repository;

import com.datapoamobilidade.dto.ItinerarioDto;
import com.datapoamobilidade.entity.Itinerario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItinerarioRepository extends CrudRepository<Itinerario, Long> {

    @Query("SELECT new com.datapoamobilidade.dto.ItinerarioDto(it.id, it.latitude, it.longitude, it"
            + ".idLinha) " +
            "FROM Itinerario it " +
            "WHERE it.idLinha = :idLinha")
    ItinerarioDto findByLinha(Long idLinha);

    boolean existsByIdLinha(Long idLinha);

}
