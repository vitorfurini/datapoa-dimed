package com.datapoamobilidade.service;

import com.datapoamobilidade.constantes.Constantes;
import com.datapoamobilidade.dto.LinhaDto;
import com.datapoamobilidade.entity.Linha;
import com.datapoamobilidade.enums.OperacaoEnum;
import com.datapoamobilidade.repository.LinhaRepository;
import exceptions.BusinessException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class LinhaService {

    private final LinhaRepository linhaRepository;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private OkHttpClient httpClient;
    private Response response;
    private List<LinhaDto> lista;
    private Request request;
    private String message;

    @Autowired
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public LinhaService(LinhaRepository linhaRepository) {
        this.linhaRepository = linhaRepository;
    }

    public List<LinhaDto> getAll() {
        httpClient = new OkHttpClient();
        response = null;
        lista = new ArrayList<LinhaDto>();

        request = new Request.Builder()
                .url(Constantes.URL_BASE + Constantes.ENDPOINT_LIST_LINHAS)
                .get()
                .build();

        try {
            response = httpClient.newCall(request).execute();

            String retorno = response.body().string();

            JSONArray jsonArray = new JSONArray(retorno);

            JSONObject jsonObject = null;
            for (int i = 0; jsonArray.length() > i; i++) {
                jsonObject = jsonArray.getJSONObject(i);

                lista.add(new LinhaDto(jsonObject.getLong("id"),
                        jsonObject.getString("codigo"),
                        jsonObject.getString("nome")));
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }

        return lista;
    }

    public List<LinhaDto> findByName(String name) {

        httpClient = new OkHttpClient();
        response = null;
        lista = new ArrayList<>();

        request = new Request.Builder()
                .url(Constantes.URL_BASE + Constantes.ENDPOINT_FIND_LINHA_POR_NOME + name)
                .get()
                .build();

        try {
            response = httpClient.newCall(request).execute();

            String retorno = Objects.requireNonNull(response.body()).string();

            JSONArray jsonArray = new JSONArray(retorno);

            JSONObject jsonObject = null;
            for (int i = 0; jsonArray.length() > i; i++) {
                jsonObject = jsonArray.getJSONObject(i);

                lista.add(new LinhaDto(jsonObject.getLong("id"),
                        jsonObject.getString("codigo"),
                        jsonObject.getString("nome")));
            }

        } catch (Exception e) {
            if (lista.size() == 0) {
                lista = this.findByNome(name);
            }

            if (lista.size() == 0) {
                return Collections.emptyList();
            }
        }

        return lista;
    }

    public List<LinhaDto> findByNome(String name) {
        var found = linhaRepository.findByNome(name);
        if (found.size() > 0) {
            return found.stream().map(m -> new LinhaDto(m.getId(), m.getCodigo(), m.getNome())).collect(Collectors.toList());
        } else return new ArrayList<>();
    }


    /*
        Busca linha de ônibus por ID
     */
    public Optional<Linha> findById(Long id) {

        Optional<Linha> linha;

        try {
            linha = linhaRepository.findById(id);
        } catch (Exception e) {
            return Optional.empty();
        }

        return linha;
    }

    public List<LinhaDto> findByCode(String code) {

        httpClient = new OkHttpClient();
        response = null;
        lista = new ArrayList<LinhaDto>();

        request = new Request.Builder()
                .url(Constantes.URL_BASE + Constantes.ENDPOINT_FIND_LINHA_POR_CODIGO + code)
                .get()
                .build();

        try {
            response = httpClient.newCall(request).execute();

            String retorno = Objects.requireNonNull(response.body()).string();

            JSONArray jsonArray = new JSONArray(retorno);

            JSONObject jsonObject = null;
            for (int i = 0; jsonArray.length() > i; i++) {
                jsonObject = jsonArray.getJSONObject(i);

                lista.add(new LinhaDto(jsonObject.getLong("id"),
                        jsonObject.getString("codigo"),
                        jsonObject.getString("nome")));
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }

        return lista;
    }

    @Transactional
    public void delete(Long id) {
        Optional<Linha> linha = linhaRepository.findById(id);

        linha.ifPresent(linhaRepository::delete);

    }

    @Transactional
    public LinhaDto createLinha(LinhaDto linhaDto) {
        var linhaSave = new Linha(linhaDto.getId(), linhaDto.getCodigo(), linhaDto.getNome());
        var findExists = this.existByCodeAndName(linhaDto);

        if((message = this.validarAtributos(linhaDto, "CREATE")) != null) {
            throw new BusinessException(message);
        }
        else if(findExists) {
            throw new BusinessException("Código ou nome já cadastrados. Verifique os dados novamente");
        }
        else {
            linhaRepository.save(linhaSave);
        }
        return modelMapper.map(linhaSave, LinhaDto.class);
    }

    public LinhaDto update(LinhaDto linhaDto) {

        var linhaUpdate = linhaRepository.findById(linhaDto.getId());

        if((message = this.validarAtributos(linhaDto, "UPDATE")) != null) {
            throw new BusinessException(message);
        }

        try {
            if (linhaUpdate.isPresent()) {
                linhaUpdate.orElseThrow().setCodigo(linhaDto.getCodigo());
                linhaUpdate.orElseThrow().setId(linhaDto.getId());
                linhaUpdate.orElseThrow().setNome(linhaDto.getNome());
                linhaRepository.save(linhaUpdate.get());
            }
            else {
                throw new BusinessException("Não foi encontrada Linha de Ônibus. Verifique os dados informados.");
            }
        } catch (Exception e) {
            log.error("Um erro inesperado ocorreu ao atualizar uma nova linha de onibus" + e.getMessage());
        }
        return modelMapper.map(linhaUpdate.isPresent(), LinhaDto.class);
    }

    public boolean existByCodeAndName(LinhaDto linhaDto) {
        boolean foundName = (this.findByName(linhaDto.getNome()).size() > 0);
        boolean foundCode = (this.findByCode(linhaDto.getCodigo()).size() > 0);
        boolean foundNameDataBank = (this.existsByNome(linhaDto.getNome()));
        boolean foundCodeDataBank = (this.existsByCodigo(linhaDto.getCodigo()));

        if (foundName) {
            return foundName;
        } else if (foundCode) {
            return foundCode;
        } else if (foundNameDataBank) {
            return foundNameDataBank;
        } else if (foundCodeDataBank) {
            return foundCodeDataBank;
        } else {
            return false;
        }
    }

    public boolean existsByCodigo(String codigo) {
        return linhaRepository.existsByCodigo(codigo);
    }

    public boolean existsByNome(String nome) {
        return linhaRepository.existsByNome(nome);
    }

    public String validarAtributos(LinhaDto linhaDTO, String operacao) {

        String message = null;

        try {

            if (operacao.equals(OperacaoEnum.UPDATE.getDescription())) {
                if (linhaDTO.getId() <= 0) {
                    message = "Informe ID da Linha.";
                }
            }
            if ((linhaDTO.getCodigo().isEmpty() || linhaDTO.getCodigo().length() == 0) && message == null) {
                message = "Informe CÓDIGO da Linha.";
            }
            if ((linhaDTO.getNome().isEmpty() || linhaDTO.getNome().length() == 0) && message == null) {
                message = "Informe NOME da Linha.";
            }
        } catch (Exception e) {
            message = e.getMessage();
        }

        return message;
    }
}
