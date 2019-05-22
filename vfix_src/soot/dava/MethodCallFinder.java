package soot.dava;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.ASTParentNodeFinder;
import soot.grimp.internal.GNewInvokeExpr;
import soot.grimp.internal.GThrowStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;

public class MethodCallFinder extends DepthFirstAdapter {
   ASTMethodNode underAnalysis;
   DavaStaticBlockCleaner cleaner;

   public MethodCallFinder(DavaStaticBlockCleaner cleaner) {
      this.cleaner = cleaner;
      this.underAnalysis = null;
   }

   public MethodCallFinder(boolean verbose, DavaStaticBlockCleaner cleaner) {
      super(verbose);
      this.cleaner = cleaner;
      this.underAnalysis = null;
   }

   public void inASTMethodNode(ASTMethodNode node) {
      this.underAnalysis = node;
   }

   public void inInvokeStmt(InvokeStmt s) {
      InvokeExpr invokeExpr = s.getInvokeExpr();
      SootMethod maybeInline = invokeExpr.getMethod();
      ASTMethodNode toInlineASTMethod = this.cleaner.inline(maybeInline);
      if (toInlineASTMethod != null) {
         List<Object> subBodies = toInlineASTMethod.get_SubBodies();
         if (subBodies.size() != 1) {
            throw new RuntimeException("Found ASTMEthod node with more than one subBodies");
         } else {
            List body = (List)subBodies.get(0);
            ASTParentNodeFinder finder = new ASTParentNodeFinder();
            this.underAnalysis.apply(finder);
            List<ASTStatementSequenceNode> newChangedBodyPart = this.createChangedBodyPart(s, body, finder);
            boolean replaced = this.replaceSubBody(s, newChangedBodyPart, finder);
            if (replaced) {
               StaticDefinitionFinder defFinder = new StaticDefinitionFinder(maybeInline);
               toInlineASTMethod.apply(defFinder);
               if (defFinder.anyFinalFieldDefined()) {
                  SootClass runtime = Scene.v().loadClassAndSupport("java.lang.RuntimeException");
                  if (runtime.declaresMethod("void <init>(java.lang.String)")) {
                     SootMethod sootMethod = runtime.getMethod("void <init>(java.lang.String)");
                     SootMethodRef methodRef = sootMethod.makeRef();
                     RefType myRefType = RefType.v(runtime);
                     StringConstant tempString = StringConstant.v("This method used to have a definition of a final variable. Dava inlined the definition into the static initializer");
                     List list = new ArrayList();
                     list.add(tempString);
                     GNewInvokeExpr newInvokeExpr = new GNewInvokeExpr(myRefType, methodRef, list);
                     GThrowStmt throwStmt = new GThrowStmt(newInvokeExpr);
                     AugmentedStmt augStmt = new AugmentedStmt(throwStmt);
                     List<AugmentedStmt> sequence = new ArrayList();
                     sequence.add(augStmt);
                     ASTStatementSequenceNode seqNode = new ASTStatementSequenceNode(sequence);
                     List<Object> subBody = new ArrayList();
                     subBody.add(seqNode);
                     toInlineASTMethod.replaceBody(subBody);
                  }
               }
            }

         }
      }
   }

   public List<Object> getSubBodyFromSingleSubBodyNode(ASTNode node) {
      List<Object> subBodies = node.get_SubBodies();
      if (subBodies.size() != 1) {
         throw new RuntimeException("Found a single subBody node with more than 1 subBodies");
      } else {
         return (List)subBodies.get(0);
      }
   }

   public List<Object> createNewSubBody(List<Object> orignalBody, List<ASTStatementSequenceNode> partNewBody, Object stmtSeqNode) {
      List<Object> newBody = new ArrayList();
      Iterator it = orignalBody.iterator();

      while(it.hasNext()) {
         Object temp = it.next();
         if (temp == stmtSeqNode) {
            break;
         }

         newBody.add(temp);
      }

      newBody.addAll(partNewBody);

      while(it.hasNext()) {
         newBody.add(it.next());
      }

      return newBody;
   }

