package soot.jimple.parser.parser;

import java.util.ArrayList;

final class State {
   int state;
   ArrayList<Object> nodes;

   State(int state, ArrayList<Object> nodes) {
      this.state = state;
      this.nodes = nodes;
   }
}
