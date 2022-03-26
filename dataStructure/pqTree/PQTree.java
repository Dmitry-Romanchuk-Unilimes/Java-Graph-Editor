// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure.pqTree;

import dataStructure.Queue;

import java.util.Iterator;
import java.util.Vector;

public class PQTree
{
    public static final boolean SHOW_DEBUG_OUTPUT = false;
    private PQNode root;
    private Vector leaves;
    private Queue queue;
    private Queue clearQueue;
    private int drawWidth;
    private int drawHeight;
    private boolean hasChanged;
    private String templateMatchString;
    private String templateTimeString;
    private String reduceString;
    private boolean doneReduction;
    private boolean cleared;
    private Vector constraints;
    private boolean flaggedAsNull;
    private PQNode lastPertRoot;
    
    public PQTree() throws Exception {
        this.leaves = null;
        this.queue = null;
        this.clearQueue = null;
        this.constraints = null;
        this.lastPertRoot = null;
        this.init(true);
    }
    
    public PQTree(final Vector data) throws Exception {
        this.leaves = null;
        this.queue = null;
        this.clearQueue = null;
        this.constraints = null;
        this.lastPertRoot = null;
        this.init(true);
        final boolean save_memory = false;
        final boolean run_fast = true;
        PQNode aNode = null;
        try {
            this.leaves = new Vector(data.size());
        }
        catch (OutOfMemoryError e) {
            System.out.println("Insufficient memory to store " + data.size() + " leaf nodes");
            throw e;
        }
        if (save_memory) {
            if (data.size() == 1) {
                this.root.setData(data.firstElement());
                this.leaves.addElement(this.root);
                return;
            }
            final Iterator iter = data.iterator();
            int count = 0;
            try {
                while (iter.hasNext()) {
                    aNode = new PQNode(iter.next());
                    this.root.addChild(aNode);
                    this.leaves.addElement(aNode);
                    iter.remove();
                    ++count;
                }
                return;
            }
            catch (OutOfMemoryError e2) {
                System.out.println("Insufficient memory while creating leaf node #" + count);
                throw e2;
            }
        }
        if (run_fast) {
            int i = 0;
            try {
                if (data.size() == 1) {
                    this.root.setData(data.firstElement());
                    this.leaves.addElement(this.root);
                }
                else {
                    for (i = 0; i < data.size(); ++i) {
                        aNode = new PQNode(data.elementAt(i));
                        this.root.addChild(aNode);
                        this.leaves.addElement(aNode);
                    }
                }
            }
            catch (OutOfMemoryError e3) {
                System.out.println("Insufficient memory while creating leaf node #" + i);
                throw e3;
            }
        }
    }
    
    public PQTree(final int universeSize) throws Exception {
        this.leaves = null;
        this.queue = null;
        this.clearQueue = null;
        this.constraints = null;
        this.lastPertRoot = null;
        this.init(true);
        PQNode aNode = null;
        try {
            this.leaves = new Vector(universeSize);
        }
        catch (OutOfMemoryError e) {
            System.out.println("Insufficient memory to store " + universeSize + " leaf nodes");
            throw e;
        }
        int i = 0;
        try {
            if (universeSize == 1) {
                this.root.setData(new Integer(0));
                this.leaves.addElement(this.root);
            }
            else {
                for (i = 0; i < universeSize; ++i) {
                    aNode = new PQNode(new Integer(i));
                    this.root.addChild(aNode);
                    this.leaves.addElement(aNode);
                }
            }
        }
        catch (OutOfMemoryError e2) {
            System.out.println("Insufficient memory while creating leaf node #" + i);
            throw e2;
        }
    }
    
    private void init(final boolean initLeaves) throws Exception {
        this.hasChanged = true;
        this.templateMatchString = new String();
        this.templateTimeString = new String();
        this.reduceString = new String();
        this.doneReduction = true;
        this.flaggedAsNull = false;
        if (this.constraints != null) {
            this.clear();
            this.constraints = null;
        }
        this.cleared = true;
        this.queue = null;
        this.clearQueue = new Queue();
        this.root = new PQNode();
        if (initLeaves) {
            this.leaves = new Vector();
        }
    }
    
