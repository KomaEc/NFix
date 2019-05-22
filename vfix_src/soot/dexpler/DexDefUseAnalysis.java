package soot.dexpler;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.toolkits.scalar.LocalDefs;

public class DexDefUseAnalysis implements LocalDefs {
   private final Body body;
   private Map<Local, Set<Unit>> localToUses = new HashMap();
   private Map<Local, Set<Unit>> localToDefs = new HashMap();
   private Map<Local, Set<Unit>> localToDefsWithAliases = new HashMap();
   protected BitSet[] localToDefsBits;
   protected BitSet[] localToUsesBits;
   protected Map<Local, Integer> localToNumber = new HashMap();
   protected List<Unit> unitList;

   public DexDefUseAnalysis(Body body) {
      this.body = body;
      this.initialize();
   }

   protected void initialize() {
      int lastLocalNumber = 0;
      Iterator var2 = this.body.getLocals().iterator();

      while(var2.hasNext()) {
         Local l = (Local)var2.next();
         this.localToNumber.put(l, lastLocalNumber++);
      }

      this.localToDefsBits = new BitSet[this.body.getLocalCount()];
      this.localToUsesBits = new BitSet[this.body.getLocalCount()];
      this.unitList = new ArrayList(this.body.getUnits());

      for(int i = 0; i < this.unitList.size(); ++i) {
         Unit u = (Unit)this.unitList.get(i);
         if (u instanceof DefinitionStmt) {
            Value val = ((DefinitionStmt)u).getLeftOp();
            if (val instanceof Local) {
               int localIdx = (Integer)this.localToNumber.get(val);
               BitSet bs = this.localToDefsBits[localIdx];
               if (bs == null) {
                  bs = new BitSet();
                  this.localToDefsBits[localIdx] = bs;
               }

               bs.set(i);
            }
         }

         Iterator var11 = u.getUseBoxes().iterator();

         while(var11.hasNext()) {
            ValueBox vb = (ValueBox)var11.next();
            Value val = vb.getValue();
            if (val instanceof Local) {
               int localIdx = (Integer)this.localToNumber.get(val);
               BitSet bs = this.localToUsesBits[localIdx];
               if (bs == null) {
                  bs = new BitSet();
                  this.localToUsesBits[localIdx] = bs;
               }

               bs.set(i);
            }
         }
      }

   }

   public Set<Unit> getUsesOf(Local l) {
      Set<Unit> uses = (Set)this.localToUses.get(l);
      if (uses == null) {
         uses = new HashSet();
         BitSet bs = this.localToUsesBits[(Integer)this.localToNumber.get(l)];
         if (bs != null) {
            for(int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
               ((Set)uses).add(this.unitList.get(i));
            }
         }

         this.localToUses.put(l, uses);
      }

      return (Set)uses;
   }

   protected Set<Unit> collectDefinitionsWithAliases(Local l) {
      Set<Unit> defs = (Set)this.localToDefsWithAliases.get(l);
      if (defs == null) {
         Set<Local> seenLocals = new HashSet();
         defs = new HashSet();
         List<Local> newLocals = new ArrayList();
         newLocals.add(l);

         while(true) {
            Local curLocal;
            BitSet bsUses;
            do {
               if (newLocals.isEmpty()) {
                  this.localToDefsWithAliases.put(l, defs);
                  return (Set)defs;
               }

               curLocal = (Local)newLocals.remove(0);
               BitSet bsDefs = this.localToDefsBits[(Integer)this.localToNumber.get(curLocal)];
               if (bsDefs != null) {
                  for(int i = bsDefs.nextSetBit(0); i >= 0; i = bsDefs.nextSetBit(i + 1)) {
                     Unit u = (Unit)this.unitList.get(i);
                     ((Set)defs).add(u);
                     DefinitionStmt defStmt = (DefinitionStmt)u;
                     if (defStmt.getRightOp() instanceof Local && seenLocals.add((Local)defStmt.getRightOp())) {
                        newLocals.add((Local)defStmt.getRightOp());
                     }
                  }
               }

               bsUses = this.localToUsesBits[(Integer)this.localToNumber.get(curLocal)];
            } while(bsUses == null);

            for(int i = bsUses.nextSetBit(0); i >= 0; i = bsUses.nextSetBit(i + 1)) {
               Unit use = (Unit)this.unitList.get(i);
               if (use instanceof AssignStmt) {
                  AssignStmt assignUse = (AssignStmt)use;
                  if (assignUse.getRightOp() == curLocal && assignUse.getLeftOp() instanceof Local && seenLocals.add((Local)assignUse.getLeftOp())) {
                     newLocals.add((Local)assignUse.getLeftOp());
                  }
               }
            }
         }
      } else {
         return (Set)defs;
      }
   }

   public List<Unit> getDefsOfAt(Local l, Unit s) {
      return this.getDefsOf(l);
   }

   public List<Unit> getDefsOf(Local l) {
      Set<Unit> defs = (Set)this.localToDefs.get(l);
      if (defs == null) {
         defs = new HashSet();
         BitSet bs = this.localToDefsBits[(Integer)this.localToNumber.get(l)];
         if (bs != null) {
            for(int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
               Unit u = (Unit)this.unitList.get(i);
               if (u instanceof DefinitionStmt && ((DefinitionStmt)u).getLeftOp() == l) {
                  ((Set)defs).add(u);
               }
            }
         }

         this.localToDefs.put(l, defs);
      }

      return new ArrayList((Collection)defs);
   }
}
