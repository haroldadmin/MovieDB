package com.kshitijchauhan.haroldadmin.moviedb.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.kshitijchauhan.haroldadmin.moviedb.R
import com.kshitijchauhan.haroldadmin.moviedb.ui.main.MainViewModel
import com.kshitijchauhan.haroldadmin.moviedb.utils.SharedPreferencesDelegate
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {

    private val mainViewModel: MainViewModel by sharedViewModel()

    private var countryName by SharedPreferencesDelegate(
        get<SharedPreferences>(),
        com.kshitijchauhan.haroldadmin.moviedb.core.Constants.KEY_COUNTRY_NAME,
        Locale.getDefault().displayCountry
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel.updateToolbarTitle("Settings")
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_root, rootKey)
        findPreference(getText(R.string.key_country))
            .setOnPreferenceChangeListener { preference, newValue ->
                when (preference.key) {
                    com.kshitijchauhan.haroldadmin.moviedb.core.Constants.KEY_COUNTRY_CODE -> {
                        val listPreference = preference as ListPreference
                        val index = listPreference.findIndexOfValue(newValue as String)
                        countryName = listPreference.entries[index].toString()
                    }
                }
                true
            }
    }
}
