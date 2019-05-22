package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.Local;
import soot.Value;
import soot.dava.DavaBody;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.ASTParentNodeFinder;
import soot.dava.toolkits.base.AST.traversals.ASTUsesAndDefs;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;

public class LocalVariableCleaner extends DepthFirstAdapter {
   public final boolean DEBUG = false;
   ASTNode AST;
   ASTUsesAndDefs useDefs;
   ASTParentNodeFinder parentOf;

   public LocalVariableCleaner(ASTNode AST) {
      this.AST = AST;
      this.parentOf = new ASTParentNodeFinder();
      AST.apply(this.parentOf);
   }

   public LocalVariableCleaner(boolean verbose, ASTNode AST) {
      super(verbose);
      this.AST = AST;
      this.parentOf = new ASTParentNodeFinder();
      AST.apply(this.parentOf);
   }

   public void outASTMethodNode(ASTMethodNode node) {
      boolean redo = false;
      this.useDefs = new ASTUsesAndDefs(this.AST);
      this.AST.apply(this.useDefs);
      Iterator decIt = node.getDeclaredLocals().iterator();
      ArrayList removeList = new ArrayList();

      while(true) {
         while(decIt.hasNext()) {
            Local var = (Local)decIt.next();
            List<DefinitionStmt> defs = this.getDefs(var);
            if (defs.size() == 0) {
               removeList.add(var);
            } else {
               Iterator defIt = defs.iterator();

               while(defIt.hasNext()) {
                  DefinitionStmt ds = (DefinitionStmt)defIt.next();
                  if (this.canRemoveDef(ds)) {
                     redo = this.removeStmt(ds);
                  }
               }
            }
         }

         for(Iterator remIt = removeList.iterator(); remIt.hasNext(); redo = true) {
            Local removeLocal = (Local)remIt.next();
            node.removeDeclaredLocal(removeLocal);
            if (!(this.AST instanceof ASTMethodNode)) {
               throw new DecompilationException("found AST which is not a methodNode");
            }

            DavaBody body = ((ASTMethodNode)this.AST).getDavaBody();
            Collection<Local> localChain = body.getLocals();
            if (removeLocal != null && localChain != null) {
               localChain.remove(removeLocal);
            }
         }

         if (redo) {
            this.outASTMethodNode(node);
         }

         return;
      }
   }

   public boolean canRemoveDef(DefinitionStmt ds) {
      List uses = this.useDefs.getDUChain(ds);
      if (uses.size() != 0) {
         return false;
      } else {
         return ds.getRightOp() instanceof Local || ds.getRightOp() instanceof Constant;
      }
   }

   public List<DefinitionStmt> getDefs(Local var) {
      List<DefinitionStmt> toReturn = new ArrayList();
      HashMap<Object, List> dU = this.useDefs.getDUHashMap();
      Iterator it = dU.keySet().iterator();

      while(it.hasNext()) {
         DefinitionStmt s = (DefinitionStmt)it.next();
         Value left = s.getLeftOp();
         if (left instanceof Local && ((Local)left).getName().compareTo(var.getName()) == 0) {
            toReturn.add(s);
         }
      }

      return toReturn;
   }

   public boolean removeStmt(Stmt stmt) {
      Object tempParent = this.parentOf.getParentOf(stmt);
      if (tempParent == null) {
         return false;
      } else {
         ASTNode parent = (ASTNode)tempParent;
         if (!(parent instanceof ASTStatementSequenceNode)) {
            return false;
         } else {
            ASTStatementSequenceNode parentNode = (ASTStatementSequenceNode)parent;
            ArrayList<AugmentedStmt> newSequence = new ArrayList();
            int size = parentNode.getStatements().size();
            Iterator var7 = parentNode.getStatements().iterator();

            while(var7.hasNext()) {
               AugmentedStmt as = (AugmentedStmt)var7.next();
               Stmt s = as.get_Stmt();
               if (s.toString().compareTo(stmt.toString()) != 0) {
                  newSequence.add(as);
               }
            }

            parentNode.setStatements(newSequence);
            if (newSequence.size() < size) {
               return true;
            } else {
               return false;
            }
         }
      }
   }
}
