/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package metamindmachine;

import java.io.*;
import java.util.*;

import javax.usb.*;
import java.util.List;



/**
 *
 * @author rain
 */
public final class LEDCtrl {
    static UsbDevice usbdev;
    
    static void init() {
      try
      {
          // Access the system USB services, and access to the root 
          // hub. Then traverse through the root hub.
          System.out.println("detecting usb...");
          UsbServices services = UsbHostManager.getUsbServices();
          UsbHub rootHub = services.getRootUsbHub();
          TraverseUSB.traverse(rootHub);
          System.out.println("USB system OK");
          if(usbdev != null) {
              System.out.println("rainflash found...");
          }
          
          
      } catch (Exception e) {}

    }
    
    static void LEDon() {
      try
      {
        byte bmRequestType = UsbConst.REQUESTTYPE_DIRECTION_OUT | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        byte bRequest = 0;
        short wValue = 0;
        short wIndex = 0;
        UsbControlIrp usbctrlreq = usbdev.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);        
        usbdev.syncSubmit(usbctrlreq);
          
          
      } catch (Exception e) {}        
        
    }

    static void LEDoff() {
      try
      {
        byte bmRequestType = UsbConst.REQUESTTYPE_DIRECTION_OUT | UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
        byte bRequest = 1;
        short wValue = 0;
        short wIndex = 0;
        UsbControlIrp usbctrlreq = usbdev.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);        
        usbdev.syncSubmit(usbctrlreq);
          
          
      } catch (Exception e) {}            
    }

    
}


 
 
class TraverseUSB
{
        public static void main()
        {
          try
          {
              // Access the system USB services, and access to the root 
              // hub. Then traverse through the root hub.
              UsbServices services = UsbHostManager.getUsbServices();
              UsbHub rootHub = services.getRootUsbHub();
              traverse(rootHub);
          } catch (Exception e) {}
        }
 
        public static UsbDevice traverse(UsbDevice device)
        {
          if (device.isUsbHub())
          {   
             // This is a USB Hub, traverse through the hub.
             List attachedDevices = 
                 ((UsbHub) device).getAttachedUsbDevices();
             for (int i=0; i<attachedDevices.size(); i++)
             {
               traverse((UsbDevice) attachedDevices.get(i));
             }
          }
          else
          {
             // This is a USB function, not a hub.
             // Do something.
              try
              {
                if(device.getProductString().equals("rainflash")) {
                    //System.out.println("rainflash found...");
                    LEDCtrl.usbdev = device;
                }
              } catch (Exception e) {}              

          }
          return null;
        }
}