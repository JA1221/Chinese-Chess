
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 盧俊安
 */
public class Chinese_Chess extends javax.swing.JFrame {

    /**
     * Creates new form Chinese_Chess
     */
    public Chinese_Chess() {
        initComponents();
        chessComponents();
        chessRelocate();
        reset();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/6.png")));
        this.setLocationRelativeTo(null);
    }
    
    public Chinese_Chess(boolean continuous_kill, boolean assassinate){
        this();
//        this.connection = connection;
        this.continuous_kill = continuous_kill;
        this.assassinate = assassinate;
    }
    private void chessComponents(){
        chessLb = new JLabel[32];
        
        chessLb[0]=chess00;   chessLb[1]=chess01;    chessLb[2]=chess02;    chessLb[3]=chess03; 
        chessLb[4]=chess04;   chessLb[5]=chess05;    chessLb[6]=chess06;    chessLb[7]=chess07; 
        chessLb[8]=chess08;   chessLb[9]=chess09;    chessLb[10]=chess10;    chessLb[11]=chess11; 
        chessLb[12]=chess12;   chessLb[13]=chess13;    chessLb[14]=chess14;    chessLb[15]=chess15;
        chessLb[16]=chess16;   chessLb[17]=chess17;    chessLb[18]=chess18;    chessLb[19]=chess19; 
        chessLb[20]=chess20;   chessLb[21]=chess21;    chessLb[22]=chess22;    chessLb[23]=chess23; 
        chessLb[24]=chess24;   chessLb[25]=chess25;    chessLb[26]=chess26;    chessLb[27]=chess27;
        chessLb[28]=chess28;   chessLb[29]=chess29;    chessLb[30]=chess30;    chessLb[31]=chess31;
        
        
        for(int i=0; i<14;i++)
            chessImg[i] = new ImageIcon(getClass().getResource("/images/" + i + ".png"));
        chessImg[14] = new ImageIcon(getClass().getResource("/images/back.png"));
        chessImg[15] = new ImageIcon(getClass().getResource("/images/chessSelect.png"));
        chessImg[16] = new ImageIcon(getClass().getResource("/images/chessSelect1.png"));
        chessImg[17] = new ImageIcon(getClass().getResource("/images/Unknown_Color.png"));
        chessImg[18] = new ImageIcon(getClass().getResource("/images/red.png"));
        chessImg[19] = new ImageIcon(getClass().getResource("/images/black.png"));
        chessImg[20] = new ImageIcon(getClass().getResource("/images/Unknown_Color.png"));
        
        for(int i=0; i<32; i++){
            int chessNum = i;
            chessLb[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    GameVoice.stop();
                    if(endJudgment())
                        chessSelect(chessNum);
                }
            });
        }
    }
    private void chessRelocate(){
        int size = 100, Y_correct = 3;
        int x = checkerboardLb.getLocation().x;
        int y = checkerboardLb.getLocation().y;
        for(int i=0; i<32; i++)
            chessLb[i].setLocation(x + (i%8)*size, y + (i/8)*size + Y_correct);
        chessSelectLb.setLocation(x, y + Y_correct);
    }
    private void random(int data[],int num){
        int x, y, tmp;
        for(int i=0; i<num; i++){
            x = (int)(Math.random()*data.length);
            y = (int)(Math.random()*data.length);
            if(x == y){
                i--;
                continue;
            }
            tmp = data[x];
            data[x] = data[y];
            data[y] = tmp;
        }
        for(int i=0; i<data.length; i++)
            System.out.print(data[i] + " ");
        System.out.println();
    }
    private void reset(){
        GameVoice.stop();
        kill.stop();
        time1.stop();
        
        checkerboard = new int[] //棋子的擺盤
           {0, 0, 0, 0, 7, 7, 7, 7, 0, 1, 1, 2, 7, 8, 8, 9, 2, 3, 3, 4, 9, 10, 10, 11, 4, 5, 5, 6, 11, 12, 12, 13};
//        {0, 0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 7, 7, 7, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13};
        random(checkerboard, 100);
        
        playerChessNum = new int[]{16, 16};
        offensiveChess= new int[]{-1,-1};
        count = 0;
        selectFlag = false;
        killFlag = false;
        assassinateFlag = false;
        
        chessFlag = new int[chessLb.length];
        for(int i=0; i<chessLb.length; i++){
            chessLb[i].setIcon(chessImg[14]);
            chessFlag[i] = 2;
        }
        chessSelectLb.setIcon(null);
        TimeLb.setText(String.valueOf(CountdownTime));
        
        GameVoice.playStart();
        Player1.setEnabled(true);
        Player1.setIcon(chessImg[20]);
        Player2.setEnabled(false);
        Player2.setIcon(chessImg[20]);
        TimeLb.setForeground(Color.black);
        TimeLb.setText(String.valueOf(CountdownTime));
        Name.setText("Countdown");
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void chessSelect(int chessNum){
        if(kill.isRunning())
            return;
        if(killFlag){//連殺模式?
            if(offensiveChess[0] == chessNum){//連殺模式選自己 結束連殺
                next();
                killFlag = false;
            }else if(!(chessFlag[chessNum]==1) | isMyChess(chessNum)){//非翻開的(暗殺除外) 或 自己人 => 不動作
                if(!(chessFlag[chessNum]==2 & assassinate))
                    return;//!!!!!!!!!!!!!!!!!!!!!
            }
        }
        //=====================================================================
        switch (chessFlag[chessNum]) {
            case 2:
                //******選未翻的棋*********
                if(count==0){//第一場 雙方顏色確定                    
                    player[0] = checkerboard[chessNum]/7;
                    Player1.setIcon(chessImg[18+player[0]]);
                    player[1] = 1 - player[0];
                    Player2.setIcon(chessImg[18+player[1]]);
                }
                flipChess(chessNum);
                if(assassinate & selectFlag){
                    kill.start();
                    offensiveChess[1] = chessNum;
                }else{
                    next();
                }   break;
            case 1:
                //*******選正面的棋*********
                if(!selectFlag){//還沒選棋
                    if(isMyChess(chessNum)){
                        offensiveChess[0] = chessNum;
                        select(chessNum);
                    }
                }else{//選棋完要吃棋
                    if(isMyChess(chessNum)){//選自己 取消選棋
                        deselect();
                    }else{//attack
                        offensiveChess[1] = chessNum;
                        if(attack(offensiveChess[0],offensiveChess[1])){//吃棋 成功true 失敗false
                            if(continuous_kill){//玩連殺
                                killFlag = true;
                                select(chessNum);
                                offensiveChess[0] = offensiveChess[1];
                            }else{//不玩連殺 吃完棋換下一局
                                next();
                            }
                        }
                    }
                }   break;
            default:
                //**************沒棋****************
                offensiveChess[1] = chessNum;
                if(selectFlag & Correct_move(offensiveChess[0],offensiveChess[1])){
                    GameVoice.playMove();
                    moveChess(offensiveChess[0],offensiveChess[1]);
                    next();
                }else
                    GameVoice.playError();
                break;
        }
    }
    private boolean attack(int x, int y){//判斷 x 能不能攻擊 y
        int tmpx = checkerboard[x]%7;
        int tmpy = checkerboard[y]%7;
        if(Correct_move(x, y) & attackTable[tmpx][tmpy]==1){
            GameVoice.playEat();
            playerChessNum[(count+1)%2]--;
            System.out.println(playerChessNum[(count+1)%2]);
            moveChess(x, y);
            deselect();
            return true;
        }else
            GameVoice.playError();
        return false;
    }
    private void flipChess(int chessNum){//翻棋 顯示正面
        int tmp = checkerboard[chessNum];//哪一種棋子
        chessLb[chessNum].setIcon(chessImg[tmp]);
        chessFlag[chessNum] = 1;
        GameVoice.playTrun(tmp);
    }
    private boolean isMyChess(int num){//判斷是否是當局玩家的棋
        return checkerboard[num]/7 == player[count%2];
    }
    private boolean Correct_move(int a, int b){//判斷 a 走到 b 是否合法
        int ax = a%8, ay = a/8;
        int bx = b%8, by = b/8;
        int move_x = bx - ax;
        int move_y = by - ay;
        int chessCount = 0;
        
        if(checkerboard[b]==-1 | checkerboard[a]%7 != 1){//b位置沒棋 a不是炮的時候
            if((Math.abs(move_x)==1 & Math.abs(move_y)==0) | (Math.abs(move_x)==0 & Math.abs(move_y)==1))
                return true;
        }else if(checkerboard[a]%7 == 1){//判斷炮
            if(move_x==0 & move_y!=0){//直向
                if(ay>by)
                    a=b;
                b = Math.abs(move_y);
                for(int i=1; i<b; i++){
                    if(chessFlag[a+i*8]!=0)
                        chessCount++;
                }
            }else if(move_x!=0 & move_y==0){//橫向
                if(ax>bx)
                    a=b;
                b = Math.abs(move_x);
                for(int i=1; i<b; i++){
                    if(chessFlag[a+i]!=0)
                        chessCount++;
                }
            }
            if(chessCount==1)//炮只能隔一顆吃
                return true;
        }
        return false;
    }
    private void moveChess(int a, int b){//a 移動到 b
        checkerboard[b] = checkerboard[a];
        chessLb[b].setIcon(chessImg[checkerboard[b]]);
        chessFlag[b] = 1;
        
        checkerboard[a] = -1;
        chessLb[a].setIcon(null);
        chessFlag[a] = 0;
    }
    private void deselect(){//取消選擇圖示
        selectFlag = false;
        chessSelectLb.setIcon(null);
    }
    private void select(int chessNum){//選擇圖示標示在chessNum
        selectFlag = true;
        chessSelectLb.setIcon(chessImg[15]);
        chessSelectLb.setLocation(chessLb[chessNum].getLocation());
    }
    private void next(){
        count++;
        deselect();
        Player1.setEnabled(count%2==0);
        Player2.setEnabled(count%2==1);
        time1.stop();
        if(endJudgment()){
            TimeLb.setForeground(Color.black);
            TimeLb.setText(String.valueOf(CountdownTime));
            time1.start();
        }
    }
    private boolean endJudgment(){//true繼續 false結束
        if(playerChessNum[1]==0){
            Name.setText("Player 1 Win!!!");
            GameVoice.playWin1();
        }else if(playerChessNum[0]==0){
            Name.setText("Player 2 Win!!!");
            GameVoice.playWin2();
        }else
            return true;
        time1.stop();
        return false;
    }

    Timer kill = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = checkerboard[offensiveChess[0]]%7;
                int y = checkerboard[offensiveChess[1]]%7;
                
                kill.stop();
                assassinateFlag = false;
                if(isMyChess(offensiveChess[1])){
                    killFlag = false;
                    next();
                }else if(attack(offensiveChess[0], offensiveChess[1])){
                    if(continuous_kill){
                        killFlag = true;
                        select(offensiveChess[1]);
                        offensiveChess[0] = offensiveChess[1];
                    }else
                        next();
                }else{
                    killFlag = false;
                    next();
                }
            }
    });
    Timer time1 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int t = Integer.parseInt(TimeLb.getText());
                t--;
                if(t <= 10)//剩餘10秒
                    TimeLb.setForeground(Color.red);
                if(t==0){//時間結束 判輸
                    time1.stop();
                    playerChessNum[(count)%2] = 0;
                    next();
                }
                TimeLb.setText(String.valueOf(t));
            }
    });
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chess00 = new javax.swing.JLabel();
        chess01 = new javax.swing.JLabel();
        chess02 = new javax.swing.JLabel();
        chess03 = new javax.swing.JLabel();
        chess04 = new javax.swing.JLabel();
        chess05 = new javax.swing.JLabel();
        chess06 = new javax.swing.JLabel();
        chess07 = new javax.swing.JLabel();
        chess08 = new javax.swing.JLabel();
        chess09 = new javax.swing.JLabel();
        chess10 = new javax.swing.JLabel();
        chess11 = new javax.swing.JLabel();
        chess12 = new javax.swing.JLabel();
        chess13 = new javax.swing.JLabel();
        chess14 = new javax.swing.JLabel();
        chess15 = new javax.swing.JLabel();
        chess16 = new javax.swing.JLabel();
        chess17 = new javax.swing.JLabel();
        chess18 = new javax.swing.JLabel();
        chess19 = new javax.swing.JLabel();
        chess20 = new javax.swing.JLabel();
        chess21 = new javax.swing.JLabel();
        chess22 = new javax.swing.JLabel();
        chess23 = new javax.swing.JLabel();
        chess24 = new javax.swing.JLabel();
        chess25 = new javax.swing.JLabel();
        chess26 = new javax.swing.JLabel();
        chess27 = new javax.swing.JLabel();
        chess28 = new javax.swing.JLabel();
        chess29 = new javax.swing.JLabel();
        chess30 = new javax.swing.JLabel();
        chess31 = new javax.swing.JLabel();
        Player1 = new javax.swing.JLabel();
        Player2 = new javax.swing.JLabel();
        RestartGame = new javax.swing.JLabel();
        Name = new javax.swing.JLabel();
        TimeLb = new javax.swing.JLabel();
        chessSelectLb = new javax.swing.JLabel();
        checkerboardLb = new javax.swing.JLabel();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Dark Chess");
        setAlwaysOnTop(true);
        setPreferredSize(new java.awt.Dimension(1000, 610));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        chess00.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/0.png"))); // NOI18N
        getContentPane().add(chess00);
        chess00.setBounds(100, 150, 100, 100);

        chess01.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/10.png"))); // NOI18N
        getContentPane().add(chess01);
        chess01.setBounds(200, 150, 100, 100);

        chess02.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/11.png"))); // NOI18N
        getContentPane().add(chess02);
        chess02.setBounds(300, 150, 100, 100);

        chess03.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/7.png"))); // NOI18N
        getContentPane().add(chess03);
        chess03.setBounds(400, 150, 100, 100);

        chess04.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/13.png"))); // NOI18N
        getContentPane().add(chess04);
        chess04.setBounds(500, 150, 100, 100);

        chess05.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/11.png"))); // NOI18N
        getContentPane().add(chess05);
        chess05.setBounds(600, 150, 100, 100);

        chess06.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/5.png"))); // NOI18N
        getContentPane().add(chess06);
        chess06.setBounds(700, 150, 100, 100);

        chess07.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/7.png"))); // NOI18N
        getContentPane().add(chess07);
        chess07.setBounds(800, 150, 100, 100);

        chess08.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/0.png"))); // NOI18N
        getContentPane().add(chess08);
        chess08.setBounds(100, 250, 100, 100);

        chess09.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1.png"))); // NOI18N
        getContentPane().add(chess09);
        chess09.setBounds(200, 250, 100, 100);

        chess10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/10.png"))); // NOI18N
        getContentPane().add(chess10);
        chess10.setBounds(300, 250, 100, 100);

        chess11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/chessSelect.png"))); // NOI18N
        getContentPane().add(chess11);
        chess11.setBounds(400, 250, 100, 100);

        chess12.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess12);
        chess12.setBounds(500, 250, 100, 100);

        chess13.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess13);
        chess13.setBounds(600, 250, 100, 100);

        chess14.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess14);
        chess14.setBounds(700, 250, 100, 100);

        chess15.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess15);
        chess15.setBounds(800, 250, 100, 100);

        chess16.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess16);
        chess16.setBounds(100, 350, 100, 100);

        chess17.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess17);
        chess17.setBounds(200, 350, 100, 100);

        chess18.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess18);
        chess18.setBounds(300, 350, 100, 100);

        chess19.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess19);
        chess19.setBounds(400, 350, 100, 100);

        chess20.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess20);
        chess20.setBounds(500, 350, 100, 100);

        chess21.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess21);
        chess21.setBounds(600, 350, 100, 100);

        chess22.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess22);
        chess22.setBounds(700, 350, 100, 100);

        chess23.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess23);
        chess23.setBounds(800, 450, 100, 100);

        chess24.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess24);
        chess24.setBounds(100, 450, 100, 100);

        chess25.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess25);
        chess25.setBounds(200, 450, 100, 100);

        chess26.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess26);
        chess26.setBounds(300, 450, 100, 100);

        chess27.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess27);
        chess27.setBounds(400, 450, 100, 100);

        chess28.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess28);
        chess28.setBounds(500, 450, 100, 100);

        chess29.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess29);
        chess29.setBounds(600, 450, 100, 100);

        chess30.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess30);
        chess30.setBounds(700, 450, 100, 100);

        chess31.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chess31);
        chess31.setBounds(800, 440, 100, 100);

        Player1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Unknown_Color.png"))); // NOI18N
        getContentPane().add(Player1);
        Player1.setBounds(140, 10, 130, 130);

        Player2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Unknown_Color.png"))); // NOI18N
        Player2.setEnabled(false);
        getContentPane().add(Player2);
        Player2.setBounds(720, 10, 130, 130);

        RestartGame.setFont(new java.awt.Font("Tunga", 1, 24)); // NOI18N
        RestartGame.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RestartGame.setText("Restart ");
        RestartGame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RestartGameMouseClicked(evt);
            }
        });
        getContentPane().add(RestartGame);
        RestartGame.setBounds(450, 550, 110, 30);

        Name.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 36)); // NOI18N
        Name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Name.setText("Countdown ");
        Name.setToolTipText("");
        getContentPane().add(Name);
        Name.setBounds(290, 10, 440, 60);

        TimeLb.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 48)); // NOI18N
        TimeLb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TimeLb.setText("00");
        TimeLb.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 5, true));
        getContentPane().add(TimeLb);
        TimeLb.setBounds(440, 80, 120, 60);

        chessSelectLb.setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().add(chessSelectLb);
        chessSelectLb.setBounds(100, 150, 100, 100);

        checkerboardLb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/checkerboard.png"))); // NOI18N
        getContentPane().add(checkerboardLb);
        checkerboardLb.setBounds(100, 150, 800, 400);

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/background.jpg"))); // NOI18N
        getContentPane().add(background);
        background.setBounds(0, 0, 1000, 600);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        GameVoice.stop();
        kill.stop();
        time1.stop();
        this.dispose();
        new Game_Ｍenu().setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void RestartGameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RestartGameMouseClicked
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_RestartGameMouseClicked

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
            java.util.logging.Logger.getLogger(Chinese_Chess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chinese_Chess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chinese_Chess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chinese_Chess.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chinese_Chess().setVisible(true);
            }
        });
    }
    //================================================
    private final ImageIcon [] chessImg = new ImageIcon[21];//圖片0~6 紅<兵炮馬車相仕帥> 7~13黑<卒炮馬車象士將>
    private JLabel [] chessLb; 
    playVoice GameVoice = new playVoice();
    private int [] checkerboard ;//棋子分部圖
    private int [] chessFlag;//棋子狀態   0無棋 1有棋已翻 2有棋未翻
    private boolean connection, continuous_kill, assassinate;
    private boolean killFlag,assassinateFlag;
    private int count = 0;//局數
    private final int CountdownTime = 30;
    private boolean selectFlag = false;//選棋旗標
    private int player[] = new int[2];//0紅 1黑
    private int playerChessNum[];//0紅 1黑
    private int offensiveChess[];//第一個->功 第二個->守
    private final int attackTable[][] = new int[][]{//[攻擊方][被攻方] true成功 false失敗
            {1,0,0,0,0,0,1},
            {1,1,1,1,1,1,1},
            {1,1,1,0,0,0,0},
            {1,1,1,1,0,0,0},
            {1,1,1,1,1,0,0},
            {1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1}
        };
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Name;
    private javax.swing.JLabel Player1;
    private javax.swing.JLabel Player2;
    private javax.swing.JLabel RestartGame;
    private javax.swing.JLabel TimeLb;
    private javax.swing.JLabel background;
    private javax.swing.JLabel checkerboardLb;
    private javax.swing.JLabel chess00;
    private javax.swing.JLabel chess01;
    private javax.swing.JLabel chess02;
    private javax.swing.JLabel chess03;
    private javax.swing.JLabel chess04;
    private javax.swing.JLabel chess05;
    private javax.swing.JLabel chess06;
    private javax.swing.JLabel chess07;
    private javax.swing.JLabel chess08;
    private javax.swing.JLabel chess09;
    private javax.swing.JLabel chess10;
    private javax.swing.JLabel chess11;
    private javax.swing.JLabel chess12;
    private javax.swing.JLabel chess13;
    private javax.swing.JLabel chess14;
    private javax.swing.JLabel chess15;
    private javax.swing.JLabel chess16;
    private javax.swing.JLabel chess17;
    private javax.swing.JLabel chess18;
    private javax.swing.JLabel chess19;
    private javax.swing.JLabel chess20;
    private javax.swing.JLabel chess21;
    private javax.swing.JLabel chess22;
    private javax.swing.JLabel chess23;
    private javax.swing.JLabel chess24;
    private javax.swing.JLabel chess25;
    private javax.swing.JLabel chess26;
    private javax.swing.JLabel chess27;
    private javax.swing.JLabel chess28;
    private javax.swing.JLabel chess29;
    private javax.swing.JLabel chess30;
    private javax.swing.JLabel chess31;
    private javax.swing.JLabel chessSelectLb;
    // End of variables declaration//GEN-END:variables

}
