package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.AmbAssign;
import polyglot.ast.AmbExpr;
import polyglot.ast.AmbPrefix;
import polyglot.ast.AmbQualifierNode;
import polyglot.ast.AmbReceiver;
import polyglot.ast.AmbTypeNode;
import polyglot.ast.ArrayAccess;
import polyglot.ast.ArrayAccessAssign;
import polyglot.ast.ArrayInit;
import polyglot.ast.ArrayTypeNode;
import polyglot.ast.Assert;
import polyglot.ast.Assign;
import polyglot.ast.Binary;
import polyglot.ast.Block;
import polyglot.ast.BooleanLit;
import polyglot.ast.Branch;
import polyglot.ast.Call;
import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.Case;
import polyglot.ast.Cast;
import polyglot.ast.Catch;
import polyglot.ast.CharLit;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassLit;
import polyglot.ast.Conditional;
import polyglot.ast.ConstructorCall;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.DelFactory;
import polyglot.ast.Do;
import polyglot.ast.Empty;
import polyglot.ast.Eval;
import polyglot.ast.Expr;
import polyglot.ast.ExtFactory;
import polyglot.ast.Field;
import polyglot.ast.FieldAssign;
import polyglot.ast.FieldDecl;
import polyglot.ast.FloatLit;
import polyglot.ast.For;
import polyglot.ast.Formal;
import polyglot.ast.If;
import polyglot.ast.Import;
import polyglot.ast.Initializer;
import polyglot.ast.Instanceof;
import polyglot.ast.IntLit;
import polyglot.ast.Labeled;
import polyglot.ast.Local;
import polyglot.ast.LocalAssign;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.LocalDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.New;
import polyglot.ast.NewArray;
import polyglot.ast.NullLit;
import polyglot.ast.PackageNode;
import polyglot.ast.Prefix;
import polyglot.ast.QualifierNode;
import polyglot.ast.Receiver;
import polyglot.ast.Return;
import polyglot.ast.SourceCollection;
import polyglot.ast.SourceFile;
import polyglot.ast.Special;
import polyglot.ast.Stmt;
import polyglot.ast.StringLit;
import polyglot.ast.Switch;
import polyglot.ast.SwitchBlock;
import polyglot.ast.Synchronized;
import polyglot.ast.Throw;
import polyglot.ast.Try;
import polyglot.ast.TypeNode;
import polyglot.ast.Unary;
import polyglot.ast.While;
import polyglot.types.Flags;
import polyglot.types.Package;
import polyglot.types.Type;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class NodeFactory_c extends AbstractNodeFactory_c {
   private final ExtFactory extFactory;
   private final DelFactory delFactory;

   public NodeFactory_c() {
      this(new AbstractExtFactory_c() {
      }, new AbstractDelFactory_c() {
      });
   }

   public NodeFactory_c(ExtFactory extFactory) {
      this(extFactory, new AbstractDelFactory_c() {
      });
   }

   public NodeFactory_c(ExtFactory extFactory, DelFactory delFactory) {
      this.extFactory = extFactory;
      this.delFactory = delFactory;
   }

   protected ExtFactory extFactory() {
      return this.extFactory;
   }

   protected DelFactory delFactory() {
      return this.delFactory;
   }

   protected final ExtFactory findExtFactInstance(Class c) {
      for(ExtFactory e = this.extFactory(); e != null; e = e.nextExtFactory()) {
         if (c.isInstance(e)) {
            return e;
         }
      }

      return null;
   }

   public AmbPrefix AmbPrefix(Position pos, Prefix prefix, String name) {
      AmbPrefix n = new AmbPrefix_c(pos, prefix, name);
      AmbPrefix n = (AmbPrefix)n.ext(this.extFactory.extAmbPrefix());
      n = (AmbPrefix)n.del(this.delFactory.delAmbPrefix());
      return n;
   }

   public AmbReceiver AmbReceiver(Position pos, Prefix prefix, String name) {
      AmbReceiver n = new AmbReceiver_c(pos, prefix, name);
      AmbReceiver n = (AmbReceiver)n.ext(this.extFactory.extAmbReceiver());
      n = (AmbReceiver)n.del(this.delFactory.delAmbReceiver());
      return n;
   }

   public AmbQualifierNode AmbQualifierNode(Position pos, QualifierNode qualifier, String name) {
      AmbQualifierNode n = new AmbQualifierNode_c(pos, qualifier, name);
      AmbQualifierNode n = (AmbQualifierNode)n.ext(this.extFactory.extAmbQualifierNode());
      n = (AmbQualifierNode)n.del(this.delFactory.delAmbQualifierNode());
      return n;
   }

   public AmbExpr AmbExpr(Position pos, String name) {
      AmbExpr n = new AmbExpr_c(pos, name);
      AmbExpr n = (AmbExpr)n.ext(this.extFactory.extAmbExpr());
      n = (AmbExpr)n.del(this.delFactory.delAmbExpr());
      return n;
   }

   public AmbTypeNode AmbTypeNode(Position pos, QualifierNode qualifier, String name) {
      AmbTypeNode n = new AmbTypeNode_c(pos, qualifier, name);
      AmbTypeNode n = (AmbTypeNode)n.ext(this.extFactory.extAmbTypeNode());
      n = (AmbTypeNode)n.del(this.delFactory.delAmbTypeNode());
      return n;
   }

   public ArrayAccess ArrayAccess(Position pos, Expr base, Expr index) {
      ArrayAccess n = new ArrayAccess_c(pos, base, index);
      ArrayAccess n = (ArrayAccess)n.ext(this.extFactory.extArrayAccess());
      n = (ArrayAccess)n.del(this.delFactory.delArrayAccess());
      return n;
   }

   public ArrayInit ArrayInit(Position pos, List elements) {
      ArrayInit n = new ArrayInit_c(pos, elements);
      ArrayInit n = (ArrayInit)n.ext(this.extFactory.extArrayInit());
      n = (ArrayInit)n.del(this.delFactory.delArrayInit());
      return n;
   }

   public Assert Assert(Position pos, Expr cond, Expr errorMessage) {
      Assert n = new Assert_c(pos, cond, errorMessage);
      Assert n = (Assert)n.ext(this.extFactory.extAssert());
      n = (Assert)n.del(this.delFactory.delAssert());
      return n;
   }

   public Assign Assign(Position pos, Expr left, Assign.Operator op, Expr right) {
      if (left instanceof Local) {
         return this.LocalAssign(pos, (Local)left, op, right);
      } else if (left instanceof Field) {
         return this.FieldAssign(pos, (Field)left, op, right);
      } else {
         return (Assign)(left instanceof ArrayAccess ? this.ArrayAccessAssign(pos, (ArrayAccess)left, op, right) : this.AmbAssign(pos, left, op, right));
      }
   }

   public LocalAssign LocalAssign(Position pos, Local left, Assign.Operator op, Expr right) {
      LocalAssign n = new LocalAssign_c(pos, left, op, right);
      LocalAssign n = (LocalAssign)n.ext(this.extFactory.extLocalAssign());
      n = (LocalAssign)n.del(this.delFactory.delLocalAssign());
      return n;
   }

   public FieldAssign FieldAssign(Position pos, Field left, Assign.Operator op, Expr right) {
      FieldAssign n = new FieldAssign_c(pos, left, op, right);
      FieldAssign n = (FieldAssign)n.ext(this.extFactory.extFieldAssign());
      n = (FieldAssign)n.del(this.delFactory.delFieldAssign());
      return n;
   }

   public ArrayAccessAssign ArrayAccessAssign(Position pos, ArrayAccess left, Assign.Operator op, Expr right) {
      ArrayAccessAssign n = new ArrayAccessAssign_c(pos, left, op, right);
      ArrayAccessAssign n = (ArrayAccessAssign)n.ext(this.extFactory.extArrayAccessAssign());
      n = (ArrayAccessAssign)n.del(this.delFactory.delArrayAccessAssign());
      return n;
   }

   public AmbAssign AmbAssign(Position pos, Expr left, Assign.Operator op, Expr right) {
      AmbAssign n = new AmbAssign_c(pos, left, op, right);
      AmbAssign n = (AmbAssign)n.ext(this.extFactory.extAmbAssign());
      n = (AmbAssign)n.del(this.delFactory.delAmbAssign());
      return n;
   }

   public Binary Binary(Position pos, Expr left, Binary.Operator op, Expr right) {
      Binary n = new Binary_c(pos, left, op, right);
      Binary n = (Binary)n.ext(this.extFactory.extBinary());
      n = (Binary)n.del(this.delFactory.delBinary());
      return n;
   }

   public Block Block(Position pos, List statements) {
      Block n = new Block_c(pos, statements);
      Block n = (Block)n.ext(this.extFactory.extBlock());
      n = (Block)n.del(this.delFactory.delBlock());
      return n;
   }

   public SwitchBlock SwitchBlock(Position pos, List statements) {
      SwitchBlock n = new SwitchBlock_c(pos, statements);
      SwitchBlock n = (SwitchBlock)n.ext(this.extFactory.extSwitchBlock());
      n = (SwitchBlock)n.del(this.delFactory.delSwitchBlock());
      return n;
   }

   public BooleanLit BooleanLit(Position pos, boolean value) {
      BooleanLit n = new BooleanLit_c(pos, value);
      BooleanLit n = (BooleanLit)n.ext(this.extFactory.extBooleanLit());
      n = (BooleanLit)n.del(this.delFactory.delBooleanLit());
      return n;
   }

   public Branch Branch(Position pos, Branch.Kind kind, String label) {
      Branch n = new Branch_c(pos, kind, label);
      Branch n = (Branch)n.ext(this.extFactory.extBranch());
      n = (Branch)n.del(this.delFactory.delBranch());
      return n;
   }

   public Call Call(Position pos, Receiver target, String name, List args) {
      Call n = new Call_c(pos, target, name, args);
      Call n = (Call)n.ext(this.extFactory.extCall());
      n = (Call)n.del(this.delFactory.delCall());
      return n;
   }

   public Case Case(Position pos, Expr expr) {
      Case n = new Case_c(pos, expr);
      Case n = (Case)n.ext(this.extFactory.extCase());
      n = (Case)n.del(this.delFactory.delCase());
      return n;
   }

   public Cast Cast(Position pos, TypeNode type, Expr expr) {
      Cast n = new Cast_c(pos, type, expr);
      Cast n = (Cast)n.ext(this.extFactory.extCast());
      n = (Cast)n.del(this.delFactory.delCast());
      return n;
   }

   public Catch Catch(Position pos, Formal formal, Block body) {
      Catch n = new Catch_c(pos, formal, body);
      Catch n = (Catch)n.ext(this.extFactory.extCatch());
      n = (Catch)n.del(this.delFactory.delCatch());
      return n;
   }

   public CharLit CharLit(Position pos, char value) {
      CharLit n = new CharLit_c(pos, value);
      CharLit n = (CharLit)n.ext(this.extFactory.extCharLit());
      n = (CharLit)n.del(this.delFactory.delCharLit());
      return n;
   }

   public ClassBody ClassBody(Position pos, List members) {
      ClassBody n = new ClassBody_c(pos, members);
      ClassBody n = (ClassBody)n.ext(this.extFactory.extClassBody());
      n = (ClassBody)n.del(this.delFactory.delClassBody());
      return n;
   }

   public ClassDecl ClassDecl(Position pos, Flags flags, String name, TypeNode superClass, List interfaces, ClassBody body) {
      ClassDecl n = new ClassDecl_c(pos, flags, name, superClass, interfaces, body);
      ClassDecl n = (ClassDecl)n.ext(this.extFactory.extClassDecl());
      n = (ClassDecl)n.del(this.delFactory.delClassDecl());
      return n;
   }

   public ClassLit ClassLit(Position pos, TypeNode typeNode) {
      ClassLit n = new ClassLit_c(pos, typeNode);
      ClassLit n = (ClassLit)n.ext(this.extFactory.extClassLit());
      n = (ClassLit)n.del(this.delFactory.delClassLit());
      return n;
   }

   public Conditional Conditional(Position pos, Expr cond, Expr consequent, Expr alternative) {
      Conditional n = new Conditional_c(pos, cond, consequent, alternative);
      Conditional n = (Conditional)n.ext(this.extFactory.extConditional());
      n = (Conditional)n.del(this.delFactory.delConditional());
      return n;
   }

   public ConstructorCall ConstructorCall(Position pos, ConstructorCall.Kind kind, Expr outer, List args) {
      ConstructorCall n = new ConstructorCall_c(pos, kind, outer, args);
      ConstructorCall n = (ConstructorCall)n.ext(this.extFactory.extConstructorCall());
      n = (ConstructorCall)n.del(this.delFactory.delConstructorCall());
      return n;
   }

   public ConstructorDecl ConstructorDecl(Position pos, Flags flags, String name, List formals, List throwTypes, Block body) {
      ConstructorDecl n = new ConstructorDecl_c(pos, flags, name, formals, throwTypes, body);
      ConstructorDecl n = (ConstructorDecl)n.ext(this.extFactory.extConstructorDecl());
      n = (ConstructorDecl)n.del(this.delFactory.delConstructorDecl());
      return n;
   }

   public FieldDecl FieldDecl(Position pos, Flags flags, TypeNode type, String name, Expr init) {
      FieldDecl n = new FieldDecl_c(pos, flags, type, name, init);
      FieldDecl n = (FieldDecl)n.ext(this.extFactory.extFieldDecl());
      n = (FieldDecl)n.del(this.delFactory.delFieldDecl());
      return n;
   }

   public Do Do(Position pos, Stmt body, Expr cond) {
      Do n = new Do_c(pos, body, cond);
      Do n = (Do)n.ext(this.extFactory.extDo());
      n = (Do)n.del(this.delFactory.delDo());
      return n;
   }

   public Empty Empty(Position pos) {
      Empty n = new Empty_c(pos);
      Empty n = (Empty)n.ext(this.extFactory.extEmpty());
      n = (Empty)n.del(this.delFactory.delEmpty());
      return n;
   }

   public Eval Eval(Position pos, Expr expr) {
      Eval n = new Eval_c(pos, expr);
      Eval n = (Eval)n.ext(this.extFactory.extEval());
      n = (Eval)n.del(this.delFactory.delEval());
      return n;
   }

   public Field Field(Position pos, Receiver target, String name) {
      Field n = new Field_c(pos, target, name);
      Field n = (Field)n.ext(this.extFactory.extField());
      n = (Field)n.del(this.delFactory.delField());
      return n;
   }

   public FloatLit FloatLit(Position pos, FloatLit.Kind kind, double value) {
      FloatLit n = new FloatLit_c(pos, kind, value);
      FloatLit n = (FloatLit)n.ext(this.extFactory.extFloatLit());
      n = (FloatLit)n.del(this.delFactory.delFloatLit());
      return n;
   }

   public For For(Position pos, List inits, Expr cond, List iters, Stmt body) {
      For n = new For_c(pos, inits, cond, iters, body);
      For n = (For)n.ext(this.extFactory.extFor());
      n = (For)n.del(this.delFactory.delFor());
      return n;
   }

   public Formal Formal(Position pos, Flags flags, TypeNode type, String name) {
      Formal n = new Formal_c(pos, flags, type, name);
      Formal n = (Formal)n.ext(this.extFactory.extFormal());
      n = (Formal)n.del(this.delFactory.delFormal());
      return n;
   }

   public If If(Position pos, Expr cond, Stmt consequent, Stmt alternative) {
      If n = new If_c(pos, cond, consequent, alternative);
      If n = (If)n.ext(this.extFactory.extIf());
      n = (If)n.del(this.delFactory.delIf());
      return n;
   }

   public Import Import(Position pos, Import.Kind kind, String name) {
      Import n = new Import_c(pos, kind, name);
      Import n = (Import)n.ext(this.extFactory.extImport());
      n = (Import)n.del(this.delFactory.delImport());
      return n;
   }

   public Initializer Initializer(Position pos, Flags flags, Block body) {
      Initializer n = new Initializer_c(pos, flags, body);
      Initializer n = (Initializer)n.ext(this.extFactory.extInitializer());
      n = (Initializer)n.del(this.delFactory.delInitializer());
      return n;
   }

   public Instanceof Instanceof(Position pos, Expr expr, TypeNode type) {
      Instanceof n = new Instanceof_c(pos, expr, type);
      Instanceof n = (Instanceof)n.ext(this.extFactory.extInstanceof());
      n = (Instanceof)n.del(this.delFactory.delInstanceof());
      return n;
   }

   public IntLit IntLit(Position pos, IntLit.Kind kind, long value) {
      IntLit n = new IntLit_c(pos, kind, value);
      IntLit n = (IntLit)n.ext(this.extFactory.extIntLit());
      n = (IntLit)n.del(this.delFactory.delIntLit());
      return n;
   }

   public Labeled Labeled(Position pos, String label, Stmt body) {
      Labeled n = new Labeled_c(pos, label, body);
      Labeled n = (Labeled)n.ext(this.extFactory.extLabeled());
      n = (Labeled)n.del(this.delFactory.delLabeled());
      return n;
   }

   public Local Local(Position pos, String name) {
      Local n = new Local_c(pos, name);
      Local n = (Local)n.ext(this.extFactory.extLocal());
      n = (Local)n.del(this.delFactory.delLocal());
      return n;
   }

   public LocalClassDecl LocalClassDecl(Position pos, ClassDecl decl) {
      LocalClassDecl n = new LocalClassDecl_c(pos, decl);
      LocalClassDecl n = (LocalClassDecl)n.ext(this.extFactory.extLocalClassDecl());
      n = (LocalClassDecl)n.del(this.delFactory.delLocalClassDecl());
      return n;
   }

   public LocalDecl LocalDecl(Position pos, Flags flags, TypeNode type, String name, Expr init) {
      LocalDecl n = new LocalDecl_c(pos, flags, type, name, init);
      LocalDecl n = (LocalDecl)n.ext(this.extFactory.extLocalDecl());
      n = (LocalDecl)n.del(this.delFactory.delLocalDecl());
      return n;
   }

   public MethodDecl MethodDecl(Position pos, Flags flags, TypeNode returnType, String name, List formals, List throwTypes, Block body) {
      MethodDecl n = new MethodDecl_c(pos, flags, returnType, name, formals, throwTypes, body);
      MethodDecl n = (MethodDecl)n.ext(this.extFactory.extMethodDecl());
      n = (MethodDecl)n.del(this.delFactory.delMethodDecl());
      return n;
   }

   public New New(Position pos, Expr outer, TypeNode objectType, List args, ClassBody body) {
      New n = new New_c(pos, outer, objectType, args, body);
      New n = (New)n.ext(this.extFactory.extNew());
      n = (New)n.del(this.delFactory.delNew());
      return n;
   }

   public NewArray NewArray(Position pos, TypeNode base, List dims, int addDims, ArrayInit init) {
      NewArray n = new NewArray_c(pos, base, dims, addDims, init);
      NewArray n = (NewArray)n.ext(this.extFactory.extNewArray());
      n = (NewArray)n.del(this.delFactory.delNewArray());
      return n;
   }

   public NullLit NullLit(Position pos) {
      NullLit n = new NullLit_c(pos);
      NullLit n = (NullLit)n.ext(this.extFactory.extNullLit());
      n = (NullLit)n.del(this.delFactory.delNullLit());
      return n;
   }

   public Return Return(Position pos, Expr expr) {
      Return n = new Return_c(pos, expr);
      Return n = (Return)n.ext(this.extFactory.extReturn());
      n = (Return)n.del(this.delFactory.delReturn());
      return n;
   }

   public SourceCollection SourceCollection(Position pos, List sources) {
      SourceCollection n = new SourceCollection_c(pos, sources);
      SourceCollection n = (SourceCollection)n.ext(this.extFactory.extSourceCollection());
      n = (SourceCollection)n.del(this.delFactory.delSourceCollection());
      return n;
   }

   public SourceFile SourceFile(Position pos, PackageNode packageName, List imports, List decls) {
      SourceFile n = new SourceFile_c(pos, packageName, imports, decls);
      SourceFile n = (SourceFile)n.ext(this.extFactory.extSourceFile());
      n = (SourceFile)n.del(this.delFactory.delSourceFile());
      return n;
   }

   public Special Special(Position pos, Special.Kind kind, TypeNode outer) {
      Special n = new Special_c(pos, kind, outer);
      Special n = (Special)n.ext(this.extFactory.extSpecial());
      n = (Special)n.del(this.delFactory.delSpecial());
      return n;
   }

   public StringLit StringLit(Position pos, String value) {
      StringLit n = new StringLit_c(pos, value);
      StringLit n = (StringLit)n.ext(this.extFactory.extStringLit());
      n = (StringLit)n.del(this.delFactory.delStringLit());
      return n;
   }

   public Switch Switch(Position pos, Expr expr, List elements) {
      Switch n = new Switch_c(pos, expr, elements);
      Switch n = (Switch)n.ext(this.extFactory.extSwitch());
      n = (Switch)n.del(this.delFactory.delSwitch());
      return n;
   }

   public Synchronized Synchronized(Position pos, Expr expr, Block body) {
      Synchronized n = new Synchronized_c(pos, expr, body);
      Synchronized n = (Synchronized)n.ext(this.extFactory.extSynchronized());
      n = (Synchronized)n.del(this.delFactory.delSynchronized());
      return n;
   }

   public Throw Throw(Position pos, Expr expr) {
      Throw n = new Throw_c(pos, expr);
      Throw n = (Throw)n.ext(this.extFactory.extThrow());
      n = (Throw)n.del(this.delFactory.delThrow());
      return n;
   }

   public Try Try(Position pos, Block tryBlock, List catchBlocks, Block finallyBlock) {
      Try n = new Try_c(pos, tryBlock, catchBlocks, finallyBlock);
      Try n = (Try)n.ext(this.extFactory.extTry());
      n = (Try)n.del(this.delFactory.delTry());
      return n;
   }

   public ArrayTypeNode ArrayTypeNode(Position pos, TypeNode base) {
      ArrayTypeNode n = new ArrayTypeNode_c(pos, base);
      ArrayTypeNode n = (ArrayTypeNode)n.ext(this.extFactory.extArrayTypeNode());
      n = (ArrayTypeNode)n.del(this.delFactory.delArrayTypeNode());
      return n;
   }

   public CanonicalTypeNode CanonicalTypeNode(Position pos, Type type) {
      if (!type.isCanonical()) {
         throw new InternalCompilerError("Cannot construct a canonical type node for a non-canonical type.");
      } else {
         CanonicalTypeNode n = new CanonicalTypeNode_c(pos, type);
         CanonicalTypeNode n = (CanonicalTypeNode)n.ext(this.extFactory.extCanonicalTypeNode());
         n = (CanonicalTypeNode)n.del(this.delFactory.delCanonicalTypeNode());
         return n;
      }
   }

   public PackageNode PackageNode(Position pos, Package p) {
      PackageNode n = new PackageNode_c(pos, p);
      PackageNode n = (PackageNode)n.ext(this.extFactory.extPackageNode());
      n = (PackageNode)n.del(this.delFactory.delPackageNode());
      return n;
   }

   public Unary Unary(Position pos, Unary.Operator op, Expr expr) {
      Unary n = new Unary_c(pos, op, expr);
      Unary n = (Unary)n.ext(this.extFactory.extUnary());
      n = (Unary)n.del(this.delFactory.delUnary());
      return n;
   }

   public While While(Position pos, Expr cond, Stmt body) {
      While n = new While_c(pos, cond, body);
      While n = (While)n.ext(this.extFactory.extWhile());
      n = (While)n.del(this.delFactory.delWhile());
      return n;
   }
}
