package soot.dava.internal.AST;

import soot.UnitPrinter;
import soot.dava.DavaUnitPrinter;
import soot.dava.toolkits.base.AST.analysis.Analysis;

public class ASTOrCondition extends ASTAggregatedCondition {
   public ASTOrCondition(ASTCondition left, ASTCondition right) {
      super(left, right);
   }

   public void apply(Analysis a) {
      a.caseASTOrCondition(this);
   }

   public String toString() {
      if (this.left instanceof ASTUnaryBinaryCondition) {
         if (this.right instanceof ASTUnaryBinaryCondition) {
            return this.not ? "!(" + this.left.toString() + " || " + this.right.toString() + ")" : this.left.toString() + " || " + this.right.toString();
         } else {
            return this.not ? "!(" + this.left.toString() + " || (" + this.right.toString() + " ))" : this.left.toString() + " || (" + this.right.toString() + " )";
         }
      } else if (this.right instanceof ASTUnaryBinaryCondition) {
         return this.not ? "!(( " + this.left.toString() + ") || " + this.right.toString() + ")" : "( " + this.left.toString() + ") || " + this.right.toString();
      } else {
         return this.not ? "!(( " + this.left.toString() + ") || (" + this.right.toString() + " ))" : "( " + this.left.toString() + ") || (" + this.right.toString() + " )";
      }
   }

   public void toString(UnitPrinter up) {
      if (up instanceof DavaUnitPrinter) {
         if (this.not) {
            ((DavaUnitPrinter)up).addNot();
            ((DavaUnitPrinter)up).addLeftParen();
         }

         if (this.left instanceof ASTUnaryBinaryCondition) {
            if (this.right instanceof ASTUnaryBinaryCondition) {
               this.left.toString(up);
               ((DavaUnitPrinter)up).addAggregatedOr();
               this.right.toString(up);
            } else {
               this.left.toString(up);
               ((DavaUnitPrinter)up).addAggregatedOr();
               ((DavaUnitPrinter)up).addLeftParen();
               this.right.toString(up);
               ((DavaUnitPrinter)up).addRightParen();
            }
         } else if (this.right instanceof ASTUnaryBinaryCondition) {
            ((DavaUnitPrinter)up).addLeftParen();
            this.left.toString(up);
            ((DavaUnitPrinter)up).addRightParen();
            ((DavaUnitPrinter)up).addAggregatedOr();
            this.right.toString(up);
         } else {
            ((DavaUnitPrinter)up).addLeftParen();
            this.left.toString(up);
            ((DavaUnitPrinter)up).addRightParen();
            ((DavaUnitPrinter)up).addAggregatedOr();
            ((DavaUnitPrinter)up).addLeftParen();
            this.right.toString(up);
            ((DavaUnitPrinter)up).addRightParen();
         }

         if (this.not) {
            ((DavaUnitPrinter)up).addRightParen();
         }

      } else {
         throw new RuntimeException();
      }
   }
}
