package implementacaobd;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import static java.nio.file.Files.readAllLines;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mateus
 */
public class ImplementacaoBD {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // expressao regular: SELECT + qualquer quantidade de d, + d + qualquer quantidade de \n ou espaco + FROM + qlqr quaatidade de espaco ... e por ai vai
        // alguns parentenses definem os grupos, nao remova
        Pattern pattern = Pattern.compile("SELECT ((\\d,)*\\d)(\\n|\\s)+FROM(\\s)+(\\w+.txt)\\s+JOIN\\s+(\\w+.txt)\\s+ON\\s+(\\d+)=(\\d+)");
        String teste = "SELECT 1,2,3,4,5 \n FROM arq1.txt JOIN arq2.txt ON 4=5";
        Matcher matcher = pattern.matcher(teste);
        String valoresCSV = "";
        if (matcher.matches()) {
            System.out.println("\n grupo 1: " + matcher.group(1) + "\n grupo 2: " + matcher.group(5) + "\n grupo 3: " + matcher.group(6) + " \n grupo 4: " + matcher.group(7) + " \n grupo 5: " + matcher.group(8));
        } else {
            System.out.println("ERRO: Sintaxe errada");
        }
        // matcher.group(1) = valores do select
        // matcher.group(5) = nome do primeiro arquivo
        // matcher.group(6) = nome do segundo arquivo
        // matcher.group(7) = nome do primeiro argumento do ON
        // matcher.group(8) = nome do segundo argumento do ON
        valoresCSV = matcher.group(1);
        String[] valoresSelectstring = valoresCSV.split(",");
        List<Integer> valoresSelectint = new ArrayList<Integer>();
        List<String> arquivo1Linhas = new ArrayList<String>();
        List<String> arquivo2Linhas = new ArrayList<String>();
        try {

            arquivo1Linhas = Files.readAllLines(Paths.get(matcher.group(5)), Charset.forName("Cp1252")); // abre arquivo 1, divide em linhas e coloca na lista
            arquivo2Linhas = Files.readAllLines(Paths.get(matcher.group(6)), Charset.forName("Cp1252")); // abre  arquivo 2, divide em linhas e coloca na lista
        } catch (IOException ex) {
            Logger.getLogger(ImplementacaoBD.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        List<List<String>> arquivo1 = new ArrayList<List<String>>(); 
        List<List<String>> arquivo2 = new ArrayList<List<String>>();
        int index = 0;
        for (String s : arquivo1Linhas) {
            arquivo1.add(Arrays.asList(s.split(","))); // pega cada linha do arquvo 1 e coloca em uma lista de string, cada string equivale a 1 coluna
        }
        for (String s : arquivo2Linhas) {
            arquivo2.add(Arrays.asList(s.split(","))); // pega cada linha do arquvo 2 e coloca em uma lista de string, cada string equivale a 1 coluna
        }

        Integer control = 0;
        for (String s : valoresSelectstring) {
            valoresSelectint.add(Integer.valueOf(s));
            if (control % 2 == 0) {
                if (Integer.valueOf(s) > arquivo1.get(0).size()) {
                    System.out.println("ERRO: Coluna select maior que o possivel");
                }
            }
            if (control % 2 == 1) {
               if (Integer.valueOf(s)> arquivo2.get(0).size()) {
                    System.out.println("ERRO: Coluna select maior que o possivel");
               }
            }
            control++;
        }
        Integer on1 = Integer.valueOf(matcher.group(7));
        Integer on2 = Integer.valueOf(matcher.group(8));
        if (on1 > arquivo1.get(0).size()) {
            System.out.println("ERRO: ON com coluna maior que o possivel no arquvo 1");
        }
        if (on2 > arquivo2.get(0).size()) {
            System.out.println("ERRO: ON com coluna maior que o possivel no arquvo 2");
        }

    }

}