package soot.dava.toolkits.base.finders;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.toolkits.graph.DirectedGraph;

class SwitchNodeGraph implements DirectedGraph {
   private LinkedList body = new LinkedList();
   private final LinkedList heads;
   private final LinkedList tails;
   private final HashMap binding;

   public SwitchNodeGraph(List body) {
      this.body.addAll(body);
      this.binding = new HashMap();
      this.heads = new LinkedList();
      this.tails = new LinkedList();
      Iterator it = body.iterator();

      SwitchNode sn;
      while(it.hasNext()) {
         sn = (SwitchNode)it.next();
         this.binding.put(sn.get_AugStmt().bsuccs.get(0), sn);
         sn.reset();
      }

      it = body.iterator();

      while(it.hasNext()) {
         ((SwitchNode)it.next()).setup_Graph(this.binding);
      }

      it = body.iterator();

      while(it.hasNext()) {
         sn = (SwitchNode)it.next();
         if (sn.get_Preds().isEmpty()) {
            this.heads.add(sn);
         }

         if (sn.get_Succs().isEmpty()) {
            this.tails.add(sn);
         }
      }

   }

   public int size() {
      return this.body.size();
   }

   public List getHeads() {
      return this.heads;
   }

   public List getTails() {
      return this.tails;
   }

   public List getPredsOf(Object o) {
      return ((SwitchNode)o).get_Preds();
   }

   public List getSuccsOf(Object o) {
      return ((SwitchNode)o).get_Succs();
   }

   public Iterator iterator() {
      return this.body.iterator();
   }

   public List getBody() {
      return this.body;
   }
}
