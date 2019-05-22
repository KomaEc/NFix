package soot.toolkits.graph.pdg;

import java.util.ArrayList;
import java.util.List;
import soot.toolkits.graph.Block;

public class PDGNode {
   protected PDGNode.Type m_type;
   protected Object m_node = null;
   protected List<PDGNode> m_dependents = new ArrayList();
   protected List<PDGNode> m_backDependents = new ArrayList();
   protected PDGNode m_next = null;
   protected PDGNode m_prev = null;
   protected PDGNode.Attribute m_attrib;
   protected boolean m_visited;

   public PDGNode(Object obj, PDGNode.Type t) {
      this.m_attrib = PDGNode.Attribute.NORMAL;
      this.m_visited = false;
      this.m_node = obj;
      this.m_type = t;
   }

   public PDGNode.Type getType() {
      return this.m_type;
   }

   public void setType(PDGNode.Type t) {
      this.m_type = t;
   }

   public Object getNode() {
      return this.m_node;
   }

   public void setNext(PDGNode n) {
      this.m_next = n;
   }

   public PDGNode getNext() {
      return this.m_next;
   }

   public void setPrev(PDGNode n) {
      this.m_prev = n;
   }

   public PDGNode getPrev() {
      return this.m_prev;
   }

   public void setVisited(boolean v) {
      this.m_visited = v;
   }

   public boolean getVisited() {
      return this.m_visited;
   }

   public void setNode(Object obj) {
      this.m_node = obj;
   }

   public PDGNode.Attribute getAttrib() {
      return this.m_attrib;
   }

   public void setAttrib(PDGNode.Attribute a) {
      this.m_attrib = a;
   }

   public void addDependent(PDGNode node) {
      if (!this.m_dependents.contains(node)) {
         this.m_dependents.add(node);
      }

   }

   public void addBackDependent(PDGNode node) {
      this.m_backDependents.add(node);
   }

   public void removeDependent(PDGNode node) {
      this.m_dependents.remove(node);
   }

   public List<PDGNode> getDependents() {
      return this.m_dependents;
   }

   public List<PDGNode> getBackDependets() {
      return this.m_backDependents;
   }

   public String toString() {
      new String();
      String s = "Type: " + (this.m_type == PDGNode.Type.REGION ? "REGION: " : "CFGNODE: ");
      s = s + this.m_node;
      return s;
   }

   public String toShortString() {
      new String();
      String s = "Type: " + (this.m_type == PDGNode.Type.REGION ? "REGION: " : "CFGNODE: ");
      if (this.m_type == PDGNode.Type.REGION) {
         s = s + ((IRegion)this.m_node).getID();
      } else {
         s = s + ((Block)this.m_node).toShortString();
      }

      return s;
   }

   public static enum Attribute {
      NORMAL,
      ENTRY,
      CONDHEADER,
      LOOPHEADER;
   }

   public static enum Type {
      REGION,
      CFGNODE;
   }
}
