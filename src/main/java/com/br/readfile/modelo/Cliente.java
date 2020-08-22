package com.br.readfile.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Cliente {
    private String cnpj;
    private String name;
    private String businessArea;
}
