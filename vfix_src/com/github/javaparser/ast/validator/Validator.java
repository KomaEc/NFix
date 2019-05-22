package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.Node;

public interface Validator extends TypedValidator<Node> {
   void accept(Node node, ProblemReporter problemReporter);
}
