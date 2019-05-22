package com.gzoltar.shaded.org.jacoco.core.analysis;

import java.util.Collection;

public interface IClassCoverage extends ISourceNode {
   long getId();

   boolean isNoMatch();

   String getSignature();

   String getSuperName();

   String[] getInterfaceNames();

   String getPackageName();

   String getSourceFileName();

   Collection<IMethodCoverage> getMethods();
}
