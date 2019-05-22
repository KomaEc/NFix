package com.gzoltar.shaded.org.objectweb.asm.util;

import com.gzoltar.shaded.org.objectweb.asm.Label;
import java.util.Map;

public interface ASMifiable {
   void asmify(StringBuffer var1, String var2, Map<Label, String> var3);
}
