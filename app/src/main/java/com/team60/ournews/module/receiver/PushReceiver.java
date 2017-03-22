package com.team60.ournews.module.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.team60.ournews.module.bean.ManagerUser;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.ui.activity.MainActivity;
import com.team60.ournews.module.ui.activity.NewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wujiaquan on 2017/3/13.
 */

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jsonObject = new JSONObject(extra);
                JSONObject dataJSON = jsonObject.getJSONObject("pushContent");
                switch (dataJSON.getString("type")) {
                    case "new":
                        JSONObject newJSON = dataJSON.getJSONObject("data");
                        New n = new New();
                        n.setId(newJSON.getLong("id"));
                        n.setTitle(newJSON.getString("title"));
                        n.setCover(newJSON.getString("cover"));
                        n.setAbstractContent(newJSON.getString("abstract"));
                        n.setCreateTime(newJSON.getString("create_time"));
                        n.setType(newJSON.getInt("type"));
                        n.setCommentNum(newJSON.getInt("comment_num"));
                        n.setCollectionNum(newJSON.getInt("collection_num"));
                        n.setHistoryNum(newJSON.getInt("history_num"));
                        ManagerUser managerUser = new ManagerUser();
                        JSONObject managerJSON = newJSON.getJSONObject("manager");
                        managerUser.setId(managerJSON.getLong("id"));
                        managerUser.setNickName(managerJSON.getString("nick_name"));
                        managerUser.setSex(managerJSON.getInt("sex"));
                        managerUser.setSign(managerJSON.getString("sign"));
                        managerUser.setBirthday(managerJSON.getInt("birthday"));
                        managerUser.setPhoto(managerJSON.getString("photo"));
                        n.setManagerUser(managerUser);

                        Intent newIntent = new Intent(context, NewActivity.class);
                        newIntent.putExtra(New.class.getName(), n);
                        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(newIntent);
                        break;
                    default:
                        Intent defaultIntent = new Intent(context, MainActivity.class);
                        defaultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(defaultIntent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Intent defaultIntent = new Intent(context, MainActivity.class);
                defaultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(defaultIntent);
            }
        }
    }
}
