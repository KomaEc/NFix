package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.IdentityUnit;
import soot.Local;
import soot.PhaseOptions;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.GroupIntPair;
import soot.options.Options;
import soot.util.DeterministicHashMap;

public class LocalPacker extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(LocalPacker.class);

   public LocalPacker(Singletons.Global g) {
   }

   public static LocalPacker v() {
      return G.v().soot_toolkits_scalar_LocalPacker();
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      boolean isUnsplit = PhaseOptions.getBoolean(options, "unsplit-original-locals");
      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "] Packing locals...");
      }

      Map<Local, Object> localToGroup = new DeterministicHashMap(body.getLocalCount() * 2 + 1, 0.7F);
      Map<Object, Integer> groupToColorCount = new HashMap(body.getLocalCount() * 2 + 1, 0.7F);
      Map<Local, Integer> localToColor = new HashMap(body.getLocalCount() * 2 + 1, 0.7F);
      Iterator var9 = body.getLocals().iterator();

      while(var9.hasNext()) {
         Local l = (Local)var9.next();
         Type g = l.getType();
         localToGroup.put(l, g);
         if (!groupToColorCount.containsKey(g)) {
            groupToColorCount.put(g, 0);
         }
      }

      var9 = body.getUnits().iterator();

      Unit s;
      while(var9.hasNext()) {
         s = (Unit)var9.next();
         if (s instanceof IdentityUnit && ((IdentityUnit)s).getLeftOp() instanceof Local) {
            Local l = (Local)((IdentityUnit)s).getLeftOp();
            Object group = localToGroup.get(l);
            int count = (Integer)groupToColorCount.get(group);
            localToColor.put(l, new Integer(count));
            ++count;
            groupToColorCount.put(group, new Integer(count));
         }
      }

      if (isUnsplit) {
         FastColorer.unsplitAssignColorsToLocals(body, localToGroup, localToColor, groupToColorCount);
      } else {
         FastColorer.assignColorsToLocals(body, localToGroup, localToColor, groupToColorCount);
      }

      List<Local> originalLocals = new ArrayList(body.getLocals());
      Map<Local, Local> localToNewLocal = new HashMap(body.getLocalCount() * 2 + 1, 0.7F);
      Map<GroupIntPair, Local> groupIntToLocal = new HashMap(body.getLocalCount() * 2 + 1, 0.7F);
      body.getLocals().clear();
      Set<String> usedLocalNames = new HashSet();

      Local newLocal;
      Local l;
      for(Iterator var26 = originalLocals.iterator(); var26.hasNext(); localToNewLocal.put(l, newLocal)) {
         l = (Local)var26.next();
         Object group = localToGroup.get(l);
         int color = (Integer)localToColor.get(l);
         GroupIntPair pair = new GroupIntPair(group, color);
         if (groupIntToLocal.containsKey(pair)) {
            newLocal = (Local)groupIntToLocal.get(pair);
         } else {
            newLocal = (Local)l.clone();
            newLocal.setType((Type)group);
            int signIndex = newLocal.getName().indexOf("#");
            if (signIndex != -1) {
               String newName = newLocal.getName().substring(0, signIndex);
               if (usedLocalNames.add(newName)) {
                  newLocal.setName(newName);
               }
            }

            groupIntToLocal.put(pair, newLocal);
            body.getLocals().add(newLocal);
         }
      }

      var9 = body.getUnits().iterator();

      while(var9.hasNext()) {
         s = (Unit)var9.next();
         Iterator var25 = s.getUseBoxes().iterator();

         ValueBox box;
         while(var25.hasNext()) {
            box = (ValueBox)var25.next();
            if (box.getValue() instanceof Local) {
               l = (Local)box.getValue();
               box.setValue((Local)localToNewLocal.get(l));
            }
         }

         var25 = s.getDefBoxes().iterator();

         while(var25.hasNext()) {
            box = (ValueBox)var25.next();
            if (box.getValue() instanceof Local) {
               l = (Local)box.getValue();
               box.setValue((Local)localToNewLocal.get(l));
            }
         }
      }

   }
}
