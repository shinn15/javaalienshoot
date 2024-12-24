import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class mainfile extends JPanel implements ActionListener,KeyListener{
    //System.out.println("helo2");
    class block{
        int x;
        int y;
        int height;
        int width;
        Image img;
        boolean isalive = true;
        boolean isused= false;

        block(int x,int y, int height,int width,Image img){
            this.x=x;
            this.y=y;
            this.height=height;
            this.width=width;
            this.img=img;
        }
    }

    //inside frame
	int tilesz=32;
    int rows=16;
    int column=16;

    int framewidth=512;
    int frameheight=512;

    //image
    Image shipimg;
    Image alien1;
    Image alien2;
    Image alien3;
    Image alien4;
    ArrayList<Image>alienimgaray;

    //ship
    block ship;

    int shipw=80;//width
    int shiph=50;//height

    int shipx=tilesz*column/2 - tilesz;
    int shipy=frameheight - tilesz*2;

    int velocityshipx=tilesz;

    

    //alien
    ArrayList<block>alienaray;

    int alienw=45;
    int alienh=32;

    int alienx=tilesz;
    int alieny=tilesz;

    int alienrow=2;
    int aliencolm=3;
    int aliencount=0;

    int alienvelocityx=1;

    //buletss
    ArrayList<block> bulletaray;
    int bulletw= 16;
    int bulleth=4;

    int bulletvelocityx= -10;

    int score= 0;
    Timer loopgame;
    boolean isgameover=false;

    //file game
    mainfile(){
    	setPreferredSize(new Dimension(framewidth,frameheight));
    	setBackground(Color.black);
        //movement
        setFocusable(true);
        addKeyListener(this);

        //get img
        shipimg = new ImageIcon(getClass().getResource("./src/shipp1.png")).getImage();
        alien1 = new ImageIcon(getClass().getResource("./src/allien1.png")).getImage();
        alien2 = new ImageIcon(getClass().getResource("./src/allien2.png")).getImage();
        alien3 = new ImageIcon(getClass().getResource("./src/allien3.png")).getImage();


        alienimgaray = new ArrayList<Image>();
        alienimgaray.add(alien1);
        alienimgaray.add(alien2);
        alienimgaray.add(alien3);

        //
        //ship
        ship=new block(shipx,shipy,shipw,shiph,shipimg);
        //aline
        alienaray= new ArrayList<block>();
        //bulet
        bulletaray=new ArrayList<block>();

        //game time
        aliencreate();
        loopgame = new Timer(1000/60,this);
        loopgame.start();
    }

    //draw img
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //ship
        g.drawImage(ship.img,ship.x,ship.y,ship.width,ship.height,null);

        //alien
        for(int i=0; i < alienaray.size(); i++){
            block alien = alienaray.get(i);
            if(alien.isalive){
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height,null);
            }
        }

        //bullet
        g.setColor(Color.blue);
        for(int i=0; i < bulletaray.size(); i++){
            block bullet = bulletaray.get(i);
            if (!bullet.isused){
                g.fillRect(bullet.x,bullet.y,bullet.width,bullet.height);
            }
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.BOLD,35));
        if(isgameover){
            g.drawString("Game Over: "+ String.valueOf(score),10,35);
        }else{
            g.drawString(String.valueOf(score),10,35);
        }
    }

    //move logic
    public void move_logic(){
        //alien
        for (int i=0; i < alienaray.size(); i++){
            block alien = alienaray.get(i);
            if(alien.isalive){
                alien.x += alienvelocityx;

                //border touch
                if (alien.x + alien.width >= framewidth || alien.x <= 0){
                    alienvelocityx *= -1;
                    alien.x += alienvelocityx*2;

                    //alien move down 
                    for (int j=0; j < alienaray.size(); j++){
                        alienaray.get(j).y += alienh;
                    }
                }
                //if alien touch ship
                if(alien.y >= ship.y){
                    isgameover=true;
                }
            }
        }

        //bullet
        for(int i=0;i < bulletaray.size(); i++){
            block bullet = bulletaray.get(i);
            bullet.y += bulletvelocityx;

            //bulet hit alien
            for(int j = 0; j < alienaray.size(); j++){
                block alien = alienaray.get(j);
                if(!bullet.isused && alien.isalive && detectcol(bullet,alien)){
                    bullet.isused = true;
                    alien.isalive = false;
                    aliencount--;
                    score += 50;
                }
            }
        }

        //clean buletshooting
        while(bulletaray.size() > 0 && (bulletaray.get(0).isused || bulletaray.get(0).y < 0)){
            bulletaray.remove(0);
        }

        //add alien next lvl
        if(aliencount == 0){
            score += aliencolm * alienrow * 100;
            //increase alien
            aliencolm = Math.min(aliencolm + 1,column/2 -2);
            alienrow = Math.min(alienrow + 1,rows - 6);

            alienaray.clear();
            bulletaray.clear();
            alienvelocityx = 1;
            aliencreate();
        }


    }

    //generate alien
    public void aliencreate(){  
        Random random = new Random();

        for (int a=0; a < aliencolm; a++){
            for(int c=0; c < alienrow; c++){
                int randomAlienIndex=random.nextInt(alienimgaray.size());
                block alien = new block(
                    alienx + a*alienw,
                    alieny + c*alienh,
                    alienw,
                    alienh,
                    alienimgaray.get(randomAlienIndex)
                );
                alienaray.add(alien);
            }
        }
        aliencount = alienaray.size();
    }

    //colision
    public boolean detectcol(block a, block b){
        return a.x < b.x + b.width &&
               a.x + a.width >b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    //action
    @Override
    public void actionPerformed(ActionEvent e){
        move_logic();
        repaint();
        if(isgameover){
            loopgame.stop();
        }
    }
//
    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyPressed(KeyEvent e){}

    //move keys
    @Override
    public void keyReleased(KeyEvent e){
        //press any keyrestart game 
        if(isgameover){
            ship.x = shipx;
            alienaray.clear();
            bulletaray.clear();
            score=0;
            alienvelocityx=1;
            aliencolm=3;
            alienrow=2;
            isgameover=false;
            aliencreate();
            loopgame.start();
        }
                                            //boundary
        else if(e.getKeyCode()== KeyEvent.VK_A && ship.x - velocityshipx >= 0){
            ship.x -= velocityshipx;
        }else if(e.getKeyCode()== KeyEvent.VK_D && ship.x + ship.width + velocityshipx
                <= framewidth){
            ship.x += velocityshipx;
        }else if(e.getKeyCode() == KeyEvent.VK_SPACE){
            block bullet = new block(ship.x + shipw*15/32, ship.y,bulletw,bulleth,null);
            bulletaray.add(bullet);
        }
    }
}

