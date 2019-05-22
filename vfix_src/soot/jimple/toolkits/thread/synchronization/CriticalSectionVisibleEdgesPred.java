package soot.jimple.toolkits.thread.synchronization;

import java.util.Collection;
import java.util.Iterator;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.EdgePredicate;

public class CriticalSectionVisibleEdgesPred implements EdgePredicate {
   Collection<CriticalSection> tns;
   CriticalSection exemptTn;

   public CriticalSectionVisibleEdgesPred(Collection<CriticalSection> tns) {
      this.tns = tns;
   }

   public void setExemptTransaction(CriticalSection exemptTn) {
      this.exemptTn = exemptTn;
   }

   public boolean want(Edge e) {
      String tgtMethod = e.tgt().toString();
      String tgtClass = e.tgt().getDeclaringClass().toString();
      String srcMethod = e.src().toString();
      String srcClass = e.src().getDeclaringClass().toString();
      if (tgtClass.startsWith("sun.")) {
         return false;
      } else if (tgtClass.startsWith("com.sun.")) {
         return false;
      } else if (tgtMethod.endsWith("void <clinit>()>")) {
         return false;
      } else if ((tgtClass.startsWith("java.") || tgtClass.startsWith("javax.")) && e.tgt().toString().endsWith("boolean equals(java.lang.Object)>")) {
         return false;
      } else if (!tgtClass.startsWith("java.util") && !srcClass.startsWith("java.util")) {
         if (!tgtClass.startsWith("java.lang") && !srcClass.startsWith("java.lang")) {
            if (tgtClass.startsWith("java")) {
               return false;
            } else if (e.tgt().isSynchronized()) {
               return false;
            } else {
               if (this.tns != null) {
                  Iterator tnIt = this.tns.iterator();

                  while(tnIt.hasNext()) {
                     CriticalSection tn = (CriticalSection)tnIt.next();
                     if (tn != this.exemptTn && tn.units.contains(e.srcStmt())) {
                        return false;
                     }
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
