package net.java.sip.communicator.impl.osdependent.systemtray.swingframe;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.RootPaneContainer;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.jitsi.util.OSUtils;

/** Similar to AWTMouseAdapter. **/
public class SwingActionListener
{
    private JPopupMenu popup = null;
    private Window hiddenWindow = null;

    public SwingActionListener(JPopupMenu p)
    {
        this.popup = p;
        this.popup.addPopupMenuListener(new PopupMenuListener()
        {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e)
            {}

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
            {
                if (hiddenWindow != null)
                {
                    hiddenWindow.dispose();
                    hiddenWindow = null;
                }
            }

            public void popupMenuCanceled(PopupMenuEvent e)
            {
                if (hiddenWindow != null)
                {
                    hiddenWindow.dispose();
                    hiddenWindow = null;
                }
            }
        });
    }

    public void actionPerformed(int posX, int posY)
    {
        if (popup != null)
        {
            if (hiddenWindow == null)
            {
                if (OSUtils.IS_WINDOWS)
                {
                    hiddenWindow = new JDialog((Frame) null);
                    ((JDialog) hiddenWindow).setUndecorated(true);
                }
                else
                {
                    hiddenWindow = new JWindow((Frame) null);
                }

                hiddenWindow.setAlwaysOnTop(true);
                Dimension size = popup.getPreferredSize();

                Point centerPoint = GraphicsEnvironment
                                        .getLocalGraphicsEnvironment()
                                            .getCenterPoint();

                if(posY > centerPoint.getY())
                    hiddenWindow
                        .setLocation(posX, posY - size.height);
                else
                    hiddenWindow
                        .setLocation(posX, posY);

                hiddenWindow.setVisible(true);

                popup.show(
                        ((RootPaneContainer)hiddenWindow).getContentPane(),
                        0, 0);

                // popup works only for focused windows
                hiddenWindow.toFront();
            }
            else
            {
                hiddenWindow.dispose();
                hiddenWindow = null;
            }
        }
    }

}
