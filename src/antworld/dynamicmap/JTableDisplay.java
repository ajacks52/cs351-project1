/**
 * @author Adam Mitchell
 * 
 * The JTableDisplay class creates a JTable that shows and updates the 
 * location of food and enemy ants once they've been spotted by an ant. 
 * Provides the coordinates of each active ant as well.
 */
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
  String columnNames[] =
  { "Ants", "Ant Location", "Food", "Food Location", "Enemey Ants Location" };
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

  /**
   * 
   * 
   * @param Cdata
   */
  public static void updateTableAnts(CommData Cdata)
  {
    StringBuilder sb = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();
    for (int i = 0; i < Cdata.myAntList.size(); i++)
    {
      sb = new StringBuilder();
      sb.append(Cdata.myAntList.get(i).id);
      dataValues[i][0] = sb.toString();

      sb2 = new StringBuilder();
      sb2.append(Cdata.myAntList.get(1).gridX);
      sb2.append(",");
      sb2.append(Cdata.myAntList.get(1).gridY);
      dataValues[i][1] = sb2.toString();
    }
    table.repaint();
  }

  /**
   * 
   * @param Cdata
   */
  public static void updateTableFood(CommData Cdata)
  {

    FoodData[] foodset = new FoodData[Cdata.foodSet.size()];
    StringBuilder sb = new StringBuilder();

    for (Iterator<FoodData> i = Cdata.foodSet.iterator(); i.hasNext();)
    {
      int j = 0;
      FoodData element = i.next();
      sb = new StringBuilder();
      sb.append(element.gridX);
      sb.append(",");
      sb.append(element.gridY);
      dataValues[j][3] = sb.toString();
      sb = new StringBuilder();
      sb.append(element.foodType.toString());
      dataValues[j++][2] = sb.toString();
    }

    table.repaint();
  }

  /**
   * 
   * @param Cdata
   */
  public static void updateTableEnemy(CommData Cdata)
  {
    StringBuilder sb = new StringBuilder();
    int j = 0;
    for (Iterator<AntData> i = Cdata.enemyAntSet.iterator(); i.hasNext();)
    {
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