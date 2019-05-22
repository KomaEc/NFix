package com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter;

import com.gzoltar.shaded.org.objectweb.asm.tree.MethodNode;

public interface IFilter {
   void filter(String var1, String var2, MethodNode var3, IFilterOutput var4);
}
