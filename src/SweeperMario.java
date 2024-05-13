import java.awt.Component;
import java.awt.Font;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author hp
 */
public class SweeperMario extends javax.swing.JFrame {
    Clip startingSound;
    Clip coinEffect;
    int noOfBombs;
    int timeLimit;  
    int[][] symbols;
    TimerController timerController;

    /**
     * Creates new form SweeperMario
     */
    public SweeperMario() {
        noOfBombs = 1;
        timeLimit = 60;
        initComponents();
        symbols = new int[4][4];
        disablePanel(gamePanel);
        disablePanel(difficultyPanel);
        disablePanel(exitPanel);
        startingSound = soundEffects("/my/audio/startSound.wav");

    try {
        InputStream fontStream = getClass().getResourceAsStream("/my/resources/super-mario-bros-nes.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(10f);
        jLabelScore.setFont(font);
        jLabelTime.setFont(font);
        jLabelScoreChange.setFont(font);
        jLabelTimeChange.setFont(font);
        credits.setFont(font);
        credits1.setFont(font);
        credits2.setFont(font);
        credits3.setFont(font);
        jLabel1.setFont(font.deriveFont(13f));
        jLabel2.setFont(font.deriveFont(13f));
        jLabel3.setFont(font.deriveFont(13f));
        jLabel10.setFont(font.deriveFont(13f));
        jLabel11.setFont(font.deriveFont(13f));
        jLabel12.setFont(font.deriveFont(13f));
        jLabel16.setFont(font.deriveFont(13f));
        jLabel17.setFont(font.deriveFont(13f));
        jLabel18.setFont(font.deriveFont(13f));
        difficultyInfo.setFont(font.deriveFont(10f));
    } catch (Exception e) {
        e.printStackTrace();
    }
        setLocationRelativeTo(null);
        
    }
    
    int score = 0;
    //Loads Images
    Icon iconstone = new ImageIcon(getClass().getResource("/my/resources/goomba.png"));
    Icon iconheart = new ImageIcon(getClass().getResource("/my/resources/coin.png"));
    //All mario Skins
    Icon[] icons = {
        new ImageIcon(getClass().getResource("/my/resources/mario_fire.gif")),
        new ImageIcon(getClass().getResource("/my/resources/mario_frog.gif")),
        new ImageIcon(getClass().getResource("/my/resources/mario_raccoon.gif")),
        new ImageIcon(getClass().getResource("/my/resources/mario_raccoon2.gif")),
        new ImageIcon(getClass().getResource("/my/resources/mario_white.gif")),
        new ImageIcon(getClass().getResource("/my/resources/mario_default.gif")),
        new ImageIcon(getClass().getResource("/my/resources/mario_powerup.gif"))
    };
    
    //Assigns three bombs in three random places
    public static void assignBomb(int[][] array, int noOfBombs) {
        //Sets the initial value of all elements to zero
        for (int i = 0; i<array.length; i++) {
            for(int j = 0; j<array.length; j++) {
                array[i][j] = 0;
            }
        }
        //Sets random buttons as a bomb
        while (noOfBombs != 0) {   
            int x = (int)Math.floor(Math.random()*4);
            int y = (int)Math.floor(Math.random()*4);
            //Avoids assignment duplicates;
            if (array[x][y] == 1) {
                continue;
            }
            array[x][y] = 1;
            noOfBombs--;
        }
        
        for (int i = 0; i<array.length; i++) {
            for (int k = 0; k<array.length; k++) {
                System.out.print(array[i][k]);
            }
            System.out.println();
        }
        System.out.println("-------------------------------------");
    }
    //Clicked function  
    void clicked(int x, int y, JButton button) {
        //If the button clicked contains a goomba, game over
        if (symbols[x][y] == 1) {
            button.setIcon(iconstone);
            timerController.stop();
            loseGame();
            return;
        }
        score++;
        //If the user has completely collected all coins.
        if (score == (16 - noOfBombs)) {
            button.setIcon(iconheart);   
            button.setEnabled(false);
            timerController.stop();
            winGame();
            return;
        }
        button.setIcon(iconheart);
        jLabelScoreChange.setText(""+score);
        button.setDisabledIcon(iconheart);
        button.setEnabled(false);
        coinEffect = soundEffects("/my/audio/coin.wav");
        } 
    //Randomizes Skin
    void randomizeSkin() {
        mario.setIcon(icons[6]);
        int skinVal = (int)Math.floor(Math.random()*5);
        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        mario.setIcon(icons[skinVal]);
                    }
                }, 
                800 
        );
    }
   
   //Activates Panel
   static void activatePanel(java.awt.Container container) {
    Component[] components = container.getComponents();
    for (Component component : components) {
        component.setEnabled(true);
        if (component instanceof java.awt.Container) {
            activatePanel((java.awt.Container) component);
        }
    }
    container.setVisible(true);
    }
   
   //Disables Panel
   static void disablePanel(java.awt.Container container) {
    Component[] components = container.getComponents();
    for (Component component : components) {
        component.setEnabled(false);
        if (component instanceof java.awt.Container) {
            disablePanel((java.awt.Container) component);
        }
    }
    container.setVisible(false);
    }
    //Plays a sound, returns the clip of the sound
    public Clip soundEffects(String soundLoc) {
        Clip clip = null;
    try {
        InputStream inputStream = getClass().getResourceAsStream(soundLoc);
        if (inputStream != null) {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } else {
            System.out.println("Sound file not found.");
        }
    } catch(Exception e){
                e.printStackTrace();
        }
    return clip;
    }
    
    //Back to menu and resets the game
    public void resetGame(Clip soundEffect) {
        score = 0;
        soundEffect.stop();
        startingSound.start();
        jLabelScoreChange.setText("0");
        jLabelTimeChange.setText("0");
        disablePanel(gamePanel);
        activatePanel(menuPanel);
        timerController.stop();
        Component[] components = jPanel2.getComponents();
        for (Component component : components)  {
            if (component instanceof JButton) {
              JButton button = (JButton) component;
              button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
            }
        }
       
    }
    
    public void loseGame() {
      startingSound.stop();
      Clip gameOver = soundEffects("/my/audio/gameOver.wav");
      JOptionPane.showMessageDialog(null,"Sorry, you have lost!","Try again",JOptionPane.ERROR_MESSAGE);
      resetGame(gameOver);
    }
    
    public void winGame() {
      startingSound.stop();
      Clip win = soundEffects("/my/audio/winningSound.wav");
      JOptionPane.showMessageDialog(null,"You have won the game!!", "Congratulations!!",JOptionPane.INFORMATION_MESSAGE);
      resetGame(win);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        mario1 = new javax.swing.JLabel();
        logo1 = new javax.swing.JLabel();
        credits1 = new javax.swing.JLabel();
        bg1 = new javax.swing.JLabel();
        difficultyPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        difficultyInfo = new javax.swing.JLabel();
        mario2 = new javax.swing.JLabel();
        logo2 = new javax.swing.JLabel();
        credits2 = new javax.swing.JLabel();
        bg2 = new javax.swing.JLabel();
        gamePanel = new javax.swing.JPanel();
        jLabelScore = new javax.swing.JLabel();
        jLabelTimeChange = new javax.swing.JLabel();
        jLabelScoreChange = new javax.swing.JLabel();
        jLabelTime = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        mario = new javax.swing.JLabel();
        logo = new javax.swing.JLabel();
        credits = new javax.swing.JLabel();
        bg = new javax.swing.JLabel();
        exitPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        mario3 = new javax.swing.JLabel();
        logo3 = new javax.swing.JLabel();
        credits3 = new javax.swing.JLabel();
        bg3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(610, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        menuPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 340, 30, 40));
        menuPanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 260, 30, 40));
        menuPanel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 300, 30, 40));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(225, 225, 225));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Exit Game");
        jLabel1.setAutoscrolls(true);
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jLabel1MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel1MouseMoved(evt);
            }
        });
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
        });
        menuPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 360, 170, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(225, 225, 225));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Start Game");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });
        menuPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 280, 180, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(225, 225, 225));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Difficulty");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });
        menuPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, 160, -1));

        mario1.setIcon(icons[6]);
        menuPanel.add(mario1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 110, 130));

        logo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/logo2.png"))); // NOI18N
        menuPanel.add(logo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, 220, 100));

        credits1.setForeground(new java.awt.Color(255, 255, 255));
        credits1.setText("Developed By: Ralph Ganzon");
        menuPanel.add(credits1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 310, 20));

        bg1.setIcon(new javax.swing.ImageIcon("C:\\Users\\hp\\Desktop\\New folder\\bg.png")); // NOI18N
        bg1.setText("jLabel1");
        menuPanel.add(bg1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 600));

        getContentPane().add(menuPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 600));

        difficultyPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        difficultyPanel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 340, 30, 40));
        difficultyPanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 260, 30, 40));
        difficultyPanel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 300, 30, 40));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(225, 225, 225));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Hard");
        jLabel10.setAutoscrolls(true);
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jLabel10MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel10MouseMoved(evt);
            }
        });
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel10MouseExited(evt);
            }
        });
        difficultyPanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 360, 100, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(225, 225, 225));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Easy");
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel11MouseExited(evt);
            }
        });
        difficultyPanel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 280, 100, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(225, 225, 225));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Medium");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel12MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel12MouseExited(evt);
            }
        });
        difficultyPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 320, 130, -1));

        difficultyInfo.setForeground(new java.awt.Color(255, 255, 255));
        difficultyInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        difficultyInfo.setText("1 goombas. 60 seconds. (Default) ");
        difficultyPanel.add(difficultyInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 450, 420, 40));

        mario2.setIcon(icons[6]);
        difficultyPanel.add(mario2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 110, 130));

        logo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/logo2.png"))); // NOI18N
        difficultyPanel.add(logo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, 220, 100));

        credits2.setForeground(new java.awt.Color(255, 255, 255));
        credits2.setText("Developed By: Ralph Ganzon");
        difficultyPanel.add(credits2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 310, 20));

        bg2.setIcon(new javax.swing.ImageIcon("C:\\Users\\hp\\Desktop\\New folder\\bg.png")); // NOI18N
        bg2.setText("jLabel1");
        difficultyPanel.add(bg2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 600));

        getContentPane().add(difficultyPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 600));

        gamePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        gamePanel.setBackground(new java.awt.Color(213, 134, 145, 0));

        jLabelScore.setForeground(new java.awt.Color(255, 255, 255));
        jLabelScore.setText("Score: ");
        gamePanel.add(jLabelScore, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 240, 80, -1));

        jLabelTimeChange.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTimeChange.setText("0");
        jLabelTimeChange.setEnabled(false);
        gamePanel.add(jLabelTimeChange, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 240, 70, -1));

        jLabelScoreChange.setForeground(new java.awt.Color(255, 255, 255));
        jLabelScoreChange.setText("0");
        jLabelScoreChange.setEnabled(false);
        gamePanel.add(jLabelScoreChange, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, 60, -1));

        jLabelTime.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTime.setText("Time:");
        gamePanel.add(jLabelTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 240, 60, -1));

        jPanel2.setBackground(new java.awt.Color(222, 81, 56));
        jPanel2.setLayout(new java.awt.GridLayout(4, 4));

        jButton1.setBackground(new java.awt.Color(229, 56, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        jButton5.setBackground(new java.awt.Color(229, 56, 0));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);

        jButton3.setBackground(new java.awt.Color(229, 56, 0));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);

        jButton8.setBackground(new java.awt.Color(229, 56, 0));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.setEnabled(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton8);

        jButton4.setBackground(new java.awt.Color(229, 56, 0));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);

        jButton9.setBackground(new java.awt.Color(229, 56, 0));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.setEnabled(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton9);

        jButton2.setBackground(new java.awt.Color(229, 56, 0));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        jButton6.setBackground(new java.awt.Color(229, 56, 0));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6);

        jButton7.setBackground(new java.awt.Color(229, 56, 0));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.setEnabled(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton7);

        jButton11.setBackground(new java.awt.Color(229, 56, 0));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton11.setEnabled(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton11);

        jButton10.setBackground(new java.awt.Color(229, 56, 0));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton10.setEnabled(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton10);

        jButton12.setBackground(new java.awt.Color(229, 56, 0));
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton12.setEnabled(false);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton12);

        jButton13.setBackground(new java.awt.Color(229, 56, 0));
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton13.setEnabled(false);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton13);

        jButton14.setBackground(new java.awt.Color(229, 56, 0));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton14.setEnabled(false);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton14);

        jButton15.setBackground(new java.awt.Color(229, 56, 0));
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton15.setEnabled(false);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton15);

        jButton16.setBackground(new java.awt.Color(229, 56, 0));
        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mystery_final.png"))); // NOI18N
        jButton16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton16.setEnabled(false);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton16);

        gamePanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 290, 270));

        mario.setIcon(icons[6]);
        gamePanel.add(mario, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 110, 130));

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/logo2.png"))); // NOI18N
        gamePanel.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, 220, 100));

        credits.setForeground(new java.awt.Color(255, 255, 255));
        credits.setText("Developed By: Ralph Ganzon");
        gamePanel.add(credits, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 310, 20));

        bg.setIcon(new javax.swing.ImageIcon("C:\\Users\\hp\\Desktop\\New folder\\bg.png")); // NOI18N
        bg.setText("jLabel1");
        gamePanel.add(bg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 600));

        getContentPane().add(gamePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 600));

        exitPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        exitPanel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 290, 30, 40));
        exitPanel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 290, 30, 40));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(225, 225, 225));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("No");
        jLabel16.setAutoscrolls(true);
        jLabel16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel16.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jLabel16MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel16MouseMoved(evt);
            }
        });
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel16MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel16MouseExited(evt);
            }
        });
        exitPanel.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 310, 100, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Are you sure you want to exit?");
        jLabel17.setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel17MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel17MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel17MouseExited(evt);
            }
        });
        exitPanel.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 500, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(225, 225, 225));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Yes");
        jLabel18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel18MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel18MouseExited(evt);
            }
        });
        exitPanel.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 310, 130, -1));

        mario3.setIcon(icons[6]);
        exitPanel.add(mario3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 110, 130));

        logo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/logo2.png"))); // NOI18N
        exitPanel.add(logo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, 220, 100));

        credits3.setForeground(new java.awt.Color(255, 255, 255));
        credits3.setText("Developed By: Ralph Ganzon");
        exitPanel.add(credits3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 310, 20));

        bg3.setIcon(new javax.swing.ImageIcon("C:\\Users\\hp\\Desktop\\New folder\\bg.png")); // NOI18N
        bg3.setText("jLabel1");
        exitPanel.add(bg3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 600));

        getContentPane().add(exitPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 600));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        clicked(3, 3, jButton16);
        randomizeSkin();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        clicked(3, 2, jButton15);
        randomizeSkin();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        clicked(3, 1, jButton14);
        randomizeSkin();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        clicked(3, 0, jButton13);
        randomizeSkin();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        clicked(2, 3, jButton12);
        randomizeSkin();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        clicked(2, 2, jButton10);
        randomizeSkin();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        clicked(2, 1, jButton11);
        randomizeSkin();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        clicked(2, 0, jButton7);
        randomizeSkin();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        clicked(1, 3, jButton6);
        randomizeSkin();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clicked(1, 2, jButton2);
        randomizeSkin();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        clicked(1, 1, jButton9);
        randomizeSkin();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        clicked(1, 0, jButton4);
        randomizeSkin();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        clicked(0, 3, jButton8);
        randomizeSkin();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clicked(0, 2, jButton3);
        randomizeSkin();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        clicked(0, 1, jButton5);
        randomizeSkin();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        clicked(0, 0, jButton1);
        randomizeSkin();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseMoved

    }//GEN-LAST:event_jLabel1MouseMoved

    private void jLabel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseDragged

        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseDragged

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mushroom.png"))); // NOI18N

    }//GEN-LAST:event_jLabel1MouseEntered

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseExited
        jLabel1.setForeground(new java.awt.Color(225, 225, 225));
        jLabel4.setIcon(null); // NOI18N
        
    }//GEN-LAST:event_jLabel1MouseExited

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mushroom.png"))); // NOI18N

    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        jLabel2.setForeground(new java.awt.Color(225, 225, 225));
        jLabel5.setIcon(null); // NOI18N
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mushroom.png"))); // NOI18N
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        jLabel3.setForeground(new java.awt.Color(225, 225, 225));
        jLabel6.setIcon(null); // NOI18N
    }//GEN-LAST:event_jLabel3MouseExited

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        disablePanel(menuPanel);
        activatePanel(gamePanel);
        jLabelTimeChange.setEnabled(true);
        jLabelScoreChange.setEnabled(true);
        mario.setIcon(icons[5]); // NOI18N
        assignBomb(symbols, noOfBombs);
        jLabelTimeChange.setText(String.valueOf(timeLimit));
        System.out.println(timeLimit);
        timerController = new TimerController(jLabelTimeChange, timeLimit, this);
        timerController.start(startingSound);
        
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseEntered
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        difficultyInfo.setText("2 goombas. 45 seconds.");
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mushroom.png")));
    }//GEN-LAST:event_jLabel12MouseEntered

    private void jLabel12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseExited
        jLabel12.setForeground(new java.awt.Color(225, 225, 225));
        jLabel9.setIcon(null); // NOI18N
    }//GEN-LAST:event_jLabel12MouseExited

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        disablePanel(menuPanel);
        activatePanel(difficultyPanel);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseExited
        jLabel10.setForeground(new java.awt.Color(225, 225, 225));
        jLabel7.setIcon(null); // NOI18N
    }//GEN-LAST:event_jLabel10MouseExited

    private void jLabel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseEntered
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        difficultyInfo.setText("3 goombas. 30 seconds.");
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mushroom.png")));
    }//GEN-LAST:event_jLabel10MouseEntered

    private void jLabel10MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseMoved

    }//GEN-LAST:event_jLabel10MouseMoved

    private void jLabel10MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel10MouseDragged

    private void jLabel11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseExited
        jLabel11.setForeground(new java.awt.Color(225, 225, 225));
        jLabel8.setIcon(null); // NOI18N
    }//GEN-LAST:event_jLabel11MouseExited

    private void jLabel11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseEntered
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        difficultyInfo.setText("1 goombas. 60 seconds (Default)");
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mushroom.png")));
    }//GEN-LAST:event_jLabel11MouseEntered

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        noOfBombs = 1;
        timeLimit = 60;
        disablePanel(difficultyPanel);
        activatePanel(menuPanel);
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        noOfBombs = 2;
        timeLimit = 45;
        disablePanel(difficultyPanel);
        activatePanel(menuPanel);
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        noOfBombs = 3;
        timeLimit = 30;
        disablePanel(difficultyPanel);
        activatePanel(menuPanel);
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel16MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel16MouseDragged

    private void jLabel16MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel16MouseMoved

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        disablePanel(exitPanel);
        activatePanel(menuPanel);
    }//GEN-LAST:event_jLabel16MouseClicked

    private void jLabel16MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseEntered
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mushroom.png")));
    }//GEN-LAST:event_jLabel16MouseEntered

    private void jLabel16MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseExited
        jLabel16.setForeground(new java.awt.Color(225, 225, 225));
        jLabel13.setIcon(null);
    }//GEN-LAST:event_jLabel16MouseExited

    private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel17MouseClicked

    private void jLabel17MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel17MouseEntered

    private void jLabel17MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel17MouseExited

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel18MouseClicked

    private void jLabel18MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseEntered
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/my/resources/mushroom.png")));
    }//GEN-LAST:event_jLabel18MouseEntered

    private void jLabel18MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseExited
        jLabel18.setForeground(new java.awt.Color(225, 225, 225));
        jLabel15.setIcon(null);
    }//GEN-LAST:event_jLabel18MouseExited

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
       disablePanel(menuPanel);
       activatePanel(exitPanel);
    }//GEN-LAST:event_jLabel1MouseClicked


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SweeperMario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SweeperMario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SweeperMario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SweeperMario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SweeperMario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bg;
    private javax.swing.JLabel bg1;
    private javax.swing.JLabel bg2;
    private javax.swing.JLabel bg3;
    private javax.swing.JLabel credits;
    private javax.swing.JLabel credits1;
    private javax.swing.JLabel credits2;
    private javax.swing.JLabel credits3;
    private javax.swing.JLabel difficultyInfo;
    private javax.swing.JPanel difficultyPanel;
    private javax.swing.JPanel exitPanel;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelScore;
    private javax.swing.JLabel jLabelScoreChange;
    private javax.swing.JLabel jLabelTime;
    private javax.swing.JLabel jLabelTimeChange;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel logo;
    private javax.swing.JLabel logo1;
    private javax.swing.JLabel logo2;
    private javax.swing.JLabel logo3;
    private javax.swing.JLabel mario;
    private javax.swing.JLabel mario1;
    private javax.swing.JLabel mario2;
    private javax.swing.JLabel mario3;
    private javax.swing.JPanel menuPanel;
    // End of variables declaration//GEN-END:variables
}
