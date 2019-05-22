package com.github.javaparser.metamodel;

import com.github.javaparser.ast.CompilationUnit;
import java.util.Optional;

public class CompilationUnitMetaModel extends NodeMetaModel {
   public PropertyMetaModel importsPropertyMetaModel;
   public PropertyMetaModel modulePropertyMetaModel;
   public PropertyMetaModel packageDeclarationPropertyMetaModel;
   public PropertyMetaModel typesPropertyMetaModel;

   CompilationUnitMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, CompilationUnit.class, "CompilationUnit", "com.github.javaparser.ast", false, false);
   }
}
