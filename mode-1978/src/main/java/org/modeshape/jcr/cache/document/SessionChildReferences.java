/*
 * ModeShape (http://www.modeshape.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * ModeShape is free software. Unless otherwise indicated, all code in ModeShape
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * ModeShape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.modeshape.jcr.cache.document;

import java.util.Iterator;
import org.modeshape.common.annotation.ThreadSafe;
import org.modeshape.jcr.cache.ChildReference;
import org.modeshape.jcr.cache.ChildReferences;
import org.modeshape.jcr.cache.NodeKey;
import org.modeshape.jcr.value.Name;

/**
 * A {@link ChildReferences} implementation that projects a single, changeable view of the child references of a node, allowing
 * child references to be added to the end of the persisted state. This view always reflects the view's changes on top of the
 * current persisted list of child references, even when the persisted state is changed by other sessions.
 * <p>
 * For example, consider a node A that has two children, B and C. If a {@link SessionChildReferences} instance is created for A,
 * then it includes {@link ChildReference} for nodes B and C. If a new child node, D, is created and appended to the list, the
 * MutableChildReferences object will show three children: B, C and D. Before these changes are saved, however, another client
 * adds to node A a new node, E, and persists those changes. At that instant, the MutableChildReferences will see four children
 * for node A: the persisted children B, C and E, plus the transient node D that has not yet been saved.
 * </p>
 */
@ThreadSafe
public class SessionChildReferences extends AbstractChildReferences {

    private final ChildReferences persisted;
    private final MutableChildReferences appended;
    private final SessionNode.ChangedChildren changedChildren;

    public SessionChildReferences( ChildReferences persisted,
                                   MutableChildReferences appended,
                                   SessionNode.ChangedChildren changedChildren ) {
        this.persisted = persisted != null ? persisted : ImmutableChildReferences.EMPTY_CHILD_REFERENCES;
        this.appended = appended;
        this.changedChildren = changedChildren;
    }

    @Override
    public long size() {
        return persisted.size() + (appended != null ? appended.size() : 0);
    }

    @Override
    public int getChildCount( Name name ) {
        int appendedChildren = appended != null ? appended.getChildCount(name) : 0;
        int persistedChildrenCount = persisted.getChildCount(name);

        int removedChildren = 0;
        int renamedChildren = 0;
        int appendedThenReorderedChildren = 0;

        if (changedChildren != null) {
            appendedThenReorderedChildren = changedChildren.insertionCount(name);

            for (Iterator<ChildReference> childReferenceIterator = persisted.iterator(name); childReferenceIterator.hasNext(); ) {
                ChildReference persistedChild = childReferenceIterator.next();

                if (changedChildren.isRemoved(persistedChild)) {
                    --removedChildren;
                    continue;
                }

                if (changedChildren.isRenamed(persistedChild)) {
                    --renamedChildren;
                }
            }

            if (changedChildren.isRenamed(name)) {
                ++renamedChildren;
            }
        }
        return persistedChildrenCount + appendedChildren + appendedThenReorderedChildren + removedChildren + renamedChildren;
    }

    @Override
    public ChildReference getChild( Name name,
                                    int snsIndex,
                                    Context context ) {
        if (changedChildren != null) context = new WithChanges(context, changedChildren);
        // First look in the delegate references ...
        ChildReference ref = persisted.getChild(name, snsIndex, context);
        if (ref == null) {
            if (appended != null) {
                // Look in the added ...
                ref = appended.getChild(name, snsIndex, context);
                if (ref == null && changedChildren != null && changedChildren.insertionCount() > 0) {
                    //look in added and then reordered (which don't appear as appended)
                    Iterator<ChildInsertions> insertionsWithName = changedChildren.insertions(name);
                    while (insertionsWithName.hasNext()) {
                        for (ChildReference inserted : insertionsWithName.next().inserted()) {
                            if (inserted.getSnsIndex() == snsIndex) {
                                return inserted;
                            }
                        }
                    }
                }
            }
        }
        return ref;
    }

