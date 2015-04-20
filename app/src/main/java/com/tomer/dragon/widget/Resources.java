package com.tomer.dragon.widget;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by tomer on 4/18/2015.
 */
public class Resources {
    public static final int[][] pics = {
            {R.drawable.goku,R.drawable.goku_kaikan,
                    R.drawable.goku_ssj,
                    R.drawable.goku_ssj2,
                    R.drawable.goku_ssj3,
                    R.drawable.goku_ssjg},
            {
                    R.drawable.vegeta,
                    R.drawable.vegeta_ssj,
                    R.drawable.vegeta_super,
                    R.drawable.vegeta_majin,
                    R.drawable.vegeta_ssj2},
            {
                    R.drawable.kid_gohan,
                    R.drawable.kid_gohan_long,
                    R.drawable.kid_gohan_namek,
                    R.drawable.kid_gohan_ssj
            },
            {
                    R.drawable.teen_gohan,
                    R.drawable.teen_gohan_ssj,
                    R.drawable.teen_gohan_ssj2
            },
            {
                    R.drawable.adult_gohan,
                    R.drawable.adult_gohan_ssj,
                    R.drawable.adult_gohan_ssj2,
                    R.drawable.adult_gohan_mystic
            },
            {
                    R.drawable.trunks,
                    R.drawable.trunks_ssj,
                    R.drawable.trunks_super
            },
            {
                    R.drawable.goten,
                    R.drawable.goten_ssj,
                    R.drawable.gotenks,
                    R.drawable.gotenks_ssj,
                    R.drawable.gotenks_ssj3
            },
            {
                    R.drawable.trunks_kid,
                    R.drawable.trunks_kid_ssj,
                    R.drawable.gotenks,
                    R.drawable.gotenks_ssj,
                    R.drawable.gotenks_ssj3
            },
            {
                    R.drawable.roshi,
                    R.drawable.roshi_perfect
            },
            {
                    R.drawable.broly,
                    R.drawable.broly_ssj,
                    R.drawable.broly_ssj2
            },
            {
                    R.drawable.cell,
                    R.drawable.cell_second,
                    R.drawable.cell_perfect,
                    R.drawable.cell_super_perfect
            },
            {
                    R.drawable.frieza,
                    R.drawable.frieza_second,
                    R.drawable.frieza_third,
                    R.drawable.frieza_perfect,
                    R.drawable.frieza_full_power,
                    R.drawable.frieza_gold
            },
            {
                    R.drawable.buu_fat,
                    R.drawable.buu_evil,
                    R.drawable.buu_super,
                    R.drawable.buu_gotenks,
                    R.drawable.buu_gohan,
                    R.drawable.buu_kid
            }
    };
    public static float getBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert batteryIntent != null;
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float)level / (float)scale) * 100.0f;
    }
}
