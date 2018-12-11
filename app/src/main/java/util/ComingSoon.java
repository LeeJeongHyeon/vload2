package util;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class ComingSoon {
    public static View.OnClickListener comingSoonListener(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "준비 중입니다.", Toast.LENGTH_SHORT).show();
            }
        };

    }
}
