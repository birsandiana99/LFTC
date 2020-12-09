package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Grammar {
    Set<String> terms;
    List<String> nonTerms;
    List<Prod> productions;
    String file_name;
    String startingSymbol;

    public Grammar(String file) {
        terms = new HashSet<>();
        nonTerms = new LinkedList<>();
        productions = new ArrayList<>();
        this.file_name = file;
        readFromFile();
    }

    private void readFromFile() {
        try {
            int i = 0;
            for (String line : Files.readAllLines(Paths.get("src\\grammar.txt"))) {
                if (i <= 2){ //terminals ( 1st line) , non terminals (2nd line), startSym (3rd line)
                    String[] tokens = line.split(" ");

                    for (int j = 0; j < tokens.length; j++) {
                        //System.out.println("TOKENS[  "+ j + "] " + tokens[j] + "    ");
                        if (i == 0) {
                            nonTerms.add(tokens[j]);
                        }
                        if (i == 1) {
                            terms.add(tokens[j]);
                        }
                        if (i == 2) {
                            startingSymbol = tokens[j];
                        }

                    }
                }
                if (i > 2) {
                    String[] tokens = line.split(" -> ");
                    List<List<String>> rules = new ArrayList<>();

                    for ( String rule: tokens[1].split(" \\| "))
                        rules.add(Arrays.asList(rule.split(" ")));
                    productions.add(new Prod(tokens[0], rules));
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public Set<Prod> getProductionsContainingNonterminal(String nonterminal) {
        Set<Prod> productionsForNonterminal = new HashSet<>();
        for (Prod production : productions) {
            for (List<String> rule : production.getRules())
                if (rule.contains(nonterminal))
                    productionsForNonterminal.add(production);
        }
        return productionsForNonterminal;
    }

    public List<Prod> getProductionsForNonterminal(String nonterminal) {
        List<Prod> productionsForNonterminal = new LinkedList<>();
        for (Prod production : productions) {
            if (production.getStart().equals(nonterminal)) {
//                System.out.println("GGGGGG " + nonterminal + " " + production.getRules());
                productionsForNonterminal.add(production);
            }
        }
        return productionsForNonterminal;
    }

    public List<String> getNonTerminals() {
        return nonTerms;
    }

    public Set<String> getTerminals() {
        return terms;
    }

    public List<Prod> getProductions() {
        return productions;
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }

    public String toString() {
        return "G =( " + nonTerms.toString() + ", " + terms.toString() + ", " +
                productions.toString() + ", " + startingSymbol + " )";

    }
}


