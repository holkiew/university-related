package lab2;

import java.awt.Component;
import java.beans.PropertyEditorSupport;

public class OdlegloscMiedzyRownaniamiEditor extends PropertyEditorSupport{

	private String[] options = { OdleglosciMiedzyRownaniami.DUZA.getNazwa(), OdleglosciMiedzyRownaniami.SREDNIA.getNazwa(), OdleglosciMiedzyRownaniami.MALA.getNazwa() };
	@Override
	public String getAsText(){
		int value = ((Integer) getValue()).intValue();
		switch(value){
		case 30:
			return OdleglosciMiedzyRownaniami.DUZA.getNazwa();
		case 45:
			return OdleglosciMiedzyRownaniami.SREDNIA.getNazwa();
		case 60:
			return OdleglosciMiedzyRownaniami.MALA.getNazwa();
		default:
			return OdleglosciMiedzyRownaniami.SREDNIA.getNazwa();
		}
	}
	@Override
	public void setAsText(String s) {
		for (int i = 0; i < options.length; i++) {
			if (options[i].equals(s)) {
				switch(i){
				case 0:
					setValue(new Integer(OdleglosciMiedzyRownaniami.DUZA.getWartosc())); 
					break;
				case 1:
					setValue(new Integer(OdleglosciMiedzyRownaniami.SREDNIA.getWartosc())); 
					break;
				case 2:
					setValue(new Integer(OdleglosciMiedzyRownaniami.MALA.getWartosc())); 
					break;
				default:
					setValue(new Integer(OdleglosciMiedzyRownaniami.SREDNIA.getWartosc())); 
					break;	
				}
				return;
			}
		}
	}
	
	@Override
	public boolean supportsCustomEditor(){
		return true;
	}
	@Override
	public Component getCustomEditor(){
		return new OdlegloscMiedzyRownaniamiPanel(this);
	}
	@Override
	public String[] getTags() {
		return options;
	}
	
	
}
