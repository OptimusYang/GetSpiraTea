import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;


public class DownloadUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8452900625039662723L;
	private JTextField textField;
	private JPasswordField passwordField;
	private JTextField textField_1;
	private JProgressBar progressBar;
	private JButton btnNewButton_1;
	private JButton btnNewButton;
	private JTextArea textArea = new JTextArea();
	private JTextField textField_2;
	private JTextField textField_3;

	public JButton getGeneratebutton(){
		return btnNewButton_1;
	}
	public DownloadUI() throws MalformedURLException {
		setIconImage(Toolkit.getDefaultToolkit().getImage("//icon.png"));
		setTitle("Download SpiraTeam Incidents Tool");
		setSize(new Dimension(480, 607));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(37, 92, 77, 18);
		getContentPane().add(lblPassword);
		
		JLabel lblUserName = new JLabel("User Name:");
		lblUserName.setBounds(37, 50, 77, 18);
		getContentPane().add(lblUserName);
		
		textField = new JTextField();
		textField.setToolTipText("Enter your username");
		textField.setBounds(183, 50, 205, 28);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setToolTipText("Enter your password");
		passwordField.setBounds(183, 88, 205, 28);
		getContentPane().add(passwordField);
		
		JLabel lblSpirateamAccount = new JLabel("SpiraTeam Account");
		lblSpirateamAccount.setFont(new Font("Calibri", Font.BOLD, 14));
		lblSpirateamAccount.setBounds(37, 13, 170, 18);
		getContentPane().add(lblSpirateamAccount);
		
		JLabel lblExcelPath = new JLabel("Generate Excel Path:");
		lblExcelPath.setBounds(37, 130, 128, 18);
		getContentPane().add(lblExcelPath);
		
		textField_1 = new JTextField();
		textField_1.setToolTipText("File Path");
		textField_1.setEnabled(false);
		textField_1.setBounds(183, 126, 205, 28);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		btnNewButton = new JButton("...");
		btnNewButton.setToolTipText("select one directory");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jf = new JFileChooser();
				jf.setMultiSelectionEnabled(false);  
				jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  
				 int returnValue = jf.showOpenDialog(null); 
				 if (returnValue == JFileChooser.APPROVE_OPTION)  
			        {  
			            File f = jf.getSelectedFile();
			            textField_1.setText(f.getAbsolutePath());
			        }  
			}
		});
		btnNewButton.setBounds(408, 125, 29, 28);
		getContentPane().add(btnNewButton);
		
		
		
		btnNewButton_1 = new JButton("Click Here To Generate!");
		btnNewButton_1.setToolTipText("Click this button to generate a excel file!");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textField.getText().trim().equals("")){
					JOptionPane.showMessageDialog(
							getContentPane(),
						    "Please input username.",
						    "Warning Message",
						    JOptionPane.WARNING_MESSAGE
						);
					return;
				}
				if(passwordField.getPassword().toString().trim().equals("")){
					JOptionPane.showMessageDialog(
							getContentPane(),
						    "Please input password.",
						    "Warning Message",
						    JOptionPane.WARNING_MESSAGE
						);
					return;
				}
				if(textField_1.getText().trim().equals("")){
					JOptionPane.showMessageDialog(
							getContentPane(),
						    "Please choose a directory for excel generate.",
						    "Warning Message",
						    JOptionPane.WARNING_MESSAGE
						);
					return;
				}
				btnNewButton_1.setEnabled(false);
				textField.setEnabled(false);
				passwordField.setEnabled(false);
				btnNewButton.setEnabled(false);
				progressBar.setVisible(true);
				progressBar.setStringPainted(true);
				 progressBar.setValue(0); 
				 textArea.setText("");

				new Thread(new Runnable(){
					@SuppressWarnings("deprecation")
					public void run() {
						Reporter reporter = new Reporter();
						DownloadSpiraTeam downloader = new DownloadSpiraTeam();
						downloader.setUsername(textField.getText());
						downloader.setPassword(passwordField.getText());
						downloader.setReporter(reporter);
						downloader.setFilePath(textField_1.getText());
						new Thread(downloader).start();
						boolean end = false;
						int curPct = 0;
						while(!end){
							if(reporter.hasNewMessage()){
								Message m = reporter.getFirst();
								curPct=m.getPercent(); 
								textArea.append(m.getMessage()+"\n");
								if(m.getPercent() == 100){
									end = true;
								}
							}
							if(progressBar.getValue() < curPct && curPct != 100){
								progressBar.setValue(progressBar.getValue()+1);
							}
							if(curPct == 100){
								progressBar.setValue(100);
							}
							try {
								Thread.sleep(900);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						 
						}
						
						btnNewButton_1.setEnabled(true);
						textField.setEnabled(true);
						passwordField.setEnabled(true);
						btnNewButton.setEnabled(true);
					}}).start();

			}
		});
		btnNewButton_1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		btnNewButton_1.setBounds(37, 268, 351, 35);
		getContentPane().add(btnNewButton_1);
		progressBar = new JProgressBar();
		progressBar.setBounds(37, 327, 351, 18);
		progressBar.setVisible(false);
		getContentPane().add(progressBar);
		
		JLabel lblLog = new JLabel("Log:");
		lblLog.setBounds(37, 355, 54, 15);
		getContentPane().add(lblLog);
		textArea.setToolTipText("Log Window(Tim.Yang)");
		
		//textArea.setBorder(new LineBorder(new Color(0, 0, 0)));
		textArea.setLineWrap(true);
		textArea.setBackground(Color.WHITE);
		textArea.setEditable(false);
		//textArea.setBounds(37, 325, 351, 153);
		JScrollPane scr = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scr.setBounds(37, 380, 351, 153);
		scr.setViewportView(textArea);
		getContentPane().add(scr);
		
		JLabel lblNewLabel_1 = new JLabel("<html><a href=\"www.baidu.com\" >Contact Me</a></html");
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				 try {
					Runtime.getRuntime().exec("cmd.exe /c start " + "mailto:tim.yang@ebaotech.com");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		});
		lblNewLabel_1.setToolTipText("tim.yang@ebaotech.com");
		lblNewLabel_1.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(387, 543, 77, 26);
		getContentPane().add(lblNewLabel_1);
		
		JCheckBox chckbxProxy = new JCheckBox("Proxy");
		chckbxProxy.setBackground(Color.WHITE);
		chckbxProxy.setBounds(37, 196, 103, 23);
		getContentPane().add(chckbxProxy);
		
		textField_2 = new JTextField();
		textField_2.setBounds(81, 225, 170, 21);
		getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblHost = new JLabel("Host:");
		lblHost.setBounds(37, 225, 34, 15);
		getContentPane().add(lblHost);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(261, 228, 54, 15);
		getContentPane().add(lblPort);
		
		textField_3 = new JTextField();
		textField_3.setBounds(295, 225, 93, 21);
		getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		setBackground(Color.WHITE);
		setForeground(Color.WHITE);
		setResizable(false);
	}
	
	
	public static void main(String[] args) throws Exception{
		BeautyEyeLNFHelper.frameBorderStyle =
		BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
		BeautyEyeLNFHelper.launchBeautyEyeLNF();
		DownloadUI ui = new DownloadUI();
		ui.setVisible(true);
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setText("Try to connect to SpiraTeam...");
		lblNewLabel.setBounds(37, 162, 400, 28);
		ui.getContentPane().add(lblNewLabel);
		ImageIcon image = new ImageIcon(new URL("https://bdpteamprod.argogroupus.com/SpiraTeam/App_Themes/InflectraTheme/Images/app-icon-57x57.png"));
		if(image.getIconHeight() == -1){
			lblNewLabel.setForeground(Color.RED);
			lblNewLabel.setText("Connect to SpiraTeam failed, please check your network.");
			ui.getGeneratebutton().setEnabled(false);
		}else{
			lblNewLabel.setForeground(Color.BLUE);
			lblNewLabel.setText("Connect to SpiraTeam Successfully.");
			ui.getGeneratebutton().setEnabled(true);

		}
	}
}
