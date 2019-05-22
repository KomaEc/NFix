package org.codehaus.groovy.transform;

import groovy.lang.GroovyClassLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.ProcessingUnit;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.codehaus.groovy.control.messages.WarningMessage;
import org.codehaus.groovy.syntax.CSTNode;

public final class ASTTransformationVisitor extends ClassCodeVisitorSupport {
   private CompilePhase phase;
   private SourceUnit source;
   private List<ASTNode[]> targetNodes;
   private Map<ASTNode, List<ASTTransformation>> transforms;
   private Map<Class<? extends ASTTransformation>, ASTTransformation> transformInstances;
   private static CompilationUnit compUnit;
   private static Set<String> globalTransformNames = new HashSet();

   private ASTTransformationVisitor(CompilePhase phase) {
      this.phase = phase;
   }

   protected SourceUnit getSourceUnit() {
      return this.source;
   }

   public void visitClass(ClassNode classNode) {
      Map<Class<? extends ASTTransformation>, Set<ASTNode>> baseTransforms = classNode.getTransforms(this.phase);
      if (!baseTransforms.isEmpty()) {
         this.transformInstances = new HashMap();
         Iterator i$ = baseTransforms.keySet().iterator();

         while(i$.hasNext()) {
            Class transformClass = (Class)i$.next();

            try {
               this.transformInstances.put(transformClass, transformClass.newInstance());
            } catch (InstantiationException var8) {
               this.source.getErrorCollector().addError(new SimpleMessage("Could not instantiate Transformation Processor " + transformClass, this.source));
            } catch (IllegalAccessException var9) {
               this.source.getErrorCollector().addError(new SimpleMessage("Could not instantiate Transformation Processor " + transformClass, this.source));
            }
         }

         this.transforms = new HashMap();
         i$ = baseTransforms.entrySet().iterator();

         Iterator i$;
         while(i$.hasNext()) {
            Entry<Class<? extends ASTTransformation>, Set<ASTNode>> entry = (Entry)i$.next();

            Object list;
            for(i$ = ((Set)entry.getValue()).iterator(); i$.hasNext(); ((List)list).add(this.transformInstances.get(entry.getKey()))) {
               ASTNode node = (ASTNode)i$.next();
               list = (List)this.transforms.get(node);
               if (list == null) {
                  list = new ArrayList();
                  this.transforms.put(node, list);
               }
            }
         }

         this.targetNodes = new LinkedList();
         super.visitClass(classNode);
         i$ = this.targetNodes.iterator();

         while(i$.hasNext()) {
            ASTNode[] node = (ASTNode[])i$.next();
            i$ = ((List)this.transforms.get(node[0])).iterator();

            while(i$.hasNext()) {
               ASTTransformation snt = (ASTTransformation)i$.next();
               snt.visit(node, this.source);
            }
         }
      }

   }

   public void visitAnnotations(AnnotatedNode node) {
      super.visitAnnotations(node);
      Iterator i$ = node.getAnnotations().iterator();

      while(i$.hasNext()) {
         AnnotationNode annotation = (AnnotationNode)i$.next();
         if (this.transforms.containsKey(annotation)) {
            this.targetNodes.add(new ASTNode[]{annotation, node});
         }
      }

   }

   public static void addPhaseOperations(final CompilationUnit compilationUnit) {
      addGlobalTransforms(compilationUnit);
      compilationUnit.addPhaseOperation((CompilationUnit.PrimaryClassNodeOperation)(new CompilationUnit.PrimaryClassNodeOperation() {
         public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
            ASTTransformationCollectorCodeVisitor collector = new ASTTransformationCollectorCodeVisitor(source, compilationUnit.getTransformLoader());
            collector.visitClass(classNode);
         }
      }), 4);
      CompilePhase[] arr$ = CompilePhase.values();
      int len$ = arr$.length;
      int i$ = 0;

