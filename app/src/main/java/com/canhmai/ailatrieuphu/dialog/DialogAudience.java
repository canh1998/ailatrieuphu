package com.canhmai.ailatrieuphu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.canhmai.ailatrieuphu.databinding.DialogCallAudianceBinding;
import com.canhmai.ailatrieuphu.view.act.OnMainCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DialogAudience extends Dialog implements View.OnClickListener {

    private final DialogCallAudianceBinding binding;
    private final Context context;
    int textViewId;
    int truePercent;

    int percent2;
    int percent3;
    int percent4;
    List<Integer> intPercentageNumber = new ArrayList<>();
    private TextView barAnswerCase;//cot hien thi phan tram cua dap an dung
    private TextView tvTruePercent; // textview hien thi so phan tram cua dap an dung
    private List<TextView> listCaseBar; //mang chua cac cot hien thi phan tram
    private List<TextView> listPercentageTextView; // list chua cac textview hien thi so phan tram
    private OnMainCallBack onMainCallBack;

    public DialogAudience(@NonNull Context context) {
        super(context);
        this.context = context;
        this.onMainCallBack = onMainCallBack;
        binding = DialogCallAudianceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    private void initViews() {
        listCaseBar = new ArrayList<>();

        listCaseBar.add(binding.tvBarA);
        listCaseBar.add(binding.tvBarB);
        listCaseBar.add(binding.tvBarC);
        listCaseBar.add(binding.tvBarD);


        listPercentageTextView = new ArrayList<>();

        listPercentageTextView.add(binding.tvPercentA);
        listPercentageTextView.add(binding.tvPercentB);
        listPercentageTextView.add(binding.tvPercentC);
        listPercentageTextView.add(binding.tvPercentD);

    }

    public void votting() {
        binding.btClose.setOnClickListener(this);


        Collections.shuffle(intPercentageNumber);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //cot phan tram dap cua  an dung hien phan tram cao nhat
                barAnswerCase.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, truePercent * 7));
                tvTruePercent.setText(String.format("%d %%", truePercent));

                int i = listCaseBar.size();
                while (i >= 0) {
                    for (int j = 0; j <= listCaseBar.size() - 1; j++) {
                        listCaseBar.get(j).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, intPercentageNumber.get(j) * 7));
                        listPercentageTextView.get(j).setText(String.format("%d %%", intPercentageNumber.get(j)));
                    }
                    i--;
                }

                binding.btClose.setVisibility(View.VISIBLE);
            }
        };
        handler.post(runnable);
        binding.btClose.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        dismiss();
    }

    //an dap an neu da su dung tro giup 5050
    public void hideView(int i, int j) {
        List<TextView> caseBarToRemove = new ArrayList<>();
        List<TextView> percentNumToRemove = new ArrayList<>();

        if (i == 1 || j == 1) {
            binding.lnA.setVisibility(View.INVISIBLE);

            caseBarToRemove.add(listCaseBar.get(0));
            percentNumToRemove.add(listPercentageTextView.get(0));

        }
        if (i == 2 || j == 2) {
            binding.lnB.setVisibility(View.INVISIBLE);

            caseBarToRemove.add(listCaseBar.get(1));
            percentNumToRemove.add(listPercentageTextView.get(1));
        }
        if (i == 3 || j == 3) {
            binding.lnC.setVisibility(View.INVISIBLE);

            caseBarToRemove.add(listCaseBar.get(2));
            percentNumToRemove.add(listPercentageTextView.get(2));
        }
        if (i == 4 || j == 4) {
            binding.lnD.setVisibility(View.INVISIBLE);

            caseBarToRemove.add(listCaseBar.get(3));
            percentNumToRemove.add(listPercentageTextView.get(3));
        }

        listCaseBar.removeAll(caseBarToRemove);
        listPercentageTextView.removeAll(percentNumToRemove);
    }


    //kiem tra dap an dung, neu dap an nao dung thi cot cau dap an ay se dat ti le phan tram cao nhat
    public void setTrueCase(String trueCase) {
        if (trueCase.equals("A")) {
            barAnswerCase = binding.tvBarA;
            tvTruePercent = binding.tvPercentA;
        } else if (trueCase.equals("B")) {
            barAnswerCase = binding.tvBarB;
            tvTruePercent = binding.tvPercentB;
        } else if (trueCase.equals("C")) {
            barAnswerCase = binding.tvBarC;
            tvTruePercent = binding.tvPercentC;
        } else if (trueCase.equals("D")) {
            barAnswerCase = binding.tvBarD;
            tvTruePercent = binding.tvPercentD;
        }
        //xoa cot phan tram cua dap an dung ra khoi list cac cot
        listCaseBar.remove(barAnswerCase);
        //xoa textview hien thi so phan tram cua dap an dung ra khoi list cac cot
        listPercentageTextView.remove(tvTruePercent);
    }

    public void prepareToVote() {

        //trung gian
        int caseTemp1 = 0;
        int caseTemp2 = 0;
        int caseTemp3 = 0;


        Random random = new Random();
        if (listCaseBar.size() >= 3) {

            truePercent = random.nextInt(14) + 57;  // % cua dap an dung   //vd: 60%


            int temp1 = 100 - truePercent;   //=40%

            if (temp1 > 0) {
                caseTemp1 = random.nextInt((temp1 / 2) + 1) + (temp1 / 2);    // lay duoc phan tram cho tv 1 //==30%

                int temp2 = temp1 - caseTemp1; // 10%

                if (temp2 > 0) {
                    caseTemp2 = random.nextInt((temp2 / 2) + 1) + (temp2 / 2);  //lay duoc phan tram tv2 //=6%
                    int temp3 = temp2 - caseTemp2;
                    if (temp3 > 0) {
                        caseTemp3 = temp3;  //phan tram cua tv 3
                    }
                }
            }

            List<Integer> listCaseTemp = new ArrayList<>();

            listCaseTemp.add(caseTemp1);
            listCaseTemp.add(caseTemp2);
            listCaseTemp.add(caseTemp3);

            Collections.shuffle(listCaseTemp);

            percent2 = listCaseTemp.get(0);
            percent3 = listCaseTemp.get(1);
            percent4 = listCaseTemp.get(2);

            intPercentageNumber.add(this.percent2);
            intPercentageNumber.add(this.percent3);
            intPercentageNumber.add(this.percent4);


        } else {
            truePercent = random.nextInt(31) + 70;  // % cua dap an dung   //vd: 60%
            percent2 = 100 - truePercent;    //=20  ==30

            intPercentageNumber.add(this.percent2);

        }
    }
}