    public PQNode getRoot() {
        return this.root;
    }
    
    public void setRoot(final PQNode rootNode) {
        this.root = rootNode;
    }
    
    public Vector getLeaves() {
        return this.leaves;
    }
    
    public boolean isNullTree() {
        return !this.root.hasChildren();
    }
    
    public int getWidth() {
        return this.drawWidth;
    }
    
    public int getHeight() {
        return this.drawHeight;
    }
    
    public String getTemplateMatchString() {
        return this.templateMatchString;
    }
    
    private void setTemplateTimeString(final String newString) {
        this.templateTimeString = newString;
    }
    
    public String getReduceString() {
        return this.reduceString;
    }
    
    public void setConstraints(final Vector c) {
        this.constraints = c;
    }
    
    public Vector getConstraints() {
        return this.constraints;
    }
    
    public boolean isDoneReduction() {
        return this.doneReduction && this.cleared;
    }
    
    public boolean isReduced() {
        return this.doneReduction;
    }
    
    public PQNode getLeafAt(final int index) {
        return this.leaves.elementAt(index);
    }
    
    public void resetTree() throws Exception {
        this.init(false);
        for (int i = 0; i < this.leaves.size(); ++i) {
            final PQNode aNode = this.leaves.elementAt(i);
            aNode.clear(false);
            this.root.addChild(aNode);
        }
        System.gc();
    }
    
    public void reductionByValue(final Vector data) throws Exception {
        final Vector s = new Vector();
        for (int i = 0; i < this.leaves.size(); ++i) {
            if (data.contains(this.leaves.elementAt(i).getData())) {
                s.addElement(this.leaves.elementAt(i));
            }
        }
        this.reduction(s, 0);
    }
    
    public void reduction(final Vector s, final int numSteps) throws Exception {
        this.constraints = s;
        this.reduction(numSteps);
    }
    
    public PQNode reduction(final Vector s) throws Exception {
        this.constraints = s;
        return this.reduction(0);
    }
    
    public PQNode reduction(final int numSteps) throws Exception {
        PQNode pertRoot = null;
        if (!this.cleared) {
            this.clear();
        }
        final long timeTaken = System.currentTimeMillis();
        final boolean previouslyDoneReduction = this.doneReduction;
        if (this.queue == null) {
            this.queue = new Queue();
            final Iterator iterator = this.constraints.iterator();
            this.reduceString = "Reduced: {";
            while (iterator.hasNext()) {
                final Object obj = iterator.next();
                this.queue.enqueue(obj);
                this.reduceString = String.valueOf(this.reduceString) + obj.toString();
                if (iterator.hasNext()) {
                    this.reduceString = String.valueOf(this.reduceString) + ",";
                }
                else {
                    this.reduceString = String.valueOf(this.reduceString) + "}\n";
                }
            }
            this.bubble(this.queue);
            this.queue = null;
        }
        if (!this.flaggedAsNull) {
            if (this.doneReduction && this.cleared) {
                this.queue = new Queue();
                final Iterator iterator = this.constraints.iterator();
                while (iterator.hasNext()) {
                    this.queue.enqueue(iterator.next());
                }
                pertRoot = this.reduce(this.queue, this.constraints.size(), numSteps);
                if (this.queue == null || this.queue.size() == 0) {
                    this.doneReduction = true;
                    this.queue = null;
                }
                this.cleared = false;
            }
            else if (this.doneReduction && !this.cleared) {
                this.templateMatchString = "";
            }
            else if (!this.doneReduction) {
                pertRoot = this.reduce(this.queue, this.constraints.size(), numSteps);
                if (this.queue == null || this.queue.size() == 0) {
                    this.doneReduction = true;
                    this.queue = null;
                }
            }
        }
        if (((previouslyDoneReduction && this.doneReduction && !this.cleared) || numSteps == 0 || this.flaggedAsNull) && this.flaggedAsNull) {
            final String rString = "Could Not Reduce: " + this.reduceString.substring(this.reduceString.indexOf(123), this.reduceString.length());
            final String tString = new String(this.templateMatchString);
            this.init(false);
            this.reduceString = rString;
            this.templateMatchString = tString;
        }
        this.hasChanged = true;
        this.setTemplateTimeString("Time taken in milliseconds: " + (System.currentTimeMillis() - timeTaken) + "\n");
        return pertRoot;
    }
    
