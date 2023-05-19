package cic.h2h.controller.mes;

import org.springframework.stereotype.Controller;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import nu.pattern.OpenCV;

//import nu.pattern.OpenCV;
@Controller
public class OpenCVController {

    private JLabel imageLabel;
    public static void main(String[] args) {
    	OpenCVController app = new OpenCVController();
        app.runHaarCasCade();
    }

    public void runHaarCasCade() {
        // Load the OpenCV library
        OpenCV.loadShared();
     // Khởi tạo phông chữ
        Scalar textColor = new Scalar(0, 255, 0); // Màu chủ đạo là màu xanh lá cây
        int textFont = Imgproc.FONT_HERSHEY_SIMPLEX;
        double textSize = 1.0;
        int textThickness = 2;
        // Create and set up the JFrame
        JFrame frame = new JFrame("Object Detection on Live Video");
//        MyPanel panel = new MyPanel();
//        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setLayout(new FlowLayout());
        
        // Create and add the image label to the JFrame
        imageLabel = new JLabel();
        frame.add(imageLabel);

        // Open the webcam
        VideoCapture camera = new VideoCapture(1);

        // Create a face detector
        CascadeClassifier faceDetector = new CascadeClassifier("./src/main/resources/haarcascades/dongxu.xml");

        // Process each frame of the video
        Mat frame1 = new Mat();
        while (true) {
            // Capture a frame from the webcam
            camera.read(frame1);

            // Detect faces in the frame
            MatOfRect faceDetections = new MatOfRect();
            int minFaceSize = Math.round(frame1.rows() * 0.1f); 
            faceDetector.detectMultiScale(frame1, 
            faceDetections, 
  	        1.19, 
  	        3, 
  	        Objdetect.CASCADE_SCALE_IMAGE, 
  	        new Size(minFaceSize, minFaceSize), 
  	        new Size()
  	      );
          int numFaces = faceDetections.toArray().length;
  	      Rect[] facesArray = faceDetections.toArray(); 
  	        for(Rect rect : facesArray) { 
  	            //Imgproc.rectangle(loadedImage, face.tl(), face.br(), new Scalar(0, 0, 255), 3); 
//  	            Imgproc.rectangle(loadedImage, new Point(face.x, face.y),
//  	                    new Point(face.x + face.width, face.y + face.height), new Scalar(0, 0, 255),3); 
  	        	Imgproc.rectangle(frame1, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255),3);
//  	        	Imgproc.putText(frame1, Integer.toString(objectCount), 
//  	                    new Point(rect.x, rect.y - 5), Imgproc.FONT_HERSHEY_PLAIN, 
//  	                    1.0, new Scalar(0, 255, 0), 3);
//  	            objectCount++;
  	        	//panel.updateRect(rect.x, rect.y, rect.width, rect.height);
//  	        	Imgproc.putText(frame1, Integer.toString(numFaces) + "Objects", 
//  	        			new Point(25, 25), Imgproc.FONT_HERSHEY_PLAIN,  
//  	                  1.0, new Scalar(0, 255, 0), 3);
  	        } 
  	      Imgproc.putText(frame1, numFaces + " objects", new Point(10, 50), textFont, textSize, textColor, textThickness);
            // Convert the frame to a buffered image for display
            BufferedImage image = matToBufferedImage(frame1);

            // Set the image on the label in the JFrame
            imageLabel.setIcon(new ImageIcon(image));
            
            frame.pack();
            frame.setVisible(true);
        }
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        // Convert the Mat object to an array of bytes
        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] byteArray = new byte[bufferSize];
        mat.get(0, 0, byteArray);

        // Create a buffered image from the array of bytes
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_3BYTE_BGR);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(byteArray, 0, targetPixels, 0, byteArray.length);

        return image;
    }
//    class MyPanel extends JPanel {
//
//        private int x;
//        private int y;
//        private int width;
//        private int height;
//
//        public void updateRect(int x, int y, int width, int height) {
//            this.x = x;
//            this.y = y;
//            this.width = width;
//            this.height = height;
//            repaint();
//        }
//
//        @Override
//        public void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            Graphics2D g2d = (Graphics2D) g;
//            g2d.setColor(Color.RED);
//            g2d.draw(new Rectangle(x, y, width, height));
//        }
//    }
    
}
