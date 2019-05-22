package com.github.javaparser.metamodel;

import com.github.javaparser.ast.expr.MemberValuePair;
import java.util.Optional;

public class MemberValuePairMetaModel extends NodeMetaModel {
   public PropertyMetaModel namePropertyMetaModel;
   public PropertyMetaModel valuePropertyMetaModel;

   MemberValuePairMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, MemberValuePair.class, "MemberValuePair", "com.github.javaparser.ast.expr", false, false);
   }
}
