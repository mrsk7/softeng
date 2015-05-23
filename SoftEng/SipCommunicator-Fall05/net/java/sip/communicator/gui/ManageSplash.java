
package net.java.sip.communicator.gui;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.sql.DriverManager;
import java.util.*;
import javax.swing.*;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import net.java.sip.communicator.common.*;
import java.sql.*;
//import net.java.sip.communicator.gui.AuthenticationSplash.FocusTraversalPol;

import jmapps.ui.MessageDialog;

public class ManageSplash extends JDialog
{
	protected JTextField userNameTextField = null;
    protected JButton blockingButton = null;
    protected JButton changeButton = null;
    protected JButton forwardButton = null;
    protected JComboBox policyBox = null;
    protected String policyChoice = null;
    protected JButton cancelButton = null;
    protected String[] policyList = null;
    protected String currentPolicy = null;
    protected GuiManager guiManCallback = null;
    

    
    /**
     * Command string for a cancel action (e.g., a button).
     * This string is never presented to the user and should
     * not be internationalized.
     */
    private String CMD_CANCEL = "cmd.cancel" /*NOI18N*/;
    private String CMD_CHANGE = "cmd.change";


    public ManageSplash(Frame parent, boolean modal, String[] policyList,String currentPolicy,GuiManager gui)
    {
        super(parent, modal);
        guiManCallback = gui;
      //  initResources();
        this.policyList = policyList;
        this.currentPolicy = currentPolicy;
        initComponents();
        pack();
        centerWindow();
    }
   
