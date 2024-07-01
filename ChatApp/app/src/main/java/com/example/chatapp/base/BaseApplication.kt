package com.example.chatapp.base

import android.app.Application
import androidx.room.Room
import com.example.chatapp.db.contacts.ContactsDatabase
import com.example.chatapp.db.message.MessageDatabase
import com.example.chatapp.db.user.UserDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(appModule)
        }
    }
}

val appModule = module {
    single {
        Room.databaseBuilder(get(), UserDatabase::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        Room.databaseBuilder(get(), ContactsDatabase::class.java, "contacts_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        Room.databaseBuilder(get(), MessageDatabase::class.java, "message_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<UserDatabase>().userDao }
    single { get<ContactsDatabase>().contactsDao }
    single { get<MessageDatabase>().messageDao }

}
