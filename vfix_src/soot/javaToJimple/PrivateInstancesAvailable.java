package soot.javaToJimple;

import java.util.ArrayList;
import polyglot.ast.FieldDecl;
import polyglot.ast.Node;
import polyglot.ast.ProcedureDecl;
import polyglot.types.FieldInstance;
import polyglot.types.ProcedureInstance;
import polyglot.util.IdentityKey;
import polyglot.visit.NodeVisitor;

public class PrivateInstancesAvailable extends NodeVisitor {
   private final ArrayList<IdentityKey> list = new ArrayList();

   public ArrayList<IdentityKey> getList() {
      return this.list;
   }

   public Node leave(Node old, Node n, NodeVisitor visitor) {
      if (n instanceof FieldDecl) {
         FieldInstance fi = ((FieldDecl)n).fieldInstance();
         if (fi.flags().isPrivate()) {
            this.list.add(new IdentityKey(fi));
         }
      }

      if (n instanceof ProcedureDecl) {
         ProcedureInstance pi = ((ProcedureDecl)n).procedureInstance();
         if (pi.flags().isPrivate()) {
            this.list.add(new IdentityKey(pi));
         }
      }

      return n;
   }
}
