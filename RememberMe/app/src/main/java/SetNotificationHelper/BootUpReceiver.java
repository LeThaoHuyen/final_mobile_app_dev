package SetNotificationHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.rememberme.MainActivity;

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, MainActivity.class); // TODO: xem co can chinh activity ko
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
