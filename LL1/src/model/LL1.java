package model;

import java.util.*;

public class LL1 {
    private Grammar grammar;
    private Map<String, Set<String>> firstSet;
    private Map<String, Set<String>> followSet;
    private ParseTable parseTable = new ParseTable();
    private static Stack<List<String>> rules = new Stack<>();
    private Map<Pair<String, List<String>>, Integer> productionsNumbered = new HashMap<>();
    private Stack<String> alpha = new Stack<>(); //input stack
    private Stack<String> beta = new Stack<>(); //working stack
    private Stack<String> pi = new Stack<>(); //output stack



    public LL1(Grammar grammar){
        this.grammar = grammar;
        this.firstSet = new HashMap<>();
        this.followSet = new HashMap<>();
        generateSets();
    }

    private void generateSets() {
        generateFirstSet();
        generateFollowSet();
        createParseTable();
    }



    private void generateFirstSet() {
        for (String nonTerminal : grammar.getNonTerminals()) {
            firstSet.put(nonTerminal, this.firstOf(nonTerminal));
        }
    }

    private Set<String> firstOf(String nonTerminal) {
        if (firstSet.containsKey(nonTerminal)) // if we already have the nonTerm as a key in the set, we return the value for the non-term in the dict
            return firstSet.get(nonTerminal);
        Set<String> temp = new HashSet<>();
        Set<String> terminals = grammar.getTerminals();
        for (Prod production : grammar.getProductionsForNonterminal(nonTerminal))
            for (List<String> rule : production.getRules()) {
                String firstSymbol = rule.get(0);
                if (firstSymbol.equals("ε"))
                    temp.add("ε");
                else if (terminals.contains(firstSymbol))
                    temp.add(firstSymbol);
                else //if the first of the rule is a non term,we go recursively searching its first
                    temp.addAll(firstOf(firstSymbol));
            }
        return temp;
    }

    private void generateFollowSet() {
        for (String nonTerminal : grammar.getNonTerminals()) {
            followSet.put(nonTerminal, this.followOf(nonTerminal, nonTerminal));
        }
    }

    private Set<String> followOf(String nonTerminal, String initialNonTerminal) {
        if (followSet.containsKey(nonTerminal))
            return followSet.get(nonTerminal);
        Set<String> temp = new HashSet<>();
        Set<String> terminals = grammar.getTerminals();

        if (nonTerminal.equals(grammar.getStartingSymbol())) //if it is the start symb, we put $
            temp.add("$");

        for (Prod production : grammar.getProductionsContainingNonterminal(nonTerminal)) { //we iterate through every production that contains the non terminal
            String productionStart = production.getStart(); //lhs of the prod
            for (List<String> rule : production.getRules()){ //rhs - the rules
                List<String> ruleConflict = new ArrayList<>();
                ruleConflict.add(nonTerminal); //make sure we don't generate the same rule twice for the same non-term
                ruleConflict.addAll(rule);
                if (rule.contains(nonTerminal) && !rules.contains(ruleConflict)) { // the non-term is in the rhs of the prod and the non-term - rule pair is unique
                    rules.push(ruleConflict); //push the possible conflict
                    int indexNonTerminal = rule.indexOf(nonTerminal); //take the index of the nonTerm in the rule
                    temp.addAll(followOperation(nonTerminal, temp, terminals, productionStart, rule, indexNonTerminal, initialNonTerminal));

                    List<String> sublist = rule.subList(indexNonTerminal + 1, rule.size()); //verify if the nonterm is in the next positions of the list of the rules
                    if (sublist.contains(nonTerminal))
                        temp.addAll(followOperation(nonTerminal, temp, terminals, productionStart, rule, indexNonTerminal + 1 + sublist.indexOf(nonTerminal), initialNonTerminal));

                    rules.pop();
                }
            }
        }

        return temp;
    }

