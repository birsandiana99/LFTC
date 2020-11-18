import model.Grammar;

public class Main {

    public static void main(String[] args) {
        Grammar g = new Grammar("input/grammar.txt");
        System.out.println("Non-terminals");
        System.out.println(g.getNonTerm());
        System.out.println("Terminals");
        System.out.println(g.getTerm());
        System.out.println("Productions");
        System.out.println(g.getProductions());
        System.out.println("Productions for S");
        System.out.println(g.getProductionsForNonTerminal("S"));
        System.out.println("Productions for A");
        System.out.println(g.getProductionsForNonTerminal("A"));
    }
}
