// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.menuAndToolBar;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class ButtonChooser extends JButton implements ActionListener, MouseListener, FocusListener, AncestorListener
{
    private int selectedIndex;
    private Vector buttons;
    private JWindow chooserPopupWindow;
    private boolean popupInited;
    private Action action;
    
    public ButtonChooser(final Action action, final Vector actions) {
        super((Icon)actions.firstElement().getValue("SmallIcon"));
        this.action = action;
        this.setToolTipText((String)action.getValue("ShortDescription"));
        this.selectedIndex = 0;
        this.buttons = new Vector(actions.size());
        this.popupInited = false;
        for (int i = 0; i < actions.size(); ++i) {
            this.buttons.addElement(actions.elementAt(i).getButton());
        }
        this.addFocusListener(this);
        this.addMouseListener(this);
        this.addAncestorListener(this);
    }
    
    public Vector getButtons() {
        return this.buttons;
    }
    
    public int getSelectedIndex() {
        return this.selectedIndex;
    }
    
    public void setSelectedIndex(final int index) {
        if (index >= 0 && index < this.buttons.size()) {
            this.selectedIndex = index;
            this.setIcon(this.buttons.elementAt(this.selectedIndex).getIcon());
        }
    }
    
    public JButton getSelected() {
        if (this.buttons == null || this.selectedIndex >= this.buttons.size()) {
            return null;
        }
        return this.buttons.elementAt(this.selectedIndex);
    }
    
    public void setSelected(final JButton button) {
        final int index = this.buttons.indexOf(button);
        if (index != -1) {
            this.selectedIndex = index;
            this.setIcon(this.buttons.elementAt(this.selectedIndex).getIcon());
        }
    }
    
    public void showPopup() {
        if (!this.popupInited) {
            this.popupInited = true;
            this.chooserPopupWindow = new JWindow(this.getParentWindow(this));
            final JPanel popupPanel = new JPanel();
            final GridLayout layout = new GridLayout(this.buttons.size(), 1);
            popupPanel.setLayout(layout);
            for (int i = 0; i < this.buttons.size(); ++i) {
                final JButton button = this.buttons.elementAt(i);
                button.setText("");
                button.addActionListener(this);
                popupPanel.add(button);
            }
            final Dimension dim = popupPanel.getPreferredSize();
            dim.width = this.getWidth();
            popupPanel.setSize(dim);
            popupPanel.setLocation(0, 0);
            popupPanel.setVisible(true);
            this.chooserPopupWindow.setFocusableWindowState(false);
            this.chooserPopupWindow.addFocusListener(this);
            this.chooserPopupWindow.setSize(popupPanel.getSize());
            this.chooserPopupWindow.getLayeredPane().add(popupPanel, JLayeredPane.POPUP_LAYER);
        }
        final Point location = new Point(0, this.getHeight() + this.getInsets().bottom);
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        GraphicsConfiguration gc = null;
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice[] gd = ge.getScreenDevices();
        for (int j = 0; j < gd.length; ++j) {
            if (gd[j].getType() == 0) {
                final GraphicsConfiguration dgc = gd[j].getDefaultConfiguration();
                if (dgc.getBounds().contains(location)) {
                    gc = dgc;
                    break;
                }
            }
        }
        Insets screenInsets;
        Rectangle screenBounds;
        if (gc != null) {
            screenInsets = toolkit.getScreenInsets(gc);
            screenBounds = gc.getBounds();
        }
        else {
            screenInsets = new Insets(0, 0, 0, 0);
            screenBounds = new Rectangle(toolkit.getScreenSize());
        }
        final int scrWidth = screenBounds.width - Math.abs(screenInsets.left + screenInsets.right);
        final int scrHeight = screenBounds.height - Math.abs(screenInsets.top + screenInsets.bottom);
        final Dimension size = this.chooserPopupWindow.getPreferredSize();
        SwingUtilities.convertPointToScreen(location, this);
        if (location.x + size.width > screenBounds.x + scrWidth) {
            location.x = screenBounds.x + scrWidth - size.width;
        }
        if (location.y + size.height > screenBounds.y + scrHeight) {
            location.y = screenBounds.y + scrHeight - size.height;
        }
        if (location.x < screenBounds.x) {
            location.x = screenBounds.x;
        }
        if (location.y < screenBounds.y) {
            location.y = screenBounds.y;
        }
        this.chooserPopupWindow.setLocation(location.x, location.y);
        this.chooserPopupWindow.setVisible(true);
    }
    
    public void hidePopup() {
        if (this.chooserPopupWindow != null) {
            this.chooserPopupWindow.setVisible(false);
        }
    }
    
    public void focusGained(final FocusEvent e) {
    }
    
    public void focusLost(final FocusEvent e) {
        this.hidePopup();
    }
    
    public void actionPerformed(final ActionEvent e) {
        final JButton source = (JButton)e.getSource();
        this.selectedIndex = this.buttons.indexOf(source);
        this.hidePopup();
    }
    
    public void mouseClicked(final MouseEvent e) {
    }
    
    public void mouseReleased(final MouseEvent e) {
    }
    
    public void mouseEntered(final MouseEvent e) {
    }
    
    public void mouseExited(final MouseEvent e) {
        if (e.getY() < 5 || ((e.getX() < 5 || e.getX() > this.getWidth() - 5) && e.getY() < this.getHeight() - 5)) {
            this.hidePopup();
        }
    }
    
    public void mousePressed(final MouseEvent e) {
        if (e.getSource() == this) {
            if (this.chooserPopupWindow != null && this.chooserPopupWindow.isVisible()) {
                this.hidePopup();
            }
            else {
                this.showPopup();
            }
        }
    }
    
    public void ancestorMoved(final AncestorEvent event) {
        this.hidePopup();
    }
    
    public void ancestorAdded(final AncestorEvent event) {
    }
    
    public void ancestorRemoved(final AncestorEvent event) {
    }
    
    private Window getParentWindow(final Component owner) {
        Window window = null;
        if (owner instanceof Window) {
            window = (Window)owner;
        }
        else if (owner != null) {
            window = SwingUtilities.getWindowAncestor(owner);
        }
        if (window == null) {
            window = new Frame();
        }
        return window;
    }
    
    public void setAction(final Action action) {
        this.action = action;
    }
    
    public Action getAction() {
        return this.action;
    }
}
