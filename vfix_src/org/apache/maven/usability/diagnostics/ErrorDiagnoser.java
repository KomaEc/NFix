package org.apache.maven.usability.diagnostics;

public interface ErrorDiagnoser {
   String ROLE = ErrorDiagnoser.class.getName();

   boolean canDiagnose(Throwable var1);

   String diagnose(Throwable var1);
}
