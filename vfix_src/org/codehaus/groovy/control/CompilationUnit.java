package org.codehaus.groovy.control;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyRuntimeException;
import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.ClassWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.classgen.AsmClassGenerator;
import org.codehaus.groovy.classgen.ClassCompletionVerifier;
import org.codehaus.groovy.classgen.ClassGenerator;
import org.codehaus.groovy.classgen.EnumVisitor;
import org.codehaus.groovy.classgen.ExtendedVerifier;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.classgen.InnerClassVisitor;
import org.codehaus.groovy.classgen.VariableScopeVisitor;
import org.codehaus.groovy.classgen.Verifier;
import org.codehaus.groovy.control.io.InputStreamReaderSource;
import org.codehaus.groovy.control.io.ReaderSource;
import org.codehaus.groovy.control.messages.ExceptionMessage;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.tools.GroovyClass;
import org.codehaus.groovy.transform.ASTTransformationVisitor;

public class CompilationUnit extends ProcessingUnit {
   private GroovyClassLoader transformLoader;
   protected Map<String, SourceUnit> sources;
   protected Map summariesBySourceName;
   protected Map summariesByPublicClassName;
   protected Map classSourcesByPublicClassName;
   protected List<String> names;
   protected LinkedList<SourceUnit> queuedSources;
   protected CompileUnit ast;
   protected List<GroovyClass> generatedClasses;
   protected Verifier verifier;
   protected boolean debug;
   protected boolean configured;
   protected CompilationUnit.ClassgenCallback classgenCallback;
   protected CompilationUnit.ProgressCallback progressCallback;
   protected ResolveVisitor resolveVisitor;
   protected StaticImportVisitor staticImportVisitor;
   protected OptimizerVisitor optimizer;
   LinkedList[] phaseOperations;
   LinkedList[] newPhaseOperations;
   private final CompilationUnit.SourceUnitOperation resolve;
   private CompilationUnit.PrimaryClassNodeOperation staticImport;
   private CompilationUnit.SourceUnitOperation convert;
   private CompilationUnit.GroovyClassOperation output;
   private CompilationUnit.SourceUnitOperation compileCompleteCheck;
   private CompilationUnit.PrimaryClassNodeOperation classgen;
   private CompilationUnit.SourceUnitOperation mark;

   public CompilationUnit() {
      this((CompilerConfiguration)null, (CodeSource)null, (GroovyClassLoader)null);
   }

   public CompilationUnit(GroovyClassLoader loader) {
      this((CompilerConfiguration)null, (CodeSource)null, loader);
   }

   public CompilationUnit(CompilerConfiguration configuration) {
      this(configuration, (CodeSource)null, (GroovyClassLoader)null);
   }

   public CompilationUnit(CompilerConfiguration configuration, CodeSource security, GroovyClassLoader loader) {
      this(configuration, security, loader, (GroovyClassLoader)null);
   }

