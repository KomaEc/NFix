package soot.dava.toolkits.base.AST.analysis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Immediate;
import soot.Local;
import soot.SootClass;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.baf.internal.BafLocal;
import soot.dava.internal.AST.ASTAndCondition;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTOrCondition;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DInstanceFieldRef;
import soot.dava.internal.javaRep.DThisRef;
import soot.dava.internal.javaRep.DVariableDeclarationStmt;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.Expr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.Ref;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;
import soot.jimple.internal.JimpleLocal;

public class DepthFirstAdapter extends AnalysisAdapter {
   public boolean DEBUG = false;
   boolean verbose = false;

   public DepthFirstAdapter() {
   }

   public DepthFirstAdapter(boolean verbose) {
      this.verbose = verbose;
   }

   public void inASTMethodNode(ASTMethodNode node) {
      if (this.verbose) {
         System.out.println("inASTMethodNode");
      }

   }

   public void outASTMethodNode(ASTMethodNode node) {
      if (this.verbose) {
         System.out.println("outASTMethodNode");
      }

   }

   public void caseASTMethodNode(ASTMethodNode node) {
      this.inASTMethodNode(node);
      this.normalRetrieving(node);
      this.outASTMethodNode(node);
   }

   public void inASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
      if (this.verbose) {
         System.out.println("inASTSynchronizedBlockNode");
      }

   }

   public void outASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
      if (this.verbose) {
         System.out.println("outASTSynchronizedBlockNode");
      }

   }

   public void caseASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
      this.inASTSynchronizedBlockNode(node);
      Value local = node.getLocal();
      this.decideCaseExprOrRef(local);
      this.normalRetrieving(node);
      this.outASTSynchronizedBlockNode(node);
   }

   public void inASTLabeledBlockNode(ASTLabeledBlockNode node) {
      if (this.verbose) {
         System.out.println("inASTLabeledBlockNode");
      }

   }

   public void outASTLabeledBlockNode(ASTLabeledBlockNode node) {
      if (this.verbose) {
         System.out.println("outASTLabeledBlockNode");
      }

   }

   public void caseASTLabeledBlockNode(ASTLabeledBlockNode node) {
      this.inASTLabeledBlockNode(node);
      this.normalRetrieving(node);
      this.outASTLabeledBlockNode(node);
   }

   public void inASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
      if (this.verbose) {
         System.out.println("inASTUnconditionalWhileNode");
      }

   }

   public void outASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
      if (this.verbose) {
         System.out.println("outASTUnconditionalWhileNode");
      }

   }

   public void caseASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
      this.inASTUnconditionalLoopNode(node);
      this.normalRetrieving(node);
      this.outASTUnconditionalLoopNode(node);
   }

   public void inASTSwitchNode(ASTSwitchNode node) {
      if (this.verbose) {
         System.out.println("inASTSwitchNode");
      }

   }

   public void outASTSwitchNode(ASTSwitchNode node) {
      if (this.verbose) {
         System.out.println("outASTSwitchNode");
      }

   }

   public void caseASTSwitchNode(ASTSwitchNode node) {
      this.inASTSwitchNode(node);
      this.caseExprOrRefValueBox(node.getKeyBox());
      this.normalRetrieving(node);
      this.outASTSwitchNode(node);
   }

   public void inASTIfNode(ASTIfNode node) {
      if (this.verbose) {
         System.out.println("inASTIfNode");
      }

   }

   public void outASTIfNode(ASTIfNode node) {
      if (this.verbose) {
         System.out.println("outASTIfNode");
      }

   }

   public void caseASTIfNode(ASTIfNode node) {
      this.inASTIfNode(node);
      ASTCondition condition = node.get_Condition();
      condition.apply(this);
      this.normalRetrieving(node);
      this.outASTIfNode(node);
   }

   public void inASTIfElseNode(ASTIfElseNode node) {
      if (this.verbose) {
         System.out.println("inASTIfElseNode");
      }

   }

   public void outASTIfElseNode(ASTIfElseNode node) {
      if (this.verbose) {
         System.out.println("outASTIfElseNode");
      }

   }

   public void caseASTIfElseNode(ASTIfElseNode node) {
      this.inASTIfElseNode(node);
      ASTCondition condition = node.get_Condition();
      condition.apply(this);
      this.normalRetrieving(node);
      this.outASTIfElseNode(node);
   }

   public void inASTWhileNode(ASTWhileNode node) {
      if (this.verbose) {
         System.out.println("inASTWhileNode");
      }

   }

   public void outASTWhileNode(ASTWhileNode node) {
      if (this.verbose) {
         System.out.println("outASTWhileNode");
      }

   }

   public void caseASTWhileNode(ASTWhileNode node) {
      this.inASTWhileNode(node);
      ASTCondition condition = node.get_Condition();
      condition.apply(this);
      this.normalRetrieving(node);
      this.outASTWhileNode(node);
   }

   public void inASTForLoopNode(ASTForLoopNode node) {
      if (this.verbose) {
         System.out.println("inASTForLoopNode");
      }

   }

   public void outASTForLoopNode(ASTForLoopNode node) {
      if (this.verbose) {
         System.out.println("outASTForLoopNode");
      }

   }

   public void caseASTForLoopNode(ASTForLoopNode node) {
      this.inASTForLoopNode(node);
      Iterator var2 = node.getInit().iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Stmt s = as.get_Stmt();
         if (s instanceof DefinitionStmt) {
            this.caseDefinitionStmt((DefinitionStmt)s);
         } else if (s instanceof ReturnStmt) {
            this.caseReturnStmt((ReturnStmt)s);
         } else if (s instanceof InvokeStmt) {
            this.caseInvokeStmt((InvokeStmt)s);
         } else if (s instanceof ThrowStmt) {
            this.caseThrowStmt((ThrowStmt)s);
         } else {
            this.caseStmt(s);
         }
      }

      ASTCondition condition = node.get_Condition();
      condition.apply(this);
      Iterator var7 = node.getUpdate().iterator();

      while(var7.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var7.next();
         Stmt s = as.get_Stmt();
         if (s instanceof DefinitionStmt) {
            this.caseDefinitionStmt((DefinitionStmt)s);
         } else if (s instanceof ReturnStmt) {
            this.caseReturnStmt((ReturnStmt)s);
         } else if (s instanceof InvokeStmt) {
            this.caseInvokeStmt((InvokeStmt)s);
         } else if (s instanceof ThrowStmt) {
            this.caseThrowStmt((ThrowStmt)s);
         } else {
            this.caseStmt(s);
         }
      }

      this.normalRetrieving(node);
      this.outASTForLoopNode(node);
   }

   public void inASTDoWhileNode(ASTDoWhileNode node) {
      if (this.verbose) {
         System.out.println("inASTDoWhileNode");
      }

   }

   public void outASTDoWhileNode(ASTDoWhileNode node) {
      if (this.verbose) {
         System.out.println("outASTDoWhileNode");
      }

   }

   public void caseASTDoWhileNode(ASTDoWhileNode node) {
      this.inASTDoWhileNode(node);
      ASTCondition condition = node.get_Condition();
      condition.apply(this);
      this.normalRetrieving(node);
      this.outASTDoWhileNode(node);
   }

   public void inASTTryNode(ASTTryNode node) {
      if (this.verbose) {
         System.out.println("inASTTryNode");
      }

   }

   public void outASTTryNode(ASTTryNode node) {
      if (this.verbose) {
         System.out.println("outASTTryNode");
      }

   }

   public void caseASTTryNode(ASTTryNode node) {
      this.inASTTryNode(node);
      List<Object> tryBody = node.get_TryBody();
      Iterator it = tryBody.iterator();

      while(it.hasNext()) {
         ((ASTNode)it.next()).apply(this);
      }

      Map<Object, Object> exceptionMap = node.get_ExceptionMap();
      Map<Object, Object> paramMap = node.get_ParamMap();
      List<Object> catchList = node.get_CatchList();
      Iterator itBody = null;
      it = catchList.iterator();

      while(it.hasNext()) {
         ASTTryNode.container catchBody = (ASTTryNode.container)it.next();
         SootClass sootClass = (SootClass)exceptionMap.get(catchBody);
         Type type = sootClass.getType();
         this.caseType(type);
         Local local = (Local)paramMap.get(catchBody);
         this.decideCaseExprOrRef(local);
         List body = (List)catchBody.o;
         itBody = body.iterator();

         while(itBody.hasNext()) {
            ((ASTNode)itBody.next()).apply(this);
         }
      }

      this.outASTTryNode(node);
   }

   public void inASTUnaryCondition(ASTUnaryCondition uc) {
      if (this.verbose) {
         System.out.println("inASTUnaryCondition");
      }

   }

   public void outASTUnaryCondition(ASTUnaryCondition uc) {
      if (this.verbose) {
         System.out.println("outASTUnaryCondition");
      }

   }

   public void caseASTUnaryCondition(ASTUnaryCondition uc) {
      this.inASTUnaryCondition(uc);
      this.decideCaseExprOrRef(uc.getValue());
      this.outASTUnaryCondition(uc);
   }

   public void inASTBinaryCondition(ASTBinaryCondition bc) {
      if (this.verbose) {
         System.out.println("inASTBinaryCondition");
      }

   }

   public void outASTBinaryCondition(ASTBinaryCondition bc) {
      if (this.verbose) {
         System.out.println("outASTBinaryCondition");
      }

   }

   public void caseASTBinaryCondition(ASTBinaryCondition bc) {
      this.inASTBinaryCondition(bc);
      ConditionExpr condition = bc.getConditionExpr();
      this.decideCaseExprOrRef(condition);
      this.outASTBinaryCondition(bc);
   }

   public void inASTAndCondition(ASTAndCondition ac) {
      if (this.verbose) {
         System.out.println("inASTAndCondition");
      }

   }

   public void outASTAndCondition(ASTAndCondition ac) {
      if (this.verbose) {
         System.out.println("outASTAndCondition");
      }

   }

   public void caseASTAndCondition(ASTAndCondition ac) {
      this.inASTAndCondition(ac);
      ac.getLeftOp().apply(this);
      ac.getRightOp().apply(this);
      this.outASTAndCondition(ac);
   }

   public void inASTOrCondition(ASTOrCondition oc) {
      if (this.verbose) {
         System.out.println("inASTOrCondition");
      }

   }

   public void outASTOrCondition(ASTOrCondition oc) {
      if (this.verbose) {
         System.out.println("outASTOrCondition");
      }

   }

   public void caseASTOrCondition(ASTOrCondition oc) {
      this.inASTOrCondition(oc);
      oc.getLeftOp().apply(this);
      oc.getRightOp().apply(this);
      this.outASTOrCondition(oc);
   }

   public void inType(Type t) {
      if (this.verbose) {
         System.out.println("inType");
      }

   }

   public void outType(Type t) {
      if (this.verbose) {
         System.out.println("outType");
      }

   }

   public void caseType(Type t) {
      this.inType(t);
      this.outType(t);
   }

   public void normalRetrieving(ASTNode node) {
      Iterator sbit = node.get_SubBodies().iterator();

      while(sbit.hasNext()) {
         Object subBody = sbit.next();
         Iterator it = ((List)subBody).iterator();

         while(it.hasNext()) {
            ASTNode temp = (ASTNode)it.next();
            temp.apply(this);
         }
      }

   }

   public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
      if (this.verbose) {
         System.out.println("inASTStatementSequenceNode");
      }

   }

   public void outASTStatementSequenceNode(ASTStatementSequenceNode node) {
      if (this.verbose) {
         System.out.println("outASTStatementSequenceNode");
      }

   }

   public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
      this.inASTStatementSequenceNode(node);
      Iterator var2 = node.getStatements().iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Stmt s = as.get_Stmt();
         if (s instanceof DefinitionStmt) {
            this.caseDefinitionStmt((DefinitionStmt)s);
         } else if (s instanceof ReturnStmt) {
            this.caseReturnStmt((ReturnStmt)s);
         } else if (s instanceof InvokeStmt) {
            this.caseInvokeStmt((InvokeStmt)s);
         } else if (s instanceof ThrowStmt) {
            this.caseThrowStmt((ThrowStmt)s);
         } else if (s instanceof DVariableDeclarationStmt) {
            this.caseDVariableDeclarationStmt((DVariableDeclarationStmt)s);
         } else {
            this.caseStmt(s);
         }
      }

      this.outASTStatementSequenceNode(node);
   }

   public void inDefinitionStmt(DefinitionStmt s) {
      if (this.verbose) {
         System.out.println("inDefinitionStmt" + s);
      }

   }

   public void outDefinitionStmt(DefinitionStmt s) {
      if (this.verbose) {
         System.out.println("outDefinitionStmt");
      }

   }

   public void caseDefinitionStmt(DefinitionStmt s) {
      this.inDefinitionStmt(s);
      this.caseExprOrRefValueBox(s.getRightOpBox());
      this.caseExprOrRefValueBox(s.getLeftOpBox());
      this.outDefinitionStmt(s);
   }

   public void inReturnStmt(ReturnStmt s) {
      if (this.verbose) {
         System.out.println("inReturnStmt");
      }

   }

   public void outReturnStmt(ReturnStmt s) {
      if (this.verbose) {
         System.out.println("outReturnStmt");
      }

   }

   public void caseReturnStmt(ReturnStmt s) {
      this.inReturnStmt(s);
      this.caseExprOrRefValueBox(s.getOpBox());
      this.outReturnStmt(s);
   }

   public void inInvokeStmt(InvokeStmt s) {
      if (this.verbose) {
         System.out.println("inInvokeStmt");
      }

   }

   public void outInvokeStmt(InvokeStmt s) {
      if (this.verbose) {
         System.out.println("outInvokeStmt");
      }

   }

   public void caseInvokeStmt(InvokeStmt s) {
      this.inInvokeStmt(s);
      this.caseExprOrRefValueBox(s.getInvokeExprBox());
      this.outInvokeStmt(s);
   }

   public void inThrowStmt(ThrowStmt s) {
      if (this.verbose) {
         System.out.println("\n\ninThrowStmt\n\n");
      }

   }

   public void outThrowStmt(ThrowStmt s) {
      if (this.verbose) {
         System.out.println("outThrowStmt");
      }

   }

   public void caseThrowStmt(ThrowStmt s) {
      this.inThrowStmt(s);
      this.caseExprOrRefValueBox(s.getOpBox());
      this.outThrowStmt(s);
   }

   public void inDVariableDeclarationStmt(DVariableDeclarationStmt s) {
      if (this.verbose) {
         System.out.println("\n\ninDVariableDeclarationStmt\n\n" + s);
      }

   }

   public void outDVariableDeclarationStmt(DVariableDeclarationStmt s) {
      if (this.verbose) {
         System.out.println("outDVariableDeclarationStmt");
      }

   }

   public void caseDVariableDeclarationStmt(DVariableDeclarationStmt s) {
      this.inDVariableDeclarationStmt(s);
      Type type = s.getType();
      this.caseType(type);
      List listDeclared = s.getDeclarations();
      Iterator it = listDeclared.iterator();

      while(it.hasNext()) {
         Local declared = (Local)it.next();
         this.decideCaseExprOrRef(declared);
      }

      this.outDVariableDeclarationStmt(s);
   }

   public void inStmt(Stmt s) {
      if (this.verbose) {
         System.out.println("inStmt: " + s);
      }

   }

   public void outStmt(Stmt s) {
      if (this.verbose) {
         System.out.println("outStmt");
      }

   }

   public void caseStmt(Stmt s) {
      this.inStmt(s);
      this.outStmt(s);
   }

   public void caseExprOrRefValueBox(ValueBox vb) {
      this.inExprOrRefValueBox(vb);
      this.decideCaseExprOrRef(vb.getValue());
      this.outExprOrRefValueBox(vb);
   }

   public void inExprOrRefValueBox(ValueBox vb) {
      if (this.verbose) {
         System.out.println("inExprOrRefValueBox" + vb);
      }

   }

   public void outExprOrRefValueBox(ValueBox vb) {
      if (this.verbose) {
         System.out.println("outExprOrRefValueBox" + vb);
      }

   }

   public void decideCaseExprOrRef(Value v) {
      if (v instanceof Expr) {
         this.caseExpr((Expr)v);
      } else if (v instanceof Ref) {
         this.caseRef((Ref)v);
      } else {
         this.caseValue(v);
      }

   }

   public void inValue(Value v) {
      if (this.verbose) {
         System.out.println("inValue" + v);
         if (v instanceof DThisRef) {
            System.out.println("DTHISREF.................");
         } else if (v instanceof Immediate) {
            System.out.println("\tIMMEDIATE");
            if (v instanceof JimpleLocal) {
               System.out.println("\t\tJimpleLocal...................." + v);
            } else if (v instanceof Constant) {
               System.out.println("\t\tconstant....................");
               if (v instanceof IntConstant) {
                  System.out.println("\t\t INTconstant....................");
               }
            } else if (v instanceof BafLocal) {
               System.out.println("\t\tBafLocal....................");
            } else {
               System.out.println("\t\telse!!!!!!!!!!!!");
            }
         } else {
            System.out.println("NEITHER................");
         }
      }

   }

   public void outValue(Value v) {
      if (this.verbose) {
         System.out.println("outValue");
      }

   }

   public void caseValue(Value v) {
      this.inValue(v);
      this.outValue(v);
   }

   public void inExpr(Expr e) {
      if (this.verbose) {
         System.out.println("inExpr");
      }

   }

   public void outExpr(Expr e) {
      if (this.verbose) {
         System.out.println("outExpr");
      }

   }

   public void caseExpr(Expr e) {
      this.inExpr(e);
      this.decideCaseExpr(e);
      this.outExpr(e);
   }

   public void inRef(Ref r) {
      if (this.verbose) {
         System.out.println("inRef");
      }

   }

   public void outRef(Ref r) {
      if (this.verbose) {
         System.out.println("outRef");
      }

   }

   public void caseRef(Ref r) {
      this.inRef(r);
      this.decideCaseRef(r);
      this.outRef(r);
   }

   public void decideCaseExpr(Expr e) {
      if (e instanceof BinopExpr) {
         this.caseBinopExpr((BinopExpr)e);
      } else if (e instanceof UnopExpr) {
         this.caseUnopExpr((UnopExpr)e);
      } else if (e instanceof NewArrayExpr) {
         this.caseNewArrayExpr((NewArrayExpr)e);
      } else if (e instanceof NewMultiArrayExpr) {
         this.caseNewMultiArrayExpr((NewMultiArrayExpr)e);
      } else if (e instanceof InstanceOfExpr) {
         this.caseInstanceOfExpr((InstanceOfExpr)e);
      } else if (e instanceof InvokeExpr) {
         this.caseInvokeExpr((InvokeExpr)e);
      } else if (e instanceof CastExpr) {
         this.caseCastExpr((CastExpr)e);
      }

   }

   public void inBinopExpr(BinopExpr be) {
      if (this.verbose) {
         System.out.println("inBinopExpr");
      }

   }

   public void outBinopExpr(BinopExpr be) {
      if (this.verbose) {
         System.out.println("outBinopExpr");
      }

   }

   public void caseBinopExpr(BinopExpr be) {
      this.inBinopExpr(be);
      this.caseExprOrRefValueBox(be.getOp1Box());
      this.caseExprOrRefValueBox(be.getOp2Box());
      this.outBinopExpr(be);
   }

   public void inUnopExpr(UnopExpr ue) {
      if (this.verbose) {
         System.out.println("inUnopExpr");
      }

   }

   public void outUnopExpr(UnopExpr ue) {
      if (this.verbose) {
         System.out.println("outUnopExpr");
      }

   }

   public void caseUnopExpr(UnopExpr ue) {
      this.inUnopExpr(ue);
      this.caseExprOrRefValueBox(ue.getOpBox());
      this.outUnopExpr(ue);
   }

   public void inNewArrayExpr(NewArrayExpr nae) {
      if (this.verbose) {
         System.out.println("inNewArrayExpr");
      }

   }

   public void outNewArrayExpr(NewArrayExpr nae) {
      if (this.verbose) {
         System.out.println("outNewArrayExpr");
      }

   }

   public void caseNewArrayExpr(NewArrayExpr nae) {
      this.inNewArrayExpr(nae);
      this.caseExprOrRefValueBox(nae.getSizeBox());
      this.outNewArrayExpr(nae);
   }

   public void inNewMultiArrayExpr(NewMultiArrayExpr nmae) {
      if (this.verbose) {
         System.out.println("inNewMultiArrayExpr");
      }

   }

   public void outNewMultiArrayExpr(NewMultiArrayExpr nmae) {
      if (this.verbose) {
         System.out.println("outNewMultiArrayExpr");
      }

   }

   public void caseNewMultiArrayExpr(NewMultiArrayExpr nmae) {
      this.inNewMultiArrayExpr(nmae);

      for(int i = 0; i < nmae.getSizeCount(); ++i) {
         this.caseExprOrRefValueBox(nmae.getSizeBox(i));
      }

      this.outNewMultiArrayExpr(nmae);
   }

   public void inInstanceOfExpr(InstanceOfExpr ioe) {
      if (this.verbose) {
         System.out.println("inInstanceOfExpr");
      }

   }

   public void outInstanceOfExpr(InstanceOfExpr ioe) {
      if (this.verbose) {
         System.out.println("outInstanceOfExpr");
      }

   }

   public void caseInstanceOfExpr(InstanceOfExpr ioe) {
      this.inInstanceOfExpr(ioe);
      this.caseExprOrRefValueBox(ioe.getOpBox());
      this.outInstanceOfExpr(ioe);
   }

   public void inInvokeExpr(InvokeExpr ie) {
      if (this.verbose) {
         System.out.println("inInvokeExpr");
      }

   }

   public void outInvokeExpr(InvokeExpr ie) {
      if (this.verbose) {
         System.out.println("outInvokeExpr");
      }

   }

   public void caseInvokeExpr(InvokeExpr ie) {
      this.inInvokeExpr(ie);

      for(int i = 0; i < ie.getArgCount(); ++i) {
         this.caseExprOrRefValueBox(ie.getArgBox(i));
      }

      if (ie instanceof InstanceInvokeExpr) {
         this.caseInstanceInvokeExpr((InstanceInvokeExpr)ie);
      }

      this.outInvokeExpr(ie);
   }

   public void inInstanceInvokeExpr(InstanceInvokeExpr iie) {
      if (this.verbose) {
         System.out.println("inInstanceInvokeExpr");
      }

   }

   public void outInstanceInvokeExpr(InstanceInvokeExpr iie) {
      if (this.verbose) {
         System.out.println("outInstanceInvokeExpr");
      }

   }

   public void caseInstanceInvokeExpr(InstanceInvokeExpr iie) {
      this.inInstanceInvokeExpr(iie);
      this.caseExprOrRefValueBox(iie.getBaseBox());
      this.outInstanceInvokeExpr(iie);
   }

   public void inCastExpr(CastExpr ce) {
      if (this.verbose) {
         System.out.println("inCastExpr");
      }

   }

   public void outCastExpr(CastExpr ce) {
      if (this.verbose) {
         System.out.println("outCastExpr");
      }

   }

   public void caseCastExpr(CastExpr ce) {
      this.inCastExpr(ce);
      Type type = ce.getCastType();
      this.caseType(type);
      this.caseExprOrRefValueBox(ce.getOpBox());
      this.outCastExpr(ce);
   }

   public void decideCaseRef(Ref r) {
      if (r instanceof ArrayRef) {
         this.caseArrayRef((ArrayRef)r);
      } else if (r instanceof InstanceFieldRef) {
         this.caseInstanceFieldRef((InstanceFieldRef)r);
      } else if (r instanceof StaticFieldRef) {
         this.caseStaticFieldRef((StaticFieldRef)r);
      }

   }

   public void inArrayRef(ArrayRef ar) {
      if (this.verbose) {
         System.out.println("inArrayRef");
      }

   }

   public void outArrayRef(ArrayRef ar) {
      if (this.verbose) {
         System.out.println("outArrayRef");
      }

   }

   public void caseArrayRef(ArrayRef ar) {
      this.inArrayRef(ar);
      this.caseExprOrRefValueBox(ar.getBaseBox());
      this.caseExprOrRefValueBox(ar.getIndexBox());
      this.outArrayRef(ar);
   }

   public void inInstanceFieldRef(InstanceFieldRef ifr) {
      if (this.verbose) {
         System.out.println("inInstanceFieldRef");
         if (ifr instanceof DInstanceFieldRef) {
            System.out.println("...........DINSTANCEFIELDREF");
         }
      }

   }

   public void outInstanceFieldRef(InstanceFieldRef ifr) {
      if (this.verbose) {
         System.out.println("outInstanceFieldRef");
      }

   }

   public void caseInstanceFieldRef(InstanceFieldRef ifr) {
      this.inInstanceFieldRef(ifr);
      this.caseExprOrRefValueBox(ifr.getBaseBox());
      this.outInstanceFieldRef(ifr);
   }

   public void inStaticFieldRef(StaticFieldRef sfr) {
      if (this.verbose) {
         System.out.println("inStaticFieldRef");
      }

   }

   public void outStaticFieldRef(StaticFieldRef sfr) {
      if (this.verbose) {
         System.out.println("outStaticFieldRef");
      }

   }

   public void caseStaticFieldRef(StaticFieldRef sfr) {
      this.inStaticFieldRef(sfr);
      this.outStaticFieldRef(sfr);
   }

   public void debug(String className, String methodName, String debug) {
      if (this.DEBUG) {
         System.out.println("Analysis" + className + "..Method:" + methodName + "    DEBUG: " + debug);
      }

   }
}
