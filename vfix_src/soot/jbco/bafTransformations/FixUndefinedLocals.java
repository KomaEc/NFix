package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.RefLikeType;
import soot.StmtAddressType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.baf.DoubleWordType;
import soot.baf.IdentityInst;
import soot.baf.IncInst;
import soot.baf.NopInst;
import soot.baf.OpTypeArgInst;
import soot.baf.PushInst;
import soot.baf.WordType;
import soot.baf.internal.AbstractOpTypeInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.GuaranteedDefs;

public class FixUndefinedLocals extends BodyTransformer implements IJbcoTransform {
   private int undefined = 0;
   public static String[] dependancies = new String[]{"bb.jbco_j2bl", "bb.jbco_ful", "bb.lp"};
   public static String name = "bb.jbco_ful";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
      out.println("Undefined Locals fixed with pre-initializers: " + this.undefined);
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int icount = 0;
      boolean passedIDs = false;
      Map<Local, Local> bafToJLocals = (Map)Main.methods2Baf2JLocals.get(b.getMethod());
      ArrayList<Value> initialized = new ArrayList();
      PatchingChain<Unit> units = b.getUnits();
      GuaranteedDefs gd = new GuaranteedDefs(new ExceptionalUnitGraph(b));
      Iterator<Unit> unitIt = units.snapshotIterator();
      Object after = null;

      while(true) {
         while(unitIt.hasNext()) {
            Unit u = (Unit)unitIt.next();
            if (passedIDs || !(u instanceof IdentityInst)) {
               passedIDs = true;
               if (after == null) {
                  after = Baf.v().newNopInst();
                  units.addFirst((Unit)after);
               }

               List<?> defs = gd.getGuaranteedDefs(u);
               Iterator useIt = u.getUseBoxes().iterator();

               while(useIt.hasNext()) {
                  Value v = ((ValueBox)useIt.next()).getValue();
                  if (v instanceof Local && !defs.contains(v) && !initialized.contains(v)) {
                     Type t = null;
                     Local l = (Local)v;
                     Local jl = (Local)bafToJLocals.get(l);
                     if (jl != null) {
                        t = jl.getType();
                     } else {
                        t = l.getType();
                        if (u instanceof OpTypeArgInst) {
                           OpTypeArgInst ota = (OpTypeArgInst)u;
                           t = ota.getOpType();
                        } else if (u instanceof AbstractOpTypeInst) {
                           AbstractOpTypeInst ota = (AbstractOpTypeInst)u;
                           t = ota.getOpType();
                        } else if (u instanceof IncInst) {
                           t = IntType.v();
                        }

                        if (t instanceof DoubleWordType || t instanceof WordType) {
                           throw new RuntimeException("Shouldn't get here (t is a double or word type: in FixUndefinedLocals)");
                        }
                     }

                     Unit store = Baf.v().newStoreInst((Type)t, l);
                     units.insertAfter((Unit)store, (Unit)after);
                     if (t instanceof ArrayType) {
                        Unit tmp = Baf.v().newInstanceCastInst((Type)t);
                        units.insertBefore((Unit)tmp, (Unit)store);
                        store = tmp;
                     }

                     Unit pinit = getPushInitializer(l, (Type)t);
                     units.insertBefore((Unit)pinit, (Unit)store);
                     initialized.add(l);
                  }
               }
            } else {
               Value v = ((IdentityInst)u).getLeftOp();
               if (v instanceof Local) {
                  initialized.add(v);
                  ++icount;
               }

               after = u;
            }
         }

         if (after instanceof NopInst) {
            units.remove(after);
         }

         this.undefined += initialized.size() - icount;
         return;
      }
   }

   public static PushInst getPushInitializer(Local l, Type t) {
      if (t instanceof IntegerType) {
         return Baf.v().newPushInst(IntConstant.v(Rand.getInt()));
      } else if (!(t instanceof RefLikeType) && !(t instanceof StmtAddressType)) {
         if (t instanceof LongType) {
            return Baf.v().newPushInst(LongConstant.v(Rand.getLong()));
         } else if (t instanceof FloatType) {
            return Baf.v().newPushInst(FloatConstant.v(Rand.getFloat()));
         } else {
            return t instanceof DoubleType ? Baf.v().newPushInst(DoubleConstant.v(Rand.getDouble())) : null;
         }
      } else {
         return Baf.v().newPushInst(NullConstant.v());
      }
   }
}
