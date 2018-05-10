package joelbits.modules.preprocessing.plugins.listeners;

import joelbits.model.ast.protobuf.ASTProtos.Expression;
import joelbits.model.ast.protobuf.ASTProtos.Expression.ExpressionType;
import joelbits.modules.preprocessing.plugins.golang.GolangBaseListener;
import joelbits.modules.preprocessing.plugins.golang.GolangParser;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.TypeDeclContext;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.ArgumentsContext;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.ExpressionContext;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.PrimaryExprContext;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.VarSpecContext;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.VarDeclContext;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.UnaryExprContext;
import joelbits.modules.preprocessing.utils.ASTNodeCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ExpressionListener extends GolangBaseListener {
    private final ASTNodeCreator astNodeCreator = new ASTNodeCreator();
    private final List<Expression> expressions = new ArrayList<>();

    @Override
    public void enterPrimaryExpr(PrimaryExprContext ctx) {
        if (ctx.arguments() != null) {
            if (ctx.arguments().expressionList() == null) {
                expressions.add(astNodeCreator
                        .createMethodCallExpression(ctx.getText(), Collections.emptyList()));
            } else {
                ExpressionListener expressionListener = new ExpressionListener();
                expressionListener.enterArguments(ctx.arguments());
                expressions.add(astNodeCreator
                        .createMethodCallExpression(ctx.getText(), expressionListener.expressions()));
                expressionListener.clear();
            }
        } else {
            expressions.add(astNodeCreator
                    .createMethodBodyExpression(ExpressionType.OTHER, ctx.start.getText(),""));
        }
    }

    @Override
    public void enterExpression(ExpressionContext ctx) {
        if (ctx.unaryExpr() != null && ctx.unaryExpr().primaryExpr() != null) {
            enterPrimaryExpr(ctx.unaryExpr().primaryExpr());
        } else if (ctx.unaryExpr() != null) {
            enterUnaryExpr(ctx.unaryExpr());
        } else {
            expressions.add(astNodeCreator.createArgumentExpression(ctx.getText()));
        }
    }

    @Override
    public void enterUnaryExpr(UnaryExprContext ctx) {
        expressions.add(astNodeCreator
                .createMethodBodyExpression(ExpressionType.OTHER, ctx.getText(), ""));
    }

    @Override
    public void enterTypeDecl(TypeDeclContext ctx) {
        for (GolangParser.TypeSpecContext type : ctx.typeSpec()) {
            String name = type.IDENTIFIER().getText();
            expressions.add(astNodeCreator
                    .createMethodBodyExpression(ExpressionType.OTHER, name, type.type().getText()));
        }
    }

    @Override
    public void enterVarDecl(VarDeclContext ctx) {
        for (VarSpecContext varSpecContext : ctx.varSpec()) {
            expressions.add(astNodeCreator.createVarDeclarationExpression(varSpecContext.getText(), ""));
        }
    }

    @Override
    public void enterArguments(ArgumentsContext ctx) {
        expressions.add(astNodeCreator.createArgumentExpression(ctx.getText()));
    }

    List<Expression> expressions() {
        return expressions;
    }

    void clear() {
        expressions.clear();
    }
}
