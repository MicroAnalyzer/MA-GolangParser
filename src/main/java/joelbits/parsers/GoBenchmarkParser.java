package joelbits.parsers;

import joelbits.parsers.golang.GolangLexer;
import joelbits.parsers.golang.GolangParser;
import joelbits.parsers.spi.Parser;
import joelbits.parsers.types.ParserType;
import org.antlr.v4.parse.ANTLRParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;

/**
 * the Benchmark functions have a “Benchmark” prefix.
 * What changes is also the argument from *testing.T to *testing.B.
 *
 * A feature complete example of a benchmark would be this:
 *
 * func BenchmarkStringJoin1(b *testing.B) {
 b.ReportAllocs()
 input := []string{"Hello", "World"}
 for i := 0; i < b.N; i++ {
 result := strings.Join(input, " ")
 if result != "Hello World" {
 b.Error("Unexpected result: " + result)
 }
 }
 * }
 *
 */
public class GoBenchmarkParser implements Parser {
    private static final Logger log = LoggerFactory.getLogger(GoBenchmarkParser.class);

    /**
     * WARNING: If you use both the deprecated and the new streams, you will see a nontrivial performance
     * degradation. This speed hit is because the Lexer's internal code goes from a monomorphic to megamorphic
     * dynamic dispatch to get characters from the input stream. Java's on-the-fly compiler (JIT) is unable to
     * perform the same optimizations so stick with either the old or the new streams, if performance is a
     * primary concern.
     *
     * @param args
     * @throws Exception
     */
    public static void main( String[] args) throws Exception  {
        // Lexing, i.e. partitioning the text into tokens
        // Parsing, i.e. building a parse tree from the tokens

        //Since lexing must preceed parsing there is a consequence: The lexer is independent of the parser, the parser
        // cannot influence lexing.

        File file = new File("C:\\Users\\joel\\Desktop\\thesis\\dataset\\Go\\gin-gonic\\gin\\benchmarks_test.go");
        org.antlr.v4.runtime.CharStream input = org.antlr.v4.runtime.CharStreams.fromStream(new FileInputStream(file));
        GolangLexer lexer = new GolangLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(MyBaseErrorListener.INSTANCE);
        org.antlr.v4.runtime.CommonTokenStream tokens = new CommonTokenStream(lexer);

        GolangParser parser = new GolangParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new MyErrorListener());
        //parser.addParseListener(new MyGolangBaseListener());
        //parser.addErrorListener(new MyErrorListener());
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        try {
            //System.out.println(parser.getBuildParseTree());
            //parser.stat();  // STAGE 1
            MyGolangBaseVisitor visitor = new MyGolangBaseVisitor();

            parser.reset();
            if (true) {
                visitor.visit(parser.packageClause());
                visitor.visit(parser.importDecl());
                Token token = parser.getCurrentToken();
                System.out.println(token.getText());
                System.out.println(token.getTokenSource());
                System.out.println(parser.getRuleContext());
                System.out.println("begin: " + parser.getTokenStream().getText() + "\n end");
                System.out.println(parser.getCurrentToken().getTokenIndex());
                System.out.println(parser.functionDecl().IDENTIFIER());
                System.out.println(parser.functionDecl().IDENTIFIER());

                visitor.visit(parser.functionDecl());
                visitor.visit(parser.functionDecl());
                visitor.visit(parser.functionDecl());
                GolangParser.FunctionDeclContext delc = parser.functionDecl();
                for (ParseTree tree : delc.children) {
                    System.out.println(tree.getText());
                    System.out.println(tree.getChildCount());
                    if (tree.getChildCount() > 0) {
                        for (int i = 0; i < tree.getChildCount(); i++) {
                            System.out.println(tree.getChild(i).getText());
                            System.out.println(tree.getChildCount());
                        }
                    }
                }
                visitor.visit(parser.functionDecl());
                visitor.visit(parser.functionDecl());
                visitor.visit(parser.functionDecl());
                visitor.visit(parser.functionDecl());
                visitor.visit(parser.functionDecl());
                return;
            }

            visitor.visitPackageClause(parser.packageClause());

            ParseTreeWalker walker = new ParseTreeWalker();
            //GolangParser.FunctionDeclContext func = parser.functionDecl();
            visitor.visitImportDecl(parser.importDecl());

            visitor.visitFunctionDecl(parser.functionDecl());
            visitor.visitFunctionDecl(parser.functionDecl());
            visitor.visitFunctionDecl(parser.functionDecl());
            visitor.visitFunctionDecl(parser.functionDecl());
            visitor.visitFunctionDecl(parser.functionDecl());
            visitor.visitFunctionDecl(parser.functionDecl());




            //visitor.visitFunctionDecl(parser.functionDecl());

//            GolangParser.FunctionDeclContext func = parser.functionDecl();
//
//            int childCount = func.getChildCount();
//            visitor.visit(func);
//            visitor.visit(parser.functionDecl());
//            visitor.visit(parser.functionDecl());
//            visitor.visit(parser.functionDecl());
//            visitor.visit(parser.functionDecl());
//            System.out.println();


//            visitor.visit(parser.functionDecl());
//            visitor.visit(parser.functionDecl());
//            for (int i = 0; i < childCount; i++) {
//                List<ParseTree> tree = parser.functionDecl().children;
//                for (ParseTree parseTree : tree) {
//                    System.out.println("BEGIN " + parseTree.getText() + " END");
//                    if (parseTree.getText().contains("testing.B")) {
//                        System.out.println("JA");
//                        visitor.visit(parseTree);
//                    }
//                }
//            }

            //visitor.visitFunctionDecl(parser.functionDecl());
            //visitor.visitMethodDecl(parser.methodDecl());
            //visitor.visitParameterList(parser.parameterList());
            //visitor.visitReturnStmt(parser.returnStmt());
            //visitor.visitSignature(parser.signature());
            //visitor.visit();
        }
        catch (Exception ex) {
            tokens.reset(); // rewind input stream
            parser.reset();
            parser.getInterpreter().setPredictionMode(PredictionMode.LL);
            //parser.stat();  // STAGE 2
            // if we parse ok, it's LL not SLL
        }

