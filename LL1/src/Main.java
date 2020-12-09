import model.Grammar;
import model.LL1;

import java.util.*;

public class Main {



    public static void main(String[] args) {
        Grammar g = new Grammar("grammar.txt");
        System.out.println("Starting Symbol");
        System.out.println(g.getStartingSymbol());
        System.out.println("Non-terminals");
        System.out.println(g.getNonTerminals());
        System.out.println("Terminals");
        System.out.println(g.getTerminals());
        System.out.println("Productions");
        System.out.println(g.getProductions());
        System.out.println("Productions for S");
        System.out.println(g.getProductionsForNonterminal("S"));
        System.out.println("Productions for A");
        System.out.println(g.getProductionsForNonterminal("A"));
        System.out.println(g.getProductionsContainingNonterminal("S"));

        LL1 parser = new LL1(g);

//        Map<String, Set<String>> followSet = parser.getFollowSet();
//
//        Map<String, Set<String>> firstSet = parser.getFirstSet();

//        for (String nonterminal : g.getNonTerminals()){
//            System.out.println(nonterminal + ":"  );
//            System.out.println(firstSet.get(nonterminal));
//        }
//
//        System.out.println(followSet);

        System.out.println(parser.getParseTable());

        Scanner inScanner = new Scanner(System.in);
        List<String> w = Arrays.asList(inScanner.nextLine().replace("\n", "").split(" "));
        System.out.println(w);
        boolean result = parser.parse(w);
        if (result) {
            System.out.println("Sequence " + w + " accepted.");
            Stack<String> pi = parser.getPi();
            System.out.println(pi);
            System.out.println(parser.displayPiProductions(pi));
        } else {
            System.out.println("Sequence " + w + " is not accepted.");
        }

    }
}
