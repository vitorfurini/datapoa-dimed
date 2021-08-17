package com.datapoamobilidade.controller;

import com.datapoamobilidade.dto.ItinerarioDto;
import com.datapoamobilidade.dto.LinhaDto;
import com.datapoamobilidade.service.ItinerarioService;
import exceptions.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/v1/itinerarios")
@Api(value = "API REST Itinerario Controller")
public class ItinerarioController {

    private final ItinerarioService itinerarioService;

    private ItinerarioDto dto;
    private String message;

    @Autowired
    public ItinerarioController(ItinerarioService itinerarioService) {
        this.itinerarioService = itinerarioService;
    }

    @GetMapping("/buscarRotas")
    @ApiOperation(value = "Listagem de Linhas de Ônibus determinadas por Latitude Minima, Longitude Minima e raio em KM")
    ResponseEntity<List<LinhaDto>> buscarRotas(@Valid @RequestParam(name = "latitude") Double latitude,
            @RequestParam(name = "longitude") Double longitude,
            @RequestParam(name = "raio") Double raio) {

        try {
            dto = new ItinerarioDto();

            dto.setLatitude(latitude);
            dto.setLongitude(longitude);
            dto.setRaio(raio);

            if ((message = itinerarioService.validarAtributos(dto, "ROTA")) != null) {
                throw new BusinessException(message);
            }

            List<LinhaDto> listaLinhas = itinerarioService.rotas(latitude, longitude, raio);

            if (listaLinhas.isEmpty()) {
                throw new BusinessException("Nenhuma linha encontrada nos dados informados.");
            }

            return new ResponseEntity<>(listaLinhas, HttpStatus.OK);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

    }

    @PostMapping("/buscarPorLinha/{idlinha}")
    @ApiOperation(value = "Listagem de Itinerário por determinada Linha.")
    ResponseEntity<List<ItinerarioDto>> buscarPorLinha(@Valid @RequestParam(name = "idlinha") String idlinha) {

        try {
            List<ItinerarioDto> lista = itinerarioService.findByIdLinha(idlinha);

            if (lista == null || lista.size() == 0) {
                throw new BusinessException("Não foram encontrados itineários para a linha de ônibus.");
            }

            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

    }

    @PostMapping("/create")
    @ApiOperation(value = "Criação de itinerario de Linha de Ônibus.")
    ResponseEntity<ItinerarioDto> create(@Valid @RequestBody ItinerarioDto itinerarioDto) {

        try {
            if ((message = itinerarioService.validarAtributos(itinerarioDto, "CREATE")) != null) {
                throw new BusinessException(message);
            }

            boolean found = itinerarioService.existByItinerario(itinerarioDto);
            dto = itinerarioService.findByLinhaDataBank(itinerarioDto);

            boolean founddatabank = true;

            if (found) {
                throw new BusinessException("Itinerario de Linha já cadastrada, dados de localização já cadastrados. Verifique os dados informados.");
            } else if (dto != null) {
                if (!dto.getLatitude().equals(itinerarioDto.getLatitude())) {
                    founddatabank = false;
                }
                if (!dto.getLongitude().equals(itinerarioDto.getLongitude())) {
                    founddatabank = false;
                }
                if (!founddatabank) {
                    throw new BusinessException("Dados de localização já cadastrados. Verifique os dados informados.");
                }
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        return new ResponseEntity<>(itinerarioService.save(itinerarioDto), HttpStatus.OK);
    }

    @PutMapping("/update")
    @ApiOperation(value = "Alteração de dados de Itinerario de Linha de Ônibus.")
    ResponseEntity<ItinerarioDto> update(@Valid @RequestBody ItinerarioDto ItinerarioDto) {

        try {
            if ((message = itinerarioService.validarAtributos(ItinerarioDto, "UPDATE")) != null) {
                throw new BusinessException(message);
            }

            dto = new ItinerarioDto();
            BeanUtils.copyProperties(ItinerarioDto, dto);

            if (itinerarioService.findById(dto.getId()).isEmpty()) {
                throw new BusinessException("Não foi encontrado Itinerario de linha de ônibus. Verifique os dados informados.");
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        return new ResponseEntity<>(itinerarioService.save(dto), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Exclusão de Itinerario de Linha de Ônibus.")
    ResponseEntity<String> delete(@Valid @PathVariable Long id) {

        try {
            if (itinerarioService.findById(id).isEmpty()) {
                throw new BusinessException("Não foi encontrado Itinerario de Linha de Ônibus. Verifique os dados informados.");
            } else {
                itinerarioService.delete(id);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        return new ResponseEntity<>("Itineario de Linha de Ônibus excluido com sucesso.", HttpStatus.OK);

    }
}
