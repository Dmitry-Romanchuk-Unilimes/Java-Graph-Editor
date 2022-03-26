// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;

class JGraphEdInternalFrameAction extends AbstractAction
{
    public JGraphEdInternalFrameAction(final String text, final ImageIcon icon, final String desc, final Integer mnemonic, final boolean enabled) {
        super(text, icon);
        this.setEnabled(enabled);
        this.putValue("ShortDescription", desc);
        this.putValue("MnemonicKey", mnemonic);
    }
    
    public void actionPerformed(final ActionEvent e) {
    }
}
