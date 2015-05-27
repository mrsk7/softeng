package gov.nist.sip.proxy.gui;
import javax.swing.JOptionPane;

public class PopupWindow {

	public PopupWindow( String text, String title) {
		infoBox(text,title);		
	}
	
	public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}
