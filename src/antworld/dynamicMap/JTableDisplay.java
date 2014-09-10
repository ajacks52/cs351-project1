package antworld.dynamicmap;

// Imports
import java.awt.*;
import java.util.Iterator;
import javax.swing.*;

import antworld.data.AntData;
import antworld.data.CommData;
import antworld.data.FoodData;


public class JTableDisplay extends JFrame
{
  private JPanel topPanel;
  private static JTable table;
  private JScrollPane scrollPane;
  // Create columns names
  String columnNames[] = { "Ants", "Ant Location", "Food", "Food Location", "Enemey Ants Location" };
  static// Create some data
  String dataValues[][] = new String[100][5];

  public JTableDisplay()
  {
    setTitle("Table");
    setSize(400, 300);
    this.setLocation(0, 650);
    setBackground(Color.gray);
    topPanel = new JPanel();
    topPanel.setLayout(new BorderLayout());
    getContentPane().add(topPanel);
    table = new JTable(dataValues, columnNames);
    scrollPane = new JScrollPane(table);
    topPanel.add(scrollPane, BorderLayout.CENTER);
    this.setVisible(true);
  }

  public static void updateTable(CommData Cdata)
  {
    FoodData[] foodset = new FoodData[Cdata.foodSet.size()];
    StringBuilder sb = new StringBuilder();
    
    for (int i = 0; i < Cdata.myAntList.size(); i++)
    {
      sb = new StringBuilder();
      sb.append(Cdata.myAntList.get(i).id);
      dataValues[i++][0] = sb.toString();
    }
    for (int i = 0; i < Cdata.myAntList.size(); i++)
    {
      sb = new StringBuilder();
      sb.append(Cdata.myAntList.get(1).gridX);
      sb.append(",");
      sb.append(Cdata.myAntList.get(1).gridY);
      dataValues[i++][1] = sb.toString();
    }
  
    for (Iterator<FoodData> i = Cdata.foodSet.iterator(); i.hasNext();)
    {
      int j = 0;
      FoodData element = i.next();
      sb = new StringBuilder();
      sb.append(element.gridX);
      sb.append(",");
      sb.append(element.gridY);
      dataValues[j++][3] = sb.toString();
      sb = new StringBuilder();
      sb.append(element.foodType.toString());
      dataValues[j++][2] = sb.toString();
    }
    for (Iterator<AntData> i = Cdata.enemyAntSet.iterator(); i.hasNext();)
    {
      int j = 0;
      AntData element = i.next();
      sb = new StringBuilder();
      sb.append(element.gridX);
      sb.append(",");
      sb.append(element.gridY);
      dataValues[j++][4] = sb.toString();
    }
    table.repaint();
  }

}