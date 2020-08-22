package com.br.readfile.service;

import com.br.readfile.modelo.Cliente;
import com.br.readfile.modelo.Item;
import com.br.readfile.modelo.Venda;
import com.br.readfile.modelo.Vendedor;
import com.br.readfile.modelo.leitura.ModeloAbstract;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ProcessOutputFile {


    public static void writeFile(String fileName,
                                 List<Vendedor> vendedorList,
                                 List<Cliente> clienteList,
                                 List<Venda> vendaList) {
        String[] fileNameArray = fileName.split("\\.",2);
        String PATH_OUT = "data/out/";
        fileName = PATH_OUT + fileNameArray[0] + ".done." + fileNameArray[1];
        File myObj = new File(fileName);
        try {
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("Clientes importados: "+clienteList.size());
            myWriter.write("\nVendedores importados: "+vendedorList.size());
            myWriter.write("\nId maior venda: "+ maiorVenda(vendaList));
            myWriter.write("\nPior Vendedor: "+piorVendedor(vendaList));
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Integer maiorVenda(List<Venda> vendaList){
        Collections.sort(vendaList);
        return vendaList.get(vendaList.size() - 1).getSaleId();
    }

    private static String piorVendedor(List<Venda> vendaList){
        Map<String, Double> vendasMap = new HashMap<>();
        vendaList.forEach(v ->{
            if(vendasMap.containsKey(v.getSalesmanName())){
                vendasMap.put(v.getSalesmanName(),
                        vendasMap.get(v.getSalesmanName()) +
                        v.getItemList().stream().map(Item::getPrice)
                                .reduce(0.0, Double::sum));
            }else{
                vendasMap.put(v.getSalesmanName(),
                        v.getItemList().stream().map(Item::getPrice)
                                .reduce(0.0, Double::sum));
            }
        });
        vendasMap.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Double>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return vendasMap.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .get().getKey();
    }
}
