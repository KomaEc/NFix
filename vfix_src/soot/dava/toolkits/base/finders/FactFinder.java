package soot.dava.toolkits.base.finders;

import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.asg.AugmentedStmtGraph;

public interface FactFinder {
   void find(DavaBody var1, AugmentedStmtGraph var2, SETNode var3) throws RetriggerAnalysisException;
}
