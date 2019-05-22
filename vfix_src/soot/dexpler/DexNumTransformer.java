package soot.dexpler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.DoubleType;
import soot.FloatType;
import soot.Local;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CmpExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.ReturnStmt;

public class DexNumTransformer extends DexTransformer {
   private boolean usedAsFloatingPoint;
   boolean doBreak = false;

   public static DexNumTransformer v() {
      return new DexNumTransformer();
   }

   protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
      final DexDefUseAnalysis localDefs = new DexDefUseAnalysis(body);
      Iterator var5 = this.getNumCandidates(body).iterator();

      while(true) {
         Set defs;
         Iterator var8;
         Unit u;
         do {
            if (!var5.hasNext()) {
               return;
            }

            Local loc = (Local)var5.next();
            this.usedAsFloatingPoint = false;
            defs = localDefs.collectDefinitionsWithAliases(loc);
            this.doBreak = false;
            var8 = defs.iterator();

            while(var8.hasNext()) {
               u = (Unit)var8.next();
               final Local l = u instanceof DefinitionStmt ? (Local)((DefinitionStmt)u).getLeftOp() : null;
               u.apply(new AbstractStmtSwitch() {
                  public void caseAssignStmt(AssignStmt stmt) {
                     Value r = stmt.getRightOp();
                     if (r instanceof BinopExpr && !(r instanceof CmpExpr)) {
                        DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.examineBinopExpr(stmt);
                        DexNumTransformer.this.doBreak = true;
                     } else if (r instanceof FieldRef) {
                        DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(((FieldRef)r).getFieldRef().type());
                        DexNumTransformer.this.doBreak = true;
                     } else {
                        Type arType;
                        if (r instanceof NewArrayExpr) {
                           NewArrayExpr nae = (NewArrayExpr)r;
                           arType = nae.getType();
                           DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(arType);
                           DexNumTransformer.this.doBreak = true;
                        } else if (r instanceof ArrayRef) {
                           ArrayRef ar = (ArrayRef)r;
                           arType = ar.getType();
                           if (arType instanceof UnknownType) {
                              Type t = DexNumTransformer.this.findArrayType(localDefs, stmt, 0, Collections.emptySet());
                              DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(t);
                           } else {
                              DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(ar.getType());
                           }

                           DexNumTransformer.this.doBreak = true;
                        } else if (r instanceof CastExpr) {
                           DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(((CastExpr)r).getCastType());
                           DexNumTransformer.this.doBreak = true;
                        } else if (r instanceof InvokeExpr) {
                           DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(((InvokeExpr)r).getType());
                           DexNumTransformer.this.doBreak = true;
                        } else if (r instanceof LengthExpr) {
                           DexNumTransformer.this.usedAsFloatingPoint = false;
                           DexNumTransformer.this.doBreak = true;
                        }
                     }

                  }

                  public void caseIdentityStmt(IdentityStmt stmt) {
                     if (stmt.getLeftOp() == l) {
                        DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(stmt.getRightOp().getType());
                        DexNumTransformer.this.doBreak = true;
                     }

                  }
               });
               if (this.doBreak) {
                  break;
               }

               Iterator var11 = localDefs.getUsesOf(l).iterator();

               while(var11.hasNext()) {
                  Unit use = (Unit)var11.next();
                  use.apply(new AbstractStmtSwitch() {
                     private boolean examineInvokeExpr(InvokeExpr e) {
                        List<Value> args = e.getArgs();
                        List<Type> argTypes = e.getMethodRef().parameterTypes();

                        assert args.size() == argTypes.size();

                        for(int i = 0; i < args.size(); ++i) {
                           if (args.get(i) == l && DexNumTransformer.this.isFloatingPointLike((Type)argTypes.get(i))) {
                              return true;
                           }
                        }

                        return false;
                     }

                     public void caseInvokeStmt(InvokeStmt stmt) {
                        InvokeExpr e = stmt.getInvokeExpr();
                        DexNumTransformer.this.usedAsFloatingPoint = this.examineInvokeExpr(e);
                     }

                     public void caseAssignStmt(AssignStmt stmt) {
                        Value left = stmt.getLeftOp();
                        if (left instanceof ArrayRef) {
                           ArrayRef ar = (ArrayRef)left;
                           if (ar.getIndex() == l) {
                              DexNumTransformer.this.doBreak = true;
                              return;
                           }
                        }

                        Value r = stmt.getRightOp();
                        if (r instanceof ArrayRef) {
                           if (((ArrayRef)r).getIndex() == l) {
                              DexNumTransformer.this.doBreak = true;
                              return;
                           }
                        } else {
                           if (r instanceof InvokeExpr) {
                              DexNumTransformer.this.usedAsFloatingPoint = this.examineInvokeExpr((InvokeExpr)r);
                              DexNumTransformer.this.doBreak = true;
                              return;
                           }

                           if (r instanceof BinopExpr) {
                              DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.examineBinopExpr(stmt);
                              DexNumTransformer.this.doBreak = true;
                              return;
                           }

                           if (r instanceof CastExpr) {
                              DexNumTransformer.this.usedAsFloatingPoint = stmt.hasTag("FloatOpTag") || stmt.hasTag("DoubleOpTag");
                              DexNumTransformer.this.doBreak = true;
                              return;
                           }

                           if (r instanceof Local && r == l) {
                              if (left instanceof FieldRef) {
                                 FieldRef fr = (FieldRef)left;
                                 if (DexNumTransformer.this.isFloatingPointLike(fr.getType())) {
                                    DexNumTransformer.this.usedAsFloatingPoint = true;
                                 }

                                 DexNumTransformer.this.doBreak = true;
                                 return;
                              }

                              if (left instanceof ArrayRef) {
                                 ArrayRef arx = (ArrayRef)left;
                                 Type arType = arx.getType();
                                 if (arType instanceof UnknownType) {
                                    arType = DexNumTransformer.this.findArrayType(localDefs, stmt, 0, Collections.emptySet());
                                 }

                                 DexNumTransformer.this.usedAsFloatingPoint = DexNumTransformer.this.isFloatingPointLike(arType);
                                 DexNumTransformer.this.doBreak = true;
                                 return;
                              }
                           }
                        }

                     }

                     public void caseReturnStmt(ReturnStmt stmt) {
                        DexNumTransformer.this.usedAsFloatingPoint = stmt.getOp() == l && DexNumTransformer.this.isFloatingPointLike(body.getMethod().getReturnType());
                        DexNumTransformer.this.doBreak = true;
                     }
                  });
                  if (this.doBreak) {
                     break;
                  }
               }

               if (this.doBreak) {
                  break;
               }
            }
         } while(!this.usedAsFloatingPoint);

         var8 = defs.iterator();

         while(var8.hasNext()) {
            u = (Unit)var8.next();
            this.replaceWithFloatingPoint(u);
         }
      }
   }

   protected boolean examineBinopExpr(Unit u) {
      return u.hasTag("FloatOpTag") || u.hasTag("DoubleOpTag");
   }

   private boolean isFloatingPointLike(Type t) {
      return t instanceof FloatType || t instanceof DoubleType;
   }

   private Set<Local> getNumCandidates(Body body) {
      Set<Local> candidates = new HashSet();
      Iterator var3 = body.getUnits().iterator();

      while(true) {
         Local l;
         Value r;
         do {
            AssignStmt a;
            do {
               Unit u;
               do {
                  if (!var3.hasNext()) {
                     return candidates;
                  }

                  u = (Unit)var3.next();
               } while(!(u instanceof AssignStmt));

               a = (AssignStmt)u;
            } while(!(a.getLeftOp() instanceof Local));

            l = (Local)a.getLeftOp();
            r = a.getRightOp();
         } while(!(r instanceof IntConstant) && !(r instanceof LongConstant));

         candidates.add(l);
      }
   }

   private void replaceWithFloatingPoint(Unit u) {
      if (u instanceof AssignStmt) {
         AssignStmt s = (AssignStmt)u;
         Value v = s.getRightOp();
         if (v instanceof IntConstant) {
            int vVal = ((IntConstant)v).value;
            s.setRightOp(FloatConstant.v(Float.intBitsToFloat(vVal)));
         } else if (v instanceof LongConstant) {
            long vVal = ((LongConstant)v).value;
            s.setRightOp(DoubleConstant.v(Double.longBitsToDouble(vVal)));
         }
      }

   }
}
