package Grapher;

import Display.Graph;
import Display.Window;

/**
 * This class contains the main file of the program and is used to execute it
 */
public class Main
{
  public static void main(String[] args)
  {
    Window myWindow = new Window();
    myWindow.setVisible(true);

    // Create a new panel with an empty graph
    Graph myGraph = new Graph(512, 512, -10, 10, -10, 10);
    myWindow.add(myGraph);
    myWindow.pack();

    // Allow user to type new functions
    myWindow.functionField.addActionListener(e -> {
      myGraph.setFunctions(myWindow.functionField.getText());
      myGraph.repaint();
    });
  }
}
