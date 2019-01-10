package vmp.company.vexmall.navigation;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import util.category.LogTag;
import vmp.company.vexmall.R;

public class InviteDialog extends Dialog {
    private final String TAG = LogTag.getTag(this);
    public static final String PARAMETER_KEY = "recommeder_id";
    final String DEEP_LINK = "http://vmp.company/vexMall/back/redirect.php";
    private ImageView recommend_f_close_iv;
    private TextView recommend_f_kakao_tv;
    private TextView recommend_f_copy_url_tv;
    private SharedPreferences pref;
    private String mb_id = "";
    private Uri shortLink;


    // DeepLink : http://vmp.company/vexMall/back/redirect.php?recommender_id=추천인아이디
    private Uri getDeepLink() {
        return Uri.parse(DEEP_LINK + "?" + PARAMETER_KEY + "=" + mb_id);
    }

    // DynamicLink 생성 (DeepLink + recommender_id)
    private void createDynamicLink() {
        Log.d(TAG, "createDynamicLink() begins to run...");
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(getDeepLink())
                .setDynamicLinkDomain("vexmall.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder(getContext().getPackageName())
                                .setMinimumVersion(21)
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        Log.d("Sera's", "OnCompleteListener.onComplete() begins to run... the result : " + task.isSuccessful());

                        if (task.isSuccessful()) {

                            shortLink = task.getResult().getShortLink();

                        } else {
                            Log.d(TAG, "recommendToFriendDialog.java --- " + task.toString());
                        }
                    }
                });
    }

    public InviteDialog(Context context) {
        super(context);
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        mb_id = pref.getString("mb_id", "");
        if (!mb_id.isEmpty()) {
            createDynamicLink();
        }


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_invite);
        initUI();
        setListeners();


    }

    public void initUI() {
        recommend_f_close_iv = findViewById(R.id.recommend_f_close_iv);
        recommend_f_kakao_tv = findViewById(R.id.recommend_f_kakao_tv);
        recommend_f_copy_url_tv = findViewById(R.id.recommend_f_copy_url_tv);
    }

    public void setListeners() {
        recommend_f_close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        recommend_f_kakao_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mb_id.isEmpty()) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                        intent.setType("text/plain");
                        getContext().startActivity(Intent.createChooser(intent, "Share"));
                        Log.d("Sera's", "shortLink.toString() : " + shortLink.toString());
                    } catch (ActivityNotFoundException e) {

                    }
                } else {
                    Toast.makeText(getContext(), "로그인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recommend_f_copy_url_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mb_id.isEmpty()) {
                    ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("vexmall_url", shortLink.toString());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(getContext(), "주소를 클립보드에 복사하였습니다.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "shortLink.toString() : " + shortLink.toString());
                } else {
                    Toast.makeText(getContext(), "로그인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

}
