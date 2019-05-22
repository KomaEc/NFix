package polyglot.visit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import polyglot.ast.Block;
import polyglot.ast.Catch;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.Expr;
import polyglot.ast.Formal;
import polyglot.ast.Import;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.SourceFile;
import polyglot.ast.Stmt;
import polyglot.ast.TypeNode;
import polyglot.util.CodeWriter;

public class NodeScrambler extends NodeVisitor {
   public NodeScrambler.FirstPass fp = new NodeScrambler.FirstPass();
   protected HashMap pairs = new HashMap();
   protected LinkedList nodes = new LinkedList();
   protected LinkedList currentParents = new LinkedList();
   protected long seed;
   protected Random ran;
   protected boolean scrambled = false;
   protected CodeWriter cw;
   // $FF: synthetic field
   static Class class$polyglot$ast$Node;
   // $FF: synthetic field
   static Class class$polyglot$ast$Import;
   // $FF: synthetic field
   static Class class$polyglot$ast$TypeNode;
   // $FF: synthetic field
   static Class class$polyglot$ast$ClassDecl;
   // $FF: synthetic field
   static Class class$polyglot$ast$ClassMember;
   // $FF: synthetic field
   static Class class$polyglot$ast$Formal;
   // $FF: synthetic field
   static Class class$polyglot$ast$Expr;
   // $FF: synthetic field
   static Class class$polyglot$ast$Block;
   // $FF: synthetic field
   static Class class$polyglot$ast$Catch;
   // $FF: synthetic field
   static Class class$polyglot$ast$LocalDecl;
   // $FF: synthetic field
   static Class class$polyglot$ast$Stmt;

   public NodeScrambler() {
      this.cw = new CodeWriter(System.err, 72);
      Random ran = new Random();
      this.seed = ran.nextLong();
      System.err.println("Using seed: " + this.seed);
      this.ran = new Random(this.seed);
   }

   public NodeScrambler(long seed) {
      this.cw = new CodeWriter(System.err, 72);
      this.seed = seed;
      this.ran = new Random(seed);
   }

   public long getSeed() {
      return this.seed;
   }

   public Node override(Node n) {
      if (this.coinFlip()) {
         Node m = this.potentialScramble(n);
         if (m == null) {
            return null;
         } else {
            this.scrambled = true;

            try {
               System.err.println("Replacing:");
               n.dump(this.cw);
               this.cw.newline();
               this.cw.flush();
               System.err.println("With:");
               m.dump(this.cw);
               this.cw.newline();
               this.cw.flush();
               return m;
            } catch (Exception var4) {
               var4.printStackTrace();
               return null;
            }
         }
      } else {
         return null;
      }
   }

   protected boolean coinFlip() {
      if (this.scrambled) {
         return false;
      } else {
         return this.ran.nextDouble() > 0.9D;
      }
   }

   protected Node potentialScramble(Node n) {
      Class required = class$polyglot$ast$Node == null ? (class$polyglot$ast$Node = class$("polyglot.ast.Node")) : class$polyglot$ast$Node;
      if (n instanceof SourceFile) {
         return null;
      } else {
         if (n instanceof Import) {
            required = class$polyglot$ast$Import == null ? (class$polyglot$ast$Import = class$("polyglot.ast.Import")) : class$polyglot$ast$Import;
         } else if (n instanceof TypeNode) {
            required = class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode;
         } else if (n instanceof ClassDecl) {
            required = class$polyglot$ast$ClassDecl == null ? (class$polyglot$ast$ClassDecl = class$("polyglot.ast.ClassDecl")) : class$polyglot$ast$ClassDecl;
         } else if (n instanceof ClassMember) {
            required = class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember;
         } else if (n instanceof Formal) {
            required = class$polyglot$ast$Formal == null ? (class$polyglot$ast$Formal = class$("polyglot.ast.Formal")) : class$polyglot$ast$Formal;
         } else if (n instanceof Expr) {
            required = class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr;
         } else if (n instanceof Block) {
            required = class$polyglot$ast$Block == null ? (class$polyglot$ast$Block = class$("polyglot.ast.Block")) : class$polyglot$ast$Block;
         } else if (n instanceof Catch) {
            required = class$polyglot$ast$Catch == null ? (class$polyglot$ast$Catch = class$("polyglot.ast.Catch")) : class$polyglot$ast$Catch;
         } else if (n instanceof LocalDecl) {
            required = class$polyglot$ast$LocalDecl == null ? (class$polyglot$ast$LocalDecl = class$("polyglot.ast.LocalDecl")) : class$polyglot$ast$LocalDecl;
         } else if (n instanceof Stmt) {
            required = class$polyglot$ast$Stmt == null ? (class$polyglot$ast$Stmt = class$("polyglot.ast.Stmt")) : class$polyglot$ast$Stmt;
         }

         LinkedList parents = (LinkedList)this.pairs.get(n);
         Iterator iter1 = this.nodes.iterator();

         boolean isParent;
         Node m;
         do {
            do {
               if (!iter1.hasNext()) {
                  return null;
               }

               m = (Node)iter1.next();
            } while(!required.isAssignableFrom(m.getClass()));

            isParent = false;
            Iterator iter2 = parents.iterator();

            while(iter2.hasNext()) {
               if (m == iter2.next()) {
                  isParent = true;
               }
            }
         } while(isParent || m == n);

         return m;
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

   public class FirstPass extends NodeVisitor {
      public NodeVisitor enter(Node n) {
         NodeScrambler.this.pairs.put(n, NodeScrambler.this.currentParents.clone());
         NodeScrambler.this.nodes.add(n);
         NodeScrambler.this.currentParents.add(n);
         return this;
      }

      public Node leave(Node old, Node n, NodeVisitor v) {
         NodeScrambler.this.currentParents.remove(n);
         return n;
      }
   }
}
