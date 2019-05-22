package soot.javaToJimple;

import java.util.ArrayList;
import java.util.List;
import polyglot.ast.Assign;
import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Unary;
import soot.Local;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.javaToJimple.jj.ast.JjAccessField_c;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;

public class AccessFieldJBB extends AbstractJimpleBodyBuilder {
   protected boolean needsAccessor(Expr expr) {
      return expr instanceof JjAccessField_c ? true : this.ext().needsAccessor(expr);
   }

   protected Local handlePrivateFieldUnarySet(Unary unary) {
      if (!(unary.expr() instanceof JjAccessField_c)) {
         return this.ext().handlePrivateFieldUnarySet(unary);
      } else {
         JjAccessField_c accessField = (JjAccessField_c)unary.expr();
         Local baseLocal = (Local)this.base().getBaseLocal(accessField.field().target());
         Local fieldGetLocal = this.handleCall(accessField.field(), accessField.getMeth(), (Value)null, baseLocal);
         Local tmp = this.base().generateLocal(accessField.field().type());
         AssignStmt stmt1 = Jimple.v().newAssignStmt(tmp, fieldGetLocal);
         this.ext().body.getUnits().add((Unit)stmt1);
         Util.addLnPosTags(stmt1, unary.position());
         Value incVal = this.base().getConstant(Util.getSootType(accessField.field().type()), 1);
         Object binExpr;
         if (unary.operator() != Unary.PRE_INC && unary.operator() != Unary.POST_INC) {
            binExpr = Jimple.v().newSubExpr(tmp, incVal);
         } else {
            binExpr = Jimple.v().newAddExpr(tmp, incVal);
         }

         Local tmp2 = this.generateLocal(accessField.field().type());
         AssignStmt assign = Jimple.v().newAssignStmt(tmp2, (Value)binExpr);
         this.ext().body.getUnits().add((Unit)assign);
         if (unary.operator() != Unary.PRE_INC && unary.operator() != Unary.PRE_DEC) {
            this.base().handlePrivateFieldSet(accessField, tmp2, baseLocal);
            return tmp;
         } else {
            return this.base().handlePrivateFieldSet(accessField, tmp2, baseLocal);
         }
      }
   }

   protected Local handlePrivateFieldAssignSet(Assign assign) {
      if (assign.left() instanceof JjAccessField_c) {
         JjAccessField_c accessField = (JjAccessField_c)assign.left();
         Local baseLocal = (Local)this.base().getBaseLocal(accessField.field().target());
         if (assign.operator() == Assign.ASSIGN) {
            Value right = this.base().getSimpleAssignRightLocal(assign);
            return this.base().handlePrivateFieldSet(accessField, right, baseLocal);
         } else {
            Local leftLocal = this.handleCall(accessField.field(), accessField.getMeth(), (Value)null, baseLocal);
            Value right = this.base().getAssignRightLocal(assign, leftLocal);
            return this.handleFieldSet(accessField, right, baseLocal);
         }
      } else {
         return this.ext().handlePrivateFieldAssignSet(assign);
      }
   }

   private Local handleCall(Field field, Call call, Value param, Local base) {
      Type sootRecType = Util.getSootType(call.target().type());
      SootClass receiverTypeClass = Scene.v().getSootClass("java.lang.Object");
      if (sootRecType instanceof RefType) {
         receiverTypeClass = ((RefType)sootRecType).getSootClass();
      }

      SootMethodRef methToCall = this.base().getSootMethodRef(call);
      List<Value> params = new ArrayList();
      if (param != null) {
         params.add(param);
      }

      Local baseLocal = base;
      if (base == null) {
         baseLocal = (Local)this.ext().getBaseLocal(call.target());
      }

      Object invoke;
      if (methToCall.isStatic()) {
         invoke = Jimple.v().newStaticInvokeExpr(methToCall, (List)params);
      } else if (Modifier.isInterface(receiverTypeClass.getModifiers()) && call.methodInstance().flags().isAbstract()) {
         invoke = Jimple.v().newInterfaceInvokeExpr(baseLocal, methToCall, (List)params);
      } else {
         invoke = Jimple.v().newVirtualInvokeExpr(baseLocal, methToCall, (List)params);
      }

      Local retLocal = this.base().generateLocal(field.type());
      AssignStmt assignStmt = Jimple.v().newAssignStmt(retLocal, (Value)invoke);
      this.ext().body.getUnits().add((Unit)assignStmt);
      return retLocal;
   }

   protected Local handlePrivateFieldSet(Expr expr, Value right, Value baseLocal) {
      if (expr instanceof JjAccessField_c) {
         JjAccessField_c accessField = (JjAccessField_c)expr;
         return this.handleCall(accessField.field(), accessField.setMeth(), right, (Local)null);
      } else {
         return this.ext().handlePrivateFieldSet(expr, right, baseLocal);
      }
   }

   private Local handleFieldSet(JjAccessField_c accessField, Value right, Local base) {
      return this.handleCall(accessField.field(), accessField.setMeth(), right, base);
   }

   protected Value createAggressiveExpr(Expr expr, boolean redAgg, boolean revIfNec) {
      if (expr instanceof JjAccessField_c) {
         JjAccessField_c accessField = (JjAccessField_c)expr;
         return this.handleCall(accessField.field(), accessField.getMeth(), (Value)null, (Local)null);
      } else {
         return this.ext().createAggressiveExpr(expr, redAgg, revIfNec);
      }
   }

   protected Value createLHS(Expr expr) {
      if (expr instanceof JjAccessField_c) {
         JjAccessField_c accessField = (JjAccessField_c)expr;
         return this.handleCall(accessField.field(), accessField.getMeth(), (Value)null, (Local)null);
      } else {
         return this.ext().createLHS(expr);
      }
   }
}
