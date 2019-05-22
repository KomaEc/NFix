package com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter;

import com.gzoltar.shaded.org.objectweb.asm.tree.AbstractInsnNode;

public interface IFilterOutput {
   void ignore(AbstractInsnNode var1, AbstractInsnNode var2);
}
