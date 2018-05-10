package joelbits.modules.preprocessing.plugins;

import com.google.auto.service.AutoService;
import joelbits.model.ast.protobuf.ASTProtos.Variable;
import joelbits.model.ast.protobuf.ASTProtos.Method;
import joelbits.model.ast.protobuf.ASTProtos.Namespace;
import joelbits.modules.preprocessing.plugins.golang.GolangParser;
import joelbits.modules.preprocessing.plugins.golang.GolangLexer;
import joelbits.modules.preprocessing.plugins.listeners.ClassListener;
import joelbits.modules.preprocessing.plugins.listeners.ImportListener;
import joelbits.modules.preprocessing.plugins.spi.FileParser;
import joelbits.modules.preprocessing.plugins.utils.NamespaceCreator;
import joelbits.modules.preprocessing.utils.ASTNodeCreator;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@AutoService(FileParser.class)
public final class GoParser implements FileParser {
    private final List<String> imports = new ArrayList<>();
    private final List<Namespace> namespaces = new ArrayList<>();
    private final ASTNodeCreator astNodeCreator = new ASTNodeCreator();
    private GolangParser parser;

    @Override
    public byte[] parse(File file) throws Exception {
        loadFile(file);

        NamespaceCreator namespaceCreator = new NamespaceCreator();
        ParseTree tree = parser.sourceFile();
        ParseTreeWalker walker = new ParseTreeWalker();
        String namespace = file.getName().substring(0, file.getName().lastIndexOf("."));
        ClassListener classListener = new ClassListener(namespace);
        ImportListener importListener = new ImportListener(imports);
        walker.walk(classListener, tree);
        walker.walk(importListener, tree);
        namespaceCreator.createNamespace(namespace, classListener.namespaceDeclarations());
        classListener.clear();

        return astNodeCreator.createAstRoot(imports, namespaceCreator.namespaces()).toByteArray();
    }

    private void loadFile(File file) throws Exception {
        clearData();
        org.antlr.v4.runtime.CharStream input = org.antlr.v4.runtime.CharStreams.fromStream(new FileInputStream(file));
        initParser(initLexer(input));
    }

    private CommonTokenStream initLexer(CharStream input) {
        GolangLexer lexer = new GolangLexer(input);
        lexer.removeErrorListeners();
        return new CommonTokenStream(lexer);
    }

    private void initParser(CommonTokenStream tokens) {
        parser = new GolangParser(tokens);
        parser.removeErrorListeners();
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        parser.reset();
    }

    private void clearData() {
        imports.clear();
        namespaces.clear();
    }

    @Override
    public boolean hasBenchmarks(File file) throws Exception {
        loadFile(file);

        String namespace = file.getName().substring(0, file.getName().lastIndexOf("."));
        ClassListener classListener = new ClassListener(namespace);
        ParseTree tree = parser.sourceFile();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(classListener, tree);

        boolean hasBenchmark = hasBenchmarks(classListener.methods());
        classListener.clear();
        return hasBenchmark;
    }

    private boolean hasBenchmarks(List<Method> methods) {
        for (Method method : methods) {
            for(Variable argument : method.getArgumentsList()) {
                if(argument.getType().getName().contains("*testing.B")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "go";
    }
}
