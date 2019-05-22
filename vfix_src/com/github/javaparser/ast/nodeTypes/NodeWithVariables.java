package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.metamodel.DerivedProperty;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface NodeWithVariables<N extends Node> {
   NodeList<VariableDeclarator> getVariables();

   N setVariables(NodeList<VariableDeclarator> variables);

   default VariableDeclarator getVariable(int i) {
      return (VariableDeclarator)this.getVariables().get(i);
   }

   default N setVariable(int i, VariableDeclarator variableDeclarator) {
      this.getVariables().set(i, (Node)variableDeclarator);
      return (Node)this;
   }

   default N addVariable(VariableDeclarator variableDeclarator) {
      this.getVariables().add((Node)variableDeclarator);
      return (Node)this;
   }

   default Type getCommonType() {
      NodeList<VariableDeclarator> variables = this.getVariables();
      if (variables.isEmpty()) {
         throw new AssertionError("There is no common type since there are no variables.");
      } else {
         Type type = ((VariableDeclarator)variables.get(0)).getType();

         for(int i = 1; i < variables.size(); ++i) {
            if (!((VariableDeclarator)variables.get(i)).getType().equals(type)) {
               throw new AssertionError("The variables do not have a common type.");
            }
         }

         return type;
      }
   }

   default Type getElementType() {
      NodeList<VariableDeclarator> variables = this.getVariables();
      if (variables.isEmpty()) {
         throw new AssertionError("There is no element type since there are no variables.");
      } else {
         Type type = ((VariableDeclarator)variables.get(0)).getType().getElementType();

         for(int i = 1; i < variables.size(); ++i) {
            if (!((VariableDeclarator)variables.get(i)).getType().getElementType().equals(type)) {
               throw new AssertionError("The variables do not have a common type.");
            }
         }

         return type;
      }
   }

   @DerivedProperty
   default Optional<Type> getMaximumCommonType() {
      return calculateMaximumCommonType((List)this.getVariables().stream().map((v) -> {
         return v.getType();
      }).collect(Collectors.toList()));
   }

   static Optional<Type> calculateMaximumCommonType(List<Type> types) {
      class Helper {
         private Optional<Type> toArrayLevel(Type type, int level) {
            if (level > type.getArrayLevel()) {
               return Optional.empty();
            } else {
               for(int i = type.getArrayLevel(); i > level; --i) {
                  if (!(type instanceof ArrayType)) {
                     return Optional.empty();
                  }

                  type = ((ArrayType)type).getComponentType();
               }

               return Optional.of(type);
            }
         }
      }

      Helper helper = new Helper();
      int level = 0;
      boolean keepGoing = true;

      while(true) {
         while(keepGoing) {
            Object[] values = types.stream().map((v) -> {
               Optional<Type> t = helper.toArrayLevel(v, level);
               return (String)t.map(Node::toString).orElse((Object)null);
            }).distinct().toArray();
            if (values.length == 1 && values[0] != null) {
               ++level;
            } else {
               keepGoing = false;
            }
         }

         Type var10001 = (Type)types.get(0);
         --level;
         return helper.toArrayLevel(var10001, level);
      }
   }
}