   public CompilationUnit(CompilerConfiguration configuration, CodeSource security, GroovyClassLoader loader, GroovyClassLoader transformLoader) {
      super(configuration, loader, (ErrorCollector)null);
      this.resolve = new CompilationUnit.SourceUnitOperation() {
         public void call(SourceUnit source) throws CompilationFailedException {
            List<ClassNode> classes = source.ast.getClasses();
            Iterator i$ = classes.iterator();

            while(i$.hasNext()) {
               ClassNode node = (ClassNode)i$.next();
               VariableScopeVisitor scopeVisitor = new VariableScopeVisitor(source);
               scopeVisitor.visitClass(node);
               CompilationUnit.this.resolveVisitor.startResolving(node, source);
            }

         }
      };
      this.staticImport = new CompilationUnit.PrimaryClassNodeOperation() {
         public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
            CompilationUnit.this.staticImportVisitor.visitClass(classNode, source);
         }
      };
      this.convert = new CompilationUnit.SourceUnitOperation() {
         public void call(SourceUnit source) throws CompilationFailedException {
            source.convert();
            CompilationUnit.this.ast.addModule(source.getAST());
            if (CompilationUnit.this.progressCallback != null) {
               CompilationUnit.this.progressCallback.call(source, CompilationUnit.this.phase);
            }

         }
      };
      this.output = new CompilationUnit.GroovyClassOperation() {
         public void call(GroovyClass gclass) throws CompilationFailedException {
            boolean failures = false;
            String name = gclass.getName().replace('.', File.separatorChar) + ".class";
            File path = new File(CompilationUnit.this.configuration.getTargetDirectory(), name);
            File directory = path.getParentFile();
            if (directory != null && !directory.exists()) {
               directory.mkdirs();
            }

            byte[] bytes = gclass.getBytes();
            FileOutputStream stream = null;

            try {
               stream = new FileOutputStream(path);
               stream.write(bytes, 0, bytes.length);
            } catch (IOException var17) {
               CompilationUnit.this.getErrorCollector().addError(Message.create((String)var17.getMessage(), (ProcessingUnit)CompilationUnit.this));
               failures = true;
            } finally {
               if (stream != null) {
                  try {
                     stream.close();
                  } catch (Exception var16) {
                  }
               }

            }

         }
      };
      this.compileCompleteCheck = new CompilationUnit.SourceUnitOperation() {
         public void call(SourceUnit source) throws CompilationFailedException {
            List<ClassNode> classes = source.ast.getClasses();
            Iterator i$x = classes.iterator();

            while(i$x.hasNext()) {
               ClassNode node = (ClassNode)i$x.next();
               CompileUnit cu = node.getCompileUnit();
               Iterator iter = cu.iterateClassNodeToCompile();

               while(iter.hasNext()) {
                  String name = (String)iter.next();
                  SourceUnit su = CompilationUnit.this.ast.getScriptSourceLocation(name);
                  List<ClassNode> classesInSourceUnit = su.ast.getClasses();
                  StringBuffer message = new StringBuffer();
                  message.append("Compilation incomplete: expected to find the class ").append(name).append(" in ").append(su.getName());
                  if (classesInSourceUnit.isEmpty()) {
                     message.append(", but the file seems not to contain any classes");
                  } else {
                     message.append(", but the file contains the classes: ");
                     boolean first = true;

                     ClassNode cn;
                     for(Iterator i$ = classesInSourceUnit.iterator(); i$.hasNext(); message.append(cn.getName())) {
                        cn = (ClassNode)i$.next();
                        if (!first) {
                           message.append(", ");
                        } else {
                           first = false;
                        }
                     }
                  }

                  CompilationUnit.this.getErrorCollector().addErrorAndContinue(new SimpleMessage(message.toString(), CompilationUnit.this));
                  iter.remove();
               }
            }

         }
      };
      this.classgen = new CompilationUnit.PrimaryClassNodeOperation() {
         public boolean needSortedInput() {
            return true;
         }

         public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
            CompilationUnit.this.optimizer.visitClass(classNode, source);
            if (!classNode.isSynthetic()) {
               GenericsVisitor genericsVisitor = new GenericsVisitor(source);
               genericsVisitor.visitClass(classNode);
            }

            try {
               CompilationUnit.this.verifier.visitClass(classNode);
            } catch (GroovyRuntimeException var12) {
               ASTNode node = var12.getNode();
               CompilationUnit.this.getErrorCollector().addError(new SyntaxException(var12.getMessage(), (Throwable)null, node.getLineNumber(), node.getColumnNumber()), source);
            }

            LabelVerifier lv = new LabelVerifier(source);
            lv.visitClass(classNode);
            ClassCompletionVerifier completionVerifier = new ClassCompletionVerifier(source);
            completionVerifier.visitClass(classNode);
            ExtendedVerifier xverifier = new ExtendedVerifier(source);
            xverifier.visitClass(classNode);
            CompilationUnit.this.getErrorCollector().failIfErrors();
            ClassVisitor visitor = CompilationUnit.this.createClassVisitor();
            String sourceName = source == null ? classNode.getModule().getDescription() : source.getName();
            if (sourceName != null) {
               sourceName = sourceName.substring(Math.max(sourceName.lastIndexOf(92), sourceName.lastIndexOf(47)) + 1);
            }

            ClassGenerator generator = new AsmClassGenerator(source, context, visitor, CompilationUnit.this.classLoader, sourceName);
            generator.visitClass(classNode);
            byte[] bytes = ((ClassWriter)visitor).toByteArray();
            CompilationUnit.this.generatedClasses.add(new GroovyClass(classNode.getName(), bytes));
            if (CompilationUnit.this.classgenCallback != null) {
               CompilationUnit.this.classgenCallback.call(visitor, classNode);
            }

            LinkedList innerClasses = generator.getInnerClasses();

            while(!innerClasses.isEmpty()) {
               CompilationUnit.this.classgen.call(source, context, (ClassNode)innerClasses.removeFirst());
            }

         }
      };
      this.mark = new CompilationUnit.SourceUnitOperation() {
         public void call(SourceUnit source) throws CompilationFailedException {
            if (source.phase < CompilationUnit.this.phase) {
               source.gotoPhase(CompilationUnit.this.phase);
            }

            if (source.phase == CompilationUnit.this.phase && CompilationUnit.this.phaseComplete && !source.phaseComplete) {
               source.completePhase();
            }

         }
      };
      this.transformLoader = transformLoader;
      this.names = new ArrayList();
      this.queuedSources = new LinkedList();
      this.sources = new HashMap();
      this.summariesBySourceName = new HashMap();
      this.summariesByPublicClassName = new HashMap();
      this.classSourcesByPublicClassName = new HashMap();
      this.ast = new CompileUnit(this.classLoader, security, this.configuration);
      this.generatedClasses = new ArrayList();
      this.verifier = new Verifier();
      this.resolveVisitor = new ResolveVisitor(this);
      this.staticImportVisitor = new StaticImportVisitor(this);
      this.optimizer = new OptimizerVisitor(this);
      this.phaseOperations = new LinkedList[10];
      this.newPhaseOperations = new LinkedList[10];

      for(int i = 0; i < this.phaseOperations.length; ++i) {
         this.phaseOperations[i] = new LinkedList();
         this.newPhaseOperations[i] = new LinkedList();
      }

      this.addPhaseOperation((CompilationUnit.SourceUnitOperation)(new CompilationUnit.SourceUnitOperation() {
         public void call(SourceUnit source) throws CompilationFailedException {
            source.parse();
         }
      }), 2);
      this.addPhaseOperation((CompilationUnit.SourceUnitOperation)this.convert, 3);
      this.addPhaseOperation((CompilationUnit.PrimaryClassNodeOperation)(new CompilationUnit.PrimaryClassNodeOperation() {
         public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
            EnumVisitor ev = new EnumVisitor(CompilationUnit.this, source);
            ev.visitClass(classNode);
         }
      }), 3);
      this.addPhaseOperation((CompilationUnit.SourceUnitOperation)this.resolve, 4);
      this.addPhaseOperation((CompilationUnit.PrimaryClassNodeOperation)this.staticImport, 4);
      this.addPhaseOperation((CompilationUnit.PrimaryClassNodeOperation)(new CompilationUnit.PrimaryClassNodeOperation() {
         public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
            InnerClassVisitor iv = new InnerClassVisitor(CompilationUnit.this, source);
            iv.visitClass(classNode);
         }
      }), 4);
      this.addPhaseOperation((CompilationUnit.SourceUnitOperation)this.compileCompleteCheck, 5);
      this.addPhaseOperation((CompilationUnit.PrimaryClassNodeOperation)this.classgen, 7);
      this.addPhaseOperation(this.output);
      ASTTransformationVisitor.addPhaseOperations(this);
      this.classgenCallback = null;
   }

   public GroovyClassLoader getTransformLoader() {
      return this.transformLoader == null ? this.getClassLoader() : this.transformLoader;
   }

   public void addPhaseOperation(CompilationUnit.SourceUnitOperation op, int phase) {
      if (phase >= 0 && phase <= 9) {
         this.phaseOperations[phase].add(op);
      } else {
         throw new IllegalArgumentException("phase " + phase + " is unknown");
      }
   }

   public void addPhaseOperation(CompilationUnit.PrimaryClassNodeOperation op, int phase) {
      if (phase >= 0 && phase <= 9) {
         this.phaseOperations[phase].add(op);
      } else {
         throw new IllegalArgumentException("phase " + phase + " is unknown");
      }
   }

   public void addPhaseOperation(CompilationUnit.GroovyClassOperation op) {
      this.phaseOperations[8].addFirst(op);
   }

   public void addNewPhaseOperation(CompilationUnit.SourceUnitOperation op, int phase) {
      if (phase >= 0 && phase <= 9) {
         this.newPhaseOperations[phase].add(op);
      } else {
         throw new IllegalArgumentException("phase " + phase + " is unknown");
      }
   }

   public void configure(CompilerConfiguration configuration) {
      super.configure(configuration);
      this.debug = configuration.getDebug();
      if (!this.configured && this.classLoader instanceof GroovyClassLoader) {
         this.appendCompilerConfigurationClasspathToClassLoader(configuration, this.classLoader);
      }

      this.configured = true;
   }

   private void appendCompilerConfigurationClasspathToClassLoader(CompilerConfiguration configuration, GroovyClassLoader classLoader) {
   }

   public CompileUnit getAST() {
      return this.ast;
   }

   public Map getSummariesBySourceName() {
      return this.summariesBySourceName;
   }

   public Map getSummariesByPublicClassName() {
      return this.summariesByPublicClassName;
   }

   public Map getClassSourcesByPublicClassName() {
      return this.classSourcesByPublicClassName;
   }

   public boolean isPublicClass(String className) {
      return this.summariesByPublicClassName.containsKey(className);
   }

   public List getClasses() {
      return this.generatedClasses;
   }

   public ClassNode getFirstClassNode() {
      return (ClassNode)((ModuleNode)this.ast.getModules().get(0)).getClasses().get(0);
   }

   public ClassNode getClassNode(final String name) {
      final ClassNode[] result = new ClassNode[]{null};
      CompilationUnit.PrimaryClassNodeOperation handler = new CompilationUnit.PrimaryClassNodeOperation() {
         public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) {
            if (classNode.getName().equals(name)) {
               result[0] = classNode;
            }

         }
      };

      try {
         this.applyToPrimaryClassNodes(handler);
      } catch (CompilationFailedException var5) {
         if (this.debug) {
            var5.printStackTrace();
         }
      }

      return result[0];
   }

   public void addSources(String[] paths) {
      String[] arr$ = paths;
      int len$ = paths.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String path = arr$[i$];
         this.addSource(new File(path));
      }

   }

   public void addSources(File[] files) {
      File[] arr$ = files;
      int len$ = files.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         File file = arr$[i$];
         this.addSource(file);
      }

   }

   public SourceUnit addSource(File file) {
      return this.addSource(new SourceUnit(file, this.configuration, this.classLoader, this.getErrorCollector()));
   }

   public SourceUnit addSource(URL url) {
      return this.addSource(new SourceUnit(url, this.configuration, this.classLoader, this.getErrorCollector()));
   }

   public SourceUnit addSource(String name, InputStream stream) {
      ReaderSource source = new InputStreamReaderSource(stream, this.configuration);
      return this.addSource(new SourceUnit(name, source, this.configuration, this.classLoader, this.getErrorCollector()));
   }

   public SourceUnit addSource(String name, String scriptText) {
      return this.addSource(new SourceUnit(name, scriptText, this.configuration, this.classLoader, this.getErrorCollector()));
   }

   public SourceUnit addSource(SourceUnit source) {
      String name = source.getName();
      source.setClassLoader(this.classLoader);
      Iterator i$ = this.queuedSources.iterator();

      SourceUnit su;
      do {
         if (!i$.hasNext()) {
            this.queuedSources.add(source);
            return source;
         }

         su = (SourceUnit)i$.next();
      } while(!name.equals(su.getName()));

      return su;
   }

   public Iterator<SourceUnit> iterator() {
      return new Iterator<SourceUnit>() {
         Iterator<String> nameIterator;

         {
            this.nameIterator = CompilationUnit.this.names.iterator();
         }

         public boolean hasNext() {
            return this.nameIterator.hasNext();
         }

         public SourceUnit next() {
            String name = (String)this.nameIterator.next();
            return (SourceUnit)CompilationUnit.this.sources.get(name);
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public void addClassNode(ClassNode node) {
      ModuleNode module = new ModuleNode(this.ast);
      this.ast.addModule(module);
      module.addClass(node);
   }

   public void setClassgenCallback(CompilationUnit.ClassgenCallback visitor) {
      this.classgenCallback = visitor;
   }

   public void setProgressCallback(CompilationUnit.ProgressCallback callback) {
      this.progressCallback = callback;
   }

   public void compile() throws CompilationFailedException {
      this.compile(9);
   }

   public void compile(int throughPhase) throws CompilationFailedException {
      this.gotoPhase(1);
      throughPhase = Math.min(throughPhase, 9);

      while(throughPhase >= this.phase && this.phase <= 9) {
         if (this.phase == 4) {
            this.doPhaseOperation(this.resolve);
            if (this.dequeued()) {
               continue;
            }
         }

         this.processPhaseOperations(this.phase);
         this.processNewPhaseOperations(this.phase);
         if (this.progressCallback != null) {
            this.progressCallback.call(this, this.phase);
         }

         this.completePhase();
         this.applyToSourceUnits(this.mark);
         if (!this.dequeued()) {
            this.gotoPhase(this.phase + 1);
            if (this.phase == 7) {
               this.sortClasses();
            }
         }
      }

      this.errorCollector.failIfErrors();
   }

   private void processPhaseOperations(int ph) {
      LinkedList ops = this.phaseOperations[ph];
      Iterator i$ = ops.iterator();

      while(i$.hasNext()) {
         Object next = i$.next();
         this.doPhaseOperation(next);
      }

   }

   private void processNewPhaseOperations(int currPhase) {
      this.recordPhaseOpsInAllOtherPhases(currPhase);

      for(LinkedList currentPhaseNewOps = this.newPhaseOperations[currPhase]; !currentPhaseNewOps.isEmpty(); currentPhaseNewOps = this.newPhaseOperations[currPhase]) {
         Object operation = currentPhaseNewOps.removeFirst();
         this.phaseOperations[currPhase].add(operation);
         this.doPhaseOperation(operation);
         this.recordPhaseOpsInAllOtherPhases(currPhase);
      }

   }

   private void doPhaseOperation(Object operation) {
      if (operation instanceof CompilationUnit.PrimaryClassNodeOperation) {
         this.applyToPrimaryClassNodes((CompilationUnit.PrimaryClassNodeOperation)operation);
      } else if (operation instanceof CompilationUnit.SourceUnitOperation) {
         this.applyToSourceUnits((CompilationUnit.SourceUnitOperation)operation);
      } else {
         this.applyToGeneratedGroovyClasses((CompilationUnit.GroovyClassOperation)operation);
      }

   }

   private void recordPhaseOpsInAllOtherPhases(int currPhase) {
      for(int ph = 1; ph <= 9; ++ph) {
         if (ph != currPhase && !this.newPhaseOperations[ph].isEmpty()) {
            this.phaseOperations[ph].addAll(this.newPhaseOperations[ph]);
            this.newPhaseOperations[ph].clear();
         }
      }

   }

   private void sortClasses() throws CompilationFailedException {
      Iterator i$ = this.ast.getModules().iterator();

      while(i$.hasNext()) {
         ModuleNode module = (ModuleNode)i$.next();
         module.sortClasses();
      }

   }

   protected boolean dequeued() throws CompilationFailedException {
      boolean dequeue = !this.queuedSources.isEmpty();

      while(!this.queuedSources.isEmpty()) {
         SourceUnit su = (SourceUnit)this.queuedSources.removeFirst();
         String name = su.getName();
         this.names.add(name);
         this.sources.put(name, su);
      }

      if (dequeue) {
         this.gotoPhase(1);
      }

      return dequeue;
   }

   protected ClassVisitor createClassVisitor() {
      return new ClassWriter(1);
   }

   protected void mark() throws CompilationFailedException {
      this.applyToSourceUnits(this.mark);
   }

   public void applyToSourceUnits(CompilationUnit.SourceUnitOperation body) throws CompilationFailedException {
      Iterator i$ = this.names.iterator();

      while(true) {
         SourceUnit source;
         do {
            if (!i$.hasNext()) {
               this.getErrorCollector().failIfErrors();
               return;
            }

            String name = (String)i$.next();
            source = (SourceUnit)this.sources.get(name);
         } while(source.phase >= this.phase && (source.phase != this.phase || source.phaseComplete));

         try {
            body.call(source);
         } catch (CompilationFailedException var7) {
            throw var7;
         } catch (Exception var8) {
            GroovyBugError gbe = new GroovyBugError(var8);
            this.changeBugText(gbe, source);
            throw gbe;
         } catch (GroovyBugError var9) {
            this.changeBugText(var9, source);
            throw var9;
         }
      }
   }

   private int getSuperClassCount(ClassNode element) {
      int count;
      for(count = 0; element != null; element = element.getSuperClass()) {
         ++count;
      }

      return count;
   }

   private int getSuperInterfaceCount(ClassNode element) {
      int count = 1;
      ClassNode[] interfaces = element.getInterfaces();
      ClassNode[] arr$ = interfaces;
      int len$ = interfaces.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ClassNode anInterface = arr$[i$];
         count = Math.max(count, this.getSuperInterfaceCount(anInterface) + 1);
      }

      return count;
   }

   private List getPrimaryClassNodes(boolean sort) {
      List<ClassNode> unsorted = new ArrayList();
      Iterator i$ = this.ast.getModules().iterator();

      while(i$.hasNext()) {
         ModuleNode module = (ModuleNode)i$.next();
         Iterator i$ = module.getClasses().iterator();

         while(i$.hasNext()) {
            ClassNode classNode = (ClassNode)i$.next();
            unsorted.add(classNode);
         }
      }

      if (!sort) {
         return unsorted;
      } else {
         int[] indexClass = new int[unsorted.size()];
         int[] indexInterface = new int[unsorted.size()];
         int i = 0;

         for(Iterator iter = unsorted.iterator(); iter.hasNext(); ++i) {
            ClassNode element = (ClassNode)iter.next();
            if (element.isInterface()) {
               indexInterface[i] = this.getSuperInterfaceCount(element);
               indexClass[i] = -1;
            } else {
               indexClass[i] = this.getSuperClassCount(element);
               indexInterface[i] = -1;
            }
         }

         List<ClassNode> sorted = this.getSorted(indexInterface, unsorted);
         sorted.addAll(this.getSorted(indexClass, unsorted));
         return sorted;
      }
   }

   private List<ClassNode> getSorted(int[] index, List<ClassNode> unsorted) {
      List<ClassNode> sorted = new ArrayList(unsorted.size());

      for(int i = 0; i < unsorted.size(); ++i) {
         int min = -1;

         for(int j = 0; j < unsorted.size(); ++j) {
            if (index[j] != -1) {
               if (min == -1) {
                  min = j;
               } else if (index[j] < index[min]) {
                  min = j;
               }
            }
         }

         if (min == -1) {
            break;
         }

         sorted.add(unsorted.get(min));
         index[min] = -1;
      }

      return sorted;
   }

   public void applyToPrimaryClassNodes(CompilationUnit.PrimaryClassNodeOperation body) throws CompilationFailedException {
      Iterator classNodes = this.getPrimaryClassNodes(body.needSortedInput()).iterator();

      while(classNodes.hasNext()) {
         SourceUnit context = null;

         try {
            ClassNode classNode = (ClassNode)classNodes.next();
            context = classNode.getModule().getContext();
            if (context == null || context.phase < this.phase || context.phase == this.phase && !context.phaseComplete) {
               body.call(context, new GeneratorContext(this.ast), classNode);
            }
         } catch (CompilationFailedException var8) {
         } catch (NullPointerException var9) {
            throw var9;
         } catch (GroovyBugError var10) {
            this.changeBugText(var10, context);
            throw var10;
         } catch (Exception var11) {
            Exception e = var11;
            ErrorCollector nestedCollector = null;

            for(Throwable next = var11.getCause(); next != e && next != null; next = next.getCause()) {
               if (next instanceof MultipleCompilationErrorsException) {
                  MultipleCompilationErrorsException mcee = (MultipleCompilationErrorsException)next;
                  nestedCollector = mcee.collector;
                  break;
               }
            }

            if (nestedCollector != null) {
               this.getErrorCollector().addCollectorContents(nestedCollector);
            } else {
               this.getErrorCollector().addError(new ExceptionMessage(e, this.configuration.getDebug(), this));
            }
         }
      }

      this.getErrorCollector().failIfErrors();
   }

   public void applyToGeneratedGroovyClasses(CompilationUnit.GroovyClassOperation body) throws CompilationFailedException {
      if (this.phase == 8 || this.phase == 7 && this.phaseComplete) {
         Iterator i$ = this.generatedClasses.iterator();

         while(i$.hasNext()) {
            GroovyClass gclass = (GroovyClass)i$.next();

            try {
               body.call(gclass);
            } catch (CompilationFailedException var5) {
            } catch (NullPointerException var6) {
               throw var6;
            } catch (GroovyBugError var7) {
               this.changeBugText(var7, (SourceUnit)null);
               throw var7;
            } catch (Exception var8) {
               throw new GroovyBugError(var8);
            }
         }

         this.getErrorCollector().failIfErrors();
      } else {
         throw new GroovyBugError("CompilationUnit not ready for output(). Current phase=" + this.getPhaseDescription());
      }
   }

   private void changeBugText(GroovyBugError e, SourceUnit context) {
      e.setBugText("exception in phase '" + this.getPhaseDescription() + "' in source unit '" + (context != null ? context.getName() : "?") + "' " + e.getBugText());
   }

   public abstract static class GroovyClassOperation {
      public abstract void call(GroovyClass var1) throws CompilationFailedException;
   }

   public abstract static class PrimaryClassNodeOperation {
      public abstract void call(SourceUnit var1, GeneratorContext var2, ClassNode var3) throws CompilationFailedException;

      public boolean needSortedInput() {
         return false;
      }
   }

   public abstract static class SourceUnitOperation {
      public abstract void call(SourceUnit var1) throws CompilationFailedException;
   }

   public abstract static class ProgressCallback {
      public abstract void call(ProcessingUnit var1, int var2) throws CompilationFailedException;
   }

   public abstract static class ClassgenCallback {
      public abstract void call(ClassVisitor var1, ClassNode var2) throws CompilationFailedException;
   }
}
