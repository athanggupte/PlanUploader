/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xdevapps.navigable.planuploader;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.internal.FirebaseAppStore;
import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author athang213
 */
public class Exporter{
    
    private FileWriter fileWriter;
    ImagePanel imagePanel;
    String url = "";

    public Exporter() throws IOException{
        
        JFileChooser fileChooser = new JFileChooser("/home/athang213/");
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            fileWriter = new FileWriter(fileChooser.getSelectedFile());
            System.out.println(fileChooser.getSelectedFile().getName());
        }
    }
    
    public void writeVertices(ArrayList<ImagePanel.Pair> vertices) throws IOException{
        int i = 0;
        fileWriter.write("V\n");
        for(ImagePanel.Pair vertex : vertices)
        {
            fileWriter.write(i+" "+vertex.x+" "+vertex.y+"\n");
            i++;
        }
    }
    
    public void writeEdges(ArrayList<ImagePanel.Pair> edges) throws IOException{
        int i = 0;
        fileWriter.write("E\n");
        for(ImagePanel.Pair edge : edges)
        {
            fileWriter.write(i+" "+edge.x+" "+edge.y+"\n");
            i++;
        }
    }

    public void flush() throws IOException {
        fileWriter.flush();
    }
}
