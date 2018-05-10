package joelbits.modules.preprocessing.plugins.listeners;

import joelbits.model.ast.protobuf.ASTProtos.Statement;
import joelbits.model.ast.protobuf.ASTProtos.Expression;
import joelbits.model.ast.protobuf.ASTProtos.Variable;
import joelbits.model.ast.protobuf.ASTProtos.Type;
import joelbits.model.ast.protobuf.ASTProtos.DeclarationType;
import joelbits.model.ast.protobuf.ASTProtos.Method;
import joelbits.modules.preprocessing.plugins.golang.GolangBaseListener;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.MethodDeclContext;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.FunctionContext;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.FunctionDeclContext;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.ParameterDeclContext;
import joelbits.modules.preprocessing.utils.ASTNodeCreator;

import java.util.ArrayList;
import java.util.List;

public final class FunctionListener extends GolangBaseListener {
    private final ASTNodeCreator astNodeCreator = new ASTNodeCreator();
    private final List<Variable> arguments = new ArrayList<>();
    private final List<Expression> expressions = new ArrayList<>();
    private final List<Statement> statements = new ArrayList<>();
    private String name;

    @Override
    public void enterFunction(FunctionContext ctx) {
        createArguments(ctx.signature().parameters().parameterList().parameterDecl());

        StatementListener statementListener = new StatementListener();
        statementListener.enterBlock(ctx.block());
        statements.addAll(statementListener.statements());
        expressions.addAll(statementListener.expressions());
        statementListener.clear();
    }

    private void createArguments(List<ParameterDeclContext> parameters) {
        List<Variable> arguments = new ArrayList<>();
        for (ParameterDeclContext parameter : parameters) {
            Type type = astNodeCreator.createType(parameter.type().getText(), DeclarationType.OTHER);
            arguments.add(astNodeCreator
                    .createVariable(parameter.start.getText(), type, Expression.getDefaultInstance()));
        }

        this.arguments.addAll(arguments);
    }

    @Override
    public void enterFunctionDecl(FunctionDeclContext ctx) {
        name = ctx.IDENTIFIER().getText();
    }

    @Override
    public void enterMethodDecl(MethodDeclContext ctx) {
        name = ctx.IDENTIFIER().getText();
    }

    Method function() {
        Type type = astNodeCreator.createType("func", DeclarationType.OTHER);
        Method method = astNodeCreator.createMethod(name, type, arguments, expressions, statements);
        clear();
        return method;
    }

    private void clear() {
        arguments.clear();
        expressions.clear();
        statements.clear();
    }
}
