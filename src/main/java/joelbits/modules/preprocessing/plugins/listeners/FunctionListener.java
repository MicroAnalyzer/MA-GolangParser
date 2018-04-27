package joelbits.modules.preprocessing.plugins.listeners;

import joelbits.model.ast.protobuf.ASTProtos.Statement;
import joelbits.model.ast.protobuf.ASTProtos.Expression;
import joelbits.model.ast.protobuf.ASTProtos.Variable;
import joelbits.model.ast.protobuf.ASTProtos.Type;
import joelbits.model.ast.protobuf.ASTProtos.DeclarationType;
import joelbits.model.ast.protobuf.ASTProtos.Method;
import joelbits.modules.preprocessing.plugins.golang.GolangBaseListener;
import joelbits.modules.preprocessing.plugins.golang.GolangParser;
import joelbits.modules.preprocessing.plugins.utils.ASTNodeCreator;

import java.util.ArrayList;
import java.util.List;

public final class FunctionListener extends GolangBaseListener {
    private final ASTNodeCreator astNodeCreator = new ASTNodeCreator();
    private List<Variable> arguments = new ArrayList<>();
    private List<Expression> expressions = new ArrayList<>();
    private List<Statement> statements = new ArrayList<>();
    private String name;

    @Override
    public void enterFunction(GolangParser.FunctionContext ctx) {
        createArguments(ctx.signature().parameters().parameterList().parameterDecl());

        BlockListener blockListener = new BlockListener();
        blockListener.enterBlock(ctx.block());
    }

    private void createArguments(List<GolangParser.ParameterDeclContext> parameters) {
        List<Variable> arguments = new ArrayList<>();
        for (GolangParser.ParameterDeclContext parameter : parameters) {
            Type type = astNodeCreator.createType(parameter.type().getText(), DeclarationType.OTHER);
            arguments.add(astNodeCreator
                    .createVariable(parameter.start.getText(), type, Expression.getDefaultInstance()));
        }

        this.arguments.addAll(arguments);
    }

    @Override
    public void enterFunctionDecl(GolangParser.FunctionDeclContext ctx) {
        name = ctx.IDENTIFIER().getText();
    }

    Method function() {
        Type type = astNodeCreator.createType("func", DeclarationType.OTHER);
        return astNodeCreator.createMethod(name, type, arguments, expressions, statements);
    }
}
