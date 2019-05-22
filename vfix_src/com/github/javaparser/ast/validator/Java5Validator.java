package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithTokenRange;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import java.util.Optional;

public class Java5Validator extends Java1_4Validator {
   Validator genericsWithoutDiamondOperator = new TreeVisitorValidator((node, reporter) -> {
      if (node instanceof NodeWithTypeArguments) {
         Optional<NodeList<Type>> typeArguments = ((NodeWithTypeArguments)node).getTypeArguments();
         if (typeArguments.isPresent() && ((NodeList)typeArguments.get()).isEmpty()) {
            reporter.report((NodeWithTokenRange)node, "The diamond operator is not supported.");
         }
      }

   });
   protected Validator noPrimitiveGenericArguments = new TreeVisitorValidator((node, reporter) -> {
      if (node instanceof NodeWithTypeArguments) {
         Optional<NodeList<Type>> typeArguments = ((NodeWithTypeArguments)node).getTypeArguments();
         typeArguments.ifPresent((types) -> {
            types.forEach((ty) -> {
               if (ty instanceof PrimitiveType) {
                  reporter.report((NodeWithTokenRange)node, "Type arguments may not be primitive.");
               }

            });
         });
      }

   });
   protected final Validator enumNotAllowed = new ReservedKeywordValidator("enum");

   public Java5Validator() {
      this.replace(this.noGenerics, this.genericsWithoutDiamondOperator);
      this.add(this.noPrimitiveGenericArguments);
      this.add(this.enumNotAllowed);
      this.remove(this.noAnnotations);
      this.remove(this.noEnums);
      this.remove(this.noVarargs);
      this.remove(this.noForEach);
      this.remove(this.noStaticImports);
   }
}
