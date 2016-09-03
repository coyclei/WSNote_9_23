package com.ws.coyc.wsnote.Utils;


import com.ws.coyc.wsnote.Data.Person;

import java.util.Comparator;

public class ComparatorPerson implements Comparator<Object>{

	@Override
	public int compare(Object arg0, Object arg1) {
		Person d0 = (Person) arg0;
		Person d1 = (Person) arg1;
		if(d0.all_prise_out>d1.all_prise_out)
		{
			return -1;
		}else if(d0.all_prise_out==d1.all_prise_out)
		{
			return 0;
		}else
		{
			return 1;
		}
	}

}
