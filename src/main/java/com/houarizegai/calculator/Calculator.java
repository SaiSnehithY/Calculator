package com.houarizegai.calculator;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.awt.Color;
import javax.swing.*;
import java.lang.Math;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static javax.swing.SwingUtilities.*;

@SuppressWarnings("serial")
class TestDrawingCanvas extends JPanel {
  private DrawingCanvas drawingCanvas = new DrawingCanvas();

  public TestDrawingCanvas() {
    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
    btnPanel.add(new JButton(new ClearAction()));

    setLayout(new BorderLayout());
    add(drawingCanvas, BorderLayout.CENTER);
    add(btnPanel, BorderLayout.PAGE_END);
  }

  private class ClearAction extends AbstractAction {
    public ClearAction() {
      super("Clear Canvas");
      putValue(MNEMONIC_KEY, KeyEvent.VK_C);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      drawingCanvas.clear();
    }
  }

  public static void createAndShowGui() {
    TestDrawingCanvas mainPanel = new TestDrawingCanvas();

    JFrame frame = new JFrame("Drawing Canvas");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.getContentPane().add(mainPanel);
    frame.pack();
    frame.setLocationByPlatform(true);
    frame.setVisible(true);
  }
}

@SuppressWarnings("serial")
class DrawingCanvas extends JPanel {
  private static final int PREF_W = 800;
  private static final int PREF_H = 600;
  public static final Color LINE_COLOR = Color.PINK;
  public static final Stroke IMG_STROKE = new BasicStroke(10 f);
  private Color lineColor = LINE_COLOR;
  private BufferedImage image;

  public DrawingCanvas() {
    setBackground(Color.WHITE);
    image = new BufferedImage(PREF_W, PREF_H, BufferedImage.TYPE_INT_ARGB);
    MyMouse myMouse = new MyMouse();
    addMouseListener(myMouse);
    addMouseMotionListener(myMouse);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (image != null) {
      g.drawImage(image, 0, 0, this);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    if (isPreferredSizeSet()) {
      return super.getPreferredSize();
    }
    return new Dimension(PREF_W, PREF_H);
  }

  public void setLineColor(Color lineColor) {
    this.lineColor = lineColor;
  }

  public void clear() {
    image = new BufferedImage(PREF_W, PREF_H, BufferedImage.TYPE_INT_ARGB);
    repaint();
  }

  private class MyMouse extends MouseAdapter {
    private Graphics2D imgG2d;
    private Point p1;

    @Override
    public void mousePressed(MouseEvent e) {
      if (e.getButton() != MouseEvent.BUTTON1) {
        return;
      }

      imgG2d = image.createGraphics();
      imgG2d.setColor(lineColor);
      imgG2d.setStroke(IMG_STROKE);
      p1 = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      drawLine(e);
      imgG2d.dispose();
      p1 = null;
      imgG2d = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      drawLine(e);
      p1 = e.getPoint();
    }

    private void drawLine(MouseEvent e) {
      if (imgG2d == null || p1 == null) {
        return;
      }
      Point p2 = e.getPoint();
      int x1 = p1.x;
      int y1 = p1.y;
      int x2 = p2.x;
      int y2 = p2.y;
      imgG2d.drawLine(x1, y1, x2, y2);
      repaint();
    }
  }
}

public class Calculator {

  private static final int WINDOW_WIDTH = 410;
  private static final int WINDOW_HEIGHT = 600;
  private static final int BUTTON_WIDTH = 80;
  private static final int BUTTON_HEIGHT = 70;
  private static final int MARGIN_X = 20;
  private static final int MARGIN_Y = 60;

  private JFrame window; // Main window
  private JComboBox < String > comboCalcType, comboTheme;
  private JTextField inText; // Input
  private JButton btnC, btnBack, btnMod, btnDiv, btnMul, btnSub, btnAdd, btnSqr,
  btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btne, btnpi,
  btnPoint, btnEqual, btnRoot, btnPower, btnLog, btnSin, btnCos,
  btnSec, btnCsc, btnCot, btnTan;

  private char opt = ' '; // Save the operator
  private boolean go = true; // For calculate with Opt != (=)
  private boolean addWrite = true; // Connect numbers in display
  private double val = 0; // Save the value typed for calculation

