package csc143.timer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * A simple timer class
 *
 * @author Alieu Sanneh
 * @version LA10: Timer: Plus
 */
public class Timer extends JPanel {

	// buttons
	private JButton start, reset;
	
	// output label
	private JLabel time = new JLabel("0.00 seconds");
	
	// volatile flag to stop thread execution
	private static volatile boolean stop = false;

	// variables to change state based on button action on UI
	private volatile boolean update = false;
	private volatile double timeVal = 0;

	/**
	 * Constructor for the class.
	 */
	public Timer() {

		setLayout(new BorderLayout());

		// create a toolbar panel and add buttons into that
		JPanel toolbar = new JPanel();
		
		// create start button and add action listener
		start = new JButton("Start");
		start.addActionListener(new ActionListener() {
      
  	      /**
	       * The method from action listener
          * @param e  The action that triggered the event
	       */
			@Override
			public void actionPerformed(ActionEvent e) {
				String label = e.getActionCommand();

				// if start or continue is clicked, start the timer
				if (label.equals("Start") || label.equals("Continue")) {
					update = true;
					start.setText("Pause");

					
				} else if (label.equals("Pause")) {
					// on clicking pause, stop the timer.
					
					update = false;
					start.setText("Continue");
				}

			}
		});

		// create reset button and add action listener
		reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
  	      /**
	       * The method from action listener
          * @param e  The action that triggered the event
	       */
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// on clicking reset, to stop the timer as well as reset its value
				timeVal = 0.00;
				time.setText(String.format("%.2f seconds", timeVal));
				update = false;
				
				// also reset the text of the button
				start.setText("Start");
			}
		});

		// add buttons to toolbar
		toolbar.add(start, BorderLayout.CENTER);
		toolbar.add(reset, BorderLayout.CENTER);

		// create the output label and align it in center of the panel
		time = new JLabel("0.00 seconds");
		time.setHorizontalAlignment(SwingConstants.CENTER);
		time.setVerticalAlignment(SwingConstants.CENTER);

		// set the font 
		Font f = new Font("Arial", Font.BOLD, 36);
		time.setFont(f);

		// make the panel as single grid layout
		JPanel outputPane = new JPanel();
		outputPane.setLayout(new GridLayout());
		outputPane.add(time);

		// add the toolbar & outputPane to the frame
		add(toolbar, BorderLayout.NORTH);
		add(outputPane, BorderLayout.SOUTH);

		// set preferred size
		outputPane.setPreferredSize(new Dimension(400, 270));

		// create a thread that gets called every 10 millis
		Thread t = new Thread(new Runnable() {
      
  	      /**
	       * The method from Runnable
          * starting the thread causes this method to be called in 
          * that separately executing thread
	       */
			@Override
			public void run() {

				// stop will become true, when user closes the window and then thread cleanup happens
				while (!stop) {
					
					// if we need to update the timer further
					if (update) {
						timeVal += 0.01;
						time.setText(String.format("%.2f seconds", timeVal));
					}
					
					// sleep the thread for next 10ms
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		
		// start the thread
		t.start();
	}
   /**
    * The application method that runs the program
    *
    * @param args The command-line arguments
    */	     
	public static void main(String[] args) {
		JFrame frame = new JFrame("My Timer");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocation(125, 125);

		JPanel spacer = new JPanel();
		spacer.add(new Timer());
		frame.add(spacer);

		// set start size
		frame.pack();
		frame.setVisible(true);
		

		// to cleanup the thread.
		// this function gets called when user closes the window
		frame.addWindowListener(new WindowAdapter() {
       	/**
	        * The method from WindowListerner
	        * Invoked when the user attempts to close the 
           * window from the window's system menu
           *
	        * @param e The action the triggered the event
	        */     
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	Timer.stop = true;
		    }
		});
	}

}
