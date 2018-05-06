package joelbits.modules.preprocessing.plugins.listeners;

import joelbits.modules.preprocessing.plugins.golang.GolangBaseListener;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.ImportDeclContext;
import joelbits.modules.preprocessing.plugins.golang.GolangParser.ImportSpecContext;

import java.util.List;

public final class ImportListener extends GolangBaseListener {
    private final List<String> imports;

    public ImportListener(List<String> imports) {
        this.imports = imports;
    }

    @Override
    public void enterImportDecl(ImportDeclContext ctx) {
        for (ImportSpecContext importSpec : ctx.importSpec()) {
            imports.add(importSpec.getText().replaceAll("\"", ""));
        }
    }
}
