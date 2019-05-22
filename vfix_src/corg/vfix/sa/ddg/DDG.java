package corg.vfix.sa.ddg;

import corg.vfix.sa.analysis.AliasQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.internal.JInstanceFieldRef;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

public class DDG {
   private ArrayList<DDGNode> nodes;
   private ArrayList<DDGEdge> edges;
   private UnitGraph cfg;
   private Map<UnitValuePair, ArrayList<Unit>> uv2def;
   private Map<UnitValuePair, ArrayList<Unit>> uv2use;
   private Map<Unit, ArrayList<UnitValuePair>> u2vdef;
   private Map<Unit, ArrayList<UnitValuePair>> u2vuse;
   private Body currentBody;

   public DDG(Body body) {
      this.currentBody = body;
      this.nodes = new ArrayList();
      this.edges = new ArrayList();
      this.cfg = new BriefUnitGraph(body);
      Iterator var3 = body.getUnits().iterator();

      while(var3.hasNext()) {
         Unit unit = (Unit)var3.next();
         this.nodes.add(new DDGNode(unit));
      }

      var3 = this.nodes.iterator();

      label44:
      while(var3.hasNext()) {
         DDGNode node1 = (DDGNode)var3.next();
         ArrayList<Value> defs = node1.getDefs();
         Iterator var6 = this.nodes.iterator();

         while(true) {
            DDGNode node2;
            do {
               if (!var6.hasNext()) {
                  continue label44;
               }

               node2 = (DDGNode)var6.next();
            } while(node1.equals(node2));

            ArrayList<Value> uses = node2.getUses();
            Iterator var9 = defs.iterator();

            while(var9.hasNext()) {
               Value def = (Value)var9.next();
               Iterator var11 = uses.iterator();

               while(var11.hasNext()) {
                  Value use = (Value)var11.next();
                  if (this.equal(def, use) && this.immediateDefUse(node1, node2, def)) {
                     this.edges.add(new DDGEdge(node1, node2, def));
                  }
               }
            }
         }
      }

      this.buildUVDefUse();
   }

   private void buildUVDefUse() {
      this.buildUV2Def();
      this.buildUV2Use();
      this.buildU2VDef();
      this.buildU2VUse();
   }

   public DDGNode getNodeByUnit(Unit unit) {
      Iterator var3 = this.nodes.iterator();

      while(var3.hasNext()) {
         DDGNode node = (DDGNode)var3.next();
         if (node.getUnit().equals(unit)) {
            return node;
         }
      }

      return null;
   }

   private boolean equal(Value def, Value use) {
      if (def instanceof Local && use instanceof Local) {
         return def.equals(use);
      } else if (def instanceof JInstanceFieldRef && use instanceof JInstanceFieldRef) {
         JInstanceFieldRef iDef = (JInstanceFieldRef)def;
         JInstanceFieldRef iUse = (JInstanceFieldRef)use;
         return AliasQuery.isAlias(iDef.getBase(), iUse.getBase()) && iDef.getField().equals(iUse.getField());
      } else {
         return false;
      }
   }

   public boolean immediateDefUse(Unit unit1, Unit unit2, Value value) {
      Stack<Unit> stack = new Stack();
      stack.push(unit1);
      return this.findPath(unit1, value, unit2, stack);
   }

   private boolean immediateDefUse(DDGNode node1, DDGNode node2, Value def) {
      Stack<Unit> stack = new Stack();
      stack.push(node1.getUnit());
      return this.findPath(node1.getUnit(), def, node2.getUnit(), stack);
   }

   private boolean findPath(Unit unit1, Value value, Unit unit2, Stack<Unit> stack) {
      Iterator var6 = this.cfg.getSuccsOf(unit1).iterator();

      while(var6.hasNext()) {
         Unit unit = (Unit)var6.next();
         if (unit.equals(unit2)) {
            return true;
         }

         if (!stack.contains(unit) && !this.isOverride(unit, value)) {
            stack.push(unit);
            if (this.findPath(unit, value, unit2, stack)) {
               return true;
            }
         }
      }

      stack.pop();
      return false;
   }

   private boolean isOverride(Unit unit, Value value) {
      DDGNode node = new DDGNode(unit);
      ArrayList<Value> defs = node.getDefs();
      Iterator var6 = defs.iterator();

      while(var6.hasNext()) {
         Value def = (Value)var6.next();
         if (this.equal(def, value)) {
            return true;
         }
      }

      return false;
   }

   public ArrayList<DDGNode> getNodes() {
      return this.nodes;
   }

   public ArrayList<DDGEdge> getEdges() {
      return this.edges;
   }

