package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public abstract class VisitorValidator extends VoidVisitorAdapter<ProblemReporter> implements Validator {
   public void accept(Node node, ProblemReporter problemReporter) {
      node.accept(this, problemReporter);
   }
}
