package com.datapoamobilidade.dto;

import com.datapoamobilidade.entity.Itinerario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItinerarioDto implements Serializable {

    private Long id;
    private Double latitude;
    private Double longitude;
    private Long idLinha;
    private Double raio;

    public ItinerarioDto(Long id, Double latitude, Double longitude, Long idLinha) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.idLinha = idLinha;
    }

    public static ItinerarioDto valueOf(Itinerario itinerario) {
        return new ItinerarioDto(itinerario.getId(),
                itinerario.getLatitude(),
                itinerario.getLongitude(),
                itinerario.getIdLinha());
    }
    public Itinerario valueOf() {
        return new Itinerario(getId(), getLatitude(), getLongitude(), getIdLinha());
    }
}
