/*       
	œÃ¡ƒ¡ 21
¡÷≈Õ‘œ’À…ƒ«” √—«√œ—«” 03107760
 œÀœ‘œ’—œ” ƒ«Ã«‘—«” 03107116
–¡—¡” ≈’œ–œ’Àœ” √…Ÿ—√œ” 03109030
*/
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

import jmapps.ui.MessageDialog;

public class RegistrationSplash extends JDialog
{
	String userName = null;
	String firstName = null;
	String lastName = null;
    char[] password = null;
    JTextField firstNameTextField = null;
    JTextField lastNameTextField = null;
    JTextField userNameTextField = null;
    JPasswordField passwordTextField = null;
    JComboBox policylist = null;
    String policyChoice = null;
    String[] policies = null;


    private JButton registerButton = null;
    private JButton cancelButton = null;
    
    /**
     * Command string for a cancel action (e.g., a button).
     * This string is never presented to the user and should
     * not be internationalized.
     */
    private String CMD_CANCEL = "cmd.cancel" /*NOI18N*/;
    private String CMD_REGISTER = "cmd.register";

    public RegistrationSplash(Frame parent, boolean modal,String[] availablePolicies)
    {
        super(parent, modal);
        this.policies=availablePolicies;
      //  initResources();
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
            title = "Registration";

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
        getAccessibleContext().setAccessibleDescription("Authentication Splash");

        String authPromptLabelValue  = "Please fill all fields with an asterisk!!";

        JLabel splashLabel = new JLabel(authPromptLabelValue );
        splashLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
        splashLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        contents.add(splashLabel, BorderLayout.NORTH);

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

        passwordTextField = new JPasswordField(); //needed below

        // password label
        JLabel passwordLabel = new JLabel();
        passwordLabel.setDisplayedMnemonic('P');
        passwordLabel.setLabelFor(passwordTextField);
        String pLabelStr = PropertiesDepot.getProperty("net.java.sip.communicator.gui.PASSWORD_LABEL");
        passwordLabel.setText(pLabelStr==null?pLabelStr: "Password*");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(11, 12, 0, 0);

        centerPane.add(
            passwordLabel, c);

        // password text
        passwordTextField.setEchoChar('\u2022');
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(11, 7, 0, 11);
        centerPane.add(passwordTextField, c);
        
        //OURS
        //Added policies option button
        if (policies.length == 0)
        	policylist=new JComboBox<>();
        else 
        	policylist = new JComboBox(policies);
        policylist.setSelectedIndex(0);
        policylist.addActionListener(null);
        JLabel policyLabel = new JLabel();
        policyLabel.setText("Policy*");
        policyLabel.setLabelFor(policylist);
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
        centerPane.add(policylist,c);

        
        firstNameTextField = new JTextField();
        JLabel firstNameLabel = new JLabel();
        firstNameLabel.setLabelFor(firstNameTextField);
        firstNameLabel.setText("First Name");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(11, 12, 0, 0);

        centerPane.add(
            firstNameLabel, c);
        
        c = new GridBagConstraints();
        c.gridx=1;
        c.gridy=gridy++;
        c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx=1.0;
        c.insets=new Insets(12,7,0,11);
        centerPane.add(firstNameTextField,c);
        
        lastNameTextField = new JTextField();
        JLabel lastNameLabel = new JLabel();
        lastNameLabel.setLabelFor(lastNameTextField);
        lastNameLabel.setText("Last Name");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(11, 12, 0, 0);

        centerPane.add(
            lastNameLabel, c);
        
        c = new GridBagConstraints();
        c.gridx=1;
        c.gridy=gridy++;
        c.fill=GridBagConstraints.HORIZONTAL;
        c.weightx=1.0;
        c.insets=new Insets(12,7,0,11);
        centerPane.add(lastNameTextField,c);
        
        
        //Set a relevant realm value
        //Bug report by Steven Lass (sltemp at comcast.net)
        //JLabel realmValueLabel = new JLabel("SipPhone.com"); // needed below

        // realm label


        // Buttons along bottom of window
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        registerButton = new JButton();
        registerButton.setText("Register");
        registerButton.setActionCommand(CMD_REGISTER);
        registerButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
            	if (userNameTextField.getText().equals("")){
   				 JOptionPane.showMessageDialog(null, "You must enter a user name");
            		System.out.println("You must enter a user name");
            	
            	}
            	else if 
            	 (passwordTextField.getText().equals("")){
      				JOptionPane.showMessageDialog(null, "You must enter a password");
            		System.out.println("You must enter a password");
            	}
            	else  {
            		dialogDone(event);
            	}
       
            	
            }
        });
        buttonPanel.add(registerButton);

        // space
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        cancelButton = new JButton();
        cancelButton.setText("Cancel");
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
        getRootPane().setDefaultButton(registerButton);
        equalizeButtonSizes();

        setFocusTraversalPolicy(new FocusTraversalPol());

    } // initComponents()
    
    
    private void equalizeButtonSizes()
    {

        JButton[] buttons = new JButton[] {
            registerButton, cancelButton
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
            userName = null;
            password = null;
        }
        else if (cmd.equals(CMD_REGISTER)) {
        	try
            {
//        		ConnectionObject co = new ConnectionObject();
//        		UserObject user = new UserObject(co);
//        		if (user.addUser(
//        				userNameTextField.getText(),
//        				passwordTextField.getText() ,
//        				firstNameTextField.getText(),
//        				lastNameTextField.getText(), 
//						"policy1") != 0){
//        				 JOptionPane.showMessageDialog(null, "Error!\nCannot Register\nTry Again With Different Username and password");
//        		
//       		}
//        		co.disconnect();
        		userName = userNameTextField.getText();
        		password = passwordTextField.getPassword();
        		firstName = firstNameTextField.getText();
        		lastName = lastNameTextField.getText();
        		policyChoice = (String) policylist.getSelectedItem(); 
        		System.out.println("Policy is " + policyChoice);

            }
            catch (Exception e)
            {
                System.err.println ("Cannot connect to database server");
            }
        }
        
        setVisible(false);
        //dispose();
    } // dialogDone()

    private class FocusTraversalPol extends LayoutFocusTraversalPolicy
    {
        public Component getDefaultComponent(Container cont)
        {
            if(  userNameTextField.getText() == null
               ||userNameTextField.getText().trim().length() == 0)
                return super.getFirstComponent(cont);
            else
                return passwordTextField;
        }
    }
} // class RegistrationSplash
    

