package Display;

import Grapher.Function;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Arrays;

/**
 * This class contains all the members needed to actually draw the graph onto the screen.
 */
public class Graph extends JPanel
{
  // region Data fields
  /**
   * The width of the display panel in pixels
   */
  private final int PANEL_WIDTH;
  /**
   * The height of the display panel in pixels
   */
  private final int PANEL_HEIGHT;
  /**
   * The maximum x-coordinate on the graph
   */
  private final double GRAPH_X_MAX;
  /**
   * The minimum x-coordinate on the graph
   */
  private final double GRAPH_X_MIN;
  /**
   * The maximum y-coordinate on the graph
   */
  private final double GRAPH_Y_MAX;
  /**
   * The minimum y-coordinate on the graph
   */
  private final double GRAPH_Y_MIN;

  /**
   * The equations to plot on this panel
   */
  private String[] functions;

  /**
   * The colors to use when plotting
   */
  private final Color[] colors = {
      Color.red,
      Color.blue,
      Color.green,
      Color.orange,
      Color.magenta,
      Color.pink,
      Color.cyan,
      Color.gray
  };
  // endregion

  /**
   * Creates a panel with the given panelHeight and panelWidth, as well as the parameters of the graph
   * to be displayed
   *
   * @param panelWidth The panelWidth of the display panel, in pixels
   * @param panelHeight The panelHeight of the display panel, in pixels
   * @param xMin The minimum x-value in the graph window
   * @param xMax The maximum x-value in the graph window
   * @param yMin The minimum y-value in the graph window
   * @param yMax The maximum y-value in the graph window
   */
  public Graph(int panelWidth, int panelHeight, double xMin, double xMax, double yMin, double yMax)
  {
    this.PANEL_WIDTH = panelWidth;
    this.PANEL_HEIGHT = panelHeight;

    this.GRAPH_X_MAX = xMax;
    this.GRAPH_X_MIN = xMin;
    this.GRAPH_Y_MAX = yMax;
    this.GRAPH_Y_MIN = yMin;

    this.functions = null; // Will be set automatically when user enters a function

    // Scales panel according to the provided arguments
    Dimension size = new Dimension(panelWidth, panelHeight);
    this.setPreferredSize(size);
  }



  /**
   * Draws the graph (with axes) to the window
   *
   * @param g the {@code Graphics} context in which to paint
   */
  @Override
  public void paint(Graphics g)
  {
    super.paint(g);

    // Fill background with white
    g.setColor(Color.white);
    g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

    if (this.functions != null)
    {
      for (int i = 0; i < this.functions.length; i++)
      {
        // Color of function curve
        g.setColor(colors[i % colors.length]);

        // Draw curve
        for (int panelX0 = 0; panelX0 < PANEL_WIDTH; panelX0++)
        {
          // Left and right bounds of the pixel on the panel
          int panelX1 = 1 + panelX0;

          // Left and right bounds of the pixel in the graph
          double graphX0 = panelToGraphX(panelX0);
          double graphX1 = panelToGraphX(panelX1);

          // Values of the function at the x bounds
          double[] xBounds = new double[]{graphX0, graphX1};
          double[] functionBounds = Function.evaluateBounds(xBounds, functions[i]);

          for (int panelY0 = 0; panelY0 < PANEL_HEIGHT; panelY0++)
          {
            // Upper and lower bounds of the pixel on the panel
            int panelY1 = 1 + panelY0;

            // Upper and lower bounds of the pixel in the graph
            double graphY0 = panelToGraphY(panelY0);
            double graphY1 = panelToGraphY(panelY1);

            // Fill in rectangle if the pixel range contains the curve
            double[] yBounds = new double[]{graphY0, graphY1};
            if (isOverlapping(yBounds, functionBounds))
            {
              g.fillRect(panelX0, panelY0, 1, 1);
            }
          }
        }
      }
    }

    int yAxisPanelCoord = graphToPanelX(0.0);
    int xAxisPanelCoord = graphToPanelY(0.0);

    // Draw x and y axes
    g.setColor(Color.black);
    g.drawLine(yAxisPanelCoord, 0, yAxisPanelCoord, PANEL_HEIGHT);
    g.drawLine(0, xAxisPanelCoord, PANEL_WIDTH, xAxisPanelCoord);
  }

  /**
   * A private helper method to determine if two ranges of numbers overlap
   *
   * @param range1 The 2 endpoints of the first range
   * @param range2 The 2 endpoints of the second range
   * @return true if the ranges overlap
   */
  private boolean isOverlapping(double[] range1, double[] range2)
  {
    Arrays.sort(range1);
    Arrays.sort(range2);

    return range1[0] <= range2[1] && range2[0] <= range1[1];
  }

  /**
   * A method to change the functions displayed on this graph
   *
   * @param newFunctions The new functions to display
   */
  public void setFunctions(String newFunctions)
  {
    this.functions = newFunctions.split(",");
  }

  /**
   * A private helper method to convert from panel (display) x-coordinates to graph x-coordinates
   *
   * @param panelX x coordinate in the panel
   * @return x coordinate in the graph
   */
  private double panelToGraphX(int panelX)
  {
    return GRAPH_X_MIN + panelX * (GRAPH_X_MAX - GRAPH_X_MIN) / this.PANEL_WIDTH;
  }

  /**
   * A private helper method to convert from panel (display) y-coordinates to graph y-coordinates
   *
   * @param panelY y coordinate in the panel
   * @return y coordinate in the graph
   */
  private double panelToGraphY(int panelY)
  {
    return GRAPH_Y_MAX - panelY * (GRAPH_Y_MAX - GRAPH_Y_MIN) / this.PANEL_HEIGHT;
  }

  /**
   * A private helper method to convert from graph x-coordinates to panel (display) x-coordinates
   *
   * @param graphX x coordinate in the graph
   * @return x coordinate in the panel
   */
  private int graphToPanelX(double graphX)
  {
    return (int) ((graphX - GRAPH_X_MIN) * this.PANEL_WIDTH / (GRAPH_X_MAX - GRAPH_X_MIN));
  }

  /**
   * A private helper method to convert from graph y-coordinates to panel (display) y-coordinates
   *
   * @param graphY y coordinate in the graph
   * @return y coordinate in the panel
   */
  private int graphToPanelY(double graphY)
  {
    return (int) -((graphY - GRAPH_Y_MAX) * this.PANEL_HEIGHT / (GRAPH_Y_MAX - GRAPH_Y_MIN));
  }
}
