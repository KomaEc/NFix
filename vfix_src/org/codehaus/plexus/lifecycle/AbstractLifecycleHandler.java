package org.codehaus.plexus.lifecycle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.plexus.component.manager.ComponentManager;
import org.codehaus.plexus.lifecycle.phase.Phase;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.PhaseExecutionException;

public abstract class AbstractLifecycleHandler implements LifecycleHandler {
   private String id = null;
   private String name = null;
   private List beginSegment = new ArrayList();
   private List suspendSegment = new ArrayList();
   private List resumeSegment = new ArrayList();
   private List endSegment = new ArrayList();

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public List getBeginSegment() {
      return this.beginSegment;
   }

   public List getSuspendSegment() {
      return this.suspendSegment;
   }

   public List getResumeSegment() {
      return this.resumeSegment;
   }

   public List getEndSegment() {
      return this.endSegment;
   }

   public void start(Object component, ComponentManager manager) throws PhaseExecutionException {
      if (!this.segmentIsEmpty(this.getBeginSegment())) {
         Iterator i = this.getBeginSegment().iterator();

         while(i.hasNext()) {
            Phase phase = (Phase)i.next();
            phase.execute(component, manager);
         }

      }
   }

   public void suspend(Object component, ComponentManager manager) throws PhaseExecutionException {
      if (!this.segmentIsEmpty(this.getSuspendSegment())) {
         Iterator i = this.getSuspendSegment().iterator();

         while(i.hasNext()) {
            Phase phase = (Phase)i.next();
            phase.execute(component, manager);
         }

      }
   }

   public void resume(Object component, ComponentManager manager) throws PhaseExecutionException {
      if (!this.segmentIsEmpty(this.getResumeSegment())) {
         Iterator i = this.getResumeSegment().iterator();

         while(i.hasNext()) {
            Phase phase = (Phase)i.next();
            phase.execute(component, manager);
         }

      }
   }

   public void end(Object component, ComponentManager manager) throws PhaseExecutionException {
      if (!this.segmentIsEmpty(this.getEndSegment())) {
         Iterator i = this.getEndSegment().iterator();

         while(i.hasNext()) {
            Phase phase = (Phase)i.next();
            phase.execute(component, manager);
         }

      }
   }

   private boolean segmentIsEmpty(List segment) {
      return segment == null || segment.size() == 0;
   }

   // $FF: synthetic method
   public abstract void initialize();
}
