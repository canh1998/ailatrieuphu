package com.canhmai.ailatrieuphu.view.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.canhmai.ailatrieuphu.App;
import com.canhmai.ailatrieuphu.R;
import com.canhmai.ailatrieuphu.databinding.M001ArchivementFragmentBinding;
import com.canhmai.ailatrieuphu.db.entities.HighScore;
import com.canhmai.ailatrieuphu.view.act.MainActivity;
import com.canhmai.ailatrieuphu.viewmodel.CommonVM;

import java.util.Collections;
import java.util.Comparator;

public class M001AchivementFragment extends BaseFragment<CommonVM, M001ArchivementFragmentBinding> {
    public static final String TAG = M001AchivementFragment.class.getName();

    @Override
    protected void initViews() {


        new Thread() {
            @Override
            public void run() {
                App.getInstance().getStorage().listHighScore = App.getInstance().getDb().getHighScoreDAO().getAll();
                Log.e(TAG, "run: AAAAAAAAAAAAAAA" + App.getInstance().getStorage().listHighScore.size());

                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.runOnUiThread(() -> upDateUI());
            }
        }.start();


    }

    private void upDateUI() {
        if (App.getInstance().getStorage().listHighScore.size() > 1) {
            Collections.sort(App.getInstance().getStorage().listHighScore, new Comparator<HighScore>() {
                @Override
                public int compare(HighScore o1, HighScore o2) {
                    return o2.scoreUser - o1.scoreUser;
                }
            });
        }
        String text1 = "";
        String text2 = "";
        String text3 = "";
        HighScore hc2 = null;
        HighScore hc3 = null;
        int mone1;
        int mone2 = 0;
        int mone3 = 0;
        if (!App.getInstance().getStorage().listHighScore.isEmpty()) {
            Log.e(TAG, "upDateUI: LISTSIZE====" + App.getInstance().getStorage().listHighScore.size());

            HighScore hc1 = App.getInstance().getStorage().listHighScore.get(0);
            Log.e(TAG, "upDateUI: NAME==" + hc1.getName());
            mone1 = hc1.getScoreUser();

            changeMoneyFromIntToString(mone1, text1, hc1, binding.tvRank1Name, binding.tvRank1Money);


            if (App.getInstance().getStorage().listHighScore.size() >= 2) {
                hc2 = App.getInstance().getStorage().listHighScore.get(1);
                mone2 = hc2.getScoreUser();

                changeMoneyFromIntToString(mone2, text2, hc2, binding.tvRank2Name, binding.tvRank2Money);
            }

            if (App.getInstance().getStorage().listHighScore.size() > 2) {
                hc3 = App.getInstance().getStorage().listHighScore.get(2);
                mone3 = hc3.getScoreUser();

                changeMoneyFromIntToString(mone3, text3, hc3, binding.tvRank3Name, binding.tvRank3Money);
            }
        }
    }

    private void changeMoneyFromIntToString(int mone, String text, HighScore hc, TextView tvName, TextView tvMoney) {
        if (mone == 150000000) {
            text = "150,000,000";
        } else if (mone == 22000000) {
            text = "22,000,000";
        } else if (mone == 20000000) {
            text = "20,000,000";
        } else if (mone == 2000000) {
            text = "2,000,000";
        } else if (mone == 200000) {
            text = "200,000";
        } else {
            text = hc.getScoreUser() + "";
        }
        tvName.setText(hc.getName());
        tvMoney.setText(text);
    }


    @Override
    protected Class<CommonVM> getClassViewModel() {
        return CommonVM.class;
    }

    @Override
    protected M001ArchivementFragmentBinding initViewBinding(LayoutInflater inflater) {
        return M001ArchivementFragmentBinding.inflate(inflater);
    }


}
