package soot.jimple.toolkits.pointer.representations;

import soot.G;
import soot.Singletons;

public class Environment {
   private final ConstantObject clsloaders;
   private final ConstantObject processes;
   private final ConstantObject threads;
   private final ConstantObject filesystem;
   private final ConstantObject classobject;
   private final ConstantObject stringobject;
   private final ConstantObject leastarray;
   private final ConstantObject leastobject;
   private final ConstantObject fieldobject;
   private final ConstantObject methodobject;
   private final ConstantObject constructorobject;
   private final ConstantObject privilegedActionException;

   public Environment(Singletons.Global g) {
      this.clsloaders = new GeneralConstObject(TypeConstants.v().CLASSLOADERCLASS, "classloader");
      this.processes = new GeneralConstObject(TypeConstants.v().PROCESSCLASS, "process");
      this.threads = new GeneralConstObject(TypeConstants.v().THREADCLASS, "thread");
      this.filesystem = new GeneralConstObject(TypeConstants.v().FILESYSTEMCLASS, "filesystem");
      this.classobject = new GeneralConstObject(TypeConstants.v().CLASSCLASS, "unknownclass");
      this.stringobject = new GeneralConstObject(TypeConstants.v().STRINGCLASS, "unknownstring");
      this.leastarray = new GeneralConstObject(TypeConstants.v().LEASTCLASS, "leastarray");
      this.leastobject = new GeneralConstObject(TypeConstants.v().LEASTCLASS, "leastobject");
      this.fieldobject = new GeneralConstObject(TypeConstants.v().FIELDCLASS, "field");
      this.methodobject = new GeneralConstObject(TypeConstants.v().METHODCLASS, "method");
      this.constructorobject = new GeneralConstObject(TypeConstants.v().CONSTRUCTORCLASS, "constructor");
      this.privilegedActionException = new GeneralConstObject(TypeConstants.v().PRIVILEGEDACTIONEXCEPTION, "constructor");
   }

   public static Environment v() {
      return G.v().soot_jimple_toolkits_pointer_representations_Environment();
   }

   public ConstantObject getClassLoaderObject() {
      return this.clsloaders;
   }

   public ConstantObject getProcessObject() {
      return this.processes;
   }

   public ConstantObject getThreadObject() {
      return this.threads;
   }

   public ConstantObject getClassObject() {
      return this.classobject;
   }

   public ConstantObject getStringObject() {
      return this.stringobject;
   }

   public ConstantObject getLeastArrayObject() {
      return this.leastarray;
   }

   public ConstantObject getLeastObject() {
      return this.leastobject;
   }

   public ConstantObject getFieldObject() {
      return this.fieldobject;
   }

   public ConstantObject getMethodObject() {
      return this.methodobject;
   }

   public ConstantObject getConstructorObject() {
      return this.constructorobject;
   }

   public ConstantObject getFileSystemObject() {
      return this.filesystem;
   }

   public ConstantObject getPrivilegedActionExceptionObject() {
      return this.privilegedActionException;
   }
}
