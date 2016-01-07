package processingtest;

import processing.core.PApplet;

/**
 * Testing out features in processing.
 *
 * @author Kristian Honningsvag.
 */
public class Sketch extends PApplet {

    int windowSize = 20;

    @Override
    public void setup() {
    }

    @Override
    public void settings() {
        size(500, 500);
    }

    @Override
    public void draw() {
        // House.
        if (mousePressed) {
            fill(255, 0, 0);
        } else {
            fill(0, 0, 255);
        }
        rect(mouseX, mouseY, 80, -100);

        // Roof.
        if (mousePressed) {
            fill(0, 0, 255);
        } else {
            fill(255, 0, 0);
        }
        triangle(mouseX - 20, mouseY - 100, mouseX + 120, mouseY - 100, mouseX + 50, mouseY - 140);

        // Door.
        if (mousePressed) {
            fill(255);
        } else {
            fill(0);
        }
        rect(mouseX + 20, mouseY, 20, -35);

        // Window.
        if (mousePressed) {
            fill(255);
        } else {
            fill(0);
        }
        rect(mouseX + 10, mouseY - 50, windowSize, -windowSize);

        // Window.
        if (mousePressed) {
            fill(255);
        } else {
            fill(0);
        }
        rect(mouseX + 55, mouseY - 50, windowSize, -windowSize);
    }
}
