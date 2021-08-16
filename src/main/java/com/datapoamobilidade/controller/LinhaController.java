package com.datapoamobilidade.controller;

import com.datapoamobilidade.dto.LinhaDto;
import com.datapoamobilidade.service.LinhaService;
import exceptions.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping(value = "/api/v1/linha")
@Api(value = "API REST Linha Controller")
public class LinhaController {

    @Autowired
    LinhaService linhaService;

    @GetMapping("/list")
    @ApiOperation(value = "Listagem de Todas as Linha de Ônibus.")
    public ResponseEntity<List<LinhaDto>> getAll() {
        List<LinhaDto> linhaList = linhaService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(linhaList);
    }

    @GetMapping("/findByName/{nome}")
    @ApiOperation(value = "Filtro das Linhas de Ônibus por nome")
    public ResponseEntity<List<LinhaDto>> findByName(@RequestParam(value = "nome", required = true) String nome) {
        return ResponseEntity.ok(this.linhaService.findByName(nome));
    }

    @PostMapping("/create")
    @ApiOperation(value = "Criação de dados da Linha de Ônibus.")
    ResponseEntity<LinhaDto> create(@Valid @RequestBody LinhaDto linhaDto) {

        LinhaDto result = linhaService.createLinha(linhaDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/update")
    @ApiOperation(value = "Atualização dos dados de uma Linha de ônibus.")
    ResponseEntity<LinhaDto> update(@Valid @RequestBody LinhaDto linhaDto) {
        LinhaDto result = linhaService.update(linhaDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Exclusão de Linha de Ônibus.")
    ResponseEntity<String> delete(@Valid @PathVariable Long id) {

        try {
            if (linhaService.findById(id).isEmpty()) {
                throw new BusinessException("Não foi encontrada Linha de Ônibus. Verifique os dados informados.");
            } else {
                linhaService.delete(id);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return new ResponseEntity<>("Linha de Ônibus excluida com sucesso.", HttpStatus.OK);
    }

}
