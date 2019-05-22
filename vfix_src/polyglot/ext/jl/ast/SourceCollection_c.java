package polyglot.ext.jl.ast;

import java.util.Iterator;
import java.util.List;
import polyglot.ast.Node;
import polyglot.ast.SourceCollection;
import polyglot.ast.SourceFile;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;

public class SourceCollection_c extends Node_c implements SourceCollection {
   protected List sources;
   // $FF: synthetic field
   static Class class$polyglot$ast$SourceFile;

   public SourceCollection_c(Position pos, List sources) {
      super(pos);
      this.sources = TypedList.copyAndCheck(sources, class$polyglot$ast$SourceFile == null ? (class$polyglot$ast$SourceFile = class$("polyglot.ast.SourceFile")) : class$polyglot$ast$SourceFile, true);
   }

   public String toString() {
      return this.sources.toString();
   }

   public List sources() {
      return this.sources;
   }

   public SourceCollection sources(List sources) {
      SourceCollection_c n = (SourceCollection_c)this.copy();
      n.sources = TypedList.copyAndCheck(sources, class$polyglot$ast$SourceFile == null ? (class$polyglot$ast$SourceFile = class$("polyglot.ast.SourceFile")) : class$polyglot$ast$SourceFile, true);
      return n;
   }

   protected SourceCollection_c reconstruct(List sources) {
      if (!CollectionUtil.equals(sources, this.sources)) {
         SourceCollection_c n = (SourceCollection_c)this.copy();
         n.sources = TypedList.copyAndCheck(sources, class$polyglot$ast$SourceFile == null ? (class$polyglot$ast$SourceFile = class$("polyglot.ast.SourceFile")) : class$polyglot$ast$SourceFile, true);
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      List sources = this.visitList(this.sources, v);
      return this.reconstruct(sources);
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      Iterator i = this.sources.iterator();

      while(i.hasNext()) {
         SourceFile s = (SourceFile)i.next();
         this.print(s, w, tr);
         w.newline(0);
      }

   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
