package org.jboss.util.state.xml;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.jboss.logging.Logger;
import org.jboss.util.state.State;
import org.jboss.util.state.StateMachine;
import org.jboss.util.state.Transition;
import org.jboss.util.xml.DOMUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StateMachineParser {
   private static Logger log = Logger.getLogger(StateMachineParser.class);

   public StateMachine parse(URL source) throws Exception {
      InputStream in = source.openConnection().getInputStream();
      Element root = DOMUtils.parse(in);
      String description = root.getAttribute("description");
      HashMap nameToStateMap = new HashMap();
      HashMap nameToTransitionsMap = new HashMap();
      HashSet states = new HashSet();
      State startState = null;
      NodeList stateList = root.getChildNodes();

      for(int i = 0; i < stateList.getLength(); ++i) {
         Node stateNode = stateList.item(i);
         if (stateNode.getNodeName().equals("state")) {
            Element stateElement = (Element)stateNode;
            String stateName = stateElement.getAttribute("name");
            State s = new State(stateName);
            states.add(s);
            nameToStateMap.put(stateName, s);
            HashMap transitions = new HashMap();
            NodeList transitionList = stateElement.getChildNodes();

            for(int j = 0; j < transitionList.getLength(); ++j) {
               Node transitionNode = transitionList.item(j);
               if (transitionNode.getNodeName().equals("transition")) {
                  Element transitionElement = (Element)transitionNode;
                  String name = transitionElement.getAttribute("name");
                  String targetName = transitionElement.getAttribute("target");
                  transitions.put(name, targetName);
               }
            }

            nameToTransitionsMap.put(stateName, transitions);
            if (Boolean.valueOf(stateElement.getAttribute("isStartState")) == Boolean.TRUE) {
               startState = s;
            }
         }
      }

      Iterator transitions = nameToTransitionsMap.keySet().iterator();
      StringBuffer resolveFailed = new StringBuffer();

      while(transitions.hasNext()) {
         String stateName = (String)transitions.next();
         State s = (State)nameToStateMap.get(stateName);
         HashMap stateTransitions = (HashMap)nameToTransitionsMap.get(stateName);
         Iterator it = stateTransitions.keySet().iterator();

         while(it.hasNext()) {
            String name = (String)it.next();
            String targetName = (String)stateTransitions.get(name);
            State target = (State)nameToStateMap.get(targetName);
            if (target == null) {
               String msg = "Failed to resolve target state: " + targetName + " for transition: " + name;
               resolveFailed.append(msg);
               log.debug(msg);
            }

            Transition t = new Transition(name, target);
            s.addTransition(t);
         }
      }

      if (resolveFailed.length() > 0) {
         throw new Exception("Failed to resolve transition targets: " + resolveFailed);
      } else {
         StateMachine sm = new StateMachine(states, startState, description);
         return sm;
      }
   }
}
