package co.in;

import co.in.ui.CoinFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    /**
     * Pull in the JFrame to be displayed.
     */
    @Autowired
    private CoinFrame frame;

    @Override
    public void run(String... args) {
        java.awt.EventQueue.invokeLater(() -> frame.setVisible(true));
    }

}