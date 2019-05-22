package com.gzoltar.shaded.org.jacoco.core.internal.analysis;

import com.gzoltar.shaded.org.jacoco.core.analysis.IClassCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.analysis.IMethodCoverage;
import java.util.ArrayList;
import java.util.Collection;

public class ClassCoverageImpl extends SourceNodeImpl implements IClassCoverage {
   private final long id;
   private final boolean noMatch;
   private final Collection<IMethodCoverage> methods;
   private String signature;
   private String superName;
   private String[] interfaces;
   private String sourceFileName;

   public ClassCoverageImpl(String name, long id, boolean noMatch) {
      super(ICoverageNode.ElementType.CLASS, name);
      this.id = id;
      this.noMatch = noMatch;
      this.methods = new ArrayList();
      this.classCounter = CounterImpl.COUNTER_1_0;
   }

   public void addMethod(IMethodCoverage method) {
      this.methods.add(method);
      this.increment(method);
      if (this.methodCounter.getCoveredCount() > 0) {
         this.classCounter = CounterImpl.COUNTER_0_1;
      }

   }

   public void setSignature(String signature) {
      this.signature = signature;
   }

   public void setSuperName(String superName) {
      this.superName = superName;
   }

   public void setInterfaces(String[] interfaces) {
      this.interfaces = interfaces;
   }

   public void setSourceFileName(String sourceFileName) {
      this.sourceFileName = sourceFileName;
   }

   public long getId() {
      return this.id;
   }

   public boolean isNoMatch() {
      return this.noMatch;
   }

   public String getSignature() {
      return this.signature;
   }

   public String getSuperName() {
      return this.superName;
   }

   public String[] getInterfaceNames() {
      return this.interfaces;
   }

   public String getPackageName() {
      int pos = this.getName().lastIndexOf(47);
      return pos == -1 ? "" : this.getName().substring(0, pos);
   }

   public String getSourceFileName() {
      return this.sourceFileName;
   }

   public Collection<IMethodCoverage> getMethods() {
      return this.methods;
   }
}
