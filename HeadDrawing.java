import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;

public class HeadDrawing extends JFrame {

    public HeadDrawing() {
        setTitle("Bezier Curve Head Drawing");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw head using Bezier curves
        drawHead(g2d, 100, 100, 300, 300);

        // Draw facial features (eyes, mouth, etc.) as needed
        // drawFacialFeatures(g2d, 100, 100, 300, 300);
    }

    private void drawHead(Graphics2D g, int x1, int y1, int x2, int y2) {
        int ctrlX = (x1 + x2) / 2; // Control point for x
        int ctrlY = (y1 + y2) / 2 + 50; // Control point for y (adding some vertical offset)

        // Draw quadratic Bezier curve for the head
        QuadCurve2D headCurve = new QuadCurve2D.Float(x1, y1, ctrlX, ctrlY, x2, y2);
        g.draw(headCurve);
    }

    private void drawFacialFeatures(Graphics2D g, int x1, int y1, int x2, int y2) {
        // You can add more Bezier curves to represent eyes, nose, mouth, etc.
        // Example:
        int eyeCtrlX = (x1 + x2) / 2 - 20;
        int eyeCtrlY = (y1 + y2) / 2 - 20;

        QuadCurve2D leftEye = new QuadCurve2D.Float(x1 + 40, y1 + 50, eyeCtrlX, eyeCtrlY, x1 + 60, y1 + 50);
        QuadCurve2D rightEye = new QuadCurve2D.Float(x2 - 60, y2 - 50, eyeCtrlX, eyeCtrlY, x2 - 40, y2 - 50);

        g.draw(leftEye);
        g.draw(rightEye);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HeadDrawing headDrawing = new HeadDrawing();
            headDrawing.setVisible(true);
        });
    }
}
