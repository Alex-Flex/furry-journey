package com.alexflex.soft.itipgu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.alexflex.soft.itipgu.logic.CommonMethods;

//настройки приложения
public class SettingsActivity extends AppCompatActivity {

    //редактор настроек
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private ActionBar actionBar;
    private ConstraintLayout constraintLayout;
    //переключатель уведомлений
    private Switch notifySwitcher;
    private TextView interval;
    private TextView moreOnInterval;
    //появляется, когда активен notifySwitcher;
    private LinearLayout moreParams;
    //константы для удобной записи в настройки
    private final String NOTIFICATIONS_ENABLED = "notifications_enabled";
    private final String INTERVAL_IN_MINUTES = "update_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        actionBar = getSupportActionBar();
        constraintLayout = findViewById(R.id.layout);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        interval = findViewById(R.id.interval);
        moreOnInterval = findViewById(R.id.more_on_interval);
        notifySwitcher = findViewById(R.id.notify_switcher);
        moreParams = findViewById(R.id.extended_params);
        if(sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED, false))
            notifySwitcher.setChecked(true);

        if(!(notifySwitcher.isChecked()))
            moreParams.setVisibility(View.GONE);

        //переключатель уведомлений
        notifySwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                editor.putBoolean(NOTIFICATIONS_ENABLED, isChecked);
                if(isChecked){ //когда уведы включаются переключателем
                    //подтверждаем действие
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this)
                            .setTitle(R.string.warning)
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_warning_black_24dp)
                            .setMessage(R.string.what_is_this)
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED, false))
                                        stopService(new Intent(SettingsActivity.this, NewsGetterService.class));
                                    editor.putBoolean(NOTIFICATIONS_ENABLED, false);
                                    editor.apply();
                                    moreParams.setVisibility(View.GONE);
                                    Toast.makeText(SettingsActivity.this, R.string.n_off, Toast.LENGTH_LONG).show();
                                    notifySwitcher.setChecked(false);
                                    dialogInterface.cancel();
                                }
                            })
                            .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //специфика, для разных устройств
                                    final Intent[] POWERMANAGER_INTENTS = {
                                            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
                                            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
                                            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
                                            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
                                            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
                                            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
                                            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
                                            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
                                            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
                                            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
                                            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
                                            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
                                            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
                                            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"))
                                    };

                                    for (final Intent intent : POWERMANAGER_INTENTS)
                                        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this)
                                                    .setTitle(R.string.warning)
                                                    .setCancelable(false)
                                                    .setIcon(R.drawable.ic_warning_black_24dp)
                                                    .setMessage(R.string.what_is_this)
                                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            if(sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED, false))
                                                                stopService(new Intent(SettingsActivity.this, NewsGetterService.class));
                                                            editor.putBoolean(NOTIFICATIONS_ENABLED, false);
                                                            editor.apply();
                                                            moreParams.setVisibility(View.GONE);
                                                            Toast.makeText(SettingsActivity.this, R.string.n_off, Toast.LENGTH_LONG).show();
                                                            notifySwitcher.setChecked(false);
                                                            dialogInterface.cancel();
                                                        }
                                                    })
                                                    .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            startActivity(intent);
                                                        }
                                                    });
                                            builder.create().show();
                                            break;
                                        }
                                    if(!(sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED, false))) {
                                        Toast.makeText(SettingsActivity.this, R.string.wait_please, Toast.LENGTH_LONG).show();
                                        startService(new Intent(SettingsActivity.this, NewsGetterService.class));
                                    }
                                    editor.putBoolean(NOTIFICATIONS_ENABLED, true);
                                    editor.apply();
                                    moreParams.setVisibility(View.VISIBLE);
                                    Toast.makeText(SettingsActivity.this, R.string.n_on, Toast.LENGTH_LONG).show();
                                    dialogInterface.cancel();
                                }
                            });
                    //показываем alert
                    builder.create().show();
                } else { //когда уведы выключаются переключателем
                    if(!(sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED, false)))
                        stopService(new Intent(SettingsActivity.this, NewsGetterService.class));
                    editor.putBoolean(NOTIFICATIONS_ENABLED, false).apply();

                    moreParams.setVisibility(View.GONE);
                    Toast.makeText(SettingsActivity.this, R.string.n_off, Toast.LENGTH_LONG).show();
                }
            }
        });

        //переключатель интервалов
        moreParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(SettingsActivity.this);
                final View v = layoutInflater.inflate(R.layout.interval_input, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this)
                        .setView(v)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    EditText editText = v.findViewById(R.id.editText);
                                    int e = Integer.parseInt(editText.getText().toString());
                                    if(e<1 || e>1440) {
                                        Toast.makeText(SettingsActivity.this, R.string.too_big_or_small_rest, Toast.LENGTH_LONG).show();
                                        dialogInterface.cancel();
                                    } else {
                                        editor.putInt(INTERVAL_IN_MINUTES, e);
                                        editor.apply();
                                        moreOnInterval.setText(String.format(getResources().getString(R.string.raz_v_minut), e));
                                        Toast.makeText(SettingsActivity.this, R.string.restart_service, Toast.LENGTH_LONG).show();
                                        dialogInterface.cancel();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(SettingsActivity.this, R.string.wrong_input, Toast.LENGTH_LONG).show();
                                    dialogInterface.cancel();
                                }
                            }
                        });
                dialog.create().show();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        actionBar.setTitle(R.string.settings);

        //смена цвета экшенбара в зависимости от сезона
        if(CommonMethods.isNightHere()){
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
            notifySwitcher.setTextColor(Color.WHITE);
            interval.setTextColor(Color.WHITE);
            moreOnInterval.setTextColor(Color.WHITE);
            constraintLayout.setBackgroundColor(Color.BLACK);
        } else {
            switch (CommonMethods.getQuarter()){
                case 2:
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorSpring1)));
                    break;
                case 3:
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Summer1)));
                    break;
                case 4:
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Autumn2)));
                    break;
                default:
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
            }
        }

        if(sharedPreferences.getInt(INTERVAL_IN_MINUTES, 5) != 5)
            moreOnInterval.setText(String.format(getResources().getString(R.string.raz_v_minut), sharedPreferences.getInt(INTERVAL_IN_MINUTES, 5)));
    }
}
