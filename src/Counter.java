import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.time.LocalTime;
import java.util.Random;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Counter {
    //fields
    private final JFormattedTextField minutes;
    private final JFormattedTextField seconds;
    private final JFormattedTextField statusText;
    private final JFormattedTextField pomodorosText;
    private final JTextArea currentQuote;
    private final Timer timer;
    private final int delay = 1000;
    private boolean work = true;
    private boolean paused = false;
    private boolean audio = true;
    private int pomodoros = 0;
    String[] workTimes = {"25", "00"}; //minutes, seconds
    String[] breakTimes = {"05", "00"}; //minutes, seconds
    JButton timerStart = new JButton("Start");
    JButton timerReset = new JButton("Reset");
    JButton timerPause = new JButton("Pause");
    JButton muteAudio = new JButton();

    JLabel poms1 = new JLabel();
    JLabel poms2 = new JLabel();
    JLabel poms3 = new JLabel();
    JLabel poms4 = new JLabel();
    JLabel poms5 = new JLabel();
    JLabel poms6 = new JLabel();
    JLabel poms7 = new JLabel();
    JLabel poms8 = new JLabel();
    JLabel powerPom = new JLabel();


//inspirational quotes
    public String[] quotes =
            {
                    "Without hard work, nothing grows but weeds.",
                    "Work and you’ll get what you need; work harder and you’ll get \nwhat you want.",
                    "Without labor, nothing prospers.",
                    "I’m a great believer in luck, and I find the harder I work the \nmore I have of it.",
                    "It’s not about money or connections – it’s the willingness to \noutwork and outlearn everyone.",
                    "A lot of hard work is hidden behind nice things.",
                    "I never took a day off in my 20s. Not one.",
                    "Hard work betrays none.",
                    "Determine never to be idle. No person will have occasion to \ncomplain of the want of time, who never loses any.",
                    "Much effort, much prosperity.",
                    "Talent is cheaper than table salt. What separates the \ntalented individual from the successful one is hard work.",
                    "A sign of a hard worker is one who works without complaint",
                    "To succeed, work hard, never give up and above all, \ncherish a magnificent obsession.",
                    "Successful people are not gifted; they just work hard, \n then succeed on purpose.",
                    "What you don’t sweat out when you’re young will turn into \ntears when you’re old.",
                    "Visualization works if you work hard. That’s the thing. \nYou can’t just visualize and go eat a sandwich.",
                    "The soul of the sluggard craves and gets nothing, while \nthe soul of the diligent is richly supplied.",
                    "Nothing ever comes to one, that is worth having, except \nas a result of hard work.",
                    "If people knew how hard I worked to achieve my mastery, it \nwouldn’t seem so wonderful after all.",
                    "Hard work is the recipe. It will not always get you to the top, \nbut should get you pretty near.",
                    "Opportunity is missed by most people because it is \n dressed in overalls and looks like work.",
                    "People might not get all they work for in this world, but \nthey must certainly work for all they get.",
                    "The three great essentials to achieve anything worthwhile are, \nfirst, hard work; second, stick-to-itiveness; third, common sense.",
                    "The difference between greed and ambition is a greedy \nperson desires things he isn’t prepared to work for.",
                    "This is the real secret of life – to be completely engaged \nwith what you are doing in the here and now.",
                    "Without ambition one starts nothing. Without work one finishes \nnothing. The prize will not be sent to you. You have to win it."
            };
    public String[] authors =
            {
                    "Gordon B. Hinckley",
                    "Prabakaran Thirumalai",
                    "Sophocles",
                    "Thomas Jefferson",
                    "Mark Cuban",
                    "Ralph Lauren",
                    "Bill Gates",
                    "Hachiman Hikigaya",
                    "Thomas Jefferson",
                    "Euripides",
                    "Stephen King",
                    "Sarah Price",
                    "Walt Disney",
                    "G.K. Neilson",
                    "Japanese Proverb",
                    "Jim Carrey",
                    "Proverbs 13:4, ESV",
                    "Booker T. Washington",
                    "Michelangelo",
                    "Margaret Thatcher",
                    "Thomas Edison",
                    "Frederick Douglass",
                    "Thomas Edison",
                    "Habeeb Akande",
                    "Alan Watts",
                    "Ralph Waldo Emerson"
            };


    int quotesLength = quotes.length;
    int randomNum;
    int rCount = 0;

    public void play(URL file) {
        try {
            final Clip clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));

            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP)
                        clip.close();
                }
            });
            clip.open(AudioSystem.getAudioInputStream(file));
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-6.0f); // Reduce volume by 6dB
            clip.start();



        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }

    //ClassLoader cl = Counter.getClassLoader();

    public static void formatButton(JButton b, int f, int x, int y, int w, int h) {
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(true);
        b.setFocusPainted(false);
        b.setBounds(x, y, w, h);

        b.setFont(new Font("Calibri", Font.BOLD, f));
        b.setBackground(new Color(200, 151, 120));
        b.setForeground(Color.BLACK);
    }

    public static void formatJFText(JFormattedTextField ft, String txt, int f, int x, int y, int w, int h) {
        ft.setText(txt);
        ft.setEditable(false);
        ft.setBounds(x, y, w, h);
        ft.setForeground(Color.black);
        ft.setOpaque(false);
        ft.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        ft.setFont(new Font("Calibri", Font.BOLD, f));
    }

    public void positionPom(JLabel l, int x, int y, int w, int h) {
        l.setVisible(false);
        l.setIcon(new ImageIcon(getClass().getClassLoader().getResource("poms.png")));
        l.setBounds(x, y, w, h);
    }

    private final ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            time = time.minusSeconds(1);
            if (time.equals(LocalTime.MIN)) {
                if (work) {
                    if (pomodoros < 8) {
                        if (audio) {
                            play(getClass().getClassLoader().getResource("tf.wav"));
                        }
                    } else if (audio) {
                            play(getClass().getClassLoader().getResource("ptf.wav"));
                    }

                    work = false;
                    pomodoros++;
                    if (pomodoros >= 1) {
                        poms1.setVisible(true);
                    }
                    if (pomodoros >= 2) {
                        poms2.setVisible(true);
                    }
                    if (pomodoros >= 3) {
                        poms3.setVisible(true);
                    }
                    if (pomodoros >= 4) {
                        poms4.setVisible(true);
                    }
                    if (pomodoros >= 5) {
                        poms5.setVisible(true);
                    }
                    if (pomodoros >= 6) {
                        poms6.setVisible(true);
                    }
                    if (pomodoros >= 7) {
                        poms7.setVisible(true);
                    }
                    if (pomodoros >= 8) {
                        poms8.setVisible(true);
                    }
                    if (pomodoros >= 9) {
                        powerPom.setVisible(true);
                    }
                    timer.stop();
                    minutes.setText(breakTimes[0]);
                    seconds.setText(breakTimes[1]);
                    time = LocalTime.of(0, Integer.parseInt(minutes.getText()), Integer.parseInt(seconds.getText()));
                    timerStart.setEnabled(false);
                    timer.start();
                    statusText.setText("Rest!");
                    pomodorosText.setText("Pomodoros: " + pomodoros);
                    currentQuote.setText("");
                    //break;
                } else if (!work) {
                    if (pomodoros < 9) {
                        if (audio) {
                            play(getClass().getClassLoader().getResource("ts.wav"));
                        }

                    } else if (audio) {
                        play(getClass().getClassLoader().getResource("pts.wav"));
                    }
                    work = true;
                    timer.stop();
                    minutes.setText(workTimes[0]);
                    seconds.setText(workTimes[1]);
                    time = LocalTime.of(0, Integer.parseInt(minutes.getText()), Integer.parseInt(seconds.getText()));
                    timerStart.setEnabled(false);
                    timer.start();
                    statusText.setText("Work!");
                    currentQuote.setText('"' + quotes[rCount] + '"' + "\n- " + authors[rCount]);
                    if (rCount == (quotesLength - 1)) {
                        rCount = 0;
                    } else rCount++;
                }
            }
            if (time.getSecond() < 10) {
                String secFormatted = "0" + time.getSecond();
                seconds.setText(secFormatted);
            } else
                seconds.setText(String.valueOf(time.getSecond()));

            if (time.getMinute() < 10) {
                String minFormatted = "0" + time.getMinute();
                minutes.setText(minFormatted);
            } else
                minutes.setText(String.valueOf(time.getMinute()));
        }
    };

    private LocalTime time = LocalTime.of(0, 0, 0);

    Counter() {
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("poms.png"));
        Icon muteIcon = new ImageIcon(getClass().getClassLoader().getResource("mute.png"));
        Icon audioIcon = new ImageIcon(getClass().getClassLoader().getResource("audio.png"));
        JFrame frame = new JFrame("Pomodoro");
        frame.setIconImage(img.getImage());
        timer = new Timer(delay, taskPerformer);
        minutes = new JFormattedTextField();
        seconds = new JFormattedTextField();
        statusText = new JFormattedTextField();
        pomodorosText = new JFormattedTextField();

        Random rd = new Random(); //randomly shuffle quotes array
        for (int i = quotesLength - 1; i > 0; i--) {
            int j = rd.nextInt(i + 1);
            String temp = quotes[i];
            String temp2 = authors[i];
            quotes[i] = quotes[j];
            authors[i] = authors[j];
            quotes[j] = temp;
            authors[j] = temp2;
        }

        currentQuote = new JTextArea();
        currentQuote.setEditable(false);
        currentQuote.setForeground(Color.black);
        currentQuote.setOpaque(false);
        currentQuote.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        currentQuote.setFont(new Font("Arial Narrow", Font.ITALIC, 15));

        //add colon between minutes and seconds ----------
        JTextField colon = new JTextField();
        colon.setEditable(false);
        colon.setText(":");
        colon.setOpaque(false);
        colon.setForeground(Color.black);
        colon.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        colon.setFont(new Font("Calibri", Font.BOLD, 150));


        //position UI elements
        formatJFText(minutes, workTimes[0], 150, 50, 25, 150, 150);   //minutes countdown
        colon.setBounds(205, 3, 150, 150);                        //timer colon
        formatJFText(seconds, workTimes[1], 150, 255, 25, 150, 150);  //seconds countdown
        formatButton(timerStart, 20, 54, 270, 110, 40);               //start button
        formatButton(timerPause, 20, 172, 270, 110, 40);              //pause button
        formatButton(timerReset, 20, 290, 270, 110, 40);              //reset button
        formatJFText(statusText, "", 40, 52, 205, 300, 40);       //status (work/rest) text
        formatJFText(pomodorosText, "", 22, 55, 240, 350, 30);    //no. of pomodoros text
        currentQuote.setBounds(52, 139, 390, 60);//current quote (JArea)

        muteAudio.setOpaque(false);
        muteAudio.setBounds(7, 5, 40, 40);
        muteAudio.setIcon(audioIcon);
        muteAudio.setContentAreaFilled(false);
        muteAudio.setBorderPainted(false);
        muteAudio.setFocusPainted(false);



        timerReset.setEnabled(false);
        timerPause.setEnabled(false);


        muteAudio.addActionListener(actionEvent -> {
            if (audio) {
                audio = false;
                muteAudio.setIcon(muteIcon);
            } else if (!audio) {
                audio = true;
                muteAudio.setIcon(audioIcon);
            }
        });


        timerStart.addActionListener(actionEvent -> {
            time = LocalTime.of(0, Integer.parseInt(minutes.getText()), Integer.parseInt(seconds.getText()));
            timerStart.setEnabled(false);
            timerPause.setEnabled(true);
            timerReset.setEnabled(true);
            timer.start();
            if (audio) {
                play(getClass().getClassLoader().getResource("ts.wav"));
            }
            statusText.setText("Work!");
            pomodorosText.setText("Pomodoros: " + pomodoros);
            currentQuote.setText('"' + quotes[rCount] + '"' + "\n- " + authors[rCount]);
            if (rCount == (quotesLength - 1)) {
                rCount = 0;
            } else rCount++;
        });

        timerPause.addActionListener(actionEvent -> {
            if (paused) {
                timer.start();
                timerPause.setText("Pause");
                statusText.setText("Work!");
                paused = false;
            } else if (!paused) {
                timer.stop();
                timerPause.setText("Resume");
                statusText.setText("");
                paused = true;
            }
        });

        timerReset.addActionListener(actionEvent -> {
            paused = false;
            timerPause.setText("Pause");
            timerPause.setEnabled(false);
            minutes.setText(workTimes[0]);
            seconds.setText(workTimes[1]);
            timerReset.setEnabled(true);
            time = LocalTime.of(0, 0, 0);
            timer.stop();
            work = true;
            timerStart.setEnabled(true);
            statusText.setText("");
            poms8.setVisible(false);
            poms7.setVisible(false);
            poms6.setVisible(false);
            poms5.setVisible(false);
            poms4.setVisible(false);
            poms3.setVisible(false);
            poms2.setVisible(false);
            poms1.setVisible(false);
            pomodoros = 0;
            pomodorosText.setText("");
            currentQuote.setText("");
        });

        //pomodoro icons ----------
        positionPom(poms1, 428, 15, 30, 30);
        positionPom(poms2, 428, 50, 30, 30);
        positionPom(poms3, 428, 85, 30, 30);
        positionPom(poms4, 428, 120, 30, 30);
        positionPom(poms5, 428, 155, 30, 30);
        positionPom(poms6, 428, 190, 30, 30);
        positionPom(poms7, 428, 225, 30, 30);
        positionPom(poms8, 428, 260, 30, 30);

        positionPom(powerPom, 305, 180, 75, 75);
        powerPom.setIcon(new ImageIcon(getClass().getClassLoader().getResource("goldPomSmall.png")));


        //frame-handling -----------------------
        frame.setResizable(true);
        JLabel l = new JLabel();
        frame.setBackground(new Color(201, 50, 50, 255));
        frame.setContentPane(l);
        //frame.setContentPane(new JLabel(new ImageIcon(getClass().getClassLoader().getResource("tomato.png"))));
        frame.setLocationRelativeTo(null);

        frame.add(statusText);
        frame.add(pomodorosText);
        frame.add(currentQuote);
        frame.add(minutes);
        frame.add(colon);
        frame.add(seconds);
        frame.add(timerStart); //Start button
        frame.add(timerPause); //Pause button
        frame.add(timerReset); //Reset button
        frame.add(muteAudio); //Mute button
        frame.add(poms1);
        frame.add(poms2);
        frame.add(poms3);
        frame.add(poms4);
        frame.add(poms5);
        frame.add(poms6);
        frame.add(poms7);
        frame.add(poms8);
        frame.add(powerPom);

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(486, 359);
        //frame.pack(); //pack to size of image
        frame.setVisible(true);
    }
}