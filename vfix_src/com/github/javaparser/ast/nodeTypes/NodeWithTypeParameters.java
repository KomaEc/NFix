package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.TypeParameter;

public interface NodeWithTypeParameters<N extends Node> {
   NodeList<TypeParameter> getTypeParameters();

   default TypeParameter getTypeParameter(int i) {
      return (TypeParameter)this.getTypeParameters().get(i);
   }

   default N setTypeParameter(int i, TypeParameter typeParameter) {
      this.getTypeParameters().set(i, (Node)typeParameter);
      return (Node)this;
   }

   default N addTypeParameter(TypeParameter typeParameter) {
      this.getTypeParameters().add((Node)typeParameter);
      return (Node)this;
   }

   default N addTypeParameter(String typeParameter) {
      return this.addTypeParameter(JavaParser.parseTypeParameter(typeParameter));
   }

   N setTypeParameters(NodeList<TypeParameter> typeParameters);

   default boolean isGeneric() {
      return this.getTypeParameters().size() > 0;
   }
}
