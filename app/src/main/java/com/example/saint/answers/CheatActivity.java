package com.example.saint.answers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    //使用包名作常量避免不同应用之间发生命名冲突
    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.saint.answers.answer_is_true";
    private static final String EXTRA__ANSWER_SHOWN = "com.example.saint.answers.answer_show";
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswer;

    //封装一个CheatActivity处理extra的方法
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    // 打开cheat页面并且看过答案
    public static boolean wasAnswerShown(Intent i) {
        return i.getBooleanExtra(EXTRA__ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        //从extra获取数据，第二个参数是制定默认答案，在无法获得键值时使用
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.btn01);
                } else {
                    mAnswerTextView.setText(R.string.btn02);
                }
                setAnswerShowResult(true);
            }
        });

    }

    //把isAnswerShown放入extra中，返回给MainActivity，看答案后按下正确答案显示的消息就是：你已经看过答案
    private void setAnswerShowResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA__ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}