    private void centerWindow()
    {
        Rectangle screen = new Rectangle(
            Toolkit.getDefaultToolkit().getScreenSize());
        Point center = new Point(
            (int) screen.getCenterX(), (int) screen.getCenterY());
        Point newLocation = new Point(
            center.x - this.getWidth() / 2, center.y - this.getHeight() / 2);
        if (screen.contains(newLocation.x, newLocation.y,
                            this.getWidth(), this.getHeight())) {
            this.setLocation(newLocation);
        }
    } // centerWindow()
    
    
    private void initComponents()
    {
        Container contents = getContentPane();
        contents.setLayout(new BorderLayout());

        String title = Utils.getProperty("net.java.sip.communicator.gui.REG_WIN_TITLE");

        if(title == null)
            title = "Account Management";

        setTitle(title);
        setResizable(false);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent event)
            {
                dialogDone(CMD_CANCEL);
            }
        });

        // Accessibility -- all frames, dialogs, and applets should
        // have a description
        getAccessibleContext().setAccessibleDescription("Manage Splash");


        JPanel centerPane = new JPanel();
        centerPane.setLayout(new GridBagLayout());

        userNameTextField = new JTextField(); // needed below

        // user name label
        JLabel userNameLabel = new JLabel();
        userNameLabel.setDisplayedMnemonic('U');
        // setLabelFor() allows the mnemonic to work
        userNameLabel.setLabelFor(userNameTextField);

        String userNameLabelValue = "User name*";

        int gridy = 0;

        userNameLabel.setText(userNameLabelValue);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=gridy;
        c.anchor=GridBagConstraints.WEST;
        c.insets=new Insets(12,12,0,0);
        centerPane.add(userNameLabel, c);

        // user name text
        c = new GridBagConstraints();
        c.gridx=1;
        c.gridy=gridy++;
        c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx=1.0;
        c.insets=new Insets(12,7,0,11);
        centerPane.add(userNameTextField, c);

        //username example
        if(GuiManager.isThisSipphoneAnywhere)
        {

            String egValue = Utils.getProperty("net.java.sip.communicator.sipphone.USER_NAME_EXAMPLE");

            if(egValue == null)
                egValue = "Example: 1-747-555-1212";

            JLabel userNameExampleLabel = new JLabel();

            userNameExampleLabel.setText(egValue);
            c = new GridBagConstraints();
            c.gridx=0;
            c.gridy=gridy++;
            c.anchor=GridBagConstraints.WEST;
            c.fill=GridBagConstraints.HORIZONTAL;
            c.insets=new Insets(12,12,0,0);
            centerPane.add(userNameExampleLabel, c);

        }
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        blockingButton = new JButton();
        blockingButton.setText("Block");
        //blockingButton.setActionCommand(CMD_BLOCK);
        blockingButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
            	if (userNameTextField.getText().equals("")){
   				 JOptionPane.showMessageDialog(null, "You must enter a user name");
            		System.out.println("You must enter a user name");
            	
            	}
            	
            	else  {
            		guiManCallback.blockingButton_actionPerformed(event);
            	}
       
            	
            }
        });
        buttonPanel.add(blockingButton);

        // space
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        
        forwardButton = new JButton();
        forwardButton.setText("Forward");
       // forwardButton.setActionCommand(CMD_FORWARD);
        forwardButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
            	if (userNameTextField.getText().equals("")){
   				 JOptionPane.showMessageDialog(null, "You must enter a user name");
            		System.out.println("You must enter a user name");
            	
            	}
            	else  {
            		guiManCallback.forwardButton_actionPerformed(event);
            	}
       
            	
            }
        });
        buttonPanel.add(forwardButton);

        // space
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        
        //OURS
        //Added policies option button
        policyBox = new JComboBox(policyList);
        policyBox.setSelectedItem(currentPolicy);
        policyBox.addActionListener(null);
        JLabel policyLabel = new JLabel();
        policyLabel.setText("Policy*");
        policyLabel.setLabelFor(policyBox);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(11, 12, 0, 0);
        centerPane.add(policyLabel, c);
        
        c = new GridBagConstraints();
        c.gridx=1;
        c.gridy=gridy++;
        c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx=1.0;
        c.insets=new Insets(12,7,0,11);
        centerPane.add(policyBox,c);

        
        
        changeButton = new JButton();
        changeButton.setText("Change Policy");
        changeButton.setActionCommand(CMD_CHANGE);
        changeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
            		dialogDone(event);
            }
        });
        buttonPanel.add(changeButton);

        // space
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        //Set a relevant realm value
        //Bug report by Steven Lass (sltemp at comcast.net)
        //JLabel realmValueLabel = new JLabel("SipPhone.com"); // needed below

        // realm label


        // Buttons along bottom of window
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        cancelButton = new JButton();
        cancelButton.setText("Done");
        cancelButton.setActionCommand(CMD_CANCEL);
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                dialogDone(event);
            }
        });
        buttonPanel.add(cancelButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.insets = new Insets(11, 12, 11, 11);

        centerPane.add(buttonPanel, c);

        contents.add(centerPane, BorderLayout.WEST);
        getRootPane().setDefaultButton(cancelButton);
        equalizeButtonSizes();


    } // initComponents()
    
    
    private void equalizeButtonSizes()
    {

        JButton[] buttons = new JButton[] {
            blockingButton, forwardButton, changeButton, cancelButton
        };

        String[] labels = new String[buttons.length];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = buttons[i].getText();
        }

        // Get the largest width and height
        int i = 0;
        Dimension maxSize = new Dimension(0, 0);
        Rectangle2D textBounds = null;
        Dimension textSize = null;
        FontMetrics metrics = buttons[0].getFontMetrics(buttons[0].getFont());
        Graphics g = getGraphics();
        for (i = 0; i < labels.length; ++i) {
            textBounds = metrics.getStringBounds(labels[i], g);
            maxSize.width =
                Math.max(maxSize.width, (int) textBounds.getWidth());
            maxSize.height =
                Math.max(maxSize.height, (int) textBounds.getHeight());
        }

        Insets insets =
            buttons[0].getBorder().getBorderInsets(buttons[0]);
        maxSize.width += insets.left + insets.right;
        maxSize.height += insets.top + insets.bottom;

        // reset preferred and maximum size since BoxLayout takes both
        // into account
        for (i = 0; i < buttons.length; ++i) {
            buttons[i].setPreferredSize( (Dimension) maxSize.clone());
            buttons[i].setMaximumSize( (Dimension) maxSize.clone());
        }
    } // equalizeButtonSizes()

    /**
     * The user has selected an option. Here we close and dispose the dialog.
     * If actionCommand is an ActionEvent, getCommandString() is called,
     * otherwise toString() is used to get the action command.
     *
     * @param actionCommand may be null
     */
    private void dialogDone(Object actionCommand)
    {
        String cmd = null;
        if (actionCommand != null) {
            if (actionCommand instanceof ActionEvent) {
                cmd = ( (ActionEvent) actionCommand).getActionCommand();
            }
            else {
                cmd = actionCommand.toString();
            }
        }
        if (cmd == null) {
            // do nothing
        }
        else if (cmd.equals(CMD_CANCEL)) {
            setVisible(false);
        }
        else if (cmd.equals(CMD_CHANGE)) {
        	policyChoice = (String) policyBox.getSelectedItem();
        	if (currentPolicy.equals(policyChoice)) {
        		PopupWindow x = new PopupWindow("You didn't change your billing policy. Please select other policy","Same policy");
        	}
        	else {
        		
        	}
        }
        
        //dispose();
    } // dialogDone()
    
    

} // class ManageSplash
    

