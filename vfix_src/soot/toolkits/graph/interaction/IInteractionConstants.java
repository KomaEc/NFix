package soot.toolkits.graph.interaction;

public interface IInteractionConstants {
   int NEW_ANALYSIS = 0;
   int WANT_ANALYSIS = 1;
   int NEW_CFG = 2;
   int CONTINUE = 3;
   int NEW_BEFORE_ANALYSIS_INFO = 4;
   int NEW_AFTER_ANALYSIS_INFO = 5;
   int DONE = 6;
   int FORWARDS = 7;
   int BACKWARDS = 8;
   int CLEARTO = 9;
   int REPLACE = 10;
   int NEW_BEFORE_ANALYSIS_INFO_AUTO = 11;
   int NEW_AFTER_ANALYSIS_INFO_AUTO = 12;
   int STOP_AT_NODE = 13;
   int CALL_GRAPH_START = 50;
   int CALL_GRAPH_NEXT_METHOD = 51;
   int CALL_GRAPH_PART = 52;
   int CALL_GRAPH_DONE = 53;
}
