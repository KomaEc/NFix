package polyglot.visit;

import java.util.HashSet;
import java.util.Set;
import polyglot.ast.Field;
import polyglot.ast.FieldAssign;
import polyglot.ast.FieldDecl;
import polyglot.ast.Initializer;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;

public class FwdReferenceChecker extends ContextVisitor {
   private boolean inInitialization = false;
   private boolean inStaticInit = false;
   private Field fieldAssignLHS = null;
   private Set declaredFields = new HashSet();

   public FwdReferenceChecker(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf);
   }

   protected NodeVisitor enterCall(Node n) throws SemanticException {
      if (n instanceof FieldDecl) {
         FieldDecl fd = (FieldDecl)n;
         this.declaredFields.add(fd.fieldInstance());
         FwdReferenceChecker frc = (FwdReferenceChecker)this.copy();
         frc.inInitialization = true;
         frc.inStaticInit = fd.flags().isStatic();
         return frc;
      } else {
         FwdReferenceChecker frc;
         if (n instanceof Initializer) {
            frc = (FwdReferenceChecker)this.copy();
            frc.inInitialization = true;
            frc.inStaticInit = ((Initializer)n).flags().isStatic();
            return frc;
         } else if (n instanceof FieldAssign) {
            frc = (FwdReferenceChecker)this.copy();
            frc.fieldAssignLHS = (Field)((FieldAssign)n).left();
            return frc;
         } else {
            if (n instanceof Field) {
               if (this.fieldAssignLHS == n) {
                  this.fieldAssignLHS = null;
               } else if (this.inInitialization) {
                  Field f = (Field)n;
                  if (this.inStaticInit == f.fieldInstance().flags().isStatic() && this.context().currentClass().equals(f.fieldInstance().container()) && !this.declaredFields.contains(f.fieldInstance()) && f.isTargetImplicit()) {
                     throw new SemanticException("Illegal forward reference", f.position());
                  }
               }
            }

            return this;
         }
      }
   }
}
