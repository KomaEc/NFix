package polyglot.ast;

import java.util.List;
import polyglot.types.Flags;
import polyglot.types.Package;
import polyglot.types.Type;
import polyglot.util.Position;

public interface NodeFactory {
   Disamb disamb();

   AmbExpr AmbExpr(Position var1, String var2);

   AmbReceiver AmbReceiver(Position var1, String var2);

   AmbReceiver AmbReceiver(Position var1, Prefix var2, String var3);

   AmbQualifierNode AmbQualifierNode(Position var1, String var2);

   AmbQualifierNode AmbQualifierNode(Position var1, QualifierNode var2, String var3);

   AmbPrefix AmbPrefix(Position var1, String var2);

   AmbPrefix AmbPrefix(Position var1, Prefix var2, String var3);

   AmbTypeNode AmbTypeNode(Position var1, String var2);

   AmbTypeNode AmbTypeNode(Position var1, QualifierNode var2, String var3);

   ArrayTypeNode ArrayTypeNode(Position var1, TypeNode var2);

   CanonicalTypeNode CanonicalTypeNode(Position var1, Type var2);

   ArrayAccess ArrayAccess(Position var1, Expr var2, Expr var3);

   ArrayInit ArrayInit(Position var1);

   ArrayInit ArrayInit(Position var1, List var2);

   Assert Assert(Position var1, Expr var2);

   Assert Assert(Position var1, Expr var2, Expr var3);

   Assign Assign(Position var1, Expr var2, Assign.Operator var3, Expr var4);

   LocalAssign LocalAssign(Position var1, Local var2, Assign.Operator var3, Expr var4);

   FieldAssign FieldAssign(Position var1, Field var2, Assign.Operator var3, Expr var4);

   ArrayAccessAssign ArrayAccessAssign(Position var1, ArrayAccess var2, Assign.Operator var3, Expr var4);

   AmbAssign AmbAssign(Position var1, Expr var2, Assign.Operator var3, Expr var4);

   Binary Binary(Position var1, Expr var2, Binary.Operator var3, Expr var4);

   Block Block(Position var1);

   Block Block(Position var1, Stmt var2);

   Block Block(Position var1, Stmt var2, Stmt var3);

   Block Block(Position var1, Stmt var2, Stmt var3, Stmt var4);

   Block Block(Position var1, Stmt var2, Stmt var3, Stmt var4, Stmt var5);

   Block Block(Position var1, List var2);

   SwitchBlock SwitchBlock(Position var1, List var2);

   BooleanLit BooleanLit(Position var1, boolean var2);

   Branch Break(Position var1);

   Branch Break(Position var1, String var2);

   Branch Continue(Position var1);

   Branch Continue(Position var1, String var2);

   Branch Branch(Position var1, Branch.Kind var2);

   Branch Branch(Position var1, Branch.Kind var2, String var3);

   Call Call(Position var1, String var2);

   Call Call(Position var1, String var2, Expr var3);

   Call Call(Position var1, String var2, Expr var3, Expr var4);

   Call Call(Position var1, String var2, Expr var3, Expr var4, Expr var5);

   Call Call(Position var1, String var2, Expr var3, Expr var4, Expr var5, Expr var6);

   Call Call(Position var1, String var2, List var3);

   Call Call(Position var1, Receiver var2, String var3);

   Call Call(Position var1, Receiver var2, String var3, Expr var4);

   Call Call(Position var1, Receiver var2, String var3, Expr var4, Expr var5);

   Call Call(Position var1, Receiver var2, String var3, Expr var4, Expr var5, Expr var6);

   Call Call(Position var1, Receiver var2, String var3, Expr var4, Expr var5, Expr var6, Expr var7);

   Call Call(Position var1, Receiver var2, String var3, List var4);

   Case Default(Position var1);

   Case Case(Position var1, Expr var2);

   Cast Cast(Position var1, TypeNode var2, Expr var3);

   Catch Catch(Position var1, Formal var2, Block var3);

   CharLit CharLit(Position var1, char var2);

   ClassBody ClassBody(Position var1, List var2);

   ClassDecl ClassDecl(Position var1, Flags var2, String var3, TypeNode var4, List var5, ClassBody var6);

   ClassLit ClassLit(Position var1, TypeNode var2);

