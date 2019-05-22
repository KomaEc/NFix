package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import soot.ByteType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.PrimType;
import soot.ShortType;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.grimp.internal.GCastExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;

public class TypeCastingError extends DepthFirstAdapter {
   public boolean myDebug = false;

   public TypeCastingError() {
   }

   public TypeCastingError(boolean verbose) {
      super(verbose);
   }

   public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
      Iterator var2 = node.getStatements().iterator();

      while(true) {
         while(true) {
            ValueBox rightBox;
            Value right;
            Type leftType;
            Type rightType;
            do {
               while(true) {
                  Stmt s;
                  do {
                     if (!var2.hasNext()) {
                        return;
                     }

                     AugmentedStmt as = (AugmentedStmt)var2.next();
                     s = as.get_Stmt();
                  } while(!(s instanceof DefinitionStmt));

                  DefinitionStmt ds = (DefinitionStmt)s;
                  if (this.myDebug) {
                     System.out.println("Definition stmt" + ds);
                  }

                  rightBox = ds.getRightOpBox();
                  ValueBox leftBox = ds.getLeftOpBox();
                  right = rightBox.getValue();
                  Value left = leftBox.getValue();
                  if (left.getType() instanceof PrimType && right.getType() instanceof PrimType) {
                     leftType = left.getType();
                     rightType = right.getType();
                     if (this.myDebug) {
                        System.out.println("Left type is: " + leftType);
                     }

                     if (this.myDebug) {
                        System.out.println("Right type is: " + rightType);
                     }

                     if (leftType.equals(rightType)) {
                        if (this.myDebug) {
                           System.out.println("\tTypes are the same");
                        }

                        if (this.myDebug) {
                           System.out.println("Right value is of instance" + right.getClass());
                        }
                     }
                     break;
                  }

                  if (this.myDebug) {
                     System.out.println("\tDefinition stmt does not contain prims no need to modify");
                  }
               }
            } while(leftType.equals(rightType));

            if (this.myDebug) {
               System.out.println("\tDefinition stmt has to be modified");
            }

            if (leftType instanceof ByteType && (rightType instanceof DoubleType || rightType instanceof FloatType || rightType instanceof IntType || rightType instanceof LongType || rightType instanceof ShortType)) {
               if (this.DEBUG) {
                  System.out.println("Explicit casting to BYTE required");
               }

               rightBox.setValue(new GCastExpr(right, ByteType.v()));
               if (this.DEBUG) {
                  System.out.println("New right expr is " + rightBox.getValue().toString());
               }
            } else if (!(leftType instanceof ShortType) || !(rightType instanceof DoubleType) && !(rightType instanceof FloatType) && !(rightType instanceof IntType) && !(rightType instanceof LongType)) {
               if (leftType instanceof IntType && (rightType instanceof DoubleType || rightType instanceof FloatType || rightType instanceof LongType)) {
                  if (this.myDebug) {
                     System.out.println("Explicit casting to INT required");
                  }

                  rightBox.setValue(new GCastExpr(right, IntType.v()));
                  if (this.myDebug) {
                     System.out.println("New right expr is " + rightBox.getValue().toString());
                  }
               } else if (leftType instanceof LongType && (rightType instanceof DoubleType || rightType instanceof FloatType)) {
                  if (this.DEBUG) {
                     System.out.println("Explicit casting to LONG required");
                  }

                  rightBox.setValue(new GCastExpr(right, LongType.v()));
                  if (this.DEBUG) {
                     System.out.println("New right expr is " + rightBox.getValue().toString());
                  }
               } else if (leftType instanceof FloatType && rightType instanceof DoubleType) {
                  if (this.DEBUG) {
                     System.out.println("Explicit casting to FLOAT required");
                  }

                  rightBox.setValue(new GCastExpr(right, FloatType.v()));
                  if (this.DEBUG) {
                     System.out.println("New right expr is " + rightBox.getValue().toString());
                  }
               }
            } else {
               if (this.DEBUG) {
                  System.out.println("Explicit casting to SHORT required");
               }

               rightBox.setValue(new GCastExpr(right, ShortType.v()));
               if (this.DEBUG) {
                  System.out.println("New right expr is " + rightBox.getValue().toString());
               }
            }
         }
      }
   }
}