   private void buildUV2Def() {
      this.uv2def = new HashMap();
      Iterator var2 = this.edges.iterator();

      while(var2.hasNext()) {
         DDGEdge edge = (DDGEdge)var2.next();
         UnitValuePair key = new UnitValuePair(edge.getToUnit(), edge.getValue());
         ArrayList l;
         if (this.uv2def.containsKey(key)) {
            l = (ArrayList)this.uv2def.get(key);
         } else {
            l = new ArrayList();
            this.uv2def.put(key, l);
         }

         Unit from = edge.getFromUnit();
         if (!l.contains(from)) {
            l.add(from);
         }
      }

   }

   private void buildUV2Use() {
      this.uv2use = new HashMap();
      Iterator var2 = this.edges.iterator();

      while(var2.hasNext()) {
         DDGEdge edge = (DDGEdge)var2.next();
         UnitValuePair key = new UnitValuePair(edge.getFromUnit(), edge.getValue());
         ArrayList l;
         if (this.uv2use.containsKey(key)) {
            l = (ArrayList)this.uv2use.get(key);
         } else {
            l = new ArrayList();
            this.uv2use.put(key, l);
         }

         Unit to = edge.getToUnit();
         if (!l.contains(to)) {
            l.add(to);
         }
      }

   }

   private void buildU2VDef() {
      this.u2vdef = new HashMap();
      Iterator var2 = this.edges.iterator();

      while(var2.hasNext()) {
         DDGEdge edge = (DDGEdge)var2.next();
         Unit key = edge.getToUnit();
         ArrayList l;
         if (this.u2vdef.containsKey(key)) {
            l = (ArrayList)this.u2vdef.get(key);
         } else {
            l = new ArrayList();
            this.u2vdef.put(key, l);
         }

         UnitValuePair pair = new UnitValuePair(edge.getFromUnit(), edge.getValue());
         if (!l.contains(pair)) {
            l.add(pair);
         }
      }

   }

   public Body getBody() {
      return this.currentBody;
   }

   private void buildU2VUse() {
      this.u2vuse = new HashMap();
      Iterator var2 = this.edges.iterator();

      while(var2.hasNext()) {
         DDGEdge edge = (DDGEdge)var2.next();
         Unit key = edge.getFromUnit();
         ArrayList l;
         if (this.u2vuse.containsKey(key)) {
            l = (ArrayList)this.u2vuse.get(key);
         } else {
            l = new ArrayList();
            this.u2vuse.put(key, l);
         }

         UnitValuePair pair = new UnitValuePair(edge.getToUnit(), edge.getValue());
         if (!l.contains(pair)) {
            l.add(pair);
         }
      }

   }

   public ArrayList<Unit> getDefOf(Unit unit, Value value) {
      UnitValuePair key = new UnitValuePair(unit, value);
      if (this.uv2def == null) {
         return new ArrayList();
      } else {
         ArrayList<Unit> units = (ArrayList)this.uv2def.get(key);
         return units == null ? new ArrayList() : units;
      }
   }

   public ArrayList<Unit> getUseOf(Unit unit, Value value) {
      UnitValuePair key = new UnitValuePair(unit, value);
      if (this.uv2use == null) {
         return new ArrayList();
      } else {
         ArrayList<Unit> units = (ArrayList)this.uv2use.get(key);
         return units == null ? new ArrayList() : units;
      }
   }

   public ArrayList<Unit> getDefOf(Unit unit) {
      ArrayList<Unit> units = new ArrayList();
      if (this.u2vdef == null) {
         return units;
      } else {
         ArrayList<UnitValuePair> uvpairs = (ArrayList)this.u2vdef.get(unit);
         if (uvpairs == null) {
            return units;
         } else {
            Iterator var5 = uvpairs.iterator();

            while(var5.hasNext()) {
               UnitValuePair pair = (UnitValuePair)var5.next();
               Unit u = pair.getUnit();
               if (!units.contains(u)) {
                  units.add(u);
               }
            }

            return units;
         }
      }
   }

   public ArrayList<Unit> getUseOf(Unit unit) {
      ArrayList<Unit> units = new ArrayList();
      if (this.u2vuse == null) {
         return units;
      } else {
         ArrayList<UnitValuePair> uvpairs = (ArrayList)this.u2vuse.get(unit);
         if (uvpairs == null) {
            return units;
         } else {
            Iterator var5 = uvpairs.iterator();

            while(var5.hasNext()) {
               UnitValuePair pair = (UnitValuePair)var5.next();
               Unit u = pair.getUnit();
               if (!units.contains(u)) {
                  units.add(u);
               }
            }

            return units;
         }
      }
   }

   public ArrayList<DDGNode> getUseByUnit() {
      ArrayList<DDGNode> uses = new ArrayList();
      return uses;
   }

   public void printUV2Def() {
      Iterator var2 = this.uv2def.keySet().iterator();

      while(var2.hasNext()) {
         UnitValuePair key = (UnitValuePair)var2.next();
         System.out.println(key + ": ");
         System.out.println(this.uv2def.get(key) + "\n");
      }

   }
}
