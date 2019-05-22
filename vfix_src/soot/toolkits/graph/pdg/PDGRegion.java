package soot.toolkits.graph.pdg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.options.Options;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.UnitGraph;

public class PDGRegion implements IRegion, Iterable<PDGNode> {
   private static final Logger logger = LoggerFactory.getLogger(PDGRegion.class);
   private SootClass m_class;
   private SootMethod m_method;
   private List<PDGNode> m_nodes;
   private List<Unit> m_units;
   private LinkedHashMap<Unit, PDGNode> m_unit2pdgnode;
   private int m_id;
   private UnitGraph m_unitGraph;
   private PDGNode m_corrspondingPDGNode;
   private IRegion m_parent;
   private List<IRegion> m_children;

   public PDGRegion(int id, SootMethod m, SootClass c, UnitGraph ug, PDGNode node) {
      this(id, new ArrayList(), m, c, ug, node);
   }

   public PDGRegion(int id, List<PDGNode> nodes, SootMethod m, SootClass c, UnitGraph ug, PDGNode node) {
      this.m_class = null;
      this.m_method = null;
      this.m_nodes = null;
      this.m_units = null;
      this.m_unit2pdgnode = null;
      this.m_id = -1;
      this.m_unitGraph = null;
      this.m_corrspondingPDGNode = null;
      this.m_parent = null;
      this.m_children = new ArrayList();
      this.m_nodes = nodes;
      this.m_id = id;
      this.m_method = m;
      this.m_class = c;
      this.m_unitGraph = ug;
      this.m_units = null;
      this.m_corrspondingPDGNode = node;
      if (Options.v().verbose()) {
         logger.debug("New pdg region create: " + id);
      }

   }

   public PDGRegion(PDGNode node) {
      this(((IRegion)node.getNode()).getID(), new ArrayList(), ((IRegion)node.getNode()).getSootMethod(), ((IRegion)node.getNode()).getSootClass(), ((IRegion)node.getNode()).getUnitGraph(), node);
   }

   public PDGNode getCorrespondingPDGNode() {
      return this.m_corrspondingPDGNode;
   }

   public Object clone() {
      PDGRegion r = new PDGRegion(this.m_id, this.m_method, this.m_class, this.m_unitGraph, this.m_corrspondingPDGNode);
      r.m_nodes = (List)((ArrayList)this.m_nodes).clone();
      return r;
   }

   public SootMethod getSootMethod() {
      return this.m_method;
   }

   public SootClass getSootClass() {
      return this.m_class;
   }

   public List<PDGNode> getNodes() {
      return this.m_nodes;
   }

   public UnitGraph getUnitGraph() {
      return this.m_unitGraph;
   }

   public Iterator<PDGNode> iterator() {
      return new PDGRegion.ChildPDGFlowIterator(this.m_nodes);
   }

   public List<Unit> getUnits() {
      if (this.m_units == null) {
         this.m_units = new LinkedList();
         this.m_unit2pdgnode = new LinkedHashMap();
         Iterator itr = this.iterator();

         while(true) {
            PDGNode node;
            label37:
            do {
               while(itr.hasNext()) {
                  node = (PDGNode)itr.next();
                  if (node.getType() == PDGNode.Type.REGION) {
                     continue label37;
                  }

                  if (node.getType() != PDGNode.Type.CFGNODE) {
                     throw new RuntimeException("Exception in PDGRegion.getUnits: PDGNode's type is undefined!");
                  }

                  Block b = (Block)node.getNode();
                  Iterator itr1 = b.iterator();

                  while(itr1.hasNext()) {
                     Unit u = (Unit)itr1.next();
                     ((LinkedList)this.m_units).addLast(u);
                     this.m_unit2pdgnode.put(u, node);
                  }
               }

               return this.m_units;
            } while(!(node instanceof LoopedPDGNode));

            LoopedPDGNode n = (LoopedPDGNode)node;
            PDGNode header = n.getHeader();
            Block headerBlock = (Block)header.getNode();
            Iterator itr1 = headerBlock.iterator();

            while(itr1.hasNext()) {
               Unit u = (Unit)itr1.next();
               ((LinkedList)this.m_units).addLast(u);
               this.m_unit2pdgnode.put(u, header);
            }
         }
      } else {
         return this.m_units;
      }
   }

