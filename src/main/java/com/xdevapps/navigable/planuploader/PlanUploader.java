/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xdevapps.navigable.planuploader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import jdk.nashorn.internal.codegen.CompilerConstants;

/**
 *
 * @author athang213
 */
public class PlanUploader extends JFrame implements MouseListener {
    
    JToolBar toolBar;
    ImagePanel imagePanel;
    JTextPane statusPanel;
    StorageHandler storageHandler;
    int srcPt;
    boolean isDeleteOn = false;
    boolean isSrcSet = false;
    
    public static enum DrawMode {
        REGION, EDGE
    }
    
    private DrawMode drawMode = DrawMode.REGION;
    
    public PlanUploader(){
        JFileChooser fileChooser = new JFileChooser("/home/shubham/Documents/");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("jpeg", "jpg","png","bmp");
        fileChooser.setFileFilter(filter);
            if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
            try {
                imagePanel = new ImagePanel(ImageIO.read(fileChooser.getSelectedFile()));
                getContentPane().add(imagePanel, BorderLayout.CENTER);
            } catch (IOException ex) {
                Logger.getLogger(PlanUploader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
      /*  try {
            storageHandler = new StorageHandler();
        } catch (IOException ex) {
            Logger.getLogger(PlanUploader.class.getName()).log(Level.SEVERE, null, ex);
        }*/
            
        addMouseListener(this);
        
        toolBar = new JToolBar("Tools");
        toolBar.setFloatable(false);
        toolBar.setPreferredSize(new Dimension(1280, 30));
        add(toolBar, BorderLayout.PAGE_START);
        
        JButton toggleButton = new JButton("Draw Edges");
        toolBar.add(toggleButton);
        
        JButton saveButton = new JButton("Save Data");
        toolBar.add(saveButton);
        
        JButton resetButton = new JButton("Reset");
        toolBar.add(resetButton);
        
        JButton deleteButton = new JButton("Delete");
        toolBar.add(deleteButton);
        
        statusPanel = new JTextPane();
        statusPanel.setText("src:");
        toolBar.add(statusPanel);
        
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                toggleMode();
                if(toggleButton.getText().contains("Region"))
                    toggleButton.setText("Draw Edges");
                else if(toggleButton.getText().contains("Edges"))
                    toggleButton.setText("Draw Regions");
            }
        });
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try{
                    imagePanel.writeToFile();
                    JOptionPane.showMessageDialog(getContentPane(), "Saved Successfully");
                }catch(IOException e){
                    e.printStackTrace();
                }
                
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                imagePanel.reset();
                repaint();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	toggleDelete(); 
            }
        });
       
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setVisible(true);
    }
    
    public DrawMode getDrawMode() {
        return drawMode;
    }
    
    
    @Override
    public void mouseClicked(MouseEvent me) {
        //imagePanel.drawPoint(me.getX(), me.getY());
        //repaint();
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int x = me.getX(); 
        int y = me.getY()-58;
        
        if(isDeleteOn == true){	
        	
        	imagePanel.deletePolygon(x,y);	
        	
        	repaint();
        }
        else {
	        if(drawMode == DrawMode.REGION){
	            if(me.getButton() == MouseEvent.BUTTON1) {
	                imagePanel.drawPoint(x, y);
	                repaint();
	            } else if(me.getButton() == MouseEvent.BUTTON3) {
	                imagePanel.drawPolygon();
	                repaint();
	            }
	        } else if(drawMode == DrawMode.EDGE){
	            if(me.getButton() == MouseEvent.BUTTON1) {
	                int closestPoint = imagePanel.isCloseTo(x, y);
	                statusPanel.setText("closest:" + closestPoint);
	                if(closestPoint != -1){
	                    if(isSrcSet) {
	                        imagePanel.drawEdge(srcPt, closestPoint);
	                    }
	                    else {
	                        srcPt = closestPoint;
	                        statusPanel.setText("src:" + srcPt);
	                    }
	                    isSrcSet = !isSrcSet;
	                    repaint();
	                }
	            } else if(me.getButton() == MouseEvent.BUTTON3) {
	                
	            }
	        } 
        }   
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void toggleMode() {
        if(drawMode == DrawMode.REGION) {
            drawMode = DrawMode.EDGE;
        }
        else {
            drawMode = DrawMode.REGION;
        }
    }
    
    void toggleDelete() {
    	if(isDeleteOn == true)
    	{
    		isDeleteOn = false;
    	}
    	else {
    		isDeleteOn = true;
    	}
    }
    
    public static void main(String[] args) {
        PlanUploader planUploader = new PlanUploader();
    }
}
