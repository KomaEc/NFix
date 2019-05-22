package com.github.javaparser.metamodel;

import com.github.javaparser.ast.body.AnnotationDeclaration;
import java.util.Optional;

public class AnnotationDeclarationMetaModel extends TypeDeclarationMetaModel {
   AnnotationDeclarationMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, AnnotationDeclaration.class, "AnnotationDeclaration", "com.github.javaparser.ast.body", false, false);
   }
}
