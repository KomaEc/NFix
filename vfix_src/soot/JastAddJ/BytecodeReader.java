package soot.JastAddJ;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface BytecodeReader {
   CompilationUnit read(InputStream var1, String var2, Program var3) throws FileNotFoundException, IOException;
}
