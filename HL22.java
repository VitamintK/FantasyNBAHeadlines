import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import java.lang.Object.*;
import java.net.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.awt.event.*;
//import javax.swing.event.*;

/**
Prints out the current top ESPN News headlines for Fantasy Basketball.
Asks the user for input of the amount of headlines to be shown.
The program will also print out the url for the news story of the last headline shown.
*/
public class HL22 extends JPanel implements ActionListener, FocusListener{
	final String apikey = Config.apikey;
	String lakenews = "http://api.espn.com/v1/sports/basketball/nba/teams/13/news?_accept=text/xml&apikey="+apikey;
	String fantasy = "http://api.espn.com/v1/fantasy/basketball/news?_accept=text/xml&apikey="+apikey;
	String news = fantasy;
	Document document;
	protected JTextField textField = new JTextField("Enter the amount of headlines you wish to view");
    protected JTextArea textArea;
	protected JTextArea urlArea;
    private final static String newline = "\n";
	
	public static String getResult(String request){
		try{
			String line;
			String xml = "";
			URL url = new URL(request);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while((line = rd.readLine()) != null){
				xml +=line;
			}	
			return(xml);

		} catch(Exception e){
			e.printStackTrace();
			return("");
		}
	}
	
	public Document getDocs(){
		Document urlresult;
		try{
			String theresult = getResult(news);
			DocumentBuilderFactory domf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = domf.newDocumentBuilder();
			urlresult = db.parse(news);
			return urlresult;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}	
	}
	
	public String getStrings(int index,Document document){
	 	//NodeList att = document.getElementsByTagName("shortLinkText");
		NodeList att = document.getElementsByTagName("title");
		//for(int i=0;i<att.getLength();i++){
		return(att.item(index).getTextContent());
		//}
	}
	

	public void actionPerformed(ActionEvent evt) {
        String text = textField.getText();
		NodeList titles = document.getElementsByTagName("title");
		int cap;
		try{
		if (Integer.parseInt(text) < document.getElementsByTagName("title").getLength()){
			cap = Integer.parseInt(text);
		} else{
			cap = document.getElementsByTagName("title").getLength();
		}
		} catch(Exception e){
			cap = document.getElementsByTagName("title").getLength();
		}
		
		textArea.setText("");
		int ghettocounter = 0;
		for(int i=0;i<titles.getLength();i++){
			if(ghettocounter == cap){
			break;
			}
			if(titles.item(i).getParentNode().getNodeName() == "headlinesItem"){
				textArea.append(getStrings(i,document)+newline);
				String fullurl = titles.item(i).getParentNode().getChildNodes().item(6).getChildNodes().item(1).getFirstChild().getTextContent();
				String workingurl = fullurl.substring(0,fullurl.length()-22);
				urlArea.setText(workingurl);
				ghettocounter++;
			}
		}
        textField.selectAll();
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

	public void focusGained(FocusEvent e) {
        //if (textField.getText() == "Enter the amount of headlines you wish to view"){
			//textField.selectAll();
			//urlArea.selectAll();
			try{
			JTextArea ddd = (JTextArea)(e.getComponent());
			ddd.selectAll();
			}catch(Exception f){
			textField.selectAll();
			}
			/*
			try{
			JTextField eee = (JTextField)(e.getComponent());
			eee.selectAll();
			}catch(Exception f){
			
			}*/
		//	}
    }
    public void focusLost(FocusEvent e) {
        //System.out.println(e.getComponent());
    }
	
	public HL22(){
		super(new GridBagLayout());
        //textField = new JTextField(20);
        textField.addActionListener(this);
		textField.addFocusListener(this);
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
		urlArea = new JTextArea(1,1);
		urlArea.setEditable(false);
		urlArea.addFocusListener(this);
        JScrollPane scrollPane = new JScrollPane(textArea);		
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);
		add(urlArea,c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
		document = getDocs();
	}
	public static void main(String[] args){
		JFrame frame = new JFrame("Main Menu");
		frame.setSize(1000,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new HL22());
        frame.setVisible(true);
	}
	
}