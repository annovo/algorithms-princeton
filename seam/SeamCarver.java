/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

import java.awt.Color;

public class SeamCarver {
    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL = false;
    private Picture p;
    private double[] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("args cant be null");
        this.p = new Picture(picture);
        energy = new double[p.width() * p.height()];
        fillEnergy();
    }

    // current picture
    public Picture picture() {
        return new Picture(p);
    }

    // width of current picture
    public int width() {
        return this.p.width();
    }

    // height of current picture
    public int height() {
        return this.p.height();
    }

    private void fillEnergy() {
        for (int i = 0; i < p.height(); i++) {
            for (int j = 0; j < p.width(); j++) {
                energy[i * p.width() + j] = findEnergy(j, i);
            }
        }
    }

    private double findEnergy(int x, int y) {
        if (x > 0 && x + 1 < p.width() && y > 0 && y + 1 < p.height()) {
            Color rgbxF = p.get(x + 1, y);
            Color rgbxL = p.get(x - 1, y);
            int rx = rgbxF.getRed() - rgbxL.getRed();
            int gx = rgbxF.getGreen() - rgbxL.getGreen();
            int bx = rgbxF.getBlue() - rgbxL.getBlue();
            double ex = Math.pow(rx, 2) + Math.pow(gx, 2) + Math.pow(bx, 2);

            Color rgbyF = p.get(x, y + 1);
            Color rgbyL = p.get(x, y - 1);
            int ry = rgbyF.getRed() - rgbyL.getRed();
            int gy = rgbyF.getGreen() - rgbyL.getGreen();
            int by = rgbyF.getBlue() - rgbyL.getBlue();
            double ey = Math.pow(ry, 2) + Math.pow(gy, 2) + Math.pow(by, 2);

            return Math.sqrt(ex + ey);
        }
        return 1000;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= p.width() || y < 0 || y >= p.height())
            throw new IllegalArgumentException("args cant be null");
        return energy[y * width() + x];
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] a = new int[p.width()];
        SPimg sp = new SPimg(energy, p.width(), HORIZONTAL);
        Stack<Integer> s = sp.getPath(p.width(), HORIZONTAL);

        int t = 0;
        while (!s.isEmpty())
            a[t++] = s.pop();
        return a;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] a = new int[p.height()];
        SPimg sp = new SPimg(energy, p.width(), VERTICAL);
        Stack<Integer> s = sp.getPath(p.width(), VERTICAL);

        int t = 0;
        while (!s.isEmpty())
            a[t++] = s.pop();
        return a;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (p.height() <= 1)
            throw new IllegalArgumentException("pic too small");
        if (seam == null || seam.length != p.width())
            throw new IllegalArgumentException("not valid seam");

        Picture newPic = new Picture(width(), height() - 1);
        energy = new double[width() * (height() - 1)];
        for (int j = 0; j < p.width(); j++) {
            if (seam[j] < 0 || seam[j] >= p.height())
                throw new IllegalArgumentException("out of range");
            if (j > 0 && (seam[j] - seam[j - 1] > 1 || seam[j] - seam[j - 1] < -1))
                throw new IllegalArgumentException("out of range");
            int delta = 0;
            for (int i = 0; i < p.height(); i++) {
                if (seam[j] == i)
                    delta = -1;
                else
                    newPic.set(j, i + delta, p.get(j, i));
            }
        }
        p = newPic;
        fillEnergy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (p.width() <= 1)
            throw new IllegalArgumentException("pic too small");
        if (seam == null || seam.length != p.height())
            throw new IllegalArgumentException("not valid seam");

        Picture newPic = new Picture(width() - 1, height());
        energy = new double[height() * (width() - 1)];
        for (int j = 0; j < p.height(); j++) {
            if (seam[j] < 0 || seam[j] >= p.width())
                throw new IllegalArgumentException("out of range");
            if (j > 0 && (seam[j] - seam[j - 1] > 1 || seam[j] - seam[j - 1] < -1))
                throw new IllegalArgumentException("out of range");
            int delta = 0;
            for (int i = 0; i < p.width(); i++) {
                if (seam[j] == i)
                    delta = -1;
                else
                    newPic.set(i + delta, j, p.get(i, j));
            }
        }
        p = newPic;
        fillEnergy();
    }

    // empty because i want to
    public static void main(String[] args) {

    }
}
