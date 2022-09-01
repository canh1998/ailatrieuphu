package com.canhmai.ailatrieuphu.view.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.canhmai.ailatrieuphu.App;
import com.canhmai.ailatrieuphu.MediaManager;
import com.canhmai.ailatrieuphu.R;
import com.canhmai.ailatrieuphu.databinding.M001SettingFragmentBinding;
import com.canhmai.ailatrieuphu.viewmodel.CommonVM;

public class M001SettingFragment extends BaseFragment<CommonVM, M001SettingFragmentBinding> {
    public static final String TAG = M001SettingFragment.class.getName();


    @Override
    protected void initViews() {

        setUI("ivMusic", App.getInstance().getMediaManager().getBGOn());
        setUI("ivSound", App.getInstance().getMediaManager().getGameOn());

        setClick();


    }

    private void setUI(String key, boolean state) {
        if (key.equals("ivMusic")) {

            if (state) {
                binding.ivMusic.setImageResource(R.drawable.ic_on_button_on);


            } else {
                binding.ivMusic.setImageResource(R.drawable.ic_off_button_off);


            }

        } else {
            if (state) {
                binding.ivSound.setImageResource(R.drawable.ic_on_button_on);

            } else {
                binding.ivSound.setImageResource(R.drawable.ic_off_button_off);

            }

        }
    }


    @Override
    protected void clickView(View v) {
        super.clickView(v);
        switch (v.getId()) {

            case R.id.iv_music:
                if (!App.getInstance().getMediaManager().getBGOn()) {
                    binding.ivMusic.setImageResource(R.drawable.ic_on_button_on);
                    App.getInstance().getMediaManager().setBGOn(true);
                    App.getInstance().getMediaManager().playBG(MediaManager.BG_SOUND);


                } else {
                    binding.ivMusic.setImageResource(R.drawable.ic_off_button_off);
                    App.getInstance().getMediaManager().setBGOn(false);
                    App.getInstance().getMediaManager().stopBGSound();

                }
                break;

            case R.id.iv_sound:
                if (!App.getInstance().getMediaManager().getGameOn()) {
                    binding.ivSound.setImageResource(R.drawable.ic_on_button_on);
                    App.getInstance().getMediaManager().setGameOn(true);

                } else {
                    binding.ivSound.setImageResource(R.drawable.ic_off_button_off);
                    App.getInstance().getMediaManager().setGameOn(false);
                }
                break;


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void setClick() {
        binding.ivMusic.setOnClickListener(this);
        binding.ivSound.setOnClickListener(this);

    }

    @Override
    protected Class<CommonVM> getClassViewModel() {
        return CommonVM.class;
    }

    @Override
    protected M001SettingFragmentBinding initViewBinding(LayoutInflater inflater) {
        return M001SettingFragmentBinding.inflate(inflater);
    }


}
