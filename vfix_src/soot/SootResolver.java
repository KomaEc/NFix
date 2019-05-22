package soot;

import beaver.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.JastAddJ.BytecodeParser;
import soot.JastAddJ.CompilationUnit;
import soot.JastAddJ.JastAddJavaParser;
import soot.JastAddJ.JavaParser;
import soot.JastAddJ.Program;
import soot.javaToJimple.IInitialResolver;
import soot.options.Options;
import soot.util.ConcurrentHashMultiMap;
import soot.util.MultiMap;

public class SootResolver {
   private static final Logger logger = LoggerFactory.getLogger(SootResolver.class);
   protected MultiMap<SootClass, Type> classToTypesSignature = new ConcurrentHashMultiMap();
   protected MultiMap<SootClass, Type> classToTypesHierarchy = new ConcurrentHashMultiMap();
   private final Deque<SootClass>[] worklist = new Deque[4];
   private Program program = null;

   public SootResolver(Singletons.Global g) {
      this.worklist[1] = new ArrayDeque();
      this.worklist[2] = new ArrayDeque();
      this.worklist[3] = new ArrayDeque();
   }

   protected void initializeProgram() {
      if (Options.v().src_prec() != 6) {
         this.program = new Program();
         this.program.state().reset();
         this.program.initBytecodeReader(new BytecodeParser());
         this.program.initJavaParser(new JavaParser() {
            public CompilationUnit parse(InputStream is, String fileName) throws IOException, Parser.Exception {
               return (new JastAddJavaParser()).parse(is, fileName);
            }
         });
         this.program.options().initOptions();
         this.program.options().addKeyValueOption("-classpath");
         this.program.options().setValueForOption(Scene.v().getSootClassPath(), "-classpath");
         if (Options.v().src_prec() == 4) {
            this.program.setSrcPrec(1);
         } else if (Options.v().src_prec() == 1) {
            this.program.setSrcPrec(2);
         } else if (Options.v().src_prec() == 2) {
            this.program.setSrcPrec(2);
         }

         this.program.initPaths();
      }

   }

   public static SootResolver v() {
      return G.v().soot_SootResolver();
   }

   protected boolean resolveEverything() {
      if (Options.v().on_the_fly()) {
         return false;
      } else {
         return Options.v().whole_program() || Options.v().whole_shimple() || Options.v().full_resolver() || Options.v().output_format() == 15;
      }
   }

   public SootClass makeClassRef(String className) {
      if (Scene.v().containsClass(className)) {
         return Scene.v().getSootClass(className);
      } else {
         SootClass newClass = new SootClass(className);
         newClass.setResolvingLevel(0);
         Scene.v().addClass(newClass);
         return newClass;
      }
   }

   public SootClass resolveClass(String className, int desiredLevel) {
      SootClass resolvedClass = null;

      try {
         resolvedClass = this.makeClassRef(className);
         this.addToResolveWorklist(resolvedClass, desiredLevel);
         this.processResolveWorklist();
         return resolvedClass;
      } catch (SootResolver.SootClassNotFoundException var5) {
         if (resolvedClass != null) {
            assert resolvedClass.resolvingLevel() == 0;

            Scene.v().removeClass(resolvedClass);
         }

         throw var5;
      }
   }

   protected void processResolveWorklist() {
      label73:
      for(int i = 3; i >= 1; --i) {
         while(true) {
            while(true) {
               while(true) {
                  if (this.worklist[i].isEmpty()) {
                     continue label73;
                  }

                  SootClass sc = (SootClass)this.worklist[i].pop();
                  if (this.resolveEverything()) {
                     boolean onlySignatures = sc.isPhantom() || Options.v().no_bodies_for_excluded() && Scene.v().isExcluded(sc) && !Scene.v().getBasicClasses().contains(sc.getName());
                     if (onlySignatures) {
                        this.bringToSignatures(sc);
                        sc.setPhantomClass();
                        Iterator var4 = sc.getMethods().iterator();

                        while(var4.hasNext()) {
                           SootMethod m = (SootMethod)var4.next();
                           m.setPhantom(true);
                        }

                        var4 = sc.getFields().iterator();

                        while(var4.hasNext()) {
                           SootField f = (SootField)var4.next();
                           f.setPhantom(true);
                        }
                     } else {
                        this.bringToBodies(sc);
                     }
                  } else {
                     switch(i) {
                     case 1:
                        this.bringToHierarchy(sc);
                        break;
                     case 2:
                        this.bringToSignatures(sc);
                        break;
                     case 3:
                        this.bringToBodies(sc);
                     }
                  }
               }
            }
         }
      }

   }

   protected void addToResolveWorklist(Type type, int level) {
      if (type instanceof RefType) {
         this.addToResolveWorklist(((RefType)type).getSootClass(), level);
      } else if (type instanceof ArrayType) {
         this.addToResolveWorklist(((ArrayType)type).baseType, level);
      }

   }

   protected void addToResolveWorklist(SootClass sc, int desiredLevel) {
      if (sc.resolvingLevel() < desiredLevel) {
         this.worklist[desiredLevel].add(sc);
      }
   }

   protected void bringToHierarchy(SootClass sc) {
      if (sc.resolvingLevel() < 1) {
         if (Options.v().debug_resolver()) {
            logger.debug("bringing to HIERARCHY: " + sc);
         }

         sc.setResolvingLevel(1);
         this.bringToHierarchyUnchecked(sc);
      }
   }

