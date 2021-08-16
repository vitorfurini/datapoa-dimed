package com.datapoamobilidade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinhaDto implements Serializable {

    private Long id;
    private String codigo;
    private String nome;
}