    public void bubble(final Queue queue) throws Exception {
        final Vector blockedNodeVector = new Vector();
        int blockCount = 0;
        int blockedNodes = 0;
        int offTheTop = 0;
        while (queue.size() + blockCount + offTheTop > 1 && queue.size() != 0) {
            final PQNode currentNode = (PQNode)queue.dequeue();
            if (!currentNode.isQueued()) {
                currentNode.setPertinentLeafCount(1);
            }
            currentNode.setBlocked();
            final Vector blockedSiblings = currentNode.getBlockedSiblings();
            final Vector unblockedSiblings = currentNode.getUnblockedSiblings();
            if (unblockedSiblings.size() > 0) {
                currentNode.setParent(unblockedSiblings.elementAt(0).getParent());
                currentNode.setUnblocked();
            }
            else if (currentNode.getSiblings() == null || currentNode.getSiblings().size() < 2) {
                currentNode.setUnblocked();
            }
            if (!currentNode.isBlocked()) {
                final PQNode parentNode = currentNode.getParent();
                Vector list = new Vector(0);
                if (blockedSiblings.size() > 0) {
                    list = currentNode.getMaximalConsecutiveBlockedSiblings();
                    for (final PQNode blockedSibling : list) {
                        blockedSibling.setUnblocked();
                        blockedSibling.setParent(parentNode);
                        parentNode.setPertinentChildCount(parentNode.getPertinentChildCount() + 1);
                    }
                }
                if (parentNode == null) {
                    offTheTop = 1;
                }
                else {
                    parentNode.setPertinentChildCount(parentNode.getPertinentChildCount() + 1);
                    if (!parentNode.isBlocked() && !parentNode.isQueued()) {
                        queue.enqueue(parentNode);
                        parentNode.setQueued();
                    }
                }
                blockCount -= blockedSiblings.size();
                blockedNodes -= list.size();
            }
            else {
                blockedNodeVector.addElement(currentNode);
                blockCount -= blockedSiblings.size() - 1;
                ++blockedNodes;
            }
        }
        if (blockCount == 1 && blockedNodes > 1) {
            final PQNode pseudoNode = new PQNode();
            pseudoNode.convertToQNode();
            pseudoNode.setPertinentChildCount(blockedNodes);
            pseudoNode.pseudoNode();
            PQNode aBlockedNode = null;
            PQNode startNode = null;
            PQNode lastNode = null;
            PQNode prevNode = null;
            for (int i = 0; i < blockedNodeVector.size(); ++i) {
                aBlockedNode = blockedNodeVector.elementAt(i);
                aBlockedNode.setParent(pseudoNode);
                if (!aBlockedNode.getNonDirectedSibling(aBlockedNode.siblings.PQNodeAt(0)).isBlocked()) {
                    if (startNode == null) {
                        startNode = aBlockedNode;
                        prevNode = aBlockedNode.siblings.PQNodeAt(1);
                    }
                    else {
                        lastNode = aBlockedNode;
                    }
                }
                else if (!aBlockedNode.getNonDirectedSibling(aBlockedNode.siblings.PQNodeAt(1)).isBlocked()) {
                    if (startNode == null) {
                        startNode = aBlockedNode;
                        prevNode = aBlockedNode.siblings.PQNodeAt(0);
                    }
                    else {
                        lastNode = aBlockedNode;
                    }
                }
            }
            if (startNode == null || prevNode == null || lastNode == null) {
                throw new Exception("*** ERROR no starting blocked node could be found to add to a new pseudonode!");
            }
            final Vector pseudoVector = new Vector();
            aBlockedNode = startNode;
            aBlockedNode.setParent(pseudoNode);
            pseudoVector.addElement(aBlockedNode);
            while (aBlockedNode != lastNode) {
                final PQNode tempNode = aBlockedNode;
                aBlockedNode = aBlockedNode.siblings.otherPQNode(prevNode);
                prevNode = tempNode;
                aBlockedNode.setParent(pseudoNode);
                pseudoVector.addElement(aBlockedNode);
            }
            for (int p = 0; p < pseudoVector.size(); ++p) {
                pseudoNode.addChild(pseudoVector.elementAt(p), false);
            }
            pseudoNode.getEndMostChildren().addPQNode(pseudoVector.firstElement());
            pseudoNode.getEndMostChildren().addPQNode(pseudoVector.lastElement());
        }
        else if (blockCount > 1) {
            this.flaggedAsNull = true;
            this.templateMatchString = "Templates Matched: {NONE}\n";
            this.reduceString = "Could Not educed: {";
        }
        this.hasChanged = true;
    }
    
