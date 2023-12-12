import java.awt.*;
import java.awt.geom.CubicCurve2D;

import javax.swing.*;

public class Assignment1 extends JPanel {
    final int LINE_WIDTH = 2;

    public static void main(String[] args) {
        Assignment1 n = new Assignment1();

        JFrame f = new JFrame();
        f.add(n);
        f.setTitle("NewYear");
        f.setSize(600, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setLocationRelativeTo(null); // หน้าต่างอยู่ตรงกลาง
    }

    @Override
    public void paintComponent(Graphics g) {
        myPaint(g);
    }

    private void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }

    private void drawRectangle(Graphics g, int x1, int y1, int x2, int y2, Color color) {
        g.setColor(color);
        for (int x = x1; x < x2; x++) {
            for (int y = y1; y < y2; y++) {
                plot(g, x, y);
            }
        }
    }

    private void drawSquare(Graphics g, int x, int y, int x2, int y2, int x3, int y3, int x4, int y4, Color colorO,
            Color colorI,
            int thickness) {
        if (colorO != null) {
            drawLine(g, x, y, x2, y2, colorO, thickness);// top
            drawLine(g, x2, y2, x3, y3, colorO, thickness);// right
            drawLine(g, x3, y3, x4, y4, colorO, thickness);// crease
            drawLine(g, x4, y4, x, y, colorO, thickness);// left
        }

        if (colorI != null) {
            // Fill the inner part of the rectangle
            int tHalf = thickness / 2;
            g.setColor(colorI);
            g.fillPolygon(new int[] { x + tHalf, x2 - tHalf, x4 + tHalf },
                    new int[] { y + tHalf, y2 + tHalf, y4 - tHalf }, 3);
            g.fillPolygon(new int[] { x2 - tHalf, x3 - tHalf, x4 + tHalf },
                    new int[] { y2 + tHalf, y3 - tHalf, y4 - tHalf }, 3);
        }

    }

    private void drawBezierCurve(Graphics g, int x1, int y1, int ctrlX1, int ctrlY1, int ctrlX2, int ctrlY2, int x2,
            int y2, int thickness,
            Color color) {
        g.setColor(color);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        CubicCurve2D curve = new CubicCurve2D.Float(x1, y1, ctrlX1, ctrlY1, ctrlX2, ctrlY2, x2, y2);
        g2d.draw(curve);
        // for (int i = 1; i <= thickness / 2; i++) {
        // CubicCurve2D offsetCurve1 = new CubicCurve2D.Float(
        // x1, y1 + i,
        // ctrlX1, ctrlY1 + i,
        // ctrlX2, ctrlY2 + i,
        // x2, y2 + i
        // );
        // g2d.draw(offsetCurve1);

        // CubicCurve2D offsetCurve2 = new CubicCurve2D.Float(
        // x1, y1 - i,
        // ctrlX1, ctrlY1 - i,
        // ctrlX2, ctrlY2 - i,
        // x2, y2 - i
        // );
        // g2d.draw(offsetCurve2);
        // }

    }

    private void drawLine(Graphics g, int x1, int y1, int x2, int y2, Color color, int thickness) {
        g.setColor(color);
        // g.set
        Graphics2D g2d = (Graphics2D) g;
        // float thickness = 2.0f; // Set your desired thickness here
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawLine(x1, y1, x2, y2);
    }

    private void drawCubicBezierCurveWithThickness(Graphics g, int x1, int y1, int ctrlX1, int ctrlY1,
            int ctrlX2, int ctrlY2, int x2, int y2, int thickness, Color color) {

        g.setColor(color);

        int resolution = 500;

        for (int t = 0; t <= resolution; t++) {
            float u = t / (float) resolution;
            float uComp = 1 - u;

            int x = (int) (uComp * uComp * uComp * x1 + 3 * uComp * uComp * u * ctrlX1
                    + 3 * uComp * u * u * ctrlX2 + u * u * u * x2);
            int y = (int) (uComp * uComp * uComp * y1 + 3 * uComp * uComp * u * ctrlY1
                    + 3 * uComp * u * u * ctrlY2 + u * u * u * y2);

            // Draw rectangles to simulate thickness
            if (t == 0 || t == resolution) {
                fillRectWithRound(g, x, y, thickness);
            } else {
                fillRect(g, x, y, thickness);
            }

        }
    }

