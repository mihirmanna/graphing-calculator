package Display;

import javax.swing.JTextField;
import javax.swing.JFrame;
import java.awt.BorderLayout;

/**
 * This class contains the window onto which the graph and input field are attached
 */
public class Window extends JFrame
{
  // region Data fields
  /**
   * Text field to input functions
   */
  public JTextField functionField = new JTextField();
  // endregion

  public Window()
  {
    this.setTitle("Graphing Calculator");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Add input field to this window
    this.add(functionField, BorderLayout.NORTH);
  }
}
