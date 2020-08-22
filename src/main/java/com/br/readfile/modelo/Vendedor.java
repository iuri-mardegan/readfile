package com.br.readfile.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vendedor {
    private String cpf;
    private String name;
    private String salary;
}
