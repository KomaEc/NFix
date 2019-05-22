package polyglot.ext.jl.ast;

import polyglot.ast.Ext;
import polyglot.ast.Node;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.StringUtil;

public abstract class Ext_c implements Ext {
   protected Node node;
   protected Ext ext;

   public Ext_c(Ext ext) {
      this.node = null;
      this.ext = ext;
   }

   public Ext_c() {
      this((Ext)null);
      this.node = null;
   }

   public void init(Node node) {
      if (this.node != null) {
         throw new InternalCompilerError("Already initialized.");
      } else {
         this.node = node;
         if (this.ext != null) {
            this.ext.init(node);
         }

      }
   }

   public Node node() {
      return this.node;
   }

   public Ext ext() {
      return this.ext;
   }

   public Ext ext(Ext ext) {
      Ext old = this.ext;
      this.ext = null;
      Ext_c copy = (Ext_c)this.copy();
      copy.ext = ext;
      this.ext = old;
      return copy;
   }

   public Object copy() {
      try {
         Ext_c copy = (Ext_c)super.clone();
         if (this.ext != null) {
            copy.ext = (Ext)this.ext.copy();
         }

         copy.node = null;
         return copy;
      } catch (CloneNotSupportedException var2) {
         throw new InternalCompilerError("Java clone() weirdness.");
      }
   }

   public String toString() {
      return StringUtil.getShortNameComponent(this.getClass().getName());
   }

   public void dump(CodeWriter w) {
      w.write(this.toString());
   }
}
