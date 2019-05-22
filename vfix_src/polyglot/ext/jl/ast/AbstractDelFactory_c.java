package polyglot.ext.jl.ast;

import polyglot.ast.DelFactory;
import polyglot.ast.JL;
import polyglot.util.InternalCompilerError;

public abstract class AbstractDelFactory_c implements DelFactory {
   private DelFactory nextDelFactory;

   protected AbstractDelFactory_c() {
      this((DelFactory)null);
   }

   protected AbstractDelFactory_c(DelFactory nextDelFactory) {
      this.nextDelFactory = nextDelFactory;
   }

   public DelFactory nextDelFactory() {
      return this.nextDelFactory;
   }

   protected JL composeDels(JL e1, JL e2) {
      if (e1 == null) {
         return e2;
      } else if (e2 == null) {
         return e1;
      } else {
         throw new InternalCompilerError("Composition of delegates unimplemented.");
      }
   }

   public final JL delAmbAssign() {
      JL e = this.delAmbAssignImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delAmbAssign();
         e = this.composeDels(e, e2);
      }

      return this.postDelAmbAssign(e);
   }

   public final JL delAmbExpr() {
      JL e = this.delAmbExprImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delAmbExpr();
         e = this.composeDels(e, e2);
      }

      return this.postDelAmbExpr(e);
   }

   public final JL delAmbPrefix() {
      JL e = this.delAmbPrefixImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delAmbPrefix();
         e = this.composeDels(e, e2);
      }

      return this.postDelAmbPrefix(e);
   }

   public final JL delAmbQualifierNode() {
      JL e = this.delAmbQualifierNodeImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delAmbQualifierNode();
         e = this.composeDels(e, e2);
      }

      return this.postDelAmbQualifierNode(e);
   }

   public final JL delAmbReceiver() {
      JL e = this.delAmbReceiverImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delAmbReceiver();
         e = this.composeDels(e, e2);
      }

      return this.postDelAmbReceiver(e);
   }

   public final JL delAmbTypeNode() {
      JL e = this.delAmbTypeNodeImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delAmbTypeNode();
         e = this.composeDels(e, e2);
      }

      return this.postDelAmbTypeNode(e);
   }

   public final JL delArrayAccess() {
      JL e = this.delArrayAccessImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delArrayAccess();
         e = this.composeDels(e, e2);
      }

      return this.postDelArrayAccess(e);
   }

   public final JL delArrayInit() {
      JL e = this.delArrayInitImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delArrayInit();
         e = this.composeDels(e, e2);
      }

      return this.postDelArrayInit(e);
   }

   public final JL delArrayTypeNode() {
      JL e = this.delArrayTypeNodeImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delArrayTypeNode();
         e = this.composeDels(e, e2);
      }

      return this.postDelArrayTypeNode(e);
   }

   public final JL delAssert() {
      JL e = this.delAssertImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delAssert();
         e = this.composeDels(e, e2);
      }

      return this.postDelAssert(e);
   }

   public final JL delAssign() {
      JL e = this.delAssignImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delAssign();
         e = this.composeDels(e, e2);
      }

      return this.postDelAssign(e);
   }

   public final JL delLocalAssign() {
      JL e = this.delLocalAssignImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delLocalAssign();
         e = this.composeDels(e, e2);
      }

      return this.postDelLocalAssign(e);
   }

   public final JL delFieldAssign() {
      JL e = this.delFieldAssignImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delFieldAssign();
         e = this.composeDels(e, e2);
      }

      return this.postDelFieldAssign(e);
   }

   public final JL delArrayAccessAssign() {
      JL e = this.delArrayAccessAssignImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delArrayAccessAssign();
         e = this.composeDels(e, e2);
      }

      return this.postDelArrayAccessAssign(e);
   }

   public final JL delBinary() {
      JL e = this.delBinaryImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delBinary();
         e = this.composeDels(e, e2);
      }

      return this.postDelBinary(e);
   }

   public final JL delBlock() {
      JL e = this.delBlockImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delBlock();
         e = this.composeDels(e, e2);
      }

      return this.postDelBlock(e);
   }

   public final JL delBooleanLit() {
      JL e = this.delBooleanLitImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delBooleanLit();
         e = this.composeDels(e, e2);
      }

      return this.postDelBooleanLit(e);
   }

   public final JL delBranch() {
      JL e = this.delBranchImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delBranch();
         e = this.composeDels(e, e2);
      }

      return this.postDelBranch(e);
   }

   public final JL delCall() {
      JL e = this.delCallImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delCall();
         e = this.composeDels(e, e2);
      }

      return this.postDelCall(e);
   }

   public final JL delCanonicalTypeNode() {
      JL e = this.delCanonicalTypeNodeImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delCanonicalTypeNode();
         e = this.composeDels(e, e2);
      }

      return this.postDelCanonicalTypeNode(e);
   }

   public final JL delCase() {
      JL e = this.delCaseImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delCase();
         e = this.composeDels(e, e2);
      }

      return this.postDelCase(e);
   }

   public final JL delCast() {
      JL e = this.delCastImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delCast();
         e = this.composeDels(e, e2);
      }

      return this.postDelCast(e);
   }

   public final JL delCatch() {
      JL e = this.delCatchImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delCatch();
         e = this.composeDels(e, e2);
      }

      return this.postDelCatch(e);
   }

   public final JL delCharLit() {
      JL e = this.delCharLitImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delCharLit();
         e = this.composeDels(e, e2);
      }

      return this.postDelCharLit(e);
   }

   public final JL delClassBody() {
      JL e = this.delClassBodyImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delClassBody();
         e = this.composeDels(e, e2);
      }

      return this.postDelClassBody(e);
   }

   public final JL delClassDecl() {
      JL e = this.delClassDeclImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delClassDecl();
         e = this.composeDels(e, e2);
      }

      return this.postDelClassDecl(e);
   }

   public final JL delClassLit() {
      JL e = this.delClassLitImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delClassLit();
         e = this.composeDels(e, e2);
      }

      return this.postDelClassLit(e);
   }

   public final JL delClassMember() {
      JL e = this.delClassMemberImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delClassMember();
         e = this.composeDels(e, e2);
      }

      return this.postDelClassMember(e);
   }

   public final JL delCodeDecl() {
      JL e = this.delCodeDeclImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delCodeDecl();
         e = this.composeDels(e, e2);
      }

      return this.postDelCodeDecl(e);
   }

   public final JL delConditional() {
      JL e = this.delConditionalImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delConditional();
         e = this.composeDels(e, e2);
      }

      return this.postDelConditional(e);
   }

   public final JL delConstructorCall() {
      JL e = this.delConstructorCallImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delConstructorCall();
         e = this.composeDels(e, e2);
      }

      return this.postDelConstructorCall(e);
   }

   public final JL delConstructorDecl() {
      JL e = this.delConstructorDeclImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delConstructorDecl();
         e = this.composeDels(e, e2);
      }

      return this.postDelConstructorDecl(e);
   }

   public final JL delDo() {
      JL e = this.delDoImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delDo();
         e = this.composeDels(e, e2);
      }

      return this.postDelDo(e);
   }

   public final JL delEmpty() {
      JL e = this.delEmptyImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delEmpty();
         e = this.composeDels(e, e2);
      }

      return this.postDelEmpty(e);
   }

   public final JL delEval() {
      JL e = this.delEvalImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delEval();
         e = this.composeDels(e, e2);
      }

      return this.postDelEval(e);
   }

   public final JL delExpr() {
      JL e = this.delExprImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delExpr();
         e = this.composeDels(e, e2);
      }

      return this.postDelExpr(e);
   }

   public final JL delField() {
      JL e = this.delFieldImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delField();
         e = this.composeDels(e, e2);
      }

      return this.postDelField(e);
   }

   public final JL delFieldDecl() {
      JL e = this.delFieldDeclImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delFieldDecl();
         e = this.composeDels(e, e2);
      }

      return this.postDelFieldDecl(e);
   }

   public final JL delFloatLit() {
      JL e = this.delFloatLitImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delFloatLit();
         e = this.composeDels(e, e2);
      }

      return this.postDelFloatLit(e);
   }

   public final JL delFor() {
      JL e = this.delForImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delFor();
         e = this.composeDels(e, e2);
      }

      return this.postDelFor(e);
   }

   public final JL delFormal() {
      JL e = this.delFormalImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delFormal();
         e = this.composeDels(e, e2);
      }

      return this.postDelFormal(e);
   }

   public final JL delIf() {
      JL e = this.delIfImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delIf();
         e = this.composeDels(e, e2);
      }

      return this.postDelIf(e);
   }

   public final JL delImport() {
      JL e = this.delImportImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delImport();
         e = this.composeDels(e, e2);
      }

      return this.postDelImport(e);
   }

   public final JL delInitializer() {
      JL e = this.delInitializerImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delInitializer();
         e = this.composeDels(e, e2);
      }

      return this.postDelInitializer(e);
   }

   public final JL delInstanceof() {
      JL e = this.delInstanceofImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delInstanceof();
         e = this.composeDels(e, e2);
      }

      return this.postDelInstanceof(e);
   }

   public final JL delIntLit() {
      JL e = this.delIntLitImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delIntLit();
         e = this.composeDels(e, e2);
      }

      return this.postDelIntLit(e);
   }

   public final JL delLabeled() {
      JL e = this.delLabeledImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delLabeled();
         e = this.composeDels(e, e2);
      }

      return this.postDelLabeled(e);
   }

   public final JL delLit() {
      JL e = this.delLitImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delLit();
         e = this.composeDels(e, e2);
      }

      return this.postDelLit(e);
   }

   public final JL delLocal() {
      JL e = this.delLocalImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delLocal();
         e = this.composeDels(e, e2);
      }

      return this.postDelLocal(e);
   }

   public final JL delLocalClassDecl() {
      JL e = this.delLocalClassDeclImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delLocalClassDecl();
         e = this.composeDels(e, e2);
      }

      return this.postDelLocalClassDecl(e);
   }

   public final JL delLocalDecl() {
      JL e = this.delLocalDeclImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delLocalDecl();
         e = this.composeDels(e, e2);
      }

      return this.postDelLocalDecl(e);
   }

   public final JL delLoop() {
      JL e = this.delLoopImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delLoop();
         e = this.composeDels(e, e2);
      }

      return this.postDelLoop(e);
   }

   public final JL delMethodDecl() {
      JL e = this.delMethodDeclImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delMethodDecl();
         e = this.composeDels(e, e2);
      }

      return this.postDelMethodDecl(e);
   }

   public final JL delNewArray() {
      JL e = this.delNewArrayImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delNewArray();
         e = this.composeDels(e, e2);
      }

      return this.postDelNewArray(e);
   }

   public final JL delNode() {
      JL e = this.delNodeImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delNode();
         e = this.composeDels(e, e2);
      }

      return this.postDelNode(e);
   }

   public final JL delNew() {
      JL e = this.delNewImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delNew();
         e = this.composeDels(e, e2);
      }

      return this.postDelNew(e);
   }

   public final JL delNullLit() {
      JL e = this.delNullLitImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delNullLit();
         e = this.composeDels(e, e2);
      }

      return this.postDelNullLit(e);
   }

   public final JL delNumLit() {
      JL e = this.delNumLitImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delNumLit();
         e = this.composeDels(e, e2);
      }

      return this.postDelNumLit(e);
   }

   public final JL delPackageNode() {
      JL e = this.delPackageNodeImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delPackageNode();
         e = this.composeDels(e, e2);
      }

      return this.postDelPackageNode(e);
   }

   public final JL delProcedureDecl() {
      JL e = this.delProcedureDeclImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delProcedureDecl();
         e = this.composeDels(e, e2);
      }

      return this.postDelProcedureDecl(e);
   }

   public final JL delReturn() {
      JL e = this.delReturnImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delReturn();
         e = this.composeDels(e, e2);
      }

      return this.postDelReturn(e);
   }

   public final JL delSourceCollection() {
      JL e = this.delSourceCollectionImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delSourceCollection();
         e = this.composeDels(e, e2);
      }

      return this.postDelSourceCollection(e);
   }

   public final JL delSourceFile() {
      JL e = this.delSourceFileImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delSourceFile();
         e = this.composeDels(e, e2);
      }

      return this.postDelSourceFile(e);
   }

   public final JL delSpecial() {
      JL e = this.delSpecialImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delSpecial();
         e = this.composeDels(e, e2);
      }

      return this.postDelSpecial(e);
   }

   public final JL delStmt() {
      JL e = this.delStmtImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delStmt();
         e = this.composeDels(e, e2);
      }

      return this.postDelStmt(e);
   }

   public final JL delStringLit() {
      JL e = this.delStringLitImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delStringLit();
         e = this.composeDels(e, e2);
      }

      return this.postDelStringLit(e);
   }

   public final JL delSwitchBlock() {
      JL e = this.delSwitchBlockImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delSwitchBlock();
         e = this.composeDels(e, e2);
      }

      return this.postDelSwitchBlock(e);
   }

   public final JL delSwitchElement() {
      JL e = this.delSwitchElementImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delSwitchElement();
         e = this.composeDels(e, e2);
      }

      return this.postDelSwitchElement(e);
   }

   public final JL delSwitch() {
      JL e = this.delSwitchImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delSwitch();
         e = this.composeDels(e, e2);
      }

      return this.postDelSwitch(e);
   }

   public final JL delSynchronized() {
      JL e = this.delSynchronizedImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delSynchronized();
         e = this.composeDels(e, e2);
      }

      return this.postDelSynchronized(e);
   }

   public final JL delTerm() {
      JL e = this.delTermImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delTerm();
         e = this.composeDels(e, e2);
      }

      return this.postDelTerm(e);
   }

   public final JL delThrow() {
      JL e = this.delThrowImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delThrow();
         e = this.composeDels(e, e2);
      }

      return this.postDelThrow(e);
   }

   public final JL delTry() {
      JL e = this.delTryImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delTry();
         e = this.composeDels(e, e2);
      }

      return this.postDelTry(e);
   }

   public final JL delTypeNode() {
      JL e = this.delTypeNodeImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delTypeNode();
         e = this.composeDels(e, e2);
      }

      return this.postDelTypeNode(e);
   }

   public final JL delUnary() {
      JL e = this.delUnaryImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delUnary();
         e = this.composeDels(e, e2);
      }

      return this.postDelUnary(e);
   }

   public final JL delWhile() {
      JL e = this.delWhileImpl();
      if (this.nextDelFactory != null) {
         JL e2 = this.nextDelFactory.delWhile();
         e = this.composeDels(e, e2);
      }

      return this.postDelWhile(e);
   }

   protected JL delAmbAssignImpl() {
      return this.delAssignImpl();
   }

   protected JL delAmbExprImpl() {
      return this.delExprImpl();
   }

   protected JL delAmbPrefixImpl() {
      return this.delNodeImpl();
   }

   protected JL delAmbQualifierNodeImpl() {
      return this.delNodeImpl();
   }

   protected JL delAmbReceiverImpl() {
      return this.delNodeImpl();
   }

   protected JL delAmbTypeNodeImpl() {
      return this.delTypeNodeImpl();
   }

   protected JL delArrayAccessImpl() {
      return this.delExprImpl();
   }

   protected JL delArrayInitImpl() {
      return this.delExprImpl();
   }

   protected JL delArrayTypeNodeImpl() {
      return this.delTypeNodeImpl();
   }

   protected JL delAssertImpl() {
      return this.delStmtImpl();
   }

   protected JL delAssignImpl() {
      return this.delExprImpl();
   }

   protected JL delLocalAssignImpl() {
      return this.delAssignImpl();
   }

   protected JL delFieldAssignImpl() {
      return this.delAssignImpl();
   }

   protected JL delArrayAccessAssignImpl() {
      return this.delAssignImpl();
   }

   protected JL delBinaryImpl() {
      return this.delExprImpl();
   }

   protected JL delBlockImpl() {
      return this.delStmtImpl();
   }

   protected JL delBooleanLitImpl() {
      return this.delLitImpl();
   }

   protected JL delBranchImpl() {
      return this.delStmtImpl();
   }

   protected JL delCallImpl() {
      return this.delExprImpl();
   }

   protected JL delCanonicalTypeNodeImpl() {
      return this.delTypeNodeImpl();
   }

   protected JL delCaseImpl() {
      return this.delSwitchElementImpl();
   }

   protected JL delCastImpl() {
      return this.delExprImpl();
   }

   protected JL delCatchImpl() {
      return this.delStmtImpl();
   }

   protected JL delCharLitImpl() {
      return this.delNumLitImpl();
   }

   protected JL delClassBodyImpl() {
      return this.delTermImpl();
   }

   protected JL delClassDeclImpl() {
      return this.delTermImpl();
   }

   protected JL delClassLitImpl() {
      return this.delLitImpl();
   }

   protected JL delClassMemberImpl() {
      return this.delNodeImpl();
   }

   protected JL delCodeDeclImpl() {
      return this.delClassMemberImpl();
   }

   protected JL delConditionalImpl() {
      return this.delExprImpl();
   }

   protected JL delConstructorCallImpl() {
      return this.delStmtImpl();
   }

   protected JL delConstructorDeclImpl() {
      return this.delProcedureDeclImpl();
   }

   protected JL delDoImpl() {
      return this.delLoopImpl();
   }

   protected JL delEmptyImpl() {
      return this.delStmtImpl();
   }

   protected JL delEvalImpl() {
      return this.delStmtImpl();
   }

   protected JL delExprImpl() {
      return this.delTermImpl();
   }

   protected JL delFieldImpl() {
      return this.delExprImpl();
   }

   protected JL delFieldDeclImpl() {
      return this.delClassMemberImpl();
   }

   protected JL delFloatLitImpl() {
      return this.delLitImpl();
   }

   protected JL delForImpl() {
      return this.delLoopImpl();
   }

   protected JL delFormalImpl() {
      return this.delNodeImpl();
   }

   protected JL delIfImpl() {
      return this.delStmtImpl();
   }

   protected JL delImportImpl() {
      return this.delNodeImpl();
   }

   protected JL delInitializerImpl() {
      return this.delCodeDeclImpl();
   }

   protected JL delInstanceofImpl() {
      return this.delExprImpl();
   }

   protected JL delIntLitImpl() {
      return this.delNumLitImpl();
   }

   protected JL delLabeledImpl() {
      return this.delStmtImpl();
   }

   protected JL delLitImpl() {
      return this.delExprImpl();
   }

   protected JL delLocalImpl() {
      return this.delExprImpl();
   }

   protected JL delLocalClassDeclImpl() {
      return this.delStmtImpl();
   }

   protected JL delLocalDeclImpl() {
      return this.delNodeImpl();
   }

   protected JL delLoopImpl() {
      return this.delStmtImpl();
   }

   protected JL delMethodDeclImpl() {
      return this.delProcedureDeclImpl();
   }

   protected JL delNewArrayImpl() {
      return this.delExprImpl();
   }

   protected JL delNodeImpl() {
      return null;
   }

   protected JL delNewImpl() {
      return this.delExprImpl();
   }

   protected JL delNullLitImpl() {
      return this.delLitImpl();
   }

   protected JL delNumLitImpl() {
      return this.delLitImpl();
   }

   protected JL delPackageNodeImpl() {
      return this.delNodeImpl();
   }

   protected JL delProcedureDeclImpl() {
      return this.delCodeDeclImpl();
   }

   protected JL delReturnImpl() {
      return this.delStmtImpl();
   }

   protected JL delSourceCollectionImpl() {
      return this.delNodeImpl();
   }

   protected JL delSourceFileImpl() {
      return this.delNodeImpl();
   }

   protected JL delSpecialImpl() {
      return this.delExprImpl();
   }

   protected JL delStmtImpl() {
      return this.delTermImpl();
   }

   protected JL delStringLitImpl() {
      return this.delLitImpl();
   }

   protected JL delSwitchBlockImpl() {
      return this.delSwitchElementImpl();
   }

   protected JL delSwitchElementImpl() {
      return this.delStmtImpl();
   }

   protected JL delSwitchImpl() {
      return this.delStmtImpl();
   }

   protected JL delSynchronizedImpl() {
      return this.delStmtImpl();
   }

   protected JL delTermImpl() {
      return this.delNodeImpl();
   }

   protected JL delThrowImpl() {
      return this.delStmtImpl();
   }

   protected JL delTryImpl() {
      return this.delStmtImpl();
   }

   protected JL delTypeNodeImpl() {
      return this.delNodeImpl();
   }

   protected JL delUnaryImpl() {
      return this.delExprImpl();
   }

   protected JL delWhileImpl() {
      return this.delLoopImpl();
   }

   protected JL postDelAmbAssign(JL del) {
      return this.postDelAssign(del);
   }

   protected JL postDelAmbExpr(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelAmbPrefix(JL del) {
      return this.postDelNode(del);
   }

   protected JL postDelAmbQualifierNode(JL del) {
      return this.postDelNode(del);
   }

   protected JL postDelAmbReceiver(JL del) {
      return this.postDelNode(del);
   }

   protected JL postDelAmbTypeNode(JL del) {
      return this.postDelTypeNode(del);
   }

   protected JL postDelArrayAccess(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelArrayInit(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelArrayTypeNode(JL del) {
      return this.postDelTypeNode(del);
   }

   protected JL postDelAssert(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelAssign(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelLocalAssign(JL del) {
      return this.postDelAssign(del);
   }

   protected JL postDelFieldAssign(JL del) {
      return this.postDelAssign(del);
   }

   protected JL postDelArrayAccessAssign(JL del) {
      return this.postDelAssign(del);
   }

   protected JL postDelBinary(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelBlock(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelBooleanLit(JL del) {
      return this.postDelLit(del);
   }

   protected JL postDelBranch(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelCall(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelCanonicalTypeNode(JL del) {
      return this.postDelTypeNode(del);
   }

   protected JL postDelCase(JL del) {
      return this.postDelSwitchElement(del);
   }

   protected JL postDelCast(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelCatch(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelCharLit(JL del) {
      return this.postDelNumLit(del);
   }

   protected JL postDelClassBody(JL del) {
      return this.postDelTerm(del);
   }

   protected JL postDelClassDecl(JL del) {
      return this.postDelTerm(del);
   }

   protected JL postDelClassLit(JL del) {
      return this.postDelLit(del);
   }

   protected JL postDelClassMember(JL del) {
      return this.postDelNode(del);
   }

   protected JL postDelCodeDecl(JL del) {
      return this.postDelClassMember(del);
   }

   protected JL postDelConditional(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelConstructorCall(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelConstructorDecl(JL del) {
      return this.postDelProcedureDecl(del);
   }

   protected JL postDelDo(JL del) {
      return this.postDelLoop(del);
   }

   protected JL postDelEmpty(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelEval(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelExpr(JL del) {
      return this.postDelTerm(del);
   }

   protected JL postDelField(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelFieldDecl(JL del) {
      return this.postDelClassMember(del);
   }

   protected JL postDelFloatLit(JL del) {
      return this.postDelLit(del);
   }

   protected JL postDelFor(JL del) {
      return this.postDelLoop(del);
   }

   protected JL postDelFormal(JL del) {
      return this.postDelNode(del);
   }

   protected JL postDelIf(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelImport(JL del) {
      return this.postDelNode(del);
   }

   protected JL postDelInitializer(JL del) {
      return this.postDelCodeDecl(del);
   }

   protected JL postDelInstanceof(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelIntLit(JL del) {
      return this.postDelNumLit(del);
   }

   protected JL postDelLabeled(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelLit(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelLocal(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelLocalClassDecl(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelLocalDecl(JL del) {
      return this.postDelNode(del);
   }

   protected JL postDelLoop(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelMethodDecl(JL del) {
      return this.postDelProcedureDecl(del);
   }

   protected JL postDelNewArray(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelNode(JL del) {
      return del;
   }

   protected JL postDelNew(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelNullLit(JL del) {
      return this.postDelLit(del);
   }

   protected JL postDelNumLit(JL del) {
      return this.postDelLit(del);
   }

   protected JL postDelPackageNode(JL del) {
      return this.postDelNode(del);
   }

   protected JL postDelProcedureDecl(JL del) {
      return this.postDelCodeDecl(del);
   }

   protected JL postDelReturn(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelSourceCollection(JL del) {
      return this.postDelNode(del);
   }

   protected JL postDelSourceFile(JL del) {
      return this.postDelNode(del);
   }

   protected JL postDelSpecial(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelStmt(JL del) {
      return this.postDelTerm(del);
   }

   protected JL postDelStringLit(JL del) {
      return this.postDelLit(del);
   }

   protected JL postDelSwitchBlock(JL del) {
      return this.postDelSwitchElement(del);
   }

   protected JL postDelSwitchElement(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelSwitch(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelSynchronized(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelTerm(JL del) {
      return this.postDelNode(del);
   }

   protected JL postDelThrow(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelTry(JL del) {
      return this.postDelStmt(del);
   }

   protected JL postDelTypeNode(JL del) {
      return this.postDelNode(del);
   }

   protected JL postDelUnary(JL del) {
      return this.postDelExpr(del);
   }

   protected JL postDelWhile(JL del) {
      return this.postDelLoop(del);
   }
}
