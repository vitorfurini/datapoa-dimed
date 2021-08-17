package com.datapoamobilidade.service;

import com.datapoamobilidade.entity.Taxi;
import com.datapoamobilidade.repository.TaxiRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaxiServiceTest {

    @Mock
    TaxiService taxiService;

    @Mock
    TaxiRepository taxiRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.taxiService = new TaxiService(taxiRepository);
    }

    @Test
    public void findAll() {

        Taxi taxi = new Taxi();
        taxi.setNomeDoPonto("pontoTaxi");
        taxi.setLatitude(-30.02128257730300000);
        taxi.setLongitude(-51.18255810938000000);

        List<Taxi> list = new ArrayList<>();

        list.add(taxi);

        when(taxiRepository.findAll()).thenReturn(list);

        List<Taxi> collection = taxiService.findAll();

        assertFalse(collection.isEmpty());
    }

    @Test
    public void findById() {

        Optional<Taxi> taxiOptional = Optional.of(new Taxi());

        when(taxiRepository.findById(123L)).thenReturn(taxiOptional);

        taxiService.findById(123L);

        verify(taxiRepository, times(1)).findById(123L);
    }

    @Test
    public void save() {
        Taxi taxi = new Taxi();

        when(taxiRepository.save(any())).thenReturn(taxi);

        taxiService.save(taxi);

        verify(taxiRepository, times(1)).save(taxi);
    }

    @Test
    public void delete() {
        Taxi taxi = new Taxi();

        when(taxiRepository.findById(123L)).thenReturn(Optional.of(taxi));

        taxiService.delete(123L);

        verify(taxiRepository, times(1)).deleteById(taxi.getId());

    }

}