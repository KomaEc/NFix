package soot.jimple.toolkits.typing.fast;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.ShortType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NegExpr;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.typing.Util;
import soot.toolkits.scalar.LocalDefs;

public class TypeResolver {
   private JimpleBody jb;
   private final List<DefinitionStmt> assignments;
   private final HashMap<Local, BitSet> depends;

   public TypeResolver(JimpleBody jb) {
      this.jb = jb;
      this.assignments = new ArrayList();
      this.depends = new HashMap(jb.getLocalCount());
      Iterator var2 = this.jb.getLocals().iterator();

      while(var2.hasNext()) {
         Local v = (Local)var2.next();
         this.addLocal(v);
      }

      this.initAssignments();
   }

   private void initAssignments() {
      Iterator var1 = this.jb.getUnits().iterator();

      while(var1.hasNext()) {
         Unit stmt = (Unit)var1.next();
         if (stmt instanceof DefinitionStmt) {
            this.initAssignment((DefinitionStmt)stmt);
         }
      }

   }

   private void initAssignment(DefinitionStmt ds) {
      Value lhs = ds.getLeftOp();
      Value rhs = ds.getRightOp();
      if (lhs instanceof Local || lhs instanceof ArrayRef) {
         int assignmentIdx = this.assignments.size();
         this.assignments.add(ds);
         if (rhs instanceof Local) {
            this.addDepend((Local)rhs, assignmentIdx);
         } else if (rhs instanceof BinopExpr) {
            BinopExpr be = (BinopExpr)rhs;
            Value lop = be.getOp1();
            Value rop = be.getOp2();
            if (lop instanceof Local) {
               this.addDepend((Local)lop, assignmentIdx);
            }

            if (rop instanceof Local) {
               this.addDepend((Local)rop, assignmentIdx);
            }
         } else {
            Value op;
            if (rhs instanceof NegExpr) {
               op = ((NegExpr)rhs).getOp();
               if (op instanceof Local) {
                  this.addDepend((Local)op, assignmentIdx);
               }
            } else if (rhs instanceof CastExpr) {
               op = ((CastExpr)rhs).getOp();
               if (op instanceof Local) {
                  this.addDepend((Local)op, assignmentIdx);
               }
            } else if (rhs instanceof ArrayRef) {
               this.addDepend((Local)((ArrayRef)rhs).getBase(), assignmentIdx);
            }
         }
      }

   }

   private void addLocal(Local v) {
      this.depends.put(v, new BitSet());
   }

   private void addDepend(Local v, int stmtIndex) {
      ((BitSet)this.depends.get(v)).set(stmtIndex);
   }

   public void inferTypes() {
      AugEvalFunction ef = new AugEvalFunction(this.jb);
      BytecodeHierarchy bh = new BytecodeHierarchy();
      Collection<Typing> sigma = this.applyAssignmentConstraints(new Typing(this.jb.getLocals()), ef, bh);
      if (!sigma.isEmpty()) {
         int[] castCount = new int[1];
         Typing tg = this.minCasts(sigma, bh, castCount);
         if (castCount[0] != 0) {
            this.split_new();
            sigma = this.applyAssignmentConstraints(new Typing(this.jb.getLocals()), ef, bh);
            tg = this.minCasts(sigma, bh, castCount);
         }

         this.insertCasts(tg, bh, false);
         IntType inttype = IntType.v();
         BottomType bottom = BottomType.v();

         Iterator var8;
         Local v;
         Object t;
         for(var8 = this.jb.getLocals().iterator(); var8.hasNext(); v.setType((Type)t)) {
            v = (Local)var8.next();
            t = tg.get(v);
            if (t instanceof IntegerType) {
               t = inttype;
               tg.set(v, bottom);
            }
         }

         tg = this.typePromotion(tg);
         if (tg == null) {
            soot.jimple.toolkits.typing.integer.TypeResolver.resolve(this.jb);
         } else {
            var8 = this.jb.getLocals().iterator();

            while(var8.hasNext()) {
               v = (Local)var8.next();
               v.setType(tg.get(v));
            }
         }

      }
   }

