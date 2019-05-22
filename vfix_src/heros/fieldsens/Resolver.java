package heros.fieldsens;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class Resolver<Field, Fact, Stmt, Method> {
   private Set<Resolver<Field, Fact, Stmt, Method>> interest = Sets.newHashSet();
   private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();
   protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;
   private boolean canBeResolvedEmpty = false;

   public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
      this.analyzer = analyzer;
   }

   public abstract void resolve(FlowFunction.Constraint<Field> var1, InterestCallback<Field, Fact, Stmt, Method> var2);

   public void interest(Resolver<Field, Fact, Stmt, Method> resolver) {
      if (this.interest.add(resolver)) {
         this.log("Interest given by: " + resolver);
         Iterator var2 = Lists.newLinkedList(this.interestCallbacks).iterator();

         while(var2.hasNext()) {
            InterestCallback<Field, Fact, Stmt, Method> callback = (InterestCallback)var2.next();
            callback.interest(this.analyzer, resolver);
         }

      }
   }

   protected void canBeResolvedEmpty() {
      if (!this.canBeResolvedEmpty) {
         this.canBeResolvedEmpty = true;
         Iterator var1 = Lists.newLinkedList(this.interestCallbacks).iterator();

         while(var1.hasNext()) {
            InterestCallback<Field, Fact, Stmt, Method> callback = (InterestCallback)var1.next();
            callback.canBeResolvedEmpty();
         }

      }
   }

   public boolean isInterestGiven() {
      return !this.interest.isEmpty();
   }

   protected void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {
      if (!this.interest.isEmpty()) {
         Iterator var2 = Lists.newLinkedList(this.interest).iterator();

         while(var2.hasNext()) {
            Resolver<Field, Fact, Stmt, Method> resolver = (Resolver)var2.next();
            callback.interest(this.analyzer, resolver);
         }
      }

      this.log("Callback registered");
      this.interestCallbacks.add(callback);
      if (this.canBeResolvedEmpty) {
         callback.canBeResolvedEmpty();
      }

   }

   protected abstract void log(String var1);
}