    public PQNode reduce(final Queue queue, final int pertinentCount, final int numSteps) throws Exception {
        int count = 0;
        if (this.doneReduction) {
            this.templateMatchString = new String("Templates Matched: {");
            this.doneReduction = false;
        }
        else {
            this.templateMatchString = "";
        }
        while (queue.size() > 0) {
            if (numSteps != 0 && numSteps == count++) {
                break;
            }
            final PQNode currentNode = (PQNode)queue.dequeue();
            this.clearQueue.enqueue(currentNode);
            if (currentNode.getPertinentLeafCount() < pertinentCount && pertinentCount != 1) {
                final PQNode parentNode = currentNode.getParent();
                parentNode.setPertinentLeafCount(parentNode.getPertinentLeafCount() + currentNode.getPertinentLeafCount());
                parentNode.setPertinentChildCount(parentNode.getPertinentChildCount() - 1);
                if (parentNode.getPertinentChildCount() == 0) {
                    queue.enqueue(parentNode);
                }
                if (!this.templateL1(currentNode)) {
                    if (!this.templateP1(currentNode)) {
                        if (!this.templateP3(currentNode)) {
                            if (!this.templateP5(currentNode)) {
                                if (!this.templateQ1(currentNode)) {
                                    if (!this.templateQ2(currentNode)) {
                                        final String tempString = this.templateMatchString;
                                        this.flaggedAsNull = true;
                                        this.templateMatchString = String.valueOf(tempString) + "NONE";
                                        break;
                                    }
                                    this.templateMatchString = String.valueOf(this.templateMatchString) + "Q2, ";
                                }
                                else {
                                    this.templateMatchString = String.valueOf(this.templateMatchString) + "Q1, ";
                                }
                            }
                            else {
                                this.templateMatchString = String.valueOf(this.templateMatchString) + "P5, ";
                            }
                        }
                        else {
                            this.templateMatchString = String.valueOf(this.templateMatchString) + "P3, ";
                        }
                    }
                    else {
                        this.templateMatchString = String.valueOf(this.templateMatchString) + "P1, ";
                    }
                }
                else {
                    this.templateMatchString = String.valueOf(this.templateMatchString) + "L1, ";
                }
            }
            else {
                this.lastPertRoot = currentNode;
                if (!this.templateL1(currentNode)) {
                    if (!this.templateP1(currentNode)) {
                        if (!this.templateP2(currentNode)) {
                            if (!this.templateP4(currentNode)) {
                                if (!this.templateP6(currentNode)) {
                                    if (!this.templateQ1(currentNode)) {
                                        if (!this.templateQ2(currentNode)) {
                                            if (!this.templateQ3(currentNode)) {
                                                final String tempString2 = this.templateMatchString;
                                                this.flaggedAsNull = true;
                                                this.templateMatchString = String.valueOf(tempString2) + "NONE";
                                                break;
                                            }
                                            this.templateMatchString = String.valueOf(this.templateMatchString) + "Q3";
                                        }
                                        else {
                                            this.templateMatchString = String.valueOf(this.templateMatchString) + "Q2";
                                        }
                                    }
                                    else {
                                        this.templateMatchString = String.valueOf(this.templateMatchString) + "Q1";
                                    }
                                }
                                else {
                                    this.templateMatchString = String.valueOf(this.templateMatchString) + "P6";
                                }
                            }
                            else {
                                this.templateMatchString = String.valueOf(this.templateMatchString) + "P4";
                            }
                        }
                        else {
                            this.templateMatchString = String.valueOf(this.templateMatchString) + "P2";
                        }
                    }
                    else {
                        this.templateMatchString = String.valueOf(this.templateMatchString) + "P1";
                    }
                }
                else {
                    this.templateMatchString = String.valueOf(this.templateMatchString) + "L1";
                }
            }
            this.hasChanged = true;
        }
        if (queue.size() == 0 || this.flaggedAsNull) {
            this.templateMatchString = String.valueOf(this.templateMatchString) + "}\n";
        }
        return this.lastPertRoot;
    }
    
