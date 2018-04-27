package joelbits.modules.preprocessing.plugins.listeners;

import joelbits.model.ast.protobuf.ASTProtos.DeclarationType;
import joelbits.model.ast.protobuf.ASTProtos.Variable;
import joelbits.model.ast.protobuf.ASTProtos.Method;
import joelbits.model.ast.protobuf.ASTProtos.Declaration;
import joelbits.modules.preprocessing.plugins.golang.GolangBaseListener;
import joelbits.modules.preprocessing.plugins.golang.GolangParser;
import joelbits.modules.preprocessing.plugins.utils.ASTNodeCreator;

import java.util.ArrayList;
import java.util.List;

public final class ClassListener extends GolangBaseListener {
    private List<Declaration> namespaceDeclarations;
    private final ASTNodeCreator astNodeCreator = new ASTNodeCreator();
    private final List<Variable> allFields = new ArrayList<>();
    private final List<Method> allMethods = new ArrayList<>();
    private final String namespace;

    public ClassListener(String namespace, List<Declaration> namespaceDeclarations) {
        this.namespace = namespace;
        this.namespaceDeclarations = namespaceDeclarations;
    }

    @Override
    public void enterTopLevelDecl(GolangParser.TopLevelDeclContext ctx) {
        if (ctx.functionDecl() != null) {
            FunctionListener functionListener = new FunctionListener();
            functionListener.enterFunctionDecl(ctx.functionDecl());
            functionListener.enterFunction(ctx.functionDecl().function());
            allMethods.add(functionListener.function());
        }
        if (ctx.declaration() != null && ctx.declaration().varDecl() != null) {
            FieldListener fieldListener = new FieldListener();
            fieldListener.enterVarDecl(ctx.declaration().varDecl());
            allFields.add(fieldListener.getVariable());
        }

        namespaceDeclarations.add(astNodeCreator
                .createNamespaceDeclaration(namespace, DeclarationType.CLASS, allFields, allMethods));
    }
}
