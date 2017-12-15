package joelbits.parsers;

import joelbits.parsers.golang.GolangBaseVisitor;
import joelbits.parsers.golang.GolangParser;

import java.util.HashMap;
import java.util.Map;

public class MicrobenchmarkGolangBaseVisitor<T> extends GolangBaseVisitor<T> {
    private static final Map<String, String> benchmarkParameters = new HashMap<>();
    private static final Map<String, String> benchmarkBodies = new HashMap<>();

    @Override
    public T visitFunctionDecl(GolangParser.FunctionDeclContext ctx) {
        String benchmark = ctx.getText();
        if (benchmark.contains("Benchmark")) {
            int benchmarkIndex = benchmark.indexOf("Benchmark");
            int parameterEndIndex = benchmark.indexOf(")");
            int parameterStartIndex = benchmark.indexOf("(");
            String benchmarkName = benchmark.substring(benchmarkIndex, parameterStartIndex);
            parameterEndIndex+=1;
            benchmarkParameters.put(benchmarkName, benchmark.substring(parameterStartIndex, parameterEndIndex));
            benchmarkBodies.put(benchmarkName, benchmark.substring(parameterEndIndex));
        }

        return null;
    }

    public Map<String, String> benchmarkParameters() {
        return benchmarkParameters;
    }

    public Map<String, String> benchmarkBodies() {
        return benchmarkBodies;
    }
}
