package org.codehaus.groovy.control;

import groovy.lang.GroovyClassLoader;

public abstract class ProcessingUnit {
   protected int phase = 1;
   protected boolean phaseComplete;
   protected CompilerConfiguration configuration;
   protected GroovyClassLoader classLoader;
   protected ErrorCollector errorCollector;

   public ProcessingUnit(CompilerConfiguration configuration, GroovyClassLoader classLoader, ErrorCollector er) {
      this.configuration = configuration;
      this.setClassLoader(classLoader);
      this.configure(configuration == null ? new CompilerConfiguration() : configuration);
      if (er == null) {
         er = new ErrorCollector(this.getConfiguration());
      }

      this.errorCollector = er;
   }

   public void configure(CompilerConfiguration configuration) {
      this.configuration = configuration;
   }

   public CompilerConfiguration getConfiguration() {
      return this.configuration;
   }

   public void setConfiguration(CompilerConfiguration configuration) {
      this.configuration = configuration;
   }

   public GroovyClassLoader getClassLoader() {
      return this.classLoader;
   }

   public void setClassLoader(GroovyClassLoader loader) {
      ClassLoader parent = Thread.currentThread().getContextClassLoader();
      if (parent == null) {
         parent = ProcessingUnit.class.getClassLoader();
      }

      this.classLoader = loader == null ? new GroovyClassLoader(parent, this.configuration) : loader;
   }

   public int getPhase() {
      return this.phase;
   }

   public String getPhaseDescription() {
      return Phases.getDescription(this.phase);
   }

   public ErrorCollector getErrorCollector() {
      return this.errorCollector;
   }

   public void completePhase() throws CompilationFailedException {
      this.errorCollector.failIfErrors();
      this.phaseComplete = true;
   }

   public void nextPhase() throws CompilationFailedException {
      this.gotoPhase(this.phase + 1);
   }

   public void gotoPhase(int phase) throws CompilationFailedException {
      if (!this.phaseComplete) {
         this.completePhase();
      }

      this.phase = phase;
      this.phaseComplete = false;
   }
}
