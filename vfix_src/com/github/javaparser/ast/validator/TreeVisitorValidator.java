package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.Node;
import java.util.Iterator;

public class TreeVisitorValidator implements Validator {
   private final Validator validator;

   public TreeVisitorValidator(Validator validator) {
      this.validator = validator;
   }

   public final void accept(Node node, ProblemReporter reporter) {
      this.validator.accept(node, reporter);
      Iterator var3 = node.getChildNodes().iterator();

      while(var3.hasNext()) {
         Node child = (Node)var3.next();
         this.accept(child, reporter);
      }

   }
}
