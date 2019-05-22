package soot.toolkits.graph.pdg;

public class LoopedPDGNode extends PDGNode {
   protected PDGNode m_header = null;
   protected PDGNode m_body = null;

   public LoopedPDGNode(Region obj, PDGNode.Type t, PDGNode c) {
      super(obj, t);
      this.m_header = c;
   }

   public PDGNode getHeader() {
      return this.m_header;
   }

   public void setBody(PDGNode b) {
      this.m_body = b;
   }

   public PDGNode getBody() {
      return this.m_body;
   }
}
