package polyglot.ext.jl.ast;

import polyglot.ast.Ext;
import polyglot.ast.ExtFactory;

public abstract class AbstractExtFactory_c implements ExtFactory {
   private ExtFactory nextExtFactory;

   protected AbstractExtFactory_c() {
      this((ExtFactory)null);
   }

   protected AbstractExtFactory_c(ExtFactory nextExtFactory) {
      this.nextExtFactory = nextExtFactory;
   }

   public ExtFactory nextExtFactory() {
      return this.nextExtFactory;
   }

   protected Ext composeExts(Ext e1, Ext e2) {
      if (e1 == null) {
         return e2;
      } else {
         return e2 == null ? e1 : e2.ext(this.composeExts(e1, e2.ext()));
      }
   }

   public final Ext extAmbAssign() {
      Ext e = this.extAmbAssignImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extAmbAssign();
         e = this.composeExts(e, e2);
      }

      return this.postExtAmbAssign(e);
   }

   public final Ext extAmbExpr() {
      Ext e = this.extAmbExprImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extAmbExpr();
         e = this.composeExts(e, e2);
      }

      return this.postExtAmbExpr(e);
   }

   public final Ext extAmbPrefix() {
      Ext e = this.extAmbPrefixImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extAmbPrefix();
         e = this.composeExts(e, e2);
      }

      return this.postExtAmbPrefix(e);
   }

   public final Ext extAmbQualifierNode() {
      Ext e = this.extAmbQualifierNodeImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extAmbQualifierNode();
         e = this.composeExts(e, e2);
      }

      return this.postExtAmbQualifierNode(e);
   }

   public final Ext extAmbReceiver() {
      Ext e = this.extAmbReceiverImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extAmbReceiver();
         e = this.composeExts(e, e2);
      }

      return this.postExtAmbReceiver(e);
   }

   public final Ext extAmbTypeNode() {
      Ext e = this.extAmbTypeNodeImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extAmbTypeNode();
         e = this.composeExts(e, e2);
      }

      return this.postExtAmbTypeNode(e);
   }

   public final Ext extArrayAccess() {
      Ext e = this.extArrayAccessImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extArrayAccess();
         e = this.composeExts(e, e2);
      }

      return this.postExtArrayAccess(e);
   }

   public final Ext extArrayInit() {
      Ext e = this.extArrayInitImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extArrayInit();
         e = this.composeExts(e, e2);
      }

      return this.postExtArrayInit(e);
   }

   public final Ext extArrayTypeNode() {
      Ext e = this.extArrayTypeNodeImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extArrayTypeNode();
         e = this.composeExts(e, e2);
      }

      return this.postExtArrayTypeNode(e);
   }

   public final Ext extAssert() {
      Ext e = this.extAssertImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extAssert();
         e = this.composeExts(e, e2);
      }

      return this.postExtAssert(e);
   }

   public final Ext extAssign() {
      Ext e = this.extAssignImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extAssign();
         e = this.composeExts(e, e2);
      }

      return this.postExtAssign(e);
   }

   public final Ext extLocalAssign() {
      Ext e = this.extLocalAssignImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extLocalAssign();
         e = this.composeExts(e, e2);
      }

      return this.postExtLocalAssign(e);
   }

   public final Ext extFieldAssign() {
      Ext e = this.extFieldAssignImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extFieldAssign();
         e = this.composeExts(e, e2);
      }

      return this.postExtFieldAssign(e);
   }

   public final Ext extArrayAccessAssign() {
      Ext e = this.extArrayAccessAssignImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extArrayAccessAssign();
         e = this.composeExts(e, e2);
      }

      return this.postExtArrayAccessAssign(e);
   }

   public final Ext extBinary() {
      Ext e = this.extBinaryImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extBinary();
         e = this.composeExts(e, e2);
      }

      return this.postExtBinary(e);
   }

   public final Ext extBlock() {
      Ext e = this.extBlockImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extBlock();
         e = this.composeExts(e, e2);
      }

      return this.postExtBlock(e);
   }

   public final Ext extBooleanLit() {
      Ext e = this.extBooleanLitImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extBooleanLit();
         e = this.composeExts(e, e2);
      }

      return this.postExtBooleanLit(e);
   }

   public final Ext extBranch() {
      Ext e = this.extBranchImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extBranch();
         e = this.composeExts(e, e2);
      }

      return this.postExtBranch(e);
   }

   public final Ext extCall() {
      Ext e = this.extCallImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extCall();
         e = this.composeExts(e, e2);
      }

      return this.postExtCall(e);
   }

   public final Ext extCanonicalTypeNode() {
      Ext e = this.extCanonicalTypeNodeImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extCanonicalTypeNode();
         e = this.composeExts(e, e2);
      }

      return this.postExtCanonicalTypeNode(e);
   }

   public final Ext extCase() {
      Ext e = this.extCaseImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extCase();
         e = this.composeExts(e, e2);
      }

      return this.postExtCase(e);
   }

   public final Ext extCast() {
      Ext e = this.extCastImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extCast();
         e = this.composeExts(e, e2);
      }

      return this.postExtCast(e);
   }

   public final Ext extCatch() {
      Ext e = this.extCatchImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extCatch();
         e = this.composeExts(e, e2);
      }

      return this.postExtCatch(e);
   }

   public final Ext extCharLit() {
      Ext e = this.extCharLitImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extCharLit();
         e = this.composeExts(e, e2);
      }

      return this.postExtCharLit(e);
   }

   public final Ext extClassBody() {
      Ext e = this.extClassBodyImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extClassBody();
         e = this.composeExts(e, e2);
      }

      return this.postExtClassBody(e);
   }

   public final Ext extClassDecl() {
      Ext e = this.extClassDeclImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extClassDecl();
         e = this.composeExts(e, e2);
      }

      return this.postExtClassDecl(e);
   }

   public final Ext extClassLit() {
      Ext e = this.extClassLitImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extClassLit();
         e = this.composeExts(e, e2);
      }

      return this.postExtClassLit(e);
   }

   public final Ext extClassMember() {
      Ext e = this.extClassMemberImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extClassMember();
         e = this.composeExts(e, e2);
      }

      return this.postExtClassMember(e);
   }

   public final Ext extCodeDecl() {
      Ext e = this.extCodeDeclImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extCodeDecl();
         e = this.composeExts(e, e2);
      }

      return this.postExtCodeDecl(e);
   }

   public final Ext extConditional() {
      Ext e = this.extConditionalImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extConditional();
         e = this.composeExts(e, e2);
      }

      return this.postExtConditional(e);
   }

   public final Ext extConstructorCall() {
      Ext e = this.extConstructorCallImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extConstructorCall();
         e = this.composeExts(e, e2);
      }

      return this.postExtConstructorCall(e);
   }

   public final Ext extConstructorDecl() {
      Ext e = this.extConstructorDeclImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extConstructorDecl();
         e = this.composeExts(e, e2);
      }

      return this.postExtConstructorDecl(e);
   }

   public final Ext extDo() {
      Ext e = this.extDoImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extDo();
         e = this.composeExts(e, e2);
      }

      return this.postExtDo(e);
   }

   public final Ext extEmpty() {
      Ext e = this.extEmptyImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extEmpty();
         e = this.composeExts(e, e2);
      }

      return this.postExtEmpty(e);
   }

   public final Ext extEval() {
      Ext e = this.extEvalImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extEval();
         e = this.composeExts(e, e2);
      }

      return this.postExtEval(e);
   }

   public final Ext extExpr() {
      Ext e = this.extExprImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extExpr();
         e = this.composeExts(e, e2);
      }

      return this.postExtExpr(e);
   }

   public final Ext extField() {
      Ext e = this.extFieldImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extField();
         e = this.composeExts(e, e2);
      }

      return this.postExtField(e);
   }

   public final Ext extFieldDecl() {
      Ext e = this.extFieldDeclImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extFieldDecl();
         e = this.composeExts(e, e2);
      }

      return this.postExtFieldDecl(e);
   }

   public final Ext extFloatLit() {
      Ext e = this.extFloatLitImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extFloatLit();
         e = this.composeExts(e, e2);
      }

      return this.postExtFloatLit(e);
   }

   public final Ext extFor() {
      Ext e = this.extForImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extFor();
         e = this.composeExts(e, e2);
      }

      return this.postExtFor(e);
   }

   public final Ext extFormal() {
      Ext e = this.extFormalImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extFormal();
         e = this.composeExts(e, e2);
      }

      return this.postExtFormal(e);
   }

   public final Ext extIf() {
      Ext e = this.extIfImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extIf();
         e = this.composeExts(e, e2);
      }

      return this.postExtIf(e);
   }

   public final Ext extImport() {
      Ext e = this.extImportImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extImport();
         e = this.composeExts(e, e2);
      }

      return this.postExtImport(e);
   }

   public final Ext extInitializer() {
      Ext e = this.extInitializerImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extInitializer();
         e = this.composeExts(e, e2);
      }

      return this.postExtInitializer(e);
   }

   public final Ext extInstanceof() {
      Ext e = this.extInstanceofImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extInstanceof();
         e = this.composeExts(e, e2);
      }

      return this.postExtInstanceof(e);
   }

   public final Ext extIntLit() {
      Ext e = this.extIntLitImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extIntLit();
         e = this.composeExts(e, e2);
      }

      return this.postExtIntLit(e);
   }

   public final Ext extLabeled() {
      Ext e = this.extLabeledImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extLabeled();
         e = this.composeExts(e, e2);
      }

      return this.postExtLabeled(e);
   }

   public final Ext extLit() {
      Ext e = this.extLitImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extLit();
         e = this.composeExts(e, e2);
      }

      return this.postExtLit(e);
   }

   public final Ext extLocal() {
      Ext e = this.extLocalImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extLocal();
         e = this.composeExts(e, e2);
      }

      return this.postExtLocal(e);
   }

   public final Ext extLocalClassDecl() {
      Ext e = this.extLocalClassDeclImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extLocalClassDecl();
         e = this.composeExts(e, e2);
      }

      return this.postExtLocalClassDecl(e);
   }

   public final Ext extLocalDecl() {
      Ext e = this.extLocalDeclImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extLocalDecl();
         e = this.composeExts(e, e2);
      }

      return this.postExtLocalDecl(e);
   }

   public final Ext extLoop() {
      Ext e = this.extLoopImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extLoop();
         e = this.composeExts(e, e2);
      }

      return this.postExtLoop(e);
   }

   public final Ext extMethodDecl() {
      Ext e = this.extMethodDeclImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extMethodDecl();
         e = this.composeExts(e, e2);
      }

      return this.postExtMethodDecl(e);
   }

   public final Ext extNewArray() {
      Ext e = this.extNewArrayImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extNewArray();
         e = this.composeExts(e, e2);
      }

      return this.postExtNewArray(e);
   }

   public final Ext extNode() {
      Ext e = this.extNodeImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extNode();
         e = this.composeExts(e, e2);
      }

      return this.postExtNode(e);
   }

   public final Ext extNew() {
      Ext e = this.extNewImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extNew();
         e = this.composeExts(e, e2);
      }

      return this.postExtNew(e);
   }

   public final Ext extNullLit() {
      Ext e = this.extNullLitImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extNullLit();
         e = this.composeExts(e, e2);
      }

      return this.postExtNullLit(e);
   }

   public final Ext extNumLit() {
      Ext e = this.extNumLitImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extNumLit();
         e = this.composeExts(e, e2);
      }

      return this.postExtNumLit(e);
   }

   public final Ext extPackageNode() {
      Ext e = this.extPackageNodeImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extPackageNode();
         e = this.composeExts(e, e2);
      }

      return this.postExtPackageNode(e);
   }

   public final Ext extProcedureDecl() {
      Ext e = this.extProcedureDeclImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extProcedureDecl();
         e = this.composeExts(e, e2);
      }

      return this.postExtProcedureDecl(e);
   }

   public final Ext extReturn() {
      Ext e = this.extReturnImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extReturn();
         e = this.composeExts(e, e2);
      }

      return this.postExtReturn(e);
   }

   public final Ext extSourceCollection() {
      Ext e = this.extSourceCollectionImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extSourceCollection();
         e = this.composeExts(e, e2);
      }

      return this.postExtSourceCollection(e);
   }

   public final Ext extSourceFile() {
      Ext e = this.extSourceFileImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extSourceFile();
         e = this.composeExts(e, e2);
      }

      return this.postExtSourceFile(e);
   }

   public final Ext extSpecial() {
      Ext e = this.extSpecialImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extSpecial();
         e = this.composeExts(e, e2);
      }

      return this.postExtSpecial(e);
   }

   public final Ext extStmt() {
      Ext e = this.extStmtImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extStmt();
         e = this.composeExts(e, e2);
      }

      return this.postExtStmt(e);
   }

   public final Ext extStringLit() {
      Ext e = this.extStringLitImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extStringLit();
         e = this.composeExts(e, e2);
      }

      return this.postExtStringLit(e);
   }

   public final Ext extSwitchBlock() {
      Ext e = this.extSwitchBlockImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extSwitchBlock();
         e = this.composeExts(e, e2);
      }

      return this.postExtSwitchBlock(e);
   }

   public final Ext extSwitchElement() {
      Ext e = this.extSwitchElementImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extSwitchElement();
         e = this.composeExts(e, e2);
      }

      return this.postExtSwitchElement(e);
   }

   public final Ext extSwitch() {
      Ext e = this.extSwitchImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extSwitch();
         e = this.composeExts(e, e2);
      }

      return this.postExtSwitch(e);
   }

   public final Ext extSynchronized() {
      Ext e = this.extSynchronizedImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extSynchronized();
         e = this.composeExts(e, e2);
      }

      return this.postExtSynchronized(e);
   }

   public final Ext extTerm() {
      Ext e = this.extTermImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extTerm();
         e = this.composeExts(e, e2);
      }

      return this.postExtTerm(e);
   }

   public final Ext extThrow() {
      Ext e = this.extThrowImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extThrow();
         e = this.composeExts(e, e2);
      }

      return this.postExtThrow(e);
   }

   public final Ext extTry() {
      Ext e = this.extTryImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extTry();
         e = this.composeExts(e, e2);
      }

      return this.postExtTry(e);
   }

   public final Ext extTypeNode() {
      Ext e = this.extTypeNodeImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extTypeNode();
         e = this.composeExts(e, e2);
      }

      return this.postExtTypeNode(e);
   }

   public final Ext extUnary() {
      Ext e = this.extUnaryImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extUnary();
         e = this.composeExts(e, e2);
      }

      return this.postExtUnary(e);
   }

   public final Ext extWhile() {
      Ext e = this.extWhileImpl();
      if (this.nextExtFactory != null) {
         Ext e2 = this.nextExtFactory.extWhile();
         e = this.composeExts(e, e2);
      }

      return this.postExtWhile(e);
   }

   protected Ext extAmbAssignImpl() {
      return this.extAssignImpl();
   }

   protected Ext extAmbExprImpl() {
      return this.extExprImpl();
   }

   protected Ext extAmbPrefixImpl() {
      return this.extNodeImpl();
   }

   protected Ext extAmbQualifierNodeImpl() {
      return this.extNodeImpl();
   }

   protected Ext extAmbReceiverImpl() {
      return this.extNodeImpl();
   }

   protected Ext extAmbTypeNodeImpl() {
      return this.extTypeNodeImpl();
   }

   protected Ext extArrayAccessImpl() {
      return this.extExprImpl();
   }

   protected Ext extArrayInitImpl() {
      return this.extExprImpl();
   }

   protected Ext extArrayTypeNodeImpl() {
      return this.extTypeNodeImpl();
   }

   protected Ext extAssertImpl() {
      return this.extStmtImpl();
   }

   protected Ext extAssignImpl() {
      return this.extExprImpl();
   }

   protected Ext extLocalAssignImpl() {
      return this.extAssignImpl();
   }

   protected Ext extFieldAssignImpl() {
      return this.extAssignImpl();
   }

   protected Ext extArrayAccessAssignImpl() {
      return this.extAssignImpl();
   }

   protected Ext extBinaryImpl() {
      return this.extExprImpl();
   }

   protected Ext extBlockImpl() {
      return this.extStmtImpl();
   }

   protected Ext extBooleanLitImpl() {
      return this.extLitImpl();
   }

   protected Ext extBranchImpl() {
      return this.extStmtImpl();
   }

   protected Ext extCallImpl() {
      return this.extExprImpl();
   }

   protected Ext extCanonicalTypeNodeImpl() {
      return this.extTypeNodeImpl();
   }

   protected Ext extCaseImpl() {
      return this.extSwitchElementImpl();
   }

   protected Ext extCastImpl() {
      return this.extExprImpl();
   }

   protected Ext extCatchImpl() {
      return this.extStmtImpl();
   }

   protected Ext extCharLitImpl() {
      return this.extNumLitImpl();
   }

   protected Ext extClassBodyImpl() {
      return this.extTermImpl();
   }

   protected Ext extClassDeclImpl() {
      return this.extTermImpl();
   }

   protected Ext extClassLitImpl() {
      return this.extLitImpl();
   }

   protected Ext extClassMemberImpl() {
      return this.extNodeImpl();
   }

   protected Ext extCodeDeclImpl() {
      return this.extClassMemberImpl();
   }

   protected Ext extConditionalImpl() {
      return this.extExprImpl();
   }

   protected Ext extConstructorCallImpl() {
      return this.extStmtImpl();
   }

   protected Ext extConstructorDeclImpl() {
      return this.extProcedureDeclImpl();
   }

   protected Ext extDoImpl() {
      return this.extLoopImpl();
   }

   protected Ext extEmptyImpl() {
      return this.extStmtImpl();
   }

   protected Ext extEvalImpl() {
      return this.extStmtImpl();
   }

   protected Ext extExprImpl() {
      return this.extTermImpl();
   }

   protected Ext extFieldImpl() {
      return this.extExprImpl();
   }

   protected Ext extFieldDeclImpl() {
      return this.extClassMemberImpl();
   }

   protected Ext extFloatLitImpl() {
      return this.extLitImpl();
   }

   protected Ext extForImpl() {
      return this.extLoopImpl();
   }

   protected Ext extFormalImpl() {
      return this.extNodeImpl();
   }

   protected Ext extIfImpl() {
      return this.extStmtImpl();
   }

   protected Ext extImportImpl() {
      return this.extNodeImpl();
   }

   protected Ext extInitializerImpl() {
      return this.extCodeDeclImpl();
   }

   protected Ext extInstanceofImpl() {
      return this.extExprImpl();
   }

   protected Ext extIntLitImpl() {
      return this.extNumLitImpl();
   }

   protected Ext extLabeledImpl() {
      return this.extStmtImpl();
   }

   protected Ext extLitImpl() {
      return this.extExprImpl();
   }

   protected Ext extLocalImpl() {
      return this.extExprImpl();
   }

   protected Ext extLocalClassDeclImpl() {
      return this.extStmtImpl();
   }

   protected Ext extLocalDeclImpl() {
      return this.extStmtImpl();
   }

   protected Ext extLoopImpl() {
      return this.extStmtImpl();
   }

   protected Ext extMethodDeclImpl() {
      return this.extProcedureDeclImpl();
   }

   protected Ext extNewArrayImpl() {
      return this.extExprImpl();
   }

   protected Ext extNodeImpl() {
      return null;
   }

   protected Ext extNewImpl() {
      return this.extExprImpl();
   }

   protected Ext extNullLitImpl() {
      return this.extLitImpl();
   }

   protected Ext extNumLitImpl() {
      return this.extLitImpl();
   }

   protected Ext extPackageNodeImpl() {
      return this.extNodeImpl();
   }

   protected Ext extProcedureDeclImpl() {
      return this.extCodeDeclImpl();
   }

   protected Ext extReturnImpl() {
      return this.extStmtImpl();
   }

   protected Ext extSourceCollectionImpl() {
      return this.extNodeImpl();
   }

   protected Ext extSourceFileImpl() {
      return this.extNodeImpl();
   }

   protected Ext extSpecialImpl() {
      return this.extExprImpl();
   }

   protected Ext extStmtImpl() {
      return this.extTermImpl();
   }

   protected Ext extStringLitImpl() {
      return this.extLitImpl();
   }

   protected Ext extSwitchBlockImpl() {
      return this.extSwitchElementImpl();
   }

   protected Ext extSwitchElementImpl() {
      return this.extStmtImpl();
   }

   protected Ext extSwitchImpl() {
      return this.extStmtImpl();
   }

   protected Ext extSynchronizedImpl() {
      return this.extStmtImpl();
   }

   protected Ext extTermImpl() {
      return this.extNodeImpl();
   }

   protected Ext extThrowImpl() {
      return this.extStmtImpl();
   }

   protected Ext extTryImpl() {
      return this.extStmtImpl();
   }

   protected Ext extTypeNodeImpl() {
      return this.extNodeImpl();
   }

   protected Ext extUnaryImpl() {
      return this.extExprImpl();
   }

   protected Ext extWhileImpl() {
      return this.extLoopImpl();
   }

   protected Ext postExtAmbAssign(Ext ext) {
      return this.postExtAssign(ext);
   }

   protected Ext postExtAmbExpr(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtAmbPrefix(Ext ext) {
      return this.postExtNode(ext);
   }

   protected Ext postExtAmbQualifierNode(Ext ext) {
      return this.postExtNode(ext);
   }

   protected Ext postExtAmbReceiver(Ext ext) {
      return this.postExtNode(ext);
   }

   protected Ext postExtAmbTypeNode(Ext ext) {
      return this.postExtTypeNode(ext);
   }

   protected Ext postExtArrayAccess(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtArrayInit(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtArrayTypeNode(Ext ext) {
      return this.postExtTypeNode(ext);
   }

   protected Ext postExtAssert(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtAssign(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtLocalAssign(Ext ext) {
      return this.postExtAssign(ext);
   }

   protected Ext postExtFieldAssign(Ext ext) {
      return this.postExtAssign(ext);
   }

   protected Ext postExtArrayAccessAssign(Ext ext) {
      return this.postExtAssign(ext);
   }

   protected Ext postExtBinary(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtBlock(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtBooleanLit(Ext ext) {
      return this.postExtLit(ext);
   }

   protected Ext postExtBranch(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtCall(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtCanonicalTypeNode(Ext ext) {
      return this.postExtTypeNode(ext);
   }

   protected Ext postExtCase(Ext ext) {
      return this.postExtSwitchElement(ext);
   }

   protected Ext postExtCast(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtCatch(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtCharLit(Ext ext) {
      return this.postExtNumLit(ext);
   }

   protected Ext postExtClassBody(Ext ext) {
      return this.postExtTerm(ext);
   }

   protected Ext postExtClassDecl(Ext ext) {
      return this.postExtTerm(ext);
   }

   protected Ext postExtClassLit(Ext ext) {
      return this.postExtLit(ext);
   }

   protected Ext postExtClassMember(Ext ext) {
      return this.postExtNode(ext);
   }

   protected Ext postExtCodeDecl(Ext ext) {
      return this.postExtClassMember(ext);
   }

   protected Ext postExtConditional(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtConstructorCall(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtConstructorDecl(Ext ext) {
      return this.postExtProcedureDecl(ext);
   }

   protected Ext postExtDo(Ext ext) {
      return this.postExtLoop(ext);
   }

   protected Ext postExtEmpty(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtEval(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtExpr(Ext ext) {
      return this.postExtTerm(ext);
   }

   protected Ext postExtField(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtFieldDecl(Ext ext) {
      return this.postExtClassMember(ext);
   }

   protected Ext postExtFloatLit(Ext ext) {
      return this.postExtLit(ext);
   }

   protected Ext postExtFor(Ext ext) {
      return this.postExtLoop(ext);
   }

   protected Ext postExtFormal(Ext ext) {
      return this.postExtNode(ext);
   }

   protected Ext postExtIf(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtImport(Ext ext) {
      return this.postExtNode(ext);
   }

   protected Ext postExtInitializer(Ext ext) {
      return this.postExtCodeDecl(ext);
   }

   protected Ext postExtInstanceof(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtIntLit(Ext ext) {
      return this.postExtNumLit(ext);
   }

   protected Ext postExtLabeled(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtLit(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtLocal(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtLocalClassDecl(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtLocalDecl(Ext ext) {
      return this.postExtNode(ext);
   }

   protected Ext postExtLoop(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtMethodDecl(Ext ext) {
      return this.postExtProcedureDecl(ext);
   }

   protected Ext postExtNewArray(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtNode(Ext ext) {
      return ext;
   }

   protected Ext postExtNew(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtNullLit(Ext ext) {
      return this.postExtLit(ext);
   }

   protected Ext postExtNumLit(Ext ext) {
      return this.postExtLit(ext);
   }

   protected Ext postExtPackageNode(Ext ext) {
      return this.postExtNode(ext);
   }

   protected Ext postExtProcedureDecl(Ext ext) {
      return this.postExtCodeDecl(ext);
   }

   protected Ext postExtReturn(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtSourceCollection(Ext ext) {
      return this.postExtNode(ext);
   }

   protected Ext postExtSourceFile(Ext ext) {
      return this.postExtNode(ext);
   }

   protected Ext postExtSpecial(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtStmt(Ext ext) {
      return this.postExtTerm(ext);
   }

   protected Ext postExtStringLit(Ext ext) {
      return this.postExtLit(ext);
   }

   protected Ext postExtSwitchBlock(Ext ext) {
      return this.postExtSwitchElement(ext);
   }

   protected Ext postExtSwitchElement(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtSwitch(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtSynchronized(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtTerm(Ext ext) {
      return this.postExtNode(ext);
   }

   protected Ext postExtThrow(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtTry(Ext ext) {
      return this.postExtStmt(ext);
   }

   protected Ext postExtTypeNode(Ext ext) {
      return this.postExtNode(ext);
   }

   protected Ext postExtUnary(Ext ext) {
      return this.postExtExpr(ext);
   }

   protected Ext postExtWhile(Ext ext) {
      return this.postExtLoop(ext);
   }
}
