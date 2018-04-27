package joelbits.modules.preprocessing.plugins.utils;

import static joelbits.model.ast.protobuf.ASTProtos.Method;
import static joelbits.model.ast.protobuf.ASTProtos.Variable;
import static joelbits.model.ast.protobuf.ASTProtos.Declaration;
import static joelbits.model.ast.protobuf.ASTProtos.DeclarationType;
import static joelbits.model.ast.protobuf.ASTProtos.Statement;
import static joelbits.model.ast.protobuf.ASTProtos.Statement.StatementType;
import static joelbits.model.ast.protobuf.ASTProtos.Expression;
import static joelbits.model.ast.protobuf.ASTProtos.Expression.ExpressionType;
import static joelbits.model.ast.protobuf.ASTProtos.Type;
import static joelbits.model.ast.protobuf.ASTProtos.ASTRoot;
import static joelbits.model.ast.protobuf.ASTProtos.Namespace;

import java.util.List;

/**
 * Creates the Protocol Buffer representation of the AST nodes used within MicroAnalyzer. The created PB
 * objects contains the parsed data and are later converted to their binary form for persistence.
 */
public final class ASTNodeCreator {
    public Variable createVariable(String name, Type type, Expression value) {
        return Variable.newBuilder()
                .setName(name)
                .setType(type)
                .setInitializer(value)
                .build();
    }

    public Type createType(String name, DeclarationType type) {
        return Type.newBuilder()
                .setName(name)
                .setType(type)
                .build();
    }

    public Method createMethod(String name, Type type, List<Variable> arguments, List<Expression> methodBody, List<Statement> bodyStatements) {
        return Method.newBuilder()
                .setName(name)
                .setReturnType(type)
                .addAllArguments(arguments)
                .addAllBodyContent(methodBody)
                .addAllStatements(bodyStatements)
                .build();
    }

    public Expression createMethodCallExpression(String methodCall, List<Expression> methodArguments) {
        return Expression.newBuilder()
                .setType(ExpressionType.METHODCALL)
                .setMethod(methodCall.substring(0, methodCall.indexOf("(")))
                .addAllMethodArguments(methodArguments)
                .build();
    }

    public Expression createVarDeclarationExpression(String literal, String variable) {
        return Expression.newBuilder()
                .setType(ExpressionType.VARIABLE_DECLARATION)
                .setLiteral(literal)
                .setVariable(variable)
                .build();
    }

    public Expression createArgumentExpression(String argument) {
        return Expression.newBuilder()
                .setType(ExpressionType.OTHER)
                .setVariable(argument)
                .build();
    }

    public Expression createAssignmentExpression(ExpressionType type, String literal, String variable, List<Expression> expressions) {
        return Expression.newBuilder()
                .setType(type)
                .setLiteral(literal)
                .setVariable(variable)
                .addAllExpressions(expressions)
                .build();
    }

    public Expression createCreationExpression(String literal, String variable, List<Expression> arguments) {
        return Expression.newBuilder()
                .setType(ExpressionType.NEW)
                .setLiteral(literal)
                .setVariable(variable)
                .addAllMethodArguments(arguments)
                .build();
    }

    public Expression createExpression(ExpressionType type, String literal, String variable, List<Variable> declarations, List<Expression> arguments, Type newType, List<Expression> expressions) {
        return Expression.newBuilder()
                .setType(type)
                .setLiteral(literal)
                .setVariable(variable)
                .addAllVariableDeclarations(declarations)
                .addAllMethodArguments(arguments)
                .setNewType(newType)
                .addAllExpressions(expressions)
                .build();
    }

    public Expression createMethodBodyExpression(ExpressionType type, String variable, String literal) {
        return Expression.newBuilder()
                .setType(type)
                .setVariable(variable)
                .setLiteral(literal)
                .build();
    }

    public Declaration createNamespaceDeclaration(String name, DeclarationType type, List<Variable> allFields, List<Method> allMethods) {
        return Declaration.newBuilder()
                .setName(name)
                .setType(type)
                .addAllFields(allFields)
                .addAllMethods(allMethods)
                .build();
    }

    public Statement createReturnStatement(List<Expression> expressions) {
        return Statement.newBuilder()
                .setType(StatementType.RETURN)
                .addAllExpressions(expressions)
                .build();
    }

    public Statement createStatement(StatementType type, Expression condition, List<Statement> statements) {
        return Statement.newBuilder()
                .setType(type)
                .setCondition(condition)
                .addAllStatements(statements)
                .build();
    }

    public Statement createBlockStatement(List<Expression> expressions, List<Statement> statements) {
        return Statement.newBuilder()
                .setType(StatementType.BLOCK)
                .addAllExpressions(expressions)
                .addAllStatements(statements)
                .build();
    }

    public Statement createTryStatement(List<Statement> statements) {
        return Statement.newBuilder()
                .setType(StatementType.TRY)
                .addAllStatements(statements)
                .build();
    }

    public Statement createStatement(StatementType type, List<Expression> expressions, Expression condition, List<Statement> nestedStatements, List<Expression> initializations, List<Expression> updates) {
        return Statement.newBuilder()
                .setType(type)
                .addAllExpressions(expressions)
                .setCondition(condition)
                .addAllStatements(nestedStatements)
                .addAllInitializations(initializations)
                .addAllUpdates(updates)
                .build();
    }

    public ASTRoot createAstRoot(List<String> imports, List<Namespace> namespaces) {
        return ASTRoot.newBuilder()
                .addAllImports(imports)
                .addAllNamespaces(namespaces)
                .build();
    }
}
