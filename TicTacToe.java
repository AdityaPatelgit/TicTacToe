import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

// ─────────────────────────────────────────────────────────────
//  Custom rounded button with hover + symbol painting
// ─────────────────────────────────────────────────────────────
class RoundButton extends Canvas implements MouseListener {

    private String  label      = "";
    private boolean hover      = false;
    private boolean clickable  = true;

    // colours
    private static final Color BG_IDLE   = new Color(30, 30, 50);
    private static final Color BG_HOVER  = new Color(45, 45, 75);
    private static final Color BG_LOCKED = new Color(25, 25, 40);
    private static final Color COL_X     = new Color(255,  80, 100);   // red-pink
    private static final Color COL_O     = new Color( 80, 200, 255);   // sky-blue
    private static final Color BORDER    = new Color(70,  70, 110);
    private static final Color BORDER_HL = new Color(130, 130, 200);

    interface ClickHandler { void onClick(RoundButton src); }
    private ClickHandler handler;

    RoundButton(ClickHandler h) {
        this.handler = h;
        addMouseListener(this);
        setPreferredSize(new Dimension(110, 110));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void setLabel(String s) { label = s; clickable = false; repaint(); }
    public String getLabel()        { return label; }
    public void reset()             { label = ""; clickable = true; hover = false; repaint(); }
    public void setClickable(boolean v) { clickable = v; }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        int arc = 22;

        // shadow
        g2.setColor(new Color(0,0,0,60));
        g2.fillRoundRect(4, 4, w-4, h-4, arc, arc);

        // background
        Color bg = (!clickable && label.isEmpty()) ? BG_LOCKED
                 : (hover && clickable)            ? BG_HOVER
                 :                                   BG_IDLE;
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w-4, h-4, arc, arc);

        // border
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(hover && clickable ? BORDER_HL : BORDER);
        g2.drawRoundRect(0, 0, w-5, h-5, arc, arc);

        // symbol
        if (!label.isEmpty()) {
            g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int pad = 22;
            int x1 = pad, y1 = pad, x2 = w-pad-4, y2 = h-pad-4;

            if (label.equals("X")) {
                g2.setColor(COL_X);
                g2.drawLine(x1, y1, x2, y2);
                g2.drawLine(x2, y1, x1, y2);
            } else {
                g2.setColor(COL_O);
                g2.drawOval(x1, y1, x2-x1, y2-y1);
            }
        }
    }

    // ── MouseListener ──────────────────────────────────────
    @Override public void mouseClicked(MouseEvent e)  { if (clickable && handler != null) handler.onClick(this); }
    @Override public void mouseEntered(MouseEvent e)  { hover = true;  repaint(); }
    @Override public void mouseExited (MouseEvent e)  { hover = false; repaint(); }
    @Override public void mousePressed(MouseEvent e)  {}
    @Override public void mouseReleased(MouseEvent e) {}
}

// ─────────────────────────────────────────────────────────────
//  Gradient panel (for background painting)
// ─────────────────────────────────────────────────────────────
class GradPanel extends Panel {
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth(), h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, new Color(15,15,35), w, h, new Color(30,20,60));
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
        super.paint(g);  // paint children
    }

}

// ─────────────────────────────────────────────────────────────
//  Main game window
// ─────────────────────────────────────────────────────────────
public class TicTacToe extends Frame {

    // ── board ───────────────────────────────────────────────
    private RoundButton[] cells = new RoundButton[9];
    private int turn = 0;   // even = X, odd = O

    // ── score ───────────────────────────────────────────────
    private int scoreX = 0, scoreO = 0, scoreDraw = 0;

    // ── UI labels ───────────────────────────────────────────
    private Label statusLbl, scoreLbl, turnLbl;
    private Button restartBtn;

    // ── win combinations ────────────────────────────────────
    private static final int[][] WINS = {
        {0,1,2},{3,4,5},{6,7,8},   // rows
        {0,3,6},{1,4,7},{2,5,8},   // columns
        {0,4,8},{2,4,6}            // diagonals
    };

    // ── colours ─────────────────────────────────────────────
    private static final Color COL_X    = new Color(255, 80, 100);
    private static final Color COL_O    = new Color( 80,200, 255);
    private static final Color COL_TXT  = new Color(220,220,255);
    private static final Color COL_DIM  = new Color(130,130,170);
    private static final Color BG_DARK  = new Color( 15, 15,  35);
    private static final Color ACCENT   = new Color(130,100,255);

