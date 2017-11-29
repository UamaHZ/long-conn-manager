package cn.com.uama.longconnmanager.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import junit.framework.Test;

import java.util.Map;

import cn.com.uama.longconnmanager.LMLongConnManager;
import cn.com.uama.longconnmanager.R;
import cn.com.uama.longconnmanager.WSConnection;
import cn.com.uama.longconnmanager.WSMessage;
import cn.com.uama.longconnmanager.WSMessageCode;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private RecyclerView recyclerView;

    private MainAdapter adapter;

    private WSConnection wsConnection;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.scrollToPosition(adapter.getItemCount());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter();
        recyclerView.setAdapter(adapter);

        wsConnection = LMLongConnManager.newConnection(url,
                "",
                new WSConnection.SimpleWSListener() {
                    @Override
                    public void onLoginSuccess() {
                        adapter.addSystemMessage("登录成功");
                    }

                    @Override
                    public void onLoginFailure() {
                        adapter.addSystemMessage("登录失败");
                    }

                    @Override
                    public void onMessage(WSConnection connection, WSMessage<String> message) {

                        WSMessageCode messageCode = WSMessageCode.parse(message.getCode());
                        if (messageCode != null) {
                            // 获取业务类型
                            int businessType = messageCode.getBusinessType();
                            // 获取业务码值
                            int businessCode = messageCode.getBusinessCode();
                            // 获取消息类型
                            int messageType = messageCode.getMessageType();
                        }

                        TestBody body = connection.parseBody(message.getBody(), TestBody.class);
                        if (body != null) {
                            adapter.addServerMessage(body.test);
                        }
                    }

                    @Override
                    public void onClosed() {
                        adapter.addSystemMessage("连接正常关闭");
                    }

                    @Override
                    public void onConnectionFailure() {
                        adapter.addSystemMessage("5次尝试之后，连接失败");
                    }

                    @Override
                    public void onDisconnected() {
                        adapter.addSystemMessage("连接异常断开");
                    }
                });
    }

    public void send(View view) {
        String text = editText.getText().toString();
        if (TextUtils.isEmpty(text)) return;

        TestBody body = new TestBody();
        body.test = text;

        WSMessage<TestBody> message = new WSMessage<>();
        message.setCode("1100000001");
        message.setBody(body);

        if (wsConnection.send(message)) {
            adapter.addClientMessage(text);
            editText.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LMLongConnManager.releaseConnection(wsConnection);
    }

    public void closeConnection(View view) {
        wsConnection.close();
    }

    public void connect(View view) {
        wsConnection.connect();
    }
}
