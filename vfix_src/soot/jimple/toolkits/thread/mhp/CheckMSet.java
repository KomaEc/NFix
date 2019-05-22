package soot.jimple.toolkits.thread.mhp;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.jimple.toolkits.thread.mhp.stmt.JPegStmt;
import soot.tagkit.Tag;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;

public class CheckMSet {
   CheckMSet(Map m1, Map m2) {
      this.checkKeySet(m1, m2);
      this.check(m1, m2);
   }

   private void checkKeySet(Map m1, Map m2) {
      Iterator keySetIt2 = m2.keySet().iterator();
      ArraySparseSet temp = new ArraySparseSet();

      while(true) {
         label46:
         while(keySetIt2.hasNext()) {
            Object key2 = keySetIt2.next();
            if (key2 instanceof List) {
               Iterator it = ((List)key2).iterator();

               while(true) {
                  while(true) {
                     if (!it.hasNext()) {
                        continue label46;
                     }

                     Object obj = it.next();
                     if (obj instanceof List) {
                        Iterator itit = ((List)obj).iterator();

                        while(itit.hasNext()) {
                           Object o = itit.next();
                           temp.add(o);
                           if (!m1.containsKey(o)) {
                              throw new RuntimeException("1--before compacting map does not contains key " + o);
                           }
                        }
                     } else {
                        temp.add(obj);
                        if (!m1.containsKey(obj)) {
                           throw new RuntimeException("2--before compacting map does not contains key " + obj);
                        }
                     }
                  }
               }
            } else {
               if (!(key2 instanceof JPegStmt)) {
                  throw new RuntimeException("key error: " + key2);
               }

               temp.add(key2);
               if (!m1.containsKey(key2)) {
                  throw new RuntimeException("3--before compacting map does not contains key " + key2);
               }
            }
         }

         Iterator keySetIt1 = m1.keySet().iterator();

         Object key1;
         do {
            if (!keySetIt1.hasNext()) {
               return;
            }

            key1 = keySetIt1.next();
         } while(temp.contains(key1));

         throw new RuntimeException("after compacting map does not contains key " + key1);
      }
   }

   private void check(Map m1, Map m2) {
      Iterator keySetIt = m2.keySet().iterator();

      while(true) {
         label44:
         while(keySetIt.hasNext()) {
            Object key = keySetIt.next();
            if (key instanceof JPegStmt) {
               Tag var5 = (Tag)((JPegStmt)key).getTags().get(0);
            }

            FlowSet mSet2 = (FlowSet)m2.get(key);
            if (key instanceof List) {
               Iterator it = ((List)key).iterator();

               while(true) {
                  while(true) {
                     if (!it.hasNext()) {
                        continue label44;
                     }

                     Object obj = it.next();
                     if (obj instanceof List) {
                        Iterator itit = ((List)obj).iterator();

                        while(itit.hasNext()) {
                           Object oo = itit.next();
                           FlowSet mSet11 = (FlowSet)m1.get(oo);
                           if (mSet11 == null) {
                              throw new RuntimeException("1--mSet of " + obj + " is null!");
                           }

                           if (!this.compare(mSet11, mSet2)) {
                              throw new RuntimeException("1--mSet before and after are NOT the same!");
                           }
                        }
                     } else {
                        FlowSet mSet1 = (FlowSet)m1.get(obj);
                        if (mSet1 == null) {
                           throw new RuntimeException("2--mSet of " + obj + " is null!");
                        }

                        if (!this.compare(mSet1, mSet2)) {
                           throw new RuntimeException("2--mSet before and after are NOT the same!");
                        }
                     }
                  }
               }
            } else {
               FlowSet mSet1 = (FlowSet)m1.get(key);
               if (!this.compare(mSet1, mSet2)) {
                  throw new RuntimeException("3--mSet before and after are NOT the same!");
               }
            }
         }

         return;
      }
   }

   private boolean compare(FlowSet mSet1, FlowSet mSet2) {
      Iterator it = mSet2.iterator();
      ArraySparseSet temp = new ArraySparseSet();

      while(true) {
         label83:
         while(it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof List) {
               Iterator listIt = ((List)obj).iterator();

               while(true) {
                  while(true) {
                     if (!listIt.hasNext()) {
                        continue label83;
                     }

                     Object o = listIt.next();
                     if (o instanceof List) {
                        Iterator itit = ((List)o).iterator();

                        while(itit.hasNext()) {
                           temp.add(itit.next());
                        }
                     } else {
                        temp.add(o);
                     }
                  }
               }
            } else {
               temp.add(obj);
            }
         }

         Iterator listIt = mSet1.iterator();

         Object o;
         while(listIt.hasNext()) {
            o = listIt.next();
            if (!temp.contains(o)) {
               System.out.println("mSet2: \n" + mSet2);
               System.err.println("mSet2 does not contains " + o);
               return false;
            }
         }

         it = mSet2.iterator();

         while(true) {
            label53:
            while(it.hasNext()) {
               Object obj = it.next();
               if (obj instanceof List) {
                  listIt = ((List)obj).iterator();

                  while(true) {
                     while(true) {
                        if (!listIt.hasNext()) {
                           continue label53;
                        }

                        o = listIt.next();
                        if (o instanceof List) {
                           Iterator itit = ((List)o).iterator();

                           while(itit.hasNext()) {
                              Object oo = itit.next();
                              if (!mSet1.contains(oo)) {
                                 System.err.println("1--mSet1 does not contains " + oo);
                                 return false;
                              }
                           }
                        } else if (!mSet1.contains(o)) {
                           System.err.println("2--mSet1 does not contains " + o);
                           return false;
                        }
                     }
                  }
               } else if (!mSet1.contains(obj)) {
                  System.err.println("3--mSet1 does not contains " + obj);
                  return false;
               }
            }

            return true;
         }
      }
   }
}
