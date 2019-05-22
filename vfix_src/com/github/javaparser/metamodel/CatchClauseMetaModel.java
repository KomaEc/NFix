package com.github.javaparser.metamodel;

import com.github.javaparser.ast.stmt.CatchClause;
import java.util.Optional;

public class CatchClauseMetaModel extends NodeMetaModel {
   public PropertyMetaModel bodyPropertyMetaModel;
   public PropertyMetaModel parameterPropertyMetaModel;

   CatchClauseMetaModel(Optional<BaseNodeMetaModel> superBaseNodeMetaModel) {
      super(superBaseNodeMetaModel, CatchClause.class, "CatchClause", "com.github.javaparser.ast.stmt", false, false);
   }
}
