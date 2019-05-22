package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.TokenRange;
import java.util.Optional;

public interface NodeWithTokenRange<N> {
   Optional<TokenRange> getTokenRange();

   N setTokenRange(TokenRange range);
}
