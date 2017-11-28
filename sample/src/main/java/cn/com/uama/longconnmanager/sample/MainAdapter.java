package cn.com.uama.longconnmanager.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.uama.longconnmanager.R;
import cn.com.uama.longconnmanager.WSMessageType;

/**
 * Created by liwei on 2017/11/28 21:34
 * Email: liwei@uama.com.cn
 * Description:
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private List<MessageBean> data;

    public MainAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == WSMessageType.SERVER) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.server_message_layout, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_message_layout, parent, false);
        }
        return new MainViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        MessageBean messageBean = data.get(position);
        String text = messageBean.getText();
        holder.textView.setText(text);
    }

    public void addClientMessage(String text) {
        MessageBean messageBean = new MessageBean();
        messageBean.setType(WSMessageType.CLIENT);
        messageBean.setText("客户端发送消息：" + text);
        addNewMessage(messageBean);
    }

    public void addServerMessage(String text) {
        MessageBean messageBean = new MessageBean();
        messageBean.setType(WSMessageType.SERVER);
        messageBean.setText("服务端返回消息：" + text);
        addNewMessage(messageBean);
    }

    public void addSystemMessage(String text) {
        MessageBean messageBean = new MessageBean();
        messageBean.setType(0);
        messageBean.setText("系统消息：" + text);
        addNewMessage(messageBean);
    }

    public void addNewMessage(MessageBean messageBean) {
        data.add(messageBean);
        notifyItemInserted(data.size() - 1);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MainViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
