package com.ws.coyc.wsnote.UI.Layout.ImageSelectPopup;


import java.util.Comparator;

public class ComparatorImageDateAll  implements Comparator<Object>{

	@Override
	public int compare(Object arg0, Object arg1) {
		ImageInfoContentResolver d0 = (ImageInfoContentResolver) arg0;
		ImageInfoContentResolver d1 = (ImageInfoContentResolver) arg1;
		int  i = d0.date.compareTo(d1.date);
		
		return -i;
	}

}
