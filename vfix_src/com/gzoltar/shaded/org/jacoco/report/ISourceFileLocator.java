package com.gzoltar.shaded.org.jacoco.report;

import java.io.IOException;
import java.io.Reader;

public interface ISourceFileLocator {
   Reader getSourceFile(String var1, String var2) throws IOException;

   int getTabWidth();
}
