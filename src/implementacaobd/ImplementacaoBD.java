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

        byte[] wikiArray;
        String teste="";
        try {
            wikiArray = Files.readAllBytes(Paths.get("consulta.txt"));
            teste = new String(wikiArray, "Cp1252");
        } catch (IOException ex) {
            Logger.getLogger(ImplementacaoBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        //  String teste = "SELECT 1,2,3,4,5 \n FROM arq1.txt JOIN arq2.txt ON 4=5"; se quiser testar, comenta ali em cima
        Matcher matcher = pattern.matcher(teste);

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
        String valoresCSV = matcher.group(1);
        String[] valoresSelectstring = valoresCSV.split(",");
        List<Integer> valoresSelectintarq1 = new ArrayList<Integer>();
         List<Integer> valoresSelectintarq2 = new ArrayList<Integer>();
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

        for (String s : arquivo1Linhas) {
            arquivo1.add(Arrays.asList(s.split(","))); // pega cada linha do arquvo 1 e coloca em uma lista de string, cada string equivale a 1 coluna
        }
        for (String s : arquivo2Linhas) {
            arquivo2.add(Arrays.asList(s.split(","))); // pega cada linha do arquvo 2 e coloca em uma lista de string, cada string equivale a 1 coluna
        }
        // arquivo1 e arquivo2 estao armazenados toda os arquivos em forma de matriz, para acessar arquivo1.get(linha).get(coluna) retorna a string
   
        Integer control = 0;
        for (String s : valoresSelectstring) {
            if (control % 2 == 0) { // o primeiro,terceiro,quinto... argumentos do arquivo 1
                if (Integer.valueOf(s) > arquivo1.get(0).size()) { // se o valor do argumento é maior que o numero de colunas do arquivo1
                   
                    System.out.println("ERRO: Coluna select maior que o possivel");
                    return;
                }else
                    valoresSelectintarq1.add(Integer.valueOf(s));
            }
            if (control % 2 == 1) { // o segundo,quarto,sexto... argumentos do arquivo 2
                if (Integer.valueOf(s) > arquivo2.get(0).size()) { //se o valor do argumento é maior que o numero de colunas do arquivo2
                    System.out.println("ERRO: Coluna select maior que o possivel");
                    return;
                }else
                     valoresSelectintarq2.add(Integer.valueOf(s));
            }
            control++;
        }
        Integer on1 = Integer.valueOf(matcher.group(7));
        Integer on2 = Integer.valueOf(matcher.group(8));
        if (on1 > arquivo1.get(0).size()) { // vai testar se o primeiro valor do ON é maior que o numero de colunas do arquivo1
            System.out.println("ERRO: ON com coluna maior que o possivel no arquvo 1");
            return;
        }
        if (on2 > arquivo2.get(0).size()) { // vai testar se o primeiro valor do ON é maior que o numero de colunas do arquivo2
            System.out.println("ERRO: ON com coluna maior que o possivel no arquvo 2");
            return;
        }
        
        for(List<String> arq1 : arquivo1)
        {
            for(List<String> arq2 : arquivo2)
            { 
                if(arq1.get(on1-1).equals(arq2.get(on2-1)))
                {
                    for (Integer i : valoresSelectintarq1)
                        System.out.println(arq1.get(i-1)+" ");
                    for (Integer i : valoresSelectintarq2)
                        System.out.println(arq2.get(i-1)+" ");  
                }
            }
        }
    }

}
