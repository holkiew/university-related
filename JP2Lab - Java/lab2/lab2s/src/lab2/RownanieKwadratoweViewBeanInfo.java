package lab2;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class RownanieKwadratoweViewBeanInfo extends SimpleBeanInfo {

	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor odlegloscMiedzyRownaniamiDescriptor = new PropertyDescriptor("odlegloscMiedzyRownaniami",
					RownanieKwadratoweViewBean.class);
			odlegloscMiedzyRownaniamiDescriptor.setPropertyEditorClass(OdlegloscMiedzyRownaniamiEditor.class);
			
			return new PropertyDescriptor[] { odlegloscMiedzyRownaniamiDescriptor };
		} catch (java.beans.IntrospectionException e) {
			e.printStackTrace();
		}

		return null;
	}
}
