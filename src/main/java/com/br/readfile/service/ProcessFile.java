package com.br.readfile.service;

import com.br.readfile.modelo.Cliente;
import com.br.readfile.modelo.Item;
import com.br.readfile.modelo.Venda;
import com.br.readfile.modelo.Vendedor;
import com.br.readfile.modelo.leitura.ModeloAbstract;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ProcessFile {

    private final String PATH_IN = "data/in/";
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
        for (String f : Objects.requireNonNull(path.list((dir, name) -> name.endsWith(modelo.getExtension())))) {
            processFile(modelo, f);
        }
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
            ProcessOutputFile.writeFile(fileName, vendedorList, clienteList, vendaList);
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

}
