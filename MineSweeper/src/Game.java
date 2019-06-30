

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Game extends JPanel {

	Random random = new Random();
	
    private final int CELL_SIZE = 15;
    private final int COVER_FOR_CELL = 10;
    private final int FLAG_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int FLAGGED_MINE_CELL = COVERED_MINE_CELL + FLAG_FOR_CELL;
    
    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_FLAG = 11;
    private final int DRAW_WRONG_FLAG = 12;

    private final int NUM_ROWS = 16;
    private final int NUM_COLS = 16; 
    private final int NUM_MINES = (int) Math.round((random.nextGaussian() * 5) + ( NUM_ROWS * NUM_COLS / 5)); 
    
    private final int BOARD_WIDTH = NUM_ROWS * CELL_SIZE + 1;
    private final int BOARD_HEIGHT = NUM_COLS * CELL_SIZE + 1;

    private int[] field;
    private int minesLeft;
    private int numCells;
    
    private boolean inGame;
    private BufferedImage spriteSheet;
    private final JLabel statusbar;

    public Game(JLabel statusbar) {

        this.statusbar = statusbar;
        initBoard();
    }

    private void initBoard() {

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        try {
				spriteSheet = ImageIO.read(new File("rsc/spriteSheet.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
        

        addMouseListener(new click());
        newGame();
    }

    private void newGame() {

        int cell;

        Random random = new Random();
        inGame = true;
        minesLeft = NUM_MINES;

        numCells = NUM_ROWS * NUM_COLS;
        field = new int[numCells];

        for (int i = 0; i < numCells; i++) {
            field[i] = COVER_FOR_CELL;
        }

        statusbar.setText("Mines left: " + Integer.toString(minesLeft));

        int i = 0;
        
        while (i < NUM_MINES) {

            int position = (int) (numCells * random.nextDouble());

            if ((position < numCells)
                    && (field[position] != COVERED_MINE_CELL)) {

                int current_col = position % NUM_COLS;
                field[position] = COVERED_MINE_CELL;
                i++;

                if (current_col > 0) {
                    cell = position - 1 - NUM_COLS;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position - 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }

                    cell = position + NUM_COLS - 1;
                    if (cell < numCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }

                cell = position - NUM_COLS;
                if (cell >= 0) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }
                
                cell = position + NUM_COLS;
                if (cell < numCells) {
                    if (field[cell] != COVERED_MINE_CELL) {
                        field[cell] += 1;
                    }
                }

                if (current_col < (NUM_COLS - 1)) {
                    cell = position - NUM_COLS + 1;
                    if (cell >= 0) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + NUM_COLS + 1;
                    if (cell < numCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + 1;
                    if (cell < numCells) {
                        if (field[cell] != COVERED_MINE_CELL) {
                            field[cell] += 1;
                        }
                    }
                }
            }
        }
    }

    public void find_empty_cells(int j) {

        int current_col = j % NUM_COLS;
        int cell;

        if (current_col > 0) {
            cell = j - NUM_COLS - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j - 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + NUM_COLS - 1;
            if (cell < numCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

        cell = j - NUM_COLS;
        if (cell >= 0) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        cell = j + NUM_COLS;
        if (cell < numCells) {
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL) {
                    find_empty_cells(cell);
                }
            }
        }

        if (current_col < (NUM_COLS - 1)) {
            cell = j - NUM_COLS + 1;
            if (cell >= 0) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + NUM_COLS + 1;
            if (cell < numCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + 1;
            if (cell < numCells) {
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {

        int uncover = 0;

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {

                int cell = field[(i * NUM_COLS) + j];

                if (inGame && cell == MINE_CELL) {
                    inGame = false;
                }

                if (!inGame) {
                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == FLAGGED_MINE_CELL) {
                        cell = DRAW_FLAG;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_FLAG;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }

                } else {
                    if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_FLAG;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }
                //spriteSheet(cell);
                int x = (cell % 4) * CELL_SIZE;
                int y = (cell / 4) * CELL_SIZE;
                g.drawImage(spriteSheet.getSubimage(x, y, CELL_SIZE, CELL_SIZE), (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }

        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        } else if (!inGame) {
            statusbar.setText("Game lost");
        }
    }

    private class click extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            int cellCol = x / CELL_SIZE;
            int cellRow = y / CELL_SIZE;

            boolean doRepaint = false;

            if (!inGame) {
                newGame();
                repaint();
            }

            if ((x < NUM_COLS * CELL_SIZE) && (y < NUM_ROWS * CELL_SIZE)) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (field[(cellRow * NUM_COLS) + cellCol] > MINE_CELL) {
                        doRepaint = true;

                        if (field[(cellRow * NUM_COLS) + cellCol] <= COVERED_MINE_CELL) {
                            if (minesLeft > 0) {
                                field[(cellRow * NUM_COLS) + cellCol] += FLAG_FOR_CELL;
                                minesLeft--;
                                String msg = Integer.toString(minesLeft);
                                statusbar.setText("Mines Left: " + msg);
                            } else {
                                statusbar.setText("No marks left");
                            }
                        } else {

                            field[(cellRow * NUM_COLS) + cellCol] -= FLAG_FOR_CELL;
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            statusbar.setText("Mines Left: " + msg);
                        }
                    }

                } else {

                    if (field[(cellRow * NUM_COLS) + cellCol] > COVERED_MINE_CELL) {
                        return;
                    }

                    if ((field[(cellRow * NUM_COLS) + cellCol] > MINE_CELL)
                            && (field[(cellRow * NUM_COLS) + cellCol] < FLAGGED_MINE_CELL)) {

                        field[(cellRow * NUM_COLS) + cellCol] -= COVER_FOR_CELL;
                        doRepaint = true;

                        if (field[(cellRow * NUM_COLS) + cellCol] == MINE_CELL) {
                            inGame = false;
                        }
                        
                        if (field[(cellRow * NUM_COLS) + cellCol] == EMPTY_CELL) {
                            find_empty_cells((cellRow * NUM_COLS) + cellCol);
                        }
                    }
                }

                if (doRepaint) {
                    repaint();
                }

            }
        }
    }
}

