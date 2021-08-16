package com.datapoamobilidade.service;

import com.datapoamobilidade.constantes.Constantes;
import com.datapoamobilidade.dto.ItinerarioDto;
import com.datapoamobilidade.dto.LinhaDto;
import com.datapoamobilidade.entity.Itinerario;
import com.datapoamobilidade.repository.ItinerarioRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

@Service
public class ItinerarioService {

    private final ItinerarioRepository itinerarioRepository;

    @Autowired
    public ItinerarioService(ItinerarioRepository itinerarioRepository) {
        this.itinerarioRepository = itinerarioRepository;
    }

    public List<LinhaDto> rotas(Double latitude, Double longitude, Double raio) {

        List<LinhaDto> lista = new ArrayList<>();
        LinhaDto linhaDto;

        Double raioTerra = 6371.0;
        Double r = (raio/raioTerra);

        Double LatMin = latitude - r;
        Double LatMax = latitude + r;

        Double ArcoLng = asin(sin(r)/cos(latitude));

        Double LngMin = longitude - ArcoLng;
        Double LngMax = longitude + ArcoLng;

        OkHttpClient httpClient = new OkHttpClient();
        Response response = null;
        String retorno = null;

        Request request = new Request.Builder()
                .url(Constantes.URL_BASE + Constantes.ENDPOINT_LIST_LINHAS_ROTA + "((" +
                        LatMax + "," + LngMax + "), (" +
                        LatMin + "," + LngMin + ")))")
                .get()
                .build();

        try {
            response = httpClient.newCall(request).execute();

            retorno = response.body().string();

            if (retorno.contains("encontrada")) {
                return Collections.EMPTY_LIST;
            }

            JSONArray jsonArrayRouter = new JSONArray(retorno);

            JSONArray jsonArrayLinhas = null;
            JSONObject jsonObjectLinha = null;
            JSONObject json = null;
            for (int i = 0; jsonArrayRouter.length() > i; i++) {
                json = jsonArrayRouter.getJSONObject(i);

                jsonArrayLinhas = json.getJSONArray("linhas");

                if (jsonArrayLinhas.length() > 0) {
                    for(int j = 0; jsonArrayLinhas.length() > j; j++) {
                        jsonObjectLinha = jsonArrayLinhas.getJSONObject(j);
                        linhaDto = new LinhaDto();

                        linhaDto.setId(Long.valueOf(jsonObjectLinha.get("idLinha").toString()));
                        linhaDto.setCodigo(jsonObjectLinha.get("codigoLinha").toString());
                        linhaDto.setNome(jsonObjectLinha.get("nomeLinha").toString());

                        lista.add(linhaDto);
                    }
                }
            }
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }

        return lista;
    }

    /*
        Busca Itinerarios de linhas de ônibus via integração PoaTransporte
     */
    public List<ItinerarioDto> findByIdLinha(String idLinha) {

        OkHttpClient httpClient = new OkHttpClient();
        Response response = null;
        List<ItinerarioDto> lista = new ArrayList<>();
        String retorno = null;

        Request request = new Request.Builder()
                .url(Constantes.URL_BASE + Constantes.ENDPOINT_LIST_ITINERARIO_POR_LINHA + idLinha)
                .get()
                .build();

        try {
            response = httpClient.newCall(request).execute();

            retorno = response.body().string();

            if (retorno.contains("encontrada")) {
                return Collections.EMPTY_LIST;
            }

            JSONObject jsonObjectLinha = null;
            JSONObject jsonObjectRouter = null;
            JSONArray jsonArrayRouter = new JSONArray();
            HashMap<String, Object> map = null;

            TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};

            ObjectMapper mapper = new ObjectMapper();
            map = mapper.readValue(retorno, typeRef);

            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();

                if (jsonObjectRouter == null && (!key.equals("idlinha") &&
                        !key.equals("codigo")  &&
                        !key.equals("nome"))) {
                    jsonObjectRouter = new JSONObject();

                    jsonObjectRouter.put(key, entry.getValue());
                    jsonArrayRouter.put(jsonObjectRouter);

                    jsonObjectRouter = null;
                }

                if (entry.getValue() instanceof LinkedHashMap) {
                    LinkedHashMap<String, Object> value = (LinkedHashMap<String, Object>) entry.getValue();

                    for(Map.Entry<String, Object> inner : value.entrySet()) {
                        String innerKey = inner.getKey();
                        Object values = inner.getValue();

                        if (innerKey.equals("lat") || innerKey.equals("lng")) {
                            if (jsonObjectRouter == null) {
                                jsonObjectRouter = new JSONObject();
                            }

                            jsonObjectRouter.put(innerKey, values);
                            jsonArrayRouter.put(jsonObjectRouter);

                            jsonObjectRouter = null;
                        }
                    }
                }
            }

