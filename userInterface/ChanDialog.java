// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChanDialog extends GraphEditorDialog implements ActionListener
{
    public static int WIDTH;
    public static int HEIGHT;
    private ButtonGroup buttonGroup;
    private JRadioButton button1;
    private JRadioButton button2;
    private JRadioButton button3;
    private GraphEditorWindow owner;
    private JButton runButton;
    
    static {
        ChanDialog.WIDTH = 400;
        ChanDialog.HEIGHT = 200;
    }
    
    public ChanDialog(final GraphController controller, final GraphEditorWindow owner, final String title, final String message) {
        super(controller, String.valueOf(owner.getTitle()) + " - " + title, true, true, true, false);
        this.owner = owner;
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
        this.button1 = new JRadioButton("Method 1");
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.button1, layoutCons);
        this.getContentPane().add(this.button1);
        this.button2 = new JRadioButton("Method 2");
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.button2, layoutCons);
        this.getContentPane().add(this.button2);
        this.button3 = new JRadioButton("Method 3");
        layoutCons.gridx = -1;
        layoutCons.gridy = -1;
        layoutCons.gridwidth = 0;
        layoutCons.gridheight = 1;
        layoutCons.fill = 1;
        layoutCons.insets = new Insets(3, 3, 3, 3);
        layoutCons.anchor = 11;
        layoutCons.weightx = 1.0;
        layoutCons.weighty = 1.0;
        layout.setConstraints(this.button3, layoutCons);
        this.getContentPane().add(this.button3);
        this.buttonGroup = new ButtonGroup();
        this.button1.setSelected(true);
        this.buttonGroup.add(this.button1);
        this.buttonGroup.add(this.button2);
        this.buttonGroup.add(this.button3);
        (this.runButton = new JButton("Run")).addActionListener(this);
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
        this.setSize(ChanDialog.WIDTH, ChanDialog.HEIGHT);
        this.setVisible(true);
    }
    
    public int getSelectedMethodNumber() {
        if (this.button1.isSelected()) {
            return 1;
        }
        if (this.button2.isSelected()) {
            return 2;
        }
        if (this.button3.isSelected()) {
            return 3;
        }
        return -1;
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