    public void clear() throws Exception {
        this.cleared = true;
        if (this.clearQueue != null) {
            while (this.clearQueue.size() > 0) {
                final PQNode currentNode = (PQNode)this.clearQueue.dequeue();
                currentNode.clear();
            }
            if (this.constraints != null) {
                for (int j = 0; j < this.constraints.size(); ++j) {
                    this.constraints.elementAt(j).clear();
                }
            }
        }
    }
    
    public void clear(final Queue queueToClear) throws Exception {
        this.cleared = true;
        if (queueToClear != null) {
            while (queueToClear.size() > 0) {
                final PQNode currentNode = (PQNode)this.clearQueue.dequeue();
                currentNode.clear(false);
            }
        }
    }
    
    public boolean templateL1(final PQNode currentNode) throws Exception {
        if (!currentNode.hasChildren()) {
            currentNode.labelAsFull();
            return true;
        }
        return false;
    }
    
    public boolean templateP1(final PQNode currentNode) throws Exception {
        if (currentNode.isPNode() && currentNode.getNumChildren() == currentNode.getNumFullChildren()) {
            currentNode.labelAsFull();
            return true;
        }
        return false;
    }
    
    public boolean templateP2(final PQNode currentNode) throws Exception {
        if (currentNode.isPNode() && currentNode.getNumPartialChildren() == 0 && currentNode.getNumFullChildren() > 0) {
            if (currentNode.getNumFullChildren() > 1 && currentNode.getNumEmptyChildren() > 0) {
                final PQNode newNode = new PQNode();
                newNode.labelAsFull();
                currentNode.moveFullChildrenTo(newNode);
                currentNode.addChild(newNode);
                this.lastPertRoot = newNode;
            }
            else {
                this.lastPertRoot = currentNode.getOnlyFullChild();
            }
            return true;
        }
        return false;
    }
    
    public boolean templateP3(final PQNode currentNode) throws Exception {
        if (currentNode.isPNode() && currentNode.getNumPartialChildren() == 0 && currentNode.getNumFullChildren() > 0) {
            final PQNode newNode = new PQNode();
            newNode.convertToQNode();
            newNode.labelAsPartial();
            if (currentNode.getNumFullChildren() > 1) {
                final PQNode groupNode = new PQNode();
                groupNode.labelAsFull();
                currentNode.moveFullChildrenTo(groupNode);
                newNode.addChild(groupNode);
            }
            else {
                newNode.addChild(currentNode.removeOnlyFullChild());
            }
            currentNode.getParent().replaceChild(currentNode, newNode);
            if (currentNode.getNumEmptyChildren() > 1) {
                currentNode.clear(false);
                newNode.addChild(currentNode);
            }
            else {
                newNode.addChild(currentNode.removeOnlyEmptyChild());
            }
            return true;
        }
        return false;
    }
    
