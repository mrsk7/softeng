package gov.nist.sip.proxy.gui;


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
import java.sql.*;

public class PolicySplash extends JDialog {
	protected JTextField name = null;
	protected JTextField starting = null;
	protected JTextField costPer=null;
	protected JButton addButton = null;
	protected JButton deleteButton = null;
	protected JComboBox policyBox = null;
	protected String policyChoice = null;
	protected JButton cancelButton = null;
	protected String[] policyList = null;

	protected ListenerProxy listenerProxy = null;

	/**
	 * Command string for a cancel action (e.g., a button).
	 * This string is never presented to the user and should
	 * not be internationalized.
	 */
	private String CMD_CANCEL = "cmd.cancel" /*NOI18N*/;
	private GridBagConstraints c_1;


	public PolicySplash(Frame parent, boolean modal, ListenerProxy listenerProxy, String[] policyList)
	{
		super(parent, modal);
		this.listenerProxy = listenerProxy;
		//  initResources();
		this.policyList = policyList;
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

		String title = "Manage policies";

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

		//Name of policy
		name = new JTextField();
		JLabel nameLabel = new JLabel();
		nameLabel.setDisplayedMnemonic('N');
		nameLabel.setLabelFor(name);

		String nameLabelValue = "Name of new policy*";
		
		int gridy = 0;

		nameLabel.setText(nameLabelValue);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=gridy;
		c.anchor=GridBagConstraints.WEST;
		c.insets=new Insets(12, 12, 5, 5);
		centerPane.add(nameLabel, c);

		// Starting cost text
		c = new GridBagConstraints();
		c.gridx=1;
		c.gridy=gridy++;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=1.0;
		c.insets=new Insets(12, 7, 5, 11);
		centerPane.add(name, c);
		
		
		//Starting cost
		starting = new JTextField(); // needed below
		// Starting cost label
		JLabel startingLabel = new JLabel();
		startingLabel.setDisplayedMnemonic('S');
		startingLabel.setLabelFor(starting);

		String startingLabelValue = "Starting cost*";
		startingLabel.setText(startingLabelValue);
		c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=gridy;
		c.anchor=GridBagConstraints.WEST;
		c.insets=new Insets(12, 12, 5, 5);
		centerPane.add(startingLabel, c);

		// Starting cost text
		c = new GridBagConstraints();
		c.gridx=1;
		c.gridy=gridy++;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=1.0;
		c.insets=new Insets(12, 7, 5, 11);
		centerPane.add(starting, c);
		
		//Cost per second
		costPer = new JTextField(); // needed below

		// Starting cost label
		JLabel costPerLabel = new JLabel();
		costPerLabel.setDisplayedMnemonic('C');
		costPerLabel.setLabelFor(starting);

		String costPerLabelValue = "Cost per second*";

		costPerLabel.setText(costPerLabelValue);
		c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=gridy;
		c.anchor=GridBagConstraints.WEST;
		c.insets=new Insets(12, 12, 5, 5);
		centerPane.add(costPerLabel, c);

		// Starting cost text
		c = new GridBagConstraints();
		c.gridx=1;
		c.gridy=gridy++;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=1.0;
		c.insets=new Insets(12, 7, 5, 11);
		centerPane.add(costPer, c);
	

		policyBox = new JComboBox(policyList);
		policyBox.addActionListener(null);
		policyBox.setSelectedItem(null);
		JLabel policyLabel = new JLabel();
		policyLabel.setText("Policy choice");
		policyLabel.setLabelFor(policyBox);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = gridy;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(11, 12, 5, 5);
		centerPane.add(policyLabel, c);

		c = new GridBagConstraints();
		c.gridx=1;
		c.gridy=gridy++;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=1.0;
		c.insets=new Insets(12, 7, 5, 11);
		centerPane.add(policyBox,c);
	
		

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
		addButton = new JButton();
		addButton.setText("Add");
		addButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if (starting.getText().equals("") || costPer.getText().equals(""))
					JOptionPane.showMessageDialog(null, "You must enter a starting cost"
							+ " and a cost per seconds");

				else  {
					listenerProxy.addButton_actionPerformed(event);
				}
			}
		});
		
		buttonPanel.add(addButton);

		// space
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));


		deleteButton = new JButton();
		deleteButton.setText("Delete");
		// forwardButton.setActionCommand(CMD_FORWARD);
		deleteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				policyChoice = (String) policyBox.getSelectedItem();
				if (policyChoice.equals(""))
					JOptionPane.showMessageDialog(null, "You must choose a policy "
							+ "to delete");

				else  {
					listenerProxy.deleteButton_actionPerformed(event);
				}
			}
		});
		buttonPanel.add(deleteButton);

		// space
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
		getRootPane().setDefaultButton(cancelButton);


		/////END
		contents.add(centerPane, BorderLayout.WEST);
		equalizeButtonSizes();


	} // initComponents()


	private void equalizeButtonSizes()
	{

		JButton[] buttons = new JButton[] {
				addButton, deleteButton, cancelButton
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
		System.out.println(cmd);
		if (cmd == null) {
			// do nothing
		}
		else if (cmd.equals(CMD_CANCEL)) {
			setVisible(false);
		}
		//dispose();
	} // dialogDone()




}



