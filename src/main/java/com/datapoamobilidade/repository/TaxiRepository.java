package com.datapoamobilidade.repository;

import com.datapoamobilidade.entity.Taxi;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxiRepository extends JpaRepository<Taxi, Long> {

    List<Taxi> findAll();
    Optional<Taxi> findById(Long id);
    Taxi save(Taxi pontoTaxi);
    void delete(Taxi pontoTaxi) throws DataAccessException;
}
