package com.github.javaparser.version;

import com.github.javaparser.ParseResult;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.VarType;

public class Java10PostProcessor extends PostProcessors {
   protected final ParseResult.PostProcessor varNodeCreator = (result, configuration) -> {
      result.getResult().ifPresent((node) -> {
         node.findAll(ClassOrInterfaceType.class).forEach((n) -> {
            if (n.getNameAsString().equals("var")) {
               n.replace(new VarType((TokenRange)n.getTokenRange().orElse((Object)null)));
            }

         });
      });
   };

   public Java10PostProcessor() {
      super();
      this.add(this.varNodeCreator);
   }
}
