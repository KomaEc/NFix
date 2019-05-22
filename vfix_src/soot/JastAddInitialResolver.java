package soot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.JastAddJ.BodyDecl;
import soot.JastAddJ.CompilationUnit;
import soot.JastAddJ.ConstructorDecl;
import soot.JastAddJ.MethodDecl;
import soot.JastAddJ.Problem;
import soot.JastAddJ.Program;
import soot.JastAddJ.TypeDecl;
import soot.javaToJimple.IInitialResolver;

public class JastAddInitialResolver implements IInitialResolver {
   private static final Logger logger = LoggerFactory.getLogger(JastAddInitialResolver.class);
   protected Map<String, CompilationUnit> classNameToCU = new HashMap();

   public JastAddInitialResolver(Singletons.Global g) {
   }

   public static JastAddInitialResolver v() {
      return G.v().soot_JastAddInitialResolver();
   }

   public void formAst(String fullPath, List<String> locations, String className) {
      Program program = SootResolver.v().getProgram();
      CompilationUnit u = program.getCachedOrLoadCompilationUnit(fullPath);
      if (u != null && !u.isResolved) {
         u.isResolved = true;
         ArrayList<Problem> errors = new ArrayList();
         u.errorCheck(errors);
         if (!errors.isEmpty()) {
            Iterator var10 = errors.iterator();

            while(var10.hasNext()) {
               Problem p = (Problem)var10.next();
               logger.debug("" + p);
            }

            throw new CompilationDeathException(0, "there were errors during parsing and/or type checking (JastAdd frontend)");
         }

         u.transformation();
         u.jimplify1phase1();
         u.jimplify1phase2();
         HashSet<SootClass> types = new HashSet();
         Iterator var8 = u.getTypeDecls().iterator();

         while(var8.hasNext()) {
            TypeDecl typeDecl = (TypeDecl)var8.next();
            this.collectTypeDecl(typeDecl, types);
         }

         if (types.isEmpty()) {
            this.classNameToCU.put(className, u);
         } else {
            var8 = types.iterator();

            while(var8.hasNext()) {
               SootClass sc = (SootClass)var8.next();
               this.classNameToCU.put(sc.getName(), u);
            }
         }
      }

   }

   private void collectTypeDecl(TypeDecl typeDecl, HashSet<SootClass> types) {
      types.add(typeDecl.getSootClassDecl());
      Iterator var3 = typeDecl.nestedTypes().iterator();

      while(var3.hasNext()) {
         TypeDecl nestedType = (TypeDecl)var3.next();
         this.collectTypeDecl(nestedType, types);
      }

   }

   private TypeDecl findNestedTypeDecl(TypeDecl typeDecl, SootClass sc) {
      if (typeDecl.sootClass() == sc) {
         return typeDecl;
      } else {
         Iterator var3 = typeDecl.nestedTypes().iterator();

         TypeDecl t;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            TypeDecl nestedType = (TypeDecl)var3.next();
            t = this.findNestedTypeDecl(nestedType, sc);
         } while(t == null);

         return t;
      }
   }

   public IInitialResolver.Dependencies resolveFromJavaFile(SootClass sootclass) {
      CompilationUnit u = (CompilationUnit)this.classNameToCU.get(sootclass.getName());
      if (u == null) {
         throw new RuntimeException("Error: couldn't find class: " + sootclass.getName() + " are the packages set properly?");
      } else {
         HashSet<SootClass> types = new HashSet();
         Iterator var4 = u.getTypeDecls().iterator();

         while(var4.hasNext()) {
            TypeDecl typeDecl = (TypeDecl)var4.next();
            this.collectTypeDecl(typeDecl, types);
         }

         IInitialResolver.Dependencies deps = new IInitialResolver.Dependencies();
         u.collectTypesToHierarchy(deps.typesToHierarchy);
         u.collectTypesToSignatures(deps.typesToSignature);
         Iterator var10 = types.iterator();

         while(var10.hasNext()) {
            SootClass sc = (SootClass)var10.next();
            Iterator var7 = sc.getMethods().iterator();

            while(var7.hasNext()) {
               SootMethod m = (SootMethod)var7.next();
               m.setSource(new MethodSource() {
                  public Body getBody(SootMethod m, String phaseName) {
                     SootClass sc = m.getDeclaringClass();
                     CompilationUnit u = (CompilationUnit)JastAddInitialResolver.this.classNameToCU.get(sc.getName());
                     Iterator var5 = u.getTypeDecls().iterator();

                     while(true) {
                        TypeDecl typeDecl;
                        do {
                           if (!var5.hasNext()) {
                              throw new RuntimeException("Could not find body for " + m.getSignature() + " in " + m.getDeclaringClass().getName());
                           }

                           typeDecl = (TypeDecl)var5.next();
                           typeDecl = JastAddInitialResolver.this.findNestedTypeDecl(typeDecl, sc);
                        } while(typeDecl == null);

                        if (typeDecl.clinit == m) {
                           typeDecl.jimplify2clinit();
                           return m.getActiveBody();
                        }

                        Iterator var7 = typeDecl.getBodyDecls().iterator();

                        while(var7.hasNext()) {
                           BodyDecl bodyDecl = (BodyDecl)var7.next();
                           if (bodyDecl instanceof MethodDecl) {
                              MethodDecl methodDecl = (MethodDecl)bodyDecl;
                              if (m.equals(methodDecl.sootMethod)) {
                                 methodDecl.jimplify2();
                                 return m.getActiveBody();
                              }
                           } else if (bodyDecl instanceof ConstructorDecl) {
                              ConstructorDecl constrDecl = (ConstructorDecl)bodyDecl;
                              if (m.equals(constrDecl.sootMethod)) {
                                 constrDecl.jimplify2();
                                 return m.getActiveBody();
                              }
                           }
                        }
                     }
                  }
               });
            }
         }

         return deps;
      }
   }
}
