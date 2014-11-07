package net.blog.dev.gestion.stocks.middle.helpers.api;

import net.blog.dev.gestion.stocks.dto.DtoStock;
import net.blog.dev.gestion.stocks.middle.beans.StockListBean;

/**
 * Created by romainn on 03/11/2014.
 */
public interface IStockHelper {

    StockListBean stockDtoToBean(DtoStock dtoStock);

}
