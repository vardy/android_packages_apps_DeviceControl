/*
 *  Copyright (C) 2013 - 2014 Alexander "Evisceration" Martinz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.namelessrom.devicecontrol.ui.fragments.tools;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.namelessrom.devicecontrol.MainActivity;
import org.namelessrom.devicecontrol.R;
import org.namelessrom.devicecontrol.ui.preferences.CustomPreference;
import org.namelessrom.devicecontrol.ui.views.AttachPreferenceFragment;
import org.namelessrom.devicecontrol.utils.IOUtils;
import org.namelessrom.devicecontrol.utils.Utils;
import org.namelessrom.devicecontrol.utils.constants.DeviceConstants;

public class ToolsMoreFragment extends AttachPreferenceFragment implements DeviceConstants {
    private CustomPreference mMediaScan;

    private CustomPreference mBuildProp;
    private CustomPreference mSysctlVm;
    private CustomPreference mAppManager;
    private CustomPreference mWirelessFileManager;

    @Override protected int getFragmentId() { return ID_TOOLS_MORE; }

    @Override public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tools_more);

        mMediaScan = (CustomPreference) findPreference("media_scan");

        mAppManager = (CustomPreference) findPreference("app_manager");
        mWirelessFileManager = (CustomPreference) findPreference("wireless_file_manager");

        mBuildProp = (CustomPreference) findPreference("build_prop");
        mSysctlVm = (CustomPreference) findPreference("sysctl_vm");
    }

    @Override public boolean onPreferenceTreeClick(final PreferenceScreen preferenceScreen,
            @NonNull final Preference preference) {
        final String key = preference.getKey();

        if (key == null || key.isEmpty()) return false;

        if (mMediaScan == preference) {
            startMediaScan();
        } else if (mAppManager == preference) {
            MainActivity.loadFragment(getActivity(), ID_TOOLS_APP_MANAGER);
        } else if (mWirelessFileManager == preference) {
            MainActivity.loadFragment(getActivity(), ID_TOOLS_WIRELESS_FM);
        } else if (mBuildProp == preference) {
            MainActivity.loadFragment(getActivity(), ID_TOOLS_BUILD_PROP);
        } else if (mSysctlVm == preference) {
            MainActivity.loadFragment(getActivity(), ID_TOOLS_VM);
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void startMediaScan() {
        mMediaScan.setSummary(R.string.media_scan_triggered);
        final String format = "am broadcast -a android.intent.action.MEDIA_MOUNTED -d file://%s";
        Utils.runRootCommand(String.format(format, IOUtils.get().getPrimarySdCard()));
        if (!TextUtils.isEmpty(IOUtils.get().getSecondarySdCard())) {
            Utils.runRootCommand(String.format(format, IOUtils.get().getSecondarySdCard()));
        }
    }

}
