package joelbits.modules.preprocessing.plugins.listeners;

import joelbits.modules.preprocessing.plugins.golang.GolangBaseListener;
import joelbits.modules.preprocessing.plugins.golang.GolangParser;

import java.util.List;

public final class ImportListener extends GolangBaseListener {
    private final List<String> imports;

    public ImportListener(List<String> imports) {
        this.imports = imports;
    }

    @Override
    public void enterImportDecl(GolangParser.ImportDeclContext ctx) {
        for (GolangParser.ImportSpecContext importSpec : ctx.importSpec()) {
            imports.add(importSpec.getText().replaceAll("\"", ""));
        }
    }
}
