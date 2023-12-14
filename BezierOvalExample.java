import javax.swing.*;
import java.awt.*;
import java.awt.geom.CubicCurve2D;

public class BezierOvalExample extends JFrame {

    public BezierOvalExample() {
        setTitle("Bezier Oval Example");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw oval using Bezier curves
        drawBezierOval(g2d, 50, 50, 50, 70);
    }

    private void drawBezierOval(Graphics2D g, double x, double y, double width, double height) {
        double kappa = 0.5522848; // Kappa is a constant used to approximate the circular arc with cubic Bezier curves

        double ox = 0.5 * width; // Oval x-radius
        double oy = 0.5 * height; // Oval y-radius

        double ctrlX = kappa * ox; // Control point offset for x
        double ctrlY = kappa * oy; // Control point offset for y

        double x0 = x + ox;
        double y0 = y;
        double x1 = x + width;
        double y1 = y + oy;
        double x2 = x + ox;
        double y2 = y + height;
        double x3 = x;
        double y3 = y + oy;

        CubicCurve2D cubicCurve1 = new CubicCurve2D.Double(x0, y0, x0 + ctrlX, y0, x1, y1 - ctrlY, x1, y1);
        CubicCurve2D cubicCurve2 = new CubicCurve2D.Double(x1, y1, x1, y1 + ctrlY, x2 + ctrlX, y2, x2, y2);
        CubicCurve2D cubicCurve3 = new CubicCurve2D.Double(x2, y2, x2 - ctrlX, y2, x3, y3 + ctrlY, x3, y3);
        CubicCurve2D cubicCurve4 = new CubicCurve2D.Double(x3, y3, x3, y3 - ctrlY, x0 - ctrlX, y0, x0, y0);

        g.draw(cubicCurve1);
        g.draw(cubicCurve2);
        g.draw(cubicCurve3);
        g.draw(cubicCurve4);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BezierOvalExample example = new BezierOvalExample();
            example.setVisible(true);
        });
    }
}