    public boolean templateP4(final PQNode currentNode) throws Exception {
        if (currentNode.isPNode() && currentNode.getNumPartialChildren() == 1) {
            final PQNode partialChild = currentNode.getPartialChild(0);
            if (currentNode.getNumFullChildren() > 0) {
                PQNode newNode;
                if (currentNode.getNumFullChildren() > 1) {
                    newNode = new PQNode();
                    newNode.labelAsFull();
                    currentNode.moveFullChildrenTo(newNode);
                }
                else {
                    newNode = currentNode.removeOnlyFullChild();
                }
                partialChild.addChild(newNode);
            }
            if (currentNode.hasOnlyOneChild()) {
                if (currentNode.getParent() != null) {
                    if (currentNode.getParent().isDeleted()) {
                        currentNode.becomeChild(partialChild);
                    }
                    else {
                        currentNode.getParent().replaceChild(currentNode, partialChild);
                        currentNode.delete();
                        this.lastPertRoot = partialChild;
                    }
                }
                else {
                    partialChild.becomeRoot();
                    this.setRoot(partialChild);
                    this.lastPertRoot = partialChild;
                }
            }
            else {
                this.lastPertRoot = partialChild;
            }
            return true;
        }
        return false;
    }
    
    public boolean templateP5(final PQNode currentNode) throws Exception {
        if (currentNode.isPNode() && currentNode.getNumPartialChildren() == 1) {
            final PQNode partialChild = currentNode.getPartialChild(0);
            currentNode.removeChild(partialChild);
            if (currentNode.getNumFullChildren() > 0) {
                PQNode newNode;
                if (currentNode.getNumFullChildren() > 1) {
                    newNode = new PQNode();
                    newNode.labelAsFull();
                    currentNode.moveFullChildrenTo(newNode);
                }
                else {
                    newNode = currentNode.removeOnlyFullChild();
                }
                partialChild.addChild(newNode);
            }
            if (currentNode.getNumEmptyChildren() > 0) {
                PQNode newNode;
                if (currentNode.getNumEmptyChildren() == 1) {
                    newNode = currentNode.removeOnlyEmptyChild();
                    currentNode.getParent().replaceChild(currentNode, partialChild);
                    currentNode.delete();
                }
                else {
                    currentNode.getParent().replaceChild(currentNode, partialChild);
                    currentNode.clear(false);
                    newNode = currentNode;
                }
                partialChild.addChild(newNode);
            }
            else {
                currentNode.getParent().replaceChild(currentNode, partialChild);
                currentNode.delete();
            }
            return true;
        }
        return false;
    }
    
    public boolean templateP6(final PQNode currentNode) throws Exception {
        if (!currentNode.isPNode() || currentNode.getNumPartialChildren() != 2) {
            return false;
        }
        final PQNode partialChild1 = currentNode.getPartialChild(0);
        final PQNode partialChild2 = currentNode.getPartialChild(1);
        if (!partialChild1.checkFullAreEndMost() || !partialChild2.checkFullAreEndMost()) {
            return false;
        }
        partialChild1.setPertinentLeafCount(currentNode.getPertinentLeafCount());
        if (currentNode.getNumFullChildren() > 0) {
            PQNode newNode = null;
            if (currentNode.getNumFullChildren() > 1) {
                newNode = new PQNode();
                newNode.labelAsFull();
                currentNode.moveFullChildrenTo(newNode);
            }
            else {
                newNode = currentNode.removeOnlyFullChild();
            }
            partialChild1.addChild(newNode);
        }
        currentNode.mergePartialChildren(partialChild2, partialChild1);
        partialChild2.delete();
        if (currentNode.hasOnlyOneChild()) {
            if (currentNode.getParent() != null) {
                if (currentNode.getParent().isDeleted()) {
                    currentNode.becomeChild(partialChild1);
                }
                else {
                    currentNode.getParent().replaceChild(currentNode, partialChild1);
                    currentNode.delete();
                    this.lastPertRoot = partialChild1;
                }
            }
            else {
                partialChild1.becomeRoot();
                this.setRoot(partialChild1);
                this.lastPertRoot = partialChild1;
            }
        }
        else {
            this.lastPertRoot = partialChild1;
        }
        return true;
    }
    
