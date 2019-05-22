package soot.dava.toolkits.base.AST;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.Singletons;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTTryNode;

public class UselessTryRemover extends ASTAnalysis {
   public UselessTryRemover(Singletons.Global g) {
   }

   public static UselessTryRemover v() {
      return G.v().soot_dava_toolkits_base_AST_UselessTryRemover();
   }

   public int getAnalysisDepth() {
      return 0;
   }

   public void analyseASTNode(ASTNode n) {
      Iterator sbit = n.get_SubBodies().iterator();

      label50:
      while(sbit.hasNext()) {
         List<Object> subBody = null;
         List<Object> toRemove = new ArrayList();
         if (n instanceof ASTTryNode) {
            subBody = (List)((ASTTryNode.container)sbit.next()).o;
         } else {
            subBody = (List)sbit.next();
         }

         Iterator cit = subBody.iterator();

         while(true) {
            ASTTryNode tryNode;
            do {
               Object child;
               do {
                  if (!cit.hasNext()) {
                     Iterator trit = toRemove.iterator();

                     while(trit.hasNext()) {
                        tryNode = (ASTTryNode)trit.next();
                        subBody.addAll(subBody.indexOf(tryNode), tryNode.get_TryBody());
                        subBody.remove(tryNode);
                     }

                     if (!toRemove.isEmpty()) {
                        G.v().ASTAnalysis_modified = true;
                     }
                     continue label50;
                  }

                  child = cit.next();
               } while(!(child instanceof ASTTryNode));

               tryNode = (ASTTryNode)child;
               tryNode.perform_Analysis(TryContentsFinder.v());
            } while(!tryNode.get_CatchList().isEmpty() && !tryNode.isEmpty());

            toRemove.add(tryNode);
         }
      }

   }
}