      while(i$ < len$) {
         CompilePhase phase = arr$[i$];
         final ASTTransformationVisitor visitor = new ASTTransformationVisitor(phase);
         switch(phase) {
         default:
            compilationUnit.addPhaseOperation(new CompilationUnit.PrimaryClassNodeOperation() {
               public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
                  visitor.source = source;
                  visitor.visitClass(classNode);
               }
            }, phase.getPhaseNumber());
         case INITIALIZATION:
         case PARSING:
         case CONVERSION:
            ++i$;
         }
      }

   }

   public static void addGlobalTransformsAfterGrab() {
      doAddGlobalTransforms(compUnit, false);
   }

   public static void addGlobalTransforms(CompilationUnit compilationUnit) {
      compUnit = compilationUnit;
      doAddGlobalTransforms(compilationUnit, true);
   }

   private static void doAddGlobalTransforms(CompilationUnit compilationUnit, boolean isFirstScan) {
      GroovyClassLoader transformLoader = compilationUnit.getTransformLoader();
      LinkedHashMap transformNames = new LinkedHashMap();

      try {
         Enumeration globalServices = transformLoader.getResources("META-INF/services/org.codehaus.groovy.transform.ASTTransformation");

         label88:
         while(true) {
            URL service;
            BufferedReader svcIn;
            String className;
            while(true) {
               if (!globalServices.hasMoreElements()) {
                  break label88;
               }

               service = (URL)globalServices.nextElement();
               svcIn = new BufferedReader(new InputStreamReader(service.openStream()));

               try {
                  className = svcIn.readLine();
                  break;
               } catch (IOException var11) {
                  compilationUnit.getErrorCollector().addError(new SimpleMessage("IOException reading the service definition at " + service.toExternalForm() + " because of exception " + var11.toString(), (ProcessingUnit)null));
               }
            }

            while(className != null) {
               if (!className.startsWith("#") && className.length() > 0) {
                  if (transformNames.containsKey(className)) {
                     if (!service.equals(transformNames.get(className))) {
                        compilationUnit.getErrorCollector().addWarning(2, "The global transform for class " + className + " is defined in both " + ((URL)transformNames.get(className)).toExternalForm() + " and " + service.toExternalForm() + " - the former definition will be used and the latter ignored.", (CSTNode)null, (SourceUnit)null);
                     }
                  } else {
                     transformNames.put(className, service);
                  }
               }

               try {
                  className = svcIn.readLine();
               } catch (IOException var9) {
                  compilationUnit.getErrorCollector().addError(new SimpleMessage("IOException reading the service definition at " + service.toExternalForm() + " because of exception " + var9.toString(), (ProcessingUnit)null));
               }
            }
         }
      } catch (IOException var12) {
         compilationUnit.getErrorCollector().addError(new SimpleMessage("IO Exception attempting to load global transforms:" + var12.getMessage(), (ProcessingUnit)null));
      }

      try {
         Class.forName("java.lang.annotation.Annotation");
      } catch (Exception var10) {
         StringBuffer sb = new StringBuffer();
         sb.append("Global ASTTransformations are not enabled in retro builds of groovy.\n");
         sb.append("The following transformations will be ignored:");
         Iterator i$ = transformNames.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, URL> entry = (Entry)i$.next();
            sb.append('\t');
            sb.append((String)entry.getKey());
            sb.append('\n');
         }

         compilationUnit.getErrorCollector().addWarning(new WarningMessage(2, sb.toString(), (CSTNode)null, (SourceUnit)null));
         return;
      }

      Iterator it;
      Entry entry;
      if (isFirstScan) {
         it = transformNames.entrySet().iterator();

         while(it.hasNext()) {
            entry = (Entry)it.next();
            globalTransformNames.add(entry.getKey());
         }

         addPhaseOperationsForGlobalTransforms(compilationUnit, transformNames, isFirstScan);
      } else {
         it = transformNames.entrySet().iterator();

         while(it.hasNext()) {
            entry = (Entry)it.next();
            if (!globalTransformNames.add(entry.getKey())) {
               it.remove();
            }
         }

         addPhaseOperationsForGlobalTransforms(compilationUnit, transformNames, isFirstScan);
      }

   }

   private static void addPhaseOperationsForGlobalTransforms(CompilationUnit compilationUnit, Map<String, URL> transformNames, boolean isFirstScan) {
      GroovyClassLoader transformLoader = compilationUnit.getTransformLoader();
      Iterator i$ = transformNames.entrySet().iterator();

      while(i$.hasNext()) {
         Entry entry = (Entry)i$.next();

         try {
            Class gTransClass = transformLoader.loadClass((String)entry.getKey(), false, true, false);
            GroovyASTTransformation transformAnnotation = (GroovyASTTransformation)gTransClass.getAnnotation(GroovyASTTransformation.class);
            if (transformAnnotation == null) {
               compilationUnit.getErrorCollector().addWarning(new WarningMessage(2, "Transform Class " + (String)entry.getKey() + " is specified as a global transform in " + ((URL)entry.getValue()).toExternalForm() + " but it is not annotated by " + GroovyASTTransformation.class.getName() + " the global tranform associated with it may fail and cause the compilation to fail.", (CSTNode)null, (SourceUnit)null));
            } else if (ASTTransformation.class.isAssignableFrom(gTransClass)) {
               final ASTTransformation instance = (ASTTransformation)gTransClass.newInstance();
               CompilationUnit.SourceUnitOperation suOp = new CompilationUnit.SourceUnitOperation() {
                  public void call(SourceUnit source) throws CompilationFailedException {
                     instance.visit(new ASTNode[]{source.getAST()}, source);
                  }
               };
               if (isFirstScan) {
                  compilationUnit.addPhaseOperation(suOp, transformAnnotation.phase().getPhaseNumber());
               } else {
                  compilationUnit.addNewPhaseOperation(suOp, transformAnnotation.phase().getPhaseNumber());
               }
            } else {
               compilationUnit.getErrorCollector().addError(new SimpleMessage("Transform Class " + (String)entry.getKey() + " specified at " + ((URL)entry.getValue()).toExternalForm() + " is not an ASTTransformation.", (ProcessingUnit)null));
            }
         } catch (Exception var10) {
            compilationUnit.getErrorCollector().addError(new SimpleMessage("Could not instantiate global transform class " + (String)entry.getKey() + " specified at " + ((URL)entry.getValue()).toExternalForm() + "  because of exception " + var10.toString(), (ProcessingUnit)null));
         }
      }

   }
}