    public boolean templateQ1(final PQNode currentNode) throws Exception {
        if (currentNode.isQNode() && !currentNode.isPseudoNode() && currentNode.childrenAreFull()) {
            currentNode.labelAsFull();
            return true;
        }
        return false;
    }
    
    public boolean templateQ2(final PQNode currentNode) throws Exception {
        if (!currentNode.isQNode() || currentNode.getNumPartialChildren() > 1 || currentNode.isPseudoNode()) {
            return false;
        }
        if (!currentNode.checkFullAreAdjacent()) {
            return false;
        }
        if (currentNode.checkEndMostAreEmptyOrPartial() && currentNode.getNumFullChildren() != 0) {
            return false;
        }
        currentNode.labelAsPartial();
        if (currentNode.getNumPartialChildren() == 1) {
            final PQNodePair endMostChildren = currentNode.getEndMostChildren();
            final PQNode partialChild = currentNode.getPartialChild(0);
            if (!currentNode.checkFullAreAdjacentTo(partialChild)) {
                return false;
            }
            if (currentNode.getNumFullChildren() == 0 && !endMostChildren.contains(partialChild)) {
                return false;
            }
            if (!partialChild.checkFullAreEndMost()) {
                return false;
            }
            currentNode.absorbPartialChild(partialChild);
        }
        return true;
    }
    
    public boolean templateQ3(final PQNode currentNode) throws Exception {
        if (currentNode.isQNode() && currentNode.getNumPartialChildren() <= 2) {
            if (!currentNode.isPseudoNode()) {
                currentNode.checkEndMostAreEmptyOrPartial();
            }
            currentNode.labelAsPartial();
            if (currentNode.getNumPartialChildren() == 1) {
                final PQNode partialChild = currentNode.getPartialChild(0);
                if (!currentNode.checkFullAreAdjacentTo(partialChild)) {
                    return false;
                }
                if (!partialChild.checkFullAreEndMost()) {
                    return false;
                }
                if (currentNode.isPseudoNode() && !currentNode.checkPartialAreAtEnds()) {
                    return false;
                }
                currentNode.absorbPartialChild(partialChild);
            }
            else if (currentNode.getNumPartialChildren() == 2) {
                final PQNode partialChild2 = currentNode.getPartialChild(0);
                final PQNode partialChild3 = currentNode.getPartialChild(1);
                if (currentNode.getNumFullChildren() == 0 && !partialChild2.isSiblingOf(partialChild3)) {
                    return false;
                }
                if (!partialChild2.checkFullAreEndMost() || !partialChild3.checkFullAreEndMost()) {
                    return false;
                }
                if (!currentNode.checkFullAreAdjacentTo(partialChild2) || !currentNode.checkFullAreAdjacentTo(partialChild3)) {
                    return false;
                }
                if (currentNode.isPseudoNode() && !currentNode.checkPartialAreAtEnds()) {
                    return false;
                }
                currentNode.absorbPartialChild(partialChild2);
                currentNode.absorbPartialChild(partialChild3);
            }
            if (currentNode.isPseudoNode()) {
                currentNode.delete();
            }
            return true;
        }
        return false;
    }
    
    public void printFrontier() {
        final Vector leaves = this.getLeaves();
        for (int i = 0; i < leaves.size(); ++i) {
            System.out.println(leaves.elementAt(i));
        }
    }
    
    public void printTree() throws Exception {
        System.out.println("$$$ PRINT TREE START $$$");
        this.root.printStructure();
        System.out.println("$$$ PRINT TREE END $$$");
    }
    
    public void prepareToDrawTree() throws Exception {
        if (this.hasChanged) {
            final int depth = 0;
            final int width = this.root.countSubLeaves(depth);
            this.drawWidth = width * 24 + 50;
            this.drawHeight = this.root.getDepth() * 36 + 50;
        }
    }
    
    public int getNumNodes() throws Exception {
        return this.root.countSubNodes();
    }
    
    public int getNumDeletedNodes() throws Exception {
        return this.root.countSubDeletedNodes();
    }
}
