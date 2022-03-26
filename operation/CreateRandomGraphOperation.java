// 
// Decompiled by Procyon v0.5.36
// 

package operation;

import graphStructure.Graph;
import graphStructure.Location;

import java.util.Random;

public class CreateRandomGraphOperation
{
    public static void createRandomNodes(final Graph g, final int numNodes, final int width, final int height) {
        createRandomNodes(g, numNodes, width, height, System.currentTimeMillis());
    }
    
    public static void createRandomNodes(final Graph g, final int numNodes, final int width, final int height, final long seed) {
        g.deleteAll();
        final Random random = new Random(seed);
        boolean useX;
        double interval;
        if (height <= width) {
            useX = true;
            interval = width / (double)numNodes;
        }
        else {
            useX = false;
            interval = height / (double)numNodes;
        }
        for (int i = 0; i < numNodes; ++i) {
            if (useX) {
                g.createNode(new Location(i * interval, random.nextDouble() * height));
            }
            else {
                g.createNode(new Location(random.nextDouble() * width, i * interval));
            }
        }
    }
}