   protected void bringToHierarchyUnchecked(SootClass sc) {
      String className = sc.getName();
      ClassSource is = SourceLocator.v().getClassSource(className);

      try {
         boolean modelAsPhantomRef = is == null;
         if (modelAsPhantomRef) {
            if (!Scene.v().allowsPhantomRefs()) {
               String suffix = "";
               if (className.equals("java.lang.Object")) {
                  suffix = " Try adding rt.jar to Soot's classpath, e.g.:\njava -cp sootclasses.jar soot.Main -cp .:/path/to/jdk/jre/lib/rt.jar <other options>";
               } else if (className.equals("javax.crypto.Cipher")) {
                  suffix = " Try adding jce.jar to Soot's classpath, e.g.:\njava -cp sootclasses.jar soot.Main -cp .:/path/to/jdk/jre/lib/rt.jar:/path/to/jdk/jre/lib/jce.jar <other options>";
               }

               throw new SootResolver.SootClassNotFoundException("couldn't find class: " + className + " (is your soot-class-path set properly?)" + suffix);
            }

            sc.setPhantomClass();
         } else {
            IInitialResolver.Dependencies dependencies = is.resolve(sc);
            if (!dependencies.typesToSignature.isEmpty()) {
               this.classToTypesSignature.putAll(sc, dependencies.typesToSignature);
            }

            if (!dependencies.typesToHierarchy.isEmpty()) {
               this.classToTypesHierarchy.putAll(sc, dependencies.typesToHierarchy);
            }
         }
      } finally {
         if (is != null) {
            is.close();
         }

      }

      this.reResolveHierarchy(sc, 1);
   }

   public void reResolveHierarchy(SootClass sc, int level) {
      SootClass superClass = sc.getSuperclassUnsafe();
      if (superClass != null) {
         this.addToResolveWorklist(superClass, level);
      }

      SootClass outerClass = sc.getOuterClassUnsafe();
      if (outerClass != null) {
         this.addToResolveWorklist(outerClass, level);
      }

      Iterator var5 = sc.getInterfaces().iterator();

      while(var5.hasNext()) {
         SootClass iface = (SootClass)var5.next();
         this.addToResolveWorklist(iface, level);
      }

   }

   protected void bringToSignatures(SootClass sc) {
      if (sc.resolvingLevel() < 2) {
         this.bringToHierarchy(sc);
         if (Options.v().debug_resolver()) {
            logger.debug("bringing to SIGNATURES: " + sc);
         }

         sc.setResolvingLevel(2);
         this.bringToSignaturesUnchecked(sc);
      }
   }

   protected void bringToSignaturesUnchecked(SootClass sc) {
      Iterator var2 = sc.getFields().iterator();

      while(var2.hasNext()) {
         SootField f = (SootField)var2.next();
         this.addToResolveWorklist((Type)f.getType(), 1);
      }

      var2 = sc.getMethods().iterator();

      while(true) {
         List exceptions;
         do {
            if (!var2.hasNext()) {
               this.reResolveHierarchy(sc, 2);
               return;
            }

            SootMethod m = (SootMethod)var2.next();
            this.addToResolveWorklist((Type)m.getReturnType(), 1);
            Iterator var4 = m.getParameterTypes().iterator();

            while(var4.hasNext()) {
               Type ptype = (Type)var4.next();
               this.addToResolveWorklist((Type)ptype, 1);
            }

            exceptions = m.getExceptionsUnsafe();
         } while(exceptions == null);

         Iterator var9 = exceptions.iterator();

         while(var9.hasNext()) {
            SootClass exception = (SootClass)var9.next();
            this.addToResolveWorklist((SootClass)exception, 1);
         }
      }
   }

   protected void bringToBodies(SootClass sc) {
      if (sc.resolvingLevel() < 3) {
         this.bringToSignatures(sc);
         if (Options.v().debug_resolver()) {
            logger.debug("bringing to BODIES: " + sc);
         }

         sc.setResolvingLevel(3);
         this.bringToBodiesUnchecked(sc);
      }
   }

   protected void bringToBodiesUnchecked(SootClass sc) {
      Collection<Type> references = this.classToTypesHierarchy.get(sc);
      Iterator it;
      Type t;
      if (references != null) {
         it = references.iterator();

         while(it.hasNext()) {
            t = (Type)it.next();
            this.addToResolveWorklist((Type)t, 1);
         }
      }

      references = this.classToTypesSignature.get(sc);
      if (references != null) {
         it = references.iterator();

         while(it.hasNext()) {
            t = (Type)it.next();
            this.addToResolveWorklist((Type)t, 2);
         }
      }

   }

   public void reResolve(SootClass cl, int newResolvingLevel) {
      int resolvingLevel = cl.resolvingLevel();
      if (resolvingLevel < newResolvingLevel) {
         this.reResolveHierarchy(cl, 1);
         cl.setResolvingLevel(newResolvingLevel);
         this.addToResolveWorklist(cl, resolvingLevel);
         this.processResolveWorklist();
      }
   }

   public void reResolve(SootClass cl) {
      this.reResolve(cl, 1);
   }

   public Program getProgram() {
      if (this.program == null) {
         this.initializeProgram();
      }

      return this.program;
   }

   public class SootClassNotFoundException extends RuntimeException {
      private static final long serialVersionUID = 1563461446590293827L;

      private SootClassNotFoundException(String s) {
         super(s);
      }

      // $FF: synthetic method
      SootClassNotFoundException(String x1, Object x2) {
         this(x1);
      }
   }
}
