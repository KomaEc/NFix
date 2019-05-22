package com.github.javaparser.metamodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration;
import java.util.Optional;

public class CallableDeclarationMetaModel extends BodyDeclarationMetaModel {
   public PropertyMetaModel modifiersPropertyMetaModel;
   public PropertyMetaModel namePropertyMetaModel;
   public PropertyMetaModel parametersPropertyMetaModel;
   public PropertyMetaModel receiverParameterPropertyMetaModel;
   public PropertyMetaModel thrownExceptionsPropertyMetaModel;
   public PropertyMetaModel typeParametersPropertyMetaModel;

   CallableDeclarationMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, CallableDeclaration.class, "CallableDeclaration", "com.github.javaparser.ast.body", true, true);
   }

   protected CallableDeclarationMetaModel(Optional<BaseNodeMetaModel> superNodeMetaModel, Class<? extends Node> type, String name, String packageName, boolean isAbstract, boolean hasWildcard) {
      super(superNodeMetaModel, type, name, packageName, isAbstract, hasWildcard);
   }
}
