package soot.shimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.shimple.PhiExpr;
import soot.shimple.ShimpleExprSwitch;
import soot.toolkits.graph.Block;
import soot.toolkits.scalar.ValueUnitPair;
import soot.util.Switch;

public class SPhiExpr implements PhiExpr {
   private static final Logger logger = LoggerFactory.getLogger(SPhiExpr.class);
   protected List<ValueUnitPair> argPairs = new ArrayList();
   protected Map<Unit, ValueUnitPair> predToPair = new HashMap();
   protected Type type;
   int blockId = -1;

   public SPhiExpr(Local leftLocal, List<Block> preds) {
      this.type = leftLocal.getType();
      Iterator var3 = preds.iterator();

      while(var3.hasNext()) {
         Block pred = (Block)var3.next();
         this.addArg(leftLocal, (Block)pred);
      }

   }

   public SPhiExpr(List<Value> args, List<Unit> preds) {
      if (args.size() == 0) {
         throw new RuntimeException("Arg list may not be empty");
      } else if (args.size() != preds.size()) {
         throw new RuntimeException("Arg list does not match Pred list");
      } else {
         this.type = ((Value)args.get(0)).getType();
         Iterator<Value> argsIt = args.iterator();
         Iterator predsIt = preds.iterator();

         while(argsIt.hasNext()) {
            Value arg = (Value)argsIt.next();
            Object pred = predsIt.next();
            if (pred instanceof Block) {
               this.addArg(arg, (Block)pred);
            } else {
               if (!(pred instanceof Unit)) {
                  throw new RuntimeException("Must be a CFG block or tail unit.");
               }

               this.addArg(arg, (Unit)pred);
            }
         }

      }
   }

   public List<ValueUnitPair> getArgs() {
      return Collections.unmodifiableList(this.argPairs);
   }

   public List<Value> getValues() {
      List<Value> args = new ArrayList();
      Iterator var2 = this.argPairs.iterator();

      while(var2.hasNext()) {
         ValueUnitPair vup = (ValueUnitPair)var2.next();
         Value arg = vup.getValue();
         args.add(arg);
      }

      return args;
   }

   public List<Unit> getPreds() {
      List<Unit> preds = new ArrayList();
      Iterator var2 = this.argPairs.iterator();

      while(var2.hasNext()) {
         ValueUnitPair up = (ValueUnitPair)var2.next();
         Unit arg = up.getUnit();
         preds.add(arg);
      }

      return preds;
   }

   public int getArgCount() {
      return this.argPairs.size();
   }

   public ValueUnitPair getArgBox(int index) {
      return index >= 0 && index < this.argPairs.size() ? (ValueUnitPair)this.argPairs.get(index) : null;
   }

   public Value getValue(int index) {
      ValueUnitPair arg = this.getArgBox(index);
      return arg == null ? null : arg.getValue();
   }

   public Unit getPred(int index) {
      ValueUnitPair arg = this.getArgBox(index);
      return arg == null ? null : arg.getUnit();
   }

   public int getArgIndex(Unit predTailUnit) {
      ValueUnitPair pair = this.getArgBox(predTailUnit);
      return this.argPairs.indexOf(pair);
   }

   public ValueUnitPair getArgBox(Unit predTailUnit) {
      ValueUnitPair vup = (ValueUnitPair)this.predToPair.get(predTailUnit);
      if (vup == null || vup.getUnit() != predTailUnit) {
         this.updateCache();
         vup = (ValueUnitPair)this.predToPair.get(predTailUnit);
         if (vup != null && vup.getUnit() != predTailUnit) {
            throw new RuntimeException("Assertion failed.");
         }
      }

      return vup;
   }

   public Value getValue(Unit predTailUnit) {
      ValueBox vb = this.getArgBox(predTailUnit);
      return vb == null ? null : vb.getValue();
   }

   public int getArgIndex(Block pred) {
      ValueUnitPair box = this.getArgBox(pred);
      return this.argPairs.indexOf(box);
   }

   public ValueUnitPair getArgBox(Block pred) {
      Unit predTailUnit = pred.getTail();

      ValueUnitPair box;
      for(box = this.getArgBox(predTailUnit); box == null; box = this.getArgBox(predTailUnit)) {
         predTailUnit = pred.getPredOf(predTailUnit);
         if (predTailUnit == null) {
            break;
         }
      }

      return box;
   }

   public Value getValue(Block pred) {
      ValueBox vb = this.getArgBox(pred);
      return vb == null ? null : vb.getValue();
   }

   public boolean setArg(int index, Value arg, Unit predTailUnit) {
      boolean ret1 = this.setValue(index, arg);
      boolean ret2 = this.setPred(index, predTailUnit);
      if (ret1 != ret2) {
         throw new RuntimeException("Assertion failed.");
      } else {
         return ret1;
      }
   }

   public boolean setArg(int index, Value arg, Block pred) {
      return this.setArg(index, arg, pred.getTail());
   }

   public boolean setValue(int index, Value arg) {
      ValueUnitPair argPair = this.getArgBox(index);
      if (argPair == null) {
         return false;
      } else {
         argPair.setValue(arg);
         return true;
      }
   }

   public boolean setValue(Unit predTailUnit, Value arg) {
      int index = this.getArgIndex(predTailUnit);
      return this.setValue(index, arg);
   }

