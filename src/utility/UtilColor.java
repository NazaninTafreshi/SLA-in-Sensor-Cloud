/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 *
 * @author Nazanin Tafreshi
 */
public class UtilColor {

    private final ArrayList<Color> colors;

    public UtilColor() {
        colors = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/res/Color.txt")));) {
            bf.lines().forEach(line -> {
                colors.add(Color.web(line));
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
    public Color getColor(int index) {
        return colors.get(index);
    }

    
}