   public boolean replaceSubBody(InvokeStmt s, List<ASTStatementSequenceNode> newChangedBodyPart, ASTParentNodeFinder finder) {
      Object stmtSeqNode = finder.getParentOf(s);
      Object ParentOfStmtSeq = finder.getParentOf(stmtSeqNode);
      if (ParentOfStmtSeq == null) {
         throw new RuntimeException("MethodCall FInder: parent of stmt seq node not found");
      } else {
         ASTNode node = (ASTNode)ParentOfStmtSeq;
         List tryBody;
         List ifBody;
         if (node instanceof ASTMethodNode) {
            tryBody = this.getSubBodyFromSingleSubBodyNode(node);
            ifBody = this.createNewSubBody(tryBody, newChangedBodyPart, stmtSeqNode);
            ((ASTMethodNode)node).replaceBody(ifBody);
            return true;
         } else if (node instanceof ASTSynchronizedBlockNode) {
            tryBody = this.getSubBodyFromSingleSubBodyNode(node);
            ifBody = this.createNewSubBody(tryBody, newChangedBodyPart, stmtSeqNode);
            ((ASTSynchronizedBlockNode)node).replaceBody(ifBody);
            return true;
         } else if (node instanceof ASTLabeledBlockNode) {
            tryBody = this.getSubBodyFromSingleSubBodyNode(node);
            ifBody = this.createNewSubBody(tryBody, newChangedBodyPart, stmtSeqNode);
            ((ASTLabeledBlockNode)node).replaceBody(ifBody);
            return true;
         } else if (node instanceof ASTUnconditionalLoopNode) {
            tryBody = this.getSubBodyFromSingleSubBodyNode(node);
            ifBody = this.createNewSubBody(tryBody, newChangedBodyPart, stmtSeqNode);
            ((ASTUnconditionalLoopNode)node).replaceBody(ifBody);
            return true;
         } else if (node instanceof ASTIfNode) {
            tryBody = this.getSubBodyFromSingleSubBodyNode(node);
            ifBody = this.createNewSubBody(tryBody, newChangedBodyPart, stmtSeqNode);
            ((ASTIfNode)node).replaceBody(ifBody);
            return true;
         } else if (node instanceof ASTWhileNode) {
            tryBody = this.getSubBodyFromSingleSubBodyNode(node);
            ifBody = this.createNewSubBody(tryBody, newChangedBodyPart, stmtSeqNode);
            ((ASTWhileNode)node).replaceBody(ifBody);
            return true;
         } else if (node instanceof ASTDoWhileNode) {
            tryBody = this.getSubBodyFromSingleSubBodyNode(node);
            ifBody = this.createNewSubBody(tryBody, newChangedBodyPart, stmtSeqNode);
            ((ASTDoWhileNode)node).replaceBody(ifBody);
            return true;
         } else if (node instanceof ASTForLoopNode) {
            tryBody = this.getSubBodyFromSingleSubBodyNode(node);
            ifBody = this.createNewSubBody(tryBody, newChangedBodyPart, stmtSeqNode);
            ((ASTForLoopNode)node).replaceBody(ifBody);
            return true;
         } else {
            if (node instanceof ASTIfElseNode) {
               tryBody = node.get_SubBodies();
               if (tryBody.size() != 2) {
                  throw new RuntimeException("Found an ifelse ASTNode which does not have two bodies");
               }

               ifBody = (List)tryBody.get(0);
               List<Object> elseBody = (List)tryBody.get(1);
               int subBodyNumber = -1;
               Iterator it = ifBody.iterator();

               Object temp;
               while(it.hasNext()) {
                  temp = it.next();
                  if (temp == stmtSeqNode) {
                     subBodyNumber = 0;
                     break;
                  }
               }

               if (subBodyNumber != 0) {
                  it = elseBody.iterator();

                  while(it.hasNext()) {
                     temp = it.next();
                     if (temp == stmtSeqNode) {
                        subBodyNumber = 1;
                        break;
                     }
                  }
               }

               temp = null;
               List subBodyToReplace;
               if (subBodyNumber == 0) {
                  subBodyToReplace = ifBody;
               } else {
                  if (subBodyNumber != 1) {
                     throw new RuntimeException("Could not find the related ASTNode in the method");
                  }

                  subBodyToReplace = elseBody;
               }

               List<Object> newBody = this.createNewSubBody(subBodyToReplace, newChangedBodyPart, stmtSeqNode);
               if (subBodyNumber == 0) {
                  ((ASTIfElseNode)node).replaceBody(newBody, elseBody);
                  return true;
               }

               if (subBodyNumber == 1) {
                  ((ASTIfElseNode)node).replaceBody(ifBody, newBody);
                  return true;
               }
            } else {
               if (node instanceof ASTTryNode) {
                  tryBody = ((ASTTryNode)node).get_TryBody();
                  Iterator<Object> it = tryBody.iterator();
                  boolean inTryBody = false;

                  while(it.hasNext()) {
                     ASTNode temp = (ASTNode)it.next();
                     if (temp == stmtSeqNode) {
                        inTryBody = true;
                        break;
                     }
                  }

                  if (!inTryBody) {
                     return false;
                  }

                  List<Object> newBody = this.createNewSubBody(tryBody, newChangedBodyPart, stmtSeqNode);
                  ((ASTTryNode)node).replaceTryBody(newBody);
                  return true;
               }

               if (node instanceof ASTSwitchNode) {
                  tryBody = ((ASTSwitchNode)node).getIndexList();
                  Map<Object, List<Object>> index2BodyList = ((ASTSwitchNode)node).getIndex2BodyList();
                  Iterator it = tryBody.iterator();

                  Object currentIndex;
                  List body;
                  boolean found;
                  do {
                     do {
                        if (!it.hasNext()) {
                           return false;
                        }

                        currentIndex = it.next();
                        body = (List)index2BodyList.get(currentIndex);
                     } while(body == null);

                     found = false;
                     Iterator itBody = body.iterator();

                     while(itBody.hasNext()) {
                        ASTNode temp = (ASTNode)itBody.next();
                        if (temp == stmtSeqNode) {
                           found = true;
                           break;
                        }
                     }
                  } while(!found);

                  List<Object> newBody = this.createNewSubBody(body, newChangedBodyPart, stmtSeqNode);
                  index2BodyList.put(currentIndex, newBody);
                  ((ASTSwitchNode)node).replaceIndex2BodyList(index2BodyList);
                  return true;
               }
            }

            return false;
         }
      }
   }

