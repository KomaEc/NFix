package pxb.android.axml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Axml extends AxmlVisitor {
   public List<Axml.Node> firsts = new ArrayList();
   public List<Axml.Ns> nses = new ArrayList();

   public void accept(AxmlVisitor visitor) {
      Iterator var2 = this.nses.iterator();

      while(var2.hasNext()) {
         Axml.Ns ns = (Axml.Ns)var2.next();
         ns.accept(visitor);
      }

      var2 = this.firsts.iterator();

      while(var2.hasNext()) {
         Axml.Node first = (Axml.Node)var2.next();
         first.accept(visitor);
      }

   }

   public NodeVisitor child(String ns, String name) {
      Axml.Node node = new Axml.Node();
      node.name = name;
      node.ns = ns;
      this.firsts.add(node);
      return node;
   }

   public void ns(String prefix, String uri, int ln) {
      Axml.Ns ns = new Axml.Ns();
      ns.prefix = prefix;
      ns.uri = uri;
      ns.ln = ln;
      this.nses.add(ns);
   }

   public static class Ns {
      public int ln;
      public String prefix;
      public String uri;

      public void accept(AxmlVisitor visitor) {
         visitor.ns(this.prefix, this.uri, this.ln);
      }
   }

   public static class Node extends NodeVisitor {
      public List<Axml.Node.Attr> attrs = new ArrayList();
      public List<Axml.Node> children = new ArrayList();
      public Integer ln;
      public String ns;
      public String name;
      public Axml.Node.Text text;

      public void accept(NodeVisitor nodeVisitor) {
         NodeVisitor nodeVisitor2 = nodeVisitor.child(this.ns, this.name);
         this.acceptB(nodeVisitor2);
         nodeVisitor2.end();
      }

      public void acceptB(NodeVisitor nodeVisitor) {
         if (this.text != null) {
            this.text.accept(nodeVisitor);
         }

         Iterator var2 = this.attrs.iterator();

         while(var2.hasNext()) {
            Axml.Node.Attr a = (Axml.Node.Attr)var2.next();
            a.accept(nodeVisitor);
         }

         if (this.ln != null) {
            nodeVisitor.line(this.ln);
         }

         var2 = this.children.iterator();

         while(var2.hasNext()) {
            Axml.Node c = (Axml.Node)var2.next();
            c.accept(nodeVisitor);
         }

      }

      public void attr(String ns, String name, int resourceId, int type, Object obj) {
         Axml.Node.Attr attr = new Axml.Node.Attr();
         attr.name = name;
         attr.ns = ns;
         attr.resourceId = resourceId;
         attr.type = type;
         attr.value = obj;
         this.attrs.add(attr);
      }

      public NodeVisitor child(String ns, String name) {
         Axml.Node node = new Axml.Node();
         node.name = name;
         node.ns = ns;
         this.children.add(node);
         return node;
      }

      public void line(int ln) {
         this.ln = ln;
      }

      public void text(int lineNumber, String value) {
         Axml.Node.Text text = new Axml.Node.Text();
         text.ln = lineNumber;
         text.text = value;
         this.text = text;
      }

      public static class Text {
         public int ln;
         public String text;

         public void accept(NodeVisitor nodeVisitor) {
            nodeVisitor.text(this.ln, this.text);
         }
      }

      public static class Attr {
         public String ns;
         public String name;
         public int resourceId;
         public int type;
         public Object value;

         public void accept(NodeVisitor nodeVisitor) {
            nodeVisitor.attr(this.ns, this.name, this.resourceId, this.type, this.value);
         }
      }
   }
}
