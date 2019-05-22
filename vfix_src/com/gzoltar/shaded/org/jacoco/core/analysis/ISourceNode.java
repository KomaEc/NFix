package com.gzoltar.shaded.org.jacoco.core.analysis;

public interface ISourceNode extends ICoverageNode {
   int UNKNOWN_LINE = -1;

   int getFirstLine();

   int getLastLine();

   ILine getLine(int var1);
}