            if (jsonArrayRouter != null) {
                jsonObjectLinha = new JSONObject();

                jsonObjectLinha.put("idlinha", idLinha);

                jsonObjectLinha.put("Routers", jsonArrayRouter);
            }

            JSONObject jsonObject = null;
            ItinerarioDto itinerarioDto = null;
            for (int i = 0; jsonArrayRouter.length() > i; i++) {
                jsonObject = jsonArrayRouter.getJSONObject(i);

                if (itinerarioDto == null) {
                    itinerarioDto = new ItinerarioDto();
                }

                itinerarioDto.setIdLinha(jsonObjectLinha.getLong("idlinha"));

                if (jsonObject.has("lat")) {
                    itinerarioDto.setLatitude(jsonObject.getDouble("lat"));
                }

                if (jsonObject.has("lng")) {
                    itinerarioDto.setLongitude(jsonObject.getDouble("lng"));
                }

                if (!jsonObject.has("lat") && !jsonObject.has("lng")) {
                    String str = jsonObject.names().get(0).toString();
                    Long id = Long.valueOf(str) + 1;
                    itinerarioDto.setId(id);
                }

                if (itinerarioDto.getIdLinha()   != null &&
                        itinerarioDto.getLatitude()  != null &&
                        itinerarioDto.getLongitude() != null) {
                    lista.add(itinerarioDto);

                    itinerarioDto = null;
                }
            }

            return lista;
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }

    }

    /*
        Verifica se itinerario já existe
     */
    public boolean existByItinerario(ItinerarioDto ItinerarioDto) {
        List<ItinerarioDto> lista = this.findByIdLinha(ItinerarioDto.getIdLinha().toString());

        boolean foundIntegracao = (lista.size() > 0 ? true : false);
        boolean found = true;

        if (foundIntegracao) {

            for(ItinerarioDto dto : lista) {
                if (ItinerarioDto.getLatitude() != dto.getLatitude()) {
                    found = false;
                    break;
                }

                if (ItinerarioDto.getLongitude() != dto.getLongitude()) {
                    found = false;
                    break;
                }
            }

            foundIntegracao = found;
        }

        return foundIntegracao;
    }

    /*
        Busca itinerario de linha de ônibus por ID
     */
    public Optional<Itinerario> findById(Long id) {

        Optional<Itinerario> itinerario = null;

        try {
            itinerario = itinerarioRepository.findById(id);
        } catch (Exception e) {
            return Optional.empty();
        }

        return itinerario;
    }


    public ItinerarioDto findByLinhaDataBank(ItinerarioDto itinerarioDto) {
        return itinerarioRepository.findByLinha(itinerarioDto.getIdLinha());
    }

    /*
        Salva dados de novo itinerario na base de dados
     */
    public Itinerario save(ItinerarioDto itinerarioDto) {
        return itinerarioRepository.save(itinerarioDto.valueOf());
    }

    /*
        Método usado para DELETE - via DELETE
     */
    public void delete(Long id) {
        Itinerario itinerario = this.findById(id).get();
        itinerarioRepository.delete(itinerario);
    }

    public String validarAtributos(ItinerarioDto ItinerarioDto, String operacao) {

        String message = null;

        try {
            if (operacao.equals("UPDATE")) {
                if (ItinerarioDto.getId() <= 0) {
                    message = "Informe ID do Itinerario.";
                }
            }
            if (!operacao.equals("ROTA")) {
                if (ItinerarioDto.getIdLinha() <= 0 && message == null) {
                    message = "Informe ID da Linha.";
                }
            }
            if (ItinerarioDto.getLatitude() == 0 && message == null) {
                message = "Informe a Latitude.";
            }
            if (ItinerarioDto.getLongitude() == 0  && message == null) {
                message = "Informe a Longitude.";
            }
            if (operacao.equals("ROTA") && message == null) {
                if (ItinerarioDto.getRaio() == 0) {
                    message = "Informa o raio em KM.";
                }
            }
        } catch (Exception e) {
            message = e.getMessage();
        }

        return message;
    }
}
