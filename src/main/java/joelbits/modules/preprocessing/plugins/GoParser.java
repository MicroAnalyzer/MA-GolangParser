package joelbits.modules.preprocessing.plugins;

import joelbits.model.ast.protobuf.ASTProtos.Variable;
import joelbits.model.ast.protobuf.ASTProtos.Method;
import joelbits.model.ast.protobuf.ASTProtos.Declaration;
import joelbits.model.ast.protobuf.ASTProtos.Namespace;
import joelbits.modules.preprocessing.plugins.golang.GolangParser;
import joelbits.modules.preprocessing.plugins.golang.GolangLexer;
import joelbits.modules.preprocessing.plugins.listeners.ClassListener;
import joelbits.modules.preprocessing.plugins.spi.FileParser;
import joelbits.modules.preprocessing.plugins.utils.ASTNodeCreator;
import joelbits.modules.preprocessing.plugins.utils.NamespaceCreator;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public final class GoParser implements FileParser {
    private static final List<String> imports = new ArrayList<>();
    private static final List<Namespace> namespaces = new ArrayList<>();
    private static final ASTNodeCreator astNodeCreator = new ASTNodeCreator();
    private GolangParser parser;

    @Override
    public byte[] parse(File file) throws Exception {
        loadFile(file);

        List<Declaration> declarations = new ArrayList<>();
        String namespace = file.getName().substring(0, file.getName().lastIndexOf("."));
        NamespaceCreator namespaceCreator = new NamespaceCreator();
        namespaceCreator.createNamespace(namespace, new ArrayList<>());

        try {
            ParseTree tree = parser.sourceFile();
            ParseTreeWalker walker = new ParseTreeWalker();
            ClassListener classListener = new ClassListener(namespace, declarations);
            walker.walk(classListener, tree);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return astNodeCreator.createAstRoot(imports, namespaceCreator.namespaces()).toByteArray();
    }

    private void loadFile(File file) throws Exception {
        clearData();
        org.antlr.v4.runtime.CharStream input = org.antlr.v4.runtime.CharStreams.fromStream(new FileInputStream(file));
        initParser(input);
    }

    private void initParser(CharStream input) {
        GolangLexer lexer = new GolangLexer(input);
        lexer.removeErrorListeners();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
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
        for (Method method : namespaces.get(0).getDeclarationsList().get(0).getMethodsList()) {
            for(Variable argument : method.getArgumentsList()) {
                if(argument.getName().contains("*testing.B")) {
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
