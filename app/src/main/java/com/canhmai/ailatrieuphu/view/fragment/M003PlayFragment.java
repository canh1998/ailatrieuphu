package com.canhmai.ailatrieuphu.view.fragment;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.GravityCompat;

import com.canhmai.ailatrieuphu.App;
import com.canhmai.ailatrieuphu.MediaManager;
import com.canhmai.ailatrieuphu.R;
import com.canhmai.ailatrieuphu.databinding.M003PlayFragmentBinding;
import com.canhmai.ailatrieuphu.db.entities.Question;
import com.canhmai.ailatrieuphu.dialog.DialogAudience;
import com.canhmai.ailatrieuphu.dialog.DialogCallForHelp;
import com.canhmai.ailatrieuphu.dialog.DialogHighScore;
import com.canhmai.ailatrieuphu.dialog.NoticeDialog;
import com.canhmai.ailatrieuphu.view.act.MainActivity;
import com.canhmai.ailatrieuphu.viewmodel.CommonVM;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class M003PlayFragment extends BaseFragment<CommonVM, M003PlayFragmentBinding> {
    public static final String TAG = M003PlayFragment.class.getName();
    public CountDownTimer countDownTimer;  // dem nguoc
    Question questionAtThisLv;
    Question questionChange;
    TextView hide1;  // dap an bi an
    List<TextView> hiddenList = new ArrayList<>();  // list chua dap an bi an
    private int progress;
    private View viewClicked;   // view da duoc click chua
    private int level = -1;  //  xac dinh level cau hoi
    private boolean isHidden;
    private TextView[] textViewLevel;    //moc tien thuong
    private int trueCase;    //dap an dung
    private boolean isChangeQues = false;  //xac dinh nguoi choi da doi cau hoi chua
    private long currentTime = 30000;
    // xu ly clickable cho tung button
    private boolean changeQuestionAble = true;
    private boolean askAudienceAble = true;
    private boolean callAble = true;
    private boolean dofifty_fiftyAble = true;

    public long getCurrentTime() {
        return currentTime;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    @Override
    protected void initViews() {

        new Thread() {
            @Override
            public void run() {
                App.getInstance().getStorage().listQ = App.getInstance().getDb().getQuestionDAO().getAll();
                App.getInstance().getStorage().setListQuestion(App.getInstance().getStorage().getListQ());
                Log.e(TAG, "LIST.SIZE = " + App.getInstance().getStorage().listQ.size());
            }
        }.start();
        addTextViewLevel();
        showMoneyLevel();
        doClick();

    }

    private void addTextViewLevel() {
        textViewLevel = new TextView[15];
        textViewLevel[0] = binding.include.tvLv1;
        textViewLevel[1] = binding.include.tvLv2;
        textViewLevel[2] = binding.include.tvLv3;
        textViewLevel[3] = binding.include.tvLv4;
        textViewLevel[4] = binding.include.tvLv5;
        textViewLevel[5] = binding.include.tvLv6;
        textViewLevel[6] = binding.include.tvLv7;
        textViewLevel[7] = binding.include.tvLv8;
        textViewLevel[8] = binding.include.tvLv9;
        textViewLevel[9] = binding.include.tvLv10;
        textViewLevel[10] = binding.include.tvLv11;
        textViewLevel[11] = binding.include.tvLv12;
        textViewLevel[12] = binding.include.tvLv13;
        textViewLevel[13] = binding.include.tvLv14;
        textViewLevel[14] = binding.include.tvLv15;
    }

    private void doClick() {
        binding.listQuestion.setOnClickListener(this);
        binding.ivGiveUp.setOnClickListener(this);
        binding.ivChangeQuestion.setOnClickListener(this);
        binding.iv5050.setOnClickListener(this);
        binding.ivAskAudience.setOnClickListener(this);
        binding.ivCall.setOnClickListener(this);
        binding.tvA.setOnClickListener(this);
        binding.tvB.setOnClickListener(this);
        binding.tvC.setOnClickListener(this);
        binding.tvD.setOnClickListener(this);

    }

    protected void clickView(View v) {

        switch (v.getId()) {
            case R.id.list_question:
                showListMoney();
                break;
            case R.id.iv_give_up:
                askForExitGame();
                break;
            case R.id.iv_5050:
                hideTwoAns();
                break;
            case R.id.iv_ask_audience:
                showPercentage();
                break;
            case R.id.iv_call:
                doCalling();
                break;
            case R.id.iv_change_question:
                changeQuestion();
                break;

            case R.id.tv_A:
                checkAns(binding.tvA, MediaManager.CHOOSE_A, MediaManager.TRUE_A, 1);
                break;
            case R.id.tv_B:
                checkAns(binding.tvB, MediaManager.CHOOSE_B, MediaManager.TRUE_B, 2);
                break;
            case R.id.tv_C:
                checkAns(binding.tvC, MediaManager.CHOOSE_C, MediaManager.TRUE_C, 3);
                break;
            case R.id.tv_D:
                checkAns(binding.tvD, MediaManager.CHOOSE_D, MediaManager.TRUE_D, 4);
                break;
        }
    }

    //trả về id cua question hiẹn tai
    private int getCurrentID() {
        Question curQuestion = App.getInstance().getStorage().listQ.get(level);

        int id = curQuestion.getId();

        return id;
    }


    //show moc tien hien tai
    private void showMoneyLevel() {
        level++;
        //Hien list tien
        binding.drDrawerLayout.openDrawer(GravityCompat.START);
        // thay doi background cua moc tien hien tai
        textViewLevel[level].setBackgroundResource(R.drawable.bg_player_image_money_curent);
        //am thanh
        loadQuestionSound();
        //thay doi background moc tien truoc ve mac dinh neu vuot qua cau hoi
        if (level - 1 >= 0) {
            textViewLevel[level - 1].setBackground(null);
        }
    }

    //kiem tra dap an
    private void checkAns(TextView v, int[] chooseAns, int[] trueAns, int idAns) {

        setClickableButton(false);
        countDownTimer.cancel();
        App.getInstance().setAllowbacked(false);
        viewClicked = v;

        v.setBackgroundResource(R.drawable.ic_answer_background_selected);

        App.getInstance().getMediaManager().playGameSound(chooseAns, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                App.getInstance().getMediaManager().playGameSound(MediaManager.ANS_NOW, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //neu dung
                        if (idAns == trueCase) {
                            answerTrue(trueAns);
                            //neu sai
                        } else {
                            answerFalse();
                        }
                    }
                });

            }
        });

        isChangeQues = false;
    }

    // dap an sai
    private void answerFalse() {
        viewClicked.setBackgroundResource(R.drawable.ic_answer_background_wrong);
        //dap an dung
        getAnswerTextView(trueCase).setBackgroundResource(R.drawable.ic_answer_background_true);
        startAnimation(getAnswerTextView(trueCase));
        App.getInstance().getMediaManager().playGameSound(App.getInstance().getMediaManager().getSoundLoseCase(trueCase), new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        countDownTimer.cancel();
                        if (level == 14 || level == 4 || level == 9) {
                            level--;
                        }
                        saveScore();
                    }
                }, 1000);
            }
        });
    }

    //dap an dung
    private void answerTrue(int[] trueAns) {

        getAnswerTextView(trueCase).setBackgroundResource(R.drawable.ic_answer_background_true);
        startAnimation(getAnswerTextView(trueCase));

        App.getInstance().getMediaManager().playGameSound(trueAns, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                String money = (textViewLevel[level].getText().toString());
                binding.tvMoneyCount.setText(money);
                //tra loi dung cau 5
                if (level == 4) { // lv =5, lv = 10, lv = 15  .. textviewlevel[level-1] =5

                    doCongratulate(MediaManager.VUOT_MOC_1);

                    //tra loi dung cau 10
                } else if (level == 9) {

                    doCongratulate(MediaManager.VUOT_MOC_2);

                    //tra loi dung cau 15
                } else if (level == 14) {
                    doCongratulate(MediaManager.VUOT_MOC_3);

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showMoneyLevel();
                        }
                    }, 1000);
                }
            }
        });

    }

    //thong bao vuot moc
    private void doCongratulate(int[] vuotMocSound) {
        //neu nguoi choi vuot qua moc 3
        if (vuotMocSound.equals(MediaManager.VUOT_MOC_3)) {
            NoticeDialog noticeDialog = new NoticeDialog(mContext);
            App.getInstance().getMediaManager().playGameSound(MediaManager.CHAMPION, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    App.getInstance().getMediaManager().playGameSound(vuotMocSound, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            noticeDialog.addNotice(false, "Chúc mừng", "Bạn đã là nhà vô địch", null, "OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (v.getId() == R.id.bt_ok) {
                                        saveScore();
                                        noticeDialog.dismiss();
                                    }
                                }
                            });
                            noticeDialog.show();
                        }
                    });
                }
            });

        } else {
            //   nguoi choi vuot qua moc 1 va 2
            App.getInstance().getMediaManager().playGameSound(vuotMocSound, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    showMoneyLevel();
                }
            });
        }
    }

    //xu ly am thanh cho cau hoi
    private void loadQuestionSound() {

        App.getInstance().getMediaManager().playGameSound(App.getInstance().getMediaManager().getSoundLevel(level), new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if (level == 4 || level == 9 || level == 14) { //vd: lv =5, lv = 10, lv = 15 .... textViewLevel[level - 1]=tv_lv4
                    textViewLevel[level].setBackgroundResource(R.drawable.bg_player_image_money_curent);
                    App.getInstance().getMediaManager().playGameSound(MediaManager.IMPORTANT, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            binding.drDrawerLayout.closeDrawers();
                            startGame();
                        }
                    });

                } else {
                    //     binding.icludeList.frMain.setVisibility(View.GONE);
                    startGame();
                }

            }
        });
    }

    //bat dau tro choi
    private void startGame() {
        progress = 0;
        playGameSound();
        //cac button co the click
        setClickableButton(true);
        //dat lai thoi gian
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        setCountDown(30000);

        //dat lai backgroud mac dinh cho cac cau  tra loi
        setBackgroundAns();
        //an dp an (5050)
        isHidden = false;
        App.getInstance().setAllowbacked(true);

        int lv = level;
        questionAtThisLv = App.getInstance().getStorage().listQ.get(level);

        Log.e(TAG, "startGame: AAAAAAAAAA" + questionAtThisLv.toString());
        trueCase = questionAtThisLv.getTrueCase();
        binding.tvQuestionNumber.setText(String.format("Câu %d", lv + 1));

        binding.tvShowQuestion.setText(questionAtThisLv.getQuestion());

        binding.tvA.setText(String.format("A:  %s", questionAtThisLv.getCaseA()));
        binding.tvB.setText(String.format("B:  %s", questionAtThisLv.getCaseB()));
        binding.tvC.setText(String.format("C:  %s", questionAtThisLv.getCaseC()));
        binding.tvD.setText(String.format("D:  %s", questionAtThisLv.getCaseD()));

    }

    private void playGameSound() {
        if (level <= 4) {
            App.getInstance().getMediaManager().playBG(MediaManager.BG_MOC1);
        } else if (level <= 9) {
            App.getInstance().getMediaManager().playBG(MediaManager.BG_MOC2);
        } else if (level <= 14) {
            App.getInstance().getMediaManager().playBG(MediaManager.BG_MOC3);
        }
    }

    //background mac dinh cho 4 dap an
    private void setBackgroundAns() {
        binding.tvA.setBackgroundResource(R.drawable.ic_player_answer);
        binding.tvB.setBackgroundResource(R.drawable.ic_player_answer);
        binding.tvC.setBackgroundResource(R.drawable.ic_player_answer);
        binding.tvD.setBackgroundResource(R.drawable.ic_player_answer);
    }

    //dem nguoc
    public void setCountDown(long time) {
        binding.progressBarLoading.setProgress((int) (100 / (time / 1000)));
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvTimeCountdown.setText("" + millisUntilFinished / 1000); //30000/1000==30

                currentTime = millisUntilFinished;
                progress += 3;
                binding.progressBarLoading.setProgress(progress);
                if (millisUntilFinished / 1000 == 0) {
                    binding.progressBarLoading.setProgress(0);
                }
            }

            @Override
            public void onFinish() {
                setClickableButton(false);
                App.getInstance().getMediaManager().playGameSound(MediaManager.OUT_OF_TIME, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        NoticeDialog noticeDialog = new NoticeDialog(mContext);

                        noticeDialog.addNotice(false, "Thua rồi!!", "Bạn đã hết thời gian", null, "Đóng", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (v.getId() == R.id.bt_ok) {
                                    App.getInstance().setAllowbacked(false);
                                    noticeDialog.dismiss();
                                    if (level == 14) {
                                        level--;
                                    }
                                    saveScore();
                                }
                            }
                        });
                        noticeDialog.show();
                    }
                });
            }
        }.start();
    }


    //hien list tien
    private void showListMoney() {
        binding.drDrawerLayout.openDrawer(GravityCompat.START);
        textViewLevel[level].setBackgroundResource(R.drawable.bg_player_image_money_curent);
    }

    //trang thai click
    private void setClickableButton(boolean clickable) {

        binding.tvA.setClickable(clickable);
        binding.tvB.setClickable(clickable);
        binding.tvC.setClickable(clickable);
        binding.tvD.setClickable(clickable);

        binding.ivGiveUp.setClickable(clickable);
        binding.tvQuestionNumber.setClickable(clickable);

        if (!changeQuestionAble) {
            binding.ivChangeQuestion.setClickable(false);
        } else {
            binding.ivChangeQuestion.setClickable(clickable);
        }

        if (!dofifty_fiftyAble) {
            binding.iv5050.setClickable(false);
        } else {
            binding.iv5050.setClickable(clickable);
        }

        if (!askAudienceAble) {
            binding.ivAskAudience.setClickable(false);
        } else {
            binding.ivAskAudience.setClickable(clickable);
        }

        if (!callAble) {
            binding.ivCall.setClickable(false);
        } else {
            binding.ivCall.setClickable(clickable);
        }


    }

    //tra ve id truecase cho tung cau hoi
    private int getIDTrueCase() {
        return App.getInstance().getStorage().listQ.get(level).getTrueCase();
    }

    //tra ve dap an dung cho tung cau hoi
    private TextView getAnswerTextView(int id) {
        //tra ve view ung voi truecase cua tung cau hoi

        TextView kq = null;
        if (id == 1) {
            kq = binding.tvA;
        } else if (id == 2) {
            kq = binding.tvB;
        }
        if (id == 3) {
            kq = binding.tvC;
        }
        if (id == 4) {
            kq = binding.tvD;
        }
        return kq;

    }


    //thay doi cau hoi
    private void changeQuestion() {
        changeQuestionAble = false;
        setClickableButton(true);
        setBackgroundAns();
        binding.ivChangeQuestion.setImageResource(R.drawable.player_button_image_help_change_question_active);
        countDownTimer.cancel();
        NoticeDialog noticeDialog = new NoticeDialog(mContext);
        noticeDialog.addNotice(false, "Nhắc nhở", "Bạn muốn đổi câu hỏi?", "Không", "Có", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.bt_ok) {

                    synchronized (this) {
                        new Thread() {
                            @Override
                            public void run() {
                                questionChange = App.getInstance().getDb().getQuestionDAO().getQuestionAtLevel(getCurrentLevel(), getCurrentID());
                                Log.e(TAG, "run: BBBBBBBB" + questionChange.toString());
                                App.getInstance().getStorage().replaceQuestion(questionChange, level);

                                MainActivity mainActivity = (MainActivity) mContext;
                                mainActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.ivChangeQuestion.setImageResource(R.drawable.ic__change_question_x);
                                        startGame();
                                        noticeDialog.dismiss();
                                    }
                                });
                            }

                        }.start();
                    }
                    isChangeQues = true;
                    Log.e(TAG, "onClick: LLLLLLLL");

                } else {
                    binding.ivChangeQuestion.setImageResource(R.drawable.ic_change_question);
                    changeQuestionAble = true;
                    noticeDialog.dismiss();
                    setCountDown(currentTime);
                }
            }
        });
        noticeDialog.show();
        //   setCountDown(currentTime);
    }

    //goi dien thoai cho nguoi than
    private void doCalling() {
        countDownTimer.cancel();
        callAble = false;
        binding.ivCall.setImageResource(R.drawable.player_button_image_help_call_active);
        DialogCallForHelp dialogCallForHelp = new DialogCallForHelp(mContext);
        dialogCallForHelp.setCancelable(false);
        dialogCallForHelp.setCanceledOnTouchOutside(false);
        dialogCallForHelp.show();
        Window window = dialogCallForHelp.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogCallForHelp.show();

        dialogCallForHelp.setTrueAnswer(App.getInstance().getStorage().listQ.get(level).getTrueCase());

        App.getInstance().getMediaManager().playGameSound(MediaManager.CALL_WHO, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                dialogCallForHelp.setClickable(true);
            }
        });
        dialogCallForHelp.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setCountDown(currentTime);
                binding.ivCall.setImageResource(R.drawable.ic_help_call_x);
            }
        });
    }

    //hoi khan gia trong truong quay
    private void showPercentage() {
        DialogAudience dialogAudience = new DialogAudience(mContext);

        setClickableButton(false);
        askAudienceAble = false;
        countDownTimer.cancel();
        binding.ivAskAudience.setImageResource(R.drawable.player_button_image_help_audience_active);


        List<TextView> textViewsArray = new ArrayList<>();
        textViewsArray.add(binding.tvA);
        textViewsArray.add(binding.tvB);
        textViewsArray.add(binding.tvC);
        textViewsArray.add(binding.tvD);

        //dap an dung
        TextView trueCase;

        trueCase = getAnswerTextView(getIDTrueCase());

        if (isHidden) {
            TextView tv1 = hiddenList.get(0);
            TextView tv2 = hiddenList.get(1);

            //an cot % cua dap an neu da su dung 5050
            dialogAudience.hideView(checkWhatViewisBeingHidden(tv1), checkWhatViewisBeingHidden(tv2));
        }
        if (trueCase.equals(binding.tvA)) {
            dialogAudience.setTrueCase("A");
        } else if (trueCase.equals(binding.tvB)) {
            dialogAudience.setTrueCase("B");
        } else if (trueCase.equals(binding.tvC)) {
            dialogAudience.setTrueCase("C");
        } else if (trueCase.equals(binding.tvD)) {
            dialogAudience.setTrueCase("D");
        }

        dialogAudience.prepareToVote();
        Window window = dialogAudience.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogAudience.show();
        App.getInstance().getMediaManager().playGameSound(MediaManager.KHAN_GIA, mp -> App.getInstance().getMediaManager().playGameSound(MediaManager.CHUYEN_GIA, mp1 -> {
            dialogAudience.votting();
            setClickableButton(true);
        }));
        dialogAudience.setOnDismissListener(dialog -> {
            M003PlayFragment.this.setCountDown(currentTime);
            binding.ivAskAudience.setImageResource(R.drawable.ic_help_audience_x);
        });
    }

    //kiem tra dap an nao dang bi an
    private int checkWhatViewisBeingHidden(TextView tv) {
        int kq = 0;

        if (tv.equals(binding.tvA)) {
            kq = 1;
        } else if (tv.equals(binding.tvB)) {
            kq = 2;

        } else if (tv.equals(binding.tvC)) {
            kq = 3;

        } else if (tv.equals(binding.tvD)) {
            kq = 4;
        }
        return kq;
    }

    // tro giup 5050
    private void hideTwoAns() {
        dofifty_fiftyAble = false;
        countDownTimer.cancel();
        binding.iv5050.setImageResource(R.drawable.player_button_image_help_5050_active);
        setClickableButton(false);


        App.getInstance().getMediaManager().playGameSound(MediaManager.SOUND_5050, mp -> {
            setClickableButton(true);
            int b = 0;
            int count = 0;
            int trueCaseId;
            trueCaseId = getIDTrueCase();
            while (count <= 1) {
                Random random = new Random();
                int ran = random.nextInt(4) + 1;
                if (ran != trueCaseId && ran != b) {  //id tc =3
                    b = ran;
                    hide1 = getAnswerTextView(b);
                    hiddenList.add(hide1);
                    hide1.setBackgroundResource(R.drawable.ic_answer_background_hide);
                    hide1.setText("");
                    hide1.setClickable(false);
                    count++;
                }

            }
            binding.iv5050.setImageResource(R.drawable.ic_help_5050_off);
            setCountDown(currentTime);
            isHidden = true;
        });


    }

    //thoat game
    private void askForExitGame() {
        binding.ivGiveUp.setImageResource(R.drawable.player_button_image_help_stop_active);

        NoticeDialog noticeDialog = new NoticeDialog(mContext);
        noticeDialog.addNotice(false, "Thông báo", "Bạn muốn dừng cuộc chơi?", "Hủy", "Có", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.bt_ok) {
                    noticeDialog.dismiss();

                    if (level == 14) {
                        level--;
                    }
                    saveScore();
                } else {
                    binding.ivGiveUp.setImageResource(R.drawable.ic_stop_game);
                    noticeDialog.dismiss();
                }
            }
        });
        noticeDialog.show();
    }

    // hoat anh dap an
    private void startAnimation(View v) {
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setDuration(100);
        animation.setRepeatCount(21);
        v.startAnimation(animation);

    }

    @Override
    public void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        countDownTimer.cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        setCountDown(currentTime);
    }

    public void stopCountDown() {
        countDownTimer.cancel();
    }

    //tra ve level hien tai
    private int getCurrentLevel() {
        return level + 1;
    }

    //luu diem
    private void saveScore() {
        countDownTimer.cancel();
        DialogHighScore dialogHighScore = new DialogHighScore(mContext);
        if (level > 0) {
            if (level < 4) {
                dialogHighScore.setScore("200,000");
            } else if (level < 9) {
                dialogHighScore.setScore("2,000,000");
            } else if (level < 14) {
                dialogHighScore.setScore("22,000,000");
            } else {
                dialogHighScore.setScore("150,000,000");
            }

            dialogHighScore.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    App.getInstance().getMediaManager().playGameSound(MediaManager.LOSE, new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            onMainCallBack.showFragment(M001StartFragment.TAG, null, false);
                            App.getInstance().getMediaManager().playBG(MediaManager.BG_SOUND);
                            App.getInstance().setAllowbacked(true);
                        }
                    });

                }
            });
            dialogHighScore.show();
        } else {
            App.getInstance().getMediaManager().playGameSound(MediaManager.LOSE, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onMainCallBack.showFragment(M001StartFragment.TAG, false, false);
                    App.getInstance().setAllowbacked(true);
                    App.getInstance().getMediaManager().playBG(MediaManager.BG_SOUND);
                }
            });
        }
    }

    @Override
    protected Class<CommonVM> getClassViewModel() {
        return CommonVM.class;
    }

    @Override
    protected M003PlayFragmentBinding initViewBinding(LayoutInflater inflater) {
        return M003PlayFragmentBinding.inflate(inflater);
    }
}

