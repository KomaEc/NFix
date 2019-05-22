package org.apache.maven.surefire.providerapi;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import org.apache.maven.surefire.report.ReporterException;
import org.apache.maven.surefire.suite.RunResult;
import org.apache.maven.surefire.testset.TestSetFailedException;

public interface SurefireProvider {
   Iterator getSuites();

   RunResult invoke(Object var1) throws TestSetFailedException, ReporterException, InvocationTargetException;

   void cancel();
}
