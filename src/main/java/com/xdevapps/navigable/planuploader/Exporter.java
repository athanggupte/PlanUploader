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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    private FileReader fileReader;
    ImagePanel imagePanel;
    private String fileName;
    String url = "";

    public Exporter() throws IOException{
        
        JFileChooser fileChooser = new JFileChooser("/home/shubham/");
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            fileWriter = new FileWriter(fileChooser.getSelectedFile());
            System.out.println(fileChooser.getSelectedFile().getName());
        }
        fileName = fileChooser.getSelectedFile().getPath();
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
    
    public byte[] exportBytes() throws FileNotFoundException,IOException{
    	FileInputStream fs = new FileInputStream(fileName);
    	byte[] buffer = new byte[1024];
    	int size = fs.read(buffer);
    	System.out.println("Buffer : "+buffer.toString()+"Size : "+size+"");
    	return buffer;
    }
}
