package jp.cafe.golfbudget;

import android.content.Context;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by takashi on 2015/01/25.
 */
public class BudgetUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    public BudgetUncaughtExceptionHandler(Context context) {
        mContext = context;

        // デフォルト例外ハンドラを保持する。
        mDefaultUncaughtExceptionHandler = Thread
                .getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // スタックトレースを文字列にします。
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        String stackTrace = stringWriter.toString();

        Log.d("BudgetUncaughtException", ex.toString());
        Log.d("BudgetUncaughtException", stackTrace);

        // スタックトレースを SharedPreferences に保存します。
//        SharedPreferences preferences = mContext.getSharedPreferences(
//                MainActivity.PREF_NAME_SAMPLE, Context.MODE_PRIVATE);
//        preferences.edit().putString(MainActivity.EX_STACK_TRACE, stackTrace)
//                .commit();

        // デフォルト例外ハンドラを実行し、強制終了します。
        mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
    }
}
