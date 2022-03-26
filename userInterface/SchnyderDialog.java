// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SchnyderDialog extends GraphEditorDialog implements ActionListener
{
    public static int WIDTH;
    public static int HEIGHT;
    private boolean useEmbeddingBox;
    private boolean useRandomButton;
    private GraphEditorWindow owner;
    private JButton runButton;
    private JCheckBox embeddingBox;
    private JButton randomButton;
    
    static {
        SchnyderDialog.WIDTH = 400;
        SchnyderDialog.HEIGHT = 200;
    }
    
    public SchnyderDialog(final GraphController controller, final GraphEditorWindow owner, final String title, final String message) {
        this(controller, owner, title, message, false, false);
    }
    
    public SchnyderDialog(final GraphController controller, final GraphEditorWindow owner, final String title, final String message, final boolean useEmbeddingBox, final boolean useRandomButton) {
        super(controller, String.valueOf(owner.getTitle()) + " - " + title, true, true, true, false);
        this.owner = owner;
        this.useEmbeddingBox = useEmbeddingBox;
        this.useRandomButton = useRandomButton;
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints layoutCons = new GridBagConstraints();
        this.getContentPane().setLayout(layout);
        final JLabel messageLabel = new JLabel(message);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(messageLabel, layoutCons);
        this.getContentPane().add(messageLabel);
        if (useEmbeddingBox) {
            (this.embeddingBox = new JCheckBox("Display on Embedding")).setSelected(controller.getDrawOnEmbedding());
            layoutCons.gridx = -1;
            layoutCons.gridy = -1;
            layoutCons.gridwidth = 0;
            layoutCons.gridheight = 1;
            layoutCons.fill = 1;
            layoutCons.insets = new Insets(3, 3, 3, 3);
            layoutCons.anchor = 11;
            layoutCons.weightx = 1.0;
            layoutCons.weighty = 1.0;
            layout.setConstraints(this.embeddingBox, layoutCons);
            this.getContentPane().add(this.embeddingBox);
        }
        if (useRandomButton) {
            (this.randomButton = new JButton("Run With Random Outer Face")).addActionListener(this);
            layoutCons.gridx = -1;
            layoutCons.gridy = -1;
            layoutCons.gridwidth = 0;
            layoutCons.gridheight = 1;
            layoutCons.fill = 1;
            layoutCons.insets = new Insets(3, 3, 3, 3);
            layoutCons.anchor = 11;
            layoutCons.weightx = 1.0;
            layoutCons.weighty = 1.0;
            layout.setConstraints(this.randomButton, layoutCons);
            this.getContentPane().add(this.randomButton);
        }
        if (useRandomButton) {
            this.runButton = new JButton("Run With Selected Outer Face");
        }
        else {
            this.runButton = new JButton("Run");
        }
        this.runButton.addActionListener(this);
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.runButton, layoutCons);
        this.getContentPane().add(this.runButton);
        this.addInternalFrameListener(controller);
        this.setSize(SchnyderDialog.WIDTH, SchnyderDialog.HEIGHT);
        this.setVisible(true);
    }
    
    public JButton getRandomButton() {
        return this.randomButton;
    }
    
    public boolean getOnEmbedding() {
        return this.embeddingBox.isSelected();
    }
    
    public void enableRunButton() {
        this.runButton.setEnabled(true);
    }
    
    public void disableRunButton() {
        this.runButton.setEnabled(false);
    }
    
    public GraphEditorWindow getOwner() {
        return this.owner;
    }
    
    public void setOwner(final GraphEditorWindow o) {
        this.owner = o;
    }
    
    public void actionPerformed(final ActionEvent e) {
    }
}
