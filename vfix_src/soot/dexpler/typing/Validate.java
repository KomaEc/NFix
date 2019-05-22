package soot.dexpler.typing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethodRef;
import soot.SootResolver;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
import soot.jimple.StringConstant;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.UnusedLocalEliminator;

public class Validate {
   public static void validateArrays(Body b) {
      Set<DefinitionStmt> definitions = new HashSet();
      Set<Unit> unitWithArrayRef = new HashSet();
      Iterator var3 = b.getUnits().iterator();

      Iterator var6;
      while(var3.hasNext()) {
         Unit u = (Unit)var3.next();
         if (u instanceof DefinitionStmt) {
            DefinitionStmt s = (DefinitionStmt)u;
            definitions.add(s);
         }

         List<ValueBox> uses = u.getUseBoxes();
         var6 = uses.iterator();

         while(var6.hasNext()) {
            ValueBox vb = (ValueBox)var6.next();
            Value v = vb.getValue();
            if (v instanceof ArrayRef) {
               unitWithArrayRef.add(u);
            }
         }
      }

      LocalDefs localDefs = LocalDefs.Factory.newLocalDefs(b, true);
      Set<Unit> toReplace = new HashSet();
      Iterator var26 = unitWithArrayRef.iterator();

      while(var26.hasNext()) {
         Unit u = (Unit)var26.next();
         boolean ok = false;
         List<ValueBox> uses = u.getUseBoxes();
         Iterator var9 = uses.iterator();

         label138:
         do {
            Value v;
            do {
               if (!var9.hasNext()) {
                  break label138;
               }

               ValueBox vb = (ValueBox)var9.next();
               v = vb.getValue();
            } while(!(v instanceof ArrayRef));

            ArrayRef ar = (ArrayRef)v;
            Local base = (Local)ar.getBase();
            List<Unit> defs = localDefs.getDefsOfAt(base, u);
            HashSet alreadyHandled = new HashSet();

            boolean isMore;
            Unit d;
            AssignStmt assStmt;
            do {
               isMore = false;
               Iterator var17 = defs.iterator();

               while(var17.hasNext()) {
                  d = (Unit)var17.next();
                  if (!alreadyHandled.contains(d) && d instanceof AssignStmt) {
                     assStmt = (AssignStmt)d;
                     Value r = assStmt.getRightOp();
                     if (r instanceof Local) {
                        defs.addAll(localDefs.getDefsOfAt((Local)r, d));
                        alreadyHandled.add(d);
                        isMore = true;
                        break;
                     }

                     if (r instanceof ArrayRef) {
                        ArrayRef arrayRef = (ArrayRef)r;
                        Local l = (Local)arrayRef.getBase();
                        defs.addAll(localDefs.getDefsOfAt(l, d));
                        alreadyHandled.add(d);
                        isMore = true;
                        break;
                     }
                  }
               }
            } while(isMore);

            Iterator var40 = defs.iterator();

            while(var40.hasNext()) {
               Unit def = (Unit)var40.next();
               d = null;
               Value r;
               if (def instanceof IdentityStmt) {
                  IdentityStmt idstmt = (IdentityStmt)def;
                  r = idstmt.getRightOp();
               } else {
                  if (!(def instanceof AssignStmt)) {
                     throw new RuntimeException("error: definition statement not an IdentityStmt nor an AssignStmt! " + def);
                  }

                  assStmt = (AssignStmt)def;
                  r = assStmt.getRightOp();
               }

               assStmt = null;
               Type t;
               if (r instanceof InvokeExpr) {
                  InvokeExpr ie = (InvokeExpr)r;
                  t = ie.getType();
                  if (t instanceof ArrayType) {
                     ok = true;
                  }
               } else if (r instanceof FieldRef) {
                  FieldRef ref = (FieldRef)r;
                  t = ref.getType();
                  if (t instanceof ArrayType) {
                     ok = true;
                  }
               } else if (r instanceof IdentityRef) {
                  IdentityRef ir = (IdentityRef)r;
                  t = ir.getType();
                  if (t instanceof ArrayType) {
                     ok = true;
                  }
               } else if (r instanceof CastExpr) {
                  CastExpr c = (CastExpr)r;
                  t = c.getType();
                  if (t instanceof ArrayType) {
                     ok = true;
                  }
               } else if (!(r instanceof ArrayRef)) {
                  if (r instanceof NewArrayExpr) {
                     ok = true;
                  } else if (!(r instanceof Local) && !(r instanceof Constant)) {
                     throw new RuntimeException("error: unknown right hand side of definition stmt " + def);
                  }
               }

               if (ok) {
                  break;
               }
            }
         } while(!ok);

         if (!ok) {
            toReplace.add(u);
         }
      }

      int i = 0;
      var6 = toReplace.iterator();

      while(var6.hasNext()) {
         Unit u = (Unit)var6.next();
         System.out.println("warning: incorrect array def, replacing unit " + u);
         RefType throwableType = RefType.v("java.lang.Throwable");
         Jimple var10000 = Jimple.v();
         StringBuilder var10001 = (new StringBuilder()).append("ttt_");
         ++i;
         Local ttt = var10000.newLocal(var10001.append(i).toString(), throwableType);
         b.getLocals().add(ttt);
         Value r = Jimple.v().newNewExpr(throwableType);
         Unit initLocalUnit = Jimple.v().newAssignStmt(ttt, r);
         List<String> pTypes = new ArrayList();
         pTypes.add("java.lang.String");
         boolean isStatic = false;
         SootMethodRef mRef = makeMethodRef("java.lang.Throwable", "<init>", "", pTypes, isStatic);
         List<Value> parameters = new ArrayList();
         parameters.add(StringConstant.v("Soot updated this instruction"));
         InvokeExpr ie = Jimple.v().newSpecialInvokeExpr(ttt, mRef, (List)parameters);
         Unit initMethod = Jimple.v().newInvokeStmt(ie);
         Unit newUnit = Jimple.v().newThrowStmt(ttt);
         b.getUnits().swapWith((Unit)u, (Unit)newUnit);
         b.getUnits().insertBefore((Unit)initMethod, (Unit)newUnit);
         b.getUnits().insertBefore((Unit)initLocalUnit, (Unit)initMethod);
      }

      DeadAssignmentEliminator.v().transform(b);
      UnusedLocalEliminator.v().transform(b);
      NopEliminator.v().transform(b);
      UnreachableCodeEliminator.v().transform(b);
   }

   public static SootMethodRef makeMethodRef(String cName, String mName, String rType, List<String> pTypes, boolean isStatic) {
      SootClass sc = SootResolver.v().makeClassRef(cName);
      Type returnType = null;
      if (rType == "") {
         returnType = VoidType.v();
      } else {
         returnType = RefType.v(rType);
      }

      List<Type> parameterTypes = new ArrayList();
      Iterator var8 = pTypes.iterator();

      while(var8.hasNext()) {
         String p = (String)var8.next();
         parameterTypes.add(RefType.v(p));
      }

      return Scene.v().makeMethodRef(sc, mName, parameterTypes, (Type)returnType, isStatic);
   }
}
