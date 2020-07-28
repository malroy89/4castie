package de.lamaka.fourcastie.data

import android.content.SharedPreferences

class FakeSharedPreferences : SharedPreferences {

    private val storage: MutableMap<String, Any> = mutableMapOf()

    override fun contains(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getBoolean(p0: String?, p1: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun unregisterOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

    override fun getInt(p0: String?, p1: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getAll(): MutableMap<String, *> {
        TODO("Not yet implemented")
    }

    override fun edit(): SharedPreferences.Editor = Editor()

    override fun getLong(p0: String?, p1: Long): Long {
        TODO("Not yet implemented")
    }

    override fun getFloat(p0: String?, p1: Float): Float {
        TODO("Not yet implemented")
    }

    override fun getStringSet(p0: String?, p1: MutableSet<String>?): MutableSet<String> {
        TODO("Not yet implemented")
    }

    override fun registerOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

    override fun getString(p0: String?, p1: String?): String? {
        val value = storage[p0] ?: return null
        return value as String
    }

    private inner class Editor : SharedPreferences.Editor {

        private val editorStorage: MutableMap<String, Any> = mutableMapOf()

        override fun clear(): SharedPreferences.Editor {
            TODO("Not yet implemented")
        }

        override fun putLong(p0: String?, p1: Long): SharedPreferences.Editor {
            TODO("Not yet implemented")
        }

        override fun putInt(p0: String?, p1: Int): SharedPreferences.Editor {
            TODO("Not yet implemented")
        }

        override fun remove(p0: String?): SharedPreferences.Editor {
            TODO("Not yet implemented")
        }

        override fun putBoolean(p0: String?, p1: Boolean): SharedPreferences.Editor {
            TODO("Not yet implemented")
        }

        override fun putStringSet(p0: String?, p1: MutableSet<String>?): SharedPreferences.Editor {
            TODO("Not yet implemented")
        }

        override fun commit(): Boolean {
            storage.putAll(editorStorage)
            return true
        }

        override fun putFloat(p0: String?, p1: Float): SharedPreferences.Editor {
            TODO("Not yet implemented")
        }

        override fun apply() {
            storage.putAll(editorStorage)
        }

        override fun putString(p0: String, p1: String?): SharedPreferences.Editor {
            p1?.let {
                editorStorage[p0] = p1
            }
            return this
        }

    }
}