   Conditional Conditional(Position var1, Expr var2, Expr var3, Expr var4);

   ConstructorCall ThisCall(Position var1, List var2);

   ConstructorCall ThisCall(Position var1, Expr var2, List var3);

   ConstructorCall SuperCall(Position var1, List var2);

   ConstructorCall SuperCall(Position var1, Expr var2, List var3);

   ConstructorCall ConstructorCall(Position var1, ConstructorCall.Kind var2, List var3);

   ConstructorCall ConstructorCall(Position var1, ConstructorCall.Kind var2, Expr var3, List var4);

   ConstructorDecl ConstructorDecl(Position var1, Flags var2, String var3, List var4, List var5, Block var6);

   FieldDecl FieldDecl(Position var1, Flags var2, TypeNode var3, String var4);

   FieldDecl FieldDecl(Position var1, Flags var2, TypeNode var3, String var4, Expr var5);

   Do Do(Position var1, Stmt var2, Expr var3);

   Empty Empty(Position var1);

   Eval Eval(Position var1, Expr var2);

   Field Field(Position var1, String var2);

   Field Field(Position var1, Receiver var2, String var3);

   FloatLit FloatLit(Position var1, FloatLit.Kind var2, double var3);

   For For(Position var1, List var2, Expr var3, List var4, Stmt var5);

   Formal Formal(Position var1, Flags var2, TypeNode var3, String var4);

   If If(Position var1, Expr var2, Stmt var3);

   If If(Position var1, Expr var2, Stmt var3, Stmt var4);

   Import Import(Position var1, Import.Kind var2, String var3);

   Initializer Initializer(Position var1, Flags var2, Block var3);

   Instanceof Instanceof(Position var1, Expr var2, TypeNode var3);

   IntLit IntLit(Position var1, IntLit.Kind var2, long var3);

   Labeled Labeled(Position var1, String var2, Stmt var3);

   Local Local(Position var1, String var2);

   LocalClassDecl LocalClassDecl(Position var1, ClassDecl var2);

   LocalDecl LocalDecl(Position var1, Flags var2, TypeNode var3, String var4);

   LocalDecl LocalDecl(Position var1, Flags var2, TypeNode var3, String var4, Expr var5);

   MethodDecl MethodDecl(Position var1, Flags var2, TypeNode var3, String var4, List var5, List var6, Block var7);

   New New(Position var1, TypeNode var2, List var3);

   New New(Position var1, TypeNode var2, List var3, ClassBody var4);

   New New(Position var1, Expr var2, TypeNode var3, List var4);

   New New(Position var1, Expr var2, TypeNode var3, List var4, ClassBody var5);

   NewArray NewArray(Position var1, TypeNode var2, List var3);

   NewArray NewArray(Position var1, TypeNode var2, List var3, int var4);

   NewArray NewArray(Position var1, TypeNode var2, int var3, ArrayInit var4);

   NewArray NewArray(Position var1, TypeNode var2, List var3, int var4, ArrayInit var5);

   NullLit NullLit(Position var1);

   Return Return(Position var1);

   Return Return(Position var1, Expr var2);

   SourceCollection SourceCollection(Position var1, List var2);

   SourceFile SourceFile(Position var1, List var2);

   SourceFile SourceFile(Position var1, List var2, List var3);

   SourceFile SourceFile(Position var1, PackageNode var2, List var3, List var4);

   Special This(Position var1);

   Special This(Position var1, TypeNode var2);

   Special Super(Position var1);

   Special Super(Position var1, TypeNode var2);

   Special Special(Position var1, Special.Kind var2);

   Special Special(Position var1, Special.Kind var2, TypeNode var3);

   StringLit StringLit(Position var1, String var2);

   Switch Switch(Position var1, Expr var2, List var3);

   Synchronized Synchronized(Position var1, Expr var2, Block var3);

   Throw Throw(Position var1, Expr var2);

   Try Try(Position var1, Block var2, List var3);

   Try Try(Position var1, Block var2, List var3, Block var4);

   PackageNode PackageNode(Position var1, Package var2);

   Unary Unary(Position var1, Unary.Operator var2, Expr var3);

   Unary Unary(Position var1, Expr var2, Unary.Operator var3);

   While While(Position var1, Expr var2, Stmt var3);
}
