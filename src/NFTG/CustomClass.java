/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NFTG;



public class CustomClass {
    private String folderpath;
    private int width;
    private int height;
    private int posx;
    private int posy;
    private int rotate;

    // Constructor
    public CustomClass(String folderpath, int width, int height, int posx, int posy, int rotate) {
        this.folderpath = folderpath;
        this.width = width;
        this.height = height;
        this.posx = posx;
        this.posy = posy;
        this.rotate = rotate;
    }

    // Getter and setter for folderpath
    public String getFolderpath() {
        return folderpath;
    }

    public void setFolderpath(String folderpath) {
        this.folderpath = folderpath;
    }

    // Getter and setter for width
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    // Getter and setter for height
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // Getter and setter for posx
    public int getPosx() {
        return posx;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }

    // Getter and setter for posy
    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }

    // Getter and setter for rotate
    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

     @Override
    public String toString() {
        return "CustomClass{" +
                "folderpath='" + folderpath + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", posx=" + posx +
                ", posy=" + posy +
                ", rotate=" + rotate +
                '}';
    }
}
