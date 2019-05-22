package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.ast.Formal;
import polyglot.ast.Local;
import polyglot.ast.LocalDecl;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.util.IdentityKey;
import polyglot.visit.NodeVisitor;

public class LocalUsesChecker extends NodeVisitor {
   private final ArrayList<IdentityKey> locals = new ArrayList();
   private final ArrayList<IdentityKey> localDecls = new ArrayList();
   private final ArrayList<Node> news = new ArrayList();

   public ArrayList<IdentityKey> getLocals() {
      return this.locals;
   }

   public ArrayList<Node> getNews() {
      return this.news;
   }

   public ArrayList<IdentityKey> getLocalDecls() {
      return this.localDecls;
   }

   public Node leave(Node old, Node n, NodeVisitor visitor) {
      if (n instanceof Local && !this.locals.contains(new IdentityKey(((Local)n).localInstance())) && !((Local)n).isConstant()) {
         this.locals.add(new IdentityKey(((Local)n).localInstance()));
      }

      if (n instanceof LocalDecl) {
         this.localDecls.add(new IdentityKey(((LocalDecl)n).localInstance()));
      }

      if (n instanceof Formal) {
         this.localDecls.add(new IdentityKey(((Formal)n).localInstance()));
      }

      if (n instanceof New) {
         this.news.add(n);
      }

      return n;
   }
}
