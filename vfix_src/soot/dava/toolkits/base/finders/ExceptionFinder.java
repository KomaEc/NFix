package soot.dava.toolkits.base.finders;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import soot.G;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETTryNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.jimple.GotoStmt;
import soot.jimple.Stmt;
import soot.util.IterableSet;

public class ExceptionFinder implements FactFinder {
   public ExceptionFinder(Singletons.Global g) {
   }

   public static ExceptionFinder v() {
      return G.v().soot_dava_toolkits_base_finders_ExceptionFinder();
   }

   public void find(DavaBody body, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
      Dava.v().log("ExceptionFinder::find()");
      Iterator var4 = body.get_ExceptionFacts().iterator();

      ExceptionNode en;
      IterableSet fullBody;
      do {
         do {
            if (!var4.hasNext()) {
               return;
            }

            en = (ExceptionNode)var4.next();
         } while(body.get_SynchronizedBlockFacts().contains(en));

         fullBody = new IterableSet();
         Iterator var7 = en.get_CatchList().iterator();

         while(var7.hasNext()) {
            IterableSet<AugmentedStmt> is = (IterableSet)var7.next();
            fullBody.addAll(is);
         }

         fullBody.addAll(en.get_TryBody());
      } while(SET.nest(new SETTryNode(fullBody, en, asg, body)));

      throw new RetriggerAnalysisException();
   }

