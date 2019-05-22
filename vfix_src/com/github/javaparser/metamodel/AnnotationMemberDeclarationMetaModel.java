package com.github.javaparser.metamodel;

import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import java.util.Optional;

public class AnnotationMemberDeclarationMetaModel extends BodyDeclarationMetaModel {
   public PropertyMetaModel defaultValuePropertyMetaModel;
   public PropertyMetaModel modifiersPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;
   public PropertyMetaModel typePropertyMetaModel;

   AnnotationMemberDeclarationMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, AnnotationMemberDeclaration.class, "AnnotationMemberDeclaration", "com.github.javaparser.ast.body", false, false);
   }
}
