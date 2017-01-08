package net.java.sip.communicator.impl.osdependent.systemtray.appindicator;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import net.java.sip.communicator.impl.osdependent.systemtray.TrayIcon;
import net.java.sip.communicator.impl.osdependent.systemtray.appindicator.starcom.CompatibleIcon;
import net.java.sip.communicator.impl.osdependent.systemtray.appindicator.starcom.MenuEntry;
import net.java.sip.communicator.impl.osdependent.systemtray.appindicator.starcom.MenuListener;
import net.java.sip.communicator.impl.osdependent.systemtray.appindicator.starcom.NativeAppIndicator;
import net.java.sip.communicator.impl.osdependent.systemtray.swingframe.SwingActionListener;

public class IndicatorTrayIcon implements TrayIcon
{
  /** Set this var to true, to load installed libs of Linux filesystem instead.
   * <br/>Package libappindicator has to be installed. **/
  public boolean useSystemLib = false;
  
  /** Set this var to true, if libNativeAppIndicator.so is already stored on filesystem.
   * <br/>Path must be included into java.library.path! **/
  public boolean useStoredLib = false;
  
  NativeAppIndicator appIndicator;
  HashMap<String,String> iconMap = new HashMap<String,String>();
  String currentIconFile;
  ActionListener showJitsilistener;
  SwingActionListener menuShowListener;
  
  public IndicatorTrayIcon(Object popup)
  {
    if (!(popup instanceof JPopupMenu))
    {
        throw new IllegalArgumentException("Invalid popup menu type");
    }
    menuShowListener = new SwingActionListener((JPopupMenu)popup);
    NativeAppIndicator.menuListener = createListener(menuShowListener);
  }
  
  private MenuListener createListener(final SwingActionListener menuShowListener)
  {
    MenuListener act = new MenuListener()
    {
      @Override
      public void menuPressed(String actionName)
      {
        final String actionNameFinal = actionName;
        Runnable t = new Runnable()
        {
          @Override
          public void run()
          {
            menuPressedInvoke(actionNameFinal);
          }
        };
        SwingUtilities.invokeLater(t);
      }
      
      private void menuPressedInvoke(String actionName)
      {
        if (actionName.equals("Jitsi"))
        {
          if (showJitsilistener!=null)
          {
            showJitsilistener.actionPerformed(null);
          }
        }
        else
        {
          if (menuShowListener!=null)
          {
            Point point = MouseInfo.getPointerInfo().getLocation();
            menuShowListener.actionPerformed(point.x, point.y);
          }
        }
      }
    }; 
    return act;
  }

  @Override
  public void addActionListener(ActionListener listener)
  {
    this.showJitsilistener = listener;
  }

  @Override
  public void addBalloonActionListener(ActionListener listener)
  {
    // Not supported!
    
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
    String key = icon.toString();
    if (iconMap.containsKey(key)) { currentIconFile = iconMap.get(key); }
    else
    {
      try
      {
        File target = File.createTempFile("Jitsi", ".png");
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        Graphics2D g = image.createGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        boolean written = javax.imageio.ImageIO.write(image,"PNG",target);
        if (written)
        {
          currentIconFile = target.getPath();
          iconMap.put(key, currentIconFile);
          target.deleteOnExit();
          if (appIndicator!=null)
          {
            appIndicator.updateIcons(currentIconFile, currentIconFile);
          }
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    setIconActive();
  }

  @Override
  public void setIconAutoSize(boolean autoSize)
  {
    // Not supported
  }
  
  public void setIconActive()
  {
    if (appIndicator==null && currentIconFile!=null)
    {
      loadLib();
      appIndicator = new NativeAppIndicator();
      MenuEntry entries[] = new MenuEntry[2];
      entries[0] = new MenuEntry("Jitsi", new CompatibleIcon(CompatibleIcon.IconName.media_playback_start));
      entries[1] = new MenuEntry("Menu", new CompatibleIcon(CompatibleIcon.IconName.applications_system));
      appIndicator.initIndicator("Jitsi", currentIconFile, currentIconFile, entries);
      Runtime.getRuntime().addShutdownHook(new Thread()
      {
        @Override
        public void run()
        {
          appIndicator.quit();
        }
      });
    }
  }
  
  private void loadLib()
  {
    String libPath = "/net/java/sip/communicator/impl/osdependent/systemtray/appindicator/starcom/";
    String libPathLinuxA = libPath;
    String libPathLinuxB = libPath;
    String libPathLinuxC = libPath;
    String libPathLinuxD = libPath;
    String arch = System.getProperty("os.arch");
    if (arch.toLowerCase().equals("amd64"))
    {
      libPath = libPath + "linux64/libNativeAppIndicator.so";
      libPathLinuxA = libPathLinuxA + "linux64/libdbusmenu-glib.so.4.0.12";
      libPathLinuxB = libPathLinuxB + "linux64/libdbusmenu-gtk.so.4.0.12";
      libPathLinuxC = libPathLinuxC + "linux64/libindicator.so.7.0.0";
      libPathLinuxD = libPathLinuxD + "linux64/libappindicator.so.1.0.0";
    }
    else
    {
      libPath = libPath + "linux32/libNativeAppIndicator.so";
      libPathLinuxA = libPathLinuxA + "linux32/libdbusmenu-glib.so.4.0.12";
      libPathLinuxB = libPathLinuxB + "linux32/libdbusmenu-gtk.so.4.0.12";
      libPathLinuxC = libPathLinuxC + "linux32/libindicator.so.7.0.0";
      libPathLinuxD = libPathLinuxD + "linux32/libappindicator.so.1.0.0";
    }
    if (!useSystemLib)
    {
      loadLibResource(libPathLinuxA);
      loadLibResource(libPathLinuxB);
      loadLibResource(libPathLinuxC);
      loadLibResource(libPathLinuxD);
    }
    if (useStoredLib)
    {
      System.loadLibrary("NativeAppIndicator");
    }
    else
    {
      loadLibResource(libPath);
    }
  }
  
  private void loadLibResource(String libPath)
  {
    try
    {
      File libFile = File.createTempFile("AppIndicator", ".so");
      InputStream srcLib = IndicatorTrayIcon.class.getResourceAsStream(libPath);
      java.nio.file.Files.copy(srcLib, libFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      System.load(libFile.getPath());
      libFile.deleteOnExit();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

}