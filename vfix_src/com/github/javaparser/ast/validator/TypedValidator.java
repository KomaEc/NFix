package com.github.javaparser.ast.validator;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.Node;
import java.util.function.BiConsumer;

public interface TypedValidator<N extends Node> extends BiConsumer<N, ProblemReporter> {
   void accept(N node, ProblemReporter problemReporter);

   default ParseResult.PostProcessor postProcessor() {
      return (result, configuration) -> {
         result.getResult().ifPresent((node) -> {
            this.accept(node, new ProblemReporter((problem) -> {
               result.getProblems().add(problem);
            }));
         });
      };
   }
}
