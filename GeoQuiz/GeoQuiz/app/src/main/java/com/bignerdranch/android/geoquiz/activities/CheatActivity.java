package com.bignerdranch.android.geoquiz.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.android.geoquiz.R;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_IS_ANSWER_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";

    private static final String KEY_ANSWER_SHOWN = "answer_shown";

    private boolean mAnswerIsTrue;
    private boolean mAnswerShown = false;

    private TextView mAnswerTextView;
    private Button mShowAnswer;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_IS_ANSWER_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN, false);
            setAnswerShownResult(true);
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_IS_ANSWER_TRUE, false);

        mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);
        mShowAnswer = (Button)findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                mAnswerShown = true;
                setAnswerShownResult(true);

                int cx = mShowAnswer.getWidth() / 2;
                int cy = mShowAnswer.getHeight() / 2;
                float radius = mShowAnswer.getWidth();
                Animator animator = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    animator = ViewAnimationUtils.createCircularReveal(mShowAnswer, cx, cy, radius, 0);

                    if (animator != null) {
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                mShowAnswer.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        animator.start();
                    }
                } else {
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ANSWER_SHOWN, mAnswerShown);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