   private Typing typePromotion(Typing tg) {
      BooleanType booleanType = BooleanType.v();
      ByteType byteType = ByteType.v();
      ShortType shortType = ShortType.v();

      boolean conversionDone;
      do {
         AugEvalFunction ef = new AugEvalFunction(this.jb);
         AugHierarchy h = new AugHierarchy();
         UseChecker uc = new UseChecker(this.jb);
         TypeResolver.TypePromotionUseVisitor uv = new TypeResolver.TypePromotionUseVisitor(this.jb, tg);

         do {
            Collection<Typing> sigma = this.applyAssignmentConstraints(tg, ef, h);
            if (sigma.isEmpty()) {
               return null;
            }

            tg = (Typing)sigma.iterator().next();
            uv.typingChanged = false;
            uc.check(tg, uv);
            if (uv.fail) {
               return null;
            }
         } while(uv.typingChanged);

         conversionDone = false;
         Iterator var13 = this.jb.getLocals().iterator();

         while(var13.hasNext()) {
            Local v = (Local)var13.next();
            Type t = tg.get(v);
            if (t instanceof Integer1Type) {
               tg.set(v, booleanType);
               conversionDone = true;
            } else if (t instanceof Integer127Type) {
               tg.set(v, byteType);
               conversionDone = true;
            } else if (t instanceof Integer32767Type) {
               tg.set(v, shortType);
               conversionDone = true;
            }
         }
      } while(conversionDone);

      return tg;
   }

   private int insertCasts(Typing tg, IHierarchy h, boolean countOnly) {
      UseChecker uc = new UseChecker(this.jb);
      TypeResolver.CastInsertionUseVisitor uv = new TypeResolver.CastInsertionUseVisitor(countOnly, this.jb, tg, h);
      uc.check(tg, uv);
      return uv.getCount();
   }

   private Typing minCasts(Collection<Typing> sigma, IHierarchy h, int[] count) {
      Typing r = null;
      count[0] = -1;
      boolean setR = false;
      Iterator var6 = sigma.iterator();

      while(true) {
         Typing tg;
         int n;
         do {
            if (!var6.hasNext()) {
               if (setR) {
                  return r;
               }

               return null;
            }

            tg = (Typing)var6.next();
            n = this.insertCasts(tg, h, true);
         } while(count[0] != -1 && n >= count[0]);

         count[0] = n;
         r = tg;
         setR = true;
      }
   }

   private Collection<Typing> applyAssignmentConstraints(Typing tg, IEvalFunction ef, IHierarchy h) {
      int numAssignments = this.assignments.size();
      LinkedList<Typing> sigma = new LinkedList();
      LinkedList<Typing> r = new LinkedList();
      if (numAssignments == 0) {
         return sigma;
      } else {
         HashMap<Typing, BitSet> worklists = new HashMap();
         sigma.add(tg);
         BitSet wl = new BitSet(numAssignments - 1);
         wl.set(0, numAssignments);
         worklists.put(tg, wl);

         while(true) {
            label82:
            while(!sigma.isEmpty()) {
               tg = (Typing)sigma.element();
               wl = (BitSet)worklists.get(tg);
               if (wl.isEmpty()) {
                  r.add(tg);
                  sigma.remove();
                  worklists.remove(tg);
               } else {
                  int defIdx = wl.nextSetBit(0);
                  wl.clear(defIdx);
                  DefinitionStmt stmt = (DefinitionStmt)this.assignments.get(defIdx);
                  Value lhs = stmt.getLeftOp();
                  Value rhs = stmt.getRightOp();
                  Local v;
                  if (lhs instanceof Local) {
                     v = (Local)lhs;
                  } else {
                     v = (Local)((ArrayRef)lhs).getBase();
                  }

                  Type told = tg.get(v);
                  Collection<Type> eval = new ArrayList(ef.eval(tg, rhs, stmt));
                  boolean isFirstType = true;
                  Iterator var17 = eval.iterator();

                  while(true) {
                     Object t_;
                     while(true) {
                        if (!var17.hasNext()) {
                           continue label82;
                        }

                        t_ = (Type)var17.next();
                        if (!(lhs instanceof ArrayRef)) {
                           break;
                        }

                        if (t_ instanceof RefType || t_ instanceof ArrayType) {
                           t_ = ((Type)t_).makeArrayType();
                           break;
                        }
                     }

                     Object lcas;
                     if (!typesEqual(told, (Type)t_) && told instanceof RefType && t_ instanceof RefType && (((RefType)told).getSootClass().isPhantom() || ((RefType)t_).getSootClass().isPhantom()) && stmt.getRightOp() instanceof CaughtExceptionRef) {
                        lcas = Collections.singleton(RefType.v("java.lang.Throwable"));
                     } else {
                        lcas = h.lcas(told, (Type)t_);
                     }

                     for(Iterator var20 = ((Collection)lcas).iterator(); var20.hasNext(); isFirstType = false) {
                        Type t = (Type)var20.next();
                        if (!typesEqual(t, told)) {
                           Typing tg_;
                           BitSet wl_;
                           if (isFirstType) {
                              tg_ = tg;
                              wl_ = wl;
                           } else {
                              tg_ = new Typing(tg);
                              wl_ = new BitSet(numAssignments - 1);
                              wl_.or(wl);
                              sigma.add(tg_);
                              worklists.put(tg_, wl_);
                           }

                           tg_.set(v, t);
                           BitSet dependsV = (BitSet)this.depends.get(v);
                           if (dependsV != null) {
                              wl_.or(dependsV);
                           }
                        }
                     }
                  }
               }
            }

            Typing.minimize(r, h);
            return r;
         }
      }
   }

