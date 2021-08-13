package com.datapoamobilidade.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LinhaDto implements Serializable {

    private Long id;
    private String codigo;
    private String nome;
}
