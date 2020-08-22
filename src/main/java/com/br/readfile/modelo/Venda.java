package com.br.readfile.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Venda implements Comparable<Venda> {
    private Integer saleId;
    private List<Item> itemList;
    private String salesmanName;

    @Override
    public int compareTo(Venda o) {
        return (this.itemList.stream().map(i -> i.getPrice())
                .reduce(0.0, Double::sum)
                .compareTo(
                o.getItemList().stream().map(i -> i.getPrice())
                .reduce(0.0, Double::sum)));
    }
}
