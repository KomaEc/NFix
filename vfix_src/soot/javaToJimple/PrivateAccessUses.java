package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.ast.Call;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Field;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.ProcedureInstance;
import polyglot.util.IdentityKey;
import polyglot.visit.NodeVisitor;

public class PrivateAccessUses extends NodeVisitor {
   private final ArrayList<IdentityKey> list = new ArrayList();
   private ArrayList avail;

   public ArrayList<IdentityKey> getList() {
      return this.list;
   }

   public void avail(ArrayList list) {
      this.avail = list;
   }

   public Node leave(Node old, Node n, NodeVisitor visitor) {
      if (n instanceof Field) {
         FieldInstance fi = ((Field)n).fieldInstance();
         if (this.avail.contains(new IdentityKey(fi))) {
            this.list.add(new IdentityKey(fi));
         }
      }

      if (n instanceof Call) {
         ProcedureInstance pi = ((Call)n).methodInstance();
         if (this.avail.contains(new IdentityKey(pi))) {
            this.list.add(new IdentityKey(pi));
         }
      }

      ConstructorInstance pi;
      if (n instanceof New) {
         pi = ((New)n).constructorInstance();
         if (this.avail.contains(new IdentityKey(pi))) {
            this.list.add(new IdentityKey(pi));
         }
      }

      if (n instanceof ConstructorCall) {
         pi = ((ConstructorCall)n).constructorInstance();
         if (this.avail.contains(new IdentityKey(pi))) {
            this.list.add(new IdentityKey(pi));
         }
      }

      return n;
   }
}
