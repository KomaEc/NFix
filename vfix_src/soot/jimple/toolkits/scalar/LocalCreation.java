package soot.jimple.toolkits.scalar;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.Local;
import soot.Type;
import soot.jimple.Jimple;

public class LocalCreation {
   public static final String DEFAULT_PREFIX = "soot";
   private String prefix;
   private int counter;
   private Set<String> locals;
   private Collection<Local> localChain;

   public LocalCreation(Collection<Local> locals) {
      this(locals, "soot");
   }

   public LocalCreation(Collection<Local> locals, String prefix) {
      this.locals = new HashSet(locals.size());
      this.localChain = locals;
      Iterator it = locals.iterator();

      while(it.hasNext()) {
         Local l = (Local)it.next();
         this.locals.add(l.getName());
      }

      this.prefix = prefix;
      this.counter = 0;
   }

   public Local newLocal(Type type) {
      return this.newLocal(this.prefix, type);
   }

   public Local newLocal(String prefix, Type type) {
      int suffix = 0;
      if (prefix == this.prefix || prefix.equals(this.prefix)) {
         suffix = this.counter;
      }

      while(this.locals.contains(prefix + suffix)) {
         ++suffix;
      }

      if (prefix == this.prefix || prefix.equals(this.prefix)) {
         this.counter = suffix + 1;
      }

      String newName = prefix + suffix;
      Local newLocal = Jimple.v().newLocal(newName, type);
      this.localChain.add(newLocal);
      this.locals.add(newName);
      return newLocal;
   }
}
