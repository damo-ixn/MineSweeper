import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Board extends JFrame {

    private JLabel statusbar;
    
    public Board() {
        init();
    }
    
    private void init() {

        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);

        add(new Game(statusbar));        
        
        setResizable(false);
        pack();
        
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            Board ex = new Board();
            ex.setVisible(true);
        });
    }
}
