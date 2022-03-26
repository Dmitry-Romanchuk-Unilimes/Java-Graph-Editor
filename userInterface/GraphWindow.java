// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import graphStructure.Graph;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Vector;

public class GraphWindow extends JPanel
{
    private JDesktopPane desktopPane;
    
    public GraphWindow() {
        this.desktopPane = new JDesktopPane();
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints layoutCons = new GridBagConstraints();
        this.setLayout(layout);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 0;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(1, 1, 1, 1);
        layoutCons.anchor = 11;
        layoutCons.weightx = 120.0;
        layoutCons.weighty = 120.0;
        layout.setConstraints(this.desktopPane, layoutCons);
        this.add(this.desktopPane);
    }
    
    public void addGraphEditorWindow(final GraphController graphController) {
        final GraphEditorWindow gew = new GraphEditorWindow(graphController);
        this.desktopPane.add(gew);
        try {
            gew.setSelected(true);
        }
        catch (PropertyVetoException pve) {
            pve.printStackTrace();
        }
        this.desktopPane.getDesktopManager().activateFrame(gew);
    }
    
    public void addGraphEditorWindow(final GraphController graphController, final Graph graph) {
        final GraphEditorWindow gew = new GraphEditorWindow(graphController, graph);
        this.desktopPane.add(gew);
        try {
            gew.setSelected(true);
        }
        catch (PropertyVetoException pve) {
            pve.printStackTrace();
        }
        this.desktopPane.getDesktopManager().activateFrame(gew);
    }
    
    public void close(final JGraphEdInternalFrame intFrame) {
        intFrame.dispose();
    }
    
    public void activate(final JGraphEdInternalFrame intFrame) {
        try {
            if (intFrame.isIcon()) {
                intFrame.setIcon(false);
            }
            else {
                intFrame.setSelected(true);
            }
        }
        catch (PropertyVetoException pve) {
            pve.printStackTrace();
        }
        if (intFrame.isSelected()) {
            this.desktopPane.getDesktopManager().activateFrame(intFrame);
        }
    }
    
    public void show(final JGraphEdInternalFrame intFrame) {
        if (!intFrame.isIcon() && this.desktopPane.getIndexOf(intFrame) == -1) {
            intFrame.setVisible(true);
            this.desktopPane.add(intFrame);
        }
        try {
            if (intFrame.isIcon()) {
                intFrame.setIcon(false);
            }
            intFrame.setSelected(true);
        }
        catch (PropertyVetoException pve) {
            pve.printStackTrace();
        }
        this.desktopPane.getDesktopManager().activateFrame(intFrame);
    }
    
    public void showInfo(final GraphEditorWindow window) {
        if (this.desktopPane.getIndexOf(window.getInfoWindow()) == -1) {
            window.getInfoWindow().setVisible(true);
            this.desktopPane.add(window.getInfoWindow());
        }
        window.getInfoWindow().update();
        try {
            window.getInfoWindow().setClosed(false);
            window.getInfoWindow().setSelected(true);
        }
        catch (PropertyVetoException pve) {
            pve.printStackTrace();
        }
        this.desktopPane.getDesktopManager().activateFrame(window.getInfoWindow());
    }
    
    public void closeInfo(final GraphEditorInfoWindow window) {
        try {
            window.setClosed(true);
        }
        catch (PropertyVetoException pve) {
            pve.printStackTrace();
        }
    }
    
    public void showLog(final GraphEditorWindow window) {
        if (this.desktopPane.getIndexOf(window.getLogWindow()) == -1) {
            window.getLogWindow().setVisible(true);
            this.desktopPane.add(window.getLogWindow());
        }
        window.getLogWindow().update();
        try {
            window.getLogWindow().setClosed(false);
            window.getLogWindow().setSelected(true);
        }
        catch (PropertyVetoException pve) {
            pve.printStackTrace();
        }
        this.desktopPane.getDesktopManager().activateFrame(window.getLogWindow());
    }
    
    public void closeLog(final GraphEditorLogWindow window) {
        try {
            window.setClosed(true);
        }
        catch (PropertyVetoException pve) {
            pve.printStackTrace();
        }
    }
    
    public void showDialog(final GraphEditorDialog dialog) {
        if (this.desktopPane.getIndexOf(dialog) == -1) {
            dialog.setVisible(true);
            this.desktopPane.add(dialog);
        }
        try {
            dialog.setClosed(false);
            dialog.setSelected(true);
        }
        catch (PropertyVetoException pve) {
            pve.printStackTrace();
        }
        this.desktopPane.getDesktopManager().activateFrame(dialog);
    }
    
    public void activateDialog(final GraphEditorDialog dialog) {
        try {
            if (dialog.getOwner().isSelected()) {
                dialog.getOwner().setSelected(false);
            }
            dialog.setClosed(false);
            dialog.setSelected(true);
        }
        catch (PropertyVetoException pve) {
            pve.printStackTrace();
        }
        this.desktopPane.getDesktopManager().activateFrame(dialog);
    }
    
    public void forceGraphRepaints() {
        final JInternalFrame[] frames = this.desktopPane.getAllFrames();
        for (int i = 0; i < frames.length; ++i) {
            if (frames[i] instanceof GraphEditorWindow) {
                ((GraphEditorWindow)frames[i]).getGraphEditor().getGraph().markForRepaint();
            }
        }
        this.repaint();
    }
    
    public Vector getAllGraphEditorWindows() {
        final Vector toReturn = new Vector();
        final JInternalFrame[] frames = this.desktopPane.getAllFrames();
        for (int i = 0; i < frames.length; ++i) {
            if (frames[i] instanceof GraphEditorWindow) {
                toReturn.add(frames[i]);
            }
        }
        return toReturn;
    }
}
