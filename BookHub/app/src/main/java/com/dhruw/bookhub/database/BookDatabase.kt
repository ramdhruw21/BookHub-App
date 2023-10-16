package com.dhruw.bookhub.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [BookEntity ::class], version = 1)
abstract class BookDatabase : RoomDatabase(){


     /**  Abstraction is process of hiding the implementation details
      * and showing only the functionality to the user
      *
      * Abstract  Methods of an abstract class will not
      * have any default implementation.
      *
      * abstract method is a method, which uses the keyword 'abstract'
      * in its declaration
      *
      *So, an abstract class can have both abstract and non-abstract methods.
      *
      * The abstract methods will never have a default implementation, while
      * the non-abstract methods will always have a default implementation.
      * */


   abstract fun bookDao() : BookDao

}