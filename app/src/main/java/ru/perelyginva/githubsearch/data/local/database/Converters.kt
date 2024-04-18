package ru.perelyginva.githubsearch.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.perelyginva.githubsearch.data.model.Owner

class Converters {

    @TypeConverter
    fun fromOwner(owner: Owner): String {
        return Gson().toJson(owner)
    }

    @TypeConverter
    fun toOwner(ownerString: String): Owner {
        return Gson().fromJson(ownerString, Owner::class.java)
    }

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }
}