package com.datapoamobilidade.service;

import com.datapoamobilidade.dto.ItinerarioDto;
import com.datapoamobilidade.dto.LinhaDto;
import com.datapoamobilidade.entity.Itinerario;
import com.datapoamobilidade.repository.ItinerarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItinerarioServiceTest {

    @Mock
    ItinerarioService itinerarioService;

    @Mock
    ItinerarioRepository itinerarioRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.itinerarioService = new ItinerarioService(itinerarioRepository);
    }

    @Test
    public void rotasTest() {
        List<LinhaDto> list = itinerarioService.rotas(-30.02128257730300000, -51.18255810938000000, 10.0);

        assertFalse(list.size() > 0);
    }

    @Test
    public void findByIdLinhaTest() {

        List<ItinerarioDto> list1 = itinerarioService.findByIdLinha("5566");

        assertFalse(list1.isEmpty());

    }

    @Test
    public void existByItinerario() {
        ItinerarioDto itinerarioDto = new ItinerarioDto();
        itinerarioDto.setId(4321 - 1L);
        itinerarioDto.setLatitude(-30.02077557730300000);
        itinerarioDto.setLongitude(-51.18157510938000000);
        itinerarioDto.setIdLinha(5566L);

        List<ItinerarioDto> itinerarioDtos = new ArrayList<>();
        itinerarioDtos.add(itinerarioDto);

        boolean teste = itinerarioService.existByItinerario(itinerarioDto);

        assertFalse(teste);
    }

    @Test
    public void findById() {
        Itinerario itinerario = new Itinerario();

        when(itinerarioRepository.findById(any())).thenReturn(Optional.of(itinerario));

        itinerarioService.findById(5566L);

        verify(itinerarioRepository, times(1)).findById(any());
    }

    @Test
    public void findByLinhaDataBank() {

        ItinerarioDto itinerarioDto = new ItinerarioDto();

        when(itinerarioRepository.findByLinha(any())).thenReturn(itinerarioDto);

        itinerarioService.findByLinhaDataBank(itinerarioDto);

        verify(itinerarioRepository, times(1)).findByLinha(any());
    }

    @Test
    public void save() {

        ItinerarioDto itinerarioDto = new ItinerarioDto();
        itinerarioDto.setId(4321 - 1L);
        itinerarioDto.setLatitude(-30.02077557730300000);
        itinerarioDto.setLongitude(-51.18157510938000000);
        itinerarioDto.setIdLinha(5566L);

        itinerarioService.save(itinerarioDto);

        verify(itinerarioRepository, times(1)).save(any());
    }

    @Test
    public void delete() {

        Itinerario itinerario = new Itinerario();

        when(itinerarioRepository.findById(123L)).thenReturn(java.util.Optional.of(itinerario));

        itinerarioService.delete(123L);

        verify(itinerarioRepository, times(1)).delete(itinerario);
    }

    @Test
    public void validarAtributos() {

    }


}