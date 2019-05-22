package org.apache.velocity.runtime;

import org.apache.velocity.runtime.parser.Parser;

public interface ParserPool {
   void initialize(RuntimeServices var1);

   Parser get();

   void put(Parser var1);
}
