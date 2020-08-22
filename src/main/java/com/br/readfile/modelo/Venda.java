package com.br.readfile.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Venda {
    private String saleId;
    private List<Item> itemList;
    private String salesmanName;
}
