package net.java.sip.communicator.impl.osdependent.systemtray.appindicator;

import javax.swing.ImageIcon;

import net.java.sip.communicator.impl.osdependent.systemtray.SystemTray;
import net.java.sip.communicator.impl.osdependent.systemtray.TrayIcon;

public class IndicatorSystemTray extends SystemTray
{

  @Override
  public void addTrayIcon(TrayIcon trayIcon)
  {
    IndicatorTrayIcon inTrayIcon = (IndicatorTrayIcon) trayIcon;
    inTrayIcon.setIconActive();
  }

  @Override
  public TrayIcon createTrayIcon(ImageIcon icon, String tooltip, Object popup)
  {
    IndicatorTrayIcon iti = new IndicatorTrayIcon(popup);
    iti.setIcon(icon);
    return iti;
  }

  @Override
  public boolean useSwingPopupMenu()
  {
    return true;
  }
  
}