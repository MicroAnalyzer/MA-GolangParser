package joelbits.modules.preprocessing.plugins.listeners;

import joelbits.model.ast.protobuf.ASTProtos.Variable;
import joelbits.model.ast.protobuf.ASTProtos.DeclarationType;
import joelbits.model.ast.protobuf.ASTProtos.Expression;
import joelbits.modules.preprocessing.plugins.golang.GolangBaseListener;
import joelbits.modules.preprocessing.plugins.golang.GolangParser;
import joelbits.modules.preprocessing.utils.ASTNodeCreator;

public final class FieldListener extends GolangBaseListener {
    private final ASTNodeCreator astNodeCreator = new ASTNodeCreator();
    private Variable variable;

    @Override
    public void enterVarDecl(GolangParser.VarDeclContext ctx) {
        for (GolangParser.VarSpecContext spec : ctx.varSpec()) {
            String name = spec.start.getText();
            String value = "";
            if (spec.expressionList() != null) {
                for (GolangParser.ExpressionContext expr : spec.expressionList().expression()) {
                    value = expr.getText();
                }
            }
            Expression expression = astNodeCreator
                    .createVarDeclarationExpression(value, name);
            variable = astNodeCreator.createVariable(name, astNodeCreator
                    .createType("var", DeclarationType.OTHER), expression);
        }
    }

    Variable getVariable() {
        return variable;
    }
}
