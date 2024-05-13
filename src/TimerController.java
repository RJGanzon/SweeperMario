import java.io.InputStream;
import javax.swing.JLabel;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class TimerController {
    private int secondsPassed;
    private int currentSecondsPassed;
    private Timer timer;
    private JLabel jLabel;
    private SweeperMario sweeperMario;

    public TimerController(JLabel jLabel, int secondsPassed, SweeperMario sweeperMario) {
        this.jLabel = jLabel;
        this.secondsPassed = secondsPassed;
        this.currentSecondsPassed = secondsPassed;
        this.sweeperMario = sweeperMario;
    }

public void start(Clip startingSound) {
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
            secondsPassed--;
            SwingUtilities.invokeLater(() -> {
                jLabel.setText(String.valueOf(secondsPassed));
                if(secondsPassed <= 0) {
                    timer.cancel();
                    sweeperMario.loseGame();
                }
            });
        }
    }, 1000, 1000); // Schedule to run every 1 second after an initial delay of 1 second
}

public void stop() {
    if (timer!= null) {
        timer.cancel();
        timer = null;
        this.secondsPassed = currentSecondsPassed;
    }
}




}
