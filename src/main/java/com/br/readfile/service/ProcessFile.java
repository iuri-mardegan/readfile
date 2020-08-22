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

public class ProcessFile {

    private final String PATH_IN = "data/in/";
    private final String PATH_OUT = "data/out/";
    private final String VENDEDOR = "001";
    private final String CLIENTE = "002";
    private final String VENDA = "003";

    private List<Vendedor> vendedorList;
    private List<Cliente> clienteList;
    private List<Venda> vendaList;

    private void inicializa() {
        vendedorList = new ArrayList<>();
        clienteList = new ArrayList<>();
        vendaList = new ArrayList<>();
    }

    public void read(ModeloAbstract modelo) {
        File path = new File(PATH_IN);
        Arrays.stream(Objects.requireNonNull(path.list()))
                .forEach(f -> processFile(modelo, f));
    }

    private void processFile(ModeloAbstract modelo, String fileName) {
        try {
            inicializa();
            File file = new File(PATH_IN + fileName);

            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                processData(data.split(modelo.getSeparador()));
            }
            myReader.close();
            file.delete();
            writeFile(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processData(String[] data) {
        switch (data[0]) {
            case VENDEDOR:
                vendedorList.add(new Vendedor(data[1], data[2], data[3]));
                break;
            case CLIENTE:
                clienteList.add(new Cliente(data[1], data[2], data[3]));
                break;
            case VENDA:
                vendaList.add(new Venda(Integer.parseInt(data[1]), generateItemList(data[2]), data[3]));
                break;
            default:
                break;
        }
    }

    private List<Item> generateItemList(String itemListString) {
        List<Item> itemList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(itemListString);
            for (int i = 0; i < jsonArray.length(); i++) {
                String itemValue = (String) jsonArray.get(i);
                String[] itemValueArray = itemValue.split("-");
                itemList.add(new Item(itemValueArray[0], Integer.parseInt(itemValueArray[1]), Double.parseDouble(itemValueArray[2])));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return itemList;
    }

    private void writeFile(String fileName) {
        String[] fileNameArray = fileName.split("\\.",2);
        fileName = PATH_OUT + fileNameArray[0] + ".done." + fileNameArray[1];
        File myObj = new File(fileName);
        try {
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("Clientes importados: "+clienteList.size());
            myWriter.write("\nVendedores importados: "+vendedorList.size());
            myWriter.write("\nId maior venda: "+maiorVenda());
            myWriter.write("\nPior Vendedor: "+piorVendedor());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer maiorVenda(){
        Collections.sort(vendaList);
        return vendaList.get(vendaList.size() - 1).getSaleId();
    }

    private String piorVendedor(){
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
                .min(Comparator.comparing(Map.Entry::getValue))
                .get().getKey();
    }
}
