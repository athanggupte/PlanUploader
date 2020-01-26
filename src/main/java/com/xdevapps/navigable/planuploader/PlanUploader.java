/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xdevapps.navigable.planuploader;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
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
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
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
import javax.swing.plaf.ButtonUI;

/**
 *
 * @author athang213
 */
public class PlanUploader extends JFrame implements MouseListener {
    
    JToolBar toolBar;
    ImagePanel imagePanel;
    JTextPane statusPanel;
    StorageHandler storageHandler;
    int x_pt;
    int y_pt;
    int flag = 0;
    boolean isSrcSet = false;
    
    public static enum DrawMode {
        REGION, EDGE,SETAXIS,DELETE
    }
    
    private DrawMode drawMode = DrawMode.REGION;
    
    public PlanUploader(){
        
        JFileChooser fileChooser = new JFileChooser("/home/athang213/Downloads/");
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
        
        try {
                storageHandler = new StorageHandler();
        } catch (IOException ex) {
            Logger.getLogger(PlanUploader.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        addMouseListener(this);
        
        toolBar = new JToolBar("Tools");
        toolBar.setFloatable(false);
        toolBar.setPreferredSize(new Dimension(1280, 30));
        add(toolBar, BorderLayout.PAGE_START);
        
        JButton setOriginButton = new JButton("Set Origin");
        toolBar.add(setOriginButton);
        
        JButton drawRegionButton = new JButton("Draw Region");
        toolBar.add(drawRegionButton);
        
        JButton drawEdgeButton = new JButton("Draw Edges");
        toolBar.add(drawEdgeButton);
        
        JButton saveButton = new JButton("Save Data");
        toolBar.add(saveButton);
        
        JButton resetButton = new JButton("Reset");
        toolBar.add(resetButton);
        
        JButton deleteButton = new JButton("Delete");
        toolBar.add(deleteButton);
        
        statusPanel = new JTextPane();
        statusPanel.setText("src:");
        toolBar.add(statusPanel);
        
        drawEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                toggleMode("EDGE");
            }
        });
        
        drawRegionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                toggleMode("REGION");
            }
        });
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try{
                    byte[] bytes = imagePanel.writeToFile();
                    storageHandler.uploadFile(bytes, bytes.length);
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
                
                /*AbstractButton b = (AbstractButton) ae.getSource();
                ButtonModel m = b.getModel();
                m.setPressed(true);
                 if (! b.isFocusOwner())
                    b.requestFocus();*/
                imagePanel.reset();
                repaint();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            	toggleMode("DELETE"); 
            }
        });
       
        setOriginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                toggleMode("SETAXIS");
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        int y = me.getY()-30;
        
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
       
	       // statusPanel.setText("closest:" + closestPoint);
                imagePanel.highlightPt(x, y);
                
	        imagePanel.drawEdge(x, y);
	        repaint();
            } 
	} else if(drawMode == DrawMode.SETAXIS){
            imagePanel.setAxis(x,y);
            repaint();
        } else if(drawMode == DrawMode.DELETE){
        //    imagePanel.deletePolygon(x,y);	
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void toggleMode(String mode) {
       if (mode.equals("EDGE")) {
            drawMode = DrawMode.EDGE;
        }else if (mode.equals("REGION")) {
            drawMode = DrawMode.REGION;
        }else if (mode.equals("SETAXIS")) {
            drawMode = DrawMode.SETAXIS; 
        }else if (mode.equals("DELETE")) {
            drawMode = DrawMode.DELETE;   
        } 
    }
    
    
    public static void main(String[] args) {
        PlanUploader planUploader = new PlanUploader();
    }
}
