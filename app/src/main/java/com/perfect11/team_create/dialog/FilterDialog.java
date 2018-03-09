package com.perfect11.team_create.dialog;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.perfect11.R;

public class FilterDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getWindow().getDecorView().findViewById(android.R.id.content).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.filter_dialog);
        readFromBundle();
        initView();
        attachListener();
//        publishList(adListDtoArrayList);
    }

    private void initView() {
//        lv_adList = findViewById(R.id.lv_adList);
    }

    private void readFromBundle() {
//        adListDtoArrayList = (ArrayList<AdListDto>) getIntent().getExtras().getSerializable("clickedAdListDtoArrayList");
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.ll_root:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btn_close:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    public void attachListener() {
//        lv_adList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AdListDto adListDto = adListDtoArrayList.get(position);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("adListDto", adListDto);
//                Intent intent = new Intent();
//                intent.putExtras(bundle);
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });
    }

//    public void publishList(final ArrayList<AdListDto> adListDtoArrayList) {
//        this.adListDtoArrayList = adListDtoArrayList;
//        if (adListDtoArrayList != null && adListDtoArrayList.size() != 0) {
//            ClusterDialogListAdapter clusterDialogListAdapter = new ClusterDialogListAdapter(this, adListDtoArrayList);
//            lv_adList.setAdapter(clusterDialogListAdapter);
//        }
//    }
}
