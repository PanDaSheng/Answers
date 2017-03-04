package com.example.saint.answers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueBtn;
    private Button mFalseBtn;
    private Button mNextBtn;
    private Button mCheatBtn;
    private TextView mQuestionTextView;
    //问题数组对象
    private Question[] mQuestionsBank = new Question[]{
            new Question(R.string.question_plus01, true),
            new Question(R.string.question_plus02, false),
            new Question(R.string.question_plus03, true),
            new Question(R.string.question_plus04, false),

    };
    //取数组对象位置用
    private int mCurrentIndex = 0;
    //是否看答案作弊
    private boolean mIsCheater;

    //更新问题代码有重复性，最后优化封装成私有方法
    private void updateQuestion() {
        int question = mQuestionsBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    //判断对错逻辑
    private void checkAnswer(boolean userPressedTrue) {
        //Question类里 布尔值的get方法，把对象的布尔值取出来对比
        boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
        int messageResId;
        //先判断有没有作弊，若没有再判断对不对。
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.y_toast;
            } else {
                messageResId = R.string.n_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //若不为空取出键值相对应的数，不会因为转屏失去数据
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        //问题文字取出显示
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        //对错按钮事件触发
        mTrueBtn = (Button) findViewById(R.id.button01);
        mFalseBtn = (Button) findViewById(R.id.button02);
        mTrueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        //下一题按钮触发
        mNextBtn = (Button) findViewById(R.id.next_button);
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //一个数取余一个比它大的数为它本身，例如：2取余5为2
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                mIsCheater = false;
                updateQuestion();
            }

        });
        //看答案按钮触发
        mCheatBtn = (Button) findViewById(R.id.cheat_button);
        mCheatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();
                //自己封装的方法newIntent()，后面那个是EXTRA附带的信息
                Intent i = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });
        updateQuestion();
    }

    //覆盖onActivityResult获取保存在CheatActivity的回传值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    //把到那个问题的 数组数存入savedInstanceState，以免翻转屏幕后回到初始值。
    //Bundle类就是 键值对 的一种结构。
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

}
