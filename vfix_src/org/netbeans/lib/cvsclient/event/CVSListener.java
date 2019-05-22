package org.netbeans.lib.cvsclient.event;

public interface CVSListener {
   void messageSent(MessageEvent var1);

   void messageSent(BinaryMessageEvent var1);

   void fileAdded(FileAddedEvent var1);

   void fileToRemove(FileToRemoveEvent var1);

   void fileRemoved(FileRemovedEvent var1);

   void fileUpdated(FileUpdatedEvent var1);

   void fileInfoGenerated(FileInfoEvent var1);

   void commandTerminated(TerminationEvent var1);

   void moduleExpanded(ModuleExpansionEvent var1);
}
