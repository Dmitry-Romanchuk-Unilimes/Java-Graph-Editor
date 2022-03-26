// 
// Decompiled by Procyon v0.5.36
// 

package graphStructure.mementos;

import graphStructure.Graph;

public interface MementoInterface
{
    void apply(final Graph p0);
    
    boolean isUseless();
}
