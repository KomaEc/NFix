package soot.dexpler.instructions;

import org.jf.dexlib2.analysis.ClassPath;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.Method;

public interface OdexInstruction {
   void deOdex(DexFile var1, Method var2, ClassPath var3);
}
