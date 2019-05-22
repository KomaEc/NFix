package soot.jimple.spark.ondemand.pautil;

import java.util.Iterator;
import java.util.Set;
import soot.jimple.spark.ondemand.genericutil.ArraySet;
import soot.jimple.spark.ondemand.genericutil.HashSetMultiMap;
import soot.jimple.spark.ondemand.genericutil.MultiMap;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.VarNode;
import soot.toolkits.scalar.Pair;

public class ValidMatches {
   private final MultiMap<VarNode, VarNode> vMatchEdges = new HashSetMultiMap();
   private final MultiMap<VarNode, VarNode> vMatchBarEdges = new HashSetMultiMap();

   public ValidMatches(PAG pag, SootUtil.FieldToEdgesMap fieldToStores) {
      Iterator iter = pag.loadSources().iterator();

      label31:
      while(iter.hasNext()) {
         FieldRefNode loadSource = (FieldRefNode)iter.next();
         SparkField field = loadSource.getField();
         VarNode loadBase = loadSource.getBase();
         ArraySet<Pair<VarNode, VarNode>> storesOnField = fieldToStores.get(field);
         Iterator var8 = storesOnField.iterator();

         while(true) {
            Pair store;
            VarNode storeBase;
            do {
               if (!var8.hasNext()) {
                  continue label31;
               }

               store = (Pair)var8.next();
               storeBase = (VarNode)store.getO2();
            } while(!loadBase.getP2Set().hasNonEmptyIntersection(storeBase.getP2Set()));

            VarNode matchSrc = (VarNode)store.getO1();
            Node[] loadTargets = pag.loadLookup(loadSource);

            for(int i = 0; i < loadTargets.length; ++i) {
               VarNode matchTgt = (VarNode)loadTargets[i];
               this.vMatchEdges.put(matchSrc, matchTgt);
               this.vMatchBarEdges.put(matchTgt, matchSrc);
            }
         }
      }

   }

   public Set<VarNode> vMatchLookup(VarNode src) {
      return this.vMatchEdges.get(src);
   }

   public Set<VarNode> vMatchInvLookup(VarNode src) {
      return this.vMatchBarEdges.get(src);
   }
}
