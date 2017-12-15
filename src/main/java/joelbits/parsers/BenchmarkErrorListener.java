package joelbits.parsers;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class BenchmarkErrorListener implements ANTLRErrorListener {
    private static final Map<String, String> benchmarkBodies = new HashMap<>();
    private static final Map<String, String> benchmarkParameters = new HashMap<>();

    static final BenchmarkErrorListener INSTANCE = new BenchmarkErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object o, int i, int i1, String s, RecognitionException e) {
        if(o.getClass().getName().equals("org.antlr.v4.runtime.CommonToken")) {
            CommonToken token = (CommonToken) o;
            if (token.getText().contains("Benchmark")) {
                int benchmarkIndex = token.getText().indexOf("Benchmark", 0);
                String benchmark = token.getText().substring(benchmarkIndex);
                benchmarkBodies.put(benchmark, "");
                benchmarkParameters.put(benchmark, "");
                if (benchmark.contains(")")) {
                    int parameterEndIndex = benchmark.indexOf(')');
                    int parameterStartIndex = benchmark.indexOf('(');
                    String declaration = benchmark.substring(0, parameterStartIndex);
                    String body = benchmark.substring(++parameterEndIndex);
                    String parameters = benchmark.substring(parameterStartIndex, parameterEndIndex);

                    benchmarkBodies.remove(benchmark);
                    benchmarkParameters.remove(benchmark);
                    benchmarkBodies.put(declaration, body);
                    benchmarkParameters.put(declaration, parameters);
                }
            }
        }
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

    public Map<String, String> benchmarkBodies() {
        return benchmarkBodies;
    }

    public Map<String, String> benchmarkParameters() {
        return benchmarkParameters;
    }
}
