package com.team60.ournews.module.view;

import com.team60.ournews.module.model.New;
import com.team60.ournews.module.view.base.BaseView;

import java.util.List;

/**
 * Created by Misutesu on 2017/1/20 0020.
 */

public interface TypeView extends BaseView {
    void getNewListEnd();

    void getNewListSuccess(List<New> news, int page);

    void getNewListError(String message, int page);
}