   public boolean setValue(Block pred, Value arg) {
      int index = this.getArgIndex(pred);
      return this.setValue(index, arg);
   }

   public boolean setPred(int index, Unit predTailUnit) {
      ValueUnitPair argPair = this.getArgBox(index);
      if (argPair == null) {
         return false;
      } else {
         int other = this.getArgIndex(predTailUnit);
         if (other != -1) {
            logger.warn("An argument with control flow predecessor " + predTailUnit + " already exists in " + this + "!");
            logger.warn("setPred resulted in deletion of " + argPair + " from " + this + ".");
            this.removeArg(argPair);
            return false;
         } else {
            argPair.setUnit(predTailUnit);
            return true;
         }
      }
   }

   public boolean setPred(int index, Block pred) {
      return this.setPred(index, pred.getTail());
   }

   public boolean removeArg(int index) {
      ValueUnitPair arg = this.getArgBox(index);
      return this.removeArg(arg);
   }

   public boolean removeArg(Unit predTailUnit) {
      ValueUnitPair arg = this.getArgBox(predTailUnit);
      return this.removeArg(arg);
   }

   public boolean removeArg(Block pred) {
      ValueUnitPair arg = this.getArgBox(pred);
      return this.removeArg(arg);
   }

   public boolean removeArg(ValueUnitPair arg) {
      if (this.argPairs.remove(arg)) {
         this.predToPair.remove(arg.getUnit());
         arg.getUnit().removeBoxPointingToThis(arg);
         return true;
      } else {
         return false;
      }
   }

   public boolean addArg(Value arg, Block pred) {
      return this.addArg(arg, pred.getTail());
   }

   public boolean addArg(Value arg, Unit predTailUnit) {
      if (predTailUnit == null) {
         return false;
      } else if (this.predToPair.keySet().contains(predTailUnit)) {
         return false;
      } else {
         ValueUnitPair vup = new SValueUnitPair(arg, predTailUnit);
         this.argPairs.add(vup);
         this.predToPair.put(predTailUnit, vup);
         return true;
      }
   }

   public void setBlockId(int blockId) {
      this.blockId = blockId;
   }

   public int getBlockId() {
      if (this.blockId == -1) {
         throw new RuntimeException("Assertion failed:  Block Id unknown.");
      } else {
         return this.blockId;
      }
   }

   protected void updateCache() {
      int needed = this.argPairs.size();
      this.predToPair = new HashMap(needed << 1, 1.0F);
      Iterator var2 = this.argPairs.iterator();

      while(var2.hasNext()) {
         ValueUnitPair vup = (ValueUnitPair)var2.next();
         this.predToPair.put(vup.getUnit(), vup);
      }

   }

   public boolean equivTo(Object o) {
      if (o instanceof SPhiExpr) {
         SPhiExpr pe = (SPhiExpr)o;
         if (this.getArgCount() != pe.getArgCount()) {
            return false;
         } else {
            for(int i = 0; i < this.getArgCount(); ++i) {
               if (!this.getArgBox(i).equivTo(pe.getArgBox(i))) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int equivHashCode() {
      int hashcode = 1;

      for(int i = 0; i < this.getArgCount(); ++i) {
         hashcode = hashcode * 17 + this.getArgBox(i).equivHashCode();
      }

      return hashcode;
   }

   public List<UnitBox> getUnitBoxes() {
      Set<UnitBox> boxes = new HashSet(this.argPairs.size());
      Iterator var2 = this.argPairs.iterator();

      while(var2.hasNext()) {
         ValueUnitPair up = (ValueUnitPair)var2.next();
         boxes.add(up);
      }

      return new ArrayList(boxes);
   }

   public void clearUnitBoxes() {
      Iterator var1 = this.getUnitBoxes().iterator();

      while(var1.hasNext()) {
         UnitBox box = (UnitBox)var1.next();
         box.setUnit((Unit)null);
      }

   }

   public List<ValueBox> getUseBoxes() {
      Set<ValueBox> set = new HashSet();
      Iterator var2 = this.argPairs.iterator();

      while(var2.hasNext()) {
         ValueUnitPair argPair = (ValueUnitPair)var2.next();
         set.addAll(argPair.getValue().getUseBoxes());
         set.add(argPair);
      }

      return new ArrayList(set);
   }

   public Type getType() {
      return this.type;
   }

   public String toString() {
      StringBuffer expr = new StringBuffer("Phi(");
      boolean isFirst = true;

      for(Iterator var3 = this.argPairs.iterator(); var3.hasNext(); isFirst = false) {
         ValueUnitPair vuPair = (ValueUnitPair)var3.next();
         if (!isFirst) {
            expr.append(", ");
         }

         Value arg = vuPair.getValue();
         expr.append(arg.toString());
      }

      expr.append(")");
      return expr.toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("Phi");
      up.literal("(");
      boolean isFirst = true;

      for(Iterator var3 = this.argPairs.iterator(); var3.hasNext(); isFirst = false) {
         ValueUnitPair vuPair = (ValueUnitPair)var3.next();
         if (!isFirst) {
            up.literal(", ");
         }

         vuPair.toString(up);
      }

      up.literal(")");
   }

   public void apply(Switch sw) {
      ((ShimpleExprSwitch)sw).casePhiExpr(this);
   }

   public Object clone() {
      return new SPhiExpr(this.getValues(), this.getPreds());
   }
}
