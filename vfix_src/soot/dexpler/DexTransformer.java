package soot.dexpler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.NullType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.Stmt;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;

public abstract class DexTransformer extends BodyTransformer {
   protected List<Unit> collectDefinitionsWithAliases(Local l, LocalDefs localDefs, LocalUses localUses, Body body) {
      Set<Local> seenLocals = new HashSet();
      List<Local> newLocals = new ArrayList();
      List<Unit> defs = new ArrayList();
      newLocals.add(l);
      seenLocals.add(l);

      while(!newLocals.isEmpty()) {
         Local local = (Local)newLocals.remove(0);
         Iterator var9 = this.collectDefinitions(local, localDefs).iterator();

         while(var9.hasNext()) {
            Unit u = (Unit)var9.next();
            if (u instanceof AssignStmt) {
               Value r = ((AssignStmt)u).getRightOp();
               if (r instanceof Local && seenLocals.add((Local)r)) {
                  newLocals.add((Local)r);
               }
            }

            defs.add(u);
            List<UnitValueBoxPair> usesOf = localUses.getUsesOf(u);
            Iterator var12 = usesOf.iterator();

            while(var12.hasNext()) {
               UnitValueBoxPair pair = (UnitValueBoxPair)var12.next();
               Unit unit = pair.getUnit();
               if (unit instanceof AssignStmt) {
                  AssignStmt assignStmt = (AssignStmt)unit;
                  Value right = assignStmt.getRightOp();
                  Value left = assignStmt.getLeftOp();
                  if (right == local && left instanceof Local && seenLocals.add((Local)left)) {
                     newLocals.add((Local)left);
                  }
               }
            }
         }
      }

      return defs;
   }

   private List<Unit> collectDefinitions(Local l, LocalDefs localDefs) {
      return localDefs.getDefsOf(l);
   }

   protected Type findArrayType(LocalDefs localDefs, Stmt arrayStmt, int depth, Set<Unit> alreadyVisitedDefs) {
      ArrayRef aRef = null;
      if (arrayStmt.containsArrayRef()) {
         aRef = arrayStmt.getArrayRef();
      }

      Local aBase = null;
      if (null == aRef) {
         if (!(arrayStmt instanceof AssignStmt)) {
            throw new RuntimeException("ERROR: not an assign statement: " + arrayStmt);
         }

         AssignStmt stmt = (AssignStmt)arrayStmt;
         aBase = (Local)stmt.getRightOp();
      } else {
         aBase = (Local)aRef.getBase();
      }

      List<Unit> defsOfaBaseList = localDefs.getDefsOfAt(aBase, arrayStmt);
      if (defsOfaBaseList != null && !defsOfaBaseList.isEmpty()) {
         Type aType = null;
         int nullDefCount = 0;
         Iterator var10 = defsOfaBaseList.iterator();

         while(var10.hasNext()) {
            Unit baseDef = (Unit)var10.next();
            if (!alreadyVisitedDefs.contains(baseDef)) {
               Set<Unit> newVisitedDefs = new HashSet(alreadyVisitedDefs);
               newVisitedDefs.add(baseDef);
               Type t;
               if (!(baseDef instanceof AssignStmt)) {
                  if (!(baseDef instanceof IdentityStmt)) {
                     throw new RuntimeException("ERROR: base local def must be AssignStmt or IdentityStmt! " + baseDef);
                  }

                  IdentityStmt stmt = (IdentityStmt)baseDef;
                  ArrayType at = (ArrayType)stmt.getRightOp().getType();
                  t = at.getArrayElementType();
                  if (depth != 0) {
                     return t;
                  }

                  aType = t;
                  break;
               }

               AssignStmt stmt = (AssignStmt)baseDef;
               Value r = stmt.getRightOp();
               ArrayType at;
               if (r instanceof FieldRef) {
                  t = ((FieldRef)r).getFieldRef().type();
                  if (t instanceof ArrayType) {
                     at = (ArrayType)t;
                     t = at.getArrayElementType();
                  }

                  if (depth != 0) {
                     return t;
                  }

                  aType = t;
                  break;
               }

               Type t;
               if (r instanceof ArrayRef) {
                  ArrayRef ar = (ArrayRef)r;
                  if (!ar.getType().toString().equals(".unknown") && !ar.getType().toString().equals("unknown")) {
                     at = (ArrayType)stmt.getRightOp().getType();
                     Type t = at.getArrayElementType();
                     if (depth != 0) {
                        return t;
                     }

                     aType = t;
                  } else {
                     ++depth;
                     t = this.findArrayType(localDefs, stmt, depth, newVisitedDefs);
                     if (t instanceof ArrayType) {
                        ArrayType at = (ArrayType)t;
                        t = at.getArrayElementType();
                     }

                     if (depth != 0) {
                        return t;
                     }

                     aType = t;
                  }
                  break;
               }

               if (r instanceof NewArrayExpr) {
                  NewArrayExpr expr = (NewArrayExpr)r;
                  t = expr.getBaseType();
                  if (depth != 0) {
                     return t;
                  }

                  aType = t;
                  break;
               }

               if (r instanceof CastExpr) {
                  t = ((CastExpr)r).getCastType();
                  if (t instanceof ArrayType) {
                     at = (ArrayType)t;
                     t = at.getArrayElementType();
                  }

                  if (depth != 0) {
                     return t;
                  }

                  aType = t;
                  break;
               }

               if (r instanceof InvokeExpr) {
                  t = ((InvokeExpr)r).getMethodRef().returnType();
                  if (t instanceof ArrayType) {
                     at = (ArrayType)t;
                     t = at.getArrayElementType();
                  }

                  if (depth != 0) {
                     return t;
                  }

                  aType = t;
                  break;
               }

               if (r instanceof Local) {
                  ++depth;
                  t = this.findArrayType(localDefs, stmt, depth, newVisitedDefs);
                  if (depth == 0) {
                     aType = t;
                  } else {
                     aType = t;
                  }
               } else {
                  if (!(r instanceof Constant)) {
                     throw new RuntimeException("ERROR: def statement not possible! " + stmt);
                  }

                  ++nullDefCount;
               }

               if (aType != null) {
                  break;
               }
            }
         }

         if (depth == 0 && aType == null) {
            if (nullDefCount == defsOfaBaseList.size()) {
               return NullType.v();
            } else {
               throw new RuntimeException("ERROR: could not find type of array from statement '" + arrayStmt + "'");
            }
         } else {
            return aType;
         }
      } else {
         throw new RuntimeException("ERROR: no def statement found for array base local " + arrayStmt);
      }
   }
}
