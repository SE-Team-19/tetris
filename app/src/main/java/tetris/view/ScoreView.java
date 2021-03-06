package tetris.view;

import javax.swing.*;
import javax.swing.SwingConstants;
import java.awt.*;

import java.util.*;
import java.util.List;

public class ScoreView extends MasterView {

    private AbstractButton returnScoreToMainBtn;
    private JPanel rankingPane;
    private JLabel rankingTitle;
    private List<List<String>> rankingList;

    /* SingleTone 패턴 */
    private ScoreView() {
        initComponents();
        initView();
    }

    private static class LazyHolder {
        private static final ScoreView INSTANCE = new ScoreView();
    }

    public static ScoreView getInstance() {
        return LazyHolder.INSTANCE;
    }

    /***********************************************/

    public void resetRankingPane() {
        super.remove(rankingPane);
        initRankingPane();
        super.add(rankingPane, addGridBagComponents(0, 1));
    }

    private void initView() {
        GridBagLayout gridBag = new GridBagLayout();
        gridBag.columnWidths = new int[] { 0, 0 };
        gridBag.rowHeights = new int[] { 0, 0, 0, 0 };
        gridBag.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBag.rowWeights = new double[] { 0.2, 1.0, 0.2, Double.MIN_VALUE };
        super.setLayout(gridBag);

        super.add(rankingTitle, addGridBagComponents(0, 0));
        super.add(rankingPane, addGridBagComponents(0, 1));
        super.add(returnScoreToMainBtn, addGridBagComponents(0, 2));
    }

    private void initComponents() {
        rankingTitle = initAndSetName("rankingTitle", new JLabel("Ranking"));
        rankingTitle.setHorizontalAlignment(SwingConstants.CENTER);
        returnScoreToMainBtn = initAndSetName("returnScoreToMainBtn", new JButton("Return"));
        rankingList = new ArrayList<>();
        initRankingPane();
    }

    public void initRankingPane() {
        GridBagLayout gridBag = new GridBagLayout();
        gridBag.columnWidths = new int[] { 0, 0, 0, 0 };
        gridBag.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gridBag.columnWeights = new double[] { 0.5, 1.0, 1.0, 0.5, Double.MIN_VALUE };
        gridBag.rowWeights = new double[] { 0.8, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                Double.MIN_VALUE };

        rankingPane = new JPanel();

        rankingPane.setLayout(gridBag);
        addGridBagComponents(rankingPane, new JLabel("랭크"), 0, 0);
        addGridBagComponents(rankingPane, new JLabel("이름"), 1, 0);
        addGridBagComponents(rankingPane, new JLabel("점수"), 2, 0);
        addGridBagComponents(rankingPane, new JLabel("난이도"), 3, 0);
    }

    private void addRankInfo(Container pane, List<String> array, int y) {
        int i = 0;
        y++;
        addGridBagComponents(pane, new JLabel(String.valueOf(y)), i, y);
        i++;
        for (String a : array) {
            addGridBagComponents(pane, new JLabel(a), i, y);
            i++;
        }
    }

    private void addRankInfo(Container pane, List<String> array, int y, String userName) {
        int i = 0;
        y++;
        addGridBagComponents(pane, new JLabel(String.valueOf(y)), i, y);
        i++;
        for (String a : array) {
            JLabel user = new JLabel(a);
            if (a.equals(userName)) {
                user.setForeground(Color.BLUE);
            }
            addGridBagComponents(pane, user, i, y);
            i++;
        }
        rankingPane.repaint();
        rankingPane.revalidate();
    }

    public void fillScoreBoard() {
        ListIterator<List<String>> iter = rankingList.listIterator();
        int i = 0;
        while (iter.hasNext() && i < 10) {
            addRankInfo(rankingPane, iter.next(), i);
            i++;
        }
    }

    public void fillScoreBoard(String userName) {
        ListIterator<List<String>> iter = rankingList.listIterator();
        int i = 0;
        while (iter.hasNext() && i < 10) {
            addRankInfo(rankingPane, iter.next(), i, userName);
            i++;
        }
    }

    public void addRankingList(List<String> array) {
        rankingList.add(array);
    }

    public List<List<String>> getRankingList() {
        return this.rankingList;
    }

    public void setRankingList(List<List<String>> rankingList) {
        this.rankingList = rankingList;
    }

    public void resetRankingList() {
        this.rankingList = new ArrayList<>();
    }

    public AbstractButton getReturnScoreToMainBtn() {
        return this.returnScoreToMainBtn;
    }

    public JPanel getRankingPane() {
        return this.rankingPane;
    }

    public JLabel getRankingTitle() {
        return this.rankingTitle;
    }
}
