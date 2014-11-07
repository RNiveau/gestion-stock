/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import net.blog.dev.gestion.stocks.dto.DtoMovement;
import net.blog.dev.gestion.stocks.dto.DtoStock;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Kiva
 * 
 */
public abstract class AbstractMService {

	/**
	 * @param listStocks
	 */
	protected void sortStockList(final List<DtoStock> listStocks) {
		if (listStocks != null) {
			Collections.sort(listStocks, new Comparator<DtoStock>() {

				@Override
				public int compare(DtoStock o1, DtoStock o2) {
					if (o1 == null || o2 == null || o1.getBuyOrdre() == null
							|| o2.getBuyOrdre() == null
							|| o1.getBuyOrdre().getDate() == null
							|| o2.getBuyOrdre().getDate() == null)
						return 0;
					final DateTime date1 = new DateTime(o1.getBuyOrdre()
							.getDate());
					final DateTime date2 = new DateTime(o2.getBuyOrdre()
							.getDate());
					if (date1.isBefore(date2))
						return -1;
					if (date1.isAfter(date2))
						return 1;
					return 0;
				}
			});
		}
	}

	protected void sortMovementList(final List<DtoMovement> listMovements) {
		if (listMovements != null) {
			Collections.sort(listMovements, new Comparator<DtoMovement>() {

				@Override
				public int compare(DtoMovement o1, DtoMovement o2) {
					if (o1 == null || o2 == null || o1.getDate() == null
							|| o2.getDate() == null)
						return 0;
					final DateTime date1 = new DateTime(o1.getDate());
					final DateTime date2 = new DateTime(o2.getDate());
					if (date1.isBefore(date2))
						return -1;
					if (date1.isAfter(date2))
						return 1;
					return 0;
				}
			});
		}
	}

	protected List<String> getListEnum(final Enum<?>[] enums) {
		List<String> list = new ArrayList<>();
		for (Enum<?> enu : enums)
			list.add(enu.name());
		return list;
    }




}
