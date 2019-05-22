package soot.dava.toolkits.base.finders;

import java.util.Iterator;
import java.util.LinkedList;
import soot.G;
import soot.Singletons;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETIfElseNode;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.util.IterableSet;

public class IfFinder implements FactFinder {
   public IfFinder(Singletons.Global g) {
   }

   public static IfFinder v() {
      return G.v().soot_dava_toolkits_base_finders_IfFinder();
   }

   public void find(DavaBody body, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
      Dava.v().log("IfFinder::find()");
      Iterator asgit = asg.iterator();

      label57:
      while(true) {
         AugmentedStmt as;
         IfStmt ifs;
         do {
            Stmt s;
            do {
               if (!asgit.hasNext()) {
                  return;
               }

               as = (AugmentedStmt)asgit.next();
               s = as.get_Stmt();
            } while(!(s instanceof IfStmt));

            ifs = (IfStmt)s;
         } while(body.get_ConsumedConditions().contains(as));

         body.consume_Condition(as);
         AugmentedStmt succIf = asg.get_AugStmt(ifs.getTarget());
         AugmentedStmt succElse = (AugmentedStmt)as.bsuccs.get(0);
         if (succIf == succElse) {
            succElse = (AugmentedStmt)as.bsuccs.get(1);
         }

         asg.calculate_Reachability(succIf, succElse, as);
         asg.calculate_Reachability(succElse, succIf, as);
         IterableSet fullBody = new IterableSet();
         IterableSet ifBody = this.find_Body(succIf, succElse);
         IterableSet elseBody = this.find_Body(succElse, succIf);
         fullBody.add(as);
         fullBody.addAll(ifBody);
         fullBody.addAll(elseBody);
         Iterator enlit = body.get_ExceptionFacts().iterator();

         while(true) {
            IterableSet tryBody;
            do {
               if (!enlit.hasNext()) {
                  SET.nest(new SETIfElseNode(as, fullBody, ifBody, elseBody));
                  continue label57;
               }

               ExceptionNode en = (ExceptionNode)enlit.next();
               tryBody = en.get_TryBody();
            } while(!tryBody.contains(as));

            Iterator fbit = fullBody.snapshotIterator();

            while(fbit.hasNext()) {
               AugmentedStmt fbas = (AugmentedStmt)fbit.next();
               if (!tryBody.contains(fbas)) {
                  fullBody.remove(fbas);
                  if (ifBody.contains(fbas)) {
                     ifBody.remove(fbas);
                  }

                  if (elseBody.contains(fbas)) {
                     elseBody.remove(fbas);
                  }
               }
            }
         }
      }
   }

   private IterableSet find_Body(AugmentedStmt targetBranch, AugmentedStmt otherBranch) {
      IterableSet body = new IterableSet();
      if (targetBranch.get_Reachers().contains(otherBranch)) {
         return body;
      } else {
         LinkedList<AugmentedStmt> worklist = new LinkedList();
         worklist.addLast(targetBranch);

         while(true) {
            AugmentedStmt as;
            do {
               if (worklist.isEmpty()) {
                  return body;
               }

               as = (AugmentedStmt)worklist.removeFirst();
            } while(body.contains(as));

            body.add(as);
            Iterator sit = as.csuccs.iterator();

            while(sit.hasNext()) {
               AugmentedStmt sas = (AugmentedStmt)sit.next();
               if (!sas.get_Reachers().contains(otherBranch) && sas.get_Dominators().contains(targetBranch)) {
                  worklist.addLast(sas);
               }
            }
         }
      }
   }
}