  int[] x = {
    MARGIN_X,
    MARGIN_X + 90,
    200,
    290,
    380
  };
  int[] y = {
    MARGIN_Y,
    MARGIN_Y + 100,
    MARGIN_Y + 180,
    MARGIN_Y + 260,
    MARGIN_Y + 340,
    MARGIN_Y + 420
  };

  /*
      Mx Calculator: 
      X = Row
      Y = Column
  
      +-------------------+
      |   +-----------+   |   y[0]
      |   |           |   |
      |   +-----------+   |
      |                   |
      |   C  <-   %   /   |   y[1]
      |   7   8   9   *   |   y[2]
      |   4   5   6   -   |   y[3]
      |   1   2   3   +   |   y[4]
      |   .   0     =     |   y[5]
      +-------------------+
       x[0] x[1] x[2] x[3]
  
  */

  /*    
      +-------------------+
      |   +-----------+   |   y[0]
      |   |           |   |
      |   +-----------+   |
      |                   |
      |   0   1   1   3   |   y[1]
      |   4   5   6   7   |   y[2]
      |   8   9   10  11  |   y[3]
      |   12  13  14  15  |   y[4]
      |   16  17    18    |   y[5]
      +-------------------+
       x[0] x[1] x[2] x[3]
  
  */

  public Calculator() {
    window = new JFrame("Calculator");
    window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    window.setLocationRelativeTo(null); // Move window to center
    comboCalcType = initCombo(new String[] {
      "Standard",
      "Scientific",
      "Scientific++"
    }, 20, 30, "Calculator type", calcTypeSwitchEventConsumer);

    comboTheme = initCombo(new String[] {
      "Simple",
      "Rainbow",
      "Contrast",
      "DarkTheme"
    }, 230, 30, "Theme", themeSwitchEventConsumer);

    inText = new JTextField("0");
    inText.setBounds(x[0], y[0], 350, 70);
    inText.setEditable(false);
    inText.setBackground(Color.WHITE);
    inText.setFont(new Font("Comic Sans MS", Font.PLAIN, 33));
    window.add(inText);

    btnC = initBtn("C", x[0], y[1], event -> {
      repaintFont();
      inText.setText("0");
      opt = ' ';
      val = 0;
    });

    btnBack = initBtn("<-", x[1], y[1], event -> {
      repaintFont();
      String str = inText.getText();
      StringBuilder str2 = new StringBuilder();
      for (int i = 0; i < (str.length() - 1); i++) {
        str2.append(str.charAt(i));
      }
      if (str2.toString().equals("")) {
        inText.setText("0");
      } else {
        inText.setText(str2.toString());
      }
    });

    btnMod = initBtn("%", x[2], y[1], event -> {
      repaintFont();
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = calc(val, inText.getText(), opt);
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = '%';
          go = false;
          addWrite = false;
        }
    });

    btnDiv = initBtn("/", x[3], y[1], event -> {
      repaintFont();
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = calc(val, inText.getText(), opt);
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = '/';
          go = false;
          addWrite = false;
        } else {
          opt = '/';
        }
    });

    btn7 = initBtn("7", x[0], y[2], event -> {
      repaintFont();
      if (addWrite) {
        if (Pattern.matches("[0]*", inText.getText())) {
          inText.setText("7");
        } else {
          inText.setText(inText.getText() + "7");
        }
      } else {
        inText.setText("7");
        addWrite = true;
      }
      go = true;
    });

    btn8 = initBtn("8", x[1], y[2], event -> {
      repaintFont();
      if (addWrite) {
        if (Pattern.matches("[0]*", inText.getText())) {
          inText.setText("8");
        } else {
          inText.setText(inText.getText() + "8");
        }
      } else {
        inText.setText("8");
        addWrite = true;
      }
      go = true;
    });

    btn9 = initBtn("9", x[2], y[2], event -> {
      repaintFont();
      if (addWrite) {
        if (Pattern.matches("[0]*", inText.getText())) {
          inText.setText("9");
        } else {
          inText.setText(inText.getText() + "9");
        }
      } else {
        inText.setText("9");
        addWrite = true;
      }
      go = true;
    });

