/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NFTG;


import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.tree.TreePath;

public class NFTNVN extends JFrame {
   CustomClass  cc;static String newfolder;
    ArrayList<CustomClass> data=new ArrayList();
    private JTree folderTree;
    private JLabel imageLabel;
     private JLabel imageLabel2;
     private JLabel imageLabel3;
      private Image originalImage,capImage;
 static  int status=0; static Image bgimage;
 JTextField tposx,tposy,tsizex,tsizey,trotate;
static String folderpath; boolean foldercheck;
    public NFTNVN(String folderPath) {
        super("Folder Explorer");

        // Create the root node for the tree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new File(folderPath));

        // Build the tree
        buildTree(root, new File(folderPath));

        // Create the tree model
        DefaultTreeModel treeModel = new DefaultTreeModel(root);

        // Create the JTree with the tree model
        folderTree = new JTree(treeModel);

        // Add a TreeSelectionListener to handle tree selection events
  folderTree.addTreeSelectionListener(new TreeSelectionListener() {
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        // Get the selected node
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) folderTree.getLastSelectedPathComponent();

        if (selectedNode != null) {
            // Check if the selected node represents a file or folder
            File selectedFile = (File) selectedNode.getUserObject();
            if (selectedFile.isDirectory()) {
                // If it's a directory, print the full path
                String folderPath = selectedFile.getAbsolutePath();
                //System.out.println("Selected Folder Path: " + folderPath);

                // Additional actions related to the folder path can be performed here
            } else if (isImageFile(selectedFile)) {
                // If it's an image file, find and print the parent folder path
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                if (parentNode != null) {
                    File parentFolder = (File) parentNode.getUserObject();
                    folderpath = parentFolder.getAbsolutePath();
                   // System.out.println("Parent Folder Path: " +folderpath);

                    // Additional actions related to the parent folder path can be performed here
                }
            }

            // Rest of your code...

            if (isImageFile(selectedFile)) {
                // If it's an image file, display the image in the JLabel
                displayImage(selectedFile);
            } else {
                // If it's not an image file, clear the image label
                clearImageLabel();
            }
        }
    }
});


        // Create the JLabel for image preview
        JPanel middlepane =new JPanel();
        middlepane.setLayout(new GridLayout(2, 1));
        
        imageLabel = new JLabel();
        imageLabel2 = new JLabel();
        Border border = BorderFactory.createLineBorder(Color.BLUE);
        imageLabel2.setBorder(border);
        imageLabel2.setBackground(Color.YELLOW);
        imageLabel2.setPreferredSize(new Dimension(300, 300));
        
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel2.setHorizontalAlignment(JLabel.CENTER);
        
        
       
        
        middlepane.add(imageLabel);
         middlepane.add(imageLabel2);
         // Enable drag and drop for the source label
           imageLabel.setTransferHandler(new TransferHandler("icon") {
                @Override
                protected Transferable createTransferable(JComponent c) {
                    return new Transferable() {
                        @Override
                        public DataFlavor[] getTransferDataFlavors() {
                            return new DataFlavor[]{DataFlavor.imageFlavor};
                        }

                        @Override
                        public boolean isDataFlavorSupported(DataFlavor flavor) {
                            return flavor.equals(DataFlavor.imageFlavor);
                        }

                        @Override
                        public Object getTransferData(DataFlavor flavor) {
                            return ((ImageIcon) imageLabel.getIcon()).getImage();
                        }
                    };
                }
            });

            // Enable drop for the destination label
           imageLabel2.setTransferHandler(new TransferHandler("icon") {
                @Override
                public boolean canImport(TransferHandler.TransferSupport support) {
                    return support.isDataFlavorSupported(DataFlavor.imageFlavor);
                }

                @Override
                public boolean importData(TransferHandler.TransferSupport support) {
                    if (!canImport(support)) {
                        return false;
                    }

                    try {
                        Image capimage = (Image) support.getTransferable().getTransferData(DataFlavor.imageFlavor);
                        // Assuming your JLabel is named 'myLabel'
                        if (imageLabel2.getIcon() != null) {
                            // The JLabel has an image
                             imageLabel2.setIcon(new ImageIcon(capimage));
                              imageLabel2.setIcon(new ImageIcon(putCap(bgimage,((ImageIcon)imageLabel.getIcon()).getImage(),100,100,50,50,0)));
                           
                              imageLabel2.repaint(); // or imageLabel2.revalidate();
                        } else {
                            // The JLabel does not have an image
                            imageLabel2.setIcon(new ImageIcon(capimage));
                            bgimage=((ImageIcon)imageLabel2.getIcon()).getImage();
                            
                            cc=new CustomClass(folderpath,0,0,500,500,0);
                             data.add(cc);
                        }
                       // 
                        
                       // imageLabel.setIcon(null);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            });

            // Add a mouse listener to handle the drag gesture
          imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    JComponent c = (JComponent) e.getSource();
                    TransferHandler handler = c.getTransferHandler();
                    handler.exportAsDrag(c, e, TransferHandler.COPY);
                }
            });
        JPanel rightbar =new JPanel();
        rightbar .setLayout(new GridLayout(14, 1));
        JLabel posx=new JLabel("Position X Axis");
         tposx=new JTextField("100");
        tposx.setPreferredSize(new Dimension(100, 25));
         mouselistner(tposx);
         rightbar.add(posx);
          rightbar.add(tposx);
          
          
           JLabel posy=new JLabel("Position Y Axis");
        tposy=new JTextField("100");
        tposy.setPreferredSize(new Dimension(100, 25));
         mouselistner(tposy);
         rightbar.add(posy);
          rightbar.add(tposy);
          
           JLabel sizex=new JLabel("Width:");
        tsizex=new JTextField("100");
        tsizex.setPreferredSize(new Dimension(100, 25));
         mouselistner(tsizex);
         rightbar.add(sizex);
          rightbar.add(tsizex);
          
          
           JLabel sizey=new JLabel("Height:");
        tsizey=new JTextField("100");
        tsizey.setPreferredSize(new Dimension(100, 25));
         mouselistner(tsizey);
         rightbar.add(sizey);
          rightbar.add(tsizey);
          
          JLabel rotate=new JLabel("Rotate:");
        trotate=new JTextField("0");
        trotate.setPreferredSize(new Dimension(100, 25));
         mouselistner(trotate);
         rightbar.add(rotate);
          rightbar.add(trotate);
          
         
          JButton save=new JButton("Save");
          
         save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             
             
              if(folderchecking(folderpath))
              {
                   bgimage=((ImageIcon)imageLabel2.getIcon()).getImage();
                   cc=new CustomClass(folderpath,Integer.parseInt(tsizex.getText()),Integer.parseInt(tsizey.getText()),Integer.parseInt(tposx.getText()),Integer.parseInt(tposy.getText()),Integer.parseInt(trotate.getText()));
                   data.add(cc);
              }
             
            }
        });
          rightbar.add(new JLabel(" "));
           rightbar.add(save);
          rightbar.add(new JLabel(" "));
           JButton generate=new JButton("Generate");
           generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int k=0;
                for(int i=0;i<data.size()-1;i++)

                {
                    
                    if(i==0)
                    {
                        newfolder=data.get(i).getFolderpath()+"new";
                        generateimage(k++, newfolder,data.get(i),data.get(i+1));
                    }
                    else{
                        if(i==data.size()-1)
                        {
                             generateimage(k++,data.get(i+1).getFolderpath()+"new", newfolder,data.get(i+1)); 
                        }
                        else
                        {
                             generateimage(k++,data.get(i+1).getFolderpath()+"All image", newfolder,data.get(i+1)); 
                        }
                       
                    }
                }
                
         JOptionPane.showMessageDialog(null, "Successfully Image generated");
        }
        });
            rightbar.add(generate);
       // Create a GridBagLayout manager
        setLayout(new GridBagLayout());

        // Create constraints for components
        GridBagConstraints constraints = new GridBagConstraints();

        // Add JScrollPane for JTree
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.2; // Custom width for the first column
        constraints.weighty = 1.0; // Vertical weight to fill the height
        constraints.insets = new Insets(10, 10, 10, 10); // Top, left, bottom, right margins
        add(new JScrollPane(folderTree), constraints);

        // Add JLabel for imageLabel
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.6; // Custom width for the second column
        constraints.weighty = 1.0; // Vertical weight to fill the height
        constraints.insets = new Insets(10, 10, 10, 10); // Top, left, bottom, right margins
        add( middlepane, constraints);

        // Add JLabel for imageLabel2
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.2; // Custom width for the third column
        constraints.weighty = 1.0; // Vertical weight to fill the height
        constraints.insets = new Insets(10, 10, 10, 10); // Top, left, bottom, right margins
        add( rightbar, constraints);

        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize(screen.width - 20, screen.height - 70);
        setLocationRelativeTo(null);
        setVisible(true);
    }
   public void generateimage(int k,String path,String cc1,CustomClass cc2)
    {
        newfolder=path;
        File folder1 = new File(cc1);
        File folder2 = new File(cc2.getFolderpath());
        File outputFolder = new File(path);

        // Create the output folder if it doesn't exist
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        File[] images1 = folder1.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png")|| name.toLowerCase().endsWith(".jpeg"));
        File[] images2 = folder2.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png")|| name.toLowerCase().endsWith(".jpeg"));
      
        if (images1 != null && images2 != null) {
            for (File image1 : images1) {
                for (File image2 : images2) {
                    try {
                        BufferedImage img1 = ImageIO.read(image1);
                        BufferedImage img2 = ImageIO.read(image2);
                        
                        putCapsave(path,k++,img1,img2,cc2.getWidth(),cc2.getHeight(),cc2.getPosx(),cc2.getPosy(),cc2.getRotate());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } 
    }
    public void generateimage(int k,String path,CustomClass cc1,CustomClass cc2)
    {
        File folder1 = new File(cc1.getFolderpath());
        File folder2 = new File(cc2.getFolderpath());
        File outputFolder = new File(path);

        // Create the output folder if it doesn't exist
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        File[] images1 = folder1.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png")|| name.toLowerCase().endsWith(".jpeg"));
        File[] images2 = folder2.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png")|| name.toLowerCase().endsWith(".jpeg"));
      
        if (images1 != null && images2 != null) {
            for (File image1 : images1) {
                for (File image2 : images2) {
                    try {
                        BufferedImage img1 = ImageIO.read(image1);
                        BufferedImage img2 = ImageIO.read(image2);
                        
                        putCapsave(path,k++,img1,img2,cc2.getWidth(),cc2.getHeight(),cc2.getPosx(),cc2.getPosy(),cc2.getRotate());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } 
    }
    public boolean folderchecking(String folderpath)
    {

         for (CustomClass customObject : data) {
                     if(folderpath.equals(customObject.getFolderpath()))
                {
                    return false;//value exist
                }
                }
        return true;//value not exist
    }
 public void mouselistner(JTextField tt)
    {
         tt.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // This method is called when the mouse is clicked
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // This method is called when the mouse button is pressed
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // This method is called when the mouse button is released
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // This method is called when the mouse enters the component (JTextField)
               // handleMouseEnter();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // This method is called when the mouse exits the component (JTextField)
                if(imageLabel2.getIcon()!=null &&imageLabel.getIcon()!=null)
                {
                  imageLabel2.setIcon(new ImageIcon(putCap(bgimage,((ImageIcon)imageLabel.getIcon()).getImage(),Integer.parseInt(tsizex.getText()),Integer.parseInt(tsizey.getText()),Integer.parseInt(tposx.getText()),Integer.parseInt(tposy.getText()),Integer.parseInt(trotate.getText()))));  
                }

            }
           });
         tt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This method is called when Enter key is pressed in the JTextField
               if(imageLabel2.getIcon()!=null &&imageLabel.getIcon()!=null)
                {
                  imageLabel2.setIcon(new ImageIcon(putCap(bgimage,((ImageIcon)imageLabel.getIcon()).getImage(),Integer.parseInt(tsizex.getText()),Integer.parseInt(tsizey.getText()),Integer.parseInt(tposx.getText()),Integer.parseInt(tposy.getText()),Integer.parseInt(trotate.getText()))));  
                }
               
            }
         });
         tt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
               
            }
         });
         
           AbstractDocument document = (AbstractDocument) tt.getDocument();
           document.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (isNumeric(text)) {
                    super.insertString(fb, offset, text, attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (isNumeric(text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isNumeric(String text) {
              return text.matches("-?\\d*\\.?\\d*");
            }
        });
    }
    private void buildTree(DefaultMutableTreeNode parent, File file) {
        if (file.isDirectory()) {
            // If it's a directory, add a node for the directory
            DefaultMutableTreeNode directoryNode = new DefaultMutableTreeNode(file);
            parent.add(directoryNode);

            // Recursively build the tree for the contents of the directory
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    buildTree(directoryNode, subFile);
                }
            }
        } else {
            // If it's a file, add a leaf node for the file
            parent.add(new DefaultMutableTreeNode(file));
        }
    }

    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }

    private void displayImage(File imageFile) {
        // Load the image and display it in the JLabel
        ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
        Image scaledImage = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        
       imageLabel.setIcon(new ImageIcon(scaledImage));
 
         
    }

    private void clearImageLabel() {
        // Clear the image label
        imageLabel.setIcon(null);
    }
    
   private BufferedImage putCap(Image org, Image cap, int width, int height, int x, int y, int rotate) {
    BufferedImage bufferedImage = null;

    if (org != null && cap != null) {
        int originalWidth = org.getWidth(null);
        int originalHeight = org.getHeight(null);

        bufferedImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(org, 0, 0, null);

        int capWidth = cap.getWidth(null);
        int capHeight = cap.getHeight(null);

        // Convert ToolkitImage to BufferedImage
        BufferedImage capBufferedImage = new BufferedImage(capWidth, capHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D capGraphics = capBufferedImage.createGraphics();
        capGraphics.drawImage(cap, 0, 0, null);
        capGraphics.dispose();

        // Resize the cap image using direct width and height values
        AffineTransform resizeTransform = AffineTransform.getScaleInstance((double) width / capWidth, (double) height / capHeight);
        AffineTransformOp resizeOp = new AffineTransformOp(resizeTransform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage resizedCapImage = resizeOp.filter(capBufferedImage, null);

        // Rotate the resized cap image
        double rotationAngle = Math.toRadians(rotate);
        AffineTransform rotateTransform = new AffineTransform();
        rotateTransform.rotate(rotationAngle, resizedCapImage.getWidth() / 2.0, resizedCapImage.getHeight() / 2.0);

        // Create a new BufferedImage from the rotated and resized capImage
        BufferedImage rotatedResizedCapImage = new BufferedImage(resizedCapImage.getWidth(), resizedCapImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dCap = rotatedResizedCapImage.createGraphics();
        g2dCap.drawImage(resizedCapImage, rotateTransform, null);
        g2dCap.dispose();

        // Calculate the position to place the rotated and resized cap
        int capX = x;
        int capY = y;

        // Draw the rotated and resized cap image onto the original image
        g2d.drawImage(rotatedResizedCapImage, capX, capY, null);
        g2d.dispose();

       
    } else {
        JOptionPane.showMessageDialog(null, "Please select an original image and a cap image first.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    return bufferedImage;
}



private void putCapsave(String dir,int i,Image org, Image cap, int width,int height, int x, int y, int rotate) {
    BufferedImage bufferedImage = null;

    if (org != null && cap != null) {
        int originalWidth = org.getWidth(null);
        int originalHeight = org.getHeight(null);

        bufferedImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(org, 0, 0, null);

        int capWidth = cap.getWidth(null);
        int capHeight = cap.getHeight(null);

        // Convert ToolkitImage to BufferedImage
        BufferedImage capBufferedImage = new BufferedImage(capWidth, capHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D capGraphics = capBufferedImage.createGraphics();
        capGraphics.drawImage(cap, 0, 0, null);
        capGraphics.dispose();

        // Resize the cap image using direct width and height values
        AffineTransform resizeTransform = AffineTransform.getScaleInstance((double) width / capWidth, (double) height / capHeight);
        AffineTransformOp resizeOp = new AffineTransformOp(resizeTransform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage resizedCapImage = resizeOp.filter(capBufferedImage, null);

        // Rotate the resized cap image
        double rotationAngle = Math.toRadians(rotate);
        AffineTransform rotateTransform = new AffineTransform();
        rotateTransform.rotate(rotationAngle, resizedCapImage.getWidth() / 2.0, resizedCapImage.getHeight() / 2.0);

        // Create a new BufferedImage from the rotated and resized capImage
        BufferedImage rotatedResizedCapImage = new BufferedImage(resizedCapImage.getWidth(), resizedCapImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dCap = rotatedResizedCapImage.createGraphics();
        g2dCap.drawImage(resizedCapImage, rotateTransform, null);
        g2dCap.dispose();

        // Calculate the position to place the rotated and resized cap
        int capX = x;
        int capY = y;

        // Draw the rotated and resized cap image onto the original image
        g2d.drawImage(rotatedResizedCapImage, capX, capY, null);
        g2d.dispose();

        //Save the processed image to a unique filename
        saveImage(bufferedImage, dir,"img"+i+ ".png");
    } else {
        JOptionPane.showMessageDialog(this, "Please select an original image and a cap image first.", "Error", JOptionPane.ERROR_MESSAGE);
    }
 
}
private void saveImage(BufferedImage image, String directoryPath, String fileName) {
        try {
            Path directory = Paths.get(directoryPath);
            Path filePath = directory.resolve(fileName);

            // Create the directory if it doesn't exist
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            ImageIO.write(image, "png", filePath.toFile());
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
public static String loadfoalder()
{
   
    folderpath=null;
    // Prompt the user to choose a folder using JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            // Get the selected folder path
            File selectedFolder = fileChooser.getSelectedFile();
            folderpath = selectedFolder.getAbsolutePath();

        } else {
            System.out.println("User canceled folder selection.");
        }
        
       return folderpath; 
}
    public static void main(String[] args) {
        
         SwingUtilities.invokeLater(() -> {
            // Create a panel with some components
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(500, 500));

            JButton jb = new JButton("Choose Folder");
           jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.getRootFrame().dispose();
                String gg=loadfoalder();
                if(gg!=null)
                {
                   // Handle OK button action if needed
                 SwingUtilities.invokeLater(() -> new NFTNVN(gg)); 
                }
            }
        });
            // Set layout manager to FlowLayout with no alignment
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 150));
            
            // Add an empty border to create space around the button
            jb.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

            panel.add(jb);

            // Show the JOptionPane with the custom panel
            int result = JOptionPane.showConfirmDialog(null, panel, "Custom Panel Example",
                    JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // Check the result
            if (result == JOptionPane.OK_OPTION) {
                JOptionPane.getRootFrame().dispose();
                String gg=loadfoalder();
                if(gg!=null)
                {
                   // Handle OK button action if needed
                 SwingUtilities.invokeLater(() -> new NFTNVN(gg)); 
                }
                
            } else {
                System.out.println("User cancelled the operation.");
            }
        });
    }
    }



