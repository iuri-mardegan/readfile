package com.br.readfile;

import com.br.readfile.modelo.leitura.ModeloDefault;
import com.br.readfile.service.ProcessFile;

public class ReadfileApplication {

    public static void main(String[] args) {
        ProcessFile processFile = new ProcessFile();
        processFile.read(new ModeloDefault());
    }

}