    btnMul = initBtn("*", x[3], y[2], event -> {
      repaintFont();
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = calc(val, inText.getText(), opt);
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = '*';
          go = false;
          addWrite = false;
        } else {
          opt = '*';
        }
    });

    btn4 = initBtn("4", x[0], y[3], event -> {
      repaintFont();
      if (addWrite) {
        if (Pattern.matches("[0]*", inText.getText())) {
          inText.setText("4");
        } else {
          inText.setText(inText.getText() + "4");
        }
      } else {
        inText.setText("4");
        addWrite = true;
      }
      go = true;
    });

    btn5 = initBtn("5", x[1], y[3], event -> {
      repaintFont();
      if (addWrite) {
        if (Pattern.matches("[0]*", inText.getText())) {
          inText.setText("5");
        } else {
          inText.setText(inText.getText() + "5");
        }
      } else {
        inText.setText("5");
        addWrite = true;
      }
      go = true;
    });

    btn6 = initBtn("6", x[2], y[3], event -> {
      repaintFont();
      if (addWrite) {
        if (Pattern.matches("[0]*", inText.getText())) {
          inText.setText("6");
        } else {
          inText.setText(inText.getText() + "6");
        }
      } else {
        inText.setText("6");
        addWrite = true;
      }
      go = true;
    });

    btnSub = initBtn("-", x[3], y[3], event -> {
      repaintFont();
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = calc(val, inText.getText(), opt);
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }

          opt = '-';
          go = false;
          addWrite = false;
        } else {
          opt = '-';
        }
    });

    btn1 = initBtn("1", x[0], y[4], event -> {
      repaintFont();
      if (addWrite) {
        if (Pattern.matches("[0]*", inText.getText())) {
          inText.setText("1");
        } else {
          inText.setText(inText.getText() + "1");
        }
      } else {
        inText.setText("1");
        addWrite = true;
      }
      go = true;
    });

    btn2 = initBtn("2", x[1], y[4], event -> {
      repaintFont();
      if (addWrite) {
        if (Pattern.matches("[0]*", inText.getText())) {
          inText.setText("2");
        } else {
          inText.setText(inText.getText() + "2");
        }
      } else {
        inText.setText("2");
        addWrite = true;
      }
      go = true;
    });

    btn3 = initBtn("3", x[2], y[4], event -> {
      repaintFont();
      if (addWrite) {
        if (Pattern.matches("[0]*", inText.getText())) {
          inText.setText("3");
        } else {
          inText.setText(inText.getText() + "3");
        }
      } else {
        inText.setText("3");
        addWrite = true;
      }
      go = true;
    });

    btnpi = initBtn("\u03C0", x[4], y[4], event -> {
      repaintFont();
      if (addWrite) {
        if (Pattern.matches("[0]*", inText.getText())) {
          inText.setText("3.14");
        } else {
          inText.setText(inText.getText() + "3.14");
        }
      } else {
        inText.setText("3.14");
        addWrite = true;
      }
      go = true;
    });
    btnpi.setVisible(false);

    btne = initBtn("e", x[4], y[5], event -> {
      repaintFont();
      if (addWrite) {
        if (Pattern.matches("[0]*", inText.getText())) {
          inText.setText("2.78");
        } else {
          inText.setText(inText.getText() + "2.78");
        }
      } else {
        inText.setText("2.78");
        addWrite = true;
      }
      go = true;
    });
    btne.setVisible(false);

    btnAdd = initBtn("+", x[3], y[4], event -> {
      repaintFont();
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = calc(val, inText.getText(), opt);
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = '+';
          go = false;
          addWrite = false;
        } else {
          opt = '+';
        }
    });

    btnPoint = initBtn(".", x[0], y[5], event -> {
      repaintFont();
      if (addWrite) {
        if (!inText.getText().contains(".")) {
          inText.setText(inText.getText() + ".");
        }
      } else {
        inText.setText("0.");
        addWrite = true;
      }
      go = true;
    });

    btn0 = initBtn("0", x[1], y[5], event -> {
      repaintFont();
      if (addWrite) {
        if (Pattern.matches("[0]*", inText.getText())) {
          inText.setText("0");
        } else {
          inText.setText(inText.getText() + "0");
        }
      } else {
        inText.setText("0");
        addWrite = true;
      }
      go = true;
    });

    btnEqual = initBtn("=", x[2], y[5], event -> {
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = calc(val, inText.getText(), opt);
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = '=';
          addWrite = false;
        }
    });
    btnEqual.setSize(2 * BUTTON_WIDTH + 10, BUTTON_HEIGHT);

    btnRoot = initBtn("\u221A", x[4], y[1], event -> {
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = Math.sqrt(Double.parseDouble(inText.getText()));
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = '\u221A';
          addWrite = false;
        }
    });
    btnRoot.setVisible(false);

    btnSqr = initBtn("\u00B2", x[4] + 90, y[1], event -> {
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = Math.pow(Double.parseDouble(inText.getText()), 2);
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = '\u221A';
          addWrite = false;
        }
    });
    btnSqr.setVisible(false);

    btnPower = initBtn("pow", x[4], y[2], event -> {
      repaintFont();
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = calc(val, inText.getText(), opt);
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = '^';
          go = false;
          addWrite = false;
        } else {
          opt = '^';
        }
    });
    btnPower.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
    btnPower.setVisible(false);

    btnLog = initBtn("ln", x[4], y[3], event -> {
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = Math.log(Double.parseDouble(inText.getText()));
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = 'l';
          addWrite = false;
        }
    });
    btnLog.setVisible(false);

    btnSin = initBtn("sin", x[4], y[4], event -> {
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = Math.sin(Double.parseDouble(inText.getText()));
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = 's';
          addWrite = false;
        }
    });
    btnSin.setVisible(false);

    btnCos = initBtn("cos", x[4], y[5], event -> {
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = Math.cos(Double.parseDouble(inText.getText()));
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = 'c';
          addWrite = false;
        }
    });
    btnCos.setVisible(false);

    btnTan = initBtn("tan", x[4] + 90, y[2], event -> {
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = Math.tan(Double.parseDouble(inText.getText()));
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = 'c';
          addWrite = false;
        }
    });
    btnTan.setVisible(false);

    btnCsc = initBtn("csc", x[4] + 90, y[3], event -> {
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = 1 / Math.sin(Double.parseDouble(inText.getText()));
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = 'c';
          addWrite = false;
        }
    });
    btnCsc.setVisible(false);

    btnSec = initBtn("sec", x[4] + 90, y[4], event -> {
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = 1 / Math.cos(Double.parseDouble(inText.getText()));
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = 'c';
          addWrite = false;
        }
    });
    btnSec.setVisible(false);

    btnCot = initBtn("cot", x[4] + 90, y[5], event -> {
      if (Pattern.matches("([-]?\\d+[.]\\d*)|(\\d+)", inText.getText()))
        if (go) {
          val = 1 / Math.tan(Double.parseDouble(inText.getText()));
          if (Pattern.matches("[-]?[\\d]+[.][0]*", String.valueOf(val))) {
            inText.setText(String.valueOf((int) val));
          } else {
            inText.setText(String.valueOf(val));
          }
          opt = 'c';
          addWrite = false;
        }
    });
    btnCot.setVisible(false);

    window.setLayout(null);
    window.setResizable(false);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close button clicked? = End The process
    window.setVisible(true);
  }

  private JComboBox < String > initCombo(String[] items, int x, int y, String toolTip, Consumer < ItemEvent > consumerEvent) {
    JComboBox < String > combo = new JComboBox < > (items);
    combo.setBounds(x, y, 140, 25);
    combo.setToolTipText(toolTip);
    combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
    combo.addItemListener(consumerEvent::accept);
    window.add(combo);

    return combo;
  }

  private JButton initBtn(String label, int x, int y, ActionListener event) {
    JButton btn = new JButton(label);
    btn.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
    btn.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btn.addActionListener(event);
    btn.setFocusable(false);
    window.add(btn);

    return btn;
  }

  public double calc(double x, String input, char opt) {
    inText.setFont(inText.getFont().deriveFont(Font.PLAIN));
    double y = Double.parseDouble(input);
    switch (opt) {
    case '+':
      return x + y;
    case '-':
      return x - y;
    case '*':
      return x * y;
    case '/':
      return x / y;
    case '%':
      return x % y;
    case '^':
      return Math.pow(x, y);
    default:
      inText.setFont(inText.getFont().deriveFont(Font.PLAIN));
      return y;
    }
  }

  private void repaintFont() {
    inText.setFont(inText.getFont().deriveFont(Font.PLAIN));
  }

  private Consumer < ItemEvent > calcTypeSwitchEventConsumer = event -> {
    if (event.getStateChange() != ItemEvent.SELECTED) return;

    String selectedItem = (String) event.getItem();
    int offset = 40;
    switch (selectedItem) {
    case "Standard":
      inText.setBounds(x[0], y[0], 350, 70);
      comboCalcType.setBounds(20, 30, 140, 25);
      comboTheme.setBounds(230, 30, 140, 25);
      window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
      btnRoot.setVisible(false);
      btnPower.setVisible(false);
      btnLog.setVisible(false);
      btnSin.setVisible(false);
      btnCos.setVisible(false);
      btnTan.setVisible(false);
      btnSec.setVisible(false);
      btnCsc.setVisible(false);
      btnCot.setVisible(false);
      btnSqr.setVisible(false);
      btnpi.setVisible(false);
      btne.setVisible(false);
      break;
    case "Scientific":
      offset = 40;
      inText.setBounds(x[0] + offset, y[0], 350, 70);
      comboCalcType.setBounds(20 + offset, 30, 140, 25);
      comboTheme.setBounds(230 + offset, 30, 140, 25);
      window.setSize(WINDOW_WIDTH + (2 * offset), WINDOW_HEIGHT);
      btnRoot.setVisible(true);
      btnPower.setVisible(true);
      btnLog.setVisible(true);
      btnSin.setVisible(false);
      btnCos.setVisible(false);
      btnTan.setVisible(false);
      btnSec.setVisible(false);
      btnCsc.setVisible(false);
      btnCot.setVisible(false);
      btnSqr.setVisible(false);
      btnpi.setVisible(true);
      btne.setVisible(true);
      break;
    case "Scientific++":
      offset = 85;
      inText.setBounds(x[0] + offset, y[0], 350, 70);
      comboCalcType.setBounds(20 + offset, 30, 140, 25);
      comboTheme.setBounds(230 + offset, 30, 140, 25);
      window.setSize(WINDOW_WIDTH + (2 * offset), WINDOW_HEIGHT);
      btnSqr.setVisible(true);
      btnRoot.setVisible(true);
      btnPower.setVisible(true);
      btnLog.setVisible(true);
      btnSin.setVisible(true);
      btnCos.setVisible(true);
      btnTan.setVisible(true);
      btnSec.setVisible(true);
      btnCsc.setVisible(true);
      btnCot.setVisible(true);
      btnpi.setVisible(false);
      btne.setVisible(false);
      break;
    }
  };

  private Consumer < ItemEvent > themeSwitchEventConsumer = event -> {
    if (event.getStateChange() != ItemEvent.SELECTED) return;

    String selectedTheme = (String) event.getItem();
    switch (selectedTheme) {
    case "Simple":
      window.getContentPane().setBackground(null);
      btnC.setBackground(null);
      btnBack.setBackground(null);
      btnMod.setBackground(null);
      btnDiv.setBackground(null);
      btnMul.setBackground(null);
      btnSub.setBackground(null);
      btnAdd.setBackground(null);
      btnRoot.setBackground(null);
      btnLog.setBackground(null);
      btnSin.setBackground(null);
      btnCos.setBackground(null);
      btnTan.setBackground(null);
      btnSec.setBackground(null);
      btnCsc.setBackground(null);
      btnCot.setBackground(null);
      btnSqr.setBackground(null);
      btnPower.setBackground(null);
      btnEqual.setBackground(null);
      btn0.setBackground(null);
      btn1.setBackground(null);
      btn2.setBackground(null);
      btn3.setBackground(null);
      btn4.setBackground(null);
      btn5.setBackground(null);
      btn6.setBackground(null);
      btn7.setBackground(null);
      btn8.setBackground(null);
      btn9.setBackground(null);
      btnpi.setBackground(null);
      btne.setBackground(null);
      btnPoint.setBackground(null);

      btnC.setForeground(Color.BLACK);
      btnBack.setForeground(Color.BLACK);
      btnMod.setForeground(Color.BLACK);
      btnDiv.setForeground(Color.BLACK);
      btnMul.setForeground(Color.BLACK);
      btnSub.setForeground(Color.BLACK);
      btnAdd.setForeground(Color.BLACK);
      btnEqual.setForeground(Color.BLACK);
      btnLog.setForeground(Color.BLACK);
      btnPower.setForeground(Color.BLACK);
      btnRoot.setForeground(Color.BLACK);
      btnSin.setForeground(Color.BLACK);
      btnCos.setForeground(Color.BLACK);
      btnSqr.setForeground(Color.BLACK);
      btnTan.setForeground(Color.BLACK);
      btnSec.setForeground(Color.BLACK);
      btnCsc.setForeground(Color.BLACK);
      btnCot.setForeground(Color.BLACK);
      btnpi.setForeground(Color.BLACK);
      btne.setForeground(Color.BLACK);
      break;
    case "Contrast":
      window.getContentPane().setBackground(Color.BLACK);
      btnC.setBackground(Color.WHITE);
      btnBack.setBackground(Color.WHITE);
      btnMod.setBackground(Color.WHITE);
      btnDiv.setBackground(Color.WHITE);
      btnMul.setBackground(Color.WHITE);
      btnSub.setBackground(Color.WHITE);
      btnAdd.setBackground(Color.WHITE);
      btnRoot.setBackground(Color.WHITE);
      btnLog.setBackground(Color.WHITE);
      btnSin.setBackground(Color.WHITE);
      btnCos.setBackground(Color.WHITE);
      btnTan.setBackground(Color.WHITE);
      btnSec.setBackground(Color.WHITE);
      btnCsc.setBackground(Color.WHITE);
      btnCot.setBackground(Color.WHITE);
      btnSqr.setBackground(Color.WHITE);
      btnPower.setBackground(Color.WHITE);
      btnEqual.setBackground(Color.WHITE);
      btn0.setBackground(Color.WHITE);
      btn1.setBackground(Color.WHITE);
      btn2.setBackground(Color.WHITE);
      btn3.setBackground(Color.WHITE);
      btn4.setBackground(Color.WHITE);
      btn5.setBackground(Color.WHITE);
      btn6.setBackground(Color.WHITE);
      btn7.setBackground(Color.WHITE);
      btn8.setBackground(Color.WHITE);
      btn9.setBackground(Color.WHITE);
      btnPoint.setBackground(Color.WHITE);
      btnpi.setBackground(Color.WHITE);
      btne.setBackground(Color.WHITE);

      btnC.setForeground(Color.BLACK);
      btnBack.setForeground(Color.BLACK);
      btnMod.setForeground(Color.BLACK);
      btnDiv.setForeground(Color.BLACK);
      btnMul.setForeground(Color.BLACK);
      btnSub.setForeground(Color.BLACK);
      btnAdd.setForeground(Color.BLACK);
      btnEqual.setForeground(Color.BLACK);
      btnLog.setForeground(Color.BLACK);
      btnPower.setForeground(Color.BLACK);
      btnRoot.setForeground(Color.BLACK);
      btnSin.setForeground(Color.BLACK);
      btnCos.setForeground(Color.BLACK);
      btnTan.setForeground(Color.BLACK);
      btnSec.setForeground(Color.BLACK);
      btnCsc.setForeground(Color.BLACK);
      btnCot.setForeground(Color.BLACK);
      btnSqr.setForeground(Color.BLACK);
      btnpi.setForeground(Color.BLACK);
      btne.setForeground(Color.BLACK);
      break;
    case "Rainbow":
      window.getContentPane().setBackground(null);
      btnC.setBackground(Color.RED);
      btnBack.setBackground(Color.ORANGE);
      btnMod.setBackground(Color.GREEN);
      btnDiv.setBackground(Color.PINK);
      btnMul.setBackground(Color.PINK);
      btnSub.setBackground(Color.PINK);
      btnAdd.setBackground(Color.PINK);
      btnRoot.setBackground(Color.PINK);
      btnLog.setBackground(Color.PINK);
      btnSin.setBackground(Color.PINK);
      btnCos.setBackground(Color.PINK);
      btnTan.setBackground(Color.PINK);
      btnSec.setBackground(Color.PINK);
      btnCsc.setBackground(Color.PINK);
      btnCot.setBackground(Color.PINK);
      btnSqr.setBackground(Color.PINK);
      btnPower.setBackground(Color.PINK);
      btnEqual.setBackground(Color.BLUE);
      btn0.setBackground(Color.WHITE);
      btn1.setBackground(Color.WHITE);
      btn2.setBackground(Color.WHITE);
      btn3.setBackground(Color.WHITE);
      btn4.setBackground(Color.WHITE);
      btn5.setBackground(Color.WHITE);
      btn6.setBackground(Color.WHITE);
      btn7.setBackground(Color.WHITE);
      btn8.setBackground(Color.WHITE);
      btn9.setBackground(Color.WHITE);
      btnPoint.setBackground(Color.WHITE);
      btnpi.setBackground(Color.WHITE);
      btne.setBackground(Color.WHITE);

      btnC.setForeground(Color.WHITE);
      btnBack.setForeground(Color.WHITE);
      btnMod.setForeground(Color.WHITE);
      btnDiv.setForeground(Color.WHITE);
      btnMul.setForeground(Color.WHITE);
      btnSub.setForeground(Color.WHITE);
      btnAdd.setForeground(Color.WHITE);
      btnEqual.setForeground(Color.WHITE);
      btnLog.setForeground(Color.WHITE);
      btnPower.setForeground(Color.WHITE);
      btnRoot.setForeground(Color.WHITE);
      btnSin.setForeground(Color.WHITE);
      btnCos.setForeground(Color.WHITE);
      btnTan.setForeground(Color.WHITE);
      btnSec.setForeground(Color.WHITE);
      btnCsc.setForeground(Color.WHITE);
      btnCot.setForeground(Color.WHITE);
      btnSqr.setForeground(Color.WHITE);
      btnpi.setForeground(Color.WHITE);
      btne.setForeground(Color.WHITE);
      break;
    case "DarkTheme":
      final Color primaryDarkColor = new Color(141, 38, 99);
      final Color secondaryDarkColor = new Color(171, 171, 171);
      final Color tertiaryDarkColor = new Color(68, 68, 68);

      window.getContentPane().setBackground(tertiaryDarkColor);
      btn0.setBackground(secondaryDarkColor);
      btn1.setBackground(secondaryDarkColor);
      btn2.setBackground(secondaryDarkColor);
      btn3.setBackground(secondaryDarkColor);
      btn4.setBackground(secondaryDarkColor);
      btn5.setBackground(secondaryDarkColor);
      btn6.setBackground(secondaryDarkColor);
      btn7.setBackground(secondaryDarkColor);
      btn8.setBackground(secondaryDarkColor);
      btn9.setBackground(secondaryDarkColor);
      btnPoint.setBackground(secondaryDarkColor);

      btnC.setForeground(secondaryDarkColor);
      btnBack.setForeground(secondaryDarkColor);
      btnMod.setForeground(secondaryDarkColor);
      btnDiv.setForeground(secondaryDarkColor);
      btnMul.setForeground(secondaryDarkColor);
      btnSub.setForeground(secondaryDarkColor);
      btnAdd.setForeground(secondaryDarkColor);
      btnEqual.setForeground(secondaryDarkColor);
      btnLog.setForeground(secondaryDarkColor);
      btnPower.setForeground(secondaryDarkColor);
      btnRoot.setForeground(secondaryDarkColor);
      btnSin.setForeground(secondaryDarkColor);
      btnCos.setForeground(secondaryDarkColor);
      btnTan.setForeground(secondaryDarkColor);
      btnSec.setForeground(secondaryDarkColor);
      btnCsc.setForeground(secondaryDarkColor);
      btnCot.setForeground(secondaryDarkColor);
      btnSqr.setForeground(secondaryDarkColor);
      btnpi.setForeground(secondaryDarkColor);
      btne.setForeground(secondaryDarkColor);

      btnC.setBackground(primaryDarkColor);
      btnBack.setBackground(primaryDarkColor);
      btnMod.setBackground(primaryDarkColor);
      btnDiv.setBackground(primaryDarkColor);
      btnMul.setBackground(primaryDarkColor);
      btnSub.setBackground(primaryDarkColor);
      btnAdd.setBackground(primaryDarkColor);
      btnRoot.setBackground(primaryDarkColor);
      btnLog.setBackground(primaryDarkColor);
      btnPower.setBackground(primaryDarkColor);
      btnEqual.setBackground(primaryDarkColor);
      btnSin.setBackground(primaryDarkColor);
      btnCos.setBackground(primaryDarkColor);
      btnTan.setBackground(primaryDarkColor);
      btnSec.setBackground(primaryDarkColor);
      btnCsc.setBackground(primaryDarkColor);
      btnCot.setBackground(primaryDarkColor);
      btnSqr.setBackground(primaryDarkColor);
      btnpi.setForeground(primaryDarkColor);
      btne.setForeground(primaryDarkColor);
      break;
    }
  };

  public static void main(String[] args) {
    new Calculator();
    invokeLater(new Runnable() {
      public void run() {
        TestDrawingCanvas.createAndShowGui();
      }
    });
  }
}