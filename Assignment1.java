import java.awt.*;
import javax.swing.*;

// class Assignment1 extends Color {
//     public Color(int r, int g, int b) {
//         super(r, g, b);
//     }

//     public static final Color BACKGROUND = Color.decode("#E2998E");
//     public static final Color HOUSE_WALL = Color.decode("#BC645C");
//     public static final Color WINDOW = Color.decode("#DDA763");
//     public static final Color TABLE = Color.decode("#606355");
//     public static final Color NIGHTTIME = Color.decode("#13547A");
//     public static final Color MONITOR_BORDER = Color.decode("#ACB1B7");
//     public static final Color SHADOW_MONITOR_BORDER = Color.decode("#7B7D83");
//     public static final Color COMPUTER_MONITOR = Color.decode("#F4E289");
//     public static final Color VS_CODE_BACKGROUND = Color.decode("#2C2929");
//     public static final Color CODE_COLOR_ORANGE = Color.decode("#DA6F31");
//     public static final Color CODE_COLOR_GREEN = Color.decode("#179158");

//     public static final Color LINE = BLACK;

// }

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

    private void drawLine(Graphics g, int x1, int y1, int x2, int y2, Color color) {
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }

    private void drawArc(Graphics g, int x, int y, int width, int height, int startAngle, int arcAngle) {
        g.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    private void myPaint(Graphics g) {
        // g.drawPdrawPolygon(g,1200, 1200, 0.5); 
        // พื้นหลัง
        drawRectangle(g, 0, 0, 600, 600, Color.white);
    }

}
