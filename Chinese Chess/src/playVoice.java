
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

class playVoice{
    private int tmp;
    playVoice(){
    }
    AudioClip audioClip;
    public void playTrun(int che){
        audioClip = java.applet.Applet.newAudioClip(getClass().getResource("/voice/turn.wav"));
        audioClip.play();
        
        tmp = che;
        if(che>7 & che<13)
            tmp-=7;
        time1.start();
    }
    public void playMove(){
        audioClip = java.applet.Applet.newAudioClip(getClass().getResource("/voice/move.wav"));
        audioClip.play();
    }
    public void playEat(){
        audioClip = java.applet.Applet.newAudioClip(getClass().getResource("/voice/eat.wav"));
        audioClip.play();
        time2.start();
    }
    public void playError(){
        int r = (int)(Math.random()*2);
        if(r == 0){
            audioClip = java.applet.Applet.newAudioClip(getClass().getResource("/voice/Track4.wav"));
            audioClip.play();
        }
        else{
            audioClip = java.applet.Applet.newAudioClip(getClass().getResource("/voice/Track5.wav"));
            audioClip.play();
        }
    }
    public void playWin1(){
            audioClip = java.applet.Applet.newAudioClip(getClass().getResource("/voice/win1.wav"));
            audioClip.play();
    }
    public void playWin2(){
            audioClip = java.applet.Applet.newAudioClip(getClass().getResource("/voice/win2.wav"));
            audioClip.play();
    }
    public void playStart(){
        int r = (int)(Math.random()*2);
        if(r == 0)
            audioClip = java.applet.Applet.newAudioClip(getClass().getResource("/voice/st_fs.wav"));
        else
            audioClip = java.applet.Applet.newAudioClip(getClass().getResource("/voice/st_sl.wav"));
        audioClip.play();
    }
    public void playMenuBGM(){
        audioClip = java.applet.Applet.newAudioClip(getClass().getResource("/voice/Menu_BGM.wav"));
        audioClip.loop();
    }
    public void stop(){
        time1.stop();
        time2.stop();
        if(audioClip!=null)
            audioClip.stop();
    }
    
    Timer time1 = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                audioClip = java.applet.Applet.newAudioClip(getClass().getResource("/voice/"+ tmp +".wav"));
                audioClip.play();
                time1.stop();
            }
    });
    Timer time2 = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                audioClip = java.applet.Applet.newAudioClip(getClass().getResource("/voice/eatm.wav"));
                audioClip.play();
                time2.stop();
            }
    });
}