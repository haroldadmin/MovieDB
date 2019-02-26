package com.kshitijchauhan.haroldadmin.moviedb.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.kshitijchauhan.haroldadmin.moviedb.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_root, rootKey)
    }
}
