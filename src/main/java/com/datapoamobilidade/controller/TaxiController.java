package com.datapoamobilidade.controller;

import com.datapoamobilidade.entity.Taxi;
import com.datapoamobilidade.service.TaxiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pontotaxi")
@Api(value = "API REST Taxi Controller")
public class TaxiController {

    @Autowired
    private TaxiService taxiService;

    @ApiOperation(value = "Salva ou atualiza um ponto de taxi no Database. Se o ID for existente, atualiza nome e código. Se não houver o ID informado, cadastra uma nova linha.")
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> saveTaxi(@RequestBody Taxi taxi) {
        Collection<Taxi> list = taxiService.findAll();
        boolean exist = false;
        for (Taxi l : list) {
            if (l.getId().equals(taxi.getId())) {
                exist = true;
                break;
            }
        }

        Taxi pt = new Taxi();

        if (!exist) {
            if (!taxi.getNomeDoPonto().isEmpty() && taxi.getLatitude() != null && taxi.getLongitude() != null) {
                pt = taxiService.save(taxi);
                return new ResponseEntity<Taxi>(pt, HttpStatus.CREATED);
            }
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        } else {
            if (!taxi.getNomeDoPonto().isEmpty() && taxi.getLatitude() != null && taxi.getLongitude() != null) {
                pt = taxiService.save(taxi);
                return new ResponseEntity<Taxi>(pt, HttpStatus.OK);
            }
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);

        }
    }

    @ApiOperation(value = "Deleta ponto de taxi do  Database a partir de seu ID.")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Void> deleteTaxi(@PathVariable("id") Long id) {
        taxiService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Retorna uma lista de pontos de taxi que foram salvos no  Database.")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Collection<Taxi>> getAllTaxi() {
        Collection<Taxi> list = new ArrayList<>();
        list = taxiService.findAll();
        if (list == null) {
            return new ResponseEntity<Collection<Taxi>>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Collection<Taxi>>(list, HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna uma lista de pontos de taxi que foram salvos no  Database, a partir de seu ID.")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Taxi> findById(@PathVariable Long id) throws IOException {
        Optional<Taxi> taxiById = Optional.of(new Taxi());
        taxiById = taxiService.findById(id);

        taxiService.readTaxiTxt();

        return new ResponseEntity<>(taxiById.get(), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna lista de pontos de taxi existente no arquivo TXT.")
    @RequestMapping(value = "/txt", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Collection<String>> getAllTxtTaxi() throws IOException {
        Collection<String> list = new ArrayList<>();

        list = taxiService.readTaxiTxt();

        return new ResponseEntity<Collection<String>>(list, HttpStatus.OK);
    }

    @ApiOperation(value = "Insere ponto de taxi na lista de pontos existentes no TXT e a retorna a lista atualizada. Parâmetro - String no formato: NOME_DO_PONTO#LATITUDE#LONGITUDE#DATA_HORA_CADASTRO.")
    @RequestMapping(value = "/txt/save", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Collection<String>> getTxtTaxi(@RequestParam String pt) throws IOException {
        Collection<String> list = new ArrayList<>();

        list = taxiService.writeTaxiTxt(pt);
        list = taxiService.readTaxiTxt();

        return new ResponseEntity<Collection<String>>(list, HttpStatus.OK);
    }
}
