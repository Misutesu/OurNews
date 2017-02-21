package com.team60.ournews.module.view;

import com.team60.ournews.module.model.New;
import com.team60.ournews.module.view.base.BaseView;

import java.util.List;

/**
 * Created by Misutesu on 2017/1/23 0023.
 */

public interface SearchResultView extends BaseView {
    void searchEnd();

    void searchSuccess(List<New> news, int page);

    void searchError(String message);
}
