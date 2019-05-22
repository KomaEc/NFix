package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.LoadInst;
import soot.baf.SpecialInvokeInst;
import soot.baf.ThrowInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jbco.util.ThrowSet;
import soot.jimple.NullConstant;

public class ConstructorConfuser extends BodyTransformer implements IJbcoTransform {
   static int count = 0;
   static int[] instances = new int[4];
   public static String[] dependancies = new String[]{"bb.jbco_dcc", "bb.jbco_ful", "bb.lp"};
   public static String name = "bb.jbco_dcc";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
      out.println("Constructor methods have been jumbled: " + count);
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      if (b.getMethod().getSubSignature().equals("void <init>()")) {
         int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
         if (weight != 0) {
            SootClass origClass = b.getMethod().getDeclaringClass();
            SootClass c;
            if (origClass.hasSuperclass()) {
               c = origClass.getSuperclass();
            } else {
               c = null;
            }

            PatchingChain<Unit> units = b.getUnits();
            Iterator<Unit> it = units.snapshotIterator();
            Unit prev = null;

            SpecialInvokeInst sii;
            Unit u;
            for(sii = null; it.hasNext(); prev = u) {
               u = (Unit)it.next();
               if (u instanceof SpecialInvokeInst) {
                  sii = (SpecialInvokeInst)u;
                  SootMethodRef smr = sii.getMethodRef();
                  if (c != null && smr.declaringClass().getName().equals(c.getName()) && smr.name().equals("<init>")) {
                     break;
                  }

                  sii = null;
               }
            }

            if (sii != null) {
               int lowi = -1;
               int lowest = 99999999;
               int rand = Rand.getInt(4);

               for(int i = 0; i < instances.length; ++i) {
                  if (lowest > instances[i]) {
                     lowest = instances[i];
                     lowi = i;
                  }
               }

               if (instances[rand] > instances[lowi]) {
                  rand = lowi;
               }

               boolean done = false;
               ThrowInst throwThis;
               switch(rand) {
               case 0:
                  if (prev != null && prev instanceof LoadInst && sii.getMethodRef().parameterTypes().size() == 0 && !BodyBuilder.isExceptionCaughtAt(units, sii, b.getTraps().iterator())) {
                     Local bl = ((LoadInst)prev).getLocal();
                     Map<Local, Local> locals = (Map)Main.methods2Baf2JLocals.get(b.getMethod());
                     if (locals != null && locals.containsKey(bl)) {
                        Type t = ((Local)locals.get(bl)).getType();
                        if (t instanceof RefType && ((RefType)t).getSootClass().getName().equals(origClass.getName())) {
                           units.insertBefore((Unit)Baf.v().newDup1Inst(RefType.v()), (Unit)sii);
                           Unit ifinst = Baf.v().newIfNullInst(sii);
                           units.insertBeforeNoRedirect(ifinst, sii);
                           units.insertAfter((Unit)Baf.v().newThrowInst(), (Unit)ifinst);
                           units.insertAfter((Unit)Baf.v().newPushInst(NullConstant.v()), (Unit)ifinst);
                           Unit pop = Baf.v().newPopInst(RefType.v());
                           units.add((Unit)pop);
                           units.add((Unit)prev.clone());
                           b.getTraps().add(Baf.v().newTrap(ThrowSet.getRandomThrowable(), ifinst, sii, pop));
                           if (Rand.getInt() % 2 == 0) {
                              Unit pop = (Unit)pop.clone();
                              units.insertBefore((Unit)pop, (Unit)sii);
                              units.insertBefore((Unit)Baf.v().newGotoInst(sii), (Unit)pop);
                              units.add((Unit)Baf.v().newJSRInst(pop));
                           } else {
                              units.add((Unit)Baf.v().newGotoInst(sii));
                           }

                           done = true;
                           break;
                        }
                     }
                  }
               case 1:
                  if (!BodyBuilder.isExceptionCaughtAt(units, sii, b.getTraps().iterator())) {
                     throwThis = Baf.v().newThrowInst();
                     units.add((Unit)throwThis);
                     b.getTraps().add(Baf.v().newTrap(ThrowSet.getRandomThrowable(), sii, units.getSuccOf((Unit)sii), throwThis));
                     done = true;
                     break;
                  }
               case 2:
                  if (sii.getMethodRef().parameterTypes().size() == 0 && !BodyBuilder.isExceptionCaughtAt(units, sii, b.getTraps().iterator())) {
                     while(c != null) {
                        if (c.getName().equals("java.lang.Throwable")) {
                           throwThis = Baf.v().newThrowInst();
                           units.insertBefore((Unit)throwThis, (Unit)sii);
                           b.getTraps().add(Baf.v().newTrap(origClass, throwThis, sii, sii));
                           done = true;
                           break;
                        }

                        if (c.hasSuperclass()) {
                           c = c.getSuperclass();
                        } else {
                           c = null;
                        }
                     }
                  }

                  if (done) {
                     break;
                  }
               case 3:
                  Unit pop = Baf.v().newPopInst(RefType.v());
                  units.insertBefore((Unit)pop, (Unit)sii);
                  units.insertBeforeNoRedirect(Baf.v().newJSRInst(pop), pop);
                  done = true;
               }

               if (done) {
                  int var10002 = instances[rand]++;
                  ++count;
               }

               if (debug) {
                  StackTypeHeightCalculator.calculateStackHeights(b);
               }

            }
         }
      }
   }
}
