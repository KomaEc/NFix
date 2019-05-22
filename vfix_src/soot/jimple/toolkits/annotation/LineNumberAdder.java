package soot.jimple.toolkits.annotation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;
import soot.tagkit.LineNumberTag;

public class LineNumberAdder extends SceneTransformer {
   public LineNumberAdder(Singletons.Global g) {
   }

   public static LineNumberAdder v() {
      return G.v().soot_jimple_toolkits_annotation_LineNumberAdder();
   }

   public void internalTransform(String phaseName, Map opts) {
      Iterator it = Scene.v().getApplicationClasses().iterator();

      label66:
      while(it.hasNext()) {
         SootClass sc = (SootClass)it.next();
         HashMap<Integer, SootMethod> lineToMeth = new HashMap();
         Iterator methIt = sc.getMethods().iterator();

         while(true) {
            SootMethod meth;
            do {
               if (!methIt.hasNext()) {
                  Iterator methIt2 = sc.getMethods().iterator();

                  while(true) {
                     SootMethod meth;
                     do {
                        if (!methIt2.hasNext()) {
                           continue label66;
                        }

                        meth = (SootMethod)methIt2.next();
                     } while(!meth.isConcrete());

                     Body body = meth.retrieveActiveBody();

                     Stmt s;
                     for(s = (Stmt)body.getUnits().getFirst(); s instanceof IdentityStmt; s = (Stmt)body.getUnits().getSuccOf((Unit)s)) {
                     }

                     if (s.hasTag("LineNumberTag")) {
                        LineNumberTag tag = (LineNumberTag)s.getTag("LineNumberTag");
                        int line_num = tag.getLineNumber() - 1;
                        if (lineToMeth.containsKey(new Integer(line_num))) {
                           meth.addTag(new LineNumberTag(line_num + 1));
                        } else {
                           meth.addTag(new LineNumberTag(line_num));
                        }
                     }
                  }
               }

               meth = (SootMethod)methIt.next();
            } while(!meth.isConcrete());

            Body body = meth.retrieveActiveBody();

            Stmt s;
            for(s = (Stmt)body.getUnits().getFirst(); s instanceof IdentityStmt; s = (Stmt)body.getUnits().getSuccOf((Unit)s)) {
            }

            if (s.hasTag("LineNumberTag")) {
               LineNumberTag tag = (LineNumberTag)s.getTag("LineNumberTag");
               lineToMeth.put(new Integer(tag.getLineNumber()), meth);
            }
         }
      }

   }
}
