package com.github.javaparser.ast.validator.chunks;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithTokenRange;
import com.github.javaparser.ast.validator.SimpleValidator;
import com.github.javaparser.ast.validator.SingleNodeTypeValidator;
import com.github.javaparser.ast.validator.TreeVisitorValidator;
import com.github.javaparser.ast.validator.Validators;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.metamodel.PropertyMetaModel;
import java.util.Iterator;

public class CommonValidators extends Validators {
   public CommonValidators() {
      super(new SimpleValidator(ClassOrInterfaceDeclaration.class, (n) -> {
         return !n.isInterface() && n.getExtendedTypes().size() > 1;
      }, (n, reporter) -> {
         reporter.report((NodeWithTokenRange)n.getExtendedTypes(1), "A class cannot extend more than one other class.");
      }), new SimpleValidator(ClassOrInterfaceDeclaration.class, (n) -> {
         return n.isInterface() && !n.getImplementedTypes().isEmpty();
      }, (n, reporter) -> {
         reporter.report((NodeWithTokenRange)n.getImplementedTypes(0), "An interface cannot implement other interfaces.");
      }), new SingleNodeTypeValidator(ClassOrInterfaceDeclaration.class, (n, reporter) -> {
         if (n.isInterface()) {
            n.getMembers().forEach((mem) -> {
               if (mem instanceof InitializerDeclaration) {
                  reporter.report((NodeWithTokenRange)mem, "An interface cannot have initializers.");
               }

            });
         }

      }), new SingleNodeTypeValidator(AssignExpr.class, (n, reporter) -> {
         Expression target;
         for(target = n.getTarget(); target instanceof EnclosedExpr; target = ((EnclosedExpr)target).getInner()) {
         }

         if (!(target instanceof NameExpr) && !(target instanceof ArrayAccessExpr) && !(target instanceof FieldAccessExpr)) {
            reporter.report((NodeWithTokenRange)n.getTarget(), "Illegal left hand side of an assignment.");
         }
      }), new TreeVisitorValidator((node, problemReporter) -> {
         NodeMetaModel mm = node.getMetaModel();
         Iterator var3 = mm.getAllPropertyMetaModels().iterator();

         while(var3.hasNext()) {
            PropertyMetaModel ppm = (PropertyMetaModel)var3.next();
            if (ppm.isNonEmpty() && ppm.isNodeList()) {
               NodeList value = (NodeList)ppm.getValue(node);
               if (value.isEmpty()) {
                  problemReporter.report((NodeWithTokenRange)node, "%s.%s can not be empty.", mm.getTypeName(), ppm.getName());
               }
            }
         }

      }));
   }
}
