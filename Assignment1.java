import java.awt.*;
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

    private void drawLine(Graphics g, int x1, int y1, int x2, int y2, Color color,int thickness) {
        g.setColor(color);
        // g.set
        Graphics2D g2d = (Graphics2D) g;
        // float thickness = 2.0f; // Set your desired thickness here
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawLine(x1, y1, x2, y2);
    }

    private void drawArc(Graphics g, int x, int y, int width, int height, int startAngle, int arcAngle) {
        g.drawArc(x, y, width, height, startAngle, arcAngle);
    }


    private void drawBezierCurve(Graphics g, int x1, int y1, int ctrlX, int ctrlY, int x2, int y2, int thickness,Color color) {
        g.setColor(color);
        int resolution = 500;

        for (int t = 0; t <= resolution; t++) {
            float u = t / (float) resolution;
            float uComp = 1 - u;

            int x = (int) (uComp * uComp * x1 + 2 * uComp * u * ctrlX + u * u * x2);
            int y = (int) (uComp * uComp * y1 + 2 * uComp * u * ctrlY + u * u * y2);

            fillRect(g, x, y, thickness);
        }
    }

    private void fillRect(Graphics g, int x, int y, int thickness) {
        int halfThickness = thickness / 2;

        for (int i = -halfThickness; i <= halfThickness; i++) {
            for (int j = -halfThickness; j <= halfThickness; j++) {
                plot(g, x + i, y + j);
            }
        }
    }

    private void myPaint(Graphics g) {
        // g.drawPdrawPolygon(g,1200, 1200, 0.5); 
        // พื้นหลัง
        drawRectangle(g, 0, 0, 600, 600, Color.WHITE);


        //test curvature
        int x1 = 50;
        int y1 = 200;
        int x2 = 350;
        int y2 = 200;

        int ctrlX = x1 + 50; // Adjust this value for curvature
        int ctrlY = y1 - 50; // Adjust this value for curvature

        drawBezierCurve(g, x1, y1, ctrlX, ctrlY, x2, y2, 9,Color.BLACK); // Adjust thickness here
        drawNoteBook(g,50,450);
    }
    void drawNoteBook(Graphics g,int x,int y ){
        // g.setColor(Color.BLACK);
        drawLine(g, x, y+10, x+50, y, Color.BLACK,3);        
        drawLine(g, x+50, y, x+55, y+40, Color.BLACK,3);

    }
    

}