   public static boolean typesEqual(Type a, Type b) {
      if (a instanceof ArrayType && b instanceof ArrayType) {
         ArrayType a_ = (ArrayType)a;
         ArrayType b_ = (ArrayType)b;
         return a_.numDimensions == b_.numDimensions && a_.baseType.equals(b_.baseType);
      } else {
         return a.equals(b);
      }
   }

   private void split_new() {
      LocalDefs defs = LocalDefs.Factory.newLocalDefs((Body)this.jb);
      PatchingChain<Unit> units = this.jb.getUnits();
      Stmt[] stmts = new Stmt[units.size()];
      units.toArray(stmts);
      Jimple jimple = Jimple.v();
      Stmt[] var5 = stmts;
      int var6 = stmts.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Stmt stmt = var5[var7];
         if (stmt instanceof InvokeStmt) {
            InvokeStmt invoke = (InvokeStmt)stmt;
            if (invoke.getInvokeExpr() instanceof SpecialInvokeExpr) {
               SpecialInvokeExpr special = (SpecialInvokeExpr)invoke.getInvokeExpr();
               AssignStmt assign;
               if (special.getMethodRef().name().equals("<init>")) {
                  for(List deflist = defs.getDefsOfAt((Local)special.getBase(), invoke); deflist.size() == 1; deflist = defs.getDefsOfAt((Local)assign.getRightOp(), assign)) {
                     Stmt stmt2 = (Stmt)deflist.get(0);
                     if (!(stmt2 instanceof AssignStmt)) {
                        break;
                     }

                     assign = (AssignStmt)stmt2;
                     if (!(assign.getRightOp() instanceof Local)) {
                        if (assign.getRightOp() instanceof NewExpr) {
                           Local newlocal = jimple.newLocal("tmp", (Type)null);
                           newlocal.setName("tmp$" + System.identityHashCode(newlocal));
                           this.jb.getLocals().add(newlocal);
                           special.setBase(newlocal);
                           DefinitionStmt assignStmt = jimple.newAssignStmt(assign.getLeftOp(), newlocal);
                           Unit u = Util.findLastIdentityUnit(this.jb, assign);
                           units.insertAfter((Unit)assignStmt, (Unit)u);
                           assign.setLeftOp(newlocal);
                           this.addLocal(newlocal);
                           this.initAssignment(assignStmt);
                        }
                        break;
                     }
                  }
               }
            }
         }
      }

   }

   private class TypePromotionUseVisitor implements IUseVisitor {
      private JimpleBody jb;
      private Typing tg;
      public boolean fail;
      public boolean typingChanged;
      private final ByteType byteType = ByteType.v();
      private final Integer32767Type integer32767Type = Integer32767Type.v();
      private final Integer127Type integer127Type = Integer127Type.v();

      public TypePromotionUseVisitor(JimpleBody jb, Typing tg) {
         this.jb = jb;
         this.tg = tg;
         this.fail = false;
         this.typingChanged = false;
      }

      private Type promote(Type tlow, Type thigh) {
         if (tlow instanceof Integer1Type) {
            if (thigh instanceof IntType) {
               return Integer127Type.v();
            } else if (thigh instanceof ShortType) {
               return this.byteType;
            } else if (!(thigh instanceof BooleanType) && !(thigh instanceof ByteType) && !(thigh instanceof CharType) && !(thigh instanceof Integer127Type) && !(thigh instanceof Integer32767Type)) {
               throw new RuntimeException();
            } else {
               return thigh;
            }
         } else if (tlow instanceof Integer127Type) {
            if (thigh instanceof ShortType) {
               return this.byteType;
            } else if (thigh instanceof IntType) {
               return this.integer127Type;
            } else if (!(thigh instanceof ByteType) && !(thigh instanceof CharType) && !(thigh instanceof Integer32767Type)) {
               throw new RuntimeException();
            } else {
               return thigh;
            }
         } else if (tlow instanceof Integer32767Type) {
            if (thigh instanceof IntType) {
               return this.integer32767Type;
            } else if (!(thigh instanceof ShortType) && !(thigh instanceof CharType)) {
               throw new RuntimeException();
            } else {
               return thigh;
            }
         } else {
            throw new RuntimeException();
         }
      }

      public Value visit(Value op, Type useType, Stmt stmt) {
         if (this.finish()) {
            return op;
         } else {
            Type t = AugEvalFunction.eval_(this.tg, op, stmt, this.jb);
            if (!AugHierarchy.ancestor_(useType, t)) {
               this.fail = true;
            } else if (op instanceof Local && (t instanceof Integer1Type || t instanceof Integer127Type || t instanceof Integer32767Type)) {
               Local v = (Local)op;
               if (!TypeResolver.typesEqual(t, useType)) {
                  Type t_ = this.promote(t, useType);
                  if (!TypeResolver.typesEqual(t, t_)) {
                     this.tg.set(v, t_);
                     this.typingChanged = true;
                  }
               }
            }

            return op;
         }
      }

      public boolean finish() {
         return this.typingChanged || this.fail;
      }
   }

   private class CastInsertionUseVisitor implements IUseVisitor {
      private JimpleBody jb;
      private Typing tg;
      private IHierarchy h;
      private boolean countOnly;
      private int count;

      public CastInsertionUseVisitor(boolean countOnly, JimpleBody jb, Typing tg, IHierarchy h) {
         this.jb = jb;
         this.tg = tg;
         this.h = h;
         this.countOnly = countOnly;
         this.count = 0;
      }

      public Value visit(Value op, Type useType, Stmt stmt) {
         Jimple jimple = Jimple.v();
         Type t = AugEvalFunction.eval_(this.tg, op, stmt, this.jb);
         if (this.h.ancestor(useType, t)) {
            return op;
         } else {
            ++this.count;
            if (this.countOnly) {
               return op;
            } else {
               if (stmt.containsArrayRef() && stmt.getArrayRef().getBase() == op && stmt instanceof DefinitionStmt) {
                  Type baseType = this.tg.get((Local)stmt.getArrayRef().getBase());
                  DefinitionStmt defStmt = (DefinitionStmt)stmt;
                  if (baseType instanceof RefType && defStmt.getLeftOp() instanceof Local) {
                     RefType rt = (RefType)baseType;
                     String name = rt.getSootClass().getName();
                     if (name.equals("java.lang.Object") || name.equals("java.io.Serializable") || name.equals("java.lang.Cloneable")) {
                        this.tg.set((Local)((DefinitionStmt)stmt).getLeftOp(), ((ArrayType)useType).getElementType());
                     }
                  }
               }

               Local vold;
               if (!(op instanceof Local)) {
                  vold = jimple.newLocal("tmp", t);
                  vold.setName("tmp$" + System.identityHashCode(vold));
                  this.tg.set(vold, t);
                  this.jb.getLocals().add(vold);
                  Unit u = Util.findFirstNonIdentityUnit(this.jb, stmt);
                  this.jb.getUnits().insertBefore((Unit)jimple.newAssignStmt(vold, op), (Unit)u);
               } else {
                  vold = (Local)op;
               }

               Local vnew = jimple.newLocal("tmp", useType);
               vnew.setName("tmp$" + System.identityHashCode(vnew));
               this.tg.set(vnew, useType);
               this.jb.getLocals().add(vnew);
               Unit ux = Util.findFirstNonIdentityUnit(this.jb, stmt);
               this.jb.getUnits().insertBefore((Unit)jimple.newAssignStmt(vnew, jimple.newCastExpr(vold, useType)), (Unit)ux);
               return vnew;
            }
         }
      }

      public int getCount() {
         return this.count;
      }

      public boolean finish() {
         return false;
      }
   }
}