        // Get the parse tree from your parser, then traverse it using a visitor.
        //ParseTreeWalker.DEFAULT.walk(listener, tree);

        //System.out.println(parser.action().getTree().getChildrenAsArray());



        //ParserInterpreter parserInterpreter = grammar.createParserInterpreter(new org.antlr.v4.runtime.CommonTokenStream(xPathLexer));
        //System.out.println(parserInterpreter.getContext());

        //MarkupVisitor visitor = new MarkupVisitor();
        //visitor.visit(fileContext);
    }

    @Override
    public void parse(String filePath) {
        int test = ANTLRParser.PLUS;
        //Token symbol1 = CommonTokenFactory.DEFAULT.create(1, "public");
        //Token symbol2 = CommonTokenFactory.DEFAULT.create(2, "void");
        //new ANTLRParser(new CommonTokenStream(new ListTokenSource(Arrays.asList(symbol1, symbol2))));
//        TokenSource tokenSource = new LexerInterpreter();
//        TokenStream tokenStream = new CommonTokenStream(tokenSource);
//        ANTLRParser parser = new ANTLRParser(tokenStream);
    }

//    static class MarkupVisitor extends MarkupParserBaseVisitor<String> {
//        @Override
//        public String visitFile(MarkupParser.FileContext context) {
//            visitChildren(context);
//            System.out.println("");
//
//            return null;            // stops visitor
//        }
//
//        @Override
//        public String visitContent(MarkupParser.ContentContext context) {
//            System.out.print(context.TEXT().getText());
//
//            return visitChildren(context);      // control flow
//        }

        /**
         *     That’s how the visitor works

         every top element visit each child
         if it’s a content node, it directly returns the text
         if it’s a tag, it setups the correct delimiters and then it checks its children. It repeats step 2 for each
         children and then it returns the gathered text
         it prints the returned text

         Together with the patterns that we have seen at the beginning of this section you can see all of the options:
         to return null to stop the visit, to
         return children to continue, to return something to perform an action ordered at an higher level of the tree.

         //* @param context
         * @return
         */
//        @Override
//        public String visitTag(MarkupParser.TagContext context)
//        {
//            String text = "";
//            String startDelimiter = "", endDelimiter = "";
//
//            String id = context.ID(0).getText();
//
//            switch(id)
//            {
//                case "b":
//                    startDelimiter = endDelimiter = "**";
//                    break;
//                case "u":
//                    startDelimiter = endDelimiter = "*";
//                    break;
//                case "quote":
//                    String attribute = context.attribute().STRING().getText();
//                    attribute = attribute.substring(1,attribute.length()-1);
//                    startDelimiter = System.lineSeparator() + "> ";
//                    endDelimiter = System.lineSeparator() + "> " + System.lineSeparator() + "> – "
//                            + attribute + System.lineSeparator();
//                    break;
//            }
//
//            text += startDelimiter;
//
//            for (MarkupParser.ElementContext node: context.element())
//            {
//                if(node.tag() != null)
//                    text += visitTag(node.tag());
//                if(node.content() != null)
//                    text += visitContent(node.content());
//            }
//
//            text += endDelimiter;
//
//            return text;
//        }
//
//        private MarkupParser setup(String input)
//        {
//            ANTLRInputStream inputStream = null;
//            try {
//                inputStream = new ANTLRInputStream(input);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            this.markupLexer = new Lexer(inputStream);
//            CommonTokenStream commonTokenStream = new CommonTokenStream(markupLexer);
//            MarkupParser markupParser = new MarkupParser(commonTokenStream);
//
//            StringWriter writer = new StringWriter();
//            this.errorListener = new MarkupErrorListener(writer);
//            markupLexer.removeErrorListeners();
//            //markupLexer.addErrorListener(errorListener);
//            markupParser.removeErrorListeners();
//            markupParser.addErrorListener(errorListener);
//
//            return markupParser;
//        }
//    }

    @Override
    public String type() {
        return ParserType.GO.name();
    }

    @Override
    public String toString() {
        return "GoBenchmarkParser";
    }
}
