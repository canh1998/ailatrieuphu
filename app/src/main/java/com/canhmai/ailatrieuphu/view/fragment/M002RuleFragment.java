package com.canhmai.ailatrieuphu.view.fragment;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.canhmai.ailatrieuphu.App;
import com.canhmai.ailatrieuphu.MediaManager;
import com.canhmai.ailatrieuphu.R;
import com.canhmai.ailatrieuphu.databinding.M002RuleFragmentBinding;
import com.canhmai.ailatrieuphu.dialog.NoticeDialog;
import com.canhmai.ailatrieuphu.view.act.MainActivity;
import com.canhmai.ailatrieuphu.viewmodel.CommonVM;

public class M002RuleFragment extends BaseFragment<CommonVM, M002RuleFragmentBinding> {
    public static final String TAG = M002RuleFragment.class.getName();

    @Override
    protected void initViews() {

        binding.btHide.setVisibility(View.VISIBLE);
        binding.btHide.setClickable(true);
        binding.frMain.setVisibility(View.VISIBLE);
        binding.frMain.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_left));
        App.getInstance().getMediaManager().playGameSound(MediaManager.GAME_RULE,
                mp -> App.getInstance().getMediaManager().playGameSound(MediaManager.READY, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        showReadyDialog();
                    }
                }));
        setAnimateUI();
//
        binding.btHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btHide.setAnimation(AnimationUtils.loadAnimation(mContext, androidx.appcompat.R.anim.abc_popup_enter));
                binding.btHide.setClickable(false);

                App.getInstance().getMediaManager().playGameSound(MediaManager.GO_FIND, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        onMainCallBack.showFragment(M003PlayFragment.TAG, null, false);
                        binding.btHide.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void showReadyDialog() {
        NoticeDialog noticeDialog = new NoticeDialog(mContext);
        noticeDialog.addNotice(false, null, "Bạn đã sẵn sàng chơi chưa", "Quay lại", "Sẵn sàng", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.bt_ok) {
                    doReady();
                    noticeDialog.dismiss();
                } else {
                    doBack();
                    noticeDialog.dismiss();
                }
            }
        });
        noticeDialog.show();
    }

    private void doReady() {
        binding.btHide.setClickable(false);
        App.getInstance().getMediaManager().stopGameSound();
        App.getInstance().getMediaManager().playGameSound(MediaManager.GO_FIND, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                onMainCallBack.showFragment(M003PlayFragment.TAG, null, false);
                Log.e(TAG, "onCompletion: M002RULE");
            }
        });
    }

    private void doBack() {
        App.getInstance().getMediaManager().stopGameSound();
        MainActivity mainActivity = (MainActivity) mContext;
        mainActivity.onBackPressed();
    }

    private void setAnimateUI() {
        binding.lnRuleMain.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.abc_fade_in_custom));
    }

    @Override
    protected Class<CommonVM> getClassViewModel() {
        return CommonVM.class;
    }

    @Override
    protected M002RuleFragmentBinding initViewBinding(LayoutInflater inflater) {
        return M002RuleFragmentBinding.inflate(inflater);
    }

}
