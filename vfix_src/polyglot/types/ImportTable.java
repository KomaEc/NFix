package polyglot.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import polyglot.main.Report;
import polyglot.util.CollectionUtil;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StringUtil;

public class ImportTable extends ClassResolver {
   protected TypeSystem ts;
   protected Resolver resolver;
   protected List packageImports;
   protected Map map;
   protected List lazyImports;
   protected List classImports;
   protected String sourceName;
   protected Position sourcePos;
   protected Package pkg;
   private static final Object NOT_FOUND = "NOT FOUND";
   private static final Collection TOPICS = CollectionUtil.list("types", "resolver", "imports");

   public ImportTable(TypeSystem ts, Resolver base, Package pkg) {
      this(ts, base, pkg, (String)null);
   }

   public ImportTable(TypeSystem ts, Resolver base, Package pkg, String src) {
      this.resolver = base;
      this.ts = ts;
      this.sourceName = src;
      this.sourcePos = src != null ? new Position(src) : null;
      this.pkg = pkg;
      this.map = new HashMap();
      this.packageImports = new ArrayList();
      this.lazyImports = new ArrayList();
      this.classImports = new ArrayList();
   }

   public Package package_() {
      return this.pkg;
   }

   public void addClassImport(String className) {
      if (Report.should_report((Collection)TOPICS, 2)) {
         Report.report(2, this + ": lazy import " + className);
      }

      this.lazyImports.add(className);
      this.classImports.add(className);
   }

   public void addPackageImport(String pkgName) {
      if ((this.pkg == null || !this.pkg.fullName().equals(pkgName)) && !this.ts.defaultPackageImports().contains(pkgName) && !this.packageImports.contains(pkgName)) {
         this.packageImports.add(pkgName);
      }
   }

   public List packageImports() {
      return this.packageImports;
   }

   public List classImports() {
      return this.classImports;
   }

   public String sourceName() {
      return this.sourceName;
   }

   protected Named cachedFind(String name) throws SemanticException {
      Object res = this.map.get(name);
      if (res != null) {
         return (Named)res;
      } else {
         Named t = this.resolver.find(name);
         this.map.put(name, t);
         return t;
      }
   }

   public Named find(String name) throws SemanticException {
      if (Report.should_report((Collection)TOPICS, 2)) {
         Report.report(2, this + ".find(" + name + ")");
      }

      this.lazyImport();
      if (!StringUtil.isNameShort(name)) {
         return this.resolver.find(name);
      } else {
         Object res = this.map.get(name);
         if (res != null) {
            if (res == NOT_FOUND) {
               throw new NoClassException(name, this.sourcePos);
            } else {
               return (Named)res;
            }
         } else {
            try {
               if (this.pkg != null) {
                  Named n = this.findInPkg(name, this.pkg.fullName());
                  if (n != null) {
                     if (Report.should_report((Collection)TOPICS, 3)) {
                        Report.report(3, this + ".find(" + name + "): found in current package");
                     }

                     this.map.put(name, n);
                     return n;
                  }
               }

               List imports = new ArrayList(this.packageImports.size() + 5);
               imports.addAll(this.ts.defaultPackageImports());
               imports.addAll(this.packageImports);
               Named resolved = null;
               Iterator iter = imports.iterator();

               while(iter.hasNext()) {
                  String pkgName = (String)iter.next();
                  Named n = this.findInPkg(name, pkgName);
                  if (n != null) {
                     if (resolved != null) {
                        throw new SemanticException("Reference to \"" + name + "\" is ambiguous; both " + resolved.fullName() + " and " + n.fullName() + " match.");
                     }

                     resolved = n;
                  }
               }

               if (resolved == null) {
                  resolved = this.resolver.find(name);
                  if (!this.isVisibleFrom(resolved, "")) {
                     throw new NoClassException(name, this.sourcePos);
                  }
               }

               if (Report.should_report((Collection)TOPICS, 3)) {
                  Report.report(3, this + ".find(" + name + "): found as " + resolved.fullName());
               }

               this.map.put(name, resolved);
               return resolved;
            } catch (NoClassException var8) {
               if (Report.should_report((Collection)TOPICS, 3)) {
                  Report.report(3, this + ".find(" + name + "): didn't find it");
               }

               this.map.put(name, NOT_FOUND);
               throw var8;
            }
         }
      }
   }

   protected Named findInPkg(String name, String pkgName) throws SemanticException {
      String fullName = pkgName + "." + name;

      Named n;
      try {
         n = this.resolver.find(pkgName);
         if (n instanceof ClassType) {
            n = this.ts.classContextResolver((ClassType)n).find(name);
            return n;
         }
      } catch (NoClassException var6) {
      }

      try {
         n = this.resolver.find(fullName);
         if (this.isVisibleFrom(n, pkgName)) {
            return n;
         }
      } catch (NoClassException var5) {
      }

      return null;
   }

   protected boolean isVisibleFrom(Named n, String pkgName) {
      boolean isVisible = false;
      boolean inSamePackage = this.pkg != null && this.pkg.fullName().equals(pkgName) || this.pkg == null && pkgName.equals("");
      if (n instanceof Type) {
         Type t = (Type)n;
         isVisible = !t.isClass() || t.toClass().flags().isPublic() || inSamePackage;
      } else {
         isVisible = true;
      }

      return isVisible;
   }

   protected void lazyImport() throws SemanticException {
      if (!this.lazyImports.isEmpty()) {
         for(int i = 0; i < this.lazyImports.size(); ++i) {
            String longName = (String)this.lazyImports.get(i);
            if (Report.should_report((Collection)TOPICS, 2)) {
               Report.report(2, this + ": import " + longName);
            }

            try {
               StringTokenizer st = new StringTokenizer(longName, ".");
               StringBuffer name = new StringBuffer();
               Object t = null;

               label73:
               while(true) {
                  String shortName;
                  if (st.hasMoreTokens()) {
                     shortName = st.nextToken();
                     name.append(shortName);

                     try {
                        t = this.cachedFind(name.toString());
                        if (st.hasMoreTokens()) {
                           if (!(t instanceof ClassType)) {
                              throw new InternalCompilerError("Qualified type \"" + t + "\" is not a class type.", this.sourcePos);
                           }

                           ClassType ct = (ClassType)t;

                           while(true) {
                              if (!st.hasMoreTokens()) {
                                 continue label73;
                              }

                              String n = st.nextToken();
                              t = ct = this.ts.findMemberClass(ct, n);
                              this.map.put(n, ct);
                           }
                        }
                     } catch (SemanticException var9) {
                        if (!st.hasMoreTokens()) {
                           throw var9;
                        }

                        name.append(".");
                        continue;
                     }
                  }

                  shortName = StringUtil.getShortNameComponent(longName);
                  if (Report.should_report((Collection)TOPICS, 2)) {
                     Report.report(2, this + ": import " + shortName + " as " + t);
                  }

                  if (this.map.containsKey(shortName)) {
                     Named s = (Named)this.map.get(shortName);
                     if (!this.ts.equals(s, (TypeObject)t)) {
                        throw new SemanticException("Class " + shortName + " already defined as " + this.map.get(shortName), this.sourcePos);
                     }
                  }

                  this.map.put(shortName, t);
                  break;
               }
            } catch (SemanticException var10) {
               if (var10.position == null) {
                  var10.position = this.sourcePos;
               }

               throw var10;
            }
         }

         this.lazyImports = new ArrayList();
      }
   }

   public String toString() {
      return this.sourceName != null ? "(import " + this.sourceName + ")" : "(import)";
   }
}
