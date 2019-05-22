package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import polyglot.ast.AmbPrefix;
import polyglot.ast.AmbQualifierNode;
import polyglot.ast.AmbReceiver;
import polyglot.ast.AmbTypeNode;
import polyglot.ast.ArrayInit;
import polyglot.ast.Assert;
import polyglot.ast.Block;
import polyglot.ast.Branch;
import polyglot.ast.Call;
import polyglot.ast.Case;
import polyglot.ast.ClassBody;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Disamb;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldDecl;
import polyglot.ast.If;
import polyglot.ast.LocalDecl;
import polyglot.ast.New;
import polyglot.ast.NewArray;
import polyglot.ast.NodeFactory;
import polyglot.ast.PackageNode;
import polyglot.ast.Prefix;
import polyglot.ast.QualifierNode;
import polyglot.ast.Receiver;
import polyglot.ast.Return;
import polyglot.ast.SourceFile;
import polyglot.ast.Special;
import polyglot.ast.Stmt;
import polyglot.ast.Try;
import polyglot.ast.TypeNode;
import polyglot.ast.Unary;
import polyglot.types.Flags;
import polyglot.util.Position;

public abstract class AbstractNodeFactory_c implements NodeFactory {
   public Disamb disamb() {
      return new Disamb_c();
   }

   public final AmbPrefix AmbPrefix(Position pos, String name) {
      return this.AmbPrefix(pos, (Prefix)null, name);
   }

   public final AmbReceiver AmbReceiver(Position pos, String name) {
      return this.AmbReceiver(pos, (Prefix)null, name);
   }

   public final AmbQualifierNode AmbQualifierNode(Position pos, String name) {
      return this.AmbQualifierNode(pos, (QualifierNode)null, name);
   }

   public final AmbTypeNode AmbTypeNode(Position pos, String name) {
      return this.AmbTypeNode(pos, (QualifierNode)null, name);
   }

   public final ArrayInit ArrayInit(Position pos) {
      return this.ArrayInit(pos, Collections.EMPTY_LIST);
   }

   public final Assert Assert(Position pos, Expr cond) {
      return this.Assert(pos, cond, (Expr)null);
   }

   public final Block Block(Position pos) {
      return this.Block(pos, Collections.EMPTY_LIST);
   }

   public final Block Block(Position pos, Stmt s1) {
      List l = new ArrayList(1);
      l.add(s1);
      return this.Block(pos, l);
   }

   public final Block Block(Position pos, Stmt s1, Stmt s2) {
      List l = new ArrayList(2);
      l.add(s1);
      l.add(s2);
      return this.Block(pos, l);
   }

   public final Block Block(Position pos, Stmt s1, Stmt s2, Stmt s3) {
      List l = new ArrayList(3);
      l.add(s1);
      l.add(s2);
      l.add(s3);
      return this.Block(pos, l);
   }

   public final Block Block(Position pos, Stmt s1, Stmt s2, Stmt s3, Stmt s4) {
      List l = new ArrayList(4);
      l.add(s1);
      l.add(s2);
      l.add(s3);
      l.add(s4);
      return this.Block(pos, l);
   }

   public final Branch Break(Position pos) {
      return this.Branch(pos, Branch.BREAK, (String)null);
   }

   public final Branch Break(Position pos, String label) {
      return this.Branch(pos, Branch.BREAK, label);
   }

   public final Branch Continue(Position pos) {
      return this.Branch(pos, Branch.CONTINUE, (String)null);
   }

   public final Branch Continue(Position pos, String label) {
      return this.Branch(pos, Branch.CONTINUE, label);
   }

   public final Branch Branch(Position pos, Branch.Kind kind) {
      return this.Branch(pos, kind, (String)null);
   }

   public final Call Call(Position pos, String name) {
      return this.Call(pos, (Receiver)null, (String)name, (List)Collections.EMPTY_LIST);
   }

   public final Call Call(Position pos, String name, Expr a1) {
      List l = new ArrayList(1);
      l.add(a1);
      return this.Call(pos, (Receiver)null, (String)name, (List)l);
   }

   public final Call Call(Position pos, String name, Expr a1, Expr a2) {
      List l = new ArrayList(2);
      l.add(a1);
      l.add(a2);
      return this.Call(pos, (Receiver)null, (String)name, (List)l);
   }

   public final Call Call(Position pos, String name, Expr a1, Expr a2, Expr a3) {
      List l = new ArrayList(3);
      l.add(a1);
      l.add(a2);
      l.add(a3);
      return this.Call(pos, (Receiver)null, (String)name, (List)l);
   }

   public final Call Call(Position pos, String name, Expr a1, Expr a2, Expr a3, Expr a4) {
      List l = new ArrayList(4);
      l.add(a1);
      l.add(a2);
      l.add(a3);
      l.add(a4);
      return this.Call(pos, (Receiver)null, (String)name, (List)l);
   }

   public final Call Call(Position pos, String name, List args) {
      return this.Call(pos, (Receiver)null, (String)name, (List)args);
   }

