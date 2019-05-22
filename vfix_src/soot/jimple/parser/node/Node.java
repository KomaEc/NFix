package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class Node implements Switchable, Cloneable {
   private Node parent;

   public abstract Object clone();

   public Node parent() {
      return this.parent;
   }

   void parent(Node parent) {
      this.parent = parent;
   }

   abstract void removeChild(Node var1);

   abstract void replaceChild(Node var1, Node var2);

   public void replaceBy(Node node) {
      this.parent.replaceChild(this, node);
   }

   protected String toString(Node node) {
      return node != null ? node.toString() : "";
   }

   protected String toString(List<?> list) {
      StringBuffer s = new StringBuffer();
      Iterator i = list.iterator();

      while(i.hasNext()) {
         s.append(i.next());
      }

      return s.toString();
   }

   protected <T extends Node> T cloneNode(T node) {
      return node != null ? (Node)node.clone() : null;
   }

   protected <T extends Node> List<T> cloneList(List<T> list) {
      List<T> clone = new LinkedList();
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         T n = (Node)var3.next();
         clone.add((Node)n.clone());
      }

      return clone;
   }
}
