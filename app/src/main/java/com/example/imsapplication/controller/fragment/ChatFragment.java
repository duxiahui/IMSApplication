package com.example.imsapplication.controller.fragment;

import android.content.Intent;

import com.example.imsapplication.controller.activity.ChatActivity;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;

import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

/**
 * Created by 吕文杰 on 2018/4/12.
 */

public class ChatFragment extends EaseConversationListFragment {
    @Override
    protected void initView() {
        super.initView();
        //点击列表跳转到会话详情页
        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId());
                //是否是群聊
                if (conversation.getType()==EMConversation.EMConversationType.GroupChat){
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
                }

                startActivity(intent);
            }
        });

        //清空集合数据
        conversationList.clear();;
        //监听消息的事件
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }


    private EMMessageListener emMessageListener=new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
                //设置数据
            EaseUI.getInstance().getNotifier().notify(list);
            //刷新页面
            refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }



        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };
}
