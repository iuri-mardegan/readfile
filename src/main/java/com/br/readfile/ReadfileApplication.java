package com.br.readfile;

import com.br.readfile.modelo.leitura.ModeloDefault;
import com.br.readfile.service.ProcessFile;

import static java.lang.Thread.sleep;

public class ReadfileApplication {

    public static void main(String[] args) {
        while (true) {
            ProcessFile processFile = new ProcessFile();
            processFile.read(new ModeloDefault());
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
