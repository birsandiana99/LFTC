package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Grammar {
    List<String> term;
    List<String> nonTerm;
    HashMap<String, List<String>> productions;
    String file;

    public Grammar(String file){
        term = new ArrayList<>();
        nonTerm = new ArrayList<>();
        productions = new HashMap<>();
        this.file = file;
        readFromFile();
    }

    public List<String> getTerm() {
        return term;
    }

    public List<String> getNonTerm() {
        return nonTerm;
    }

    public HashMap<String, List<String>> getProductions() {
        return productions;
    }

    public List<String> getProductionsForNonTerminal(String nonTerm){
        return this.productions.get(nonTerm);
    }


    private void readFromFile(){
        try {
            BufferedReader buffer_reader = new BufferedReader(new FileReader(this.file));
            List<String> nonTerm = Arrays.asList(buffer_reader.readLine().split(","));
            List<String> terminals = Arrays.asList(buffer_reader.readLine().split(","));
            this.nonTerm = nonTerm;
            this.term = terminals;
            while (true){
                String line = buffer_reader.readLine();
                if(line==null || line.equals("")){
                    break;
                }
                List<String> production = Arrays.asList(line.strip().split("->"));
                String left = production.get(0);
                List<String> right = Arrays.asList(production.get(1).strip().split("\\|"));
                for(String el: right){
                    String elem = el.strip();
                    List<String> lst = productions.get(left);
                    if(lst == null){
                        lst = new ArrayList<>();
                        lst.add(elem);
                        productions.put(left, lst);
                    }
                    else{
                        lst.add(elem);
                    }
                }
            }
            buffer_reader.close();
            System.out.println(productions);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}


