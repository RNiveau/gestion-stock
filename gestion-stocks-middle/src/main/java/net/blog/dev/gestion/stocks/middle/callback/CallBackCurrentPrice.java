package net.blog.dev.gestion.stocks.middle.callback;

import net.blog.dev.gestion.stocks.back.callback.ICallback;
import net.blog.dev.gestion.stocks.core.KWeldContainer;
import net.blog.dev.gestion.stocks.core.RefreshScreen;
import net.blog.dev.gestion.stocks.middle.beans.StockListRunningBean;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Created by romainn on 18/03/2014.
 */
public class CallBackCurrentPrice implements ICallback<Float> {

    private StockListRunningBean stockListRunningBean;

    public CallBackCurrentPrice(StockListRunningBean stockListRunningBean) {
        this.stockListRunningBean = stockListRunningBean;
    }

    @Override
    public void complete(Float result) {
        if (stockListRunningBean != null && result != null) {
            stockListRunningBean.setActualPrice(result);
            KWeldContainer.getInstance().event().select(StockListRunningBean.class, new AnnotationLiteral<RefreshScreen>() {
            }).fire(stockListRunningBean);
        }
    }
}