   public final Call Call(Position pos, Receiver target, String name) {
      return this.Call(pos, (Receiver)target, (String)name, (List)Collections.EMPTY_LIST);
   }

   public final Call Call(Position pos, Receiver target, String name, Expr a1) {
      List l = new ArrayList(1);
      l.add(a1);
      return this.Call(pos, (Receiver)target, (String)name, (List)l);
   }

   public final Call Call(Position pos, Receiver target, String name, Expr a1, Expr a2) {
      List l = new ArrayList(2);
      l.add(a1);
      l.add(a2);
      return this.Call(pos, (Receiver)target, (String)name, (List)l);
   }

   public final Call Call(Position pos, Receiver target, String name, Expr a1, Expr a2, Expr a3) {
      List l = new ArrayList(3);
      l.add(a1);
      l.add(a2);
      l.add(a3);
      return this.Call(pos, (Receiver)target, (String)name, (List)l);
   }

   public final Call Call(Position pos, Receiver target, String name, Expr a1, Expr a2, Expr a3, Expr a4) {
      List l = new ArrayList(4);
      l.add(a1);
      l.add(a2);
      l.add(a3);
      l.add(a4);
      return this.Call(pos, (Receiver)target, (String)name, (List)l);
   }

   public final Case Default(Position pos) {
      return this.Case(pos, (Expr)null);
   }

   public final ConstructorCall ThisCall(Position pos, List args) {
      return this.ConstructorCall(pos, ConstructorCall.THIS, (Expr)null, args);
   }

   public final ConstructorCall ThisCall(Position pos, Expr outer, List args) {
      return this.ConstructorCall(pos, ConstructorCall.THIS, outer, args);
   }

   public final ConstructorCall SuperCall(Position pos, List args) {
      return this.ConstructorCall(pos, ConstructorCall.SUPER, (Expr)null, args);
   }

   public final ConstructorCall SuperCall(Position pos, Expr outer, List args) {
      return this.ConstructorCall(pos, ConstructorCall.SUPER, outer, args);
   }

   public final ConstructorCall ConstructorCall(Position pos, ConstructorCall.Kind kind, List args) {
      return this.ConstructorCall(pos, kind, (Expr)null, args);
   }

   public final FieldDecl FieldDecl(Position pos, Flags flags, TypeNode type, String name) {
      return this.FieldDecl(pos, flags, type, name, (Expr)null);
   }

   public final Field Field(Position pos, String name) {
      return this.Field(pos, (Receiver)null, name);
   }

   public final If If(Position pos, Expr cond, Stmt consequent) {
      return this.If(pos, cond, consequent, (Stmt)null);
   }

   public final LocalDecl LocalDecl(Position pos, Flags flags, TypeNode type, String name) {
      return this.LocalDecl(pos, flags, type, name, (Expr)null);
   }

   public final New New(Position pos, TypeNode type, List args) {
      return this.New(pos, (Expr)null, type, args, (ClassBody)null);
   }

   public final New New(Position pos, TypeNode type, List args, ClassBody body) {
      return this.New(pos, (Expr)null, type, args, body);
   }

   public final New New(Position pos, Expr outer, TypeNode objectType, List args) {
      return this.New(pos, outer, objectType, args, (ClassBody)null);
   }

   public final NewArray NewArray(Position pos, TypeNode base, List dims) {
      return this.NewArray(pos, base, dims, 0, (ArrayInit)null);
   }

   public final NewArray NewArray(Position pos, TypeNode base, List dims, int addDims) {
      return this.NewArray(pos, base, dims, addDims, (ArrayInit)null);
   }

   public final NewArray NewArray(Position pos, TypeNode base, int addDims, ArrayInit init) {
      return this.NewArray(pos, base, Collections.EMPTY_LIST, addDims, init);
   }

   public final Return Return(Position pos) {
      return this.Return(pos, (Expr)null);
   }

   public final SourceFile SourceFile(Position pos, List decls) {
      return this.SourceFile(pos, (PackageNode)null, Collections.EMPTY_LIST, decls);
   }

   public final SourceFile SourceFile(Position pos, List imports, List decls) {
      return this.SourceFile(pos, (PackageNode)null, imports, decls);
   }

   public final Special This(Position pos) {
      return this.Special(pos, Special.THIS, (TypeNode)null);
   }

   public final Special This(Position pos, TypeNode outer) {
      return this.Special(pos, Special.THIS, outer);
   }

   public final Special Super(Position pos) {
      return this.Special(pos, Special.SUPER, (TypeNode)null);
   }

   public final Special Super(Position pos, TypeNode outer) {
      return this.Special(pos, Special.SUPER, outer);
   }

   public final Special Special(Position pos, Special.Kind kind) {
      return this.Special(pos, kind, (TypeNode)null);
   }

   public final Try Try(Position pos, Block tryBlock, List catchBlocks) {
      return this.Try(pos, tryBlock, catchBlocks, (Block)null);
   }

   public final Unary Unary(Position pos, Expr expr, Unary.Operator op) {
      return this.Unary(pos, op, expr);
   }
}
