package joelbits.parsers;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;

public class MyErrorListener implements ANTLRErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object o, int i, int i1, String s, RecognitionException e) {

        // Om det kommer in error token här så kolla om den innehåller Benchmark*, om den gör det, ta index för var
        // Benchmarken börjar i texten, kapa av texten med start där Benchmark* börjar och hantera denna som en
        // metoddeklaration där jag delar upp relevanta bitar efter datamodellen.

        if(o.getClass().getName().equals("org.antlr.v4.runtime.CommonToken")) {
            CommonToken token = (CommonToken) o;
            System.out.println("Token i error: " + token.getText());

            if (token.getText().contains("Benchmark")) {
                System.out.println("CONTAINS BENCHMARK");
                int index = token.getText().indexOf("Benchmark", 0);
                System.out.println("index: " + index);
                String benchmark = token.getText().substring(index);
                System.out.println("benchmark: " + benchmark);
                if (benchmark.contains(")")) {
                    index = benchmark.indexOf(')');
                    String declaration = benchmark.substring(0, ++index);
                    String body = benchmark.substring(++index);
                    System.out.println("delcaration: " + declaration);
                    System.out.println("parameters: ");
                    System.out.println("body: " + body);
                }
            }
        }
        System.out.println("INNE I ERROR LISTENER: SYNTAXERROR: " + s);
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {

    }
}
