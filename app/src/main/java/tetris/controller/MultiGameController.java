package tetris.controller;

import javax.swing.*;
import java.awt.event.*;
import tetris.model.*;

public class MultiGameController extends SingleGameController {

    GameController gamePlayer1;
    GameController gamePlayer2;
    GameController gameRobot;
    RobotController robotController;

    int multiMode;

    JLabel multiGameTimeLabel;
    JTextPane multiGameFocusing;

    public MultiGameController(PlayerController playerController, ViewController viewController) {
        super(playerController, viewController);
        multiMode = 0;
        multiGameTimeLabel = gameView.getMultiGameTimeLabel();
        JTextPane gamepane1 = gameView.getPlayerOneGameBoardPane();
        JTextPane nextBlockPane1 = gameView.getPlayerOneNextBlockPane();
        JTextPane attackLinePane1 = gameView.getPlayerOneAttackLinePane();
        JLabel scoreLabel1 = gameView.getPlayerOneScoreLabel();
        JTextPane gamepane2 = gameView.getPlayerTwoGameBoardPane();
        JTextPane nextBlockPane2 = gameView.getPlayerTwoNextBlockPane();
        JTextPane attackLinePane2 = gameView.getPlayerTwoAttackLinePane();
        JLabel scoreLabel2 = gameView.getPlayerTwoScoreLabel();
        multiGameFocusing = gamepane2;
        gamePlayer1 = new GameController(gamepane1, nextBlockPane1, attackLinePane1, scoreLabel1, multiGameFocusing) {
            @Override
            public void doAfterGameOver() {
                playerTwoWin();
            }

            @Override
            public void doBeforeTakeOutNextBlock() {
                if (attackLines > 0) {
                    underAttack();
                }
                if (blockDeque.isEmpty()) {
                    generateBlockRandomizer(GameController.NORMAL_MODE);
                    blockDeque.addAll(randomBlockList);
                    opponentBlockDeque.addAll(randomBlockList);
                }
            }
        };

        gamePlayer2 = new GameController(gamepane2, nextBlockPane2, attackLinePane2, scoreLabel2, multiGameFocusing) {
            @Override
            public void doAfterGameOver() {
                playerOneWin();
            }

            @Override
            public void doBeforeTakeOutNextBlock() {
                if (attackLines > 0) {
                    underAttack();
                }
                if (blockDeque.isEmpty()) {
                    generateBlockRandomizer(GameController.NORMAL_MODE);
                    blockDeque.addAll(randomBlockList);
                    opponentBlockDeque.addAll(randomBlockList);
                }
            }
        };

        gameRobot = new GameController(gamepane2, nextBlockPane2, attackLinePane2, scoreLabel2, multiGameFocusing) {
            @Override
            public void doWhenGameStart() {
                robotController.findMove(currentBlock);
                robotController.moveBlock();
            }

            @Override
            public void doAfterGameOver() {
                playerOneWin();
            }

            @Override
            public void doBeforeTakeOutNextBlock() {
                if (attackLines > 0) {
                    underAttack();
                }
                if (blockDeque.isEmpty()) {
                    generateBlockRandomizer(GameController.NORMAL_MODE);
                    blockDeque.addAll(randomBlockList);
                    opponentBlockDeque.addAll(randomBlockList);
                }
                robotController.findMove(nextBlock);
            }

            @Override
            public void doAfterTakeOutNextBlock() {

                robotController.moveBlock();
            }
        };

        robotController = new RobotController(this.gameRobot);

        gameView.getVictoryLabel().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gameView.resetMultiGameDisplayPane();
                gameView.resetGameView();
                viewController.transitView(viewController.contentPane, mainView, gameView);
            }
        });
    }

    public void startLocalGame(Setting setting) {
        this.setting = setting;
        multiMode = 0;
        isSingleGameModeFlag = false;
        gamePlayer1.loadSetting(setting);
        gamePlayer2.loadSetting(setting);
        gamePlayer1.setOpponentPlayer(gamePlayer2);
        gamePlayer2.setOpponentPlayer(gamePlayer1);
        generateBlockRandomizer(GameController.NORMAL_MODE);
        gamePlayer1.setPlayerKeys(setting.getRotateKey(), setting.getMoveDownKey(), setting.getMoveLeftKey(),
                setting.getMoveRightKey(), setting.getStackKey());
        gamePlayer2.setPlayerKeys(setting.getRotate2Key(), setting.getMoveDown2Key(), setting.getMoveLeft2Key(),
                setting.getMoveRight2Key(), setting.getStack2Key());

        int currentResoultion = gameView.getWidth() * gameView.getHeight();

        gamePlayer1.startGame(GameController.NORMAL_MODE, gameMode, randomBlockList, currentResoultion);
        gamePlayer2.startGame(GameController.NORMAL_MODE, gameMode, randomBlockList, currentResoultion);
        gameTime = 0;
        showTime(multiGameTimeLabel);
        showMode();
        startTimer(gameView.getMultiGameDisplayTimeLabel(), multiGameTimeLabel);
    }

    public void startRobotGame(Setting setting) {
        this.setting = setting;
        multiMode = 1;
        isSingleGameModeFlag = false;
        gamePlayer1.setOpponentPlayer(gameRobot);
        gameRobot.setOpponentPlayer(gamePlayer1);
        generateBlockRandomizer(GameController.NORMAL_MODE);

        gamePlayer1.setPlayerKeys(setting.getRotateKey(), setting.getMoveDownKey(), setting.getMoveLeftKey(),
                setting.getMoveRightKey(), setting.getStackKey());
        gamePlayer1.loadSetting(setting);
        gameRobot.loadSetting(setting);

        int currentResoultion = gameView.getWidth() * gameView.getHeight();
        gamePlayer1.startGame(GameController.NORMAL_MODE, gameMode, randomBlockList, currentResoultion);
        gameRobot.startGame(GameController.NORMAL_MODE, gameMode, randomBlockList, currentResoultion);
        gameTime = 0;
        robotController.startRobot();
        showTime(multiGameTimeLabel);
        showMode();
        startTimer(gameView.getMultiGameDisplayTimeLabel(), multiGameTimeLabel);
    }

    protected void continueMultiGame() {
        gameView.resetMultiStopPanel();
        if (multiMode == 0) {
            gamePlayer1.continuGame();
            gamePlayer2.continuGame();
        } else if (multiMode == 1) {
            gameRobot.continuGame();
            robotController.startRobot();
        }

        gameTimer.restart();
        multiGameFocusing.requestFocus();
    }

    protected void restartMultiGame() {
        gamePlayer1.resetGame();
        gamePlayer2.resetGame();
        gameRobot.resetGame();
        gameView.resetMultiStopPanel();
        if (multiMode == 0)
            startLocalGame(setting);
        else
            startRobotGame(setting);
        multiGameFocusing.requestFocus();
    }

    protected void playerOneWin() {
        gamePlayer1.endGame();
        gamePlayer2.endGame();
        gameRobot.endGame();
        robotController.stopRobot();
        gameTimer.stop();
        gameView.setPlayerOneWin();
        Timer timer = new Timer(5000, e -> {
            gameView.getVictoryLabel().requestFocus();
        });
        timer.setRepeats(false);
        timer.start();
    }

    protected void playerTwoWin() {
        gamePlayer1.endGame();
        gamePlayer2.endGame();
        gameRobot.endGame();
        robotController.stopRobot();
        gameTimer.stop();
        gameView.setPlayerTwoWin();
        Timer timer = new Timer(5000, e -> {
            gameView.getVictoryLabel().requestFocus();
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    protected void doAfterTimeAttack() {
        if (isSingleGameModeFlag)
            super.doAfterTimeAttack();
        else {
            if (gamePlayer1.score > gamePlayer2.score) {
                playerOneWin();
            } else if (gamePlayer1.score < gamePlayer2.score) {
                playerTwoWin();
            } else {
                gameView.getVictoryLabel().setText("Draw");
                gameView.getDepeatLabel().setText("Draw");
                playerOneWin();
            }
        }
    }

    protected void setItemFreqency(int itemFreqency) {
        gamePlayer.setItemFreqency(itemFreqency);
    }

}