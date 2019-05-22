package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import java.util.Iterator;
import java.util.List;

public class ControlFlowLogic {
   private static ControlFlowLogic instance = new ControlFlowLogic();

   public static ControlFlowLogic getInstance() {
      return instance;
   }

   private ControlFlowLogic() {
   }

   public Statement breakTarget(BreakStmt breakStmt) {
      throw new UnsupportedOperationException();
   }

   public boolean exitTheStatement(BreakStmt breakStmt) {
      if (!this.isReachable(breakStmt)) {
         return false;
      } else {
         Statement breakTarget = this.breakTarget(breakStmt);
         Iterator var3 = this.containedTryStmts(breakTarget).iterator();

         TryStmt tryStmt;
         do {
            if (!var3.hasNext()) {
               return true;
            }

            tryStmt = (TryStmt)var3.next();
         } while(!this.contains(tryStmt.getTryBlock(), breakStmt) || tryStmt.getFinallyBlock().isPresent() || this.canCompleteNormally((Statement)tryStmt.getFinallyBlock().get()));

         return false;
      }
   }

   public boolean continueADoStatement(ContinueStmt continueStmt, DoStmt doStmt) {
      Iterator var3 = this.containedTryStmts(continueStmt).iterator();

      TryStmt tryStmt;
      do {
         if (!var3.hasNext()) {
            return true;
         }

         tryStmt = (TryStmt)var3.next();
      } while(!this.contains(tryStmt.getTryBlock(), continueStmt) || tryStmt.getFinallyBlock().isPresent() || this.canCompleteNormally((Statement)tryStmt.getFinallyBlock().get()));

      return false;
   }

   private boolean contains(Statement container, Statement contained) {
      throw new UnsupportedOperationException();
   }

   private List<TryStmt> containedTryStmts(Statement statement) {
      throw new UnsupportedOperationException();
   }

   private <P extends Node> boolean parentIs(Node node, Class<P> parentClass) {
      return node.getParentNode().isPresent() ? parentClass.isInstance(node.getParentNode().get()) : false;
   }

   public boolean canCompleteNormally(final Statement statement) {
      if (!this.isReachable(statement)) {
         return false;
      } else {
         GenericVisitor<Boolean, Void> visitor = new GenericVisitorAdapter<Boolean, Void>() {
            public Boolean visit(BlockStmt n, Void arg) {
               if (n.isEmpty() && !ControlFlowLogic.this.parentIs(statement, SwitchStmt.class)) {
                  return ControlFlowLogic.this.isReachable(statement);
               } else if (!n.isEmpty() && !ControlFlowLogic.this.parentIs(statement, SwitchStmt.class)) {
                  return ControlFlowLogic.this.canCompleteNormally(n.getStatement(n.getStatements().size() - 1));
               } else {
                  throw new UnsupportedOperationException();
               }
            }

            public Boolean visit(LabeledStmt n, Void arg) {
               throw new UnsupportedOperationException();
            }

            public Boolean visit(EmptyStmt n, Void arg) {
               return ControlFlowLogic.this.isReachable(n);
            }

            public Boolean visit(LocalClassDeclarationStmt n, Void arg) {
               return ControlFlowLogic.this.isReachable(n);
            }

            public Boolean visit(IfStmt n, Void arg) {
               return !n.getElseStmt().isPresent() ? ControlFlowLogic.this.isReachable(n) : ControlFlowLogic.this.canCompleteNormally(n.getThenStmt()) || ControlFlowLogic.this.canCompleteNormally((Statement)n.getElseStmt().get());
            }

            public Boolean visit(AssertStmt n, Void arg) {
               return ControlFlowLogic.this.isReachable(n);
            }

            public Boolean visit(ExpressionStmt n, Void arg) {
               if (n.getExpression() instanceof VariableDeclarationExpr) {
                  VariableDeclarationExpr expr = (VariableDeclarationExpr)n.getExpression();
                  return ControlFlowLogic.this.isReachable(n);
               } else {
                  return ControlFlowLogic.this.isReachable(n);
               }
            }
         };
         return (Boolean)statement.accept(visitor, (Object)null);
      }
   }

   private boolean isReachableBecauseOfPosition(Statement statement) {
      throw new UnsupportedOperationException();
   }

   public boolean isReachable(final Statement statement) {
      GenericVisitor<Boolean, Void> visitor = new GenericVisitorAdapter<Boolean, Void>() {
         public Boolean visit(BlockStmt n, Void arg) {
            if (statement.getParentNode().isPresent()) {
               if (statement.getParentNode().get() instanceof ConstructorDeclaration) {
                  return true;
               }

               if (statement.getParentNode().get() instanceof MethodDeclaration) {
                  return true;
               }

               if (statement.getParentNode().get() instanceof InitializerDeclaration) {
                  return true;
               }
            }

            return ControlFlowLogic.this.isReachableBecauseOfPosition(statement);
         }

         public Boolean visit(LocalClassDeclarationStmt n, Void arg) {
            return (Boolean)super.visit((LocalClassDeclarationStmt)n, arg);
         }
      };
      return (Boolean)statement.accept(visitor, (Object)null);
   }
}
