package com.datapoamobilidade.service;

import com.datapoamobilidade.dto.LinhaDto;
import com.datapoamobilidade.entity.Linha;
import com.datapoamobilidade.query.LinhaQuery;
import com.datapoamobilidade.repository.LinhaRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinhaServiceTest {

    @Mock
    LinhaService linhaService;

    @Mock
    LinhaRepository linhaRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.linhaService = new LinhaService(linhaRepository);
    }

    @Test
    public void getAll() {

        List<LinhaDto> list = linhaService.getAll();

        Assertions.assertFalse(list.isEmpty());
    }

    @Test
    public void findByName() {

        List<LinhaDto> list1 = linhaService.findByName("liberal");

        Assert.assertFalse(list1.isEmpty());
    }

    @Test
    public void findByNome() {

        List<LinhaQuery> linhaQueryList = new ArrayList<>();

        when(linhaRepository.findByNome(any())).thenReturn(linhaQueryList);

        linhaService.findByNome(any());

        verify(linhaRepository, times(1)).findByNome(any());
    }

    @Test
    public void findById() {

        when(linhaRepository.findById(any())).thenReturn(any());

        linhaService.findById(1L);

        verify(linhaRepository, times(1)).findById(any());
    }

    @Test
    public void findByCode() {

        List<LinhaQuery> linhaQueryList = new ArrayList<>();

        when(linhaRepository.findByNome(any())).thenReturn(linhaQueryList);

        List<LinhaDto> list = linhaService.findByCode("4321-1");

        assertTrue(list.size() > 0);
    }

    @Test
    public void delete() {

        Linha linha = new Linha();

        when(linhaRepository.findById(123L)).thenReturn(java.util.Optional.of(linha));

        linhaService.delete(123L);

        verify(linhaRepository, times(1)).delete(linha);
    }

    @Test
    public void createLinha() {
        LinhaDto linhaDto = new LinhaDto();

        linhaDto.setCodigo("1234");
        linhaDto.setId(1L);
        linhaDto.setNome("teste");

        linhaService.createLinha(linhaDto);

        assertEquals(1L, (long) linhaDto.getId());
    }

    @Test
    public void update() {

        LinhaDto linhaDto = new LinhaDto();

        linhaDto.setCodigo("1234");
        linhaDto.setId(1L);
        linhaDto.setNome("teste");

        linhaService.update(linhaDto);

        assertEquals(1L, (long) linhaDto.getId());
    }

    @Test
    public void existByCodeAndName() {

        LinhaDto linhaDto = new LinhaDto();
        linhaDto.setCodigo("1234");
        linhaDto.setId(1L);
        linhaDto.setNome("teste");

        when(linhaRepository.existsByNome(any())).thenReturn(true);
        when(linhaRepository.existsByCodigo(any())).thenReturn(true);

        linhaService.existByCodeAndName(linhaDto);

        verify(linhaRepository, times(1)).existsByCodigo("1234");
        verify(linhaRepository, times(1)).existsByNome("teste");

    }

    @Test
    public void existsByCodigo() {
        when(linhaRepository.existsByCodigo(any())).thenReturn(true);
        linhaService.existsByCodigo("1234");
        verify(linhaRepository, times(1)).existsByCodigo("1234");
    }

    @Test
    public void existsByNome() {
        when(linhaRepository.existsByNome(any())).thenReturn(true);
        linhaService.existsByNome("1234");
        verify(linhaRepository, times(1)).existsByNome("1234");
    }

}