   public List<ASTStatementSequenceNode> createChangedBodyPart(InvokeStmt s, List body, ASTParentNodeFinder finder) {
      Object parent = finder.getParentOf(s);
      if (parent == null) {
         throw new RuntimeException("MethodCall FInder: parent of invoke stmt not found");
      } else {
         ASTNode parentNode = (ASTNode)parent;
         if (!(parentNode instanceof ASTStatementSequenceNode)) {
            throw new RuntimeException("MethodCall FInder: parent node not a stmt seq node");
         } else {
            ASTStatementSequenceNode orignal = (ASTStatementSequenceNode)parentNode;
            List<AugmentedStmt> newInitialNode = new ArrayList();
            Iterator it = orignal.getStatements().iterator();

            while(it.hasNext()) {
               AugmentedStmt as = (AugmentedStmt)it.next();
               Stmt tempStmt = as.get_Stmt();
               if (tempStmt == s) {
                  break;
               }

               newInitialNode.add(as);
            }

            ArrayList newSecondNode = new ArrayList();

            while(it.hasNext()) {
               newSecondNode.add(it.next());
            }

            List<ASTStatementSequenceNode> toReturn = new ArrayList();
            if (newInitialNode.size() != 0) {
               toReturn.add(new ASTStatementSequenceNode(newInitialNode));
            }

            toReturn.addAll(body);
            if (newSecondNode.size() != 0) {
               toReturn.add(new ASTStatementSequenceNode(newSecondNode));
            }

            return toReturn;
         }
      }
   }
}
