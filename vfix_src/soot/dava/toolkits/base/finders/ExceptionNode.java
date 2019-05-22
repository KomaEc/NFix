package soot.dava.toolkits.base.finders;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootClass;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.util.IterableSet;

public class ExceptionNode {
   private static final Logger logger = LoggerFactory.getLogger(ExceptionNode.class);
   private final IterableSet<AugmentedStmt> body;
   private IterableSet<AugmentedStmt> tryBody;
   private IterableSet<AugmentedStmt> catchBody;
   private boolean dirty;
   private List<AugmentedStmt> exitList;
   private LinkedList<IterableSet<AugmentedStmt>> catchList;
   private SootClass exception;
   private HashMap<IterableSet<AugmentedStmt>, SootClass> catch2except;
   private AugmentedStmt handlerAugmentedStmt;

   public ExceptionNode(IterableSet<AugmentedStmt> tryBody, SootClass exception, AugmentedStmt handlerAugmentedStmt) {
      this.tryBody = tryBody;
      this.catchBody = null;
      this.exception = exception;
      this.handlerAugmentedStmt = handlerAugmentedStmt;
      this.body = new IterableSet(tryBody);
      this.dirty = true;
      this.exitList = null;
      this.catchList = null;
      this.catch2except = null;
   }

   public boolean add_TryStmts(Collection<AugmentedStmt> c) {
      Iterator var2 = c.iterator();

      AugmentedStmt as;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         as = (AugmentedStmt)var2.next();
      } while(this.add_TryStmt(as));

