import javax.swing.JTextArea;

public class ZmieniaczPolaTekstowego implements Runnable{
	private JTextArea text;
	private char[] textReklamy;
	private int counter=0;
	public ZmieniaczPolaTekstowego(JTextArea text, Reklama reklama){
		this.text = text;
		this.textReklamy = reklama.getEkran().toCharArray();
	}
	private String kolejnyZnak(){
		if(counter >= textReklamy.length){
			counter = 0;
			text.setText("");
		}
		return String.valueOf(textReklamy[counter++]);
	}
	@Override
	public void run() {
		while(true){
		try {
			Thread.sleep(130);
			text.append(kolejnyZnak());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		}
		
	}
}
