package com.cybertetris;

import java.awt.*;
import java.util.Random;

/**
 * Tetromino shapes with cyberpunk neon colors
 */
public enum Tetromino {
    I_SHAPE(new int[][]{{1,1,1,1}}, new Color(0, 255, 255)),      // Cyan
    O_SHAPE(new int[][]{{1,1},{1,1}}, new Color(255, 0, 255)),    // Magenta
    T_SHAPE(new int[][]{{0,1,0},{1,1,1}}, new Color(148, 0, 211)), // Purple
    S_SHAPE(new int[][]{{0,1,1},{1,1,0}}, new Color(0, 255, 127)), // Neon Green
    Z_SHAPE(new int[][]{{1,1,0},{0,1,1}}, new Color(255, 69, 0)), // Orange Red
    J_SHAPE(new int[][]{{1,0,0},{1,1,1}}, new Color(0, 191, 255)), // Deep Sky Blue
    L_SHAPE(new int[][]{{0,0,1},{1,1,1}}, new Color(255, 215, 0)); // Gold

    private int[][] shape;
    private Color color;
    private static final Random random = new Random();

    Tetromino(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public int[][] getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public static Tetromino randomPiece() {
        return values()[random.nextInt(values().length)];
    }
}
