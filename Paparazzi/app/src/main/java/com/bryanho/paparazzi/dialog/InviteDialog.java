package com.bryanho.paparazzi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bryanho.paparazzi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteDialog extends Dialog {

    @BindView(R.id.invite_game_room_name) TextView gameRoomName;
    private String name;

    public InviteDialog(Context context, String string) {
        super(context);
        name = string;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_invite_friend);

        ButterKnife.bind(this);
        if (name != null && name.length() != 0) {
            setGameRoomName();
        }
    }

    private void setGameRoomName() {
        gameRoomName.setText(name);
    }

    @OnClick(R.id.dismiss_dialog)
    public void dismissDialog() {
        dismiss();
    }
}
