package net.java.sip.communicator.impl.osdependent.systemtray.swingframe;

import javax.swing.ImageIcon;

import net.java.sip.communicator.impl.osdependent.systemtray.SystemTray;
import net.java.sip.communicator.impl.osdependent.systemtray.TrayIcon;

public class SwingSystemTray extends SystemTray
{
    @Override
    public void addTrayIcon(TrayIcon trayIcon)
    {
        SwingTrayIcon dbTrayIcon = (SwingTrayIcon)trayIcon;
        dbTrayIcon.setIconActive(true);
    }

    @Override
    public TrayIcon createTrayIcon(ImageIcon icon, String tooltip, Object popup)
    {
//        dorkbox.systemTray.SystemTray systemTray;
//        systemTray = dorkbox.systemTray.SystemTray.getSystemTray();
        SwingTrayIcon dbTrayIcon = new SwingTrayIcon(popup);
        dbTrayIcon.setIcon(icon);
//popup.toString();
        return dbTrayIcon;
    }

    @Override
    public boolean useSwingPopupMenu()
    {
        return true;
    }


}