    private Set<String> followOperation(String nonTerminal, Set<String> temp, Set<String> terminals, String productionStart, List<String> rule, int indexNonTerminal, String initialNonTerminal) {
        if (indexNonTerminal == rule.size() - 1) { // if its the last elem in the rule
            if (productionStart.equals(nonTerminal))
                return temp;
            if (!initialNonTerminal.equals(productionStart)){ // if one non-term contains the other one at the end, we compute recursively the follow of of the non-term that is placed at the end
                temp.addAll(followOf(productionStart, initialNonTerminal));
            }
        }
        else
        {
            String nextSymbol = rule.get(indexNonTerminal + 1);
            if (terminals.contains(nextSymbol))
                temp.add(nextSymbol);
            else{ //if the next elem in the rule is not a terminal
                if (!initialNonTerminal.equals(nextSymbol)) {
                    Set<String> firstsForNextSym = new HashSet<>(firstSet.get(nextSymbol)); //get the first set of the next symbol
                    if (firstsForNextSym.contains("ε")) { //if the first set contains epsilon, it means that eps can be generated => add all the follow set from the next sym
                        temp.addAll(followOf(nextSymbol, initialNonTerminal));
                        firstsForNextSym.remove("ε");
                    }
                    temp.addAll(firstsForNextSym);
                }
            }
        }
        return temp;
    }

    public Map<String, Set<String>> getFollowSet() {
        return followSet;
    }

    public Map<String, Set<String>> getFirstSet() {
        return firstSet;
    }

    //create a hashmap with the numbered productions
    private void numberingProductions() {
        int index = 1;
        for (Prod production: grammar.getProductions())
            for (List<String> rule: production.getRules())
                productionsNumbered.put(new Pair<>(production.getStart(), rule), index++);
    }