   public PDGNode unit2PDGNode(Unit u) {
      return this.m_unit2pdgnode.containsKey(u) ? (PDGNode)this.m_unit2pdgnode.get(u) : null;
   }

   public List<Unit> getUnits(Unit from, Unit to) {
      return this.m_units.subList(this.m_units.indexOf(from), this.m_units.indexOf(to));
   }

   public Unit getLast() {
      return this.m_units != null && this.m_units.size() > 0 ? (Unit)((LinkedList)this.m_units).getLast() : null;
   }

   public Unit getFirst() {
      return this.m_units != null && this.m_units.size() > 0 ? (Unit)((LinkedList)this.m_units).getFirst() : null;
   }

   public List<Block> getBlocks() {
      return new ArrayList();
   }

   public void addPDGNode(PDGNode node) {
      this.m_nodes.add(node);
   }

   public int getID() {
      return this.m_id;
   }

   public boolean occursBefore(Unit u1, Unit u2) {
      int i = this.m_units.lastIndexOf(u1);
      int j = this.m_units.lastIndexOf(u2);
      if (i != -1 && j != -1) {
         return i < j;
      } else {
         throw new RuntimeException("These units don't exist in the region!");
      }
   }

   public void setParent(IRegion pr) {
      this.m_parent = pr;
   }

   public IRegion getParent() {
      return this.m_parent;
   }

   public void addChildRegion(IRegion chr) {
      if (!this.m_children.contains(chr)) {
         this.m_children.add(chr);
      }

   }

   public List<IRegion> getChildRegions() {
      return this.m_children;
   }

   public String toString() {
      String str = new String();
      str = str + "Begin-----------PDGRegion:  " + this.m_id + "-------------\n";
      if (this.m_parent != null) {
         str = str + "Parent is: " + this.m_parent.getID() + "----\n";
      }

      str = str + "Children Regions are: ";

      for(Iterator ritr = this.m_children.iterator(); ritr.hasNext(); str = str + ((IRegion)ritr.next()).getID() + ", ") {
      }

      str = str + "\nUnits are: \n";
      List<Unit> regionUnits = this.getUnits();

      Unit u;
      for(Iterator itr = regionUnits.iterator(); itr.hasNext(); str = str + u + "\n") {
         u = (Unit)itr.next();
      }

      str = str + "End of PDG Region " + this.m_id + " -----------------------------\n";
      return str;
   }

   class ChildPDGFlowIterator implements Iterator<PDGNode> {
      List<PDGNode> m_list = null;
      PDGNode m_current = null;
      boolean beginning = true;

      public ChildPDGFlowIterator(List<PDGNode> list) {
         this.m_list = list;
      }

      public boolean hasNext() {
         if (this.beginning && this.m_list.size() > 0) {
            return true;
         } else {
            return this.m_current != null && this.m_current.getNext() != null;
         }
      }

      public PDGNode next() {
         if (!this.beginning) {
            if (!this.hasNext()) {
               throw new RuntimeException("No more nodes!");
            } else {
               this.m_current = this.m_current.getNext();
               return this.m_current;
            }
         } else {
            this.beginning = false;

            for(this.m_current = (PDGNode)this.m_list.get(0); this.m_current.getPrev() != null; this.m_current = this.m_current.getPrev()) {
            }

            if (this.m_current.getType() != PDGNode.Type.CFGNODE && this.m_current.getAttrib() != PDGNode.Attribute.LOOPHEADER) {
               Iterator depItr = this.m_list.iterator();

               PDGNode dep;
               do {
                  if (!depItr.hasNext()) {
                     return this.m_current;
                  }

                  dep = (PDGNode)depItr.next();
               } while(dep.getType() != PDGNode.Type.CFGNODE && dep.getAttrib() != PDGNode.Attribute.LOOPHEADER);

               for(this.m_current = dep; this.m_current.getPrev() != null; this.m_current = this.m_current.getPrev()) {
               }
            }

            return this.m_current;
         }
      }

      public void remove() {
      }
   }
}