    // Helper method to fill a rectangle (can be replaced with g.drawLine for lines)
    // private void fillRect(Graphics g, int x, int y, int thickness) {
    // g.fillRect(x - thickness / 2, y - thickness / 2, thickness, thickness);
    // }

    private void fillRect(Graphics g, int x, int y, int thickness) {
        int halfThickness = thickness / 2;

        for (int i = -halfThickness; i <= halfThickness; i++) {
            for (int j = -halfThickness; j <= halfThickness; j++) {
                plot(g, x + i, y + j);
            }
        }
    }

    private void fillRectWithRound(Graphics g, int x, int y, int thickness) {
        int halfThickness = thickness / 2;
        int radiusSquared = halfThickness * halfThickness;

        for (int i = -halfThickness; i <= halfThickness; i++) {
            for (int j = -halfThickness; j <= halfThickness; j++) {
                if (i * i + j * j <= radiusSquared) {
                    plot(g, x + i, y + j);
                }
            }
        }
    }

    private void myPaint(Graphics g) {
        // g.drawPdrawPolygon(g,1200, 1200, 0.5);
        // พื้นหลัง
        drawRectangle(g, 0, 0, 600, 600, Color.WHITE);

        // test curvature

        drawCubicBezierCurveWithThickness(g, 50, 100, 100, 50, 200, 150, 550, 100, 9, Color.red); // Adjust thickness
                                                                                                  // here
        drawBezierCurve(g, 50, 150, 100, 50, 200, 150, 550, 150, 9, Color.blue); // Adjust thickness here
        drawNoteBook(g, 50, 450);

        fillRectWithRound(g, 300, 300, 10);
    }

    void drawNoteBook(Graphics g, int x, int y) {
        // g.setColor(Color.BLACK);
        // base - board
        drawSquare(g, x + 10, y + 50, x + 56, y + 40, x + 80, y + 55, x + 40, y + 70, null, Color.decode("#40476E"), 1);
        // keyboard
        drawSquare(g, x + 20, y + 51, x + 55, y + 43, x + 72, y + 52, x + 40, y + 63, Color.black,
                Color.decode("#82869C"), 1);
        // keyboard horizon
        int r = 0;
        int c = 0;
        for (int i = 0; i < 26; i += 4) {
            drawLine(g, x + 23 + i, y + 51 - r, x + 42 + i, y + 63 - r - c, Color.BLACK, 1);// top
            r++;
            c++;
        }
        // keyboard Vertical
        r = 0;
        c = 0;
        for (int i = 0; i < 9; i += 2) {
            drawLine(g, x + 23 + r, y + 51 + i, x + 55 + r, y + 43 + i, Color.BLACK, 1);// top
            r += 4;
            c++;
        }

        // monitor
        drawLine(g, x, y + 10, x + 50, y, Color.BLACK, 3);// top
        drawLine(g, x + 50, y, x + 55, y + 40, Color.BLACK, 3);// right
        drawLine(g, x + 55, y + 40, x + 10, y + 50, Color.BLACK, 3);// crease
        drawLine(g, x + 8, y + 50, x, y + 10, Color.BLACK, 4);// left
        // drawRectangle(g, x, y1, x2, y2, Color.CYAN);

        // base - edge
        drawLine(g, x + 55, y + 38, x + 82, y + 55, Color.BLACK, 3);// right
        drawLine(g, x + 9, y + 50, x + 40, y + 70, Color.BLACK, 5);// left
        drawLine(g, x + 80, y + 55, x + 40, y + 70, Color.BLACK, 5);// button

    }

}
