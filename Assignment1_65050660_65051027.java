import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;
import java.util.Queue;

import javax.swing.*;

public class Assignment1_65050660_65051027 extends JPanel {
    // final int LINE_WIDTH = 2;

    public static void main(String[] args) {
        Assignment1_65050660_65051027 n = new Assignment1_65050660_65051027();

        JFrame f = new JFrame();
        f.add(n);
        f.setTitle("NewYear");
        f.setSize(600, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        myPaint(g);
    }

    private void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }

    int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void drawRectangle(Graphics g, int x1, int y1, int x2, int y2, Color color) {
        g.setColor(color);
        for (int x = x1; x < x2; x++) {
            for (int y = y1; y < y2; y++) {
                plot(g, x, y);
            }
        }
    }

    private void drawnOval(Graphics g, int x, int y, int width, int height, Color color) {
        g.setColor(color);
        int xR = width / 2;
        int yR = height / 2;
        g.fillOval(x - xR, y - yR, width, height);
    }

    private void drawnOvalMine(Graphics g, int x, int y, int width, int height, Color color) {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics();
        int xR = width / 2;
        int yR = height / 2;

        drawEllipse(buffer, x, y, yR, xR, 2, color);
        boolean found = false;
        if (!isVarid(x, y)) {
            int xStart = x - xR;
            int yStart = y - yR;
            int xEnd = x + xR;
            int yEnd = y + yR;
            g2.setColor(Color.green);
            for (int i = xStart; i <= xEnd; i++) {
                for (int j = yStart; j <= yEnd; j++) {
                    if (isVarid(i, j) && isInsideEllipse(i, j, x, y, xR, yR, 2)) {
                        found = true;
                        x = i;
                        y = j;
                        break;
                    }
                }
                if (found)
                    break;
            }
        }
        buffer = floodFillAlpha(buffer, x, y, color, color);
        g.drawImage(buffer, 0, 0, null);
    }

    private void setRGBMine(BufferedImage buffer, int x, int y, int thickness, Color color) {
        int halfThickness = thickness / 2;
        for (int i = -halfThickness; i <= halfThickness; i++) {
            for (int j = -halfThickness; j <= halfThickness; j++) {
                if (!isVarid(x + i, y + j)) {
                    continue;
                }
                buffer.setRGB(x + i, y + j, color.getRGB());
            }
        }
    }

    private boolean isInsideEllipse(int x, int y, int centerX, int centerY, int xRadius, int yRadius, int thickness) {
        double normalizedX = (x - centerX) / (double) (xRadius - thickness);
        double normalizedY = (y - centerY) / (double) (yRadius - thickness);
        double equationResult = Math.pow(normalizedX, 2) + Math.pow(normalizedY, 2);
        return equationResult < 1;
    }

