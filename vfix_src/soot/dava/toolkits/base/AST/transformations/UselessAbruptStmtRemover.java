package soot.dava.toolkits.base.AST.transformations;

import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.ASTParentNodeFinder;
import soot.dava.toolkits.base.AST.traversals.LabelToNodeMapper;
import soot.jimple.Stmt;

public class UselessAbruptStmtRemover extends DepthFirstAdapter {
   public static boolean DEBUG = false;
   ASTParentNodeFinder finder = null;
   ASTMethodNode methodNode;
   LabelToNodeMapper mapper;

   public UselessAbruptStmtRemover() {
   }

   public UselessAbruptStmtRemover(boolean verbose) {
      super(verbose);
   }

   public void inASTMethodNode(ASTMethodNode node) {
      this.methodNode = node;
      this.mapper = new LabelToNodeMapper();
      this.methodNode.apply(this.mapper);
   }

   public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
      Iterator<AugmentedStmt> it = node.getStatements().iterator();
      AugmentedStmt remove = null;
      ASTLabeledNode target = null;

      while(true) {
         AugmentedStmt as;
         DAbruptStmt abrupt;
         Object temp;
         do {
            String label;
            do {
               Stmt s;
               do {
                  if (!it.hasNext()) {
                     if (remove != null) {
                        List<AugmentedStmt> stmts = node.getStatements();
                        stmts.remove(remove);
                        if (DEBUG) {
                           System.out.println("\tRemoved abrupt stmt");
                        }

                        if (target != null) {
                           if (DEBUG) {
                              System.out.println("Invoking findAndKill on the target");
                           }

                           UselessLabelFinder.v().findAndKill(target);
                        }

                        G.v().ASTTransformations_modified = true;
                        this.finder = null;
                     }

                     return;
                  }

                  as = (AugmentedStmt)it.next();
                  s = as.get_Stmt();
               } while(!(s instanceof DAbruptStmt));

               abrupt = (DAbruptStmt)s;
               label = abrupt.getLabel().toString();
            } while(label == null);

            if (it.hasNext()) {
               throw new DecompilationException("Dead code detected. Report to developer");
            }

            temp = this.mapper.getTarget(label);
         } while(temp == null);

         target = (ASTLabeledNode)temp;
         if (this.finder == null) {
            this.finder = new ASTParentNodeFinder();
            this.methodNode.apply(this.finder);
         }

         if (DEBUG) {
            System.out.println("Starting useless check for abrupt stmt: " + abrupt);
         }

         ASTNode ancestorsParent;
         for(Object ancestor = node; ancestor != target; ancestor = ancestorsParent) {
            Object tempParent = this.finder.getParentOf(ancestor);
            if (tempParent == null) {
               throw new DecompilationException("Parent found was null!!. Report to Developer");
            }

            ancestorsParent = (ASTNode)tempParent;
            if (DEBUG) {
               System.out.println("\tCurrent ancestorsParent has type" + ancestorsParent.getClass());
            }

            if (!this.checkChildLastInParent((ASTNode)ancestor, ancestorsParent)) {
               if (DEBUG) {
                  System.out.println("\t\tCurrent ancestorParent has more children after this ancestor");
               }

               return;
            }

            if (ancestorsParent instanceof ASTWhileNode || ancestorsParent instanceof ASTDoWhileNode || ancestorsParent instanceof ASTUnconditionalLoopNode || ancestorsParent instanceof ASTForLoopNode || ancestorsParent instanceof ASTSwitchNode) {
               if (DEBUG) {
                  System.out.println("\t\tAncestorsParent is a loop shouldnt remove abrupt stmt");
               }

               return;
            }
         }

         if (DEBUG) {
            System.out.println("\tGot to target without returning means we can remove stmt");
         }

         remove = as;
      }
   }

   public boolean checkChildLastInParent(ASTNode child, ASTNode parent) {
      List<Object> subBodies = parent.get_SubBodies();
      Iterator it = subBodies.iterator();

      List subBody;
      do {
         if (!it.hasNext()) {
            return false;
         }

         subBody = null;
         if (parent instanceof ASTTryNode) {
            subBody = (List)((ASTTryNode.container)it.next()).o;
         } else {
            subBody = (List)it.next();
         }
      } while(!subBody.contains(child));

      if (subBody.indexOf(child) != subBody.size() - 1) {
         return false;
      } else {
         return true;
      }
   }
}
