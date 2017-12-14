package joelbits.parsers;

import joelbits.parsers.golang.GolangBaseVisitor;
import joelbits.parsers.golang.GolangParser;

public class MyGolangBaseVisitor<T> extends GolangBaseVisitor<T> {

    @Override
    public T visitPackageClause(GolangParser.PackageClauseContext ctx) {
        System.out.println("PackageClause!!!!   " + ctx.getText());
        System.out.println("PackageClause!!!!   " + ctx.start.getText());
        System.out.println("PackageClause!!!!   " + ctx.stop.getText());
        return visitChildren(ctx);
    }

    @Override
    public T visitMethodDecl(GolangParser.MethodDeclContext ctx) {
        System.out.println("MethodDecl!!!!   " + ctx.getText());
        System.out.println("MethodDecl!!!!   " + ctx.start.getText());
        System.out.println("MethodDecl!!!!   " + ctx.stop.getText());
        return visitChildren(ctx);
    }

    @Override
    public T visitSignature(GolangParser.SignatureContext ctx) {
//        System.out.println("Signature!!!!   " + ctx.getText());
//        System.out.println("Signature!!!!   " + ctx.start.getText());
//        System.out.println("Signature!!!!   " + ctx.stop.getText());
        return visitChildren(ctx);
    }

    @Override
    public T visitParameterList(GolangParser.ParameterListContext ctx) {
//        System.out.println("ParameterList!!!!   " + ctx.getText());
//        System.out.println("ParameterList!!!!   " + ctx.start.getText());
//        System.out.println("ParameterList!!!!   " + ctx.stop.getText());
        return visitChildren(ctx);
    }

    @Override
    public T visitFunction(GolangParser.FunctionContext ctx) {
        System.out.println("Function!!!!   " + ctx.getText());
//        System.out.println("Function!!!!   " + ctx.start.getText());
//        System.out.println("Function!!!!   " + ctx.stop.getText());
        return null;
    }

    @Override
    public T visitFunctionDecl(GolangParser.FunctionDeclContext ctx) {
        //System.out.println("FunctionDecl!!!!   " + ctx.getText());
        System.out.println("INNE: " + ctx.IDENTIFIER().getSymbol().getText());
        if (ctx.IDENTIFIER().getSymbol().getText().contains("Benchmark")) {
            System.out.println("Ja");
        }

//        System.out.println("FunctionDecl!!!!   " + ctx.function().signature().getText());
//        System.out.println("FunctionDecl!!!!   " + ctx.IDENTIFIER().getSymbol().getText());
//        System.out.println("FunctionDecl!!!!   " + ctx.getChildCount());
        //System.out.println(ctx.getText().contains("*testing.B"));

        //return visitChildren(ctx);      // continue to children -> visitFunction(..)
        return null;
    }

    @Override
    public T visitReturnStmt(GolangParser.ReturnStmtContext ctx) {
//        System.out.println("ReturnStmt!!!!   " + ctx.getText());
//        System.out.println("ReturnStmt!!!!   " + ctx.start.getText());
//        System.out.println("ReturnStmt!!!!   " + ctx.stop.getText());
        return visitChildren(ctx);
    }

    @Override
    public T visitImportDecl(GolangParser.ImportDeclContext ctx) {
        System.out.println("ImportDecl!!!!   " + ctx.getText());
        System.out.println("ImportDecl!!!!   " + ctx.start.getText());
        System.out.println("ImportDecl!!!!   " + ctx.stop.getText());
        return visitChildren(ctx);
    }
}
