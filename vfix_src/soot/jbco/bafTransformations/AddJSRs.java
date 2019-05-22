package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.RefType;
import soot.Trap;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.GotoInst;
import soot.baf.JSRInst;
import soot.baf.LookupSwitchInst;
import soot.baf.PopInst;
import soot.baf.TableSwitchInst;
import soot.baf.TargetArgInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;

public class AddJSRs extends BodyTransformer implements IJbcoTransform {
   private static final Logger logger = LoggerFactory.getLogger(AddJSRs.class);
   int jsrcount = 0;
   public static String[] dependancies = new String[]{"jtp.jbco_jl", "bb.jbco_cb2ji", "bb.jbco_ful", "bb.lp"};
   public static String name = "bb.jbco_cb2ji";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
      logger.info((String)"{} If/Gotos replaced with JSRs.", (Object)this.jsrcount);
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
      if (weight != 0) {
         boolean fallsthrough = false;
         HashMap<Trap, Unit> trapsToHandler = new HashMap();
         Iterator var7 = b.getTraps().iterator();

         while(var7.hasNext()) {
            Trap t = (Trap)var7.next();
            trapsToHandler.put(t, t.getHandlerUnit());
         }

         List<Unit> targets = new ArrayList();
         List<Unit> seenUts = new ArrayList();
         HashMap<Unit, List<Unit>> switches = new HashMap();
         HashMap<Unit, Unit> switchDefs = new HashMap();
         HashMap<TargetArgInst, Unit> ignoreJumps = new HashMap();
         PatchingChain<Unit> u = b.getUnits();

         Iterator it;
         Unit unit;
         for(it = u.snapshotIterator(); it.hasNext(); seenUts.add(unit)) {
            unit = (Unit)it.next();
            if (unit instanceof TargetArgInst) {
               TargetArgInst ti = (TargetArgInst)unit;
               Unit tu = ti.getTarget();
               if (Rand.getInt(10) > weight) {
                  ignoreJumps.put(ti, tu);
               } else if (!targets.contains(tu)) {
                  targets.add(tu);
               }
            }

            if (unit instanceof TableSwitchInst) {
               TableSwitchInst ts = (TableSwitchInst)unit;
               switches.put(unit, new ArrayList(ts.getTargets()));
               switchDefs.put(unit, ts.getDefaultTarget());
            } else if (unit instanceof LookupSwitchInst) {
               LookupSwitchInst ls = (LookupSwitchInst)unit;
               switches.put(unit, new ArrayList(ls.getTargets()));
               switchDefs.put(unit, ls.getDefaultTarget());
            }
         }

         it = u.snapshotIterator();
         ArrayList<Unit> processedLabels = new ArrayList();
         HashMap<Unit, JSRInst> builtJsrs = new HashMap();
         HashMap<Unit, Unit> popsBuilt = new HashMap();

         Unit sw;
         for(Unit prev = null; it.hasNext(); prev = sw) {
            sw = (Unit)it.next();
            if (targets.contains(sw)) {
               if (fallsthrough) {
                  JSRInst ji = Baf.v().newJSRInst(sw);
                  builtJsrs.put(sw, ji);
                  u.insertAfter((Unit)ji, (Unit)prev);
                  ++this.jsrcount;
               }

               PopInst pop = Baf.v().newPopInst(RefType.v());
               u.insertBefore((Unit)pop, (Unit)sw);
               processedLabels.add(sw);
               popsBuilt.put(pop, sw);
            }

            fallsthrough = sw.fallsThrough();
         }

         it = u.snapshotIterator();

         Unit def;
         TargetArgInst ti;
         while(it.hasNext()) {
            sw = (Unit)it.next();
            if (!builtJsrs.containsValue(sw) && sw instanceof TargetArgInst && !ignoreJumps.containsKey(sw)) {
               ti = (TargetArgInst)sw;
               def = ti.getTarget();
               if (!popsBuilt.containsKey(def)) {
                  throw new RuntimeException("It appears a target was found that was not updated with a POP.\n\"This makes no sense,\" said the bug as it flew through the code.");
               }

               JSRInst ji = (JSRInst)builtJsrs.get(popsBuilt.get(def));
               if (BodyBuilder.isBafIf(sw)) {
                  if (Rand.getInt(10) > weight) {
                     ti.setTarget((Unit)popsBuilt.get(def));
                  } else if (ji != null) {
                     ti.setTarget(ji);
                  } else {
                     ji = Baf.v().newJSRInst(def);
                     u.insertAfter((Unit)ji, (Unit)u.getPredOf(def));
                     ti.setTarget(ji);
                     builtJsrs.put(popsBuilt.get(def), ji);
                     ++this.jsrcount;
                  }
               } else if (sw instanceof GotoInst) {
                  if (ji != null) {
                     if (Rand.getInt(10) < weight) {
                        ((GotoInst)sw).setTarget(ji);
                     } else {
                        ((GotoInst)sw).setTarget((Unit)popsBuilt.get(def));
                     }
                  } else {
                     ((GotoInst)sw).setTarget((Unit)popsBuilt.get(def));
                  }
               }
            }
         }

         Iterator var30 = trapsToHandler.keySet().iterator();

         while(var30.hasNext()) {
            Trap t = (Trap)var30.next();
            t.setHandlerUnit((Unit)trapsToHandler.get(t));
         }

         var30 = ignoreJumps.keySet().iterator();

         while(var30.hasNext()) {
            ti = (TargetArgInst)var30.next();
            if (popsBuilt.containsKey(ti.getTarget())) {
               ti.setTarget((Unit)popsBuilt.get(ti.getTarget()));
            }
         }

         targets.clear();
         it = u.snapshotIterator();

         while(it.hasNext()) {
            sw = (Unit)it.next();
            if (sw instanceof TargetArgInst) {
               Unit targ = ((TargetArgInst)sw).getTarget();
               if (!targets.contains(targ)) {
                  targets.add(targ);
               }
            }
         }

         it = popsBuilt.keySet().iterator();

         while(it.hasNext()) {
            sw = (Unit)it.next();
            if (!targets.contains(sw)) {
               u.remove(sw);
            }
         }

         it = switches.keySet().iterator();

         while(it.hasNext()) {
            sw = (Unit)it.next();
            List<Unit> targs = (List)switches.get(sw);

            for(int i = 0; i < targs.size(); ++i) {
               if (Rand.getInt(10) <= weight) {
                  Unit unit = (Unit)targs.get(i);
                  Unit ji = (Unit)builtJsrs.get(unit);
                  if (ji != null) {
                     targs.set(i, ji);
                  }
               }
            }

            def = (Unit)switchDefs.get(sw);
            if (Rand.getInt(10) < weight && builtJsrs.get(def) != null) {
               def = (Unit)builtJsrs.get(def);
            }

            if (sw instanceof TableSwitchInst) {
               ((TableSwitchInst)sw).setTargets(targs);
               ((TableSwitchInst)sw).setDefaultTarget(def);
            } else if (sw instanceof LookupSwitchInst) {
               ((LookupSwitchInst)sw).setTargets(targs);
               ((LookupSwitchInst)sw).setDefaultTarget(def);
            }
         }

         if (debug) {
            StackTypeHeightCalculator.calculateStackHeights(b);
         }

      }
   }
}
