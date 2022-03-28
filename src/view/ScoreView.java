package tetris.src.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

import java.awt.Color;
import java.util.ArrayList;


public class ScoreView extends JPanel {

    private JButton returnButton;
    private ArrayList<JLabel> names;

    public JButton getReturnButton() {
        return this.returnButton;
    }

    public void setReturnButton(JButton returnButton) {
        this.returnButton = returnButton;
    }
    
    public ScoreView() {
        super.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        returnButton = new JButton("Return");
        names = new ArrayList<>();

        names.add(new JLabel("호호이1"));
        names.add(new JLabel("호호이2"));
        names.add(new JLabel("호호이3"));
        names.add(new JLabel("호호이4"));
        names.add(new JLabel("호호이5"));
        names.add(new JLabel("호호이6"));
        names.add(new JLabel("호호이7"));

        addLabel(names);
    }

    private void addLabel(ArrayList<JLabel> players) {
        for(JLabel player : players)
            super.add(player);
    }
}
