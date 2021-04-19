package com.example.starbuckskotlin.util

import android.content.Context
import androidx.preference.PreferenceManager

object PreferencesUtil {
    private val TAG = PreferencesUtil::class.java.name

    /**
     * <pre> DefaultSharedPreferences key(String)에  value(String)을 저장한다</pre>
     *
     * @param ctx   Context
     * @param key   Key
     * @param value Value
     */
    @JvmStatic
    @Synchronized
    fun putString(ctx: Context, key: String, value: String?) {
        val pref = PreferenceManager.getDefaultSharedPreferences(ctx)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.commit()
    }

    /**
     * <pre> DefaultSharedPreferences key(String)에  value(Boolean)을 저장한다</pre>
     *
     * @param ctx   Context
     * @param key   Key
     * @param value Value
     */
    @JvmStatic
    @Synchronized
    fun putBoolean(ctx: Context, key: String, value: Boolean) {
        val pref = PreferenceManager.getDefaultSharedPreferences(ctx)
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    /**
     * <pre> DefaultSharedPreferences key(String)에  value(Integer)을 저장한다</pre>
     *
     * @param ctx   Context
     * @param key   Key
     * @param value Value
     */
    @JvmStatic
    @Synchronized
    fun putInteger(ctx: Context, key: String, value: Int) {
        val pref = PreferenceManager.getDefaultSharedPreferences(ctx)
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    /**
     * <pre> DefaultSharedPreferences key(String)에  value(Float)을 저장한다</pre>
     *
     * @param ctx   Context
     * @param key   Key
     * @param value Value
     */
    @JvmStatic
    @Synchronized
    fun putFloat(ctx: Context, key: String, value: Float) {
        val pref = PreferenceManager.getDefaultSharedPreferences(ctx)
        val editor = pref.edit()
        editor.putFloat(key, value)
        editor.commit()
    }

    /**
     * <pre>해당하는 key에 String값을 반환한다</pre>
     *
     * @param ctx Context
     * @param key Key
     * @return Value String
     */
    @JvmStatic
    @Synchronized
    fun getString(ctx: Context, key: String): String? {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, "")
    }

    /**
     * <pre>해당하는 key에 Booolean 값을 반환한다</pre>
     *
     * @param ctx Context
     * @param key Key
     * @return Value String
     */
    @JvmStatic
    @Synchronized
    fun getBoolean(ctx: Context, key: String): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(key, false)
    }


    /**
     * <pre>해당하는 key에 Booolean 값을 반환한다</pre>
     *
     * @param ctx          Context
     * @param key          Key
     * @param defaultValue defaultValue
     * @return Value String
     */
    @JvmStatic
    @Synchronized
    fun getBoolean(ctx: Context, key: String, defaultValue: Boolean): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(key, defaultValue)
    }


    /**
     * <pre>해당하는 key에 float 값을 반환한다</pre>
     *
     * @param ctx Context
     * @param key Key
     * @return Value String
     */
    @JvmStatic
    @Synchronized
    fun getFloat(ctx: Context, key: String): Float {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getFloat(key, 0.0f)
    }

    /**
     * <pre>해당하는 key에 Integer값을 반환한다</pre>
     *
     * @param ctx Context
     * @param key Key
     * @return Value String
     */
    @JvmStatic
    @Synchronized
    fun getInteger(ctx: Context, key: String): Int {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getInt(key, 0)
    }

    /**
     * <pre>해당하는 key에 String값을 반환한다</pre>
     *
     * @param ctx Context
     * @param key Key
     * @param def default value
     */
    @JvmStatic
    @Synchronized
    fun getString(ctx: Context, key: String, def: String?): String? {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, def)
    }

    /**
     * <pre> DefaultSharedPreferences key(String)에  value(String)을 삭제한다.</pre>
     *
     * @param ctx Context
     * @param key Key
     */
    @JvmStatic
    @Synchronized
    fun remove(ctx: Context?, key: String?) {
        val pref = PreferenceManager.getDefaultSharedPreferences(ctx)
        val editor = pref.edit()
        editor.remove(key)
        editor.commit()
    }
}