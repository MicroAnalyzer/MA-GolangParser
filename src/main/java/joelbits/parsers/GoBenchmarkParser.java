package joelbits.parsers;

import joelbits.parsers.golang.GolangLexer;
import joelbits.parsers.golang.GolangParser;
import joelbits.parsers.spi.Parser;
import joelbits.parsers.types.ParserType;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class GoBenchmarkParser implements Parser {
    private static final Logger log = LoggerFactory.getLogger(GoBenchmarkParser.class);
    private static final MicrobenchmarkGolangBaseVisitor visitor = new MicrobenchmarkGolangBaseVisitor();
    private final Map<String, String> benchmarkParameters = new HashMap<>();
    private final Map<String, String> benchmarkBodies = new HashMap<>();

    public Set<String> allBenchmarks() {
        return benchmarkParameters.keySet();
    }

    public String parameters(String benchmark) {
        return benchmarkParameters.get(benchmark);
    }

    public String body(String benchmark) {
        return benchmarkBodies.get(benchmark);
    }

    public static void main( String[] args) throws Exception  {
        File file = new File("C:\\Users\\joel\\Desktop\\thesis\\dataset\\Go\\gin-gonic\\gin\\benchmarks_test.go");
        org.antlr.v4.runtime.CharStream input = org.antlr.v4.runtime.CharStreams.fromStream(new FileInputStream(file));
        GolangLexer lexer = new GolangLexer(input);
        lexer.removeErrorListeners();
        org.antlr.v4.runtime.CommonTokenStream tokens = new CommonTokenStream(lexer);

        GolangParser parser = new GolangParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(BenchmarkErrorListener.INSTANCE);
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        try {
            parser.reset();

            System.out.println("BEFORE");
            visitor.visitSourceFile(parser.sourceFile());
            System.out.println("AFTER");

            Map<String, String> benchmarks = BenchmarkErrorListener.INSTANCE.benchmarkBodies();
            Map<String, String> benchmarksParams = BenchmarkErrorListener.INSTANCE.benchmarkParameters();
            Map<String, String> moreBenchmarks = visitor.benchmarkParameters();
            Map<String, String> mostBenchmarks = visitor.benchmarkBodies();
            System.out.println("Antal benchmarkBodies i lyssnaren: " + benchmarks.size());
            System.out.println("Antal benchmarkBodies i visitor: " + moreBenchmarks.size());
            for (Map.Entry<String, String> benchmark : benchmarks.entrySet()) {
                System.out.println("key: " + benchmark.getKey() + " and value: " + benchmark.getValue());
            }
            for (Map.Entry<String, String> benchmark : moreBenchmarks.entrySet()) {
                System.out.println("visitor: " + benchmark.getKey() + " params: " + benchmark.getValue());
            }

            for (Map.Entry<String, String> benchmark : mostBenchmarks.entrySet()) {
                System.out.println("visitor: " + benchmark.getKey() + " body: " + benchmark.getValue());
            }
            for (Map.Entry<String, String> benchmark : benchmarksParams.entrySet()) {
                System.out.println("key: " + benchmark.getKey() + " and value: " + benchmark.getValue());
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void parse(String filePath) {

    }

    @Override
    public String type() {
        return ParserType.GO.name();
    }

    @Override
    public String toString() {
        return "GoBenchmarkParser";
    }
}
