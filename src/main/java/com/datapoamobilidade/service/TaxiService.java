package com.datapoamobilidade.service;

import com.datapoamobilidade.entity.Taxi;
import com.datapoamobilidade.repository.TaxiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class TaxiService {

    private TaxiRepository taxiRepository;

    @Autowired
    public TaxiService(TaxiRepository taxiRepository) {
        this.taxiRepository = taxiRepository;
    }

    public Collection<Taxi> findAll() {
        return taxiRepository.findAll();
    }

    public Optional<Taxi> findById(Long id) {
        return taxiRepository.findById(id);
    }

    public Taxi save(Taxi pontoTaxi) {
        return taxiRepository.save(pontoTaxi);
    }

    public void delete(Optional<Taxi> pontoTaxi) throws DataAccessException {
        taxiRepository.delete(pontoTaxi.get());
    }

    public Collection<String> readTaxiTxt() throws IOException {
        int count = 0;
        List<String> list = new ArrayList<String>();
        String fileName = "Taxi.txt";
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
            BufferedWriter buffWrite = new BufferedWriter(new FileWriter(fileName, true));
            String write = "PONTO-TAXI-OTTO#-30.101303856089572#-51.22885836877973#2021-01-27T12:53:52.512584Z\n" +
                    "PONTO-TAXI-CAMPOS-VELHO#-30.095005085187562#-51.22639651651551#2021-01-27T13:30:00.512584Z\n" +
                    "PONTO-TAXI-ZAFFARI#-30.108933319700505#-51.227595900500376#2021-01-27T14:15:516.512584Z84Z";
            buffWrite.write(write);
            buffWrite.close();
        }
        BufferedReader buffRead = new BufferedReader(new FileReader(fileName));
        String read = "";
        while (true) {
            if (read != null) {
                count++;
            } else
                break;
            read = buffRead.readLine();
            if(read != "" && read != null) {
                list.add(read);
            }

        }
        return list;

    }

    public Collection<String> writeTaxiTxt(String txt) throws IOException {
        int count = 0;
        List<String> list = new ArrayList<String>();
        String fileName = "Taxi.txt";
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(fileName, true));
        BufferedReader buffRead = new BufferedReader(new FileReader(fileName));
        String read = buffRead.readLine();
        String write = txt;
        buffWrite.write("\n"+ write);
        while (true) {
            if (read != null) {
                count++;
            } else
                break;
            read = buffRead.readLine();
            if(read != "" && read != null) {
                list.add(read);
            }
        }

        buffRead.close();
        buffWrite.close();

        return list;

    }
}
