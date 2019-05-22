package soot.jimple.toolkits.annotation.arraycheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.IntType;
import soot.Local;
import soot.SootField;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AddExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.LengthExpr;
import soot.jimple.MulExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
import soot.jimple.internal.JAddExpr;
import soot.jimple.internal.JSubExpr;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;

class ArrayIndexLivenessAnalysis extends BackwardFlowAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(ArrayIndexLivenessAnalysis.class);
   HashSet<Local> fullSet = new HashSet();
   ExceptionalUnitGraph eug;
   HashMap<Stmt, HashSet<Object>> genOfUnit;
   HashMap<Stmt, HashSet<Value>> absGenOfUnit;
   HashMap<Stmt, HashSet<Value>> killOfUnit;
   HashMap<Stmt, HashSet<Value>> conditionOfGen;
   HashMap<DefinitionStmt, Value> killArrayRelated;
   HashMap<DefinitionStmt, Boolean> killAllArrayRef;
   IntContainer zero = new IntContainer(0);
   private final boolean fieldin;
   HashMap<Object, HashSet<Value>> localToFieldRef;
   HashMap<Object, HashSet<Value>> fieldToFieldRef;
   HashSet<Value> allFieldRefs;
   private final boolean arrayin;
   HashMap localToArrayRef;
   HashSet allArrayRefs;
   private final boolean csin;
   HashMap<Value, HashSet<Value>> localToExpr;
   private final boolean rectarray;
   HashSet<Local> multiarraylocals;

   public ArrayIndexLivenessAnalysis(DirectedGraph dg, boolean takeFieldRef, boolean takeArrayRef, boolean takeCSE, boolean takeRectArray) {
      super(dg);
      this.fieldin = takeFieldRef;
      this.arrayin = takeArrayRef;
      this.csin = takeCSE;
      this.rectarray = takeRectArray;
      if (Options.v().debug()) {
         logger.debug("Enter ArrayIndexLivenessAnalysis");
      }

      this.eug = (ExceptionalUnitGraph)dg;
      this.retrieveAllArrayLocals(this.eug.getBody(), this.fullSet);
      this.genOfUnit = new HashMap(this.eug.size() * 2 + 1);
      this.absGenOfUnit = new HashMap(this.eug.size() * 2 + 1);
      this.killOfUnit = new HashMap(this.eug.size() * 2 + 1);
      this.conditionOfGen = new HashMap(this.eug.size() * 2 + 1);
      if (this.fieldin) {
         this.localToFieldRef = new HashMap();
         this.fieldToFieldRef = new HashMap();
         this.allFieldRefs = new HashSet();
      }

      if (this.arrayin) {
         this.localToArrayRef = new HashMap();
         this.allArrayRefs = new HashSet();
         this.killArrayRelated = new HashMap();
         this.killAllArrayRef = new HashMap();
         if (this.rectarray) {
            this.multiarraylocals = new HashSet();
            this.retrieveMultiArrayLocals(this.eug.getBody(), this.multiarraylocals);
         }
      }

      if (this.csin) {
         this.localToExpr = new HashMap();
      }

      this.getAllRelatedMaps(this.eug.getBody());
      this.getGenAndKillSet(this.eug.getBody(), this.absGenOfUnit, this.genOfUnit, this.killOfUnit, this.conditionOfGen);
      this.doAnalysis();
      if (Options.v().debug()) {
         logger.debug("Leave ArrayIndexLivenessAnalysis");
      }

   }

   public HashMap<Object, HashSet<Value>> getLocalToFieldRef() {
      return this.localToFieldRef;
   }

   public HashMap<Object, HashSet<Value>> getFieldToFieldRef() {
      return this.fieldToFieldRef;
   }

   public HashSet<Value> getAllFieldRefs() {
      return this.allFieldRefs;
   }

   public HashMap getLocalToArrayRef() {
      return this.localToArrayRef;
   }

   public HashSet getAllArrayRefs() {
      return this.allArrayRefs;
   }

   public HashMap<Value, HashSet<Value>> getLocalToExpr() {
      return this.localToExpr;
   }

   public HashSet<Local> getMultiArrayLocals() {
      return this.multiarraylocals;
   }

   private void getAllRelatedMaps(Body body) {
      Iterator unitIt = body.getUnits().iterator();

      while(unitIt.hasNext()) {
         Stmt stmt = (Stmt)unitIt.next();
         Value v;
         if (this.csin && stmt instanceof DefinitionStmt) {
            Value rhs = ((DefinitionStmt)stmt).getRightOp();
            if (rhs instanceof BinopExpr) {
               Value op1 = ((BinopExpr)rhs).getOp1();
               v = ((BinopExpr)rhs).getOp2();
               HashSet refs;
               if (rhs instanceof AddExpr) {
                  if (op1 instanceof Local && v instanceof Local) {
                     refs = (HashSet)this.localToExpr.get(op1);
                     if (refs == null) {
                        refs = new HashSet();
                        this.localToExpr.put(op1, refs);
                     }

                     refs.add(rhs);
                     refs = (HashSet)this.localToExpr.get(v);
                     if (refs == null) {
                        refs = new HashSet();
                        this.localToExpr.put(v, refs);
                     }

                     refs.add(rhs);
                  }
               } else if (rhs instanceof MulExpr) {
                  refs = (HashSet)this.localToExpr.get(op1);
                  if (refs == null) {
                     refs = new HashSet();
                     this.localToExpr.put(op1, refs);
                  }

                  refs.add(rhs);
                  refs = (HashSet)this.localToExpr.get(v);
                  if (refs == null) {
                     refs = new HashSet();
                     this.localToExpr.put(v, refs);
                  }

                  refs.add(rhs);
               } else if (rhs instanceof SubExpr && v instanceof Local) {
                  refs = (HashSet)this.localToExpr.get(v);
                  if (refs == null) {
                     refs = new HashSet();
                     this.localToExpr.put(v, refs);
                  }

                  refs.add(rhs);
                  if (op1 instanceof Local) {
                     refs = (HashSet)this.localToExpr.get(op1);
                     if (refs == null) {
                        refs = new HashSet();
                        this.localToExpr.put(op1, refs);
                     }

                     refs.add(rhs);
                  }
               }
            }
         }

         Iterator var11 = stmt.getUseAndDefBoxes().iterator();

         while(var11.hasNext()) {
            ValueBox vbox = (ValueBox)var11.next();
            v = vbox.getValue();
            if (this.fieldin) {
               if (v instanceof InstanceFieldRef) {
                  Value base = ((InstanceFieldRef)v).getBase();
                  SootField field = ((InstanceFieldRef)v).getField();
                  HashSet<Value> baseset = (HashSet)this.localToFieldRef.get(base);
                  if (baseset == null) {
                     baseset = new HashSet();
                     this.localToFieldRef.put(base, baseset);
                  }

                  baseset.add(v);
                  HashSet<Value> fieldset = (HashSet)this.fieldToFieldRef.get(field);
                  if (fieldset == null) {
                     fieldset = new HashSet();
                     this.fieldToFieldRef.put(field, fieldset);
                  }

                  fieldset.add(v);
               }

               if (v instanceof FieldRef) {
                  this.allFieldRefs.add(v);
               }
            }

            if (this.arrayin) {
            }
         }
      }

   }

   private void retrieveAllArrayLocals(Body body, Set<Local> container) {
      Iterator var3 = body.getLocals().iterator();

      while(true) {
         Local local;
         Type type;
         do {
            if (!var3.hasNext()) {
               return;
            }

            local = (Local)var3.next();
            type = local.getType();
         } while(!(type instanceof IntType) && !(type instanceof ArrayType));

         container.add(local);
      }
   }

   private void retrieveMultiArrayLocals(Body body, Set<Local> container) {
      Iterator var3 = body.getLocals().iterator();

      while(var3.hasNext()) {
         Local local = (Local)var3.next();
         Type type = local.getType();
         if (type instanceof ArrayType && ((ArrayType)type).numDimensions > 1) {
            this.multiarraylocals.add(local);
         }
      }

   }

   private void getGenAndKillSetForDefnStmt(DefinitionStmt asstmt, HashMap<Stmt, HashSet<Value>> absgen, HashSet<Object> genset, HashSet<Value> absgenset, HashSet<Value> killset, HashSet<Value> condset) {
      Value lhs = asstmt.getLeftOp();
      Value rhs = asstmt.getRightOp();
      boolean killarrayrelated = false;
      boolean killallarrayref = false;
      HashSet exprs;
      if (this.fieldin) {
         if (lhs instanceof Local) {
            exprs = (HashSet)this.localToFieldRef.get(lhs);
            if (exprs != null) {
               killset.addAll(exprs);
            }
         } else if (lhs instanceof StaticFieldRef) {
            killset.add(lhs);
            condset.add(lhs);
         } else if (lhs instanceof InstanceFieldRef) {
            SootField field = ((InstanceFieldRef)lhs).getField();
            HashSet<Value> related = (HashSet)this.fieldToFieldRef.get(field);
            if (related != null) {
               killset.addAll(related);
            }

            condset.add(lhs);
         }

         if (asstmt.containsInvokeExpr()) {
            killset.addAll(this.allFieldRefs);
         }
      }

      if (this.arrayin) {
         if (lhs instanceof Local) {
            killarrayrelated = true;
         } else if (lhs instanceof ArrayRef) {
            killallarrayref = true;
            condset.add(lhs);
         }

         if (asstmt.containsInvokeExpr()) {
            killallarrayref = true;
         }
      }

      Value size;
      Value op2;
      if (this.csin) {
         exprs = (HashSet)this.localToExpr.get(lhs);
         if (exprs != null) {
            killset.addAll(exprs);
         }

         if (rhs instanceof BinopExpr) {
            op2 = ((BinopExpr)rhs).getOp1();
            size = ((BinopExpr)rhs).getOp2();
            if (rhs instanceof AddExpr) {
               if (op2 instanceof Local && size instanceof Local) {
                  genset.add(rhs);
               }
            } else if (rhs instanceof MulExpr) {
               if (op2 instanceof Local || size instanceof Local) {
                  genset.add(rhs);
               }
            } else if (rhs instanceof SubExpr && size instanceof Local) {
               genset.add(rhs);
            }
         }
      }

      Value op1;
      if (lhs instanceof Local && this.fullSet.contains(lhs)) {
         killset.add(lhs);
         condset.add(lhs);
      } else if (lhs instanceof ArrayRef) {
         op1 = ((ArrayRef)lhs).getBase();
         op2 = ((ArrayRef)lhs).getIndex();
         absgenset.add(op1);
         if (op2 instanceof Local) {
            absgenset.add(op2);
         }
      }

      if (rhs instanceof Local) {
         if (this.fullSet.contains(rhs)) {
            genset.add(rhs);
         }
      } else if (rhs instanceof FieldRef) {
         if (this.fieldin) {
            genset.add(rhs);
         }
      } else if (rhs instanceof ArrayRef) {
         op1 = ((ArrayRef)rhs).getBase();
         op2 = ((ArrayRef)rhs).getIndex();
         absgenset.add(op1);
         if (op2 instanceof Local) {
            absgenset.add(op2);
         }

         if (this.arrayin) {
            genset.add(rhs);
            if (this.rectarray) {
               genset.add(Array2ndDimensionSymbol.v(op1));
            }
         }
      } else if (rhs instanceof NewArrayExpr) {
         op1 = ((NewArrayExpr)rhs).getSize();
         if (op1 instanceof Local) {
            genset.add(op1);
         }
      } else if (rhs instanceof NewMultiArrayExpr) {
         List sizes = ((NewMultiArrayExpr)rhs).getSizes();
         Iterator sizeIt = sizes.iterator();

         while(sizeIt.hasNext()) {
            size = (Value)sizeIt.next();
            if (size instanceof Local) {
               genset.add(size);
            }
         }
      } else if (rhs instanceof LengthExpr) {
         op1 = ((LengthExpr)rhs).getOp();
         genset.add(op1);
      } else if (rhs instanceof JAddExpr) {
         op1 = ((JAddExpr)rhs).getOp1();
         op2 = ((JAddExpr)rhs).getOp2();
         if (op1 instanceof IntConstant && op2 instanceof Local) {
            genset.add(op2);
         } else if (op2 instanceof IntConstant && op1 instanceof Local) {
            genset.add(op1);
         }
      } else if (rhs instanceof JSubExpr) {
         op1 = ((JSubExpr)rhs).getOp1();
         op2 = ((JSubExpr)rhs).getOp2();
         if (op1 instanceof Local && op2 instanceof IntConstant) {
            genset.add(op1);
         }
      }

      if (this.arrayin) {
         if (killarrayrelated) {
            this.killArrayRelated.put(asstmt, lhs);
         }

         if (killallarrayref) {
            this.killAllArrayRef.put(asstmt, new Boolean(true));
         }
      }

   }

   private void getGenAndKillSet(Body body, HashMap<Stmt, HashSet<Value>> absgen, HashMap<Stmt, HashSet<Object>> gen, HashMap<Stmt, HashSet<Value>> kill, HashMap<Stmt, HashSet<Value>> condition) {
      Iterator var6 = body.getUnits().iterator();

      while(var6.hasNext()) {
         Unit u = (Unit)var6.next();
         Stmt stmt = (Stmt)u;
         HashSet<Object> genset = new HashSet();
         HashSet<Value> absgenset = new HashSet();
         HashSet<Value> killset = new HashSet();
         HashSet<Value> condset = new HashSet();
         if (stmt instanceof DefinitionStmt) {
            this.getGenAndKillSetForDefnStmt((DefinitionStmt)stmt, absgen, genset, absgenset, killset, condset);
         } else if (stmt instanceof IfStmt) {
            Value cmpcond = ((IfStmt)stmt).getCondition();
            if (cmpcond instanceof ConditionExpr) {
               Value op1 = ((ConditionExpr)cmpcond).getOp1();
               Value op2 = ((ConditionExpr)cmpcond).getOp2();
               if (this.fullSet.contains(op1) && this.fullSet.contains(op2)) {
                  condset.add(op1);
                  condset.add(op2);
                  genset.add(op1);
                  genset.add(op2);
               }
            }
         }

         if (genset.size() != 0) {
            gen.put(stmt, genset);
         }

         if (absgenset.size() != 0) {
            absgen.put(stmt, absgenset);
         }

         if (killset.size() != 0) {
            kill.put(stmt, killset);
         }

         if (condset.size() != 0) {
            condition.put(stmt, condset);
         }
      }

   }

   protected Object newInitialFlow() {
      return new HashSet();
   }

   protected Object entryInitialFlow() {
      return new HashSet();
   }

   protected void flowThrough(Object inValue, Object unit, Object outValue) {
      HashSet inset = (HashSet)inValue;
      HashSet outset = (HashSet)outValue;
      Stmt stmt = (Stmt)unit;
      outset.clear();
      outset.addAll(inset);
      HashSet<Object> genset = (HashSet)this.genOfUnit.get(unit);
      HashSet<Value> absgenset = (HashSet)this.absGenOfUnit.get(unit);
      HashSet<Value> killset = (HashSet)this.killOfUnit.get(unit);
      HashSet<Value> condset = (HashSet)this.conditionOfGen.get(unit);
      if (killset != null) {
         outset.removeAll(killset);
      }

      if (this.arrayin) {
         Boolean killall = (Boolean)this.killAllArrayRef.get(stmt);
         if (killall != null && killall) {
            List keylist = new ArrayList(outset);
            Iterator keyIt = keylist.iterator();

            while(keyIt.hasNext()) {
               Object key = keyIt.next();
               if (key instanceof ArrayRef) {
                  outset.remove(key);
               }
            }
         } else {
            Object local = this.killArrayRelated.get(stmt);
            if (local != null) {
               List keylist = new ArrayList(outset);
               Iterator keyIt = keylist.iterator();

               while(keyIt.hasNext()) {
                  Object key = keyIt.next();
                  if (key instanceof ArrayRef) {
                     Value base = ((ArrayRef)key).getBase();
                     Value index = ((ArrayRef)key).getIndex();
                     if (base.equals(local) || index.equals(local)) {
                        outset.remove(key);
                     }
                  }

                  if (this.rectarray && key instanceof Array2ndDimensionSymbol) {
                     Object base = ((Array2ndDimensionSymbol)key).getVar();
                     if (base.equals(local)) {
                        outset.remove(key);
                     }
                  }
               }
            }
         }
      }

      if (genset != null) {
         if (condset != null && condset.size() != 0) {
            Iterator condIt = condset.iterator();

            while(condIt.hasNext()) {
               if (inset.contains(condIt.next())) {
                  outset.addAll(genset);
                  break;
               }
            }
         } else {
            outset.addAll(genset);
         }
      }

      if (absgenset != null) {
         outset.addAll(absgenset);
      }

   }

   protected void merge(Object in1, Object in2, Object out) {
      HashSet inset1 = (HashSet)in1;
      HashSet inset2 = (HashSet)in2;
      HashSet outset = (HashSet)out;
      HashSet src = inset1;
      if (outset == inset1) {
         src = inset2;
      } else if (outset == inset2) {
         src = inset1;
      } else {
         outset.clear();
         outset.addAll(inset2);
      }

      outset.addAll(src);
   }

   protected void copy(Object source, Object dest) {
      if (source != dest) {
         HashSet sourceSet = (HashSet)source;
         HashSet destSet = (HashSet)dest;
         destSet.clear();
         destSet.addAll(sourceSet);
      }
   }
}
