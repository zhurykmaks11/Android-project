package com.example.laba5.data.local

import androidx.room.TypeConverter
import com.example.laba5.model.Category
import com.example.laba5.model.Type

class Converters {

    @TypeConverter
    fun fromType(type: Type): String = type.name

    @TypeConverter
    fun toType(value: String): Type = Type.valueOf(value)

    @TypeConverter
    fun fromCategory(category: Category): String = category.name

    @TypeConverter
    fun toCategory(value: String): Category = Category.valueOf(value)
}