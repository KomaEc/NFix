package soot.javaToJimple;

import java.util.List;
import polyglot.ast.Assign;
import polyglot.ast.Block;
import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Receiver;
import polyglot.ast.Stmt;
import polyglot.ast.Unary;
import soot.Local;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
import soot.jimple.Constant;
import soot.jimple.FieldRef;
import soot.jimple.JimpleBody;

public abstract class AbstractJimpleBodyBuilder {
   protected JimpleBody body;
   private AbstractJimpleBodyBuilder ext = null;
   private AbstractJimpleBodyBuilder base = this;

   public void ext(AbstractJimpleBodyBuilder ext) {
      this.ext = ext;
      if (ext.ext != null) {
         throw new RuntimeException("Extensions created in wrong order.");
      } else {
         ext.base = this.base;
      }
   }

   public AbstractJimpleBodyBuilder ext() {
      return this.ext == null ? this : this.ext;
   }

   public void base(AbstractJimpleBodyBuilder base) {
      this.base = base;
   }

   public AbstractJimpleBodyBuilder base() {
      return this.base;
   }

   protected JimpleBody createJimpleBody(Block block, List formals, SootMethod sootMethod) {
      return this.ext().createJimpleBody(block, formals, sootMethod);
   }

   protected Value createAggressiveExpr(Expr expr, boolean reduceAggressively, boolean reverseCondIfNec) {
      return this.ext().createAggressiveExpr(expr, reduceAggressively, reverseCondIfNec);
   }

   protected void createStmt(Stmt stmt) {
      this.ext().createStmt(stmt);
   }

   protected boolean needsAccessor(Expr expr) {
      return this.ext().needsAccessor(expr);
   }

   protected Local handlePrivateFieldAssignSet(Assign assign) {
      return this.ext().handlePrivateFieldAssignSet(assign);
   }

   protected Local handlePrivateFieldUnarySet(Unary unary) {
      return this.ext().handlePrivateFieldUnarySet(unary);
   }

   protected Value getAssignRightLocal(Assign assign, Local leftLocal) {
      return this.ext().getAssignRightLocal(assign, leftLocal);
   }

   protected Value getSimpleAssignRightLocal(Assign assign) {
      return this.ext().getSimpleAssignRightLocal(assign);
   }

   protected Local handlePrivateFieldSet(Expr expr, Value right, Value base) {
      return this.ext().handlePrivateFieldSet(expr, right, base);
   }

   protected SootMethodRef getSootMethodRef(Call call) {
      return this.ext().getSootMethodRef(call);
   }

   protected Local generateLocal(Type sootType) {
      return this.ext().generateLocal(sootType);
   }

   protected Local generateLocal(polyglot.types.Type polyglotType) {
      return this.ext().generateLocal(polyglotType);
   }

   protected Local getThis(Type sootType) {
      return this.ext().getThis(sootType);
   }

   protected Value getBaseLocal(Receiver receiver) {
      return this.ext().getBaseLocal(receiver);
   }

   protected Value createLHS(Expr expr) {
      return this.ext().createLHS(expr);
   }

   protected FieldRef getFieldRef(Field field) {
      return this.ext().getFieldRef(field);
   }

   protected Constant getConstant(Type sootType, int val) {
      return this.ext().getConstant(sootType, val);
   }
}
