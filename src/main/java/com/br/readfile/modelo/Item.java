package com.br.readfile.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    private String id;
    private String quantity;
    private String price;
}