      return false;
   }

   public boolean add_TryStmt(AugmentedStmt as) {
      if (!this.body.contains(as) && !this.tryBody.contains(as)) {
         this.body.add(as);
         this.tryBody.add(as);
         return true;
      } else {
         return false;
      }
   }

   public void refresh_CatchBody(ExceptionFinder ef) {
      if (this.catchBody != null) {
         this.body.removeAll(this.catchBody);
      }

      this.catchBody = ef.get_CatchBody(this.handlerAugmentedStmt);
      this.body.addAll(this.catchBody);
   }

   public IterableSet<AugmentedStmt> get_Body() {
      return this.body;
   }

   public IterableSet<AugmentedStmt> get_TryBody() {
      return this.tryBody;
   }

   public IterableSet<AugmentedStmt> get_CatchBody() {
      return this.catchBody;
   }

   public boolean remove(AugmentedStmt as) {
      if (!this.body.contains(as)) {
         return false;
      } else {
         if (this.tryBody.contains(as)) {
            this.tryBody.remove(as);
         } else {
            if (this.catchBody == null || !this.catchBody.contains(as)) {
               return false;
            }

            this.catchBody.remove(as);
            this.dirty = true;
         }

         this.body.remove(as);
         return true;
      }
   }

   public List<AugmentedStmt> get_CatchExits() {
      if (this.catchBody == null) {
         return null;
      } else if (this.dirty) {
         this.exitList = new LinkedList();
         this.dirty = false;
         Iterator var1 = this.catchBody.iterator();

         while(true) {
            while(var1.hasNext()) {
               AugmentedStmt as = (AugmentedStmt)var1.next();
               Iterator var3 = as.bsuccs.iterator();

               while(var3.hasNext()) {
                  AugmentedStmt succ = (AugmentedStmt)var3.next();
                  if (!this.catchBody.contains(succ)) {
                     this.exitList.add(as);
                     break;
                  }
               }
            }

            return this.exitList;
         }
      } else {
         return this.exitList;
      }
   }

   public void splitOff_ExceptionNode(IterableSet<AugmentedStmt> newTryBody, AugmentedStmtGraph asg, IterableSet<ExceptionNode> enlist) {
      IterableSet<AugmentedStmt> oldTryBody = new IterableSet();
      oldTryBody.addAll(this.tryBody);
      IterableSet<AugmentedStmt> oldBody = new IterableSet();
      oldBody.addAll(this.body);
      Iterator var6 = newTryBody.iterator();

      AugmentedStmt as;
      while(var6.hasNext()) {
         as = (AugmentedStmt)var6.next();
         if (!this.remove(as)) {
            StringBuffer b = new StringBuffer();
            Iterator var9 = newTryBody.iterator();

            AugmentedStmt auBody;
            while(var9.hasNext()) {
               auBody = (AugmentedStmt)var9.next();
               b.append("\n" + auBody.toString());
            }

            b.append("\n-");
            var9 = oldTryBody.iterator();

            while(var9.hasNext()) {
               auBody = (AugmentedStmt)var9.next();
               b.append("\n" + auBody.toString());
            }

            b.append("\n-");
            var9 = oldBody.iterator();

            while(var9.hasNext()) {
               auBody = (AugmentedStmt)var9.next();
               b.append("\n" + auBody.toString());
            }

            b.append("\n-");
            throw new RuntimeException("Tried to split off a new try body that isn't in the old one.\n" + as + "\n - " + b.toString());
         }
      }

      asg.clone_Body(this.catchBody);
      AugmentedStmt oldCatchTarget = this.handlerAugmentedStmt;
      as = asg.get_CloneOf(this.handlerAugmentedStmt);
      Iterator var14 = newTryBody.iterator();

      AugmentedStmt as;
      while(var14.hasNext()) {
         as = (AugmentedStmt)var14.next();
         as.remove_CSucc(oldCatchTarget);
         oldCatchTarget.remove_CPred(as);
      }

      var14 = this.tryBody.iterator();

      while(var14.hasNext()) {
         as = (AugmentedStmt)var14.next();
         as.remove_CSucc(as);
         as.remove_CPred(as);
      }

      var14 = enlist.iterator();

      while(true) {
         ExceptionNode en;
         do {
            do {
               if (!var14.hasNext()) {
                  enlist.addLast(new ExceptionNode(newTryBody, this.exception, asg.get_CloneOf(this.handlerAugmentedStmt)));
                  var14 = enlist.iterator();

                  while(var14.hasNext()) {
                     en = (ExceptionNode)var14.next();
                     en.refresh_CatchBody(ExceptionFinder.v());
                  }

                  asg.find_Dominators();
                  return;
               }

               en = (ExceptionNode)var14.next();
            } while(this == en);
         } while(!this.catchBody.isSupersetOf(en.get_Body()));

         IterableSet<AugmentedStmt> clonedTryBody = new IterableSet();
         Iterator var11 = en.get_TryBody().iterator();

         while(var11.hasNext()) {
            AugmentedStmt au = (AugmentedStmt)var11.next();
            clonedTryBody.add(asg.get_CloneOf(au));
         }

         enlist.addLast(new ExceptionNode(clonedTryBody, en.exception, asg.get_CloneOf(en.handlerAugmentedStmt)));
      }
   }

   public void add_CatchBody(ExceptionNode other) {
      if (other.get_CatchList() == null) {
         this.add_CatchBody(other.get_CatchBody(), other.get_Exception());
      } else {
         Iterator var2 = other.get_CatchList().iterator();

         while(var2.hasNext()) {
            IterableSet<AugmentedStmt> c = (IterableSet)var2.next();
            this.add_CatchBody(c, other.get_Exception(c));
         }

      }
   }

   public void add_CatchBody(IterableSet<AugmentedStmt> newCatchBody, SootClass except) {
      if (this.catchList == null) {
         this.catchList = new LinkedList();
         this.catchList.addLast(this.catchBody);
         this.catch2except = new HashMap();
         this.catch2except.put(this.catchBody, this.exception);
      }

      this.body.addAll(newCatchBody);
      this.catchList.addLast(newCatchBody);
      this.catch2except.put(newCatchBody, except);
   }

   public List<IterableSet<AugmentedStmt>> get_CatchList() {
      List<IterableSet<AugmentedStmt>> l = this.catchList;
      if (l == null) {
         l = new LinkedList();
         l.add(this.catchBody);
      }

      return l;
   }

   public Map<IterableSet<AugmentedStmt>, SootClass> get_ExceptionMap() {
      Map<IterableSet<AugmentedStmt>, SootClass> m = this.catch2except;
      if (m == null) {
         m = new HashMap();
         m.put(this.catchBody, this.exception);
      }

      return m;
   }

   public SootClass get_Exception() {
      return this.exception;
   }

   public SootClass get_Exception(IterableSet<AugmentedStmt> catchBody) {
      return this.catch2except == null ? this.exception : (SootClass)this.catch2except.get(catchBody);
   }

   public void dump() {
      logger.debug("try {");
      Iterator var1 = this.get_TryBody().iterator();

      while(var1.hasNext()) {
         AugmentedStmt au = (AugmentedStmt)var1.next();
         logger.debug("\t" + au);
      }

      logger.debug("}");
      var1 = this.get_CatchList().iterator();

      while(var1.hasNext()) {
         IterableSet<AugmentedStmt> catchBody = (IterableSet)var1.next();
         logger.debug("catch " + this.get_ExceptionMap().get(catchBody) + " {");
         Iterator var3 = catchBody.iterator();

         while(var3.hasNext()) {
            AugmentedStmt au = (AugmentedStmt)var3.next();
            logger.debug("\t" + au);
         }

         logger.debug("}");
      }

   }
}
