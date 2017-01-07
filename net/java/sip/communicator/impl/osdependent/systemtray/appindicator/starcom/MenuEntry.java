package net.java.sip.communicator.impl.osdependent.systemtray.appindicator.starcom;

public class MenuEntry
{
  String actionName;
  String iconKey;

  /** Creates a new MenuEntry.
    * @param actionName The name for the menuentry.
    * @param iconKey The key as specified in CompatibleIcon.class.
    **/
  public MenuEntry(String actionName, CompatibleIcon iconKey)
  {
    this.actionName = actionName;
    this.iconKey = iconKey.getIconName();
  }
}