    private void createParseTable() {
        numberingProductions();

        List<String> columnSymbols = new LinkedList<>(grammar.getTerminals());
        columnSymbols.add("$");

        // M(a, a) = pop
        // M($, $) = acc

        parseTable.put(new Pair<>("$", "$"), new Pair<>(Collections.singletonList("acc"), -1));
        for (String terminal: grammar.getTerminals())
            parseTable.put(new Pair<>(terminal, terminal), new Pair<>(Collections.singletonList("pop"), -1));



//        1) M(A, a) = (α, i), if:
//            a) a ∈ first(α)
//            b) a != ε
//            c) A -> α production with index i
//
//        2) M(A, b) = (α, i), if:
//            a) ε ∈ first(α)
//            b) whichever b ∈ follow(A)
//            c) A -> α production with index i


        //iterate through each numbered production
        productionsNumbered.forEach((key, value) -> {
            String rowSymbol = key.getKey();
            List<String> rule = key.getValue();
            Pair<List<String>, Integer> parseTableValue = new Pair<>(rule, value);

            //terminals will be columns
            for (String columnSymbol : columnSymbols) {
                Pair<String, String> parseTableKey = new Pair<>(rowSymbol, columnSymbol);

                // if our column-terminal is exactly first of rule
                if (rule.get(0).equals(columnSymbol) && !columnSymbol.equals("ε"))
                    parseTable.put(parseTableKey, parseTableValue);

                // if the first symbol is a non-terminal and its first contains our column-terminal
                else if (grammar.getNonTerminals().contains(rule.get(0)) && firstSet.get(rule.get(0)).contains(columnSymbol)) {
                    if (!parseTable.containsKey(parseTableKey)) {
                        parseTable.put(parseTableKey, parseTableValue);
                    }
                }
                else {
                    // if the first symbol is ε then everything if FOLLOW(rowSymbol) will be in parse table
                    if (rule.get(0).equals("ε")) {
                        for (String b : followSet.get(rowSymbol))
                            parseTable.put(new Pair<>(rowSymbol, b), parseTableValue);
                    // if ε is in FIRST(rule)
                    } else {
                        Set<String> firsts = new HashSet<>();
                        //get all firsts from every non term of the rule
                        for (String symbol : rule)
                            if (grammar.getNonTerminals().contains(symbol))
                                firsts.addAll(firstSet.get(symbol));
                        if (firsts.contains("ε")) {
                            //iterate every first value for out non term (the row)
                            for (String b : firstSet.get(rowSymbol)) {
                                if (b.equals("ε"))
                                    b = "$";
                                parseTableKey = new Pair<>(rowSymbol, b);
                                if (!parseTable.containsKey(parseTableKey)) {
                                    parseTable.put(parseTableKey, parseTableValue);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public ParseTable getParseTable() {
        return parseTable;
    }


    public boolean parse(List<String> w) {
        initializeStacks(w);

        boolean go = true; //continue or not
        boolean result = true; //accepted or err

        while (go) {
            //take head of working and initial stack w/o deleting them from the stacks
            String betaHead = beta.peek();
            String alphaHead = alpha.peek();
            //finished parsing
            if (betaHead.equals("$") && alphaHead.equals("$")) {
                return result;
            }
            //make a pair containing the head of the working and initial stack
            Pair<String, String> heads = new Pair<>(betaHead, alphaHead);
            //get the value from the parse table for the heads
            Pair<List<String>, Integer> parseTableEntry = parseTable.get(heads);

            if (parseTableEntry == null) {
                heads = new Pair<>(betaHead, "ε");
                parseTableEntry = parseTable.get(heads);
                if (parseTableEntry != null) {
                    beta.pop();
                    continue;
                }

            }

            if (parseTableEntry == null) { //line and col of the err
                go = false;
                result = false; //if entry from the PT is null and entry for (betahead, eps) is also null => err
            } else { //entry from Parse Table not null
                List<String> production = parseTableEntry.getKey(); //get prod string from the PT
                Integer productionPos = parseTableEntry.getValue(); //get position from the PT
                // start with start_sym or term with term and acc = > acc, return result
                if (productionPos == -1 && production.get(0).equals("acc")) {
                    go = false;
                    // start with start_sym or term with term and pop = > pop from working and initial stacks
                } else if (productionPos == -1 && production.get(0).equals("pop")) {
                    beta.pop();
                    alpha.pop();
                } else {
                    beta.pop();
                    //if first from prod is not eps we push it in the working stack
                    if (!production.get(0).equals("ε")) {
                        pushAsChars(production, beta);
                    }
                    pi.push(productionPos.toString());
                }
            }
        }

        return result;
    }

    public boolean parseSource(List<model.Pair<Integer, Integer>> pif) {
        List<String> sequence = new LinkedList<>();
        for (model.Pair<Integer, Integer> pifEntry : pif) {
            sequence.add(String.valueOf(pifEntry.getKey()));
        }

        return this.parse(sequence);
    }

    private void initializeStacks(List<String> w) {
        alpha.clear();
        //add in the alpha stack the string and $
        alpha.push("$");
        pushAsChars(w, alpha);

        //add in beta the starting symbol, followed by $
        beta.clear();
        beta.push("$");
        beta.push(grammar.getStartingSymbol());

        //put epsilon in pi
        pi.clear();
        pi.push("ε");
    }

    private void pushAsChars(List<String> sequence, Stack<String> stack) {
        for (int i = sequence.size() - 1; i >= 0; i--) {
            stack.push(sequence.get(i));
        }
    }

    public Stack<String> getPi() {
        return pi;
    }

    public Map<Pair<String, List<String>>, Integer> getProductionsNumbered() {
        return productionsNumbered;
    }


    public String displayPiProductions(Stack<String> pi) {
        StringBuilder sb = new StringBuilder();

        for (String productionIndexString : pi) {
            if (productionIndexString.equals("ε")) {
                continue;
            }
            Integer productionIndex = Integer.parseInt(productionIndexString);
            getProductionsNumbered().forEach((key, value) ->{
                if (productionIndex.equals(value))
                    sb.append(value).append(": ").append(key.getKey()).append(" -> ").append(key.getValue()).append("\n");
            });
        }

        return sb.toString();
    }

}
