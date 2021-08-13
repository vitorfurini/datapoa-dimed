package com.datapoamobilidade.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ItinerarioDto implements Serializable {

    private Long id;
    private Double latitude;
    private Double longitude;
    private Long idLinha;
    private Double raio;
}