    // ────────────────────────────────────────────────────────
    TicTacToe() {
        setTitle(" TIC-TAC-TOE ");
        setSize(500, 680);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(BG_DARK);
        setLayout(new BorderLayout());

        // ── top panel ───────────────────────────────────────
        Panel topPanel = new Panel(new GridLayout(3, 1, 0, 4));
        topPanel.setBackground(BG_DARK);

        Label title = new Label(" TIC-TAC-TOE ", Label.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(COL_TXT);

        turnLbl = new Label("Player X's Turn", Label.CENTER);
        turnLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        turnLbl.setForeground(COL_X);

        statusLbl = new Label(" ", Label.CENTER);
        statusLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusLbl.setForeground(new Color(255,220,80));

        topPanel.add(title);
        topPanel.add(turnLbl);
        topPanel.add(statusLbl);

        // ── grid panel ──────────────────────────────────────
        Panel gridWrap = new Panel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gridWrap.setBackground(BG_DARK);

        Panel grid = new Panel(new GridLayout(3, 3, 10, 10));
        grid.setBackground(new Color(50, 50, 90));   // gap colour = separator

        for (int i = 0; i < 9; i++) {
            final int idx = i;
            cells[i] = new RoundButton(src -> onCellClick(idx));
            grid.add(cells[i]);
        }

        gridWrap.add(grid);

        // ── score panel ─────────────────────────────────────
        Panel scorePanel = new Panel(new FlowLayout(FlowLayout.CENTER, 30, 6));
        scorePanel.setBackground(BG_DARK);

        scoreLbl = new Label("X: 0   Draw: 0   O: 0", Label.CENTER);
        scoreLbl.setFont(new Font("Monospaced", Font.BOLD, 15));
        scoreLbl.setForeground(COL_DIM);
        scorePanel.add(scoreLbl);

        // ── restart button ──────────────────────────────────
        restartBtn = new Button("  New Game  ");
        restartBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        restartBtn.setBackground(ACCENT);
        restartBtn.setForeground(Color.WHITE);
        restartBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        restartBtn.addActionListener(e -> resetGame());

        Panel btnPanel = new Panel();
        btnPanel.setBackground(BG_DARK);
        btnPanel.add(restartBtn);

        // ── assemble ─────────────────────────────────────────
        Panel center = new Panel(new BorderLayout(0, 16));
        center.setBackground(BG_DARK);
        center.add(gridWrap,  BorderLayout.CENTER);
        center.add(scorePanel, BorderLayout.SOUTH);

        Panel south = new Panel(new BorderLayout());
        south.setBackground(BG_DARK);
        south.add(btnPanel, BorderLayout.CENTER);

        add(topPanel,  BorderLayout.NORTH);
        add(center,    BorderLayout.CENTER);
        add(south,     BorderLayout.SOUTH);

        // ── close window ────────────────────────────────────
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); System.exit(0); }
        });

        setVisible(true);
    }

    // ── cell clicked ────────────────────────────────────────
    private void onCellClick(int idx) {
        String symbol = (turn % 2 == 0) ? "X" : "O";
        cells[idx].setLabel(symbol);
        turn++;

        String winner = checkWinner();
        if (winner != null) {
            if (winner.equals("Draw")) {
                scoreDraw++;
                statusLbl.setForeground(new Color(200,200,80));
                statusLbl.setText("It's a Draw!");
                turnLbl.setText(" ");
            } else {
                if (winner.equals("X")) scoreX++; else scoreO++;
                Color wc = winner.equals("X") ? COL_X : COL_O;
                statusLbl.setForeground(wc);
                statusLbl.setText("Player " + winner + " Wins!");
                turnLbl.setText(" ");
            }
            updateScore();
            lockAll();
        } else {
            String next = (turn % 2 == 0) ? "X" : "O";
            Color nc = next.equals("X") ? COL_X : COL_O;
            turnLbl.setForeground(nc);
            turnLbl.setText("Player " + next + "'s Turn");
        }
    }

    // ── win / draw detection ────────────────────────────────
    private String checkWinner() {
        for (int[] w : WINS) {
            String a = cells[w[0]].getLabel();
            String b = cells[w[1]].getLabel();
            String c = cells[w[2]].getLabel();
            if (!a.isEmpty() && a.equals(b) && b.equals(c)) {
                highlightWin(w);
                return a;
            }
        }
        // check draw
        for (RoundButton cell : cells)
            if (cell.getLabel().isEmpty()) return null;
        return "Draw";
    }

    // ── highlight winning cells ──────────────────────────────
    private void highlightWin(int[] indices) {
        for (int i : indices) {
            cells[i].setBackground(new Color(60, 20, 20));
            cells[i].repaint();
        }
    }

    // ── lock all cells ───────────────────────────────────────
    private void lockAll() {
        for (RoundButton cell : cells) cell.setClickable(false);
    }

    // ── reset board ─────────────────────────────────────────
    private void resetGame() {
        turn = 0;
        for (RoundButton cell : cells) {
            cell.reset();
            cell.setBackground(null);   // clear any highlight
        }
        statusLbl.setText(" ");
        turnLbl.setForeground(COL_X);
        turnLbl.setText("Player X's Turn");
    }

    // ── update score label ───────────────────────────────────
    private void updateScore() {
        scoreLbl.setText("X: " + scoreX + "   Draw: " + scoreDraw + "   O: " + scoreO);
    }

    // ── entry point ─────────────────────────────────────────
    public static void main(String[] args) {
        new TicTacToe();
    }
}