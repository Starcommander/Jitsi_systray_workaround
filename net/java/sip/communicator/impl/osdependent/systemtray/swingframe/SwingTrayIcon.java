package net.java.sip.communicator.impl.osdependent.systemtray.swingframe;

import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.WindowConstants;

//import dorkbox.systemTray.SystemTray;
//import dorkbox.systemTray.SystemTrayMenuAction;

import net.java.sip.communicator.impl.osdependent.systemtray.TrayIcon;

public class SwingTrayIcon implements TrayIcon
{
    JFrame sysTrayFrame;
    JButton jitsiButton = new JButton("Jitsi");
    JButton menuButton = new JButton("Menu");
    
    
  public SwingTrayIcon(Object popup)
  {
    if (popup instanceof JPopupMenu)
    {
        final SwingActionListener menuShowListener = new SwingActionListener((JPopupMenu)popup);
        menuButton.addActionListener(createPopupListener(menuShowListener));
    }
    else
    {
        throw new IllegalArgumentException("Invalid popup menu type");
    }
  }
    
    
    
    private ActionListener createPopupListener(final SwingActionListener menuShowListener)
    {
        ActionListener act = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Point point = MouseInfo.getPointerInfo().getLocation();
                menuShowListener.actionPerformed(point.x, point.y);
            }
        }; 
        return act;
    }



    @Override
    public void addActionListener(ActionListener listener)
    {
        jitsiButton.addActionListener(listener);
    }

    @Override
    public void addBalloonActionListener(ActionListener listener)
    {
        // Not supported
        
    }

    @Override
    public void displayMessage(String caption, String text,
        MessageType messageType)
    {
        JOptionPane.showConfirmDialog(null, text);
    }

    @Override
    public void setIcon(ImageIcon icon) throws NullPointerException
    {
        jitsiButton.setIcon(icon);
    }

    @Override
    public void setIconAutoSize(boolean autoSize)
    {
        // Not used
    }



    public void setIconActive(boolean b)
    {
        if (sysTrayFrame==null)
        {
          sysTrayFrame = new JFrame("SysTray");
          sysTrayFrame.setLayout(new GridLayout(0,1));
          sysTrayFrame.add(jitsiButton);
          sysTrayFrame.add(menuButton);
          sysTrayFrame.setSize(200, 100);
          sysTrayFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        }
        sysTrayFrame.setVisible(b);
    }
}
