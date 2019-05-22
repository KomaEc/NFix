package org.codehaus.groovy.control;

import java.io.Reader;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.syntax.ParserException;
import org.codehaus.groovy.syntax.Reduction;

public interface ParserPlugin {
   Reduction parseCST(SourceUnit var1, Reader var2) throws CompilationFailedException;

   ModuleNode buildAST(SourceUnit var1, ClassLoader var2, Reduction var3) throws ParserException;
}
