package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Local;
import soot.SootClass;
import soot.Type;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.structuredAnalysis.UnreachableCodeFinder;
import soot.jimple.Stmt;

public class UnreachableCodeEliminator extends DepthFirstAdapter {
   public boolean DUBUG = true;
   ASTNode AST;
   UnreachableCodeFinder codeFinder;

   public UnreachableCodeEliminator(ASTNode AST) {
      this.AST = AST;
      this.setup();
   }

   public UnreachableCodeEliminator(boolean verbose, ASTNode AST) {
      super(verbose);
      this.AST = AST;
      this.setup();
   }

   private void setup() {
      this.codeFinder = new UnreachableCodeFinder(this.AST);
   }

   public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
      List<AugmentedStmt> toRemove = new ArrayList();
      Iterator var3 = node.getStatements().iterator();

      AugmentedStmt as;
      while(var3.hasNext()) {
         as = (AugmentedStmt)var3.next();
         Stmt s = as.get_Stmt();
         if (!this.codeFinder.isConstructReachable(s)) {
            toRemove.add(as);
         }
      }

      var3 = toRemove.iterator();

      while(var3.hasNext()) {
         as = (AugmentedStmt)var3.next();
         node.getStatements().remove(as);
      }

   }

   public void normalRetrieving(ASTNode node) {
      if (node instanceof ASTSwitchNode) {
         this.dealWithSwitchNode((ASTSwitchNode)node);
      } else {
         List<ASTNode> toReturn = new ArrayList();
         Iterator sbit = node.get_SubBodies().iterator();

         while(sbit.hasNext()) {
            Object subBody = sbit.next();
            Iterator it = ((List)subBody).iterator();

            while(it.hasNext()) {
               ASTNode temp = (ASTNode)it.next();
               if (!this.codeFinder.isConstructReachable(temp)) {
                  toReturn.add(temp);
               } else {
                  temp.apply(this);
               }
            }

            it = toReturn.iterator();

            while(it.hasNext()) {
               ((List)subBody).remove(it.next());
            }
         }

      }
   }

   public void caseASTTryNode(ASTTryNode node) {
      List<Object> tryBody = node.get_TryBody();
      Iterator<Object> it = tryBody.iterator();
      ArrayList toReturn = new ArrayList();

      while(it.hasNext()) {
         ASTNode temp = (ASTNode)it.next();
         if (!this.codeFinder.isConstructReachable(temp)) {
            toReturn.add(temp);
         } else {
            temp.apply(this);
         }
      }

      it = toReturn.iterator();

      while(it.hasNext()) {
         tryBody.remove(it.next());
      }

      Map<Object, Object> exceptionMap = node.get_ExceptionMap();
      Map<Object, Object> paramMap = node.get_ParamMap();
      List<Object> catchList = node.get_CatchList();
      Iterator<Object> itBody = null;
      it = catchList.iterator();

      while(it.hasNext()) {
         ASTTryNode.container catchBody = (ASTTryNode.container)it.next();
         SootClass sootClass = (SootClass)exceptionMap.get(catchBody);
         Type type = sootClass.getType();
         this.caseType(type);
         Local local = (Local)paramMap.get(catchBody);
         this.decideCaseExprOrRef(local);
         List<Object> body = (List)catchBody.o;
         toReturn = new ArrayList();
         itBody = body.iterator();

         while(itBody.hasNext()) {
            ASTNode temp = (ASTNode)itBody.next();
            if (!this.codeFinder.isConstructReachable(temp)) {
               toReturn.add(temp);
            } else {
               temp.apply(this);
            }
         }

         itBody = toReturn.iterator();

         while(itBody.hasNext()) {
            body.remove(itBody.next());
         }
      }

   }

   private void dealWithSwitchNode(ASTSwitchNode node) {
      List<Object> indexList = node.getIndexList();
      Map<Object, List<Object>> index2BodyList = node.getIndex2BodyList();
      Iterator it = indexList.iterator();

      while(true) {
         List body;
         do {
            if (!it.hasNext()) {
               return;
            }

            Object currentIndex = it.next();
            body = (List)index2BodyList.get(currentIndex);
         } while(body == null);

         List<ASTNode> toReturn = new ArrayList();
         Iterator itBody = body.iterator();

         while(itBody.hasNext()) {
            ASTNode temp = (ASTNode)itBody.next();
            if (!this.codeFinder.isConstructReachable(temp)) {
               toReturn.add(temp);
            } else {
               temp.apply(this);
            }
         }

         Iterator newit = toReturn.iterator();

         while(newit.hasNext()) {
            body.remove(newit.next());
         }
      }
   }
}
