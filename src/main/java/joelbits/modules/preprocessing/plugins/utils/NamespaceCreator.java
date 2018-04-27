package joelbits.modules.preprocessing.plugins.utils;

import joelbits.model.ast.protobuf.ASTProtos.Declaration;
import joelbits.model.ast.protobuf.ASTProtos.Namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NamespaceCreator {
    private final List<Namespace> namespaces = new ArrayList<>();

    public void createNamespace(String namespace, List<Declaration> declarations) {
        Namespace packageDeclaration = Namespace.newBuilder()
                .setName(namespace)
                .addAllDeclarations(declarations)
                .build();

        namespaces.add(packageDeclaration);
    }

    public List<Namespace> namespaces() {
        return Collections.unmodifiableList(namespaces);
    }
}
