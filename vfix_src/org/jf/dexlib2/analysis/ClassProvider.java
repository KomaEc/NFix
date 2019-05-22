package org.jf.dexlib2.analysis;

import javax.annotation.Nullable;
import org.jf.dexlib2.iface.ClassDef;

public interface ClassProvider {
   @Nullable
   ClassDef getClassDef(String var1);
}
