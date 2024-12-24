import javax.swing.*;


public class index {
    public static void main(String[] args) throws Exception{
        //System.out.println("helo1");

        //window    
        int tilesz=32;
        int rows=16;
        int column=16;

        int framewidth=512;
        int frameheight=512;

        JFrame frame=new JFrame("Alien Shooter");
        //frame.setVisible(true);
        frame.setSize(framewidth,frameheight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //import filegame
        mainfile mainfiles = new mainfile();
        frame.add(mainfiles);
        frame.pack();
        mainfiles.requestFocus();
        frame.setVisible(true);

    }  
}