    @Override
    public boolean hasChild( NodeKey key ) {
        return getChild(key, new BasicContext()) != null;
    }

    @Override
    public ChildReference getChild( NodeKey key ) {
        return getChild(key, new BasicContext());
    }

    @Override
    public ChildReference getChild( NodeKey key,
                                    Context context ) {
        if (changedChildren != null) context = new WithChanges(context, changedChildren);
        // First look in the delegate references ...
        // Note that we don't know the name of the child yet, so we'll have to come back
        // to the persisted node after we know the name ...
        ChildReference ref = persisted.getChild(key, context);
        if (ref == null) {
            if (appended != null) {
                // Look in the added ...
                ref = appended.getChild(key, context);
                if (ref != null) {
                    // The node was appended, but we need to increment the SNS indexes
                    // by the number of same-named children in 'persisted' ...
                    int numSnsInPersisted = persisted.getChildCount(ref.getName());
                    if (numSnsInPersisted != 0) {
                        // There were some persisted with the same name, and we didn't find these
                        // when looking in the persisted node above. So adjust the SNS index ...

                        //we need to take into account that the same node might be removed (in case of an reorder to the end)
                        int numSnsInRemoved = (changedChildren != null && changedChildren.getRemovals().contains(key)) ? 1 : 0;
                        ref = ref.with(numSnsInPersisted + ref.getSnsIndex() - numSnsInRemoved);
                    }
                } else if (changedChildren != null && changedChildren.insertionCount() > 0) {
                    //it's not in the appended list, so check if there are any transient reordered nodes - appended and
                    //reordered at the same time - they would not appear in the "appended" list
                    ref = changedChildren.inserted(key);
                }
            }
        }
        return ref;
    }

    @Override
    public Iterator<ChildReference> iterator() {
        return iterator(new BasicContext());
    }

    @Override
    public Iterator<ChildReference> iterator( Name name,
                                              Context context ) {
        if (changedChildren != null) context = new WithChanges(context, changedChildren);
        return createIterator(name, context);
    }

    protected Iterator<ChildReference> createIterator( final Name name,
                                                       final Context context ) {
        Iterator<ChildReference> firstIter = persisted.iterator(name, context);
        final MutableChildReferences appended = this.appended;
        Iterable<ChildReference> second = new Iterable<ChildReference>() {
            @Override
            public Iterator<ChildReference> iterator() {
                return appended.iterator(context, name);
            }
        };
        return appended == null ? firstIter : new UnionIterator<ChildReference>(firstIter, second);
    }

    @Override
    public Iterator<ChildReference> iterator( Context context ) {
        if (changedChildren != null) context = new WithChanges(context, changedChildren);
        return createIterator(context);
    }

    protected Iterator<ChildReference> createIterator( final Context context ) {
        Iterator<ChildReference> firstIter = persisted.iterator(context);
        final MutableChildReferences appended = this.appended;
        Iterable<ChildReference> second = new Iterable<ChildReference>() {
            @Override
            public Iterator<ChildReference> iterator() {
                return appended.iterator(context);
            }
        };
        return appended == null ? firstIter : new UnionIterator<ChildReference>(firstIter, second);
    }

    @Override
    public Iterator<NodeKey> getAllKeys() {
        Iterator<NodeKey> firstIter = persisted.getAllKeys();
        final MutableChildReferences appended = this.appended;
        Iterable<NodeKey> second = new Iterable<NodeKey>() {
            @Override
            public Iterator<NodeKey> iterator() {
                return appended.getAllKeys();
            }
        };
        return appended == null ? firstIter : new UnionIterator<NodeKey>(firstIter, second);
    }

    @Override
    public StringBuilder toString( StringBuilder sb ) {
        Iterator<ChildReference> iter = iterator();
        if (iter.hasNext()) {
            sb.append(iter.next());
            while (iter.hasNext()) {
                sb.append(", ");
                sb.append(iter.next());
            }
        }
        return sb;
    }
}
