package antworld.dynamicmap;

// Imports
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

import antworld.data.AntData;
import antworld.data.CommData;

public class JTableDisplay
		extends 	JFrame
 {
	// Instance attributes used in this example
	private	JPanel		topPanel;
	private static	JTable		table;
	private	JScrollPane scrollPane;

	
	// Create columns names
	String columnNames[] = { "Ants", "Ant Location", "Food", "Food Location", "Enemey Ants Location" };

	static // Create some data
	String dataValues[][] = new String[100][5];

	
	// Constructor of main frame
	public JTableDisplay()
	{
		// Set the frame characteristics
		setTitle( "Table" );
		setSize( 400, 300 );
		this.setLocation(0, 650);
		setBackground( Color.gray );

		// Create a panel to hold all other components
		topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );

		

		// Create a new table instance
		table = new JTable( dataValues, columnNames );

		// Add the table to a scrolling pane
		scrollPane = new JScrollPane( table );
		topPanel.add( scrollPane, BorderLayout.CENTER );
		
//		JScrollPane scrollPane = new JScrollPane(table);
//        add(scrollPane);
		
		this.setVisible( true );
	}
	
	public static void updateTable(CommData Cdata)
	{
		Object[] foodset = Cdata.foodSet.toArray();
		
		for(int i = 0; i < Cdata.myAntList.size(); i++){
			dataValues[i++][0] = Cdata.myAntList.get(i).toString();
		}

		for(int i = 0; i < Cdata.myAntList.size(); i++){
			StringBuilder sb = new StringBuilder();
			sb.append(Cdata.myAntList.get(1).gridX );
			sb.append(",");
			sb.append(Cdata.myAntList.get(1).gridY );
			
			dataValues[i++][1] = sb.toString();	 
		}
		for(int i = 0; i < foodset.length; i++){
			dataValues[i][2] = foodset[i++].toString();
		}
		
		table.repaint();
	}

	
}