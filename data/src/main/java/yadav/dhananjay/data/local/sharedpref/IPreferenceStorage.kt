package yadav.dhananjay.data.local.sharedpref

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import yadav.dhananjay.domain.model.Places
import javax.inject.Inject


interface IPreferenceStorage {
    var savedLocation: Places?
}

class SharedIPreferenceStorage @Inject constructor(context: Context,
                                                   private val gson: Gson) :
        IPreferenceStorage {
    override var savedLocation: Places?
        get() {
            val savedPlaceStr: String? = prefs.getString(PREF_SAVED_PLACE, "")
            return if (TextUtils.isEmpty(savedPlaceStr))
                null
            else gson.fromJson(savedPlaceStr, Places::class.java) as Places
        }
        set(value) {
            prefs.edit().putString(PREF_SAVED_PLACE, gson.toJson(value)).apply()
        }

    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "meesho.prefs"
        const val PREF_SAVED_PLACE = "pref_saved_place"
    }
}
