// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import dataStructure.DoublyLinkedList;
import graphStructure.Graph;

public class MementoGrouper implements MementoInterface
{
    private static boolean TRACE;
    private DoublyLinkedList list;
    private String title;
    private boolean reverse;
    
    static {
        MementoGrouper.TRACE = false;
    }
    
    public MementoGrouper(final String title) {
        if (MementoGrouper.TRACE) {
            System.out.println("\n\nnew memento grouper: " + title);
        }
        this.title = title;
        this.list = new DoublyLinkedList();
        this.reverse = false;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void addMemento(final MementoInterface memento) {
        if (MementoGrouper.TRACE) {
            System.out.println("add to memento grouper: " + memento);
        }
        this.list.enqueue(memento);
    }
    
    public void apply(final Graph graph) {
        if (MementoGrouper.TRACE) {
            System.out.print("\n\napplying memento grouper: " + this.title);
        }
        if (this.reverse) {
            if (MementoGrouper.TRACE) {
                System.out.println(" (redo)");
            }
            this.list.toHead();
        }
        else {
            if (MementoGrouper.TRACE) {
                System.out.println(" (undo)");
            }
            this.list.toTail();
        }
        while (this.list.getCurrent() != null) {
            final MementoInterface memento = (MementoInterface)this.list.getCurrent();
            if (MementoGrouper.TRACE) {
                System.out.println("applying: " + memento);
            }
            memento.apply(graph);
            if (this.reverse) {
                this.list.toNext();
            }
            else {
                this.list.toPrev();
            }
        }
        this.reverse = !this.reverse;
    }
    
    public int size() {
        return this.list.size();
    }
    
    public void removeUselessMementos() {
        if (this.list.size() > 0) {
            this.list.toHead();
            while (this.list.getCurrent() != null) {
                if (((MementoInterface)this.list.getCurrent()).isUseless()) {
                    this.list.removeCurrent();
                }
                else {
                    this.list.toNext();
                }
            }
        }
    }
    
    public boolean isUseless() {
        if (this.list.size() > 0) {
            this.list.toHead();
            while (this.list.getCurrent() != null) {
                if (!((MementoInterface)this.list.getCurrent()).isUseless()) {
                    return false;
                }
                this.list.toNext();
            }
            return true;
        }
        return true;
    }
    
    public String toString() {
        return this.title;
    }
}
