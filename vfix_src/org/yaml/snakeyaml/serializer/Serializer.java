package org.yaml.snakeyaml.serializer;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.emitter.Emitable;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.nodes.AnchorNode;
import org.yaml.snakeyaml.nodes.CollectionNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.resolver.Resolver;

public final class Serializer {
   private final Emitable emitter;
   private final Resolver resolver;
   private boolean explicitStart;
   private boolean explicitEnd;
   private DumperOptions.Version useVersion;
   private Map<String, String> useTags;
   private Set<Node> serializedNodes;
   private Map<Node, String> anchors;
   private int lastAnchorId;
   private Boolean closed;
   private Tag explicitRoot;

   public Serializer(Emitable emitter, Resolver resolver, DumperOptions opts, Tag rootTag) {
      this.emitter = emitter;
      this.resolver = resolver;
      this.explicitStart = opts.isExplicitStart();
      this.explicitEnd = opts.isExplicitEnd();
      if (opts.getVersion() != null) {
         this.useVersion = opts.getVersion();
      }

      this.useTags = opts.getTags();
      this.serializedNodes = new HashSet();
      this.anchors = new HashMap();
      this.lastAnchorId = 0;
      this.closed = null;
      this.explicitRoot = rootTag;
   }

   public void open() throws IOException {
      if (this.closed == null) {
         this.emitter.emit(new StreamStartEvent((Mark)null, (Mark)null));
         this.closed = Boolean.FALSE;
      } else if (Boolean.TRUE.equals(this.closed)) {
         throw new SerializerException("serializer is closed");
      } else {
         throw new SerializerException("serializer is already opened");
      }
   }

   public void close() throws IOException {
      if (this.closed == null) {
         throw new SerializerException("serializer is not opened");
      } else {
         if (!Boolean.TRUE.equals(this.closed)) {
            this.emitter.emit(new StreamEndEvent((Mark)null, (Mark)null));
            this.closed = Boolean.TRUE;
         }

      }
   }

   public void serialize(Node node) throws IOException {
      if (this.closed == null) {
         throw new SerializerException("serializer is not opened");
      } else if (this.closed) {
         throw new SerializerException("serializer is closed");
      } else {
         this.emitter.emit(new DocumentStartEvent((Mark)null, (Mark)null, this.explicitStart, this.useVersion, this.useTags));
         this.anchorNode(node);
         if (this.explicitRoot != null) {
            node.setTag(this.explicitRoot);
         }

         this.serializeNode(node, (Node)null);
         this.emitter.emit(new DocumentEndEvent((Mark)null, (Mark)null, this.explicitEnd));
         this.serializedNodes.clear();
         this.anchors.clear();
         this.lastAnchorId = 0;
      }
   }

   private void anchorNode(Node node) {
      if (node.getNodeId() == NodeId.anchor) {
         node = ((AnchorNode)node).getRealNode();
      }

      if (this.anchors.containsKey(node)) {
         String anchor = (String)this.anchors.get(node);
         if (null == anchor) {
            anchor = this.generateAnchor();
            this.anchors.put(node, anchor);
         }
      } else {
         this.anchors.put(node, (Object)null);
         switch(node.getNodeId()) {
         case sequence:
            SequenceNode seqNode = (SequenceNode)node;
            List<Node> list = seqNode.getValue();
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
               Node item = (Node)i$.next();
               this.anchorNode(item);
            }

            return;
         case mapping:
            MappingNode mnode = (MappingNode)node;
            List<NodeTuple> map = mnode.getValue();
            Iterator i$ = map.iterator();

            while(i$.hasNext()) {
               NodeTuple object = (NodeTuple)i$.next();
               Node key = object.getKeyNode();
               Node value = object.getValueNode();
               this.anchorNode(key);
               this.anchorNode(value);
            }
         }
      }

   }

   private String generateAnchor() {
      ++this.lastAnchorId;
      NumberFormat format = NumberFormat.getNumberInstance();
      format.setMinimumIntegerDigits(3);
      format.setMaximumFractionDigits(0);
      format.setGroupingUsed(false);
      String anchorId = format.format((long)this.lastAnchorId);
      return "id" + anchorId;
   }

   private void serializeNode(Node node, Node parent) throws IOException {
      if (node.getNodeId() == NodeId.anchor) {
         node = ((AnchorNode)node).getRealNode();
      }

      String tAlias = (String)this.anchors.get(node);
      if (this.serializedNodes.contains(node)) {
         this.emitter.emit(new AliasEvent(tAlias, (Mark)null, (Mark)null));
      } else {
         this.serializedNodes.add(node);
         switch(node.getNodeId()) {
         case sequence:
            SequenceNode seqNode = (SequenceNode)node;
            boolean implicitS = node.getTag().equals(this.resolver.resolve(NodeId.sequence, (String)null, true));
            this.emitter.emit(new SequenceStartEvent(tAlias, node.getTag().getValue(), implicitS, (Mark)null, (Mark)null, seqNode.getFlowStyle()));
            List<Node> list = seqNode.getValue();
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
               Node item = (Node)i$.next();
               this.serializeNode(item, node);
            }

            this.emitter.emit(new SequenceEndEvent((Mark)null, (Mark)null));
            break;
         case scalar:
            ScalarNode scalarNode = (ScalarNode)node;
            Tag detectedTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
            Tag defaultTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
            ImplicitTuple tuple = new ImplicitTuple(node.getTag().equals(detectedTag), node.getTag().equals(defaultTag));
            ScalarEvent event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple, scalarNode.getValue(), (Mark)null, (Mark)null, scalarNode.getStyle());
            this.emitter.emit(event);
            break;
         default:
            Tag implicitTag = this.resolver.resolve(NodeId.mapping, (String)null, true);
            boolean implicitM = node.getTag().equals(implicitTag);
            this.emitter.emit(new MappingStartEvent(tAlias, node.getTag().getValue(), implicitM, (Mark)null, (Mark)null, ((CollectionNode)node).getFlowStyle()));
            MappingNode mnode = (MappingNode)node;
            List<NodeTuple> map = mnode.getValue();
            Iterator i$ = map.iterator();

            while(i$.hasNext()) {
               NodeTuple row = (NodeTuple)i$.next();
               Node key = row.getKeyNode();
               Node value = row.getValueNode();
               this.serializeNode(key, mnode);
               this.serializeNode(value, mnode);
            }

            this.emitter.emit(new MappingEndEvent((Mark)null, (Mark)null));
         }
      }

   }
}
