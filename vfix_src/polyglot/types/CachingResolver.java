package polyglot.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import polyglot.frontend.ExtensionInfo;
import polyglot.main.Report;
import polyglot.util.CollectionUtil;
import polyglot.util.StringUtil;

public class CachingResolver implements TopLevelResolver {
   TopLevelResolver inner;
   Map cache;
   Map packageCache;
   ExtensionInfo extInfo;
   static Object NOT_FOUND = new Object();
   private static final Collection TOPICS = CollectionUtil.list("types", "resolver");

   public CachingResolver(TopLevelResolver inner, ExtensionInfo extInfo) {
      this.inner = inner;
      this.cache = new HashMap();
      this.packageCache = new HashMap();
      this.extInfo = extInfo;
   }

   public TopLevelResolver inner() {
      return this.inner;
   }

   public String toString() {
      return "(cache " + this.inner.toString() + ")";
   }

   public boolean packageExists(String name) {
      Boolean b = (Boolean)this.packageCache.get(name);
      if (b != null) {
         return b;
      } else {
         String prefix = StringUtil.getPackageComponent(name);
         if (this.packageCache.get(prefix) == Boolean.FALSE) {
            this.packageCache.put(name, Boolean.FALSE);
            return false;
         } else {
            boolean exists = this.inner.packageExists(name);
            if (exists) {
               this.packageCache.put(name, Boolean.TRUE);

               do {
                  this.packageCache.put(prefix, Boolean.TRUE);
                  prefix = StringUtil.getPackageComponent(prefix);
               } while(!prefix.equals(""));
            } else {
               this.packageCache.put(name, Boolean.FALSE);
            }

            return exists;
         }
      }
   }

   protected void cachePackage(Package p) {
      if (p != null) {
         this.packageCache.put(p.fullName(), Boolean.TRUE);
         this.cachePackage(p.prefix());
      }

   }

   public Named find(String name) throws SemanticException {
      if (Report.should_report((Collection)TOPICS, 2)) {
         Report.report(2, "CachingResolver: find: " + name);
      }

      Object o = this.cache.get(name);
      if (o == NOT_FOUND) {
         throw new NoClassException(name);
      } else {
         Named q = (Named)o;
         if (q == null) {
            if (Report.should_report((Collection)TOPICS, 3)) {
               Report.report(3, "CachingResolver: not cached: " + name);
            }

            try {
               q = this.inner.find(name);
            } catch (NoClassException var5) {
               this.cache.put(name, NOT_FOUND);
               throw var5;
            }

            if (q instanceof ClassType) {
               Package p = ((ClassType)q).package_();
               this.cachePackage(p);
            }

            this.addNamed(name, q);
            if (Report.should_report((Collection)TOPICS, 3)) {
               Report.report(3, "CachingResolver: loaded: " + name);
            }
         } else if (Report.should_report((Collection)TOPICS, 3)) {
            Report.report(3, "CachingResolver: cached: " + name);
         }

         if (q instanceof ParsedClassType) {
            this.extInfo.addDependencyToCurrentJob(((ParsedClassType)q).fromSource());
         }

         return q;
      }
   }

   public Type checkType(String name) {
      return (Type)this.check(name);
   }

   public Named check(String name) {
      Object o = this.cache.get(name);
      return o == NOT_FOUND ? null : (Named)this.cache.get(name);
   }

   public void install(String name, Named q) {
      this.cache.put(name, q);
   }

   public void addNamed(String name, Named q) throws SemanticException {
      this.install(name, q);
      if (q instanceof Type && this.packageExists(name)) {
         throw new SemanticException("Type \"" + name + "\" clashes with package of the same name.", q.position());
      }
   }
}
