package net.java.sip.communicator.impl.osdependent.systemtray.appindicator.starcom;

import java.io.File;

public class NativeAppIndicator
{
  /** Set this listener to handle actions. **/
  public static MenuListener menuListener;
  
  private static String UI_XML_START = "<ui><popup name='IndicatorPopup'>";
  private static String UI_XML_MID = "<menuitem action='X'/>";
  private static String UI_XML_END = "</popup></ui>";
  
  /** Creates a new NativeAppIndicator. **/
  public NativeAppIndicator() {}
  
  /**
   * Init the TrayIcon.
   * <br/>Each icon must exist as file.
   * <br/>Alternatively you can use CompatibleIcon(I).getIconName()
   * @param iconFile The path to the app icon.
   * @param attIconFile The Attention-Icon file.
   * @param entries The menu-entries that are selectable from SysTray-Icon.
   **/
  public void initIndicator(final String appName, final String iconFile, final String attIconFile,final MenuEntry entries[])
  {
    Thread t = new Thread()
    {
      @Override
      public void run()
      {
        String trayIconFile = validateIcon(iconFile);
        String trayAttIconFile = validateIcon(attIconFile);
        initAndWait(appName, trayIconFile, trayAttIconFile, entries);
      }

    };
    t.start();
  }
  
  private String validateIcon(String iconFileS)
  {
    File iconFile = new File(iconFileS);
    if (iconFile.exists())
    {
      if (!iconFile.isAbsolute())
      {
        iconFileS = iconFile.getAbsolutePath();
      }
    }
    return iconFileS;
  }
  
  private void initAndWait(String appName, String iconFile, String attIconFile, MenuEntry entries[])
  {
    String entriesArr[] = new String[entries.length * 2];
    StringBuilder entriesSb = new StringBuilder(UI_XML_START);
    for (int i=0; i<entries.length; i++)
    {
      MenuEntry entry = entries[i];
      entriesArr[i*2] = entry.actionName;
      entriesArr[i*2+1] = entry.iconKey;
      entriesSb.append(UI_XML_MID.replace("X",entry.actionName));
    }
    entriesSb.append(UI_XML_END);
    init(appName, iconFile, attIconFile, entriesArr, entriesSb.toString());
  }
  
  /**
   * Update the icon of Tray.
   * <br/>Each icon must exist as file.
   * <br/>Alternatively you can use CompatibleIcon(I).getIconName()
   * @param iconFile The absolute path to the app icon.
   * @param attIconFile The Attention-Icon file.
   **/
  public void updateIcons(String iconFile, String attIconFile)
  {
    iconFile = validateIcon(iconFile);
    attIconFile = validateIcon(attIconFile);
    upIcons(iconFile, attIconFile);
  }
  
  private native void init(String appName, String iconFile, String attIconFile, String[] entriesArr, String entriesStr);
  private native void upIcons(String iconFile, String attIconFile);

  /** Dispose Tray-Icon **/
  public void quit() { quitApp(); }
  
  private static native void quitApp();
  
  private static void menuPressed(String actionName)
  {
    if (menuListener!=null) { menuListener.menuPressed(actionName); }
  }
}