// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.menuAndToolBar;

import userInterface.GraphController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.lang.reflect.Method;

public class MenuAndToolBarAction extends AbstractAction implements Serializable
{
    private GraphController controller;
    protected CompoundMenuAndToolBarAction parentAction;
    private int width;
    protected JMenuItem menuItem;
    protected JButton button;
    private Method method;
    
    public MenuAndToolBarAction(final String text, final ImageIcon icon, final String desc, final Integer mnemonic, final boolean enabled, final int width, final Method method, final boolean isChild, final GraphController controller) {
        super(text, icon);
        this.setEnabled(enabled);
        this.putValue("ShortDescription", desc);
        this.putValue("MnemonicKey", mnemonic);
        this.width = width;
        this.method = method;
        (this.button = new JButton(this)).setText("");
        if (isChild) {
            this.menuItem = new JRadioButtonMenuItem(this);
        }
        else {
            this.menuItem = new JMenuItem(this);
        }
        this.controller = controller;
        this.menuItem.setIcon(null);
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (this.method != null) {
            try {
                this.method.invoke(this.controller, (Object[])null);
                this.controller.update();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public boolean equalsText(final String text) {
        return this.getText().equals(text);
    }
    
    public MenuAndToolBarAction getActionWithText(final String text) {
        if (this.equalsText(text)) {
            return this;
        }
        return null;
    }
    
    public JButton getButton() {
        return this.button;
    }
    
    public JMenuItem getMenuItem() {
        return this.menuItem;
    }
    
    public String getText() {
        return (String)this.getValue("Name");
    }
    
    public CompoundMenuAndToolBarAction getParentAction() {
        return this.parentAction;
    }
    
    public void setParentAction(final CompoundMenuAndToolBarAction parentAction) {
        this.parentAction = parentAction;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public boolean isCompound() {
        return false;
    }
    
    public void putValue(final String key, final Object newValue) {
        if (this.menuItem == null && this.button == null) {
            super.putValue(key, newValue);
        }
        else if (key.equals("Name")) {
            this.menuItem.setText((String)newValue);
        }
        else if (key.equals("SmallIcon")) {
            this.button.setIcon((Icon)newValue);
        }
        else {
            super.putValue(key, newValue);
        }
    }
}
