package soot.jimple.toolkits.thread.mhp.pegcallgraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.DirectedGraph;
import soot.util.Chain;
import soot.util.HashChain;

public class PegCallGraph implements DirectedGraph {
   List heads = new ArrayList();
   List tails = new ArrayList();
   Chain chain = new HashChain();
   private final Map<Object, List> methodToSuccs = new HashMap();
   private final Map<Object, List> methodToPreds = new HashMap();
   private final Map<Object, List> methodToSuccsTrim = new HashMap();
   private final Set clinitMethods = new HashSet();

   public PegCallGraph(CallGraph cg) {
      this.buildChainAndSuccs(cg);
      this.buildPreds();
   }

   protected void testChain() {
      System.out.println("******** chain of pegcallgraph********");
      Iterator it = this.chain.iterator();

      while(it.hasNext()) {
         SootMethod sm = (SootMethod)it.next();
         System.out.println(sm);
      }

   }

   public Set getClinitMethods() {
      return this.clinitMethods;
   }

   private void buildChainAndSuccs(CallGraph cg) {
      Iterator it = cg.sourceMethods();

      while(true) {
         SootMethod sm;
         do {
            do {
               if (!it.hasNext()) {
                  Iterator chainIt = this.chain.iterator();

                  SootMethod s;
                  while(chainIt.hasNext()) {
                     s = (SootMethod)chainIt.next();
                     if (!this.methodToSuccs.containsKey(s)) {
                        this.methodToSuccs.put(s, new ArrayList());
                     }
                  }

                  chainIt = this.chain.iterator();

                  while(it.hasNext()) {
                     s = (SootMethod)chainIt.next();
                     if (this.methodToSuccs.containsKey(s)) {
                        List succList = (List)this.methodToSuccs.get(s);
                        if (succList.size() <= 0) {
                        }
                     }
                  }

                  chainIt = this.chain.iterator();

                  while(chainIt.hasNext()) {
                     s = (SootMethod)chainIt.next();
                     if (this.methodToSuccs.containsKey(s)) {
                        this.methodToSuccs.put(s, Collections.unmodifiableList((List)this.methodToSuccs.get(s)));
                     }
                  }

                  return;
               }

               sm = (SootMethod)it.next();
               if (sm.getName().equals("main")) {
                  this.heads.add(sm);
               }
            } while(!sm.isConcrete());
         } while(!sm.getDeclaringClass().isApplicationClass());

         if (!this.chain.contains(sm)) {
            this.chain.add(sm);
         }

         List succsList = new ArrayList();
         Iterator edgeIt = cg.edgesOutOf((MethodOrMethodContext)sm);

         while(edgeIt.hasNext()) {
            Edge edge = (Edge)edgeIt.next();
            SootMethod target = edge.tgt();
            if (target.isConcrete() && target.getDeclaringClass().isApplicationClass()) {
               succsList.add(target);
               if (!this.chain.contains(target)) {
                  this.chain.add(target);
               }

               if (edge.isClinit()) {
                  this.clinitMethods.add(target);
               }
            }
         }

         if (succsList.size() > 0) {
            this.methodToSuccs.put(sm, succsList);
         }
      }
   }

   private void buildPreds() {
      Iterator unitIt = this.chain.iterator();

      while(unitIt.hasNext()) {
         this.methodToPreds.put(unitIt.next(), new ArrayList());
      }

      unitIt = this.chain.iterator();

      while(true) {
         Object s;
         List predList;
         do {
            if (!unitIt.hasNext()) {
               unitIt = this.chain.iterator();

               while(unitIt.hasNext()) {
                  SootMethod s = (SootMethod)unitIt.next();
                  if (this.methodToPreds.containsKey(s)) {
                     predList = (List)this.methodToPreds.get(s);
                     this.methodToPreds.put(s, Collections.unmodifiableList(predList));
                  }
               }

               return;
            }

            s = unitIt.next();
            predList = (List)this.methodToSuccs.get(s);
         } while(predList.size() <= 0);

         Iterator succIt = predList.iterator();

         while(succIt.hasNext()) {
            Object successor = succIt.next();
            List predList = (List)this.methodToPreds.get(successor);

            try {
               predList.add(s);
            } catch (NullPointerException var8) {
               System.out.println(s + "successor: " + successor);
               throw var8;
            }
         }
      }
   }

   public void trim() {
      Set maps = this.methodToSuccs.entrySet();
      Iterator iter = maps.iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         List list = (List)entry.getValue();
         List<Object> newList = new ArrayList();
         Iterator it = list.iterator();

         while(it.hasNext()) {
            Object obj = it.next();
            if (!list.contains(obj)) {
               newList.add(obj);
            }
         }

         this.methodToSuccsTrim.put(entry.getKey(), newList);
      }

   }

   public List getHeads() {
      return this.heads;
   }

   public List getTails() {
      return this.tails;
   }

   public List getTrimSuccsOf(Object s) {
      return !this.methodToSuccsTrim.containsKey(s) ? Collections.EMPTY_LIST : (List)this.methodToSuccsTrim.get(s);
   }

   public List getSuccsOf(Object s) {
      return !this.methodToSuccs.containsKey(s) ? Collections.EMPTY_LIST : (List)this.methodToSuccs.get(s);
   }

   public List getPredsOf(Object s) {
      return !this.methodToPreds.containsKey(s) ? Collections.EMPTY_LIST : (List)this.methodToPreds.get(s);
   }

   public Iterator iterator() {
      return this.chain.iterator();
   }

   public int size() {
      return this.chain.size();
   }

   protected void testMethodToSucc() {
      System.out.println("=====test methodToSucc ");
      Set maps = this.methodToSuccs.entrySet();
      Iterator iter = maps.iterator();

      while(true) {
         List list;
         do {
            if (!iter.hasNext()) {
               System.out.println("=========methodToSucc--ends--------");
               return;
            }

            Entry entry = (Entry)iter.next();
            System.out.println("---key=  " + entry.getKey());
            list = (List)entry.getValue();
         } while(list.size() <= 0);

         System.out.println("**succ set:");
         Iterator it = list.iterator();

         while(it.hasNext()) {
            System.out.println(it.next());
         }
      }
   }

   protected void testMethodToPred() {
      System.out.println("=====test methodToPred ");
      Set maps = this.methodToPreds.entrySet();
      Iterator iter = maps.iterator();

      while(true) {
         List list;
         do {
            if (!iter.hasNext()) {
               System.out.println("=========methodToPred--ends--------");
               return;
            }

            Entry entry = (Entry)iter.next();
            System.out.println("---key=  " + entry.getKey());
            list = (List)entry.getValue();
         } while(list.size() <= 0);

         System.out.println("**pred set:");
         Iterator it = list.iterator();

         while(it.hasNext()) {
            System.out.println(it.next());
         }
      }
   }
}
