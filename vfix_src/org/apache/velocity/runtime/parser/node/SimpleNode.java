package org.apache.velocity.runtime.parser.node;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.runtime.parser.Token;

public class SimpleNode implements Node {
   protected RuntimeServices rsvc;
   protected Log log;
   protected Node parent;
   protected Node[] children;
   protected int id;
   protected Parser parser;
   protected int info;
   public boolean state;
   protected boolean invalid;
   protected Token first;
   protected Token last;

   public SimpleNode(int i) {
      this.rsvc = null;
      this.log = null;
      this.invalid = false;
      this.id = i;
   }

   public SimpleNode(Parser p, int i) {
      this(i);
      this.parser = p;
   }

   public void jjtOpen() {
      this.first = this.parser.getToken(1);
   }

   public void jjtClose() {
      this.last = this.parser.getToken(0);
   }

   public void setFirstToken(Token t) {
      this.first = t;
   }

   public Token getFirstToken() {
      return this.first;
   }

   public Token getLastToken() {
      return this.last;
   }

   public void jjtSetParent(Node n) {
      this.parent = n;
   }

   public Node jjtGetParent() {
      return this.parent;
   }

   public void jjtAddChild(Node n, int i) {
      if (this.children == null) {
         this.children = new Node[i + 1];
      } else if (i >= this.children.length) {
         Node[] c = new Node[i + 1];
         System.arraycopy(this.children, 0, c, 0, this.children.length);
         this.children = c;
      }

      this.children[i] = n;
   }

   public Node jjtGetChild(int i) {
      return this.children[i];
   }

   public int jjtGetNumChildren() {
      return this.children == null ? 0 : this.children.length;
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public Object childrenAccept(ParserVisitor visitor, Object data) {
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            this.children[i].jjtAccept(visitor, data);
         }
      }

      return data;
   }

   public String toString(String prefix) {
      return prefix + this.toString();
   }

   public void dump(String prefix) {
      System.out.println(this.toString(prefix));
      if (this.children != null) {
         for(int i = 0; i < this.children.length; ++i) {
            SimpleNode n = (SimpleNode)this.children[i];
            if (n != null) {
               n.dump(prefix + " ");
            }
         }
      }

   }

   public String literal() {
      Token t = this.first;
      StringBuffer sb = new StringBuffer(t.image);

      while(t != this.last) {
         t = t.next;
         sb.append(t.image);
      }

      return sb.toString();
   }

   public Object init(InternalContextAdapter context, Object data) throws TemplateInitException {
      this.rsvc = (RuntimeServices)data;
      this.log = this.rsvc.getLog();
      int k = this.jjtGetNumChildren();

      for(int i = 0; i < k; ++i) {
         this.jjtGetChild(i).init(context, data);
      }

      return data;
   }

   public boolean evaluate(InternalContextAdapter context) throws MethodInvocationException {
      return false;
   }

   public Object value(InternalContextAdapter context) throws MethodInvocationException {
      return null;
   }

   public boolean render(InternalContextAdapter context, Writer writer) throws IOException, MethodInvocationException, ParseErrorException, ResourceNotFoundException {
      int k = this.jjtGetNumChildren();

      for(int i = 0; i < k; ++i) {
         this.jjtGetChild(i).render(context, writer);
      }

      return true;
   }

   public Object execute(Object o, InternalContextAdapter context) throws MethodInvocationException {
      return null;
   }

   public int getType() {
      return this.id;
   }

   public void setInfo(int info) {
      this.info = info;
   }

   public int getInfo() {
      return this.info;
   }

   public void setInvalid() {
      this.invalid = true;
   }

   public boolean isInvalid() {
      return this.invalid;
   }

   public int getLine() {
      return this.first.beginLine;
   }

   public int getColumn() {
      return this.first.beginColumn;
   }

   public String toString() {
      StringBuffer tokens = new StringBuffer();

      for(Token t = this.getFirstToken(); t != null; t = t.next) {
         tokens.append("[").append(t.image).append("]");
         if (t.next != null) {
            if (t.equals(this.getLastToken())) {
               break;
            }

            tokens.append(", ");
         }
      }

      return (new ToStringBuilder(this)).append("id", this.getType()).append("info", this.getInfo()).append("invalid", this.isInvalid()).append("children", this.jjtGetNumChildren()).append("tokens", (Object)tokens).toString();
   }
}