    private Color randomColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b);
    }

    double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private int[] getBezierCurveMine(int x1, int y1, int ctrlX, int ctrlY,
            int x2, int y2, int t, int resolution) {

        float u = t / (float) resolution;
        float uComp = 1 - u;

        int x = (int) (uComp * uComp * x1 + 2 * uComp * u * ctrlX + u * u * x2);
        int y = (int) (uComp * uComp * y1 + 2 * uComp * u * ctrlY + u * u * y2);

        return new int[] { x, y };
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
            int tHalf = thickness / 2;
            g.setColor(colorI);
            g.fillPolygon(new int[] { x + tHalf, x2 - tHalf, x4 + tHalf },
                    new int[] { y + tHalf, y2 + tHalf, y4 - tHalf }, 3);
            g.fillPolygon(new int[] { x2 - tHalf, x3 - tHalf, x4 + tHalf },
                    new int[] { y2 + tHalf, y3 - tHalf, y4 - tHalf }, 3);
        }

    }

    private void drawDottedQuadraticBezierCurveMinePixel(Graphics g, int x1, int y1, int ctrlX, int ctrlY,
            int x2, int y2, int thickness, Color color, int dotSpacing, int lenDrawn) {
        int resolution = 500;
        int[] previous = new int[] { x1, y1 };
        boolean skip = false;

        for (int t = 0; t <= resolution; t++) {
            float u = t / (float) resolution;
            float uComp = 1 - u;
            int x = Math.round(uComp * uComp * x1 + 2 * uComp * u * ctrlX + u * u * x2);
            int y = Math.round(uComp * uComp * y1 + 2 * uComp * u * ctrlY + u * u * y2);

            if (skip) {
                if (calculateDistance(previous[0], previous[1], x, y) > dotSpacing) {
                    skip = false;
                    previous = new int[] { x, y };
                } else {
                    continue;
                }
            } else if (calculateDistance(previous[0], previous[1], x, y) > lenDrawn) {
                previous = new int[] { x, y };
                skip = true;
                continue;
            }
            fillRectMine(g, x, y, thickness, color);
        }
    }

    private void drawDottedQuadraticBezierCurveMine(Graphics g, int x1, int y1, int ctrlX, int ctrlY,
            int x2, int y2, int thickness, Color color, int dotSpacing, int lenDrawn) {
        int resolution = 500;
        int countLenDrawn = 0;

        for (int t = 0; t <= resolution; t++) {
            if (countLenDrawn > lenDrawn) {
                t += dotSpacing;
                countLenDrawn = 0;
            }
            float u = t / (float) resolution;
            float uComp = 1 - u;
            int x = Math.round(uComp * uComp * x1 + 2 * uComp * u * ctrlX + u * u * x2);
            int y = Math.round(uComp * uComp * y1 + 2 * uComp * u * ctrlY + u * u * y2);
            countLenDrawn++;

            fillRectMine(g, x, y, thickness, color);
        }
    }

    private void drawLine(Graphics g, int x1, int y1, int x2, int y2, Color color, int thickness) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        boolean isSwap = false;
        if (dy > dx) {
            int dxT = dx;
            dx = dy;
            dy = dxT;
            isSwap = true;
        }
        int D = 2 * dy - dx;

        int x = x1;
        int y = y1;

        for (int i = 1; i <= dx; i++) {
            fillRectMine(g, x, y, thickness, color);

            if (D >= 0) {
                if (isSwap)
                    x += sx;
                else
                    y += sy;

                D -= 2 * dx;
            }
            if (isSwap)
                y += sy;
            else
                x += sx;

            D += 2 * dy;
        }

    }

    private void drawBezierCurveMine(Graphics g, int x1, int y1, int ctrlX1, int ctrlY1,
            int ctrlX2, int ctrlY2, int x2, int y2, int thickness, Color color) {

        int resolution = 500;

        for (int t = 0; t <= resolution; t++) {
            float u = t / (float) resolution;
            float uComp = 1 - u;

            int x = (int) (uComp * uComp * uComp * x1 + 3 * uComp * uComp * u * ctrlX1
                    + 3 * uComp * u * u * ctrlX2 + u * u * u * x2);
            int y = (int) (uComp * uComp * uComp * y1 + 3 * uComp * uComp * u * ctrlY1
                    + 3 * uComp * u * u * ctrlY2 + u * u * u * y2);

            if (t == 0 || t == resolution) {
                fillRectWithRound(g, x, y, thickness, color);
            } else {
                fillRectMine(g, x, y, thickness, color);
            }

        }
    }

    private void drawBezierCurveMine(Graphics g, int x1, int y1, int ctrlX, int ctrlY, int x2, int y2, int thickness,
            Color color) {
        int resolution = 500;
        g.setColor(color);

        for (int t = 0; t <= resolution; t++) {
            float u = t / (float) resolution;
            float uComp = 1 - u;

            int x = (int) (uComp * uComp * x1 + 2 * uComp * u * ctrlX + u * u * x2);
            int y = (int) (uComp * uComp * y1 + 2 * uComp * u * ctrlY + u * u * y2);

            fillRectMine(g, x, y, thickness, color);
        }
    }

    private void drawBezierCurveMineBuffer(BufferedImage bufferedImage, int x1, int y1, int ctrlX1, int ctrlY1,
            int ctrlX2, int ctrlY2, int x2, int y2, int thickness, Color color) {
        int resolution = 500;

        for (int t = 0; t <= resolution; t++) {
            float u = t / (float) resolution;
            float uComp = 1 - u;

            int x = (int) (uComp * uComp * uComp * x1 + 3 * uComp * uComp * u * ctrlX1
                    + 3 * uComp * u * u * ctrlX2 + u * u * u * x2);
            int y = (int) (uComp * uComp * uComp * y1 + 3 * uComp * uComp * u * ctrlY1
                    + 3 * uComp * u * u * ctrlY2 + u * u * u * y2);
            setRGBMine(bufferedImage, x, y, thickness, color);

        }
    }

    private void fillRectMine(Graphics g, int x, int y, int thickness, Color color) {
        int halfThickness = thickness / 2;
        g.setColor(color);
        for (int i = -halfThickness; i <= halfThickness; i++) {
            for (int j = -halfThickness; j <= halfThickness; j++) {
                plot(g, x + i, y + j);
            }
        }
    }

    private Color interpolateColor(Color startColor, Color endColor, float ratio) {

        int red = (int) (startColor.getRed() + ratio * (endColor.getRed() - startColor.getRed()));
        int green = (int) (startColor.getGreen() + ratio * (endColor.getGreen() - startColor.getGreen()));
        int blue = (int) (startColor.getBlue() + ratio * (endColor.getBlue() - startColor.getBlue()));
        int alpha = interpolateNumber(startColor.getAlpha(), endColor.getAlpha(), ratio);

        return new Color(red, green, blue, alpha);
    }

    private int interpolateNumber(int start, int end, float ratio) {

        int x = (int) (start + ratio * (end - start));

        return x;
    }

    private void fillRectWithRound(Graphics g, int x, int y, int thickness, Color color) {
        int halfThickness = thickness / 2;
        int radiusSquared = halfThickness * halfThickness;
        g.setColor(color);
        for (int i = -halfThickness; i <= halfThickness; i++) {
            for (int j = -halfThickness; j <= halfThickness; j++) {
                if (i * i + j * j <= radiusSquared) {
                    plot(g, x + i, y + j);
                }
            }
        }
    }

    private boolean isVarid(int x, int y) {
        return (x > -1 && x < 601) && (y > -1 && y < 601);
    }

    public BufferedImage floodFill(BufferedImage m, int x, int y, Color border) {
        return floodFill(m, x, y, border, border);
    }

    public BufferedImage floodFill(BufferedImage m, int x, int y, Color border, Color replace) {
        Queue<int[]> q = new LinkedList<>();

        q.add(new int[] { x, y });
        int borderRGB = border.getRGB();
        int replaceRGB = replace.getRGB();
        if (border.getAlpha() != 255) {
        }
        int[] currentPos;
        while (!q.isEmpty()) {
            currentPos = q.poll();
            int x1 = currentPos[0];
            int y1 = currentPos[1];

            if (isVarid(x1, y1) && m.getRGB(x1, y1) != borderRGB && m.getRGB(x1, y1) != replaceRGB) {
                m.setRGB(x1, y1, replaceRGB);
                // south
                if (isVarid(x1, y1 + 1) && m.getRGB(x1, y1 + 1) != borderRGB && m.getRGB(x1, y1 + 1) != replaceRGB) {
                    q.add(new int[] { x1, y1 + 1 });
                }
                // north
                if (isVarid(x1, y1 - 1) && m.getRGB(x1, y1 - 1) != borderRGB && m.getRGB(x1, y1 - 1) != replaceRGB) {
                    q.add(new int[] { x1, y1 - 1 });
                }
                // east
                if (isVarid(x1 + 1, y1) && m.getRGB(x1 + 1, y1) != borderRGB && m.getRGB(x1 + 1, y1) != replaceRGB) {
                    q.add(new int[] { x1 + 1, y1 });
                }
                // west
                if (isVarid(x1 - 1, y1) && m.getRGB(x1 - 1, y1) != borderRGB && m.getRGB(x1 - 1, y1) != replaceRGB) {
                    q.add(new int[] { x1 - 1, y1 });
                }
            }
        }

        return m;
    }

    public BufferedImage floodFillAlpha(BufferedImage m, int x, int y, Color border, Color replace) {
        Queue<int[]> q = new LinkedList<>();

        q.add(new int[] { x, y });
        int borderRGB = border.getRGB() & 0xFFFFFF;
        int replaceRGB = replace.getRGB() & 0xFFFFFF;

        int[] currentPos;
        while (!q.isEmpty()) {
            currentPos = q.poll();
            int x1 = currentPos[0];
            int y1 = currentPos[1];

            if (isVarid(x1, y1)) {
                int rgb = m.getRGB(x1, y1) & 0xFFFFFF;
                if (rgb != borderRGB && rgb != replaceRGB) {
                    m.setRGB(x1, y1, replace.getRGB());
                    // south
                    if (isVarid(x1, y1 + 1)) {
                        int southRGB = m.getRGB(x1, y1 + 1) & 0xFFFFFF;
                        if (southRGB != borderRGB && southRGB != replaceRGB) {
                            q.add(new int[] { x1, y1 + 1 });
                        }
                    }
                    // north
                    if (isVarid(x1, y1 - 1)) {
                        int northRGB = m.getRGB(x1, y1 - 1) & 0xFFFFFF;
                        if (northRGB != borderRGB && northRGB != replaceRGB) {
                            q.add(new int[] { x1, y1 - 1 });
                        }
                    }
                    // east
                    if (isVarid(x1 + 1, y1)) {
                        int eastRGB = m.getRGB(x1 + 1, y1) & 0xFFFFFF;
                        if (eastRGB != borderRGB && eastRGB != replaceRGB) {
                            q.add(new int[] { x1 + 1, y1 });
                        }
                    }
                    // west
                    if (isVarid(x1 - 1, y1)) {
                        int westRGB = m.getRGB(x1 - 1, y1) & 0xFFFFFF;
                        if (westRGB != borderRGB && westRGB != replaceRGB) {
                            q.add(new int[] { x1 - 1, y1 });
                        }
                    }
                }
            }
        }

        return m;
    }

    private void drawEllipse(BufferedImage buffer, int centerX, int centerY, int a, int b, int thickness,
            Color color) {
        int aHalf = a;
        int bHalf = b;

        int ctrlPointX = (int) (a * 0.55191502449); // Approximately 4*(sqrt(2)-1)/3
        int ctrlPointY = (int) (b * 0.55191502449); // Approximately 4*(sqrt(2)-1)/3

        // First quadrant
        drawBezierCurveMineBuffer(buffer, centerX, centerY - bHalf, centerX + ctrlPointX, centerY - bHalf,
                centerX + aHalf, centerY - ctrlPointY, centerX + aHalf, centerY, thickness, color);
        drawBezierCurveMineBuffer(buffer, centerX + aHalf, centerY, centerX + aHalf, centerY + ctrlPointY,
                centerX + ctrlPointX, centerY + bHalf, centerX, centerY + bHalf, thickness, color);

        // Second quadrant
        drawBezierCurveMineBuffer(buffer, centerX, centerY + bHalf, centerX - ctrlPointX, centerY + bHalf,
                centerX - aHalf, centerY + ctrlPointY, centerX - aHalf, centerY, thickness, color);
        drawBezierCurveMineBuffer(buffer, centerX - aHalf, centerY, centerX - aHalf, centerY - ctrlPointY,
                centerX - ctrlPointX, centerY - bHalf, centerX, centerY - bHalf, thickness, color);
    }

    // --------------------------------------- work
    // space---------------------------------------
    // --------------------------------------- work
    // space---------------------------------------
    // --------------------------------------- work
    // space---------------------------------------
    // --------------------------------------- work
    // space---------------------------------------
    private void myPaint(Graphics g) {
        // background
        drawRectangle(g, 0, 0, 600, 600, Color.WHITE);

        // work space
        // sky
        drawnSky(g, 0, 0);
        drawOvalGradient(g, 250, -50, 250, 300);
        drawOvalGradient(g, -50, -150, 200, 200);// LT
        drawOvalGradient(g, -10, 100, 200, 200);// LB
        drawOvalGradient(g, 500, 50, 250, 250);// RB
        drawOvalGradient(g, 400, -100, 200, 200);// RT
        drawnStar(g, 600, 400, 5);
        drawMoon(g, 100, 75, 50, 50);
        drawFireworks(g, -50, -150, 200);// LT
        drawFireworks(g, -10, 100, 200);// LB
        drawFireworks(g, 500, 50, 250);// RB
        drawFireworks(g, 400, -100, 200);// RT
        drawnMiniFireWorks(g, 340, 600, 50, 50);
        // gound
        drawnGround(g, 0, 0);
        drawHuman(g, 300, 300);
        drawHuman(g, 410, 300);
        drawNoteBook(g, 10, 500);

    }
    // --------------------------------------- work
    // space---------------------------------------
    // --------------------------------------- work
    // space---------------------------------------
    // --------------------------------------- work
    // space---------------------------------------
    // --------------------------------------- work
    // space---------------------------------------

    private void drawnMiniFireWorks(Graphics g, int y, int width, int height, int maxsize) {
        int count = 50;
        for (int i = 0; i < count; i++) {
            float ratio = (float) i / count;
            int ratioX = (int) (ratio * width);
            drawFireworks(g, getRandomNumber(ratioX, ratioX * 2), getRandomNumber(y, y + height),
                    getRandomNumber(10, maxsize));
        }
    }

    private void drawnSky(Graphics g, int x, int y) {
        Color colorStart = new Color(105, 90, 109);
        Color colorEnd = new Color(18, 26, 58);
        float count = 300f;
        float height = 450f;
        float maxHeight = 400f;
        float step = height / count;
        System.out.println(step);
        float divider = 0.3f;
        // sky
        for (int i = 0; i <= count; i++) {
            float ratio = (float) i / count;
            if (i <= count * divider) {
                ratio = (float) i / (count * divider);
            } else {
                ratio = 1.0f;
            }
            Color colorCurrent = interpolateColor(colorStart, colorEnd, ratio);
            int currentY = (int) (maxHeight - ((i / count) * height));
            drawBezierCurveMine(g, -50, currentY, 200, currentY - 50, 400, currentY + 50, 650, currentY, (int) step * 3,
                    colorCurrent);
        }
    }

    private void drawnGround(Graphics g, int x, int y) {
        Color colorStart = Color.decode("#286505");
        Color colorEnd = new Color(18, 26, 4);
        float count = 300f;
        float height = 350f;
        float minHeight = 400f;
        float step = height / count;
        System.out.println(step);
        float divider = 0.1f;
        for (int i = 0; i <= count; i++) {
            float ratio = (float) i / count;
            if (i <= count * divider) {
                ratio = (float) i / (count * divider);
            } else {
                ratio = 1.0f;
            }
            Color colorCurrent = interpolateColor(colorStart, colorEnd, ratio);
            int currentY = (int) (minHeight + (i / count) * height);
            drawBezierCurveMine(g, -50, currentY, 200, currentY - 50, 400, currentY + 50, 650, currentY, (int) step * 3,
                    colorCurrent);
        }
    }

    private void drawnStar(Graphics g, int width, int height, int maxsize) {
        int count = 200;
        for (int i = 0; i < count; i++) {
            fillRectWithRound(g, getRandomNumber(0, width), getRandomNumber(0, height), getRandomNumber(1, maxsize),
                    Color.WHITE);
        }
    }

    private void drawNoteBook(Graphics g, int x, int y) {
        // base - board
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics();
        drawSquare(g2, x + 10, y + 50, x + 56, y + 40, x + 80, y + 55, x + 40, y + 70, null, Color.decode("#40476E"),
                1);
        // keyboard
        drawSquare(g2, x + 20, y + 51, x + 55, y + 43, x + 72, y + 52, x + 40, y + 63, Color.black,
                Color.decode("#82869C"), 1);
        // keyboard horizon
        int r = 0;
        int c = 0;
        for (int i = 0; i < 26; i += 4) {
            drawLine(g2, x + 23 + i, y + 51 - r, x + 42 + i, y + 63 - r - c, Color.BLACK, 1);// top
            r++;
            c++;
        }
        // keyboard Vertical
        r = 0;
        c = 0;
        for (int i = 0; i < 9; i += 2) {
            drawLine(g2, x + 23 + r, y + 51 + i, x + 55 + r, y + 43 + i, Color.BLACK, 1);// top
            r += 4;
            c++;
        }
        // monitor
        drawLine(g2, x, y + 10, x + 50, y, Color.BLACK, 3);// top
        drawLine(g2, x + 50, y, x + 55, y + 40, Color.BLACK, 3);// right
        drawLine(g2, x + 55, y + 40, x + 10, y + 50, Color.BLACK, 3);// crease
        drawLine(g2, x + 8, y + 50, x, y + 10, Color.BLACK, 4);// left

        // monitor interior
        buffer = floodFill(buffer, x + 40, y + 30, Color.black, Color.decode("#2B2F49"));

        // base - edge
        drawLine(g2, x + 55, y + 38, x + 82, y + 55, Color.BLACK, 3);// right
        drawLine(g2, x + 9, y + 50, x + 40, y + 70, Color.BLACK, 5);// left
        drawLine(g2, x + 80, y + 55, x + 40, y + 70, Color.BLACK, 5);// button

        // dargon
        Color colorD = Color.decode("#BC4C14");
        // wing
        drawBezierCurveMine(g2, x + 11, y + 14, x + 24, y + 10, x + 25, y + 20, 1, colorD);
        drawBezierCurveMine(g2, x + 11, y + 19, x + 24, y + 15, x + 25, y + 20, 1, colorD);
        drawBezierCurveMine(g2, x + 11, y + 24, x + 24, y + 20, x + 25, y + 20, 1, colorD);
        // body
        drawBezierCurveMine(g2, x + 38, y + 25, x + 21, y + 11, x + 21, y + 37, x + 32, y + 32, 1, colorD);
        drawBezierCurveMine(g2, x + 32, y + 32, x + 40, y + 33, x + 42, y + 34, x + 46, y + 38, 1, colorD);
        drawBezierCurveMine(g2, x + 32, y + 32, x + 40, y + 33, x + 42, y + 34, x + 44, y + 40, 1, colorD);
        g.drawImage(buffer, 0, 0, null);
    }

    private void drawMoon(Graphics g, int x, int y, int width, int height) {
        int centerX = x + (width / 2);
        int centerY = y + (height / 2);
        drawnOval(g, centerX, centerY, width, height, Color.decode("#C0C3D4"));
    }

    private void drawOvalGradient(Graphics g, int x, int y, int width, int height) {
        int centerX = x + (width / 2);
        int centerY = y + (height / 2);
        width *= 1.5;
        height *= 1.5;
        int count = 100;

        Color colorStart = new Color(217, 184, 165, 255 / (count / 2 / 2 / 2));
        Color colorEnd = new Color(217, 184, 165, 0);

        float weight = 0.5f;

        for (int i = count; i >= 1; i--) {
            float ratio = (float) i / count;
            float ratioColor = (float) Math.pow(ratio, weight);
            Color colorCurrent = interpolateColor(colorStart, colorEnd, ratioColor);
            int xR = interpolateNumber(1, width, ratio);
            int yR = interpolateNumber(1, height, ratio);
            drawnOvalMine(g, centerX, centerY, xR, yR, colorCurrent);
        }
    }

    private void drawFireworks(Graphics g, int x, int y, int size) {
        int dotSpacing = 10;
        int drawnLen = 10;
        int centerX = x + (size / 2);
        int centerY = y + (size / 2);
        int half = (size) / 2;
        int count = 10;
        int stepP = half / count;
        for (int i = 1; i <= count; i++) {
            int[] points = getBezierCurveMine(0, 0, half, 0, half, half, i, count);
            int[] pointsMinus1 = getBezierCurveMine(0, 0, half, 0, half, half, i - 1, count);
            drawDottedQuadraticBezierCurveMine(g, centerX, centerY, (centerX + (stepP * i) / 2), (y + (stepP * i) / 2),
                    (centerX + points[0]),
                    (y + points[1]), 1, randomColor(), dotSpacing, drawnLen);
            drawDottedQuadraticBezierCurveMine(g, centerX, centerY, (centerX + (stepP * (i + 1)) / 2),
                    (y + size - stepP * (i + (count / 2))), (centerX + points[0]),
                    (y + size - pointsMinus1[1]), 1, randomColor(), dotSpacing, drawnLen);
            drawDottedQuadraticBezierCurveMine(g, centerX, centerY, (centerX - (stepP * i) / 2), (y + (stepP * i) / 2),
                    (centerX - points[0]),
                    (y + points[1]), 1, randomColor(), dotSpacing, drawnLen);
            drawDottedQuadraticBezierCurveMine(g, centerX, centerY, (centerX - (stepP * (i + 1)) / 2),
                    (y + size - stepP * (i + (count / 2))), (centerX - points[0]),
                    (y + size - pointsMinus1[1]), 1, randomColor(), dotSpacing, drawnLen);
        }

    }

    private void drawHuman(Graphics g, int x, int y) {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics();
        Color colorLine = Color.BLACK;
        int thickness = 2;
        // head

        // g2.f
        int endL[] = { x + 50, y + 34 };
        int endR[] = { x + 69, y + 34 };
        drawBezierCurveMine(g2, endL[0], endL[1], x + 40, y + 13, x + 73, y + 13, endR[0], endR[1], thickness,
                colorLine);
        // ear
        drawBezierCurveMine(g2, x + 48, y + 30, x + 46, y + 31, x + 48, y + 34, thickness, colorLine);// l
        drawBezierCurveMine(g2, x + 69, y + 30, x + 72, y + 31, endR[0], endR[1], thickness, colorLine);// r
        // neck
        int endNL[] = { x + 52, y + 40 };
        int endNR[] = { x + 67, y + 40 };
        drawBezierCurveMine(g2, endL[0], endL[1], x + 51, y + 35, endNL[0], endNL[1], thickness, colorLine);
        drawBezierCurveMine(g2, endR[0], endR[1], x + 68, y + 39, endNR[0], endNR[1], thickness, colorLine);
        // arm
        int startAL1[] = { x + 39, y + 43 };
        int startAL2[] = { x + 37, y + 60 };
        int startAR1[] = { x + 87, y + 45 };
        int startAR2[] = { x + 89, y + 59 };
        // L
        drawBezierCurveMine(g2, startAL1[0], startAL1[1], x + 16, y + 27, x + 25, y + 39, x + 14, y + 11, thickness,
                colorLine);
        drawBezierCurveMine(g2, startAL2[0], startAL2[1], x + 20, y + 50, x + 12, y + 42, x, y + 15, thickness,
                colorLine);
        drawLine(g2, x, y + 15, x + 14, y + 11, Color.BLACK, thickness);
        // R
        drawBezierCurveMine(g2, startAR1[0], startAR1[1], x + 108, y + 35, x + 103, y + 40, x + 113, y + 13, thickness,
                colorLine);
        drawBezierCurveMine(g2, startAR2[0], startAR2[1], x + 105, y + 58, x + 108, y + 53, x + 126, y + 19, thickness,
                colorLine);
        drawLine(g2, x + 113, y + 13, x + 126, y + 19, Color.BLACK, thickness);
        // hand
        // L
        drawBezierCurveMine(g2, x + 5, y + 13, x - 2, y + 5, x + 10, y + 1, x + 14, y + 3, thickness, colorLine);
        drawBezierCurveMine(g2, x + 14, y + 3, x + 18, y - 10, x + 18, y + 8, x + 13, y + 12, thickness, colorLine);
        // R
        drawBezierCurveMine(g2, x + 122, y + 16, x + 130, y + 5, x + 124, y + 1, x + 114, y + 4, thickness, colorLine);
        drawBezierCurveMine(g2, x + 114, y + 4, x + 110, y - 10, x + 110, y + 8, x + 114, y + 13, thickness, colorLine);
        // shirt
        int endSL[] = { x + 37, y + 109 };
        int endSR[] = { x + 89, y + 108 };
        // L
        drawBezierCurveMine(g2, endNL[0], endNL[1], x + 41, y + 38, x + 41, y + 38, startAL1[0], startAL1[1], thickness,
                colorLine);
        drawBezierCurveMine(g2, startAL2[0], startAL2[1], x + 16, y + 115, x + 16, y + 95, endSL[0], endSL[1],
                thickness,
                colorLine);
        // R
        drawBezierCurveMine(g2, endNR[0], endNR[1], x + 83, y + 36, x + 79, y + 39, startAR1[0], startAR1[1], thickness,
                colorLine);
        drawBezierCurveMine(g2, startAR2[0], startAR2[1], x + 104, y + 114, x + 114, y + 104, endSR[0], endSR[1],
                thickness,
                colorLine);
        // trousers
        int endTL1[] = { x + 32, y + 205 };
        int endTL2[] = { x + 45, y + 201 };
        int endTR1[] = { x + 98, y + 217 };
        int endTR2[] = { x + 84, y + 201 };
        int center[] = { x + 64, y + 128 };
        // L
        drawBezierCurveMine(g2, endSL[0], endSL[1], x + 34, y + 200, endTL1[0], endTL1[1], thickness, colorLine);
        drawBezierCurveMine(g2, center[0], center[1], x + 53, y + 200, x + 53, y + 194, endTL2[0], endTL2[1], thickness,
                colorLine);
        // R
        drawBezierCurveMine(g2, endSR[0], endSR[1], x + 103, y + 219, x + 103, y + 208, endTR1[0], endTR1[1], thickness,
                colorLine);
        drawBezierCurveMine(g2, center[0], center[1], x + 74, y + 162, x + 71, y + 149, endTR2[0], endTR2[1], thickness,
                colorLine);
        // foot
        // L
        drawBezierCurveMine(g2, endTL1[0], endTL1[1], x + 27, y + 207, x + 40, y + 217, x + 34, y + 222, thickness,
                colorLine);
        drawBezierCurveMine(g2, x + 34, y + 222, x + 26, y + 230, x + 53, y + 230, x + 45, y + 220, thickness,
                colorLine);
        drawBezierCurveMine(g2, x + 45, y + 220, x + 45, y + 210, x + 55, y + 205, endTL2[0], endTL2[1], thickness,
                colorLine);
        // R
        drawBezierCurveMine(g2, endTR1[0], endTR1[1], x + 100, y + 214, x + 98, y + 219, x + 100, y + 222, thickness,
                colorLine);
        drawBezierCurveMine(g2, x + 100, y + 222, x + 103, y + 226, x + 83, y + 229, x + 88, y + 221, thickness,
                colorLine);
        drawBezierCurveMine(g2, x + 88, y + 221, x + 90, y + 210, x + 79, y + 210, endTR2[0], endTR2[1], thickness,
                colorLine);

        // fill
        buffer = floodFill(buffer, x + 9, y + 7, Color.BLACK);// left
        buffer = floodFill(buffer, x + 118, y + 7, Color.BLACK);// right
        buffer = floodFill(buffer, x + 50, y + 100, Color.BLACK);
        g.drawImage(buffer, 0, 0, null);
    }

}
