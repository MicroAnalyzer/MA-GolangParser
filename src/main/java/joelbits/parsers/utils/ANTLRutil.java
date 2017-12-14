package joelbits.parsers.utils;

public class ANTLRutil {

    public static void generateAllANTLRFiles(String grammarPath) {
        String[] arg0 = {"-visitor","C:\\Users\\joel\\Desktop\\projects\\parser-golang\\src\\main\\java\\joelbits\\parsers\\golang\\Golang.g4"};
        org.antlr.v4.Tool.main(arg0);       // Generates alla files (listener/visitor/parser/lexer/etc)
    }
}
