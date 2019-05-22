package soot.javaToJimple.jj.ast;

import java.util.List;
import polyglot.ast.ArrayAccess;
import polyglot.ast.ArrayAccessAssign;
import polyglot.ast.ArrayInit;
import polyglot.ast.Assign;
import polyglot.ast.Binary;
import polyglot.ast.Call;
import polyglot.ast.Cast;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldAssign;
import polyglot.ast.FieldDecl;
import polyglot.ast.Local;
import polyglot.ast.LocalAssign;
import polyglot.ast.LocalDecl;
import polyglot.ast.NewArray;
import polyglot.ast.Return;
import polyglot.ast.TypeNode;
import polyglot.ast.Unary;
import polyglot.ext.jl.ast.NodeFactory_c;
import polyglot.types.Flags;
import polyglot.util.Position;

public class JjNodeFactory_c extends NodeFactory_c implements JjNodeFactory {
   public JjComma_c JjComma(Position pos, Expr first, Expr second) {
      JjComma_c n = new JjComma_c(pos, first, second);
      return n;
   }

   public JjAccessField_c JjAccessField(Position pos, Call getMeth, Call setMeth, Field field) {
      JjAccessField_c n = new JjAccessField_c(pos, getMeth, setMeth, field);
      return n;
   }

   public Unary Unary(Position pos, Unary.Operator op, Expr expr) {
      Unary n = new JjUnary_c(pos, op, expr);
      Unary n = (Unary)n.ext(this.extFactory().extUnary());
      n = (Unary)n.del(this.delFactory().delUnary());
      return n;
   }

   public Binary Binary(Position pos, Expr left, Binary.Operator op, Expr right) {
      Binary n = new JjBinary_c(pos, left, op, right);
      Binary n = (Binary)n.ext(this.extFactory().extBinary());
      n = (Binary)n.del(this.delFactory().delBinary());
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
      LocalAssign n = new JjLocalAssign_c(pos, left, op, right);
      LocalAssign n = (LocalAssign)n.ext(this.extFactory().extLocalAssign());
      n = (LocalAssign)n.del(this.delFactory().delLocalAssign());
      return n;
   }

   public LocalDecl LocalDecl(Position pos, Flags flags, TypeNode type, String name, Expr init) {
      LocalDecl n = new JjLocalDecl_c(pos, flags, type, name, init);
      LocalDecl n = (LocalDecl)n.ext(this.extFactory().extLocalDecl());
      n = (LocalDecl)n.del(this.delFactory().delLocalDecl());
      return n;
   }

   public FieldAssign FieldAssign(Position pos, Field left, Assign.Operator op, Expr right) {
      FieldAssign n = new JjFieldAssign_c(pos, left, op, right);
      FieldAssign n = (FieldAssign)n.ext(this.extFactory().extFieldAssign());
      n = (FieldAssign)n.del(this.delFactory().delFieldAssign());
      return n;
   }

   public FieldDecl FieldDecl(Position pos, Flags flags, TypeNode type, String name, Expr init) {
      FieldDecl n = new JjFieldDecl_c(pos, flags, type, name, init);
      FieldDecl n = (FieldDecl)n.ext(this.extFactory().extFieldDecl());
      n = (FieldDecl)n.del(this.delFactory().delFieldDecl());
      return n;
   }

   public ArrayAccessAssign ArrayAccessAssign(Position pos, ArrayAccess left, Assign.Operator op, Expr right) {
      ArrayAccessAssign n = new JjArrayAccessAssign_c(pos, left, op, right);
      ArrayAccessAssign n = (ArrayAccessAssign)n.ext(this.extFactory().extArrayAccessAssign());
      n = (ArrayAccessAssign)n.del(this.delFactory().delArrayAccessAssign());
      return n;
   }

   public Cast Cast(Position pos, TypeNode type, Expr expr) {
      Cast n = new JjCast_c(pos, type, expr);
      Cast n = (Cast)n.ext(this.extFactory().extCast());
      n = (Cast)n.del(this.delFactory().delCast());
      return n;
   }

   public NewArray NewArray(Position pos, TypeNode base, List dims, int addDims, ArrayInit init) {
      return super.NewArray(pos, base, dims, addDims, init);
   }

   public ArrayInit ArrayInit(Position pos, List elements) {
      ArrayInit n = new JjArrayInit_c(pos, elements);
      ArrayInit n = (ArrayInit)n.ext(this.extFactory().extArrayInit());
      n = (ArrayInit)n.del(this.delFactory().delArrayInit());
      return n;
   }

   public Return Return(Position pos, Expr expr) {
      Return n = new JjReturn_c(pos, expr);
      Return n = (Return)n.ext(this.extFactory().extReturn());
      n = (Return)n.del(this.delFactory().delReturn());
      return n;
   }
}
