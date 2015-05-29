

package net.java.sip.communicator.gui;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import net.java.sip.communicator.common.*;

//import samples.accessory.StringGridBagLayout;

/**
 * Sample login splash screen
 */
public class AuthenticationSplash
    extends JDialog
{
    String userName = null;
    String firstName = null;
    String lastName = null;
    char[] password = null;
    String policy = null;
    JTextField userNameTextField = null;
    JLabel     realmValueLabel = null;
    JPasswordField passwordTextField = null;
    int flag = -1;

    /**
     * Resource bundle with default locale
     */
    private ResourceBundle resources = null;

    /**
     * Path to the image resources
     */
    private String imagePath = null;

    /**
     * Command string for a cancel action (e.g., a button).
     * This string is never presented to the user and should
     * not be internationalized.
     */
    private String CMD_CANCEL = "cmd.cancel" /*NOI18N*/;

    /**
     * Command string for a help action (e.g., a button).
     * This string is never presented to the user and should
     * not be internationalized.
     */
    private String CMD_HELP = "cmd.help" /*NOI18N*/;

    /**
     * Command string for a login action (e.g., a button).
     * This string is never presented to the user and should
     * not be internationalized.
     */
    private String CMD_LOGIN = "cmd.login" /*NOI18N*/;
    private String CMD_SIGNUP = "cmd.signup";

    // Components we need to manipulate after creation
    private JButton loginButton = null;
    private JButton signupButton = null;
    private JButton cancelButton = null;
    private JButton helpButton = null;
    private Frame myparent = null;
    private RegistrationSplash reg = null;
    private String[] availablePolicies = null;

    /**
     * Creates new form AuthenticationSplash
     */
    public AuthenticationSplash(Frame parent, boolean modal,String[] availablePolicies)
    {
        super(parent, modal);
        this.availablePolicies= availablePolicies;
        initResources();
        initComponents();
        pack();
        centerWindow();
        myparent=parent;
    }

    /**
     * Loads locale-specific resources: strings, images, et cetera
     */
    private void initResources()
    {
        Locale locale = Locale.getDefault();
        imagePath = ".";
    }

    /**
     * Centers the window on the screen.
     */
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

    /**
     *
     * We use dynamic layout managers, so that layout is dynamic and will
     * adapt properly to user-customized fonts and localized text. The
     * GridBagLayout makes it easy to line up components of varying
     * sizes along invisible vertical and horizontal grid lines. It
     * is important to sketch the layout of the interface and decide
     * on the grid before writing the layout code.
     *
     * Here we actually use
     * our own subclass of GridBagLayout called StringGridBagLayout,
     * which allows us to use strings to specify constraints, rather
     * than having to create GridBagConstraints objects manually.
     *
     *
     * We use the JLabel.setLabelFor() method to connect
     * labels to what they are labeling. This allows mnemonics to work
     * and assistive to technologies used by persons with disabilities
     * to provide much more useful information to the user.
     */
    private void initComponents()
    {
        Container contents = getContentPane();
        contents.setLayout(new BorderLayout());

        String title = Utils.getProperty("net.java.sip.communicator.gui.AUTH_WIN_TITLE");

        if(title == null)
            title = "Registrar Login";

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

        String authPromptLabelValue = Utils.getProperty("net.java.sip.communicator.gui.AUTHENTICATION_PROMPT");

        if(authPromptLabelValue  == null)
            authPromptLabelValue  = "Please enter user name and password to use when registering!";

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

        String userNameLabelValue = Utils.getProperty("net.java.sip.communicator.gui.USER_NAME_LABEL");

        if(userNameLabelValue == null)
            userNameLabelValue = "User name";

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
        passwordLabel.setText(pLabelStr==null?pLabelStr: "Password");
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

        //Set a relevant realm value
        //Bug report by Steven Lass (sltemp at comcast.net)
        //JLabel realmValueLabel = new JLabel("SipPhone.com"); // needed below


        // realm label

        JLabel realmLabel = new JLabel();
        realmLabel.setDisplayedMnemonic('R');
        realmLabel.setLabelFor(realmValueLabel);
        realmLabel.setText("Realm");
        realmValueLabel = new JLabel();

        if (!GuiManager.isThisSipphoneAnywhere) {
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = gridy;
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(11, 12, 0, 0);
            centerPane.add(realmLabel, c);
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = gridy++;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(11, 7, 0, 11);
            centerPane.add(realmValueLabel, c);
        }

        // Buttons along bottom of window
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        loginButton = new JButton();
        loginButton.setText("Login");
        loginButton.setActionCommand(CMD_LOGIN);
        loginButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                dialogDone(event);
            }
        });
        buttonPanel.add(loginButton);

        // space
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        
        JPanel signupPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        signupButton = new JButton();
        signupButton.setText("Sign up");
        signupButton.setActionCommand(CMD_SIGNUP);
        signupButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                dialogDone(event);
            }
        });
        buttonPanel.add(signupButton); 
        		
        		
        		
        		

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

        helpButton = new JButton();
        helpButton.setMnemonic('H');
        helpButton.setText("Help");
        helpButton.setActionCommand(CMD_HELP);
        helpButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                dialogDone(event);
            }
        });
        //buttonPanel.add(helpButton);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.insets = new Insets(11, 12, 11, 11);

        centerPane.add(buttonPanel, c);

        contents.add(centerPane, BorderLayout.CENTER);
        getRootPane().setDefaultButton(loginButton);
        equalizeButtonSizes();

        setFocusTraversalPolicy(new FocusTraversalPol());

    } // initComponents()

    /**
     * Sets the buttons along the bottom of the dialog to be the
     * same size. This is done dynamically by setting each button's
     * preferred and maximum sizes after the buttons are created.
     * This way, the layout automatically adjusts to the locale-
     * specific strings.
     */
    private void equalizeButtonSizes()
    {

        JButton[] buttons = new JButton[] {
            loginButton, cancelButton
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
        else if (cmd.equals(CMD_HELP)) {
            System.out.println("your help code here...");
        }
        else if (cmd.equals(CMD_LOGIN)) {
        	flag = 0;
            userName = userNameTextField.getText();
            password = passwordTextField.getPassword();
        }
        //OURS
        else if (cmd.equals(CMD_SIGNUP)) {
        	flag = 1;
        	reg = new RegistrationSplash(myparent,true,availablePolicies);
        	reg.setVisible(true);
        	userName= reg.userName;
        	password = reg.password;
        	firstName = reg.firstName;
        	lastName = reg.lastName;
        	policy = reg.policyChoice;
        }
        setVisible(false);
        dispose();
    } // dialogDone()

    /**
     * This main() is provided for debugging purposes, to display a
     * sample dialog.
     */
    public static void main(String args[])
    {
        JFrame frame = new JFrame()
        {
            public Dimension getPreferredSize()
            {
                return new Dimension(200, 100);
            }
        };
        frame.setTitle("Debugging frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(false);
        String[] policies = {};
        AuthenticationSplash dialog = new AuthenticationSplash(frame, true,policies);
        dialog.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent event)
            {
                System.exit(0);
            }

            public void windowClosed(WindowEvent event)
            {
                System.exit(0);
            }
        });
        dialog.pack();
        dialog.setVisible(true);
    } // main()

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
} // class LoginSplash
