package joelbits.parsers;

import joelbits.parsers.golang.GolangBaseListener;
import joelbits.parsers.golang.GolangParser;
import org.antlr.v4.runtime.tree.ErrorNode;

public class MyGolangBaseListener extends GolangBaseListener {

    @Override
    public void enterFunctionDecl(GolangParser.FunctionDeclContext ctx) {
        System.out.println("INNE I LYSSNAREN: " + ctx.IDENTIFIER());
    }

    @Override
    public void exitFunctionDecl(GolangParser.FunctionDeclContext ctx) {
        System.out.println("GÅR UT UR LYSSNAREN: " + ctx.IDENTIFIER());
        System.out.println("GÅR UT UR LYSSNAREN: " + ctx.IDENTIFIER().getText());
        System.out.println("GÅR UT UR LYSSNAREN: " + ctx.IDENTIFIER().getSymbol().getText());
        System.out.println("GÅR UT UR LYSSNAREN: " + ctx.IDENTIFIER().getParent().getText());
    }
}
