// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.menuAndToolBar;

import userInterface.GraphController;

import javax.swing.*;
import java.util.Vector;

public class CompoundMenuAndToolBarAction extends MenuAndToolBarAction
{
    private Vector subActions;
    private ButtonChooser bc;
    
    public CompoundMenuAndToolBarAction(final String text, final ImageIcon icon, final String desc, final Integer mnemonic, final boolean enabled, final int width, final Vector subActions, final GraphController controller) {
        super(text, icon, desc, mnemonic, enabled, width, null, false, controller);
        this.subActions = subActions;
        final ButtonGroup buttonGroup = new ButtonGroup();
        ((JRadioButtonMenuItem)subActions.firstElement().getMenuItem()).setSelected(true);
        for (int i = 0; i < subActions.size(); ++i) {
            final MenuAndToolBarAction subAction = subActions.elementAt(i);
            buttonGroup.add(subAction.getMenuItem());
            subAction.setParentAction(this);
        }
        final ButtonChooser buttonChooser = new ButtonChooser(this, subActions);
        this.bc = buttonChooser;
        this.button = buttonChooser;
    }
    
    public void setEnabled(final boolean enabled) {
        if (this.bc != null) {
            this.bc.setEnabled(enabled);
        }
        super.setEnabled(enabled);
        if (this.subActions != null) {
            for (int i = 0; i < this.subActions.size(); ++i) {
                this.subActions.elementAt(i).setEnabled(enabled);
            }
        }
    }
    
    public Vector getButtons() {
        final Vector buttons = new Vector(this.subActions.size());
        for (int i = 0; i < this.subActions.size(); ++i) {
            buttons.add(this.subActions.get(i).getButton());
        }
        return buttons;
    }
    
    public Vector getMenuItems() {
        final Vector menuItems = new Vector(this.subActions.size());
        for (int i = 0; i < this.subActions.size(); ++i) {
            menuItems.add(this.subActions.get(i).getMenuItem());
        }
        return menuItems;
    }
    
    public boolean equalsText(final String text) {
        if (super.equalsText(text)) {
            return true;
        }
        for (int i = 0; i < this.subActions.size(); ++i) {
            if (this.subActions.elementAt(i).equalsText(text)) {
                return true;
            }
        }
        return false;
    }
    
    public MenuAndToolBarAction getActionWithText(final String text) {
        if (this.getText().equals(text)) {
            return this;
        }
        for (int i = 0; i < this.subActions.size(); ++i) {
            if (this.subActions.elementAt(i).equalsText(text)) {
                return this.subActions.elementAt(i);
            }
        }
        return null;
    }
    
    public boolean isCompound() {
        return true;
    }
    
    public ButtonChooser getButtonChooser() {
        return this.bc;
    }
}
