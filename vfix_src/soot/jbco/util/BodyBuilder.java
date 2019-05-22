package soot.jbco.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.baf.IfCmpEqInst;
import soot.baf.IfCmpGeInst;
import soot.baf.IfCmpGtInst;
import soot.baf.IfCmpLeInst;
import soot.baf.IfCmpLtInst;
import soot.baf.IfCmpNeInst;
import soot.baf.IfEqInst;
import soot.baf.IfGeInst;
import soot.baf.IfGtInst;
import soot.baf.IfLeInst;
import soot.baf.IfLtInst;
import soot.baf.IfNeInst;
import soot.baf.IfNonNullInst;
import soot.baf.IfNullInst;
import soot.jimple.Jimple;
import soot.jimple.ThisRef;
import soot.util.Chain;

public class BodyBuilder {
   public static boolean bodiesHaveBeenBuilt = false;
   public static boolean namesHaveBeenRetrieved = false;
   public static List<String> nameList = new ArrayList();

   public static void retrieveAllBodies() {
      if (!bodiesHaveBeenBuilt) {
         Iterator var0 = Scene.v().getApplicationClasses().iterator();

         while(var0.hasNext()) {
            SootClass c = (SootClass)var0.next();
            Iterator var2 = c.getMethods().iterator();

            while(var2.hasNext()) {
               SootMethod m = (SootMethod)var2.next();
               if (m.isConcrete() && !m.hasActiveBody()) {
                  m.retrieveActiveBody();
               }
            }
         }

         bodiesHaveBeenBuilt = true;
      }
   }

   public static void retrieveAllNames() {
      if (!namesHaveBeenRetrieved) {
         Iterator var0 = Scene.v().getApplicationClasses().iterator();

         while(var0.hasNext()) {
            SootClass c = (SootClass)var0.next();
            nameList.add(c.getName());
            Iterator var2 = c.getMethods().iterator();

            while(var2.hasNext()) {
               SootMethod m = (SootMethod)var2.next();
               nameList.add(m.getName());
            }

            var2 = c.getFields().iterator();

            while(var2.hasNext()) {
               SootField m = (SootField)var2.next();
               nameList.add(m.getName());
            }
         }

         namesHaveBeenRetrieved = true;
      }
   }

   public static Local buildThisLocal(PatchingChain<Unit> units, ThisRef tr, Collection<Local> locals) {
      Local ths = Jimple.v().newLocal("ths", tr.getType());
      locals.add(ths);
      units.add((Unit)Jimple.v().newIdentityStmt(ths, Jimple.v().newThisRef((RefType)tr.getType())));
      return ths;
   }

   public static List<Local> buildParameterLocals(PatchingChain<Unit> units, Collection<Local> locals, List<Type> paramTypes) {
      List<Local> args = new ArrayList();

      for(int k = 0; k < paramTypes.size(); ++k) {
         Type type = (Type)paramTypes.get(k);
         Local loc = Jimple.v().newLocal("l" + k, type);
         locals.add(loc);
         units.add((Unit)Jimple.v().newIdentityStmt(loc, Jimple.v().newParameterRef(type, k)));
         args.add(loc);
      }

      return args;
   }

   public static void updateTraps(Unit oldu, Unit newu, Chain<Trap> traps) {
      int size = traps.size();
      if (size != 0) {
         Trap t = (Trap)traps.getFirst();

         do {
            if (t.getBeginUnit() == oldu) {
               t.setBeginUnit(newu);
            }

            if (t.getEndUnit() == oldu) {
               t.setEndUnit(newu);
            }

            if (t.getHandlerUnit() == oldu) {
               t.setHandlerUnit(newu);
            }

            --size;
         } while(size > 0 && (t = (Trap)traps.getSuccOf(t)) != null);

      }
   }

   public static boolean isExceptionCaughtAt(Chain<Unit> units, Unit u, Iterator<Trap> trapsIt) {
      label18:
      while(true) {
         if (trapsIt.hasNext()) {
            Trap t = (Trap)trapsIt.next();
            Iterator it = units.iterator(t.getBeginUnit(), units.getPredOf(t.getEndUnit()));

            do {
               if (!it.hasNext()) {
                  continue label18;
               }
            } while(!u.equals(it.next()));

            return true;
         }

         return false;
      }
   }

   public static int getIntegerNine() {
      int r1 = Rand.getInt(8388606) * 256;
      int r2 = Rand.getInt(28) * 9;
      if (r2 > 126) {
         r2 += 4;
      }

      return r1 + r2;
   }

   public static boolean isBafIf(Unit u) {
      return u instanceof IfCmpEqInst || u instanceof IfCmpGeInst || u instanceof IfCmpGtInst || u instanceof IfCmpLeInst || u instanceof IfCmpLtInst || u instanceof IfCmpNeInst || u instanceof IfEqInst || u instanceof IfGeInst || u instanceof IfGtInst || u instanceof IfLeInst || u instanceof IfLtInst || u instanceof IfNeInst || u instanceof IfNonNullInst || u instanceof IfNullInst;
   }
}