   public void preprocess(DavaBody body, AugmentedStmtGraph asg) {
      Dava.v().log("ExceptionFinder::preprocess()");
      IterableSet<ExceptionNode> enlist = new IterableSet();
      Iterator var4 = body.getTraps().iterator();

      Iterator btit;
      while(var4.hasNext()) {
         Trap trap = (Trap)var4.next();
         Unit endUnit = trap.getEndUnit();
         IterableSet<AugmentedStmt> tryBody = new IterableSet();
         btit = body.getUnits().iterator(trap.getBeginUnit());

         for(Unit u = (Unit)btit.next(); u != endUnit; u = (Unit)btit.next()) {
            tryBody.add(asg.get_AugStmt((Stmt)u));
         }

         enlist.add(new ExceptionNode(tryBody, trap.getException(), asg.get_AugStmt((Stmt)trap.getHandlerUnit())));
      }

      var4 = enlist.iterator();

      Iterator var13;
      AugmentedStmt sas;
      ExceptionNode en;
      IterableSet tryBody;
      Iterator var23;
      while(var4.hasNext()) {
         en = (ExceptionNode)var4.next();
         tryBody = en.get_TryBody();
         var23 = tryBody.iterator();

         label206:
         while(var23.hasNext()) {
            AugmentedStmt tras = (AugmentedStmt)var23.next();
            Iterator var29 = tras.cpreds.iterator();

            while(true) {
               AugmentedStmt pas;
               Stmt ps;
               do {
                  do {
                     if (!var29.hasNext()) {
                        continue label206;
                     }

                     pas = (AugmentedStmt)var29.next();
                     ps = pas.get_Stmt();
                  } while(tryBody.contains(pas));
               } while(!(ps instanceof GotoStmt));

               boolean add_it = true;
               var13 = pas.cpreds.iterator();

               while(var13.hasNext()) {
                  sas = (AugmentedStmt)var13.next();
                  if (!(add_it = tryBody.contains(sas))) {
                     break;
                  }
               }

               if (add_it) {
                  en.add_TryStmt(pas);
               }
            }
         }
      }

      while(true) {
         label178:
         while(true) {
            var4 = enlist.iterator();

            while(var4.hasNext()) {
               en = (ExceptionNode)var4.next();
               en.refresh_CatchBody(this);
            }

            ExceptionNode[] ena = new ExceptionNode[enlist.size()];
            Iterator<ExceptionNode> enlit = enlist.iterator();

            int i;
            for(i = 0; enlit.hasNext(); ++i) {
               ena[i] = (ExceptionNode)enlit.next();
            }

            IterableSet curTryBody;
            for(i = 0; i < ena.length - 1; ++i) {
               ExceptionNode eni = ena[i];

               for(int j = i + 1; j < ena.length; ++j) {
                  ExceptionNode enj = ena[j];
                  curTryBody = eni.get_TryBody();
                  IterableSet<AugmentedStmt> enjTryBody = enj.get_TryBody();
                  if (!curTryBody.equals(enjTryBody) && curTryBody.intersects(enjTryBody) && !curTryBody.isSupersetOf(enj.get_Body()) && !enjTryBody.isSupersetOf(eni.get_Body())) {
                     IterableSet<AugmentedStmt> newTryBody = curTryBody.intersection(enjTryBody);
                     if (newTryBody.equals(enjTryBody)) {
                        eni.splitOff_ExceptionNode(newTryBody, asg, enlist);
                     } else {
                        enj.splitOff_ExceptionNode(newTryBody, asg, enlist);
                     }
                     continue label178;
                  }
               }
            }

            var4 = enlist.iterator();

            LinkedList heads;
            do {
               AugmentedStmt au;
               if (!var4.hasNext()) {
                  LinkedList<ExceptionNode> reps = new LinkedList();
                  HashMap<Serializable, LinkedList<IterableSet<AugmentedStmt>>> hCode2bucket = new HashMap();
                  HashMap<Serializable, ExceptionNode> tryBody2exceptionNode = new HashMap();
                  var23 = enlist.iterator();

                  while(var23.hasNext()) {
                     ExceptionNode en = (ExceptionNode)var23.next();
                     int hashCode = 0;
                     curTryBody = en.get_TryBody();

                     for(Iterator var40 = curTryBody.iterator(); var40.hasNext(); hashCode ^= au.hashCode()) {
                        au = (AugmentedStmt)var40.next();
                     }

                     Integer I = new Integer(hashCode);
                     LinkedList<IterableSet<AugmentedStmt>> bucket = (LinkedList)hCode2bucket.get(I);
                     if (bucket == null) {
                        bucket = new LinkedList();
                        hCode2bucket.put(I, bucket);
                     }

                     ExceptionNode repExceptionNode = null;
                     Iterator var46 = bucket.iterator();

                     while(var46.hasNext()) {
                        IterableSet<AugmentedStmt> bucketTryBody = (IterableSet)var46.next();
                        if (bucketTryBody.equals(curTryBody)) {
                           repExceptionNode = (ExceptionNode)tryBody2exceptionNode.get(bucketTryBody);
                           break;
                        }
                     }

                     if (repExceptionNode == null) {
                        tryBody2exceptionNode.put(curTryBody, en);
                        bucket.add(curTryBody);
                        reps.add(en);
                     } else {
                        repExceptionNode.add_CatchBody(en);
                     }
                  }

                  enlist.clear();
                  enlist.addAll(reps);
                  body.get_ExceptionFacts().clear();
                  body.get_ExceptionFacts().addAll(enlist);
                  return;
               }

               en = (ExceptionNode)var4.next();
               tryBody = en.get_TryBody();
               heads = new LinkedList();
               btit = tryBody.iterator();

               while(true) {
                  AugmentedStmt head;
                  while(btit.hasNext()) {
                     head = (AugmentedStmt)btit.next();
                     if (head.cpreds.isEmpty()) {
                        heads.add(head);
                     } else {
                        Iterator var35 = head.cpreds.iterator();

                        while(var35.hasNext()) {
                           AugmentedStmt pred = (AugmentedStmt)var35.next();
                           if (!tryBody.contains(pred)) {
                              heads.add(head);
                              break;
                           }
                        }
                     }
                  }

                  HashSet<AugmentedStmt> touchSet = new HashSet();
                  touchSet.addAll(heads);
                  head = (AugmentedStmt)heads.removeFirst();
                  curTryBody = new IterableSet();
                  LinkedList<AugmentedStmt> worklist = new LinkedList();
                  worklist.add(head);

                  while(!worklist.isEmpty()) {
                     au = (AugmentedStmt)worklist.removeFirst();
                     curTryBody.add(au);
                     var13 = au.csuccs.iterator();

                     while(var13.hasNext()) {
                        sas = (AugmentedStmt)var13.next();
                        if (tryBody.contains(sas) && !touchSet.contains(sas)) {
                           touchSet.add(sas);
                           if (sas.get_Dominators().contains(head)) {
                              worklist.add(sas);
                           } else {
                              heads.addLast(sas);
                           }
                        }
                     }
                  }
                  break;
               }
            } while(heads.isEmpty());

            en.splitOff_ExceptionNode(curTryBody, asg, enlist);
         }
      }
   }

   public IterableSet<AugmentedStmt> get_CatchBody(AugmentedStmt handlerAugmentedStmt) {
      IterableSet<AugmentedStmt> catchBody = new IterableSet();
      LinkedList<AugmentedStmt> catchQueue = new LinkedList();
      catchBody.add(handlerAugmentedStmt);
      catchQueue.addAll(handlerAugmentedStmt.csuccs);

      while(!catchQueue.isEmpty()) {
         AugmentedStmt as = (AugmentedStmt)catchQueue.removeFirst();
         if (!catchBody.contains(as) && as.get_Dominators().contains(handlerAugmentedStmt)) {
            catchBody.add(as);
            catchQueue.addAll(as.csuccs);
         }
      }

      return catchBody;
   }
}
